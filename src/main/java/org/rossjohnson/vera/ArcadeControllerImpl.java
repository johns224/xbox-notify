package org.rossjohnson.vera;


import org.rossjohnson.http.SimpleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArcadeControllerImpl implements ArcadeController {

	public static final String STATUS_URL =
			"http://%s:3480/data_request?id=variableget&DeviceNum=%s&serviceId=urn:upnp-org:serviceId:SwitchPower1&Variable=Status";
	public static final String TOGGLE_BASE_URL =
			"http://%s:3480/data_request?id=action&DeviceNum=%s&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=";

    private static Logger log = LoggerFactory.getLogger(ArcadeControllerImpl.class);
	private final SimpleHttpClient httpClient;
	private String statusUrl;
	private String baseToggleUrl;

	public ArcadeControllerImpl(String veraIPAddress, String arcadeDeviceId) {
		httpClient = new SimpleHttpClient();
		statusUrl = String.format(STATUS_URL, veraIPAddress, arcadeDeviceId);
		baseToggleUrl = String.format(TOGGLE_BASE_URL, veraIPAddress, arcadeDeviceId);
	}

	@Override
	public boolean isArcadePowerOn() {
		return "1".equals(httpClient.getResponse(statusUrl));
	}

	@Override
	public void toggleArcadePower() {
		log.debug("Toggle arcade call success: " + (isArcadePowerOn() ? turnAcadeOff() : turnArcadeOn()));
	}

	boolean turnArcadeOn() {
        return httpClient.getResponse(baseToggleUrl + "1") != null;
	}

	boolean turnAcadeOff() {
		return httpClient.getResponse(baseToggleUrl + "0") != null;
	}

	public static void main(String[] args) throws Exception {
		ArcadeControllerImpl ac = new ArcadeControllerImpl("192.168.1.180", "101");
		ac.toggleArcadePower();
		log.info("Arcade on? " + ac.isArcadePowerOn());
	}

}
