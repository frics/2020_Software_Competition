package kr.ac.ssu.myrecipe.User;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import kr.ac.ssu.myrecipe.MainActivity;
import kr.ac.ssu.myrecipe.R;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG =  SignInActivity.class.getSimpleName();;
    private EditText editTextId;
    private EditText editTextPw;
    private Button signInBtn;
    private Button signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextId = findViewById(R.id.user_id);
        editTextPw = findViewById(R.id.user_password);
        signInBtn = findViewById(R.id.signin_submit);
        signUpBtn = findViewById(R.id.signup_btn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 userLogin();
             }
         });
        signUpBtn.setOnClickListener(this);


    }
    private void userLogin() {
        //first getting the values
        final String id = editTextId.getText().toString();
        final String password = editTextPw.getText().toString();

        Log.d(TAG, id+", "+password);
        //validating inputs
        if (TextUtils.isEmpty(id)) {
            editTextId.setError("Please enter your username");
            editTextId.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPw.setError("Please enter your password");
            editTextPw.requestFocus();
            return;
        }

        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("password", password);

                //returing the response
                Log.d(TAG, URLs.URL_LOGIN);
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    //converting response to json object
                    Log.d(TAG, "json object"+s);
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");
                        String nickname = userJson.getString("nickname");
                        String dbname = userJson.getString("mem_idx")+nickname;

                        Log.d(TAG, id+", "+nickname +", "+dbname);

                        //Preference에 정보 삽입
                        SharedPrefManager.userSignin(SignInActivity.this, id, nickname, dbname);

                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_passwd:{
                Log.d(TAG, "비밀번호 찾기(미구현)");
                break;
            }
            case R.id.signup_btn:{
                Log.d(TAG, "계정생성 액티비티로 넘어감");
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            }
        }
    }
}