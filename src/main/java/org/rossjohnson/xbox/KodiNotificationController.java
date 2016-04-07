package org.rossjohnson.xbox;

import com.google.common.base.Splitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by ross on 4/6/16.
 */
@RestController
public class KodiNotificationController extends NotifcationController {

    private Map<String, String> urlMap;

    @Value("${kodi.instances}")
    private String urlMapFromConfig;

    @RequestMapping("/kodi-notify")
    public String index(@RequestParam(value = "title", defaultValue = "Title") String message,
                        @RequestParam(value = "message", defaultValue = "Hello World") String title,
                        @RequestParam(value = "location", defaultValue = "theater") String location)
            throws IOException, InterruptedException {

        if (urlMap == null) {
            init();
        }
        String baseUrl = urlMap.get(location);
        return sendNotification(title, message, baseUrl);
    }

    private void init() {
        urlMap = Splitter.on(",").withKeyValueSeparator("=").split(urlMapFromConfig);
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
        }
        catch (IOException e) {
            log("Error sending Kodi notification to " + url + ":\n" + e.getMessage());
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
