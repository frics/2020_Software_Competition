package kr.ac.ssu.billysrecipe.ServerConnect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDialog;

import org.json.JSONException;
import org.json.JSONObject;

import kr.ac.ssu.billysrecipe.Camera.GetReceiptActivity;
import kr.ac.ssu.billysrecipe.Camera.OCRFailActivity;
import kr.ac.ssu.billysrecipe.Camera.UploadActivity;
import kr.ac.ssu.billysrecipe.R;

public class ImageTransfer extends AsyncTask<Void, Void, String>{
    private static final String TAG = "IMAGE TRANSFER";
    AppCompatDialog progressDialog;
    private Context mContext;


    public ImageTransfer(Context context){
        this.mContext = context;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = new AppCompatDialog(mContext);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.loading_dialog);
        progressDialog.show();
        final ImageView img_loading_frame = progressDialog.findViewById(R.id.iv_frame_loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });
        final ImageView text_loading_frame = progressDialog.findViewById(R.id.tv_progress_message);
        final AnimationDrawable textAnimation = (AnimationDrawable) text_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                textAnimation.start();
            }
        });
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
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

    }
}