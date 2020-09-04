package kr.ac.ssu.myrecipe.ServerConnect;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class RequestHandler {
    private static final String TAG = "HTTP";

    //this method will send a post request to the specified url
    //in this app we are using only post request
    //in the hashmap we have the data to be sent to the server in keyvalue pairs
    public String sendPostRequest(String requestURL, HashMap<String, String> postDataParams) {
        URL url;

        StringBuilder sb = new StringBuilder();
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, StandardCharsets.UTF_8));
            //추가코드(param null pointer exception발생
            if(postDataParams != null)
                writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            Log.d(TAG, String.valueOf(conn.getResponseCode()));
            Log.d(TAG, "http 통신 직전");
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.d(TAG, "http 통신 성공");
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;

                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
            }
        }catch (MalformedInputException e){
            Log.e("Error", "wrong url");
        } catch (SocketTimeoutException e) {
            Log.e("Error", "Timeout");
        }catch (IOException e){
            Log.e("Error", "IO Exception error");
        }
        Log.d(TAG, sb.toString());
        return sb.toString();
    }


    //this method is converting keyvalue pairs data into a query string as needed to send to the server
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

}