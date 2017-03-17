package org.rossjohnson.notification.vera;

/**
 * Created by ross on 3/10/17.
 */
public interface VeraController {
    boolean isPowerOn(String deviceId);

    void togglePower(String deviceId);
}
