package org.rossjohnson.notification.controller;

import org.junit.Test;

import static org.junit.Assert.*;

public class KodiNotificationControllerTest {

	@Test
	public void testGetUrl() throws Exception {
		KodiNotificationController knc = new KodiNotificationController();

		assert(knc.getUrl("foo", "bar", "http://localhost:8080").contains("%22title%22%3A+%22foo%22"));
	}
}