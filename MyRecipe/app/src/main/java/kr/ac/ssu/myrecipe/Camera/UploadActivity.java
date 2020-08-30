package kr.ac.ssu.myrecipe.Camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import kr.ac.ssu.myrecipe.R;

public class UploadActivity extends AppCompatActivity {

    private static final String TAG = "UploadActivity" ;
    ImageView imageView;
    ImageView scanView;



    int serverResponseCode = 0;
    /************* Php script path ****************/
    String upLoadServerUri = "http://13.209.6.94/upload_act.php";
    /**********  File Path *************/
    File mFile = null;
    String uploadFilePath=null;
    String uploadFileName = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        //이미지 파일 경로 획득
        mFile = this.getExternalFilesDir(null);
        uploadFilePath = mFile+"/";
        uploadFileName = "pic.jpg";

        new Thread(new Runnable() {
            public void run() {
                uploadFile(uploadFilePath + "" + uploadFileName);
            }
        }).start();
        scanView = findViewById(R.id.scan_view);
        Glide.with(this).load(R.drawable.scan).into(scanView);
        imageView=findViewById(R.id.upload_img);



    }
    //촬영한 사진으로 이미지뷰 생성
    //디렉터리 접근 -> 사진 유무 확인
    @Override
    public void onResume() {
        super.onResume();
        String fullPath = uploadFilePath+uploadFileName;
        Log.d(TAG, fullPath);
        File imgFile = new File(fullPath);
        //사진 유무 확인
        if(imgFile.exists())
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            int width = myBitmap.getWidth();
            int height = myBitmap.getHeight();
            Bitmap rotatedBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
            imageView.setImageBitmap(rotatedBitmap);
        }
    }

    public int uploadFile(String sourceFileUri) {

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024; //1 * 1024 * 1024
        File sourceFile = new File("/storage/emulated/0/Android/data/kr.ac.ssu.myrecipe/files/"+"pic.jpg");

        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File not exist :"
                    +uploadFilePath + "" + uploadFileName);
            return 0;
        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                //request 전송 방식 : POST
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                //이미지 파일 데이터 타입 php code와 동일하게
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                //이미지 파일 변수 명 : uploaded_file(php 코드와 동일하게 맞춰야함)
                conn.setRequestProperty("uploaded_file", sourceFileUri);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + sourceFileUri + "" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){
                    //파일 전송 성공시 메인 스레드(UI Thread)에 토스트 메시지 생성
                    runOnUiThread(new Runnable() {
                        public void run() {
                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +" http://13.209.6.94:8888/OCR/resource/OriImgPath/"
                                    +uploadFileName;
                            Toast.makeText(UploadActivity.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
                //해당 서버 url에 접근할 수 없는 exception
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(UploadActivity.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                //기타 다른 Exception 예외 처리
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(UploadActivity.this, "Got Exception : check the logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("Error", "Exception : " + e.getMessage(), e);
            }
            return serverResponseCode;
        } // End else block
    }
}
