package kr.ac.ssu.billysrecipe.MajorFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import kr.ac.ssu.billysrecipe.MainActivity;
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
        MainActivity.isRecipeSetting= false; // 툴바 관련 변수

        ArrayList<String> datalist = new ArrayList<>();
        for(int i =0;i<9;i++)
            datalist.add(Recipe.recipeList[i].image_url);

        todayGridAdapter = new TodayGridAdapter(getContext(), R.layout.today_row, datalist);
        gridView = view.findViewById(R.id.deck);
        gridView.setAdapter(todayGridAdapter);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) { // 툴바 관련 메소드
        if (!MainActivity.isRecipeSetting) {
            for (int i = 0; i < menu.size(); i++) {
                if (menu.getItem(i).getTitle().toString().compareTo("list_setting") == 0) {
                    menu.getItem(i).setEnabled(false);
                    menu.getItem(i).setVisible(false);
                }
            }
        }
    }

}