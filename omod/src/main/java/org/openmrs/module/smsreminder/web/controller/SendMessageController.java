package org.openmrs.module.smsreminder.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.LocationService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.smsreminder.SmsReminderUtils;
import org.openmrs.module.smsreminder.api.SmsReminderService;
import org.openmrs.module.smsreminder.modelo.NotificationPatient;
import org.openmrs.module.smsreminder.modelo.Sent;
import org.openmrs.module.smsreminder.utils.DatasUtil;
import org.openmrs.module.smsreminder.utils.GatewayStatusNotification;
import org.openmrs.module.smsreminder.utils.InboundNotification;
import org.openmrs.module.smsreminder.utils.OutboundNotification;
import org.openmrs.module.smsreminder.utils.SMSClient;
import org.openmrs.module.smsreminder.utils.SmsReminderHandler;
import org.openmrs.module.smsreminder.utils.SmsReminderResource;
import org.openmrs.module.smsreminder.utils.Validator;
import org.smslib.InboundMessage;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Created by nelson.mahumane on 09-06-2015.
 */
@Controller
@SessionAttributes("notificationPatients")
public class SendMessageController {

	private final Log log = LogFactory.getLog(this.getClass());

	private final SmsReminderHandler smsReminderHandler = new SmsReminderHandler();
	SerialModemGateway aGateway = null;

	@RequestMapping(value = "/module/smsreminder/manual_submission", method = RequestMethod.GET)
	public ModelAndView patientList() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("notificationPatients", SmsReminderResource.getAllNotificationPatiens());
		return modelAndView;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/module/smsreminder/manual_submission", method = RequestMethod.POST)
	public ModelAndView executeSend(final HttpServletRequest request) {

		final SMSClient smsClient = new SMSClient(0);
		final List<NotificationPatient> notificationPatients = (List<NotificationPatient>) request.getSession()
				.getAttribute("notificationPatients");
		final AdministrationService administrationService = Context.getAdministrationService();
		final GlobalProperty gpSmscenter = administrationService.getGlobalPropertyObject("smsreminder.smscenter");
		final String smscenter = gpSmscenter.getPropertyValue();
		final GlobalProperty gpPort = administrationService.getGlobalPropertyObject("smsreminder.port");

		final GlobalProperty gpHis = administrationService.getGlobalPropertyObject("smsreminder.his");
		final String numbers = gpHis.getPropertyValue();

		final String port = gpPort.getPropertyValue();
		final GlobalProperty gpMessage = administrationService.getGlobalPropertyObject("smsreminder.message");
		final String message = gpMessage.getPropertyValue();
		final GlobalProperty gpUs = administrationService.getGlobalPropertyObject("smsreminder.us");
		final String us = gpUs.getPropertyValue();
		final GlobalProperty gpBandRate = administrationService.getGlobalPropertyObject("smsreminder.bandRate");
		final int bandRate = Integer.parseInt(gpBandRate.getPropertyValue());

		final SmsReminderService smsReminderService = SmsReminderUtils.getService();
		final PatientService patientService = Context.getPatientService();
		final LocationService locationService = Context.getLocationService();

		final List<String> asList = Arrays.asList(numbers.split(","));

		for (final String number : asList) {
			if (notificationPatients.size() > 1) {
				this.sendMessage(smscenter, port, bandRate, number,
						"Serão enviadas " + notificationPatients.size()
								+ " Mensagens para Pacientes da Unidade Sanitária"
								+ locationService.getLocation(Integer.valueOf(us)).getName());
			}

			if (notificationPatients.size() == 1) {
				this.sendMessage(smscenter, port, bandRate, number,
						"Sera enviada " + notificationPatients.size() + " Mensagem para Paciente da Unidade Sanitária"
								+ locationService.getLocation(Integer.valueOf(us)).getName());
			}

		}

		if ((notificationPatients != null) && !notificationPatients.isEmpty()) {
			for (final NotificationPatient notificationPatient : notificationPatients) {

				final String messagem = (notificationPatient.getSexo().equals("M"))
						? "O sr: " + notificationPatient.getNome() + " " + message + " " + "no "
								+ locationService.getLocation(Integer.valueOf(us)).getName() + " " + "no dia "
								+ DatasUtil.formatarDataPt(notificationPatient.getProximaVisita())
						: "A sra: " + notificationPatient.getNome() + " " + message + " " + "no "
								+ locationService.getLocation(Integer.valueOf(us)).getName() + " " + "no dia "
								+ DatasUtil.formatarDataPt(notificationPatient.getProximaVisita());

				this.sendMessage(smscenter, port, bandRate, notificationPatient.getTelemovel(), messagem);

				final Sent sent = new Sent();
				sent.setCellNumber(notificationPatient.getTelemovel());
				sent.setAlertDate(notificationPatient.getProximaVisita());
				sent.setMessage(messagem);
				sent.setRemainDays(notificationPatient.getDiasRemanescente());
				sent.setPatient(patientService.getPatient(notificationPatient.getIdentificador()));
				smsReminderService.saveSent(sent);
			}

			for (String number : asList) {
				if (notificationPatients.size() > 1) {
					this.sendMessage(smscenter, port, bandRate, number,
							"Foram enviadas " + notificationPatients.size()
									+ " Mensagens para Pacientes da Unidade Sanitaria"
									+ locationService.getLocation(Integer.valueOf(us)).getName());
				}

				if (notificationPatients.size() == 1) {
					this.sendMessage(smscenter, port, bandRate, number,
							"Foi enviado " + notificationPatients.size()
									+ " Mensagem para Paciente da Unidade Sanitaria"
									+ locationService.getLocation(Integer.valueOf(us)).getName());
				}

			}

		}

		request.getSession().removeAttribute("notificationPatients");
		return new ModelAndView(
				new RedirectView(request.getContextPath() + "/module/smsreminder/manual_submission.form"));
	}

