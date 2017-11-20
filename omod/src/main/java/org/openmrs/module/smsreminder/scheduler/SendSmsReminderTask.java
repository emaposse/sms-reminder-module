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
import org.openmrs.module.smsreminder.modelo.NotificationFollowUpPatient;
import org.openmrs.module.smsreminder.modelo.NotificationPatient;
import org.openmrs.module.smsreminder.modelo.Sent;
import org.openmrs.module.smsreminder.utils.DatasUtil;
import org.openmrs.module.smsreminder.utils.SMSClient;
import org.openmrs.module.smsreminder.utils.SentType;
import org.openmrs.module.smsreminder.utils.SmsReminderResource;
import org.openmrs.module.smsreminder.utils.Validator;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * Created by nelson.mahumane on 20-10-2015.
 */
public class SendSmsReminderTask extends AbstractTask {
	// private static Log log = LogFactory.getLog(SendSmsReminderTask.class);
	private final Log log = LogFactory.getLog(this.getClass());

	@Override
	public void execute() {

		Context.openSession();
		this.log.info("Starting send SMS ... ");
		this.getNotificationPatient();

		this.log.info("Sending SMS to Follow Up Patient ... ");
		this.getNotificationFollowUpPatient();

	}

	private void getNotificationFollowUpPatient() {
		try {
			final AdministrationService administrationService = Context.getAdministrationService();

			final GlobalProperty gpSmscenter = administrationService.getGlobalPropertyObject("smsreminder.smscenter");
			final String smscenter = gpSmscenter.getPropertyValue();
			final GlobalProperty gpPort = administrationService.getGlobalPropertyObject("smsreminder.port");
			final GlobalProperty gpBandRate = administrationService.getGlobalPropertyObject("smsreminder.bandRate");

			final List<NotificationFollowUpPatient> notificationPatients = SmsReminderResource
					.getAllNotificationFolowUpPatient();

			if (!notificationPatients.isEmpty()) {

				for (final NotificationFollowUpPatient notificationFollowUpPatient : notificationPatients) {

					if (notificationFollowUpPatient.getTotalFollowUpDays().intValue() == 4) {

						final String message = "Com saude ha alegria. Lembra-te que tens visita marcada para"
								+ " o dia " + notificationFollowUpPatient.getNextFila()
								+ " Vem ao teu hospital,estamos a tua espera!";

						this.sendMessage(smscenter, gpPort.getPropertyValue(),
								Integer.parseInt(gpBandRate.getPropertyValue()),
								notificationFollowUpPatient.getPhoneNumber(), message);
						notificationFollowUpPatient.setNotificationMassage(message);
						this.saveSent(notificationFollowUpPatient);
					}

					if (notificationFollowUpPatient.getTotalFollowUpDays().intValue() == 7) {
						final String message = "A tua saude e muito importante. Lembra-te que tinhas visita marcada para o dia "
								+ notificationFollowUpPatient.getNextFila() + " Nao deixes de vir ao teu hospital!";
						this.sendMessage(smscenter, gpPort.getPropertyValue(),
								Integer.parseInt(gpBandRate.getPropertyValue()),
								notificationFollowUpPatient.getPhoneNumber(), message);
						notificationFollowUpPatient.setNotificationMassage(message);

						this.saveSent(notificationFollowUpPatient);

					}
					if (notificationFollowUpPatient.getTotalFollowUpDays().intValue() == 15) {
						final String message = "A sua saude e' muito importante para si e para a sua familia."
								+ " Lembra-se que esta sem " + "vir a consulta ha 15 dias.";
						this.sendMessage(smscenter, gpPort.getPropertyValue(),
								Integer.parseInt(gpBandRate.getPropertyValue()),
								notificationFollowUpPatient.getPhoneNumber(), message);
						this.saveSent(notificationFollowUpPatient);

					}

					if (notificationFollowUpPatient.getTotalFollowUpDays().intValue() == 30) {
						final String message = "A sua saude e' muito importante para si e para a sua familia. "
								+ "Continuamos a sua espera. Nao deixe de vir ao seu hospital.";
						this.sendMessage(smscenter, gpPort.getPropertyValue(),
								Integer.parseInt(gpBandRate.getPropertyValue()),
								notificationFollowUpPatient.getPhoneNumber(), message);
						notificationFollowUpPatient.setNotificationMassage(message);

						this.saveSent(notificationFollowUpPatient);

					}
					if (notificationFollowUpPatient.getTotalFollowUpDays().intValue() == 60) {
						final String message = "Com saude construimos o futuro, "
								+ "continue a controlar a sua saude no hospital. " + "Estamos a sua espera!";
						this.sendMessage(smscenter, gpPort.getPropertyValue(),
								Integer.parseInt(gpBandRate.getPropertyValue()),
								notificationFollowUpPatient.getPhoneNumber(), message);
						notificationFollowUpPatient.setNotificationMassage(message);

						this.saveSent(notificationFollowUpPatient);

					}
				}
			}
		} catch (

		final Throwable t) {
			this.log.error("Error while sending SMS ", t);
			throw new APIException(t);
		} finally {
			Context.closeSession();
		}
		this.log.info("Finish send SMS");
	}

