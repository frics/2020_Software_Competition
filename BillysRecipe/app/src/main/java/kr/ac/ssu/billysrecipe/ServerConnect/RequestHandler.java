package kr.ac.ssu.billysrecipe.ServerConnect;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

    public final static int IMG_TRANSFER = 1;
    public final static int DATA_TRANSFER = 0;
    private Context mContext;

    public RequestHandler(){
        super();
    }
    public RequestHandler(Context context){
        super();
        this.mContext = context;
        Log.e(TAG, "context 설정");
    }


    //this method will send a post request to the specified url
    //in this app we are using only post request
    //in the hashmap we have the data to be sent to the server in keyvalue pairs
    public String sendPostRequest(String requestURL, HashMap<String, String> postDataParams, int flag) {

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

            if(flag == IMG_TRANSFER){ //여긴 이미지 전송

                Log.e(TAG,"이미지 전송 준비");
                DataOutputStream dos;
                FileInputStream fileInputStream;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1024 * 1024; //1 * 1024 * 1024

                File sourceFile = new File(mContext.getExternalFilesDir(null), "pic_crop.jpg");
                if (!sourceFile.isFile()) {
                    Log.e("uploadFile", "Source File not exist :"
                            +sourceFile);
                    return "";
                }
                conn.setUseCaches(false);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                //이미지 파일 데이터 타입 php code와 동일하게
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                //이미지 파일 변수 명 : uploaded_file(php 코드와 동일하게 맞춰야함)
                conn.setRequestProperty("uploaded_file", sourceFile+"");
                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + sourceFile + "" + lineEnd);
                dos.writeBytes(lineEnd);

                //FileInputStream에 파일을 써준다
                fileInputStream = new FileInputStream(sourceFile);

                // 최대 사이즈만큼의 버퍼 생성
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                //파일 데이터를 읽고 형식에 맞게 써줌
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }
                //데이터 전송해줌
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                //객체 다 닫아줌
                fileInputStream.close();
                dos.flush();
                dos.close();
            }

            //여기가 그냥 데이터 전송
            else {

                OutputStream os = conn.getOutputStream();

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, StandardCharsets.UTF_8));
                //추가코드(param null pointer exception발생
                if (postDataParams != null)
                    writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();
            }

            Log.d(TAG, conn.getResponseCode()+" : "+ conn.getResponseMessage());
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