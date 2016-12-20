package org.openmrs.module.smsreminder.utils;

import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
/**
 * Created by Nelson.Mahumane on 01-12-2016.
 */
public class SmsReminderProperty {
    public static final String SMSCENTER="smsreminder.smscenter";
    public static final String PORT="smsreminder.port";
    public static final String MESSAGE="smsreminder.message";
    public static final String US="smsreminder.us";
    public static final String PIN="smsreminder.simPin";
    public static final String BAUDRATE="smsreminder.bandRate";
    public static final String MODEM="smsreminder.modem";
    public static final String MODEL="smsreminder.model";

    final AdministrationService administrationService = Context.getAdministrationService();



}
