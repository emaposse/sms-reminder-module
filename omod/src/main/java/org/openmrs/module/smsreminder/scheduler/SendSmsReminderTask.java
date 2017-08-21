package org.openmrs.module.smsreminder.scheduler;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.LocationService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.smsreminder.SmsReminderUtils;
import org.openmrs.module.smsreminder.api.SmsReminderService;
import org.openmrs.module.smsreminder.modelo.NotificationPatient;
import org.openmrs.module.smsreminder.modelo.Sent;
import org.openmrs.module.smsreminder.utils.DatasUtil;
import org.openmrs.module.smsreminder.utils.SMSClient;
import org.openmrs.module.smsreminder.utils.SmsReminderResource;
import org.openmrs.module.smsreminder.utils.Validator;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * Created by nelson.mahumane on 20-10-2015.
 */
public class SendSmsReminderTask extends AbstractTask {
	// private static Log log = LogFactory.getLog(SendSmsReminderTask.class);
	private Log log = LogFactory.getLog(getClass());

	@Override
	public void execute() {
		Context.openSession();
		log.info("Starting send SMS ... ");

		try {
			AdministrationService administrationService = Context.getAdministrationService();
			List<NotificationPatient> notificationPatients = SmsReminderResource.getAllNotificationPatiens();
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
			SMSClient smsClient = new SMSClient(0);

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
							"Sera enviada " + notificationPatients.size()
									+ " Mensagem para Paciente da Unidade Sanitária"
									+ locationService.getLocation(Integer.valueOf(us)).getName());
				}

			}

			if (notificationPatients != null && !notificationPatients.isEmpty()) {

				for (NotificationPatient notificationPatient : notificationPatients) {
					String messagem = (notificationPatient.getSexo().equals("M"))
							? "O sr: " + notificationPatient.getNome() + " " + message + " "
									+ "no " + locationService.getLocation(Integer.valueOf(us)).getName() + " "
									+ "no dia  " + DatasUtil.formatarDataPt(notificationPatient.getProximaVisita())
							: "A sra: " + notificationPatient.getNome() + " " + message + " " + "no "
									+ locationService.getLocation(Integer.valueOf(us)).getName() + " " + "no dia  "
									+ DatasUtil.formatarDataPt(notificationPatient.getProximaVisita());

					sendMessage(smscenter, port, bandRate, notificationPatient.getTelemovel(), messagem);

					Sent sent = new Sent();
					sent.setCellNumber(notificationPatient.getTelemovel());
					sent.setAlertDate(notificationPatient.getProximaVisita());
					sent.setMessage(messagem);
					sent.setRemainDays(notificationPatient.getDiasRemanescente());
					sent.setPatient(patientService.getPatient(notificationPatient.getIdentificador()));
					if (smsClient.status == 0)
						smsReminderService.saveSent(sent);
				}
			}

			for (String number : asList) {
				if (notificationPatients.size() > 1) {
					sendMessage(smscenter, port, bandRate, number,
							"Foram enviadas " + notificationPatients.size()
									+ " Mensagens para Pacientes da Unidade Sanitária"
									+ locationService.getLocation(Integer.valueOf(us)).getName());
				}

				if (notificationPatients.size() == 1) {
					sendMessage(smscenter, port, bandRate, number,
							"Foi enviado " + notificationPatients.size()
									+ " Mensagem para Paciente da Unidade Sanitária"
									+ locationService.getLocation(Integer.valueOf(us)).getName());
				}

			}

		} catch (

		Throwable t) {
			log.error("Error while sending SMS ", t);
			throw new APIException(t);
		} finally {
			Context.closeSession();
		}
		log.info("Finish send SMS");
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

}
