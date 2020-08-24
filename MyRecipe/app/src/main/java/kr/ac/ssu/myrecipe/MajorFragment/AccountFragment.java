package kr.ac.ssu.myrecipe.MajorFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import kr.ac.ssu.myrecipe.R;

public class AccountFragment extends Fragment {

    public static  AccountFragment newInstance() {
        return new  AccountFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        return view;
    }
}