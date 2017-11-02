package org.openmrs.module.smsreminder.utils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.CommPortOwnershipListener;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * Created by nelson.mahumane on 09-06-2015.
 */
/**
 A class that handles the details of a serial connection. Reads from one
 TextArea and writes to a second TextArea.
 Holds the state of the connection.
 */
public class SerialConnection implements SerialPortEventListener,
        CommPortOwnershipListener {
//    private SerialDemo parent;

/*    private TextArea messageAreaOut;
    private TextArea messageAreaIn;
    */

    private final SerialParameters parameters;
    private OutputStream os;
    private InputStream is;
    private KeyHandler keyHandler;

    private CommPortIdentifier portId;
    private SerialPort sPort;

    private boolean open;

    private String receptionString="";

    public String getIncommingString(){
        final byte[] bVal= this.receptionString.getBytes();
        this.receptionString="";
        return new String (bVal);
    }

    /**
     Creates a SerialConnection object and initilizes variables passed in
     as params.

     //@param parent A SerialDemo object.
     @param parameters A SerialParameters object.
     //@param messageAreaOut The TextArea that messages that are to be sent out
     of the serial port are entered into.
     //@param messageAreaIn The TextArea that messages comming into the serial
     port are displayed on.
     */
    public SerialConnection(final SerialParameters parameters) {
        this.parameters = parameters;
        this.open = false;
    }

    /**
     Attempts to open a serial connection and streams using the parameters
     in the SerialParameters object. If it is unsuccesfull at any step it
     returns the port to a closed state, throws a
     <code>SerialConnectionException</code>, and returns.

     Gives a timeout of 30 seconds on the portOpen to allow other applications
     to reliquish the port if have it open and no longer need it.
     */
    public void openConnection() throws SerialConnectionException {

        // System.out.println("OK 0 ");
        // Obtain a CommPortIdentifier object for the port you want to open.

        try {

            this.portId = CommPortIdentifier.getPortIdentifier(this.parameters.getPortName());
        } catch
                (final NoSuchPortException e)
        {
            // System.out.println("Yes the problem is here 1 ");
            e.printStackTrace();
            // throw new SerialConnectionException(e.getMessage());
        }catch(final Exception e)
        {
            //  System.out.println("ErrorErrorErrorError");
            e.printStackTrace();
        }
        //System.out.println(portId);
        //System.out.println("OK 1 ");
        // Open the port represented by the CommPortIdentifier object. Give
        // the open call a relatively long timeout of 30 seconds to allow
        // a different application to reliquish the port if the user
        // wants to.
        try {
            this.sPort = (SerialPort)this.portId.open("SMSREMIDER",500000);
        } catch (final PortInUseException e) {

            throw new SerialConnectionException(e.getMessage());
        }
        //System.out.println("OK 2 ");
        this.sPort.sendBreak(1000);

        // Set the parameters of the connection. If they won't set, close the
        // port before throwing an exception.
        try {
            this.setConnectionParameters();
        } catch (final SerialConnectionException e) {
            this.sPort.close();
            throw e;
        }
        // System.out.println("OK 3 ");
        // Open the input and output streams for the connection. If they won't
        // open, close the port before throwing an exception.
        try {
            this.os = this.sPort.getOutputStream();
            this.is = this.sPort.getInputStream();
        } catch (final IOException e) {
            this.sPort.close();
            throw new SerialConnectionException("Error opening i/o streams");
        }
//System.out.println("OK 4 ");
/*
	// Create a new KeyHandler to respond to key strokes in the
	// messageAreaOut. Add the KeyHandler as a keyListener to the
	// messageAreaOut.
	keyHandler = new KeyHandler(os);
	messageAreaOut.addKeyListener(keyHandler);
*/
        // Add this object as an event listener for the serial port.
        try {
            this.sPort.addEventListener(this);
        } catch (final TooManyListenersException e) {
            this.sPort.close();
            throw new SerialConnectionException("too many listeners added");
        }
//System.out.println("OK 5 ");
        // Set notifyOnDataAvailable to true to allow event driven input.
        this.sPort.notifyOnDataAvailable(true);

        // Set notifyOnBreakInterrup to allow event driven break handling.
        this.sPort.notifyOnBreakInterrupt(true);

        // Set receive timeout to allow breaking out of polling loop during
        // input handling.
        try {
            this.sPort.enableReceiveTimeout(30);
        } catch (final UnsupportedCommOperationException e) {
        }
//System.out.println("OK 6 ");
        // Add ownership listener to allow ownership event handling.
        this.portId.addPortOwnershipListener(this);

        this.open = true;
    }

    /**
     Sets the connection parameters to the setting in the parameters object.
     If set fails return the parameters object to origional settings and
     throw exception.
     */
    public void setConnectionParameters() throws SerialConnectionException {

        // Save state of parameters before trying a set.
        final int oldBaudRate = this.sPort.getBaudRate();
        final int oldDatabits = this.sPort.getDataBits();
        final int oldStopbits = this.sPort.getStopBits();
        final int oldParity   = this.sPort.getParity();
        this.sPort.getFlowControlMode();

        // Set connection parameters, if set fails return parameters object
        // to original state.
        try {
            this.sPort.setSerialPortParams(this.parameters.getBaudRate(),
                    this.parameters.getDatabits(),
                    this.parameters.getStopbits(),
                    this.parameters.getParity());
        } catch (final UnsupportedCommOperationException e) {
            this.parameters.setBaudRate(oldBaudRate);
            this.parameters.setDatabits(oldDatabits);
            this.parameters.setStopbits(oldStopbits);
            this.parameters.setParity(oldParity);
            throw new SerialConnectionException("Unsupported parameter");
        }

        // Set flow control.
        try {
            this.sPort.setFlowControlMode(this.parameters.getFlowControlIn()|this.parameters.getFlowControlOut());
        } catch (final UnsupportedCommOperationException e) {
            throw new SerialConnectionException("Unsupported flow control");
        }
    }

    /**
     Close the port and clean up associated elements.
     */
    public void closeConnection() {
        // If port is alread closed just return.
        if (!this.open) {
            return;
        }

        // Remove the key listener.
//	messageAreaOut.removeKeyListener(keyHandler);

        // Check to make sure sPort has reference to avoid a NPE.
        if (this.sPort != null) {
            try {
                // close the i/o streams.
                this.os.close();
                this.is.close();
            } catch (final IOException e) {
                System.err.println(e);
            }

            // Close the port.
            this.sPort.close();

            // Remove the ownership listener.
            this.portId.removePortOwnershipListener(this);
        }

        this.open = false;
    }

    /**
     Send a one second break signal.
     */
    public void sendBreak() {
        this.sPort.sendBreak(1000);
    }

    /**
     Reports the open status of the port.
     @return true if port is open, false if port is closed.
     */
    public boolean isOpen() {
        return this.open;
    }

    /**
     Handles SerialPortEvents. The two types of SerialPortEvents that this
     program is registered to listen for are DATA_AVAILABLE and BI. During
     DATA_AVAILABLE the port buffer is read until it is drained, when no more
     data is availble and 30ms has passed the method returns. When a BI
     event occurs the words BREAK RECEIVED are written to the messageAreaIn.
     */

    @Override
	public void serialEvent(final SerialPortEvent e) {
        // Create a StringBuffer and int to receive input data.
        final StringBuffer inputBuffer = new StringBuffer();
        int newData = 0;

        // Determine type of event.

        switch (e.getEventType()) {

            // Read data until -1 is returned. If \r is received substitute
            // \n for correct newline handling.
            case SerialPortEvent.DATA_AVAILABLE:
                while (newData != -1) {
                    try {
                        newData = this.is.read();
                        if (newData == -1) {
                            break;
                        }
                        if ('\r' == (char)newData) {
                            inputBuffer.append('\n');
                        } else {
                            inputBuffer.append((char)newData);
                        }
                    } catch (final IOException ex) {
                        System.err.println(ex);
                        return;
                    }
                }

                // Append received data to messageAreaIn.
                this.receptionString=this.receptionString+ (new String(inputBuffer));
                //System.out.print("<-"+receptionString);
                break;

            // If break event append BREAK RECEIVED message.
            case SerialPortEvent.BI:
                this.receptionString=this.receptionString+("\n--- BREAK RECEIVED ---\n");
        }

    }

    /**
     Handles ownership events. If a PORT_OWNERSHIP_REQUESTED event is
     received a dialog box is created asking the user if they are
     willing to give up the port. No action is taken on other types
     of ownership events.
     */
    @Override
	public void ownershipChange(final int type) {
      /*
	if (type == CommPortOwnershipListener.PORT_OWNERSHIP_REQUESTED) {
	    PortRequestedDialog prd = new PortRequestedDialog(parent);
	}
        */
    }

    /**
     A class to handle <code>KeyEvent</code>s generated by the messageAreaOut.
     When a <code>KeyEvent</code> occurs the <code>char</code> that is
     generated by the event is read, converted to an <code>int</code> and
     writen to the <code>OutputStream</code> for the port.
     */
    class KeyHandler extends KeyAdapter {
        OutputStream os;

        /**
         Creates the KeyHandler.
         @param os The OutputStream for the port.
         */
        public KeyHandler(final OutputStream os) {
            super();
            this.os = os;
        }

        /**
         Handles the KeyEvent.
         Gets the <code>char</char> generated by the <code>KeyEvent</code>,
         converts it to an <code>int</code>, writes it to the <code>
         OutputStream</code> for the port.
         */
        @Override
		public void keyTyped(final KeyEvent evt) {
            char newCharacter = evt.getKeyChar();
            if (newCharacter==10) {
				newCharacter = '\r';
			}
            System.out.println ((int)newCharacter);
            try {
                this.os.write(newCharacter);
            } catch (final IOException e) {
                System.err.println("OutputStream write error: " + e);
            }
        }
    }
    public void send(final String message) {
        final byte[] theBytes= (message+"\n").getBytes();
        for (int i=0; i<theBytes.length;i++){

            char newCharacter = (char)theBytes[i];
            if (newCharacter==10) {
				newCharacter = '\r';
			}

            try {
                this.os.write(newCharacter);
            } catch (final IOException e) {
                System.err.println("OutputStream write error: " + e);
            }

        }
        //System.out.println (">'" +message +"' sent");




    }
}
