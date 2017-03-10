package org.rossjohnson.notification.vera;


import org.rossjohnson.notification.http.SimpleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VeraController {

	public static final String STATUS_BASE_URL =
			"http://%s:3480/data_request?id=variableget&DeviceNum=%s" +
					"&serviceId=urn:upnp-org:serviceId:SwitchPower1" +
					"&Variable=Status";
	public static final String TOGGLE_BASE_URL =
			"http://%s:3480/data_request?id=action&DeviceNum=%s" +
					"&serviceId=urn:upnp-org:serviceId:SwitchPower1" +
					"&action=SetTarget" +
                    "&newTargetValue=";

    private static Logger log = LoggerFactory.getLogger(VeraController.class);
	private final SimpleHttpClient httpClient;
	private String statusUrl;
	private String toggleUrl;

	public VeraController(String veraIPAddress, String deviceId) {
		httpClient = new SimpleHttpClient();
		statusUrl = String.format(STATUS_BASE_URL, veraIPAddress, deviceId);
		toggleUrl = String.format(TOGGLE_BASE_URL, veraIPAddress, deviceId);
	}

	public boolean isPowerOn() {
		return "1".equals(httpClient.getResponse(statusUrl));
	}

	public void togglePower() {
		log.debug("Toggle power call success: " + (isPowerOn() ? turnPowerOff() : turnPowerOn()));
	}

	boolean turnPowerOn() {
        return httpClient.getResponse(toggleUrl + "1") != null;
	}

	boolean turnPowerOff() {
		return httpClient.getResponse(toggleUrl + "0") != null;
	}

	public static void main(String[] args) throws Exception {
		VeraController ac = new VeraController("192.168.1.180", "101");
		ac.togglePower();
		log.info("Power on? " + ac.isPowerOn());
	}

}
