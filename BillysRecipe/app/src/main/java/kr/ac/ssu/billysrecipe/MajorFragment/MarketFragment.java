package kr.ac.ssu.billysrecipe.MajorFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
        weekRecycler = (RecyclerView)view.findViewById(R.id.diet_week);
        dietRecycler = (RecyclerView)view.findViewById(R.id.diet_list);
        progressBar = (ProgressBar)view.findViewById(R.id.diet_progressBar);
        totalCalories = (TextView)view.findViewById(R.id.diet_total_kcal);
        percentage = (TextView)view.findViewById(R.id.diet_percent);

        weekRecycler.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false));
        dietRecycler.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false));
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setData();
    }
    public void setData(){
        weeklist = new ArrayList<>();
        weeklist.add("  일  ");
        weeklist.add("  월  ");
        weeklist.add("  화  ");
        weeklist.add("  수  ");
        weeklist.add("  목  ");
        weeklist.add("  금  ");
        weeklist.add("  토  ");

        dataList = new ArrayList<>();
        //데이터 추가
        dataList.add(new ArrayList<DietListAdapter.Data>());
        dataList.get(0).add(new DietListAdapter.Data("url","칼륨 듬뿍 고구마죽",350));
        dataList.get(0).add(new DietListAdapter.Data("url","누룽지 두부 계란죽",314));
        dataList.get(0).add(new DietListAdapter.Data("url","두부 곤약 비빔밥",289));
        for(int i = 1; i < 7; i++){
            dataList.add(new ArrayList<DietListAdapter.Data>());
            dataList.get(i).add(new DietListAdapter.Data("url","오렌지라시 스시",249));
            dataList.get(i).add(new DietListAdapter.Data("url","취나물 비빔밥",350));
            dataList.get(i).add(new DietListAdapter.Data("url","곰취 쌈밥",290));
        }

        //현재 요일로 default 설정
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DAY_OF_WEEK)-1;
        dietListAdapter = new DietListAdapter(dataList.get(date));
        dietRecycler.setAdapter(dietListAdapter);
        weekAdapter = new CategoryAdapter(weeklist,getContext());
        weekRecycler.setAdapter(weekAdapter);
        int prePositon = weekAdapter.getClick_position();
        weekAdapter.setClick_position(date);
        weekAdapter.notifyItemChanged(prePositon);
        weekAdapter.notifyItemChanged(date);

        //요일의 kcal percentage 설정
        int result = (dietListAdapter.getTotalCalories()*100)/2000;
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
                int result = (dietListAdapter.getTotalCalories()*100)/2000;
                progressBar.setProgress(result);
                percentage.setText(result + "% (1인 하루 권장량 기준)");
                totalCalories.setText(dietListAdapter.getTotalCalories() + " kcal");
            }
        });
    }
}