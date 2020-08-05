package kr.ac.ssu.myrecipe.Camera;


import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

import kr.ac.ssu.myrecipe.R;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);



        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("영수증 인식을 위해 권한이 필요합니다.\n")
                .setPermissions(Manifest.permission.CAMERA)
                //.setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

        //카메라 프리뷰를 프래그먼트로 넘겨서 뷰 생성
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CameraFragment.newInstance())
                    .commit();
        }
    }





    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(CameraActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    };
}