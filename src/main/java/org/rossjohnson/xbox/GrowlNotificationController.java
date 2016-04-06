package org.rossjohnson.xbox;

import com.google.code.jgntp.Gntp;
import com.google.code.jgntp.GntpApplicationInfo;
import com.google.code.jgntp.GntpClient;
import com.google.code.jgntp.GntpNotificationInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by ross on 4/5/16.
 */
@RestController
public class GrowlNotificationController extends NotifcationController {

    @Value("${growl.ip}")
    private String growlIPAddress;

    @Value("${growl.port}")
    private String growlPort;

    @Value("${growl.password}")
    private String growlPassword;

    GntpClient client;
    private GntpNotificationInfo notificationInfo;

    @RequestMapping("/growl-notify")
    public String index(@RequestParam(value = "title", defaultValue = "Title") String message,
                        @RequestParam(value = "message", defaultValue = "Hello World") String title)
            throws IOException, InterruptedException {

        if (client == null || !client.isRegistered()) {
            init(growlIPAddress, Integer.parseInt(growlPort), growlPassword);
            client.waitRegistration(1L, TimeUnit.SECONDS);
        }

        sendNotification(title, message);

        return "Sent " + message;
    }

    void sendNotification(String title, String message) {
        client.notify(Gntp.notification(notificationInfo, title)
                .text(message)
                .build());
    }

    void init(String ipAddress, int port, String password) {
        GntpApplicationInfo appInfo = Gntp.appInfo("Mac").build();
        notificationInfo = Gntp.notificationInfo(appInfo, "growl-notifier").build();
        log("Registering with Growl on " + ipAddress + ":" + port);
        Gntp gntp = Gntp.client(appInfo)
                .forAddress(new InetSocketAddress(ipAddress, port))
                .withPassword(password)
                .withoutRetry();
        client = gntp.build();
        client.register();
    }

}
