package org.rossjohnson.notification.controller;

import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


@RestController
public class KodiNotificationController {

    private Map<String, String> urlMap;

    @Value("${kodi.instances}")
    private String urlMapFromConfig;

    private static Logger log = LoggerFactory.getLogger(KodiNotificationController.class);


    @RequestMapping("/kodi-notify")
    public String index(@RequestParam(value = "title", defaultValue = "Title") String title,
                        @RequestParam(value = "message", defaultValue = "Hello World") String message,
                        @RequestParam(value = "location", defaultValue = "theater") String location)
            throws IOException, InterruptedException {


        init();
        String baseUrl = urlMap.get(location);
        return sendNotification(title, message, baseUrl);
    }

    private void init() {
        if (urlMap == null) {
            urlMap = Splitter.on(",").withKeyValueSeparator("=").split(urlMapFromConfig);
        }
    }

    private String sendNotification(String title, String message, String url) {
        url = url + "/jsonrpc?request=" +
                URLEncoder.encode("{ \"jsonrpc\": \"2.0\", \"method\": \"GUI.ShowNotification\", \"params\": { \"title\": \"" +
                        title + "\", \"message\": \"" + message + "\" }, \"id\": 1 }");

        try {
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");
            int statusCode = conn.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return "Attempt to send message to " + url + " failed with response code " + statusCode + ":\n" + conn.getResponseMessage();
            }
            return conn.getResponseMessage();
        } catch (IOException e) {
            log.error("Error sending Kodi notification to " + url + ":\n", e);
            e.printStackTrace();
            return "Error sending Kodi notification to " + url + "<p/>" + e.getMessage();
        }
    }

}
