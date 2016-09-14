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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.smsreminder.api.db.SmsReminderDAO;
import org.openmrs.module.smsreminder.modelo.NotificationPatient;
import org.openmrs.module.smsreminder.modelo.Sent;
import org.openmrs.module.smsreminder.utils.DatasUtil;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of  {@link SmsReminderDAO}.
 */
public class HibernateSmsReminderDAO implements SmsReminderDAO {
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Sent saveSent(Sent sent) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(sent);
		return sent;
	}

	public void deleteSent(Sent sent) throws DAOException {
		this.sessionFactory.getCurrentSession().delete(sent);
	}

	public List<Sent> getAllSent() throws DAOException {
		Criteria c = this.sessionFactory.getCurrentSession().createCriteria(Sent.class);


		return c.list();
	}

	public Sent getSentById(Integer id)throws DAOException {
		return (Sent) this.sessionFactory.getCurrentSession().get(Sent.class, id);
	}

	public List<Sent> getSentByCellNumber(String cellNumber)throws DAOException{
		Criteria c = this.sessionFactory.getCurrentSession().createCriteria(Sent.class, cellNumber);
		c.addOrder(Order.asc("cellNumber"));
		return c.list();
	}
	public List<Sent> getSentByAlertDate(Date alertDate)throws DAOException{
		Criteria c = this.sessionFactory.getCurrentSession().createCriteria(Sent.class);
		c.add(Restrictions.eq("alertDate", DatasUtil.formatarMysqlDate(alertDate)));
		return c.list();
	}

	public List<Sent> getSentByMessage(String message)throws DAOException{
		Criteria c = this.sessionFactory.getCurrentSession().createCriteria(Sent.class,message);
		return c.list();
	}

	public List<Sent> getSentByStatus(String status)throws DAOException{
		Criteria c = this.sessionFactory.getCurrentSession().createCriteria(Sent.class,status);
		return c.list();
	}

	public List<Sent> getSentByPatient(Patient patient)throws DAOException{
		Criteria c = this.sessionFactory.getCurrentSession().createCriteria(Sent.class);
		c.add(Restrictions.eq("patient",patient));
		return c.list();
	}

	public List<NotificationPatient> getNotificationPatientList() throws DAOException {
		Criteria c = this.sessionFactory.getCurrentSession().createCriteria(NotificationPatient.class);
		return c.list();
	}
	public List<NotificationPatient> getNotificationPatientByDiasRemanescente(Integer diasRemanescente)throws DAOException{
		Criteria c = this.sessionFactory.getCurrentSession().createCriteria(NotificationPatient.class);
		c.add(Restrictions.eq("diasRemanescente",diasRemanescente));
		return c.list();
	}
}