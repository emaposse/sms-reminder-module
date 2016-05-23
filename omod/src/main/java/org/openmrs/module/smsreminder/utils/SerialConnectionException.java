package org.openmrs.module.smsreminder.utils;

/* @(#)SerialConnectionException.java
 *
 */
/**
 * Created by nelson.mahumane on 09-06-2015.
 */

public class SerialConnectionException extends Exception {

    /**
     * Constructs a <code>SerialConnectionException</code>
     * with the specified detail message.
     *
     * @param   str   the detail message.
     */
    public SerialConnectionException(String str) {
        super(str);
}

    /**
     * Constructs a <code>SerialConnectionException</code>
     * with no detail message.
     */
    public SerialConnectionException() {
        super();
    }
}
