package kr.ac.ssu.myrecipe.MajorFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.User.SharedPrefManager;

public class AccountFragment extends Fragment {

    private TextView TvNickname;
    private TextView TvId;
    private static final String KEY_ID="userId";
    private static final String KEY_NICKNAME = "userNickname";
    private String id;
    private String nickname;

    public static  AccountFragment newInstance() {
        return new  AccountFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        nickname = SharedPrefManager.getString(getContext(), KEY_NICKNAME);
        id = SharedPrefManager.getString(getContext(),KEY_ID);

        TvNickname =view.findViewById(R.id.info_nickname);
        TvId = view.findViewById(R.id.info_id);
        TvNickname.setText(nickname);
        TvId.setText(id);



        return view;
    }

}