package kr.ac.ssu.myrecipe;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStreamReader;

import kr.ac.ssu.myrecipe.User.SharedPrefManager;
import kr.ac.ssu.myrecipe.User.SignInActivity;
import kr.ac.ssu.myrecipe.database.OpenRecipeListCSV;
import kr.ac.ssu.myrecipe.MajorFragment.HomeFragment;
import kr.ac.ssu.myrecipe.recipe.RecipeOrderList;

public class LaunchActivity extends AppCompatActivity {
    private static final String TAG = LaunchActivity.class.getSimpleName();
    public final String PREFERENCE = "recentList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        int SPLASH_DISPLAY_TIME = 3000; // 스플래시 액티비티가 화면에 표시되는 시간 설정
        Handler handler = new Handler();
        final boolean isSignin = SharedPrefManager.isLoggedIn(this);

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

                // 최근 찾아본 레시피 목록 동기화
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                HomeFragment.recent_recipes = pref.getString("recentlist", "");

                // 완성도 순 레시피리스트 세팅
                RecipeOrderList.RenewOrder(getApplicationContext());

                if (isSignin) {
                    Log.d(TAG, "메인 엑티비티로 이동");
                    startActivity(new Intent(getApplication(), MainActivity.class));
                } else {
                    Log.d(TAG, "로그인 엑티비티로 이동");
                    startActivity(new Intent(getApplication(), SignInActivity.class));
                }

                // 런 액티비티를 스택에서 제거.
                LaunchActivity.this.finish();
            }
        }, SPLASH_DISPLAY_TIME);
    }
}
