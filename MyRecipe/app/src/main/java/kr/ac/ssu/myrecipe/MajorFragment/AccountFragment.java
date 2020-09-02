package kr.ac.ssu.myrecipe.MajorFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.ServerConnect.PushData;
import kr.ac.ssu.myrecipe.User.SharedPrefManager;
import kr.ac.ssu.myrecipe.adapter.AccountVPAdapter;

public class AccountFragment extends Fragment {

    private static final String TAG =  AccountFragment.class.getSimpleName();
    private TextView TvNickname;
    private Context mContext;
    private TextView TvId;
    private Button backUpBtn;
    private Button signoutBtn;
    private String id;
    private String nickname;
    private String DBName;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public static  AccountFragment newInstance() {
        return new  AccountFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_account, container, false);
        mContext = getContext();
        nickname = SharedPrefManager.getString( mContext, SharedPrefManager.KEY_NICKNAME);
        id = SharedPrefManager.getString( mContext,SharedPrefManager.KEY_ID);
        DBName = SharedPrefManager.getString( mContext, SharedPrefManager.KEY_REF_DB);

        viewPager = view.findViewById(R.id.account_viewpager);
        tabLayout = view.findViewById(R.id.account_tab);
        //viewPager 선언및 연동
        AccountVPAdapter vpAdapter = new AccountVPAdapter(getChildFragmentManager());
        viewPager.setAdapter(vpAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("스크랩");
        tabLayout.getTabAt(1).setText("장바구니");

        Log.e(TAG, nickname);
        Log.e(TAG, id);
        Log.e(TAG, DBName);


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
        backUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PushData pushData = new PushData(getContext(),PushData.BACKUP, DBName);
                pushData.execute();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater); inflater.inflate(R.menu.account_menu,menu);
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId){
            case R.id.action_search:
                Toast.makeText(getActivity(), "설정", Toast.LENGTH_SHORT).show();//tab1 메뉴 아이콘 선택시 이벤트 설정
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void signOutProcess(){
        Log.e(TAG, "이제 다이얼로그 출력되야한다.");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //사용자 Preference 삭제
                        SharedPrefManager.logout(getContext());
                        PushData pushData = new PushData(getContext(),PushData.LOGOUT, DBName);
                        pushData.execute();

                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).create().show();
    }

}