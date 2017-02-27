package org.rossjohnson.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    public HttpClient() {
    }

    public String getResponse(String connectUrl) {
        try {
            URL urlObj = new URL(connectUrl);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");
            int statusCode = conn.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return "Method failed with code " + statusCode;
            }
            else {
                return getBody(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error hitting " + connectUrl;
        }
    }

    public String getBody(HttpURLConnection conn) throws IOException {
        BufferedReader in = null;
        StringBuffer response = null;
        try {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } finally {
            if (in != null) in.close();
        }
        return response.toString();
    }
}