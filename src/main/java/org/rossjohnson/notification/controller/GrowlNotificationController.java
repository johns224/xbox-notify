package org.rossjohnson.notification.controller;

import com.google.code.jgntp.Gntp;
import com.google.code.jgntp.GntpApplicationInfo;
import com.google.code.jgntp.GntpClient;
import com.google.code.jgntp.GntpNotificationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;


@RestController
public class GrowlNotificationController  {

    @Value("${growl.ip}")
    private String growlIPAddress;

    @Value("${growl.port}")
    private String growlPort;

    @Value("${growl.password}")
    private String growlPassword;

    GntpClient client;
    private GntpNotificationInfo notificationInfo;

    private static Logger log = LoggerFactory.getLogger(GrowlNotificationController.class);


    @RequestMapping("/growl-notify")
    public String index(@RequestParam(value = "title", defaultValue = "Title") String title,
                        @RequestParam(value = "message", defaultValue = "Hello World") String message)
            throws IOException, InterruptedException {

        if (client == null || !client.isRegistered()) {
            init(growlIPAddress, Integer.parseInt(growlPort), growlPassword);
        }

        sendNotification(title, message);

        return "Sent " + message;
    }

    void sendNotification(String title, String message) {
        client.notify(Gntp.notification(notificationInfo, title)
                .text(message)
                .build());
    }

    void init(String ipAddress, int port, String password) throws InterruptedException {
        GntpApplicationInfo appInfo = Gntp.appInfo("Mac").build();
        notificationInfo = Gntp.notificationInfo(appInfo, "growl-notifier").build();
        log.info("Registering with Growl on " + ipAddress + ":" + port);
        Gntp gntp = Gntp.client(appInfo)
                .forAddress(new InetSocketAddress(ipAddress, port))
                .withPassword(password)
                .withoutRetry();
        client = gntp.build();
        client.register();
        client.waitRegistration(1L, TimeUnit.SECONDS);
    }

}
