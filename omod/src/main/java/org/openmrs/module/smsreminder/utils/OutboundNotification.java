package org.openmrs.module.smsreminder.utils;

import org.smslib.AGateway;
import org.smslib.IOutboundMessageNotification;
import org.smslib.OutboundMessage;

/**
 * Created by nelson.mahumane on 10-06-2015.
 */
public class OutboundNotification implements IOutboundMessageNotification {

    public void process(AGateway gateway, OutboundMessage msg)
    {
        System.out.println("Outbound handler called from Gateway: " + gateway.getGatewayId());
        System.out.println(msg);
    }
}
