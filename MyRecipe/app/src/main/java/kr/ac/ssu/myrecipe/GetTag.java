package kr.ac.ssu.myrecipe;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.User.RequestHandler;
import kr.ac.ssu.myrecipe.User.URLs;
import kr.ac.ssu.myrecipe.adapter.TagListAdapter;

public class GetTag extends AsyncTask<Void, Void, String>{
    private static final String TAG = GetTag.class.getSimpleName();
    private ArrayList<TagListAdapter.Item> tagList;
    private OnTaskCompleted listener;


    public interface OnTaskCompleted {
        void onTaskCompleted(String str);
        void onTaskFailure(String str);
    }

    public GetTag(ArrayList<TagListAdapter.Item> tagList, OnTaskCompleted listener) {
        this.tagList = tagList;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        RequestHandler requestHandler = new RequestHandler();
        return requestHandler.sendPostRequest(URLs.URL_GET_TAG, null);
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
            JSONObject response = object.getJSONObject("response");
           // Log.e(TAG, object.getJSONObject("response")+"");
            if(!response.getBoolean("error")) {
                JSONArray tag = object.getJSONArray("tag");
                for(int i = 0; i < tag.length(); i++){
                    TagListAdapter.Item data = new TagListAdapter.Item();
                    JSONObject arrayIndex = tag.getJSONObject(i);
                    data.setCategory(arrayIndex.getString("category"));
                    data.setTag(arrayIndex.getString("tag"));
                    data.setTagNumber(arrayIndex.getInt("tagNumber"));
                    //생성자에서 넘겨받은 arrayList에 저장
                    if(i == 0)
                        data.setCategory("가루/오일");
                    tagList.add(data);
                }
            }else{
                Log.e(TAG, response.getString("message"));
            }
            TagListAdapter.Item data = new TagListAdapter.Item();
            data.setCategory("카테고리 없음");
            data.setTag("태그없음");
            data.setTagNumber(0);
            tagList.add(data);

        } catch (JSONException e) {
            if (listener != null) {
                listener.onTaskFailure("Fail");
            }
            e.printStackTrace();
        }
        if (listener != null) {
            listener.onTaskCompleted("Success");
        }
    }

}