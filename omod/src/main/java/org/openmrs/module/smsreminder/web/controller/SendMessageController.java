package org.openmrs.module.smsreminder.web.controller;

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
import org.openmrs.module.smsreminder.utils.SMSClient;
import org.openmrs.module.smsreminder.utils.SendMessage;
import org.openmrs.module.smsreminder.utils.Validator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
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
        AdministrationService administrationService = Context.getAdministrationService();
        GlobalProperty gpRemainDays = administrationService.getGlobalPropertyObject("smsreminder.remaindays");
        String remainDays = gpRemainDays.getPropertyValue();
        List<NotificationPatient> notificationPatientsAll = new ArrayList<NotificationPatient>();
        String days[] = remainDays.split(",");
        int i = 0;

        while (i < days.length) {

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
        SMSClient smsClient = new SMSClient(0);
        List<NotificationPatient> notificationPatients = (List<NotificationPatient>) request.getSession().getAttribute("notificationPatients");
        AdministrationService administrationService = Context.getAdministrationService();
        GlobalProperty gpSmscenter = administrationService.getGlobalPropertyObject("smsreminder.smscenter");
        String smscenter = gpSmscenter.getPropertyValue();
        GlobalProperty gpPort = administrationService.getGlobalPropertyObject("smsreminder.port");
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
        if (notificationPatients != null && !notificationPatients.isEmpty()) {
            Iterator<NotificationPatient> it = notificationPatients.iterator();
            while (it.hasNext()) {
                //for (NotificationPatient notificationPatient : notificationPatients) {
                //System.out.println("O sexo: " + notificationPatient.getSexo());
                NotificationPatient notificationPatient = it.next();
                String messagem = (notificationPatient.getSexo().equals("M")) ?
                        "O sr: " + notificationPatient.getNome() + " " + message + " " + "no " + locationService.getLocation(Integer.valueOf(us)).getName() + " " + "no dia  " + DatasUtil.formatarDataPt(notificationPatient.getProximaVisita()) :
                        "A sra: " + notificationPatient.getNome() + " " + message + " " + "no " + locationService.getLocation(Integer.valueOf(us)).getName() + " " + "no dia  " + DatasUtil.formatarDataPt(notificationPatient.getProximaVisita());


                try {
                    synchronized(smsClient) {
                     smsClient.sendMessage(smscenter, port, bandRate, Validator.cellNumberValidator(notificationPatient.getTelemovel()), messagem);
                       while (smsClient.status==-1)
                        smsClient.wait();
                     }
                    Sent sent = new Sent();
                    sent.setCellNumber(notificationPatient.getTelemovel());
                    sent.setAlertDate(notificationPatient.getProximaVisita());
                    sent.setMessage(messagem);
                    sent.setRemainDays(notificationPatient.getDiasRemanescente());
                    sent.setPatient(patientService.getPatient(notificationPatient.getIdentificador()));
                    System.out.println("O Status: " + smsClient.status);
                    if (smsClient.status==0)
                        smsReminderService.saveSent(sent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        request.getSession().removeAttribute("notificationPatients");
        return new ModelAndView(new RedirectView(request.getContextPath()
                + "/module/smsreminder/manual_submission.form"));
    }

    //========================================with SMSLIB-API============================================================

    /** @RequestMapping(value = "/module/smsreminder/smslib_manual_submission", method = RequestMethod.GET)
    public ModelAndView patientListSMSLIB() {
    ModelAndView modelAndView = new ModelAndView();
    SmsReminderService smsReminderService = SmsReminderUtils.getService();
    AdministrationService administrationService = Context.getAdministrationService();
    GlobalProperty gpRemainDays = administrationService.getGlobalPropertyObject("smsreminder.remaindays");
    String remainDays = gpRemainDays.getPropertyValue();
    List<NotificationPatient> notificationPatientsAll = new ArrayList<NotificationPatient>();
    String days[] = remainDays.split(",");
    int i = 0;

    while (i < days.length) {

    List<NotificationPatient> notificationPatients = smsReminderService.getNotificationPatientByDiasRemanescente(Integer.valueOf(days[i]));
    if (notificationPatients != null && !notificationPatients.isEmpty()) {

    notificationPatientsAll.addAll(notificationPatients);
    }

    i++;
    }
    modelAndView.addObject("notificationPatients", notificationPatientsAll);

    return modelAndView;
    }


     @RequestMapping(value = "/module/smsreminder/smslib_manual_submission", method = RequestMethod.POST)
     public ModelAndView executeSendSmsLib(HttpServletRequest request) {
     SmsReminderHandler smsReminderHandler = new SmsReminderHandler();
     List<NotificationPatient> notificationPatients = (List<NotificationPatient>) request.getSession().getAttribute("notificationPatients");
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

     SerialModemGateway aGateway = smsReminderHandler.create(smscenter, port, bandRate, simPin, false);

     SmsReminderService smsReminderService = SmsReminderUtils.getService();
     PatientService patientService = Context.getPatientService();
     LocationService locationService = Context.getLocationService();
     if (notificationPatients != null && !notificationPatients.isEmpty()) {
     try {
     OutboundNotification outboundNotification = new OutboundNotification();
     Service.getInstance().setOutboundMessageNotification(outboundNotification);
     Service.getInstance().addGateway(aGateway);
     Service.getInstance().startService();
     log.info("Start Send messages from a serial gsm modem.");
     for (NotificationPatient notificationPatient : notificationPatients) {
     String messagem = (notificationPatient.getSexo().equals("M")) ?
     "O sr: " + notificationPatient.getNome() + " " + message + " " + "no " + locationService.getLocation(Integer.valueOf(us)).getName() + " " + "no dia  " + DatasUtil.formatarDataPt(notificationPatient.getProximaVisita()) :
     "A sra: " + notificationPatient.getNome() + " " + message + " " + "no " + locationService.getLocation(Integer.valueOf(us)).getName() + " " + "no dia  " + DatasUtil.formatarDataPt(notificationPatient.getProximaVisita());

     // Send a message synchronously.
     OutboundMessage msg = new OutboundMessage(Validator.cellNumberValidator(notificationPatient.getTelemovel()), messagem);
     log.info("Sending sms for: " + Validator.cellNumberValidator(notificationPatient.getTelemovel()));
     boolean confirm = Service.getInstance().sendMessage(msg);
     log.info(msg);
     System.out.println(msg);
     if (confirm) {
     Sent sent = new Sent();
     sent.setCellNumber(notificationPatient.getTelemovel());
     sent.setAlertDate(notificationPatient.getProximaVisita());
     sent.setMessage(messagem);
     sent.setRemainDays(notificationPatient.getDiasRemanescente());
     sent.setPatient(patientService.getPatient(notificationPatient.getIdentificador()));
     smsReminderService.saveSent(sent);
     log.info("SMS sent for: " + Validator.cellNumberValidator(notificationPatient.getTelemovel()));
     }
     }
     log.info("End Sent");
     Service.getInstance().stopService();
     } catch (Exception e) {
     e.printStackTrace();
     }
     }

     request.getSession().removeAttribute("notificationPatients");
     return new ModelAndView(new RedirectView(request.getContextPath()
     + "/module/smsreminder/smslib_manual_submission.form"));
     }**/


}
