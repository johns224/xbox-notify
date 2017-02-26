package org.rossjohnson.vera;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class ArcadeControllerImpl implements ArcadeController {

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
		String response = connect(statusUrl);
		return "1".equals(response);
	}

	@Override
	public void toggleArcadePower() {
		log("Toggle arcade call success: " + (isArcadePowerOn() ? turnAcadeOff() : turnArcadeOn()));
	}

	boolean turnArcadeOn() {
		return connect(baseToggleUrl + "1") != null;
	}

	boolean turnAcadeOff() {
		return connect(baseToggleUrl + "0") != null;
	}

	static void log(String message) {
		System.out.println(String.format("[%s] %s", new Date(), message));
	}

	public static void main(String[] args) throws Exception {
		ArcadeControllerImpl ac = new ArcadeControllerImpl("192.168.1.180", "101");
		ac.toggleArcadePower();
		log("Arcade on? " + ac.isArcadePowerOn());
	}

	private String connect(String connectUrl) {
		try {
			log("Executing: " + connectUrl);
			URL urlObj = new URL(connectUrl);
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			conn.setRequestMethod("GET");
			int statusCode = conn.getResponseCode();
			if (statusCode != HttpURLConnection.HTTP_OK) {
				log("Method failed: ");
			}
			else {
				return getBody(conn);
			}
		}
		catch (Exception e) {
			log("Error hitting " + connectUrl);
			e.printStackTrace();
		}
		return null;
	}

	private String getBody(HttpURLConnection conn) throws IOException {
		BufferedReader in = null;
		StringBuffer response = null;
		try {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
		}
		finally {
			if (in != null) in.close();
		}
		return response.toString();
	}
}
