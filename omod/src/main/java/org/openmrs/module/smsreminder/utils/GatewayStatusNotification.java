package org.openmrs.module.smsreminder.utils;

import org.smslib.AGateway;
import org.smslib.IGatewayStatusNotification;

/**
 * Created by nelson.mahumane on 23-08-2016.
 */
public class GatewayStatusNotification implements IGatewayStatusNotification
{
    public void process(AGateway gateway, AGateway.GatewayStatuses oldStatus, AGateway.GatewayStatuses newStatus)
    {
        System.out.println(">>> Gateway Status change for " + gateway.getGatewayId() + ", OLD: " + oldStatus + " -> NEW: " + newStatus);
    }
}