package org.openmrs.module.smsreminder.utils;

/*
 * 
 * @author : Nelson Mahumane
 *
*/
public class SMSClient implements Runnable {

  public final static int SYNCHRONOUS = 0;
  public final static int ASYNCHRONOUS = 1;
  private Thread myThread = null;

  private int mode = -1;
  private String recipient = null;
  private String message = null;
  private String smsc;
  private String porta;
  private int bandRate;

  public int status = -1;
  public long messageNo = -1;


  public SMSClient(int mode) {
    this.mode = mode;
  }

  public int sendMessage(String smsc, String porta, int bandRate, String recipient, String message) {
    this.recipient = recipient;
    this.message = message;
    this.porta = porta;
    this.smsc = smsc;
    this.bandRate = bandRate;
    //System.out.println("recipient: " + recipient + " message: " + message);
    myThread = new Thread(this);
    myThread.start();
    //run();
    //ObsService obsService = Context.getObsService();


    //EncounterService encounterService=Context.getEncounterService();

    return status;
  }

  public void run() {
    synchronized (this) {
      Sender aSender = new Sender(recipient, message, smsc, porta, bandRate);

      try {
        //send message
        aSender.send();

        // System.out.println("sending ... ");

        //in SYNCHRONOUS mode wait for return : 0 for OK, -2 for timeout, -1 for other errors
        if (mode == SYNCHRONOUS) {
          while (aSender.status == -1) {
            myThread.sleep(10000);
          }
        }
        if (aSender.status == 0) messageNo = aSender.messageNo;

      } catch (Exception e) {
        e.printStackTrace();
      }

      this.status = aSender.status;

      aSender = null;

      notifyAll();
    }
  }
}