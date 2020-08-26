package kr.ac.ssu.myrecipe.MajorFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.User.SharedPrefManager;

public class AccountFragment extends Fragment {

    private static final String TAG =  AccountFragment.class.getSimpleName();
    private TextView TvNickname;
    private TextView TvId;
    private Button backUpBtn;
    private Button signoutBtn;
    private String id;
    private String nickname;

    public static  AccountFragment newInstance() {
        return new  AccountFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        nickname = SharedPrefManager.getString(getContext(), SharedPrefManager.KEY_NICKNAME);

        id = SharedPrefManager.getString(getContext(),SharedPrefManager.KEY_ID);

        TvNickname =view.findViewById(R.id.info_nickname);
        TvId = view.findViewById(R.id.info_id);
        TvNickname.setText(nickname);
        TvId.setText(id);

        backUpBtn = view.findViewById(R.id.backup_btn);
        signoutBtn = view.findViewById(R.id.signout_btn);

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "SignOut Clicked");
                signOutProcess();
            }
        });

        return view;
    }

    private void signOutProcess(){
        Log.e(TAG, "이제 다이얼로그 출력되야한다.");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //사용자 Preference 삭제
                        SharedPrefManager.logout(getContext());

                        /*여기부터 내부 DB 삭제 코드 삽입
                        삭제할 내부 DB는 사용자의 냉장고 DB와 장보기 리스트 DB
                        사용자 냉장고 디비는 서버의 Mysql로 백업 해야한다.*/
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).create().show();
    }

}