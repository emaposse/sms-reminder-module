package org.openmrs.module.smsreminder.modelo;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Patient;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by nelson.mahumane on 03-06-2015.
 */
public class Message extends BaseOpenmrsData  implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer messageId;
    private String type;
    private String action;
    private Integer days;
    private String messageDescription;

    public Message() {
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getId() {
        return getMessageId();
    }

    public void setId(Integer id) {
        setMessageId(id);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
}

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getDays() {
        return days;
    }
    public void setDays(Integer days) {
        this.days = days;
    }
    public String getMessageDescription() {
        return messageDescription;
    }
    public void setMessageDescription(String messageDescription) {
        this.messageDescription = messageDescription;
    }
}