	private void saveSent(final NotificationFollowUpPatient notificationFollowUpPatient) {
		final Sent sent = new Sent();
		final PatientService patientService = Context.getPatientService();
		final SmsReminderService smsReminderService = SmsReminderUtils.getService();

		sent.setCellNumber(notificationFollowUpPatient.getPhoneNumber());
		sent.setMessage(notificationFollowUpPatient.getNotificationMassage());
		sent.setAlertDate(notificationFollowUpPatient.getNextFila());
		sent.setRemainDays(notificationFollowUpPatient.getTotalFollowUpDays().intValue());
		sent.setPatient(patientService.getPatient(notificationFollowUpPatient.getPatientId()));
		sent.setSentType(SentType.Follow_Up);
		smsReminderService.saveSent(sent);
		this.log.info("save SMS");

	}

	private void getNotificationPatient() {
		try {
			final AdministrationService administrationService = Context.getAdministrationService();
			final List<NotificationPatient> notificationPatients = SmsReminderResource.getAllNotificationPatiens();
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
							"Serao enviadas " + notificationPatients.size()
									+ " Mensagens para Pacientes da Unidade Sanitaria"
									+ locationService.getLocation(Integer.valueOf(us)).getName());
				}

				if (notificationPatients.size() == 1) {
					this.sendMessage(smscenter, port, bandRate, number,
							"Sera enviada " + notificationPatients.size()
									+ " Mensagem para Paciente da Unidade Sanitaria"
									+ locationService.getLocation(Integer.valueOf(us)).getName());
				}

			}

			if ((notificationPatients != null) && !notificationPatients.isEmpty()) {

				for (final NotificationPatient notificationPatient : notificationPatients) {
					final String messagem = (notificationPatient.getSexo().equals("M"))
							? "O sr: " + notificationPatient.getNome() + " " + message + " "
									+ "no " + locationService.getLocation(Integer.valueOf(us)).getName() + " "
									+ "no dia  " + DatasUtil.formatarDataPt(notificationPatient.getProximaVisita())
							: "A sra: " + notificationPatient.getNome() + " " + message + " " + "no "
									+ locationService.getLocation(Integer.valueOf(us)).getName() + " " + "no dia  "
									+ DatasUtil.formatarDataPt(notificationPatient.getProximaVisita());

					this.sendMessage(smscenter, port, bandRate, notificationPatient.getTelemovel(), messagem);

					final Sent sent = new Sent();
					sent.setCellNumber(notificationPatient.getTelemovel());
					sent.setAlertDate(notificationPatient.getProximaVisita());
					sent.setMessage(messagem);
					sent.setRemainDays(notificationPatient.getDiasRemanescente());
					sent.setPatient(patientService.getPatient(notificationPatient.getIdentificador()));
					sent.setSentType(SentType.New_Member);
					smsReminderService.saveSent(sent);
				}
			}

			for (final String number : asList) {
				if (notificationPatients.size() > 1) {
					this.sendMessage(smscenter, port, bandRate, number,
							"Foram enviadas " + notificationPatients.size()
									+ " Mensagens para Pacientes da Unidade Sanitaria"
									+ locationService.getLocation(Integer.valueOf(us)).getName());
				}

				if (notificationPatients.size() == 1) {
					this.sendMessage(smscenter, port, bandRate, number,
							"Foi enviada " + notificationPatients.size()
									+ " Mensagem para Paciente da Unidade Sanitaria"
									+ locationService.getLocation(Integer.valueOf(us)).getName());
				}

			}

		} catch (

		final Throwable t) {
			this.log.error("Error while sending SMS ", t);
			throw new APIException(t);
		} finally {
			Context.closeSession();
		}
		this.log.info("Finish send SMS");
	}

	public void sendMessage(final String smscenter, final String porta, final int bandRate, final String recipient,
			final String message) {
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

}
