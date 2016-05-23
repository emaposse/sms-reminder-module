package org.openmrs.module.smsreminder.modelo;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Patient;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Nelson.Mahumane on 03-09-2015.
 */
public class Sent extends BaseOpenmrsData implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer sentId;
    private  String cellNumber;
    private Date alertDate;
    private String message;
    private Patient patient;
    private Byte remainDays;

    public Integer getSentId() {
        return sentId;
    }

    public void setSentId(Integer sentId) {
        this.sentId = sentId;
    }
    public Integer getId() {
        return getSentId();
    }
    public void setId(Integer id) {
        setSentId(id);
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public Date getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Byte getRemainDays() {
        return remainDays;
    }

    public void setRemainDays(Byte remainDays) {
        this.remainDays = remainDays;
    }

    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }


}
