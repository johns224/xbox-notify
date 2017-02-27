package org.rossjohnson.vera;


import org.rossjohnson.http.HttpClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;

public class ArcadeControllerImpl implements ArcadeController {

	private final HttpClient httpClient = new HttpClient();
	private String statusUrl;
	private String baseToggleUrl;

	public ArcadeControllerImpl(String veraIPAddress, String arcadeDeviceId) {

		statusUrl = String.format(
				"http://%s:3480/data_request?id=variableget&DeviceNum=%s&serviceId=urn:upnp-org:serviceId:SwitchPower1&Variable=Status",
				veraIPAddress,
				arcadeDeviceId
		);

		baseToggleUrl = String.format(
				"http://%s:3480/data_request?id=action&DeviceNum=%s&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=",
				veraIPAddress,
				arcadeDeviceId
		);
	}

	@Override
	public boolean isArcadePowerOn() {
		String response = httpClient.getResponse(statusUrl);
		return "1".equals(response);
	}

	@Override
	public void toggleArcadePower() {
		log("Toggle arcade call success: " + (isArcadePowerOn() ? turnAcadeOff() : turnArcadeOn()));
	}

	boolean turnArcadeOn() {
		return httpClient.getResponse(baseToggleUrl + "1") != null;
	}

	boolean turnAcadeOff() {
		return httpClient.getResponse(baseToggleUrl + "0") != null;
	}

	static void log(String message) {
		System.out.println(String.format("[%s] %s", new Date(), message));
	}

	public static void main(String[] args) throws Exception {
		ArcadeControllerImpl ac = new ArcadeControllerImpl("192.168.1.180", "101");
		ac.toggleArcadePower();
		log("Arcade on? " + ac.isArcadePowerOn());
	}

}
