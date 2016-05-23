package org.openmrs.module.smsreminder.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.*;
import org.openmrs.api.*;
import org.openmrs.api.context.Context;
import org.openmrs.module.smsreminder.SmsReminderUtils;
import org.openmrs.module.smsreminder.api.SmsReminderService;
import org.openmrs.module.smsreminder.modelo.Device;
import org.openmrs.module.smsreminder.modelo.Message;
import org.openmrs.module.smsreminder.modelo.NotificationPatient;
import org.openmrs.module.smsreminder.modelo.Sent;
import org.openmrs.module.smsreminder.utils.DatasUtil;
import org.openmrs.module.smsreminder.utils.OpenmrsResources;
import org.openmrs.module.smsreminder.utils.SendMessage;
import org.openmrs.module.smsreminder.utils.Validator;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by nelson.mahumane on 09-06-2015.
 */
@Controller
@SessionAttributes("notificationPatients")
public class SendMessageController {
    private Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/module/smsreminder/manual_submission", method = RequestMethod.GET)
    public ModelAndView patientList() {
        ModelAndView modelAndView = new ModelAndView();
        SmsReminderService smsReminderService = SmsReminderUtils.getService();
        AdministrationService administrationService= Context.getAdministrationService();
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
        modelAndView.addObject("notificationPatients", notificationPatientsAll);

        return modelAndView;
    }

    @RequestMapping(value = "/module/smsreminder/manual_submission", method = RequestMethod.POST)
    public ModelAndView executeSend(HttpServletRequest request) {
        SendMessage sendMessage=new SendMessage();
        List < NotificationPatient > notificationPatients = (List<NotificationPatient>) request.getSession().getAttribute("notificationPatients");
        AdministrationService administrationService= Context.getAdministrationService();
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
        if (notificationPatients!=null && !notificationPatients.isEmpty()) {
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
                    }else{log.info("Erro enviando sms para: "+notificationPatient.getTelemovel());}
                } catch (Exception e) {
                    this.log.error("Erro enviando sms para: " + e.getMessage());
                }
            }
        }

        request.getSession().removeAttribute("notificationPatients");
        return new ModelAndView(new RedirectView(request.getContextPath()
                + "/module/smsreminder/manual_submission.form"));
    }


    private List<String> numberCell(){
        List lista=new ArrayList();
        lista.add("+258820069340");
        lista.add("+258840271520");
        lista.add("+258829502145");
        lista.add("+258844626770");
        lista.add("+258840499320");
        lista.add("+258842678790");
        lista.add("+258829091879");
        lista.add("+258827514420");
        lista.add("+258829194190");
        lista.add("+258842038918");
        lista.add("+258828269683");
        lista.add("+258822645407");
        return  lista;
    }


}
