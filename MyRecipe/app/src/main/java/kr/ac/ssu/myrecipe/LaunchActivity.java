package kr.ac.ssu.myrecipe;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import java.io.IOException;
import java.io.InputStreamReader;
import kr.ac.ssu.myrecipe.database.OpenRecipeListCSV;

public class LaunchActivity extends AppCompatActivity {
    private static final String TAG = LaunchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        Handler handler = new Handler();
        /* 스플래시 화면이 표시되는 시간을 설정(ms) */
        int SPLASH_DISPLAY_TIME = 3000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 레시피 리스트 내부 동기화
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(getResources().openRawResource(R.raw.recipelist));
                    OpenRecipeListCSV.readDataFromCsv(inputStreamReader);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "메인으로");
                startActivity(new Intent(getApplication(), MainActivity.class));

                /* 런 액티비티를 스택에서 제거. */
                LaunchActivity.this.finish();
            }
        }, SPLASH_DISPLAY_TIME);

    }

    @Override
    public void onBackPressed() {
        /* 스플래시 화면에서 뒤로가기 기능 제거. */
    }
}
