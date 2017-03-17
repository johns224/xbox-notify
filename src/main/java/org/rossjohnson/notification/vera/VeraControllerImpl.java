package org.rossjohnson.notification.vera;


import org.rossjohnson.notification.http.SimpleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VeraControllerImpl implements VeraController {

	public static final String STATUS_BASE_URL =
			"http://%s:3480/data_request?id=variableget" +
					"&serviceId=urn:upnp-org:serviceId:SwitchPower1" +
					"&Variable=Status"; // needs &DeviceNum=%s
	public static final String TOGGLE_BASE_URL =
			"http://%s:3480/data_request?id=action" +
					"&serviceId=urn:upnp-org:serviceId:SwitchPower1" +
					"&action=SetTarget" +
                    "&newTargetValue="; // needs &DeviceNum=%s


    private static Logger log = LoggerFactory.getLogger(VeraControllerImpl.class);
	private final SimpleHttpClient httpClient;
	private String statusUrl;
	private String toggleUrl;


	public VeraControllerImpl(String veraIPAddress) {
		httpClient = new SimpleHttpClient();
		statusUrl = String.format(STATUS_BASE_URL, veraIPAddress);
		toggleUrl = String.format(TOGGLE_BASE_URL, veraIPAddress);
	}

    @Override
    public void togglePower(String deviceId) {
        log.debug("Toggle power call success: " +
                (isPowerOn(deviceId) ? turnPowerOff(deviceId) : turnPowerOn(deviceId)));
    }

	@Override
	public boolean isPowerOn(String deviceId) {
        return "1".equals(httpClient.getResponse(statusUrl + "&DeviceNum=" + deviceId));
	}

	boolean turnPowerOn(String deviceId) {
        return httpClient.getResponse(toggleUrl + "1&DeviceNum=" + deviceId) != null;
	}

	boolean turnPowerOff(String deviceId) {
        return httpClient.getResponse(toggleUrl + "0&DeviceNum=" + deviceId) != null;
	}

	public static void main(String[] args) throws Exception {
		VeraController ac = new VeraControllerImpl("192.168.1.180");
		ac.togglePower("101");
		log.info("Power on? " + ac.isPowerOn("101"));
	}

}
