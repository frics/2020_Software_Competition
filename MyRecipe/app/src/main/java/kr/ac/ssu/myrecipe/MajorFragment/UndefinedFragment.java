package kr.ac.ssu.myrecipe.MajorFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import kr.ac.ssu.myrecipe.R;

public class UndefinedFragment extends Fragment {

    public static UndefinedFragment newInstance() {
        return new UndefinedFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_undefined, container, false);
    }
}