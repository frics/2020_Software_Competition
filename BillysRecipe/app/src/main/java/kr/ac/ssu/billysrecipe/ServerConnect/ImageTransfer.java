package kr.ac.ssu.billysrecipe.ServerConnect;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import kr.ac.ssu.billysrecipe.Camera.CropActivity;
import kr.ac.ssu.billysrecipe.Camera.GetReceiptActivity;
import kr.ac.ssu.billysrecipe.Camera.OCRFailActivity;
import kr.ac.ssu.billysrecipe.Camera.UploadActivity;

public class ImageTransfer extends AsyncTask<Void, Void, String>{
    private static final String TAG = "IMAGE TRANSFER";

    private Context mContext;


    public ImageTransfer(Context context){
        this.mContext = context;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        CropActivity.cropActivity.finish();
        UploadActivity.loadingDialong(mContext).show();

    }
    @Override
    protected String doInBackground(Void... voids) {
        RequestHandler requestHandler = new RequestHandler(mContext);
        return requestHandler.sendPostRequest(URLs.URL_IMG_TRANSFER, null, RequestHandler.IMG_TRANSFER);
    }
    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);

        try {
            JSONObject object = new JSONObject(s);
            if(!object.getBoolean("error")){
                Log.e(TAG + " 성공", object.getString("message"));
                if(!object.getBoolean("OCRerror")){
                    Log.e("OCR result : ", object.getString("OCRmessage"));
                    ((UploadActivity)mContext).finish();
                    mContext.startActivity(new Intent(mContext, GetReceiptActivity.class));
                }else {
                    Log.e("OCR result : ", object.getString("OCRmessage"));
                    ((UploadActivity)mContext).finish();
                    mContext.startActivity(new Intent(mContext, OCRFailActivity.class));
                }
            }else{
                Log.e(TAG + " 실패", object.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(UploadActivity.loadingDialong(mContext).isShowing())
            UploadActivity.loadingDialong(mContext).dismiss();

    }
}