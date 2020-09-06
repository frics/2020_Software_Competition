package kr.ac.ssu.billysrecipe.ServerConnect;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetScrapCount extends AsyncTask<Void, Void, String>{

    private static final String TAG = GetScrapCount.class.getSimpleName();


    public GetScrapCount(){
        super();
    }

    @Override
    protected String doInBackground(Void... voids) {
        RequestHandler requestHandler = new RequestHandler();
        return requestHandler.sendPostRequest(URLs.URL_GET_SCRAP_COUNT, null);
    }

    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
        try {

            JSONObject object = new JSONObject(s);
            //if no error in response
            if (!object.getBoolean("error")) {
                Log.e(TAG, object.getString("message"));
                JSONArray scrapData = object.getJSONArray("scrap_data");
                for(int i=0; i < scrapData.length(); i++){
                    JSONObject index = scrapData.getJSONObject(i);
                    String serialNum = index.getString("serial_num");
                    String scrapCnt = index.getString("scrap_cnt");
                    Log.e(i+1+"", serialNum + " : " + scrapCnt);
                }

            }else {
                Log.e(TAG,object.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
