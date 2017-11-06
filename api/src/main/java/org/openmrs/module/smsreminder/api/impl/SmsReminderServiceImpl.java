/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.smsreminder.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.db.PatientDAO;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.smsreminder.api.SmsReminderService;
import org.openmrs.module.smsreminder.api.db.SmsReminderDAO;
import org.openmrs.module.smsreminder.modelo.NotificationFollowUpPatient;
import org.openmrs.module.smsreminder.modelo.NotificationPatient;
import org.openmrs.module.smsreminder.modelo.Sent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * It is a default implementation of {@link SmsReminderService}.
 */
public class SmsReminderServiceImpl extends BaseOpenmrsService implements SmsReminderService {

	protected final Log log = LogFactory.getLog(this.getClass());

	private SmsReminderDAO dao;
	@Autowired
	private PatientDAO patientDAO;

	/**
	 * @return the dao
	 */
	public SmsReminderDAO getDao() {
		return this.dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(final SmsReminderDAO dao) {
		this.dao = dao;
	}

	// service for Sent
	@Override
	public Sent saveSent(final Sent sent) {
		return this.getDao().saveSent(sent);
	}

	@Override
	public List<Sent> getAllSent() throws APIException {
		return this.getDao().getAllSent();
	}

	@Override
	public Sent getSentById(final Integer id) throws APIException {
		return this.getDao().getSentById(id);
	}

	@Override
	public List<Sent> getSentByCellNumber(final String cellNumber) throws APIException {
		return this.getDao().getSentByCellNumber(cellNumber);
	}

	@Override
	public List<Sent> getSentByAlertDate(final Date alertDate) throws APIException {
		return this.getDao().getSentByAlertDate(alertDate);
	}

	@Override
	public List<Sent> getSentByMessage(final String message) throws APIException {
		return this.getDao().getSentByMessage(message);
	}

	@Override
	public List<Sent> getSentByStatus(final String status) throws APIException {
		return this.getDao().getSentByStatus(status);
	}

	@Override
	public List<Sent> getSentByCreated(final Date created) throws APIException {
		return this.getDao().getSentByCreated(created);
	}

	@Override
	public List<Sent> getSentBetweenCreatedAndStatus(final Date start, final Date end, final List statuses)
			throws APIException {
		return this.getDao().getSentBetweenCreatedAndStatus(start, end, statuses);
	}

	@Override
	public List<Sent> getSentByPatient(final Patient patient) throws APIException {
		return this.getDao().getSentByPatient(patient);
	}

	@Override
	public List<NotificationPatient> getNotificationPatientList() throws APIException {
		return this.getDao().getNotificationPatientList();
	}

	@Override
	public List<NotificationPatient> getNotificationPatientByDiasRemanescente(final Integer days) throws APIException {
		return this.getDao().getNotificationPatientByDiasRemanescente(days);
	}

	@Override
	public List<NotificationFollowUpPatient> searchFollowUpPatient() {
		return this.getDao().searchFollowUpPatient();
	}

}