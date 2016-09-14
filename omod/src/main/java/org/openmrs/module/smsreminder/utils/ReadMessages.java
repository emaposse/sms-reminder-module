package org.openmrs.module.smsreminder.utils;

/**
 * Created by nelson.mahumane on 23-08-2016.
 */
// ReadMessages.java - Sample application.
//
// This application shows you the basic procedure needed for reading
// SMS messages from your GSM modem, in synchronous mode.
//
// Operation description:
// The application setup the necessary objects and connects to the phone.
// As a first step, it reads all messages found in the phone.
// Then, it goes to sleep, allowing the asynchronous callback handlers to
// be called. Furthermore, for callback demonstration purposes, it responds
// to each received message with a "Got It!" reply.
//
// Tasks:
// 1) Setup Service object.
// 2) Setup one or more Gateway objects.
// 3) Attach Gateway objects to Service object.
// 4) Setup callback notifications.
// 5) Run


import org.smslib.InboundMessage;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;
import java.util.ArrayList;
import java.util.List;

public class ReadMessages {

    public static void read(String porta,SerialModemGateway gateway ) throws Exception {
        List<InboundMessage> msgList;
        // Create the notification callback method for inbound & status report
        // messages.
        InboundNotification inboundNotification = new InboundNotification();
        // Create the notification callback method for inbound voice calls.
        CallNotification callNotification = new CallNotification();
        //Create the notification callback method for gateway statuses.
        GatewayStatusNotification statusNotification = new GatewayStatusNotification();
        OrphanedMessageNotification orphanedMessageNotification = new OrphanedMessageNotification();
        try {
            System.out.println(" Reading messages from a serial gsm modem.");
            // Set up the notification methods.
            Service.getInstance().setInboundMessageNotification(inboundNotification);
            Service.getInstance().setCallNotification(callNotification);
            Service.getInstance().setGatewayStatusNotification(statusNotification);
            Service.getInstance().setOrphanedMessageNotification(orphanedMessageNotification);
            // Add the Gateway to the Service object.
            Service.getInstance().addGateway(gateway);
            // Similarly, you may define as many Gateway objects, representing
            // various GSM modems, add them in the Service object and control all of them.
            // Start! (i.e. connect to all defined Gateways)
            Service.getInstance().startService();
            msgList = new ArrayList<InboundMessage>();
            Service.getInstance().readMessages(msgList, MessageClasses.ALL);
            if(msgList!=null && !msgList.isEmpty()){
            for (InboundMessage msg : msgList)
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Service.getInstance().stopService();
        }
    }
}
