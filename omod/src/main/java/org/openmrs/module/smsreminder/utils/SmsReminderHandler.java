package org.openmrs.module.smsreminder.utils;

import org.smslib.AGateway;
import org.smslib.modem.SerialModemGateway;

/**
 * Created by nelson.mahumane on 24-08-2016.
 */
public class SmsReminderHandler {

    public SerialModemGateway create(String smsCentre,String porta,int bandRate,String simPin,boolean required){
        SerialModemGateway gateway = new SerialModemGateway("modem",porta,bandRate,"", "");
        gateway.setInbound(true);
        gateway.setOutbound(true);
        gateway.setSmscNumber(smsCentre);
        gateway.setProtocol(AGateway.Protocols.PDU);

        if (required)
            gateway.setSimPin(simPin);

        return gateway;

    }


}
