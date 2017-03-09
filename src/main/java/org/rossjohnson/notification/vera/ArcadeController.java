package org.rossjohnson.notification.vera;


import org.rossjohnson.notification.http.SimpleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArcadeController implements VeraController {

	public static final String STATUS_BASE_URL =
			"http://%s:3480/data_request?id=variableget&DeviceNum=%s" +
					"&serviceId=urn:upnp-org:serviceId:SwitchPower1" +
					"&Variable=Status";
	public static final String TOGGLE_BASE_URL =
			"http://%s:3480/data_request?id=action&DeviceNum=%s" +
					"&serviceId=urn:upnp-org:serviceId:SwitchPower1" +
					"&action=SetTarget" +
                    "&newTargetValue=";

    private static Logger log = LoggerFactory.getLogger(ArcadeController.class);
	private final SimpleHttpClient httpClient;
	private String statusUrl;
	private String toggleUrl;

	public ArcadeController(String veraIPAddress, String arcadeDeviceId) {
		httpClient = new SimpleHttpClient();
		statusUrl = String.format(STATUS_BASE_URL, veraIPAddress, arcadeDeviceId);
		toggleUrl = String.format(TOGGLE_BASE_URL, veraIPAddress, arcadeDeviceId);
	}

	@Override
	public boolean isPowerOn() {
		return "1".equals(httpClient.getResponse(statusUrl));
	}

	@Override
	public void togglePower() {
		log.debug("Toggle power call success: " + (isPowerOn() ? turnAcadeOff() : turnArcadeOn()));
	}

	boolean turnArcadeOn() {
        return httpClient.getResponse(toggleUrl + "1") != null;
	}

	boolean turnAcadeOff() {
		return httpClient.getResponse(toggleUrl + "0") != null;
	}

	public static void main(String[] args) throws Exception {
		ArcadeController ac = new ArcadeController("192.168.1.180", "101");
		ac.togglePower();
		log.info("Arcade on? " + ac.isPowerOn());
	}

}
