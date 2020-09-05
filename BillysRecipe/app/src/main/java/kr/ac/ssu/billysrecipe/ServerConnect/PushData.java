package kr.ac.ssu.billysrecipe.ServerConnect;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorData;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorDataBase;

public class PushData extends AsyncTask<Void, Void, String> {
    private Context context;
    private int flag;
    private String dbname;
    private String id;
    public final static int BACKUP = 1;
    public final static int LOGOUT = 0;


    public interface OnTaskCompleted {
        void onTaskCompleted(String str);
        void onTaskFailure(String str);
    }

    public PushData(Context context, int flag, String dbname, String id) {
        this.context = context;
        this.flag = flag;
        this.dbname = dbname;
        this.id = id;

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
            if(flag == LOGOUT)
                db.Dao().deleteAll();
            Log.e("test",refArray.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }

        /************SCRAP BACKUP**************/
        JSONArray scrapArray = new JSONArray();
        try{
            //android 내부 Scrap 디비 받아오기
            for(int i = 0; i < 10/*디비 사이즈*/; i++){
                JSONObject ScrapjsonObject = new JSONObject();
                //서버 전송 json
                ScrapjsonObject.put("serial_num", "여기다 데이터 받아서 넣으면 됨");
                scrapArray.put(ScrapjsonObject);
            }
            Log.e("test",scrapArray.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }

        /***************************************/

        String refJson = refArray.toString();
        String scrapJson = scrapArray.toString();

        HashMap<String, String> params = new HashMap<>();
        params.put("refJson", refJson);
        params.put("dbname", dbname);

        params.put("scrapJson", scrapJson);
        params.put("id", id);
        Log.e("CHECKCHECK", dbname+"");

        return requestHandler.sendPostRequest(URLs.URL_DB_BACKUP, params);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
