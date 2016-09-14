package org.openmrs.module.smsreminder.utils;

/**
 * Created by nelson.mahumane on 24-08-2016.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;

public class SendMessagesSmsLib {
    protected final Log log = LogFactory.getLog(getClass());
    public void sendMessage(String number, String text,SerialModemGateway gateway) throws Exception {
        log.info("Start Send messages from a serial gsm modem.");
        OutboundNotification outboundNotification = new OutboundNotification();
        Service.getInstance().setOutboundMessageNotification(outboundNotification);
        Service.getInstance().addGateway(gateway);

        Service.getInstance().startService();
        // Send a message synchronously.
        OutboundMessage msg = new OutboundMessage(number,text);
        log.info("Sending sms for: "+number);
        Service.getInstance().sendMessage(msg);
        log.info(msg);
        System.out.println(msg);
        log.info("SMS sent for: "+number);
        log.info("End Sent");
        Service.getInstance().stopService();
    }
}