package org.openmrs.module.smsreminder.utils;

import org.smslib.AGateway;
import org.smslib.modem.SerialModemGateway;

/**
 * Created by nelson.mahumane on 24-08-2016.
 */
public class SmsReminderHandler {

    public SerialModemGateway create(String smsCentre,String porta,int bandRate,String simPin,boolean required,String marca, String modelo){
        SerialModemGateway gateway = new SerialModemGateway("modem",porta,bandRate,marca, modelo);
        gateway.setInbound(true);
        gateway.setOutbound(true);
        gateway.setSmscNumber(smsCentre);
        //gateway.setProtocol(AGateway.Protocols.PDU);
       // gateway.getATHandler().setStorageLocations("ME");//iguala a MT
        // gateway.getATHandler().setStorageLocations("MT");//ambos cartao sim e memoria do modem
        //gateway.getATHandler().setStorageLocations("SM");//cartao sim
       //gateway.getATHandler().setStorageLocations("ME");//memoria do modem
        gateway.getATHandler().setStorageLocations("SR");//memoria de delivery
        if (required)
            gateway.setSimPin(simPin);
        return gateway;
    }
}
