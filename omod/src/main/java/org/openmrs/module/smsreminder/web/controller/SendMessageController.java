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

	private Log log = LogFactory.getLog(getClass());

	private SmsReminderHandler smsReminderHandler = new SmsReminderHandler();
	SerialModemGateway aGateway = null;

	@RequestMapping(value = "/module/smsreminder/manual_submission", method = RequestMethod.GET)
	public ModelAndView patientList() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("notificationPatients", SmsReminderResource.getAllNotificationPatiens());
		return modelAndView;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/module/smsreminder/manual_submission", method = RequestMethod.POST)
	public ModelAndView executeSend(HttpServletRequest request) {

		SMSClient smsClient = new SMSClient(0);
		List<NotificationPatient> notificationPatients = (List<NotificationPatient>) request.getSession()
				.getAttribute("notificationPatients");
		AdministrationService administrationService = Context.getAdministrationService();
		GlobalProperty gpSmscenter = administrationService.getGlobalPropertyObject("smsreminder.smscenter");
		String smscenter = gpSmscenter.getPropertyValue();
		GlobalProperty gpPort = administrationService.getGlobalPropertyObject("smsreminder.port");

		GlobalProperty gpHis = administrationService.getGlobalPropertyObject("smsreminder.his");
		String numbers = gpHis.getPropertyValue();

		String port = gpPort.getPropertyValue();
		GlobalProperty gpMessage = administrationService.getGlobalPropertyObject("smsreminder.message");
		String message = gpMessage.getPropertyValue();
		GlobalProperty gpUs = administrationService.getGlobalPropertyObject("smsreminder.us");
		String us = gpUs.getPropertyValue();
		GlobalProperty gpBandRate = administrationService.getGlobalPropertyObject("smsreminder.bandRate");
		int bandRate = Integer.parseInt(gpBandRate.getPropertyValue());

		SmsReminderService smsReminderService = SmsReminderUtils.getService();
		PatientService patientService = Context.getPatientService();
		LocationService locationService = Context.getLocationService();

		List<String> asList = Arrays.asList(numbers.split(","));

		for (String number : asList) {
			if (notificationPatients.size() > 1) {
				sendMessage(smscenter, port, bandRate, number,
						"Serão enviadas " + notificationPatients.size()
								+ " Mensagens para Pacientes da Unidade Sanitária"
								+ locationService.getLocation(Integer.valueOf(us)).getName());
			}

			if (notificationPatients.size() == 1) {
				sendMessage(smscenter, port, bandRate, number,
						"Sera enviada " + notificationPatients.size() + " Mensagem para Paciente da Unidade Sanitária"
								+ locationService.getLocation(Integer.valueOf(us)).getName());
			}

		}

		if (notificationPatients != null && !notificationPatients.isEmpty()) {
			for (NotificationPatient notificationPatient : notificationPatients) {

				String messagem = (notificationPatient.getSexo().equals("M"))
						? "O sr: " + notificationPatient.getNome() + " " + message + " " + "no "
								+ locationService.getLocation(Integer.valueOf(us)).getName() + " " + "no dia "
								+ DatasUtil.formatarDataPt(notificationPatient.getProximaVisita())
						: "A sra: " + notificationPatient.getNome() + " " + message + " " + "no "
								+ locationService.getLocation(Integer.valueOf(us)).getName() + " " + "no dia "
								+ DatasUtil.formatarDataPt(notificationPatient.getProximaVisita());

				sendMessage(smscenter, port, bandRate, notificationPatient.getTelemovel(), messagem);

				Sent sent = new Sent();
				sent.setCellNumber(notificationPatient.getTelemovel());
				sent.setAlertDate(notificationPatient.getProximaVisita());
				sent.setMessage(messagem);
				sent.setRemainDays(notificationPatient.getDiasRemanescente());
				sent.setPatient(patientService.getPatient(notificationPatient.getIdentificador()));
				smsReminderService.saveSent(sent);
			}

			for (String number : asList) {
				if (notificationPatients.size() > 1) {
					sendMessage(smscenter, port, bandRate, number,
							"Foram enviadas " + notificationPatients.size()
									+ " Mensagens para Pacientes da Unidade Sanitaria"
									+ locationService.getLocation(Integer.valueOf(us)).getName());
				}

				if (notificationPatients.size() == 1) {
					sendMessage(smscenter, port, bandRate, number,
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

	public void sendMessage(String smscenter, String porta, int bandRate, String recipient, String message) {
		SMSClient smsClient = new SMSClient(0);

		try {
			synchronized (smsClient) {
				smsClient.sendMessage(smscenter, porta, bandRate, Validator.cellNumberValidator(recipient), message);

				while (smsClient.status == -1)
					smsClient.wait();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ========================================with
	// SMSLIB-API============================================================

	@RequestMapping(value = "/module/smsreminder/smslib_manual_submission", method = RequestMethod.GET)
	public ModelAndView patientListSMSLIB() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("notificationPatients", SmsReminderResource.getAllNotificationPatiens());
		return modelAndView;
	}

	@RequestMapping(value = "/module/smsreminder/smslib_manual_submission", method = RequestMethod.POST)
	public ModelAndView executeSendSmsLib(HttpServletRequest request) {

		AdministrationService administrationService = Context.getAdministrationService();
		GlobalProperty gpSmscenter = administrationService.getGlobalPropertyObject("smsreminder.smscenter");
		String smscenter = gpSmscenter.getPropertyValue();
		GlobalProperty gpPort = administrationService.getGlobalPropertyObject("smsreminder.port");
		String port = gpPort.getPropertyValue();
		GlobalProperty gpMessage = administrationService.getGlobalPropertyObject("smsreminder.message");
		String message = gpMessage.getPropertyValue();
		GlobalProperty gpUs = administrationService.getGlobalPropertyObject("smsreminder.us");
		String us = gpUs.getPropertyValue();
		GlobalProperty gpSimPin = administrationService.getGlobalPropertyObject("smsreminder.simPin");
		String simPin = gpSimPin.getPropertyValue();
		GlobalProperty gpBandRate = administrationService.getGlobalPropertyObject("smsreminder.bandRate");
		int bandRate = Integer.parseInt(gpBandRate.getPropertyValue());

		GlobalProperty gpModem = administrationService.getGlobalPropertyObject("smsreminder.modem");
		String modem = gpModem.getPropertyValue();
		GlobalProperty gpModel = administrationService.getGlobalPropertyObject("smsreminder.model");
		String model = gpModel.getPropertyValue();

		SmsReminderService smsReminderService = SmsReminderUtils.getService();
		PatientService patientService = Context.getPatientService();
		LocationService locationService = Context.getLocationService();

		if (aGateway == null)
			aGateway = smsReminderHandler.create(smscenter, port, bandRate, simPin, false, modem, model);

		@SuppressWarnings("unchecked")
		List<NotificationPatient> notificationPatients = (List<NotificationPatient>) request.getSession()
				.getAttribute("notificationPatients");
		if (notificationPatients != null && !notificationPatients.isEmpty()) {
			try {
				OutboundNotification outboundNotification = new OutboundNotification();
				InboundNotification inboundNotification = new InboundNotification();
				GatewayStatusNotification statusNotification = new GatewayStatusNotification();

				log.info("Checking for service before send sms: " + Service.getInstance().getServiceStatus());
				if (Service.getInstance().getServiceStatus().equals(Service.ServiceStatus.STOPPED)) {
					Service.getInstance().setOutboundMessageNotification(outboundNotification);
					Service.getInstance().setInboundMessageNotification(inboundNotification);
					Service.getInstance().setGatewayStatusNotification(statusNotification);
					Service.getInstance().addGateway(aGateway);
					Service.getInstance().startService();
				}
				log.info("Start Send messages from a serial gsm modem.");
				for (NotificationPatient notificationPatient : notificationPatients) {
					String messagem = (notificationPatient.getSexo().equals("M"))
							? "O sr: " + notificationPatient.getNome() + " " + message + " "
									+ "no " + locationService.getLocation(Integer.valueOf(us)).getName() + " "
									+ "no dia  " + DatasUtil.formatarDataPt(notificationPatient.getProximaVisita())
							: "A sra: " + notificationPatient.getNome() + " " + message + " " + "no "
									+ locationService.getLocation(Integer.valueOf(us)).getName() + " " + "no dia  "
									+ DatasUtil.formatarDataPt(notificationPatient.getProximaVisita());
					// Send a message synchronously.
					OutboundMessage msg = new OutboundMessage(
							Validator.cellNumberValidator(notificationPatient.getTelemovel()), messagem);
					msg.setStatusReport(true);
					log.info("Sending sms for: " + Validator.cellNumberValidator(notificationPatient.getTelemovel()));
					boolean confirm = Service.getInstance().sendMessage(msg);
					log.info(msg);
					if (confirm) {
						Sent sent = new Sent();
						sent.setCellNumber(notificationPatient.getTelemovel());
						sent.setAlertDate(notificationPatient.getProximaVisita());
						sent.setMessage(messagem);
						sent.setRemainDays(notificationPatient.getDiasRemanescente());
						sent.setPatient(patientService.getPatient(notificationPatient.getIdentificador()));
						sent.setStatus(msg.getMessageStatus().name());
						smsReminderService.saveSent(sent);
						log.info("SMS sent for: " + Validator.cellNumberValidator(notificationPatient.getTelemovel()));
					}
				}
				log.info("End Sent");
				Service.getInstance().stopService();
				Service.getInstance().removeGateway(aGateway);
				log.info("Checking for service after send sms : " + Service.getInstance().getServiceStatus());
			} catch (Exception e) {
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
		ModelAndView modelAndView = new ModelAndView();

		AdministrationService administrationService = Context.getAdministrationService();
		GlobalProperty gpSmscenter = administrationService.getGlobalPropertyObject("smsreminder.smscenter");
		String smscenter = gpSmscenter.getPropertyValue();
		GlobalProperty gpPort = administrationService.getGlobalPropertyObject("smsreminder.port");
		String port = gpPort.getPropertyValue();
		GlobalProperty gpSimPin = administrationService.getGlobalPropertyObject("smsreminder.simPin");
		String simPin = gpSimPin.getPropertyValue();
		GlobalProperty gpBandRate = administrationService.getGlobalPropertyObject("smsreminder.bandRate");
		int bandRate = Integer.parseInt(gpBandRate.getPropertyValue());
		GlobalProperty gpModem = administrationService.getGlobalPropertyObject("smsreminder.modem");
		String modem = gpModem.getPropertyValue();
		GlobalProperty gpModel = administrationService.getGlobalPropertyObject("smsreminder.model");
		String model = gpModel.getPropertyValue();

		if (aGateway == null)
			aGateway = smsReminderHandler.create(smscenter, port, bandRate, simPin, false, modem, model);

		List<InboundMessage> msgList;

		@SuppressWarnings("unused")
		String status = null;
		InboundNotification inboundNotification = new InboundNotification();
		GatewayStatusNotification statusNotification = new GatewayStatusNotification();
		try {
			log.info(" Reading messages from a serial gsm modem.");
			// Set up the notification methods.
			log.info("Checking for service before read sms: " + Service.getInstance().getServiceStatus());
			if (Service.getInstance().getServiceStatus().equals(Service.ServiceStatus.STOPPED)) {
				Service.getInstance().setInboundMessageNotification(inboundNotification);
				Service.getInstance().setGatewayStatusNotification(statusNotification);
				Service.getInstance().addGateway(aGateway);
				Service.getInstance().startService();
			}
			msgList = new ArrayList<InboundMessage>();
			// StatusReportMessage.DeliveryStatuses.DELIVERED

			Service.getInstance().readMessages(msgList, InboundMessage.MessageClasses.ALL);
			// if(msgList!=null && !msgList.isEmpty()){
			// if( Service.getInstance().getInboundMessageCount()>0) {
			log.info("A Lista é vazia? :" + msgList.isEmpty());
			for (InboundMessage msg : msgList) {
				// if(msg.getType().equals(Message.MessageTypes.STATUSREPORT)) {
				OutboundMessage.MessageStatuses.values();
				log.info(msg);
				log.info("Type: " + msg.getType());
				log.info(msg.getOriginator());
				log.info("Text: " + msg.getText());
				log.info("Date: " + msg.getDate());
				log.info("MpRefNo: " + msg.getMpRefNo());
				log.info("MpSeqNo: " + msg.getMpSeqNo());

				// }
				// Service.getInstance().deleteMessage(msg);
			}
			// }
			// }
			log.info("End reading SMS");
			Service.getInstance().stopService();
			Service.getInstance().removeGateway(aGateway);

			log.info("Checking for service after read sms : " + Service.getInstance().getServiceStatus());
			modelAndView.addObject("openmrs_msg", "smsreminder.smslib_manual_read.success");
		} catch (Exception e) {
			modelAndView.addObject("openmrs_msg", "smsreminder.smslib_manual_read.error");
			e.printStackTrace();
		}
		return modelAndView;
	}

	@RequestMapping(value = "/module/smsreminder/smslib_manual_read", method = RequestMethod.POST)
	public void executeReadSmslibRead(HttpServletRequest request) {

	}

}
