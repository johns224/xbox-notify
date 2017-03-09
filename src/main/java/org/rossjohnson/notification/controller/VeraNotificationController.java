package org.rossjohnson.notification.controller;

import org.rossjohnson.notification.vera.VeraController;
import org.rossjohnson.notification.vera.ArcadeController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class VeraNotificationController {

    @Value("${vera.ip}")
    private String veraIPAddress;

    @Value("${arcade.deviceId}")
    private String arcadeDeviceId;

    private VeraController arcadeController;

    @RequestMapping("/vera-toggle")
    public String index(@RequestParam(value = "device", defaultValue = "arcade") String device) {

        if ("arcade".equals(device)) {
            init();
            arcadeController.togglePower();
            return "Arcade is now " + (arcadeController.isPowerOn() ? "on" : "off");
        }
        return "";
    }

    @RequestMapping("/vera-query")
    public String query(@RequestParam(value = "device", defaultValue = "arcade") String device) {

        if ("arcade".equals(device)) {
            init();
            boolean arcadePowerOn = arcadeController.isPowerOn();
            return String.format(
                    "%s is %s<p/><form action=\"/vera-toggle\"><button type=\"submit\">Turn %s</button></form>",
                    device,
                    arcadePowerOn ? "on" : "off",
                    arcadePowerOn ? "off" : "on"
            );
        }
        return "";
    }

    public void init() {
        if (arcadeController == null) {
            arcadeController = new ArcadeController(veraIPAddress, arcadeDeviceId);
        }
    }

}
