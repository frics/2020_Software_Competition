package kr.ac.ssu.myrecipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.MajorFragment.HomeFragment;
import kr.ac.ssu.myrecipe.adapter.ScrapListAdapter;

public class ScrapFragment extends Fragment {

    private RecyclerView scrapListView;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scrap, container, false);

        setViewById(view);
        setAdapter();

        return view;
    }

    private void setViewById(View view) {
        scrapListView = view.findViewById(R.id.scrap_list);
    }

    private void setAdapter() {
        // 임시 구조 세팅
        ArrayList<Integer> tmp = new ArrayList<>();

        for (int i = 1; i <= 10; i++)
            tmp.add(i * 3);

        HomeFragment homeFragment = new HomeFragment();

        LinearLayoutManager scrapLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);

        scrapListView.setLayoutManager(scrapLayoutManager);
        ScrapListAdapter scrapAdapter = new ScrapListAdapter(getContext(), tmp, homeFragment.onClickOrigin);
        scrapListView.setAdapter(scrapAdapter);
    }
}
