package org.openmrs.module.smsreminder.web.controller;

import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Nelson.Mahumane on 07-06-2015.
 */

@Controller
public class ConfigController {


    @RequestMapping(value = "/module/smsreminder/manage_port_update", method = RequestMethod.GET)
    public void manage_port_update(ModelMap model) {
        model.addAttribute("user", Context.getAuthenticatedUser());
    }

    @RequestMapping(value={"/module/smsreminder/manage_port_update"},method ={RequestMethod.POST})
    public ModelAndView processPortForm(HttpServletRequest request){
        AdministrationService administrationService= Context.getAdministrationService();
        GlobalProperty gp= administrationService.getGlobalPropertyObject("smsreminder.port");
       String porta= request.getParameter("port");
        gp.setPropertyValue(porta);
        administrationService.saveGlobalProperty(gp);
        return new ModelAndView(new RedirectView(request.getContextPath()
                + "/module/smsreminder/manage_port.form")).addObject("openmrs_msg",
                "manage_port.saved").addObject("port",porta);
    }

    @RequestMapping(value = "/module/smsreminder/manage_message_update", method = RequestMethod.GET)
    public void manage_message_update(ModelMap model) {
        model.addAttribute("user", Context.getAuthenticatedUser());

    }

    @RequestMapping(value = "/module/smsreminder/manage_message_update", method = RequestMethod.POST)
    public ModelAndView processMessageForm(HttpServletRequest request){
        AdministrationService administrationService= Context.getAdministrationService();
        GlobalProperty gp= administrationService.getGlobalPropertyObject("smsreminder.message");
        String message= request.getParameter("message");
        gp.setPropertyValue(message);
        administrationService.saveGlobalProperty(gp);
        return new ModelAndView(new RedirectView(request.getContextPath()
                + "/module/smsreminder/manage_message.form")).addObject("openmrs_msg",
                "manage_message.saved").addObject("message",message);
    }


    @RequestMapping(value = "/module/smsreminder/manage_smscenter_update", method = RequestMethod.GET)
    public void manage_smsCenter_update(ModelMap model) {
        model.addAttribute("user", Context.getAuthenticatedUser());
    }

    @RequestMapping(value = "/module/smsreminder/manage_smscenter_update", method = RequestMethod.POST)
    public ModelAndView processSmsCenterForm(HttpServletRequest request){
        AdministrationService administrationService= Context.getAdministrationService();
        GlobalProperty gp= administrationService.getGlobalPropertyObject("smsreminder.smscenter");
        String smscenter= request.getParameter("smscenter");
        gp.setPropertyValue(smscenter);
        administrationService.saveGlobalProperty(gp);
        return new ModelAndView(new RedirectView(request.getContextPath()
                + "/module/smsreminder/manage_smscenter.form")).addObject("openmrs_msg",
                "manage_smscenter.saved").addObject("smscenter",smscenter);
    }

   }
