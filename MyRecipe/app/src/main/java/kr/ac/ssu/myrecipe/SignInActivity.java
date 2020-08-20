package kr.ac.ssu.myrecipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG =  SignInActivity.class.getSimpleName();;
    private EditText editTextId;
    private EditText editTextPw;
    private Button submitBtn;
    private Button signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextId = findViewById(R.id.user_id);
        editTextPw = findViewById(R.id.user_password);
        submitBtn = findViewById(R.id.signin_submit);
        signUpBtn = findViewById(R.id.signup_btn);
        submitBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);


    }
    public void insert(View view) {
        String Id = editTextId.getText().toString();
        String Pw = editTextPw.getText().toString();
        if(!(Id.equals("") || Pw.equals(""))){
            Log.d(TAG, "DB로 전송 시작"+Id+", "+Pw);
            insertoToDatabase(Id, Pw);
        }
    }

    private void insertoToDatabase(String Id, String Pw) {
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignInActivity.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String Id = (String) params[0];
                    String Pw = (String) params[1];

                    String link = "http://13.209.6.94/SignIn.php";
                    String data = Id + "&" + Pw;
                   // data += "&" + URLEncoder.encode("Pw", "UTF-8") + "=" + URLEncoder.encode(Pw, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(Id, Pw);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin_submit: {
                insert(view);
                break;
            }
            case R.id.find_passwd:{
                Log.d(TAG, "비밀번호 찾기(미구현)");
                break;
            }
            case R.id.signup_btn:{
                Log.d(TAG, "계정생성 액티비티로 넘어감");
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}