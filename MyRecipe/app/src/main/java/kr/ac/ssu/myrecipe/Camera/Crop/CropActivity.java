package kr.ac.ssu.myrecipe.Camera.Crop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.theartofdev.edmodo.cropper.CropImage;

import kr.ac.ssu.myrecipe.R;

public class CropActivity extends AppCompatActivity {


        // region: Fields and Consts

        DrawerLayout mDrawerLayout;

        private ActionBarDrawerToggle mDrawerToggle;

        private CropFragment mCurrentFragment;

        private Uri mCropImageUri;

        private CropImageViewOptions mCropImageViewOptions = new CropImageViewOptions();
        // endregion

        public void setCurrentFragment(CropFragment fragment) {
            Log.e("순서", "1");
            mCurrentFragment = fragment;
        }

        public void setCurrentOptions(CropImageViewOptions options) {
            Log.e("순서", "2");
            mCropImageViewOptions = options;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_crop);
            Log.e("순서", "3");



            if (savedInstanceState == null) {
                setMainFragmentByPreset(CropDemoPreset.RECT);
            }
        }

        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
            Log.e("순서", "4");
            super.onPostCreate(savedInstanceState);

        }




        @Override
        @SuppressLint("NewApi")
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
                    && resultCode == AppCompatActivity.RESULT_OK) {
                Uri imageUri = CropImage.getPickImageResultUri(this, data);

                // For API >= 23 we need to check specifically that we have permissions to read external
                // storage,
                // but we don't know if we need to for the URI so the simplest is to try open the stream and
                // see if we get error.
                boolean requirePermissions = false;
                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {

                    // request permissions and handle the result in onRequestPermissionsResult()
                    requirePermissions = true;
                    mCropImageUri = imageUri;
                    requestPermissions(
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
                } else {

                    mCurrentFragment.setImageUri(imageUri);
                }
            }
        }

        @Override
        public void onRequestPermissionsResult(
                int requestCode, String permissions[], int[] grantResults) {
            if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CropImage.startPickImageActivity(this);
                } else {
                    Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG)
                            .show();
                }
            }
            if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
                if (mCropImageUri != null
                        && grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mCurrentFragment.setImageUri(mCropImageUri);
                } else {
                    Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }



        private void setMainFragmentByPreset(CropDemoPreset demoPreset) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, CropFragment.newInstance(demoPreset))
                    .commit();
        }


    }