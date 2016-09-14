package org.openmrs.module.smsreminder.utils;

import org.smslib.AGateway;
import org.smslib.ICallNotification;

/**
 * Created by nelson.mahumane on 23-08-2016.
 */
public class CallNotification implements ICallNotification
{
    public void process(AGateway gateway, String callerId)
    {
        System.out.println(">>> New call detected from Gateway: " + gateway.getGatewayId() + " : " + callerId);
    }
}
