package kr.ac.ssu.billysrecipe.MajorFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.ServerConnect.GetScrapCount;
import kr.ac.ssu.billysrecipe.ServerConnect.PushData;
import kr.ac.ssu.billysrecipe.User.SharedPrefManager;
import kr.ac.ssu.billysrecipe.User.SignInActivity;
import kr.ac.ssu.billysrecipe.adapter.AccountVPAdapter;

public class AccountFragment extends Fragment {

    private static final String TAG = AccountFragment.class.getSimpleName();
    private TextView TvNickname;
    private Context mContext;
    private TextView TvId;
    private Button backUpBtn;
    private Button signoutBtn;
    private String UserID;
    private String nickname;
    private String DBName;
    private ViewPager viewPager;
    private TabLayout tabLayout;


    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        mContext = getContext();
        nickname = SharedPrefManager.getString(mContext, SharedPrefManager.KEY_NICKNAME);
        UserID = SharedPrefManager.getString(mContext, SharedPrefManager.KEY_ID);
        DBName = SharedPrefManager.getString(mContext, SharedPrefManager.KEY_REF_DB);

        setHasOptionsMenu(true);
        Log.e(TAG, nickname);
        Log.e(TAG, UserID);
        Log.e(TAG, DBName);

        TvNickname = view.findViewById(R.id.info_nickname);
        TvId = view.findViewById(R.id.info_id);
        TvNickname.setText(nickname);
        TvId.setText(UserID);

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
                PushData pushData = new PushData(getContext(), PushData.BACKUP, DBName);
                pushData.execute();
            }
        });

        viewPager = view.findViewById(R.id.account_viewpager);
        tabLayout = view.findViewById(R.id.account_tab);
        //viewPager 선언및 연동
        AccountVPAdapter vpAdapter = new AccountVPAdapter(getChildFragmentManager());
        viewPager.setAdapter(vpAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("스크랩");
        tabLayout.getTabAt(1).setText("장바구니");


        /************* 스크랩 카운트 사용법 *******************/
        /****Example serial_num = 1000으로 설정해서 test함 ***/
        GetScrapCount getScrapCount = new GetScrapCount(1000);
        getScrapCount.execute();

        return view;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.account_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        if (curId == R.id.action_setting) {
            Toast.makeText(getActivity(), "설정", Toast.LENGTH_SHORT).show();//tab1 메뉴 아이콘 선택시 이벤트 설정
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOutProcess() {
        Log.e(TAG, "이제 다이얼로그 출력되야한다.");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //데이터 백업
                        Log.e(TAG, DBName);
                        PushData pushData = new PushData(getContext(), PushData.LOGOUT, DBName);
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