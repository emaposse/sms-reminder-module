package org.openmrs.module.smsreminder.utils;

import org.smslib.AGateway;
import org.smslib.IInboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Message;

/**
 * Created by nelson.mahumane on 23-08-2016.
 */
public class InboundNotification implements IInboundMessageNotification
{
    public void process(AGateway gateway, Message.MessageTypes msgType, InboundMessage msg)
    {
        if (msgType == Message.MessageTypes.INBOUND) System.out.println(">>> New Inbound message detected from Gateway: " + gateway.getGatewayId());
        else if (msgType == Message.MessageTypes.STATUSREPORT) System.out.println(">>> New Inbound Status Report message detected from Gateway: " + gateway.getGatewayId());
        System.out.println(msg);
    }
}
