package org.rossjohnson.xbox;

import org.rossjohnson.vera.ArcadeController;
import org.rossjohnson.vera.ArcadeControllerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class VeraNotificationController extends NotifcationController {

    @Value("${vera.ip}")
    private String veraIPAddress;

    @Value("${arcade.deviceId}")
    private String arcadeDeviceId;

    @RequestMapping("/vera-toggle")
    public String index(@RequestParam(value = "device", defaultValue = "arcade") String device) {

        if ("arcade".equals(device)) {
            ArcadeController arcadeController = new ArcadeControllerImpl(veraIPAddress, arcadeDeviceId);
            arcadeController.toggleArcadePower();
            return "Arcade is now " + (arcadeController.isArcadePowerOn() ? "on" : "off");
        }
        return "";
    }

}
