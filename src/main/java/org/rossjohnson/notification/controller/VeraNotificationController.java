package org.rossjohnson.notification.controller;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.rossjohnson.notification.vera.VeraController;
import org.rossjohnson.notification.vera.VeraControllerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private VeraController controller;

    private static Logger log = LoggerFactory.getLogger(VeraNotificationController.class);

    @RequestMapping("/vera-toggle")
    public String index(@RequestParam(value = "device", defaultValue = "arcade") String device) {

        init();
        String deviceId = deviceNameToIdMap.get(device);
        if (deviceId == null) {
            return "No such device: " + device;
        }
        controller.togglePower(deviceId);
        return device + " is now " + (controller.isPowerOn(deviceId) ? "on" : "off");
    }

    @RequestMapping("/vera-query")
    public String query(@RequestParam(value = "device", defaultValue = "arcade") String device) {

        init();
        String deviceId = deviceNameToIdMap.get(device);
        if (deviceId == null) {
            return "No such device: " + device;
        }
        boolean powerOn = controller.isPowerOn(deviceId);
        return String.format(
                "%s is %s<p/><form action=\"/vera-toggle\"><button type=\"submit\">Turn %s</button></form>",
                device,
                powerOn ? "on" : "off",
                powerOn ? "off" : "on");
    }

    public void init() {
        if (deviceNameToIdMap == null) {
            deviceNameToIdMap = Splitter.on(",").withKeyValueSeparator("=").split(deviceMapFromConfig);
            log.info(String.format("Initialized Vera Device Map with device/IDs: %s",
                    Joiner.on(", ").withKeyValueSeparator("->").join(deviceNameToIdMap)));
        }
    }

    public void setVeraController(VeraController veraController) {
        controller = veraController;
    }

    public void setDeviceMapFromConfig(String deviceMapFromConfig) {
        this.deviceMapFromConfig = deviceMapFromConfig;
    }
}
