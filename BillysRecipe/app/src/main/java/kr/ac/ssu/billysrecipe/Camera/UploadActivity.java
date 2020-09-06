package kr.ac.ssu.billysrecipe.Camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.Toolbar;

import kr.ac.ssu.billysrecipe.MainActivity;
import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.ServerConnect.ImageTransfer;

public class UploadActivity extends AppCompatActivity  {

    private static final String TAG = UploadActivity.class.getSimpleName();
    static Bitmap mImage;

    private Context mContext;

    private ImageView imageView;
    private Button recapture;
    private Button getReceipt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        //툴바 설정
        Toolbar toolbar =  findViewById(R.id.upload_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.bringToFront();

        //풀스크린 모드 설정
        hideNavigationBar();

        mContext = this;

        imageView = findViewById(R.id.resultImageView);
        recapture = findViewById(R.id.recapture_btn);
        getReceipt = findViewById(R.id.get_receipt_btn);


        //다시 찍기 버튼 -> 카메라 엑티비티로 넘어감
        recapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(mContext, CameraActivity.class);
                startActivity(intent);
            }
        });

        //서버로 이미지 전송 및 영수증 데이터 객체 획득
        getReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "로딩 다이얼로그 실행중(데이터 받으면 종료)");
                final AppCompatDialog progressDialog = new AppCompatDialog(mContext);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.setContentView(R.layout.loading_dialog);
                progressDialog.show();
                final ImageView img_loading_frame = progressDialog.findViewById(R.id.iv_frame_loading);
                final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
                img_loading_frame.post(new Runnable() {
                    @Override
                    public void run() {
                        frameAnimation.start();
                    }
                });
                final ImageView text_loading_frame = progressDialog.findViewById(R.id.tv_progress_message);
                final AnimationDrawable textAnimation = (AnimationDrawable) text_loading_frame.getBackground();
                img_loading_frame.post(new Runnable() {
                    @Override
                    public void run() {
                        textAnimation.start();
                    }
                });
                ImageTransfer imageTransfer = new ImageTransfer(mContext);
                imageTransfer.execute();
                finish();
                startActivity(new Intent(mContext, OCRFailActivity.class));
            }
        });

        if (mImage != null) {
            imageView.setImageBitmap(mImage);
        }


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
                releaseBitmap();
                finish();
                onBackPressed();
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

}