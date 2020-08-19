package kr.ac.ssu.myrecipe.Camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.io.File;

import kr.ac.ssu.myrecipe.R;


public class CapturedFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CapturedFragment";

    ImageView imageView;
    //Fragment에서 Fragment로 instance 넘길때 반환 메소드
    public static CapturedFragment newInstance() {
        return new CapturedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_captured, container,
                false);

        Toolbar toolbar =  view.findViewById(R.id.captured_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.bringToFront();

        imageView=(ImageView)view.findViewById(R.id.captured_img);
        view.findViewById(R.id.recapture_btn).setOnClickListener(this);
        view.findViewById(R.id.scanning_btn).setOnClickListener(this);


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        String path = getContext().getExternalFilesDir(null)+"/pic.jpg";
        Log.d(TAG, path);
        File imgFile = new File(path);
        if(imgFile.exists())
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            int width = myBitmap.getWidth();
            int height = myBitmap.getHeight();
            Bitmap rotatedBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
            imageView.setImageBitmap(rotatedBitmap);
        }


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recapture_btn: {
                Log.d(TAG, "다시 찍기");
                ((CameraActivity)getActivity()).replaceFragment(CameraFragment.newInstance());
                break;
            }
            case R.id.scanning_btn:{
                Log.d(TAG, "스캔 시작");

               Intent intent = new Intent(getContext(), UploadActivity.class);
               startActivity(intent);
               getActivity().finish();
            }
        }
    }
}