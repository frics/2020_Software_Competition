package kr.ac.ssu.billysrecipe.ServerConnect;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListData;
import kr.ac.ssu.billysrecipe.User.SharedPrefManager;

public class SendScrap extends AsyncTask<Void, Void, String> {

    private static final String TAG = SendScrap.class.getSimpleName();
    private Context mContext;
    private ScrapListData scrapListData;
    private String id;
    private String serial_num;


    public SendScrap(Context context, ScrapListData scrapListData) {
        this.mContext = context;
        this.scrapListData = scrapListData;
        this.id = SharedPrefManager.getString(this.mContext, SharedPrefManager.KEY_ID);
        this.serial_num = scrapListData.getId()+"";
    }

    @Override
    protected String doInBackground(Void... voids) {
        RequestHandler requestHandler = new RequestHandler();
        HashMap<String, String> params = new HashMap<>();

        params.put("id", id);
        params.put("serial_num", serial_num);
        if(scrapListData.getScraped()==1) {
            Log.e("isScraped", "true");
            params.put("isScraped", String.valueOf(true));
        }else {
            Log.e("isScraped", "false");
            params.put("isScraped", String.valueOf(false));
        }
        Log.e("마잌췤 원투원투", id+ ", "+ serial_num);

        return requestHandler.sendPostRequest(URLs.URL_SCRAP_CHANGE, params, RequestHandler.DATA_TRANSFER);
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
            if (!object.getBoolean("error") && !object.getBoolean("cntChangeError")) {
                Log.e(TAG, object.getString("message"));
                Log.e(TAG, object.getString("cntChangeMessage"));

            }else {
                Log.e(TAG,object.getString("message"));
                Log.e(TAG, object.getString("cntChangeMessage"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}