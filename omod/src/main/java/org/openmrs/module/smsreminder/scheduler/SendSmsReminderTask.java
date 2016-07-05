package org.openmrs.module.smsreminder.scheduler;

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
import org.openmrs.module.smsreminder.utils.SendMessage;
import org.openmrs.module.smsreminder.utils.SmsReminderResource;
import org.openmrs.module.smsreminder.utils.Validator;
import org.openmrs.scheduler.tasks.AbstractTask;

import java.util.List;

/**
 * Created by nelson.mahumane on 20-10-2015.
 */
public class SendSmsReminderTask extends AbstractTask {
    private static Log log = LogFactory.getLog(SendSmsReminderTask.class);

    @Override
    public void execute() {
        Context.openSession();
        log.info("Starting send SMS ... ");
        try {
            SendMessage sendMessage = new SendMessage();
            AdministrationService administrationService = Context.getAdministrationService();
            List<NotificationPatient> notificationPatients = SmsReminderResource.getAllNotificationPatiens();
            GlobalProperty gpSmscenter = administrationService.getGlobalPropertyObject("smsreminder.smscenter");
            String smscenter = gpSmscenter.getPropertyValue();
            GlobalProperty gpPort = administrationService.getGlobalPropertyObject("smsreminder.port");
            String port = gpPort.getPropertyValue();
            GlobalProperty gpMessage = administrationService.getGlobalPropertyObject("smsreminder.message");
            String message = gpMessage.getPropertyValue();
            GlobalProperty gpUs = administrationService.getGlobalPropertyObject("smsreminder.us");
            String us = gpUs.getPropertyValue();

            SmsReminderService smsReminderService = SmsReminderUtils.getService();
            PatientService patientService = Context.getPatientService();
            LocationService locationService = Context.getLocationService();
            if (notificationPatients != null && !notificationPatients.isEmpty()) {
                for (NotificationPatient notificationPatient : notificationPatients) {
                    String messagem=(notificationPatient.getSexo().equals("M"))?
                            "O sr: " + notificationPatient.getNome() + " " + message +" " + "no " + locationService.getLocation(Integer.valueOf(us)).getName()+" " +"no dia  "+ DatasUtil.formatarDataPt(notificationPatient.getProximaVisita()):
                            "A sra: " + notificationPatient.getNome() + " " + message +" " + "no " + locationService.getLocation(Integer.valueOf(us)).getName()+" " +"no dia  "+ DatasUtil.formatarDataPt(notificationPatient.getProximaVisita());
                    Sent sent = new Sent();
                    sent.setCellNumber(notificationPatient.getTelemovel());
                    sent.setAlertDate(notificationPatient.getProximaVisita());
                    sent.setMessage(messagem);
                    sent.setRemainDays(notificationPatient.getDiasRemanescente());
                    sent.setPatient(patientService.getPatient(notificationPatient.getIdentificador()));

                    try {
                        int status = sendMessage.sendMessage(smscenter, port, Validator.cellNumberValidator(notificationPatient.getTelemovel()), messagem);
                        smsReminderService.saveSent(sent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Throwable t) {
            log.error("Error while sending SMS ", t);
            throw new APIException(t);
        } finally {
            Context.closeSession();
        }
        log.info("Finish send SMS");
    }
}
