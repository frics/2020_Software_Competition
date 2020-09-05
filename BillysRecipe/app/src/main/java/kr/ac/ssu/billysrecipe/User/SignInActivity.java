package kr.ac.ssu.billysrecipe.User;

import android.content.Context;
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
import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.ssu.billysrecipe.MainActivity;
import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorData;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorDataBase;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.ThreadTask;
import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListData;
import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListDataBase;
import kr.ac.ssu.billysrecipe.recipe.Recipe;
import kr.ac.ssu.billysrecipe.recipe.RecipeOrderList;
import kr.ac.ssu.billysrecipe.ServerConnect.RequestHandler;
import kr.ac.ssu.billysrecipe.ServerConnect.URLs;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SignInActivity.class.getSimpleName();
    ;
    private EditText editTextId;
    private EditText editTextPw;
    private Button signInBtn;
    private Button signUpBtn;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextId = findViewById(R.id.user_id);
        editTextPw = findViewById(R.id.user_password);
        signInBtn = findViewById(R.id.signin_submit);
        signUpBtn = findViewById(R.id.signup_btn);
        context = this;
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

        Log.d(TAG, id + ", " + password);
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
            private Context context;

            public UserLogin(Context context) {
                this.context = context;
            }

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
                    JSONObject object = new JSONObject(s);
                    //Log.e(TAG, jsonArray+"");
                    JSONObject response = object.getJSONObject("response");
                    Log.d(TAG + "response", "response : " + response + "");
                    JSONObject refResponse = object.getJSONObject("refResponse");
                    Log.e(TAG + "refResponse", "refResponse : " + refResponse + "");
                    JSONObject scrapResponse = object.getJSONObject("scrapResponse");
                    Log.e(TAG + "scrapResponse", "scrapResponse : " + scrapResponse);

                    if (!response.getBoolean("error")) {

                        if (!refResponse.getBoolean("error")) {
                            Log.e(TAG, response.getString("message") + "\n" + refResponse.getString("message"));
                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            JSONObject userJson = object.getJSONObject("user");
                            Log.d(TAG + "user", "user : " + userJson + "");
                            String nickname = userJson.getString("nickname");
                            String dbname = userJson.getString("mem_idx") + nickname;

                            Log.d(TAG, id + ", " + nickname + ", " + dbname);

                            //Preference에 정보 삽입
                            SharedPrefManager.userSignin(SignInActivity.this, id, nickname, dbname);

                            ////냉장고 디비 받아오기
                            JSONArray refrigerator = object.getJSONArray("refrigerator");
                            Log.e(TAG + "refrigerator", "refrigerator : " + refrigerator);
                            RefrigeratorDataBase db = Room.databaseBuilder(context, RefrigeratorDataBase.class, "refrigerator.db").build();
                            ArrayList<RefrigeratorData> list = new ArrayList<>();
                            for (int i = 0; i < refrigerator.length(); i++) {
                                RefrigeratorData data = new RefrigeratorData();
                                data.setCategory(refrigerator.getJSONObject(i).getString("category"));
                                data.setTag(refrigerator.getJSONObject(i).getString("tag"));
                                data.setName(refrigerator.getJSONObject(i).getString("name"));
                                data.setTagNumber(refrigerator.getJSONObject(i).getInt("tagNumber"));
                                list.add(data);
                            }

                            //임시 스크랩 레시피 디비 세팅
                            ScrapListDataBase scrapDB;
                            scrapDB = Room.databaseBuilder(getApplicationContext(), ScrapListDataBase.class, "scraplist.db").allowMainThreadQueries().build();
                            ScrapListData dbdata = new ScrapListData();
                            JSONArray scrap = object.getJSONArray("scrap");

                            for (int i = 1; i <= Recipe.TOTAL_RECIPE_NUM; i++) {
                                dbdata.setId(i);
                                dbdata.setTotalNum(0);
                                dbdata.setScraped(0);
                                for (int j = 0; j < scrap.length(); j++) {
                                    if (Integer.parseInt(scrap.getJSONObject(j).getString("serial_num")) == i)
                                        dbdata.setScraped(1);
                                }
                                scrapDB.Dao().insert(dbdata);
                            }

                            ThreadTask.OnTaskCompleted listener = new ThreadTask.OnTaskCompleted() {
                                @Override
                                public void onTaskCompleted(String str) {
                                    // 완성도 순 레시피리스트 세팅
                                    RecipeOrderList.RenewOrder(getApplicationContext());

                                    finish();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }

                                @Override
                                public void onTaskFailure(String str) {
                                    Log.e("RefrigeratorFragment", "Task Failure!");
                                }
                            };
                            new ThreadTask(db.Dao(), listener, ThreadTask.INITIALIZE).execute(list);

                        } else { //냉장고 디비 복원 실패 시
                            Toast.makeText(getApplicationContext(), refResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } else { //비밀번호 || 아이디 오류 시
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        UserLogin ul = new UserLogin(context);
        ul.execute();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_passwd: {
                Log.d(TAG, "비밀번호 찾기(미구현)");
                break;
            }
            case R.id.signup_btn: {
                Log.d(TAG, "계정생성 액티비티로 넘어감");
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            }
        }
    }
}