	public void sendMessage(final String smscenter, final String porta, final int bandRate, final String recipient, final String message) {
		final SMSClient smsClient = new SMSClient(0);

		try {
			synchronized (smsClient) {
				smsClient.sendMessage(smscenter, porta, bandRate, Validator.cellNumberValidator(recipient), message);

				while (smsClient.status == -1) {
					smsClient.wait();
				}

			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	// ========================================with
	// SMSLIB-API============================================================

	@RequestMapping(value = "/module/smsreminder/smslib_manual_submission", method = RequestMethod.GET)
	public ModelAndView patientListSMSLIB() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("notificationPatients", SmsReminderResource.getAllNotificationPatiens());
		return modelAndView;
	}

	@RequestMapping(value = "/module/smsreminder/smslib_manual_submission", method = RequestMethod.POST)
	public ModelAndView executeSendSmsLib(final HttpServletRequest request) {

		final AdministrationService administrationService = Context.getAdministrationService();
		final GlobalProperty gpSmscenter = administrationService.getGlobalPropertyObject("smsreminder.smscenter");
		final String smscenter = gpSmscenter.getPropertyValue();
		final GlobalProperty gpPort = administrationService.getGlobalPropertyObject("smsreminder.port");
		final String port = gpPort.getPropertyValue();
		final GlobalProperty gpMessage = administrationService.getGlobalPropertyObject("smsreminder.message");
		final String message = gpMessage.getPropertyValue();
		final GlobalProperty gpUs = administrationService.getGlobalPropertyObject("smsreminder.us");
		final String us = gpUs.getPropertyValue();
		final GlobalProperty gpSimPin = administrationService.getGlobalPropertyObject("smsreminder.simPin");
		final String simPin = gpSimPin.getPropertyValue();
		final GlobalProperty gpBandRate = administrationService.getGlobalPropertyObject("smsreminder.bandRate");
		final int bandRate = Integer.parseInt(gpBandRate.getPropertyValue());

		final GlobalProperty gpModem = administrationService.getGlobalPropertyObject("smsreminder.modem");
		final String modem = gpModem.getPropertyValue();
		final GlobalProperty gpModel = administrationService.getGlobalPropertyObject("smsreminder.model");
		final String model = gpModel.getPropertyValue();

		final SmsReminderService smsReminderService = SmsReminderUtils.getService();
		final PatientService patientService = Context.getPatientService();
		final LocationService locationService = Context.getLocationService();

		if (this.aGateway == null) {
			this.aGateway = this.smsReminderHandler.create(smscenter, port, bandRate, simPin, false, modem, model);
		}

		@SuppressWarnings("unchecked")
		final
		List<NotificationPatient> notificationPatients = (List<NotificationPatient>) request.getSession()
				.getAttribute("notificationPatients");
		if ((notificationPatients != null) && !notificationPatients.isEmpty()) {
			try {
				final OutboundNotification outboundNotification = new OutboundNotification();
				final InboundNotification inboundNotification = new InboundNotification();
				final GatewayStatusNotification statusNotification = new GatewayStatusNotification();

				this.log.info("Checking for service before send sms: " + Service.getInstance().getServiceStatus());
				if (Service.getInstance().getServiceStatus().equals(Service.ServiceStatus.STOPPED)) {
					Service.getInstance().setOutboundMessageNotification(outboundNotification);
					Service.getInstance().setInboundMessageNotification(inboundNotification);
					Service.getInstance().setGatewayStatusNotification(statusNotification);
					Service.getInstance().addGateway(this.aGateway);
					Service.getInstance().startService();
				}
				this.log.info("Start Send messages from a serial gsm modem.");
				for (final NotificationPatient notificationPatient : notificationPatients) {
					final String messagem = (notificationPatient.getSexo().equals("M"))
							? "O sr: " + notificationPatient.getNome() + " " + message + " "
									+ "no " + locationService.getLocation(Integer.valueOf(us)).getName() + " "
									+ "no dia  " + DatasUtil.formatarDataPt(notificationPatient.getProximaVisita())
							: "A sra: " + notificationPatient.getNome() + " " + message + " " + "no "
									+ locationService.getLocation(Integer.valueOf(us)).getName() + " " + "no dia  "
									+ DatasUtil.formatarDataPt(notificationPatient.getProximaVisita());
					// Send a message synchronously.
					final OutboundMessage msg = new OutboundMessage(
							Validator.cellNumberValidator(notificationPatient.getTelemovel()), messagem);
					msg.setStatusReport(true);
					this.log.info("Sending sms for: " + Validator.cellNumberValidator(notificationPatient.getTelemovel()));
					final boolean confirm = Service.getInstance().sendMessage(msg);
					this.log.info(msg);
					if (confirm) {
						final Sent sent = new Sent();
						sent.setCellNumber(notificationPatient.getTelemovel());
						sent.setAlertDate(notificationPatient.getProximaVisita());
						sent.setMessage(messagem);
						sent.setRemainDays(notificationPatient.getDiasRemanescente());
						sent.setPatient(patientService.getPatient(notificationPatient.getIdentificador()));
						sent.setStatus(msg.getMessageStatus().name());
						smsReminderService.saveSent(sent);
						this.log.info("SMS sent for: " + Validator.cellNumberValidator(notificationPatient.getTelemovel()));
					}
				}
				this.log.info("End Sent");
				Service.getInstance().stopService();
				Service.getInstance().removeGateway(this.aGateway);
				this.log.info("Checking for service after send sms : " + Service.getInstance().getServiceStatus());
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		request.getSession().removeAttribute("notificationPatients");
		return new ModelAndView(
				new RedirectView(request.getContextPath() + "/module/smsreminder/smslib_manual_submission.form"));
	}

	// ========================================READMESSAGE-INCOMING============================================================
	@RequestMapping(value = "/module/smsreminder/smslib_manual_read", method = RequestMethod.GET)
	public ModelAndView smsRead() {
		final ModelAndView modelAndView = new ModelAndView();

		final AdministrationService administrationService = Context.getAdministrationService();
		final GlobalProperty gpSmscenter = administrationService.getGlobalPropertyObject("smsreminder.smscenter");
		final String smscenter = gpSmscenter.getPropertyValue();
		final GlobalProperty gpPort = administrationService.getGlobalPropertyObject("smsreminder.port");
		final String port = gpPort.getPropertyValue();
		final GlobalProperty gpSimPin = administrationService.getGlobalPropertyObject("smsreminder.simPin");
		final String simPin = gpSimPin.getPropertyValue();
		final GlobalProperty gpBandRate = administrationService.getGlobalPropertyObject("smsreminder.bandRate");
		final int bandRate = Integer.parseInt(gpBandRate.getPropertyValue());
		final GlobalProperty gpModem = administrationService.getGlobalPropertyObject("smsreminder.modem");
		final String modem = gpModem.getPropertyValue();
		final GlobalProperty gpModel = administrationService.getGlobalPropertyObject("smsreminder.model");
		final String model = gpModel.getPropertyValue();

		if (this.aGateway == null) {
			this.aGateway = this.smsReminderHandler.create(smscenter, port, bandRate, simPin, false, modem, model);
		}

		List<InboundMessage> msgList;

		final InboundNotification inboundNotification = new InboundNotification();
		final GatewayStatusNotification statusNotification = new GatewayStatusNotification();
		try {
			this.log.info(" Reading messages from a serial gsm modem.");
			// Set up the notification methods.
			this.log.info("Checking for service before read sms: " + Service.getInstance().getServiceStatus());
			if (Service.getInstance().getServiceStatus().equals(Service.ServiceStatus.STOPPED)) {
				Service.getInstance().setInboundMessageNotification(inboundNotification);
				Service.getInstance().setGatewayStatusNotification(statusNotification);
				Service.getInstance().addGateway(this.aGateway);
				Service.getInstance().startService();
			}
			msgList = new ArrayList<InboundMessage>();
			// StatusReportMessage.DeliveryStatuses.DELIVERED

			Service.getInstance().readMessages(msgList, InboundMessage.MessageClasses.ALL);
			// if(msgList!=null && !msgList.isEmpty()){
			// if( Service.getInstance().getInboundMessageCount()>0) {
			this.log.info("A Lista é vazia? :" + msgList.isEmpty());
			for (final InboundMessage msg : msgList) {
				// if(msg.getType().equals(Message.MessageTypes.STATUSREPORT)) {
				OutboundMessage.MessageStatuses.values();
				this.log.info(msg);
				this.log.info("Type: " + msg.getType());
				this.log.info(msg.getOriginator());
				this.log.info("Text: " + msg.getText());
				this.log.info("Date: " + msg.getDate());
				this.log.info("MpRefNo: " + msg.getMpRefNo());
				this.log.info("MpSeqNo: " + msg.getMpSeqNo());

				// }
				// Service.getInstance().deleteMessage(msg);
			}
			// }
			// }
			this.log.info("End reading SMS");
			Service.getInstance().stopService();
			Service.getInstance().removeGateway(this.aGateway);

			this.log.info("Checking for service after read sms : " + Service.getInstance().getServiceStatus());
			modelAndView.addObject("openmrs_msg", "smsreminder.smslib_manual_read.success");
		} catch (final Exception e) {
			modelAndView.addObject("openmrs_msg", "smsreminder.smslib_manual_read.error");
			e.printStackTrace();
		}
		return modelAndView;
	}

	@RequestMapping(value = "/module/smsreminder/smslib_manual_read", method = RequestMethod.POST)
	public void executeReadSmslibRead(final HttpServletRequest request) {

	}

}
