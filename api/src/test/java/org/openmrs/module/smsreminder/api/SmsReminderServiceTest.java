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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.List;

import org.junit.Ignore;
import org.openmrs.api.context.Context;
import org.openmrs.module.smsreminder.modelo.NotificationFollowUpPatient;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * Tests {@link {SmsReminderService}}.
 */
public class SmsReminderServiceTest extends BaseModuleContextSensitiveTest {

	public void shouldSetupContext() {
		assertNotNull(Context.getService(SmsReminderService.class));
	}

	@Ignore
	public void getNotificationFollowUpPatient() throws ParseException {

		final SmsReminderService smsReminderService = Context.getService(SmsReminderService.class);

		final List<NotificationFollowUpPatient> followUpPatients = smsReminderService.searchFollowUpPatient();

		assertFalse(followUpPatients.isEmpty());

	}

}
