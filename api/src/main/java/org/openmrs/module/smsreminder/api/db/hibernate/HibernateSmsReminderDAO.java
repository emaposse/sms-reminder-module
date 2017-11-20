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
package org.openmrs.module.smsreminder.api.db.hibernate;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.smsreminder.api.db.SmsReminderDAO;
import org.openmrs.module.smsreminder.modelo.NotificationFollowUpPatient;
import org.openmrs.module.smsreminder.modelo.NotificationPatient;
import org.openmrs.module.smsreminder.modelo.Sent;
import org.openmrs.module.smsreminder.utils.DatasUtil;

/**
 * It is a default implementation of {@link SmsReminderDAO}.
 */
public class HibernateSmsReminderDAO implements SmsReminderDAO {
	protected final Log log = LogFactory.getLog(this.getClass());

	private SessionFactory sessionFactory;

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unused")
	private org.hibernate.Session getCurrentSession() {
		try {
			return this.sessionFactory.getCurrentSession();
		} catch (final NoSuchMethodError ex) {
			try {
				final Method method = this.sessionFactory.getClass().getMethod("getCurrentSession", null);
				return (org.hibernate.Session) method.invoke(this.sessionFactory, null);
			} catch (final Exception e) {
				throw new RuntimeException("Failed to get the current hibernate session", e);
			}
		}
	}

