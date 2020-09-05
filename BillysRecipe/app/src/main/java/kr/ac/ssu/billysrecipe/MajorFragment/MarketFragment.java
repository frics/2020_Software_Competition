package kr.ac.ssu.billysrecipe.MajorFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.adapter.TodayGridAdapter;
import kr.ac.ssu.billysrecipe.recipe.Recipe;

public class MarketFragment extends Fragment {

    private GridView gridView;
    private TodayGridAdapter todayGridAdapter;

    public static MarketFragment newInstance() {
        return new MarketFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);

        ArrayList<String> datalist = new ArrayList<>();
        for(int i =0;i<9;i++)
            datalist.add(Recipe.recipeList[i].image_url);

        todayGridAdapter = new TodayGridAdapter(getContext(), R.layout.today_row, datalist);
        gridView = view.findViewById(R.id.deck);
        gridView.setAdapter(todayGridAdapter);





        return view;
    }
}