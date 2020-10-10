package kr.ac.ssu.billysrecipe.MajorFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import kr.ac.ssu.billysrecipe.MainActivity;
import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorData;
import kr.ac.ssu.billysrecipe.adapter.CategoryAdapter;
import kr.ac.ssu.billysrecipe.adapter.DietListAdapter;
import kr.ac.ssu.billysrecipe.adapter.RefrigeratorAdapter;
import kr.ac.ssu.billysrecipe.adapter.TodayGridAdapter;
import kr.ac.ssu.billysrecipe.recipe.Recipe;
import kr.ac.ssu.billysrecipe.recipe.RecipeOrderList;

public class MarketFragment extends Fragment {
    private ArrayList<ArrayList<DietListAdapter.Data>> dataList;
    private ArrayList<String> weeklist;
    private RecyclerView weekRecycler;
    private RecyclerView dietRecycler;
    private DietListAdapter dietListAdapter;
    private CategoryAdapter weekAdapter;
    private ProgressBar progressBar;
    private TextView totalCalories;
    private TextView percentage;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        weekRecycler = (RecyclerView) view.findViewById(R.id.diet_week);
        dietRecycler = (RecyclerView) view.findViewById(R.id.diet_list);
        progressBar = (ProgressBar) view.findViewById(R.id.diet_progressBar);
        totalCalories = (TextView) view.findViewById(R.id.diet_total_kcal);
        percentage = (TextView) view.findViewById(R.id.diet_percent);

        weekRecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        dietRecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    public void setData() {
        weeklist = new ArrayList<>();
        weeklist.add("  일  ");
        weeklist.add("  월  ");
        weeklist.add("  화  ");
        weeklist.add("  수  ");
        weeklist.add("  목  ");
        weeklist.add("  금  ");
        weeklist.add("  토  ");

        dataList = makeWeekDiet();

        //현재 요일로 default 설정
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        dietListAdapter = new DietListAdapter(dataList.get(date));
        dietRecycler.setAdapter(dietListAdapter);
        weekAdapter = new CategoryAdapter(weeklist, getContext());
        weekRecycler.setAdapter(weekAdapter);
        int prePositon = weekAdapter.getClick_position();
        weekAdapter.setClick_position(date);
        weekAdapter.notifyItemChanged(prePositon);
        weekAdapter.notifyItemChanged(date);

        //요일의 kcal percentage 설정
        int result = (dietListAdapter.getTotalCalories() * 100) / 2000;
        progressBar.setProgress(result);
        percentage.setText(result + "% (1인 하루 권장량 기준)");
        totalCalories.setText(dietListAdapter.getTotalCalories() + " kcal");

        //요일을 클릭했을때
        weekAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                int prePositon = weekAdapter.getClick_position();
                weekAdapter.setClick_position(position);
                weekAdapter.notifyItemChanged(prePositon);
                weekAdapter.notifyItemChanged(position);
                dietListAdapter.changeData(dataList.get(position));
                int result = (dietListAdapter.getTotalCalories() * 100) / 2000;
                progressBar.setProgress(result);
                percentage.setText(result + "% (1인 하루 권장량 기준)");
                totalCalories.setText(dietListAdapter.getTotalCalories() + " kcal");
            }
        });
    }

    // 1주일 식단 생성 메소드
    private ArrayList<ArrayList<DietListAdapter.Data>> makeWeekDiet() {
        ArrayList<ArrayList<DietListAdapter.Data>> dietList = new ArrayList<>();
        ArrayList<DietListAdapter.Data> dayList = new ArrayList<>();
        DietListAdapter.Data diet;

        for (int day = 0; day < 7; day++) {
            for (int time = 0; time < 3; time++) {
                diet = makeDiet(dietList, time);
                dayList.add(diet);
            }
            dietList.add(dayList);
            dayList = new ArrayList<>();
        }

        return dietList;
    }

    // 한끼 식단 생성 메소드
    private DietListAdapter.Data makeDiet(ArrayList<ArrayList<DietListAdapter.Data>> dietList, int time) {
        int id;
        boolean isExist;
        DietListAdapter.Data diet;
        Random random = new Random();

        while (true) {
            id = random.nextInt(1160);

            switch (time) {
                case 0: // 아침
                    break;
                case 1: // 점심
                    if (Recipe.recipeList[id].style.compareTo("밥") != 0)
                        continue;
                    break;
                case 2: // 저녁
                    if (Recipe.recipeList[id].style.compareTo("후식") == 0)
                        continue;
                    break;
            }

            isExist = false;
            for (int i = 0; i < dietList.size(); i++)
                for (int j = 0; j < dietList.get(i).size(); j++)
                    if (dietList.get(i).get(j).getName().compareTo(Recipe.recipeList[id].name) == 0)
                        isExist = true;

            if (isExist)
                continue;

            diet = new DietListAdapter.Data(Recipe.recipeList[id].image_url, Recipe.recipeList[id].name, (int) Recipe.recipeList[id].nutrition[0]);
            return diet;
        }
    }
}