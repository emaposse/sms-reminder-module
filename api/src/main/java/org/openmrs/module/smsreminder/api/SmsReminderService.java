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
package org.openmrs.module.smsreminder.api;

import java.util.Date;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.smsreminder.modelo.NotificationFollowUpPatient;
import org.openmrs.module.smsreminder.modelo.NotificationPatient;
import org.openmrs.module.smsreminder.modelo.Sent;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service exposes module's core functionality. It is a Spring managed bean
 * which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(smsreminderService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface SmsReminderService extends OpenmrsService {

	/*
	 * Add service methods here
	 * 
	 */

	@Authorized({ "Manage sent" })
	public Sent saveSent(Sent sent);

	@Transactional
	@Authorized({ "view Sent" })
	public List<Sent> getAllSent() throws APIException;

	@Transactional
	@Authorized({ "view Sent" })
	public Sent getSentById(Integer id) throws APIException;

	@Transactional
	@Authorized({ "view Sent" })
	public List<Sent> getSentByCellNumber(String cellNumber) throws APIException;

	@Authorized({ "view Sent" })
	public List<Sent> getSentByAlertDate(Date alertDate) throws APIException;

	@Transactional
	@Authorized({ "view Sent" })
	public List<Sent> getSentByMessage(String message) throws APIException;

	@Transactional
	@Authorized({ "view Sent" })
	public List<Sent> getSentByStatus(String status) throws APIException;

	@Transactional
	@Authorized({ "view Sent" })
	public List<Sent> getSentByCreated(Date created) throws APIException;

	@SuppressWarnings("rawtypes")
	@Transactional
	@Authorized({ "view Sent" })
	public List<Sent> getSentBetweenCreatedAndStatus(Date start, Date end, List status) throws APIException;

	@Transactional
	@Authorized({ "view Sent" })
	public List<Sent> getSentByPatient(Patient patient) throws APIException;

	@Transactional
	@Authorized({ "view NotificationPatient" })
	public List<NotificationPatient> getNotificationPatientList() throws APIException;

	@Transactional
	@Authorized({ "view NotificationPatient" })
	public List<NotificationPatient> getNotificationPatientByDiasRemanescente(Integer days) throws APIException;

	@Transactional
	public List<NotificationFollowUpPatient> searchFollowUpPatient();

}