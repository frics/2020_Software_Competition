package kr.ac.ssu.billysrecipe.ServerConnect;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GetScrapCount extends AsyncTask<Void, Void, String>{

    private static final String TAG = GetScrapCount.class.getSimpleName();
    private int serial_num;
    private int scrapCount;


    public GetScrapCount(int serial_num){
        this.serial_num = serial_num;
    }

    public int  getCount(){
        return this.scrapCount;
    }

    @Override
    protected String doInBackground(Void... voids) {
        RequestHandler requestHandler = new RequestHandler();
        HashMap<String, String> params = new HashMap<>();

        params.put("serial_num", String.valueOf(serial_num));

        return requestHandler.sendPostRequest(URLs.URL_GET_SCRAP_COUNT, params);
    }

    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
        try {

            JSONObject object = new JSONObject(s);

            //if no error in response
            if (!object.getBoolean("error")) {
                Log.e(TAG, object.getString("message"));
                this.scrapCount = object.getInt("scrap_cnt");
                Log.e(TAG+ "cnt ", this.scrapCount+""+"["+serial_num+"]");
            }else {
                Log.e(TAG,object.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
