package org.openmrs.module.smsreminder.modelo;

import java.io.Serializable;
import java.util.Date;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Patient;
import org.openmrs.module.smsreminder.utils.SentType;

/**
 * Created by Nelson.Mahumane on 03-09-2015.
 */
public class Sent extends BaseOpenmrsData implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer sentId;
    private  String cellNumber;
    private Date alertDate;
    private Date dateCreated;
    private String message;
    private String status;
    private Patient patient;
    private Integer remainDays;
    private SentType sentType;

    public Integer getSentId() {
        return this.sentId;
    }

    public void setSentId(final Integer sentId) {
        this.sentId = sentId;
    }
    @Override
	public Integer getId() {
        return this.getSentId();
    }
    @Override
	public void setId(final Integer id) {
        this.setSentId(id);
    }

    public String getCellNumber() {
        return this.cellNumber;
    }

    public void setCellNumber(final String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public Date getAlertDate() {
        return this.alertDate;
    }

    public void setAlertDate(final Date alertDate) {
        this.alertDate = alertDate;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Integer getRemainDays() {
        return this.remainDays;
    }

    public void setRemainDays(final Integer remainDays) {
        this.remainDays = remainDays;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public Date getDateCreated() {
        return this.dateCreated;
    }

    @Override
    public void setDateCreated(final Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Patient getPatient() {
        return this.patient;
    }
    public void setPatient(final Patient patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
			return true;
		}
        if (!(o instanceof Sent)) {
			return false;
		}
        if (!super.equals(o)) {
			return false;
		}

        final Sent sent = (Sent) o;

        return this.sentId.equals(sent.sentId);

    }

	public SentType getSentType() {
		return this.sentType;
	}

	public void setSentType(final SentType sentType) {
		this.sentType = sentType;
	}

}
