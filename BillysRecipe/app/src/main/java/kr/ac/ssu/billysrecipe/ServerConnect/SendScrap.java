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
import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListData;
import kr.ac.ssu.billysrecipe.User.SharedPrefManager;

public class SendScrap extends AsyncTask<Void, Void, String> {
    private Context context;
    private ScrapListData scrapListData;
    private String id;

    public interface OnTaskCompleted {
        void onTaskCompleted(String str);
        void onTaskFailure(String str);
    }

    public SendScrap(Context context, ScrapListData scrapListData) {
        this.context = context;
        this.scrapListData = scrapListData;
        this.id = SharedPrefManager.getString(this.context, SharedPrefManager.KEY_ID);
    }

    @Override
    protected String doInBackground(Void... voids) {
        RequestHandler requestHandler = new RequestHandler();
        HashMap<String, String> params = new HashMap<>();

        params.put("id", id);
        params.put("serial_num", ""+(scrapListData.getId()+1));
        if(scrapListData.getScraped()==1)
            params.put("isScraped", "true");
        else
            params.put("isScraped", "false");

        return requestHandler.sendPostRequest(URLs.URL_SCRAP, params);
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