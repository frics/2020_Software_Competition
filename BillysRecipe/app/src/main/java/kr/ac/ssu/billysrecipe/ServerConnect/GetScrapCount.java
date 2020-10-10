package kr.ac.ssu.billysrecipe.ServerConnect;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListData;
import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListDataBase;
import kr.ac.ssu.billysrecipe.recipe.Recipe;
import kr.ac.ssu.billysrecipe.recipe.RecipeOrderList;

public class GetScrapCount extends AsyncTask<Void, Void, String> {

    private static final String TAG = GetScrapCount.class.getSimpleName();
    private JSONArray scrapData;
    private Context context;

    public GetScrapCount(Context context) {
        this.context = context;
    }

    public JSONArray getJSONArray() {
        return this.scrapData;
    }

    @Override
    protected String doInBackground(Void... voids) {
        RequestHandler requestHandler = new RequestHandler();
        return requestHandler.sendPostRequest(URLs.URL_GET_SCRAP_COUNT, null, RequestHandler.DATA_TRANSFER);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            ScrapListDataBase db = Room.databaseBuilder(context, ScrapListDataBase.class, "scraplist.db").allowMainThreadQueries().build();
            JSONObject object = new JSONObject(s);
            //if no error in response
            if (!object.getBoolean("error")) {
                Log.e(TAG, object.getString("message"));
                scrapData = object.getJSONArray("scrap_data");

                for (int i = 0; i < scrapData.length(); i++) {
                    JSONObject index = scrapData.getJSONObject(i);
                    String serialNum = index.getString("serial_num");
                    String scrapCnt = index.getString("scrap_cnt");
                    Log.d("TAG", "SSIBAL" + i + 1 + " " + serialNum + " : " + scrapCnt);

                    ScrapListData data;
                    data = db.Dao().findData(Integer.parseInt(serialNum));

                    if(data == null) {
                        data.setTotalNum(Integer.parseInt(scrapCnt));
                        data.setId(Integer.parseInt(serialNum));
                        db.Dao().insert(data);
                    } else {
                        data.setTotalNum(Integer.parseInt(scrapCnt));
                        db.Dao().update(data);
                    }
                }

            } else {
                Log.e(TAG, object.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
