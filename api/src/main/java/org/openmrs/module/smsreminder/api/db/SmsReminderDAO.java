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
package org.openmrs.module.smsreminder.api.db;

import java.util.Date;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.smsreminder.api.SmsReminderService;
import org.openmrs.module.smsreminder.modelo.NotificationFollowUpPatient;
import org.openmrs.module.smsreminder.modelo.NotificationPatient;
import org.openmrs.module.smsreminder.modelo.Sent;

/**
 * Database methods for {@link SmsReminderService}.
 */
public interface SmsReminderDAO {

	/*
	 * Add DAO methods here
	 */

	public Sent saveSent(Sent sent);

	public void deleteSent(Sent sent) throws DAOException;

	public List<Sent> getAllSent() throws DAOException;

	public Sent getSentById(Integer id) throws DAOException;

	public List<Sent> getSentByCellNumber(String cellNumber) throws DAOException;

	public List<Sent> getSentByAlertDate(Date alertDate) throws DAOException;

	public List<Sent> getSentByMessage(String message) throws DAOException;

	public List<Sent> getSentByStatus(String status) throws DAOException;

	public List<Sent> getSentByCreated(Date created) throws DAOException;

	public List<Sent> getSentBetweenCreatedAndStatus(Date start, Date end, List statuses) throws DAOException;

	public List<Sent> getSentByPatient(Patient patient) throws DAOException;

	public List<NotificationPatient> getNotificationPatientList() throws DAOException;

	public List<NotificationPatient> getNotificationPatientByDiasRemanescente(Integer days) throws DAOException;

	public List<NotificationFollowUpPatient> searchFollowUpPatient();
}