	@Override
	public Sent saveSent(final Sent sent) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(sent);
		return sent;
	}

	@Override
	public void deleteSent(final Sent sent) throws DAOException {
		this.sessionFactory.getCurrentSession().delete(sent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sent> getAllSent() throws DAOException {
		final Criteria c = this.sessionFactory.getCurrentSession().createCriteria(Sent.class);

		return c.list();
	}

	@Override
	public Sent getSentById(final Integer id) throws DAOException {
		return (Sent) this.sessionFactory.getCurrentSession().get(Sent.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sent> getSentByCellNumber(final String cellNumber) throws DAOException {
		final Criteria c = this.sessionFactory.getCurrentSession().createCriteria(Sent.class, cellNumber);
		c.addOrder(Order.asc("cellNumber"));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sent> getSentByAlertDate(final Date alertDate) throws DAOException {
		final Criteria c = this.sessionFactory.getCurrentSession().createCriteria(Sent.class);
		c.add(Restrictions.eq("alertDate", DatasUtil.formatarMysqlDate(alertDate)));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sent> getSentByMessage(final String message) throws DAOException {
		final Criteria c = this.sessionFactory.getCurrentSession().createCriteria(Sent.class, message);
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sent> getSentByStatus(final String status) throws DAOException {
		final Criteria c = this.sessionFactory.getCurrentSession().createCriteria(Sent.class, status);
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sent> getSentByCreated(final Date created) throws DAOException {
		final Criteria c = this.sessionFactory.getCurrentSession().createCriteria(Sent.class);
		c.add(Restrictions.eq("dateCreated", DatasUtil.formatarMysqlDate(created)));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sent> getSentBetweenCreatedAndStatus(final Date start, final Date end,
			@SuppressWarnings("rawtypes") final List statuses) throws DAOException {
		final Criteria c = this.sessionFactory.getCurrentSession().createCriteria(Sent.class);
		c.add(Restrictions.between("dateCreated", DatasUtil.formatarMysqlDate(start),
				DatasUtil.formatarMysqlDate(end)));
		c.add(Restrictions.in("status", statuses));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sent> getSentByPatient(final Patient patient) throws DAOException {
		final Criteria c = this.sessionFactory.getCurrentSession().createCriteria(Sent.class);
		c.add(Restrictions.eq("patient", patient));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NotificationPatient> getNotificationPatientList() throws DAOException {
		final Criteria c = this.sessionFactory.getCurrentSession().createCriteria(NotificationPatient.class);
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NotificationPatient> getNotificationPatientByDiasRemanescente(final Integer diasRemanescente)
			throws DAOException {
		final Criteria c = this.sessionFactory.getCurrentSession().createCriteria(NotificationPatient.class);
		c.add(Restrictions.eq("diasRemanescente", diasRemanescente));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NotificationFollowUpPatient> searchFollowUpPatient() {

		final String sql = "select patient_id, max_frida.encounter_datetime, pa.value, datediff(CURDATE(),o.value_datetime) "
				+ "from(Select p.patient_id,max(encounter_datetime) encounter_datetime from patient p  "
				+ "inner join encounter e on e.patient_id=p.patient_id where p.voided=0 and e.voided=0 and e.encounter_type=18  and e.encounter_datetime<=CURDATE() "
				+ "group by p.patient_id) max_frida  inner join obs o on o.person_id=max_frida.patient_id "
				+ "inner join person_attribute pa on (pa.person_id = max_frida.patient_id) "
				+ "inner join (select p.patient_id as p from patient p "
				+ "inner join encounter e on e.patient_id = p.patient_id "
				+ "inner join obs o on o.encounter_id = e.encounter_id and o.concept_id=6306 "
				+ "inner join obs telef on telef.encounter_id=e.encounter_id and telef.concept_id=6309 and telef.value_coded=6307 and telef.voided=0 "
				+ "where e.encounter_type in (34,35) and e.voided=0 and p.voided=0 and o.voided=0 "
				+ "and p.patient_id NOT IN(select ultima.patient_id from "
				+ "(SELECT p.patient_id as patient_id, MAX(e.encounter_datetime) as encounter_datetime "
				+ "FROM patient p INNER JOIN encounter e ON e.patient_id=p.patient_id "
				+ "INNER JOIN obs o on o.encounter_id=e.encounter_id and o.concept_id=6306 and o.concept_id is not null  "
				+ "WHERE e.encounter_type in (34,35) AND o.concept_id is not null and o.value_coded is not null "
				+ "AND o.voided=0 and e.voided=0 GROUP BY p.patient_id) ultima "
				+ "inner join obs o on o.person_id=ultima.patient_id and o.obs_datetime=ultima.encounter_datetime and o.concept_id=6306 and o.voided=0 and o.value_coded=1066) "
				+ ") consitiu on consitiu.p=max_frida.patient_id "
				+ "where max_frida.encounter_datetime=o.obs_datetime and o.voided=0 and o.concept_id=5096 and pa.person_attribute_type_id = 9 and pa.value is not null and pa.voided = 0 and "
				+ "patient_id not in (select pg.patient_id from patient p "
				+ "inner join patient_program pg on p.patient_id=pg.patient_id "
				+ "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
				+ "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
				+ "pg.program_id=2 and ps.state in (7,8,9,10) and ps.end_date is null and "
				+ "ps.start_date<=CURDATE() union select patient_id from "
				+ "(Select p.patient_id,max(encounter_datetime) encounter_datetime from patient p  "
				+ "inner join encounter e on e.patient_id=p.patient_id "
				+ "inner join obs o on o.encounter_id=e.encounter_id "
				+ "where p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and "
				+ "o.voided=0 and o.concept_id=1255 and o.value_coded<>1260 and e.encounter_datetime<=CURDATE() "
				+ "group by p.patient_id) max_mov " + "inner join obs o on o.person_id=max_mov.patient_id "
				+ "where max_mov.encounter_datetime=o.obs_datetime and o.voided=0 and  "
				+ "o.concept_id=1410 and datediff(CURDATE(), o.value_datetime)<1) "
				+ "and datediff(CURDATE(),o.value_datetime) between 1 and 60 ";

		final Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql);

		final List<NotificationFollowUpPatient> notificationFollowUpPatients = new ArrayList<NotificationFollowUpPatient>();

		final List<Object[]> list = query.list();

		for (final Object[] object : list) {

			final NotificationFollowUpPatient notificationFollowUpPatient = new NotificationFollowUpPatient();

			notificationFollowUpPatient.setPatientId((Integer) object[0]);
			notificationFollowUpPatient.setNextFila((Date) object[1]);
			notificationFollowUpPatient.setPhoneNumber((String) object[2]);
			notificationFollowUpPatient.setTotalFollowUpDays((BigInteger) object[3]);

			notificationFollowUpPatients.add(notificationFollowUpPatient);
		}

		return notificationFollowUpPatients;
	}
}