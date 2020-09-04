package kr.ac.ssu.myrecipe.Camera;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Audio;
import com.otaliastudios.cameraview.controls.Engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import kr.ac.ssu.myrecipe.R;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";
    private CameraView cameraView;
    private ImageButton captureBtn;
    private Context mContext;
    File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNavigationBar();
        setContentView(R.layout.activity_camera);
        mContext = this;
        cameraView = findViewById(R.id.camera);
        cameraView.setLifecycleOwner(this);
        cameraView.setEngine(Engine.CAMERA1);
        cameraView.setAudio(Audio.OFF);
        Engine engine = cameraView.getEngine();

        mFile = new File(getExternalFilesDir(null), "pic.jpg");


        captureBtn = findViewById(R.id.capture_btn);

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.takePictureSnapshot();
            }
        });

        Toolbar toolbar =  findViewById(R.id.cam_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        toolbar.bringToFront();


        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(PictureResult result) {
                // Picture was taken!
                // If planning to show a Bitmap, we will take care of
                // EXIF rotation and background threading for you...

                final int maxWidth = result.getSize().getWidth();
                final int maxHeight = result.getSize().getHeight();


                System.out.println("Max Width: " + maxWidth + " Max Height: " + maxHeight);

                result.toBitmap(maxWidth, maxHeight, new BitmapCallback() {
                    @Override
                    public void onBitmapReady(@Nullable Bitmap bitmap) {

                        ImageSaver imageSaver = new ImageSaver(bitmap, mFile);
                        imageSaver.run();
                        finish();
                        Intent intent = new Intent(mContext, CropActivity.class);
                        startActivity(intent);

                    }


                });
            }

        });




        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.takePictureSnapshot();
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.cam_menu, menu);
        return true;
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        if (curId == R.id.go_to_main) {
            finish();
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


    private static class ImageSaver implements Runnable {
        /**
         * The JPEG image
         */
        private final Bitmap mImage;
        /**
         * The file we save the image into.
         */
        private final File mFile;

        ImageSaver(Bitmap image, File file) {
            mImage = image;
            mFile = file;
        }

        @Override
        public void run() {

            //File fileCacheItem = mFile;
            FileOutputStream output = null;
            try {
                //  fileCacheItem.createNewFile();
                output = new FileOutputStream(mFile);
                //bitmap  이미지를 JPG 형식으로 압축해서 저장
                mImage.compress(Bitmap.CompressFormat.JPEG, 100, output);
            }
            catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(null != output) {
                    try {
                        Log.e("test", "성공성공성공성공");
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}