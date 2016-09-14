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
    private String status;
    private Patient patient;
    private Integer remainDays;

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

    public Integer getRemainDays() {
        return remainDays;
    }

    public void setRemainDays(Integer remainDays) {
        this.remainDays = remainDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sent)) return false;
        if (!super.equals(o)) return false;

        Sent sent = (Sent) o;

        return sentId.equals(sent.sentId);

    }

}
