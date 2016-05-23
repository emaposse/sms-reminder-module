package org.openmrs.module.smsreminder.utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.Patient;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.smsreminder.SmsReminderUtils;
import org.openmrs.module.smsreminder.api.SmsReminderService;
import org.openmrs.module.smsreminder.modelo.Message;
import org.openmrs.module.smsreminder.modelo.NotificationPatient;
import org.openmrs.module.smsreminder.modelo.Sent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nelson.mahumane on 20-10-2015.
 * Classe que organiza todos recursos necessarios para o envio de sms filtrando as de a cordo com as suas categoria
 */
public class SmsReminderResource {
    /**
     *
     * @return List
     */

    private Log log = LogFactory.getLog(getClass());

    /**
     * @return List
     */
    public static List<NotificationPatient> getAllNotificationPatiens() {

        final AdministrationService administrationService = Context.getAdministrationService();
        PatientService patientService = Context.getPatientService();
        SmsReminderService smsReminderService = SmsReminderUtils.getService();
        GlobalProperty gpRemainDays = administrationService.getGlobalPropertyObject("smsreminder.remaindays");
        String remainDays = gpRemainDays.getPropertyValue();
        List<NotificationPatient> notificationPatientsAll = new ArrayList<NotificationPatient>();
        String days[] =remainDays.split(",");
        int i=0;

        while (i<days.length){

            List<NotificationPatient> notificationPatients = smsReminderService.getNotificationPatientByDiasRemanescente(Integer.valueOf(days[i]));
            if (notificationPatients != null && !notificationPatients.isEmpty()) {

                notificationPatientsAll.addAll(notificationPatients);
            }

            i++;
        }
        return notificationPatientsAll;
    }
}