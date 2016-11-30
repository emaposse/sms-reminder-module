package org.openmrs.module.smsreminder.utils;

/*
 * 
 * Created by nelson.mahumane on 10-06-2015.
 *
*/
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class Sender implements Runnable  {
    private Log log = LogFactory.getLog(getClass());
  private static final long STANDARD=500;
  private static final long LONG=2000;
  private static final long  VERYLONG=20000;

  SerialConnection mySerial =null;

  static final private char cntrlZ=(char)26;
  String in, out;
  Thread aThread=null;
  private long delay=STANDARD;
  private String recipient;
  private String message;
 private String csca; // the message center
  //private String csca="+25884080011"; // the message center
  private SerialParameters defaultParameters;
  public int step;
  public int status=-1;
  public long messageNo=-1;

  public Sender(String recipient, String message,String smsc,String porta,int bandRate){

     this.recipient=recipient;
    this.message=message;
      this.csca=smsc;
      defaultParameters= new SerialParameters(porta,bandRate,0,0,8,1,0);
  }
  /**
   * connect to the port and start the dialogue thread
   * conecta a porta e inicia o dialogo com o fluxo
   */
  public int send () throws Exception{

    SerialParameters params = defaultParameters;

    mySerial =new SerialConnection (params);

    mySerial.openConnection();

    aThread=new Thread(this);

    aThread.start() ;
   
    //log("start");

    return 0;
  }

  /**
   * implement the dialogue thread,
   * message / response via steps,
   * handle time out
   */

  public void run(){

    boolean timeOut=false;
    long startTime=(new Date()).getTime();



    while ((step <7) && (!timeOut)){
//      log(""+((new Date()).getTime() - startTime);
      //check where we are in specified delay
      timeOut=((new Date()).getTime() - startTime)>delay;

      //if atz does not work, type to send cntrlZ and retry, in case a message was stuck
      if (timeOut && (step==1)) {
          step=-1;
          mySerial.send(" "+cntrlZ);
      }

      //read incoming string
      String result=  mySerial.getIncommingString() ;

//    log ("<- "+result+"\n--------");
      int expectedResult=-1;

      try{
        //log ("Step:"+step);

        switch (step){
          case 0:
       // mySerial.send("AT+CFUN=6");
         mySerial.send("atz");
            delay=LONG;
            startTime=(new Date()).getTime();
            break;
          case 1:
            delay=STANDARD;
           mySerial.send("ath0");
            startTime=(new Date()).getTime();
            break;
          case 2:
            expectedResult=result.indexOf("OK");
            //log ("received ok ="+expectedResult);
              //System.out.println("Não condiz:"+result +":"+ expectedResult);
            if (expectedResult>-1){
              mySerial.send("at+cmgf=1");
              startTime=(new Date()).getTime();
            }else{
                step=step-1;
          }
            break;
          case 3:
            expectedResult=result.indexOf("OK");
           // log ("received ok ="+expectedResult);
            if (expectedResult>-1){
              mySerial.send("at+csca=\""+csca+"\"");
              startTime=(new Date()).getTime();
            }else{
             step=step-1;
            }
            break;
          case 4:
            expectedResult=result.indexOf("OK");

           // log ("received ok ="+expectedResult);
            if (expectedResult>-1){
              mySerial.send("at+cmgs=\""+recipient+"\"");
              startTime=(new Date()).getTime();
           }else{
             step=step-1;
            }

            break;
          case 5:
            expectedResult=result.indexOf(">");

           // log ("received ok ="+expectedResult);
            if (expectedResult>-1){
              mySerial.send(message+cntrlZ);
            //System.out.println("enviou....");
              startTime=(new Date()).getTime();
            }else{
              step=step-1;
            }
            delay=VERYLONG;//waitning for message ack

            break;

          case 6:
            expectedResult=result.indexOf("OK");
            //read message number
            if (expectedResult>-1){
              int n=result.indexOf("CMGS:");
              result=result.substring(n+5);
              n=result.indexOf("\n");
              status=0;
              messageNo=Long.parseLong(result.substring(0,n).trim() );

             this.log.info ("sent message no:"+messageNo);


            }else{
              step=step-1;
           }

          break;
        }
        step=step+1;

        aThread.sleep(100);

      }catch (Exception e){
          e.printStackTrace();
      }
    }

    mySerial.closeConnection() ;

    //if timed out set status

    if (timeOut ) {
        status=-2;
        this.log.info(new java.util.Date()+":"+this.getClass().getName()+":"+"*** time out at step "+step+"***");
    }
  }

}