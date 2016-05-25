package org.openmrs.module.smsreminder;

/**
 * Created by nelson.mahumane on 05-06-2015.
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.smsreminder.api.SmsReminderService;

public class SmsReminderUtils {
    protected final Log log = LogFactory.getLog(getClass());
public static SmsReminderService getService(){

return (SmsReminderService)Context.getService(SmsReminderService.class);
}
}
