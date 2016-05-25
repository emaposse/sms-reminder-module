/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.smsreminder.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * The main controller.
 */
@Controller
public class SMSRemindertoFGHPatientsManageController {

    protected final Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/module/smsreminder/manage_port", method = RequestMethod.GET)
    public ModelAndView currentPort() {
        ModelAndView modelAndView = new ModelAndView();
        AdministrationService administrationService= Context.getAdministrationService();
        GlobalProperty gp= administrationService.getGlobalPropertyObject("smsreminder.port");
        String port=gp.getPropertyValue();
        if(port.equals(null) || port.equals("")){
            modelAndView.addObject("openmrs_msg","manage_port.not.set");
        }


        modelAndView.addObject("port", port);
        return modelAndView;
    }

    @RequestMapping(value = "/module/smsreminder/manage_message", method = RequestMethod.GET)
    public ModelAndView currentMessage() {
        ModelAndView modelAndView = new ModelAndView();
        AdministrationService administrationService= Context.getAdministrationService();
        GlobalProperty gp= administrationService.getGlobalPropertyObject("smsreminder.message");
        String message=gp.getPropertyValue();
        if(message.equals(null) || message.equals("")){
            modelAndView.addObject("openmrs_msg","manage_message.not.set");
        }


        modelAndView.addObject("message", message);
        return modelAndView;
    }



    @RequestMapping(value = "/module/smsreminder/manage_smscenter", method = RequestMethod.GET)
    public ModelAndView currentSmsCenter() {
        ModelAndView modelAndView = new ModelAndView();
        AdministrationService administrationService= Context.getAdministrationService();
        GlobalProperty gp= administrationService.getGlobalPropertyObject("smsreminder.smscenter");
        String smscenter=gp.getPropertyValue();
        if(smscenter.equals(null) || smscenter.equals("")) {
            modelAndView.addObject("openmrs_msg", "manage_smscenter.not.set");

        }
            modelAndView.addObject("smscenter", smscenter);
            return modelAndView;
        }

}
