package com.pushnotification.controller;

import com.pushnotification.dto.IncludePlayerIds;
import net.minidev.json.JSONArray;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Create by Weslei Dias.
 **/
public class PushNotificationOptions {

    public static final String REST_API_KEY = "NDc3M2ViODAtZWJlMy00ZDJiLTk3NmUtMzI2NGJjNDVhODZi";
    public static final String APP_ID = "ed29332e-d5ca-4920-a17b-f502aaed2c98";

    public static void sendMessageToAllUsers(String message) {
        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization",
                "Basic " + REST_API_KEY);//REST API
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                + "\"app_id\": \"" + APP_ID + "\","
                + "\"included_segments\": [\"All\"],"
                + "\"data\": {\"foo\": \"bar\"},"
                + "\"contents\": {\"en\": \"" + message + "\"}"
                + "}";


            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            jsonResponse = mountResponseRequest(con, httpResponse);
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static String mountResponseRequest(HttpURLConnection con, int httpResponse) throws IOException {
        String jsonResponse;
        if (httpResponse >= HttpURLConnection.HTTP_OK
            && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
        } else {
            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
        }
        return jsonResponse;
    }

    public static void sendMessageToUser(String message, IncludePlayerIds userIds) {
        userIds.getInclude_player_ids().forEach(userId -> {


            try {
                String jsonResponse;
//                JSONArray playerId = new JSONArray();
//                userIds.getInclude_player_ids().forEach(userId -> {
//                    playerId.appendElement(userId);
//                });

                URL url = new URL("https://onesignal.com/api/v1/notifications");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Authorization", REST_API_KEY);
                con.setRequestMethod("POST");

                String strJsonBody = "{"
                    + "\"app_id\": \"" + APP_ID + "\","
                    + "\"include_player_ids\": [\"" + userId + "\"],"
                    + "\"data\": {\"foo\": \"bar\"},"
                    + "\"contents\": {\"en\": \"" + message + "\"}"
                    + "}";


                System.out.println("strJsonBody:\n" + strJsonBody);

                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                con.setFixedLengthStreamingMode(sendBytes.length);

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(sendBytes);

                int httpResponse = con.getResponseCode();
                System.out.println("httpResponse: " + httpResponse);

                jsonResponse = mountResponseRequest(con, httpResponse);
                System.out.println("jsonResponse:\n" + jsonResponse);

            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
