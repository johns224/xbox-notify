package org.rossjohnson.notification.controller;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.rossjohnson.notification.vera.VeraController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class VeraNotificationController {

    @Value("${vera.ip}")
    private String veraIPAddress;

    @Value("${vera.device.names.ids}")
    private String deviceMapFromConfig;

    private Map<String, String> deviceNameToIdMap;
    private static Logger log = LoggerFactory.getLogger(VeraNotificationController.class);

    @RequestMapping("/vera-toggle")
    public String index(@RequestParam(value = "device", defaultValue = "arcade") String device) {

        init();
        VeraController controller = getController(deviceNameToIdMap.get(device));
        controller.togglePower();
        return "Arcade is now " + (controller.isPowerOn() ? "on" : "off");
    }

    @RequestMapping("/vera-query")
    public String query(@RequestParam(value = "device", defaultValue = "arcade") String device) {

        init();
        boolean arcadePowerOn = getController(deviceNameToIdMap.get(device)).isPowerOn();
        return String.format(
                "%s is %s<p/><form action=\"/vera-toggle\"><button type=\"submit\">Turn %s</button></form>",
                device,
                arcadePowerOn ? "on" : "off",
                arcadePowerOn ? "off" : "on"
        );
    }

    public void init() {
        if (deviceNameToIdMap == null) {
            deviceNameToIdMap = Splitter.on(",").withKeyValueSeparator("=").split(deviceMapFromConfig);
            log.info(String.format("Initialized Vera Device Map with device/IDs: %s",
                    Joiner.on(", ").withKeyValueSeparator("->").join(deviceNameToIdMap)));
        }
    }

    public VeraController getController(String deviceId) {
        return new VeraController(veraIPAddress, deviceId);
    }

}
