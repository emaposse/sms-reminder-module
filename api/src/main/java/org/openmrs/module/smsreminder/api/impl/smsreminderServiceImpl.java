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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.smsreminder.api.SmsReminderService;
import org.openmrs.module.smsreminder.api.db.SmsReminderDAO;
import org.openmrs.module.smsreminder.modelo.NotificationPatient;
import org.openmrs.module.smsreminder.modelo.Sent;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link SmsReminderService}.
 */
public class SmsReminderServiceImpl extends BaseOpenmrsService implements SmsReminderService {

    protected final Log log = LogFactory.getLog(this.getClass());

    private SmsReminderDAO dao;

    /**
     * @return the dao
     */
    public SmsReminderDAO getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(SmsReminderDAO dao) {
        this.dao = dao;
    }

//service for Sent
    public Sent saveSent(Sent sent) {
        return this.getDao().saveSent(sent);
    }

    public List<Sent> getAllSent()throws APIException{
      return this.getDao().getAllSent();
    }

    public Sent getSentById(Integer id)throws APIException{
        return this.getDao().getSentById(id);
    }

    public List<Sent> getSentByCellNumber(String cellNumber)throws APIException{
        return this.getDao().getSentByCellNumber(cellNumber);
    }

   public List<Sent> getSentByAlertDate(Date alertDate)throws APIException{
        return this.getDao().getSentByAlertDate(alertDate);
    }

    public List<Sent> getSentByMessage(String message)throws APIException{
        return this.getDao().getSentByMessage(message);
    }

    public List<Sent> getSentByStatus(String status)throws APIException{
        return this.getDao().getSentByMessage(status);
    }

    public List<Sent> getSentByPatient(Patient patient)throws APIException{
        return this.getDao().getSentByPatient(patient);
    }


    public List<NotificationPatient> getNotificationPatientList() throws APIException{
        return this.getDao().getNotificationPatientList();
    }

    public List<NotificationPatient> getNotificationPatientByDiasRemanescente(Integer days)throws APIException{
        return  this.getDao().getNotificationPatientByDiasRemanescente(days);
    }

}