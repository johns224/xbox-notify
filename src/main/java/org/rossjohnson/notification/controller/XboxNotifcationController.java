package org.rossjohnson.notification.controller;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class XboxNotifcationController {
	public static final String MESSAGES_URL = "https://xboxapi.com/v2/messages";
	private static Logger log = LoggerFactory.getLogger(XboxNotifcationController.class);

	@Value("${xbox.user.xuid}")
	private String xuid;

	@Value("${api.key}")
	private String apiKey;

	@RequestMapping("/xbox-notify")
	public String index(@RequestParam(value = "message", defaultValue = "Hello World") String message)
			throws IOException {

		String responseString = "OK";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(MESSAGES_URL);
		post.setHeader("X-Auth", apiKey);
		post.setHeader("Content-Type", "application/json");
		StringEntity entity = new StringEntity(String.format("{ \"to\": [\"%s\"], \"message\": \"%s\"}", xuid, message));
		entity.setContentType("application/json");
		post.setEntity(entity);

		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(post);
			StatusLine status = response.getStatusLine();
			responseString = status.toString();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				log.error("Method failed: " + status);
			} else {
				log.info("Sent message '" + message + "' to " + xuid + "@" + MESSAGES_URL);
			}
		} finally {
			if (response != null)
				response.close();
		}
		return responseString;
	}


}
