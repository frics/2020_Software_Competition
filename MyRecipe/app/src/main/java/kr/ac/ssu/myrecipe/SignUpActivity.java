package kr.ac.ssu.myrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG =  SignUpActivity.class.getSimpleName();;
    private EditText editTextId;
    private EditText editTextPw;
    private EditText editTextNickName;
    private Button submitBtn;
    private Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextId = findViewById(R.id.user_id);
        editTextPw = findViewById(R.id.user_password);
        submitBtn = findViewById(R.id.signup_submit);
        signInBtn = findViewById(R.id.signin_btn);
        submitBtn.setOnClickListener(this);
        signInBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_submit: {
                break;
            }
            case R.id.signin_btn:{
                Log.d(TAG, "계정생성 액티비티로 넘어감");
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            }
        }
    }
}