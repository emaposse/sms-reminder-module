package org.openmrs.module.smsreminder.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.smsreminder.SmsReminderUtils;
import org.openmrs.module.smsreminder.api.SmsReminderService;
import org.openmrs.module.smsreminder.modelo.Device;
import org.openmrs.module.smsreminder.modelo.Message;
import org.openmrs.module.smsreminder.modelo.NotificationPatient;
import org.openmrs.module.smsreminder.utils.SendMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nelson.mahumane on 09-06-2015.
 */
@Controller
public class SendMessageController {

    private Log log = LogFactory.getLog(getClass());


    @RequestMapping(value = "/module/smsreminder/manual_submission", method = RequestMethod.GET)
    public ModelAndView patientList() {
        ModelAndView modelAndView = new ModelAndView();
        SmsReminderService service = SmsReminderUtils.getService();
        List<NotificationPatient> notificationPatients = service.getAllNotificationPatients();
        modelAndView.addObject("notificationPatients", notificationPatients);
        return modelAndView;
    }

    @RequestMapping(value = "/module/smsreminder/manual_submission", method = RequestMethod.POST)
    public ModelAndView executeSend(HttpServletRequest request) {
       SendMessage sendMessage=new SendMessage();
        SmsReminderService service = SmsReminderUtils.getService();
        List<NotificationPatient> notificationPatients = service.getAllNotificationPatients();
        Message message=new Message();
        List<Device>devices=service.getDevices();
        List<String> numerosCellTeste=numberCell();
        Device config=new Device();
        for (Device device:devices){
            config=device;
            break;
        }
        for(NotificationPatient notificationPatient:notificationPatients){
            try{
                sendMessage.sendMessage(config.getSmsCenter(),config.getPort(),notificationPatient.getTelemovel(),config.getText());

            }
            catch (Exception e){this.log.error("Erro enviando sms para: " +  e.getMessage());}
        }

      /* for (String numero:numerosCellTeste){
           try {

               sendMessage.sendMessage(config.getSmsCenter(),config.getPort(),numero,config.getText());
               /*sendMessage.send(config.getSmsCenter(),config.getPort(),"",numerosCellTeste.get(i),config.getText());
               message.setCellNumber(notificationPatient.getTelemovel());
               message.setDescription(config.getText());
               message.setMessageTime(new Date());
               message.setIdentifier(notificationPatient.getNid());
               service.saveMessage(message);

           }
         catch (Exception e){this.log.error("Erro enviando sms para: " +  e.getMessage());}
        }
        */

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
