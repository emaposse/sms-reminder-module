package org.openmrs.module.smsreminder.utils;

/**
 * Created by nelson.mahumane on 23-08-2016.
 */
// QueryMessageStatus.java
//
// This Class shows you the basic procedure needed for reading
// SMS messages from your GSM modem, in synchronous mode.
//



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.smsreminder.modelo.Sent;
import org.smslib.*;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.modem.SerialModemGateway;
import java.util.ArrayList;
import java.util.List;

public class QueryMessageStatus {
    private SerialModemGateway gateway;
    private Log log = LogFactory.getLog(getClass());

    public QueryMessageStatus(SerialModemGateway gateway){
        this.gateway=gateway;
    }
    public String query() throws Exception {
        List<InboundMessage> msgList;
        String status=null;

        InboundNotification inboundNotification = new InboundNotification();
        try {
           log.info(" Reading messages from a serial gsm modem.");
            // Set up the notification methods.
            Service.getInstance().setInboundMessageNotification(inboundNotification);
            Service.getInstance().addGateway(gateway);
            Service.getInstance().startService();
            msgList = new ArrayList<InboundMessage>();
            Service.getInstance().readMessages(msgList, MessageClasses.UNREAD);
            if(msgList!=null && !msgList.isEmpty()){
            for (InboundMessage msg : msgList){
                if(msg.getType().equals(Message.MessageTypes.STATUSREPORT)) {
                    System.out.println(msg.getOriginator());
                    System.out.println(msg.getText());
                }
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("End reading SMS");
            Service.getInstance().stopService();
        }
        return status;
    }
}
