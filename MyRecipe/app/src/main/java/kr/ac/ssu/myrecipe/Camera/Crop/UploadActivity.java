package kr.ac.ssu.myrecipe.Camera.Crop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import kr.ac.ssu.myrecipe.MainActivity;
import kr.ac.ssu.myrecipe.R;

public class UploadActivity extends AppCompatActivity {

    private static final String TAG = UploadActivity.class.getSimpleName();
    static Bitmap mImage;

    private ImageView imageView;



    int serverResponseCode = 0;
    String upLoadServerUri = "http://13.209.6.94/upload_act.php";
    /**********  File Path *************/
    File mFile = null;
    String uploadFilePath=null;
    String uploadFileName = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upload);
        hideNavigationBar();

        imageView = ((ImageView) findViewById(R.id.resultImageView));

        Toolbar toolbar =  findViewById(R.id.upload_toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.bringToFront();

        Intent intent = getIntent();
        if (mImage != null) {
            imageView.setImageBitmap(mImage);
        } else {
            Uri imageUri = intent.getParcelableExtra("URI");
            if (imageUri != null) {
                imageView.setImageURI(imageUri);
            } else {
                Toast.makeText(this, "No image is set to show", Toast.LENGTH_LONG).show();
            }
        }

        //이미지 파일 경로 획득
        mFile = this.getExternalFilesDir(null);
        uploadFilePath = mFile+"/";
        uploadFileName = "pic_crop.jpg";

        new Thread(new Runnable() {
            public void run() {
                uploadFile(uploadFilePath + "" + uploadFileName);
            }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.cam_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        Intent intent;
        switch (curId) {
            case android.R.id.home:
                Log.e(TAG, "크롭으로 간다");
                Toast.makeText( this,"go to crop", Toast.LENGTH_SHORT).show();
                releaseBitmap();
                finish();
                onBackPressed();
                //startActivity(new Intent(this, CropActivity.class));
                break;

            case R.id.go_to_main:
                Log.e(TAG, "메인으로 간다!!!!");
                releaseBitmap();
                finish();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    private void hideNavigationBar() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.d(TAG, "Turning immersive mode mode off. ");
        } else {
            Log.d(TAG, "Turning immersive mode mode on.");
        }
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }



    private void releaseBitmap() {
        if (mImage != null) {
            mImage.recycle();
            mImage = null;
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
        File sourceFile = new File("/storage/emulated/0/Android/data/kr.ac.ssu.myrecipe/files/"+"pic_crop.jpg");

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