package org.rossjohnson.notification.vera;


import org.rossjohnson.notification.http.SimpleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
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

    @Value("${vera.ip}")
    private String veraIPAddress;


	public VeraControllerImpl() {
		httpClient = new SimpleHttpClient();
	}

    @Override
    public void togglePower(String deviceId) {
        log.debug("Toggle power call success: " +
                (isPowerOn(deviceId) ? turnPowerOff(deviceId) : turnPowerOn(deviceId)));
    }

	@Override
	public boolean isPowerOn(String deviceId) {
        return "1".equals(httpClient.getResponse(String.format(STATUS_BASE_URL, veraIPAddress) + "&DeviceNum=" + deviceId));
	}

	boolean turnPowerOn(String deviceId) {
        return httpClient.getResponse(String.format(TOGGLE_BASE_URL, veraIPAddress) + "1&DeviceNum=" + deviceId) != null;
	}

	boolean turnPowerOff(String deviceId) {
        return httpClient.getResponse(String.format(TOGGLE_BASE_URL, veraIPAddress) + "0&DeviceNum=" + deviceId) != null;
	}

	public static void main(String[] args) throws Exception {
		VeraController ac = new VeraControllerImpl();
		ac.togglePower("101");
		log.info("Power on? " + ac.isPowerOn("101"));
	}

}
