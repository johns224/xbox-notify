package org.rossjohnson.notification.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rossjohnson.notification.vera.VeraController;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class VeraNotificationControllerTest {

    private VeraNotificationController controller;

    @Before
    public void setUp() throws Exception {
        controller = new VeraNotificationController();
        controller.setDeviceMapFromConfig("arcade=100,lights=101");
    }

    @After
    public void tearDown() throws Exception {
        controller = null;
    }

    @Test
    public void testIndex_arcadeOn() throws Exception {
        VeraController vc = mock(VeraController.class);
        when(vc.isPowerOn("100")).thenReturn(true);
        controller.setVeraController(vc);

        String arcadeRetVal = controller.index("arcade");

        assertEquals("arcade is now on", arcadeRetVal);
        verify(vc).togglePower("100");
    }

    @Test
    public void testIndex_arcadeOff() throws Exception {
        VeraController vc = mock(VeraController.class);
        when(vc.isPowerOn("100")).thenReturn(false);

        controller.setVeraController(vc);

        assertEquals("arcade is now off", controller.index("arcade"));
        verify(vc).togglePower("100");
    }

    @Test
    public void testIndex_nonExistentDevice() throws Exception {
        assertEquals("No such device: foo", controller.index("foo"));
    }

    @Test
    public void testQuery_LightsOff() throws Exception {
        VeraController vc = mock(VeraController.class);
        when(vc.isPowerOn("101")).thenReturn(false);
        controller.setVeraController(vc);

        String queryRetVal = controller.query("lights");
        assert (queryRetVal.startsWith("lights is off"));
        assert (queryRetVal.contains("Turn on</button>"));
    }

    @Test
    public void testQuery_LightsOn() throws Exception {
        VeraController vc = mock(VeraController.class);
        when(vc.isPowerOn("101")).thenReturn(true);
        controller.setVeraController(vc);

        String queryRetVal = controller.query("lights");
        assert (queryRetVal.startsWith("lights is on"));
        assert (queryRetVal.endsWith("Turn off</button></form>"));
    }

    @Test
    public void testQuery_nonExistentDevice() throws Exception {
        assertEquals("No such device: foo", controller.query("foo"));
    }
}