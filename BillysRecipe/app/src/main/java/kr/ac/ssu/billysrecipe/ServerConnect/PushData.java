package kr.ac.ssu.billysrecipe.ServerConnect;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import kr.ac.ssu.billysrecipe.MainActivity;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorData;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorDataBase;
import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListDataBase;
import kr.ac.ssu.billysrecipe.User.SharedPrefManager;
import kr.ac.ssu.billysrecipe.User.SignInActivity;

public class PushData extends AsyncTask<Void, Void, String> {
    private static final String TAG = "DB bakcup";
    private Context context;
    private int flag;
    private String dbname;

    public final static int BACKUP = 1;
    public final static int LOGOUT = 0;


    public interface OnTaskCompleted {
        void onTaskCompleted(String str);
        void onTaskFailure(String str);
    }

    public PushData(Context context, int flag, String dbname) {
        this.context = context;
        this.flag = flag;
        this.dbname = dbname;

    }

    @Override
    protected String doInBackground(Void... voids) {
        RequestHandler requestHandler = new RequestHandler();
        JSONArray refArray = new JSONArray();
        try{
            RefrigeratorDataBase db = Room.databaseBuilder(context, RefrigeratorDataBase.class, "refrigerator.db").build();

            List<RefrigeratorData> datalist = db.Dao().sortData();
            for(int i = 0; i < datalist.size(); i++){
                JSONObject jsonObject = new JSONObject();
                //서버 전송 json
                jsonObject.put("ref_idx","" + datalist.get(i).getId());
                jsonObject.put("category", datalist.get(i).getCategory());
                jsonObject.put("tag", datalist.get(i).getTag());
                jsonObject.put("name", datalist.get(i).getName());
                jsonObject.put("tagNumber","" + datalist.get(i).getTagNumber());
                refArray.put(jsonObject);
            }
            if(flag == LOGOUT) {
                ScrapListDataBase scrapDB = Room.databaseBuilder(context, ScrapListDataBase.class, "scraplist.db").build();
                db.Dao().deleteAll();
                scrapDB.Dao().deleteAll();
            }
            Log.e("test",refArray.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        String refJson = refArray.toString();

        HashMap<String, String> params = new HashMap<>();
        params.put("refJson", refJson);
        params.put("dbname", dbname);

        Log.e("CHECKCHECK", dbname+"");

        return requestHandler.sendPostRequest(URLs.URL_DB_BACKUP, params, RequestHandler.DATA_TRANSFER);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {

            Log.d(TAG, "json object"+s);
            JSONObject object = new JSONObject(s);

            //if no error in response
            if (!object.getBoolean("error")) {
                Log.e(TAG, object.getString("message"));
                if(flag == LOGOUT) {
                    //사용자 Preference 삭제
                    SharedPrefManager.logout(context);
                    //내부 디비 삭제
                    ((MainActivity)context).finish();
                    context.startActivity(new Intent(context, SignInActivity.class));
                }
            }else {
                Toast.makeText(context, "로그아웃 실패", Toast.LENGTH_SHORT).show();
                Log.e(TAG,object.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
