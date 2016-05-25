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
import org.openmrs.module.smsreminder.utils.SendMessage;
import org.openmrs.module.smsreminder.utils.SmsReminderResource;
import org.openmrs.module.smsreminder.utils.Validator;
import org.openmrs.scheduler.tasks.AbstractTask;

import java.util.List;

/**
 * Created by nelson.mahumane on 20-10-2015.
 */
public class SendSmsReminderTask  extends AbstractTask {
    private static Log log = LogFactory.getLog(SendSmsReminderTask.class);

    @Override
    public void execute() {
       Context.openSession();
        log.info("Starting send SMS ... ");
        try {
            SendMessage sendMessage=new SendMessage();
            AdministrationService administrationService= Context.getAdministrationService();
            List <NotificationPatient> notificationPatients = SmsReminderResource.getAllNotificationPatiens();
            GlobalProperty gpSmscenter= administrationService.getGlobalPropertyObject("smsreminder.smscenter");
            String smscenter=gpSmscenter.getPropertyValue();
            GlobalProperty gpPort= administrationService.getGlobalPropertyObject("smsreminder.port");
            String port=gpPort.getPropertyValue();
            GlobalProperty gpMessage= administrationService.getGlobalPropertyObject("smsreminder.message");
            String message=gpMessage.getPropertyValue();
            GlobalProperty gpUs= administrationService.getGlobalPropertyObject("smsreminder.us");
            String us=gpUs.getPropertyValue();

            SmsReminderService smsReminderService=SmsReminderUtils.getService();
            PatientService patientService = Context.getPatientService();
            LocationService locationService=Context.getLocationService();
            if(notificationPatients!=null && !notificationPatients.isEmpty()) {
                for (NotificationPatient notificationPatient : notificationPatients) {
                    String messagem = "O sr. " + notificationPatient.getNome() + " " + message + " " + notificationPatient.getProximaVisita() + " " + "no " + locationService.getLocation(Integer.valueOf(us)).getName();

                    try {
                        int status = sendMessage.sendMessage(smscenter, port, Validator.cellNumberValidator(notificationPatient.getTelemovel()), messagem);
                        if (status == 0) {
                            Sent sent = new Sent();
                            sent.setCellNumber(notificationPatient.getTelemovel());
                            sent.setAlertDate(notificationPatient.getProximaVisita());
                            sent.setMessage(messagem);
                            sent.setRemainDays(notificationPatient.getDiasRemanescente().byteValue());
                            sent.setPatient(patientService.getPatient(notificationPatient.getIdentificador()));
                            smsReminderService.saveSent(sent);
                            log.info("SMS enviada para o número: "+notificationPatient.getTelemovel());
                        }else{this.log.error("Erro enviando sms para: " + notificationPatient.getTelemovel());}
                    } catch (Exception e) {
                        this.log.error("Erro enviando sms : " + e.getMessage());
                    }
                }
            }
        }
        catch (Throwable t) {
            log.error("Error while sending SMS ", t);
            throw new APIException(t);
        }
        finally {
            Context.closeSession();
        }
        log.info("Finish send SMS");
    }
}
