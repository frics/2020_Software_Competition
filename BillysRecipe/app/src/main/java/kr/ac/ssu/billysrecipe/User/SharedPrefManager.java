package kr.ac.ssu.billysrecipe.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefManager {
    private static final String TAG = SharedPreferences.class.getSimpleName();
    private static final String SHARED_PREF_NAME = "userPreference";
    public static final String KEY_ID="userId";
    public static final String KEY_NICKNAME = "userNickname";

    //서버 Mysql REF_DB에 저장되어 있는 user의 냉장고 DB table 이름
    public static final String KEY_REF_DB = "userRefDB";


    private static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String getString(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(key, null);
        return value;
    }

    public static void userSignin(Context context, String id, String nickname, String dbname){
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Log.d("프리 메니저", id+", "+nickname+", "+ dbname);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NICKNAME, nickname);
        editor.putString(KEY_REF_DB, dbname);
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        Log.d(TAG, "기 로그인 확인중");
        SharedPreferences sharedPreferences = getPreferences(context);
        String id = sharedPreferences.getString(KEY_ID,null);
        return id != null;
    }

    public static void logout(Context context) {
        SharedPreferences sharedPreferences =  getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        context.startActivity(new Intent(context, SignInActivity.class));
    }
}

