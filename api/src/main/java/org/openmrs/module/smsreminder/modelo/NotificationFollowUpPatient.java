package org.openmrs.module.smsreminder.modelo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import org.openmrs.BaseOpenmrsData;

public class NotificationFollowUpPatient extends BaseOpenmrsData implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer patientId;
	private Date nextFila;
	private String phoneNumber;
	private BigInteger totalFollowUpDays;
	private String notificationMassage;

	public Integer getPatientId() {
		return this.patientId;
	}

	public void setPatientId(final Integer patientId) {
		this.patientId = patientId;
	}

	@Override
	public Integer getId() {
		return this.getPatientId();
	}

	@Override
	public void setId(final Integer id) {
		this.setPatientId(id);
	}

	public Date getNextFila() {
		return this.nextFila;
	}

	public void setNextFila(final Date nextFila) {
		this.nextFila = nextFila;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public BigInteger getTotalFollowUpDays() {
		return this.totalFollowUpDays;
	}

	public void setTotalFollowUpDays(final BigInteger totalFollowUpDays) {
		this.totalFollowUpDays = totalFollowUpDays;
	}

	public String getNotificationMassage() {
		return this.notificationMassage;
	}

	public void setNotificationMassage(final String notificationMassage) {
		this.notificationMassage = notificationMassage;
	}

}
