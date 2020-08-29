package kr.ac.ssu.myrecipe.Camera.Crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import kr.ac.ssu.myrecipe.R;


public class CropFragment extends Fragment implements CropImageView.OnSetImageUriCompleteListener,
        CropImageView.OnCropImageCompleteListener {

    private static final String TAG = CropFragment.class.getSimpleName();
    File mFile = null;
    String uploadFilePath=null;
    String uploadFileName = null;

    // region: Fields and Consts

    private CropDemoPreset mDemoPreset;

    public CropImageView mCropImageView;
    // endregion

    /** Returns a new instance of this fragment for the given section number. */
    public static CropFragment newInstance(CropDemoPreset demoPreset) {
        CropFragment fragment = new CropFragment();
        Bundle args = new Bundle();
        args.putString("DEMO_PRESET", demoPreset.name());
        fragment.setArguments(args);
        return fragment;
    }


    public void setImageUri(Uri imageUri) {
        mCropImageView.setImageUriAsync(imageUri);
    }

    /** Set the initial rectangle to use. */
    public void setInitialCropRect() {
        mCropImageView.setCropRect(new Rect(100, 300, 500, 1200));
    }

    /** Reset crop window to initial rectangle. */
    public void resetCropRect() {
        mCropImageView.resetCropRect();
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        switch (mDemoPreset) {
            case RECT:
                rootView = inflater.inflate(R.layout.fragment_crop, container, false);
                break;
            default:
                throw new IllegalStateException("Unknown preset: " + mDemoPreset);
        }
        Toolbar toolbar =  rootView.findViewById(R.id.crop_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.bringToFront();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCropImageView = view.findViewById(R.id.cropImageView);
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);

        mFile = new File(getActivity().getExternalFilesDir(null), "pic_crop.jpg");
        uploadFilePath = mFile+"/";
        uploadFileName = "pic.jpg";
        Button btn = view.findViewById(R.id.crop_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCropImageView.getCroppedImageAsync();
            }
        });

        if (savedInstanceState == null) {
            //mCropImageView.setImageUriAsync(uri);
           makePhotoView();
        }
    }




    public void makePhotoView(){
        String path = getContext().getExternalFilesDir(null)+"/pic.jpg";
        Log.e(TAG, path);
        Log.e(TAG, "IN makePhotoView");
        File imgFile = new File(path);
        //사진 유무 확인
        if(imgFile.exists())
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            int width = myBitmap.getWidth();
            int height = myBitmap.getHeight();
            Bitmap rotatedBitmap = Bitmap.createBitmap(myBitmap, 0, 0,myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
            //CROP OPTION 설정
            mCropImageView.setImageBitmap(rotatedBitmap);
            mCropImageView.setScaleType(CropImageView.ScaleType.FIT_CENTER);
            mCropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
            mCropImageView.setGuidelines(CropImageView.Guidelines.ON);
            mCropImageView.setFixedAspectRatio(false);
            mCropImageView.setMultiTouchEnabled(true);
            mCropImageView.setShowCropOverlay(true);
            mCropImageView.setShowProgressBar(false);
            mCropImageView.setAutoZoomEnabled(false);
            mCropImageView.setFlippedHorizontally(false);
            mCropImageView.setFlippedVertically(false);

        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDemoPreset = CropDemoPreset.valueOf(getArguments().getString("DEMO_PRESET"));
        ((CropActivity) activity).setCurrentFragment(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCropImageView != null) {
            mCropImageView.setOnSetImageUriCompleteListener(null);
            mCropImageView.setOnCropImageCompleteListener(null);
        }
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            Toast.makeText(getActivity(), "Image load successful", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("AIC", "Failed to load image by URI", error);
            Toast.makeText(getActivity(), "Image load failed: " + error.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        handleCropResult(result);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFile = new File(getActivity().getExternalFilesDir(null), "pic.jpg");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            handleCropResult(result);
        }
    }

    private void handleCropResult(CropImageView.CropResult result) {
        if (result.getError() == null) {
            android.content.Intent intent = new Intent(getActivity(), CropResultActivity.class);
            intent.putExtra("SAMPLE_SIZE", result.getSampleSize());
            if (result.getUri() != null) {
                intent.putExtra("URI", result.getUri());
            } else {
                CropResultActivity.mImage = result.getBitmap();
            }
           //SaveBitmapToFileCache(result.getBitmap(), uploadFilePath);
            ImageSaver imageSaver = new ImageSaver(result.getBitmap(), mFile);
            imageSaver.run();
            startActivity(intent);
        } else {
            Log.e("AIC", "Failed to crop image", result.getError());
            Toast.makeText(
                    getActivity(),
                    "Image crop failed: " + result.getError().getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    private static class ImageSaver implements Runnable {
        /**
         * The JPEG image
         */
        private final Bitmap mImage;
        /**
         * The file we save the image into.
         */
        private final File mFile;

        ImageSaver(Bitmap image, File file) {
            mImage = image;
            mFile = file;
        }

        @Override
        public void run() {

            //File fileCacheItem = mFile;
            FileOutputStream output = null;
            try {
              //  fileCacheItem.createNewFile();
                output = new FileOutputStream(mFile);
                //bitmap  이미지를 JPG 형식으로 압축해서 저장
                mImage.compress(Bitmap.CompressFormat.JPEG, 100, output);
            }
            catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(null != output) {
                    try {
                        Log.e("test", "성공성공성공성공");
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


}