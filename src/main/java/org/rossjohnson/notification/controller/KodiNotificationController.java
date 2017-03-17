package org.rossjohnson.notification.controller;

import com.google.common.base.Splitter;
import org.rossjohnson.notification.http.SimpleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;


@RestController
public class KodiNotificationController {

    private Map<String, String> urlMap;

    @Value("${kodi.instances}")
    private String urlMapFromConfig;

    private static final String BASE_NOTIFICATION_URL = "%s/jsonrpc?request=";
    private static final String BASE_QUERY_PARAMS = "{ \"jsonrpc\": \"2.0\", \"method\": \"GUI.ShowNotification\", " +
                    "\"params\": { \"title\": \"%s\", \"message\": \"%s\" }, \"id\": 1 }";

    @RequestMapping("/kodi-notify")
    public String index(@RequestParam(value = "title", defaultValue = "Title") String title,
                        @RequestParam(value = "message", defaultValue = "Hello World") String message,
                        @RequestParam(value = "location", defaultValue = "theater") String location)
            throws IOException, InterruptedException {

        init();
        return sendNotification(title, message, urlMap.get(location));
    }

    private void init() {
        if (urlMap == null) {
            urlMap = Splitter.on(",").withKeyValueSeparator("=").split(urlMapFromConfig);
        }
    }

    public String sendNotification(String title, String message, String url) throws UnsupportedEncodingException {
        return new SimpleHttpClient().getResponse(
                getUrl(title, message, url)
        );
    }

    public String getUrl(String title, String message, String url) throws UnsupportedEncodingException {
        return String.format(BASE_NOTIFICATION_URL, url) +
                URLEncoder.encode(String.format(BASE_QUERY_PARAMS, title, message), "UTF-8");
    }

}
