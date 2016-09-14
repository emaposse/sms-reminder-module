package org.openmrs.module.smsreminder.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by nelson.mahumane on 10-06-2015.
 */
public class SendMessage{

    private Log log = LogFactory.getLog(getClass());
    public int sendMessage(String smsCentre, String porta,int bandRate,String number, String text){
        SMSClient client=new  SMSClient(0);
        return  client.sendMessage(smsCentre, porta,bandRate,number,text);

    }

}
