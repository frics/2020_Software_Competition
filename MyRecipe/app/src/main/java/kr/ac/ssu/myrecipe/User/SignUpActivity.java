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

import kr.ac.ssu.myrecipe.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG =  SignUpActivity.class.getSimpleName();;
    private EditText editTextId;
    private EditText editTextPassword;
    private EditText editTextNickName;
    private Button signUpBtn;
    private Button signInBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextId = findViewById(R.id.regi_id);
        editTextPassword = findViewById(R.id.regi_password);
        editTextNickName = findViewById(R.id.regi_nickname);
        signUpBtn = findViewById(R.id.signup_submit);
        signInBtn = findViewById(R.id.signin_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             registerUser();
                                         }
                                     });
                signInBtn.setOnClickListener(this);

    }
    private void registerUser() {

        final String id = editTextId.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String nickname = editTextNickName.getText().toString().trim();


        Log.d(TAG, id+password+nickname);
        //first we will do the validations
        if (TextUtils.isEmpty(id)) {
            editTextId.setError("이메일을 입력해주세요.");
            editTextId.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(id).matches()) {
            editTextId.setError("이메일이 유효하지 않습니다.");
            editTextId.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("비밀번호를 입력해주세요");
            editTextPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(nickname)) {
            editTextNickName.setError("별명을 입력해주세요");
            editTextNickName.requestFocus();
            return;
        }

        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();

                params.put("id", id);
                params.put("password", password);
                params.put("nickname", nickname);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion

                try {
                    //converting response to json object
                    //converting response to json object
                    Log.d(TAG, "json object"+s);
                    JSONObject obj = new JSONObject(s);


                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object

                    } else {
                        if(obj.getString("message").equals("이미 등록된 이메일입니다")) {
                            editTextId.setError(obj.getString("message"));
                            editTextId.requestFocus();
                        }else
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //Toast.makeText(getApplicationContext(), "NULL RETURN", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.signin_btn:{
                Log.d(TAG, "계정생성 액티비티로 넘어감");
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
              //  overridePendingTransition(R.anim.slide_down, R.anim.stay);
                break;
            }
        }
    }
}