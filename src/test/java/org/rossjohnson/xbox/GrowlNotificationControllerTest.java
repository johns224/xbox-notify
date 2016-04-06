package org.rossjohnson.xbox;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.fail;

/**
 * Created by ross on 4/6/16.
 */
public class GrowlNotificationControllerTest {

    GrowlNotificationController controller;

    @org.junit.Before
    public void setUp() throws Exception {
        controller = new GrowlNotificationController();
        controller.init("192.168.1.26", 23053, "password");
    }

    // @Test - uncomment to run
    public void testLocalNotification() {

        try {
            controller.sendNotification("Test", "Ross is cool");
            controller.client.shutdown(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail("Could not send notification");
        }

    }

}