package kr.ac.ssu.myrecipe.MajorFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.AutoScrollHelper;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.sothree.slidinguppanel.ScrollableViewHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import kr.ac.ssu.myrecipe.MainActivity;
import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.adapter.RecipeListAdapter;
import kr.ac.ssu.myrecipe.adapter.MyListDecoration;
import kr.ac.ssu.myrecipe.adapter.RecipePagerAdapter;
import kr.ac.ssu.myrecipe.recipe.Recipe;
import kr.ac.ssu.myrecipe.recipe.RecipeIntroduction;
import kr.ac.ssu.myrecipe.recipe.RecipeListFragment;
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class HomeFragment extends Fragment {
    static public String recent_recipes;
    private ArrayList<Recipe> itemList, rankList;
    private TextView listButton;
    private RecyclerView totalListView, recentListView;
    private AutoScrollViewPager viewPager;
    public RecipeListAdapter recentAdapter;
    private RelativeLayout relativeLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        makeItemList();
        setViewById(view);
        setAdapter();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayoutManager recentLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recentListView.setLayoutManager(recentLayoutManager);
        MyListDecoration decoration = new MyListDecoration();
        ArrayList<Recipe> recent_list = getRecentRecipeList();

        if (recent_list.size() == 0)
            relativeLayout.setVisibility(GONE);
        else
            relativeLayout.setVisibility(VISIBLE);

        recentAdapter = new RecipeListAdapter(getContext(), recent_list, onClickOrigin);
        recentListView.setAdapter(recentAdapter);
        recentListView.addItemDecoration(decoration);
    }

    private void makeItemList(){
        itemList = new ArrayList<>(Arrays.asList(Recipe.myRecipeList));
        rankList = new ArrayList<>(Arrays.asList(Recipe.RankingList));
    }

    private void setViewById(View view) { // View에 id 세팅
        listButton = view.findViewById(R.id.plus);
        listButton.setOnClickListener(onClickMenu);
        viewPager = view.findViewById(R.id.popular_recipe_viewpager);
        totalListView = view.findViewById(R.id.total_recipe_listview);
        recentListView = view.findViewById(R.id.recent_listview);
        relativeLayout = view.findViewById(R.id.recent_recipe_view);
    }

    public void setAdapter() { // RecyclerView 어댑터 세팅
        MyListDecoration decoration = new MyListDecoration();

        LinearLayoutManager totalLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        totalListView.setLayoutManager(totalLayoutManager);
        RecipeListAdapter totalAdapter = new RecipeListAdapter(getContext(), itemList, onClickItem);
        totalListView.setAdapter(totalAdapter);
        totalListView.addItemDecoration(decoration);

        viewPager.startAutoScroll(); // 자동 스크롤 on
        viewPager.setInterval(6500); // 스크롤 간격
        viewPager.setScrollDurationFactor(3); // 스크롤 넘어가는 속도
        viewPager.setPageMargin(50);
        RecipePagerAdapter viewPagerAdapter = new RecipePagerAdapter(getContext(), rankList, onClickOrigin);
        viewPager.setAdapter(viewPagerAdapter);
    }

    private ArrayList<Recipe> getRecentRecipeList() {
        ArrayList<Recipe> list = new ArrayList<>();
        Log.d("DEBUG", "getRecentRecipeList: " + recent_recipes);
        String[] string_list = recent_recipes.split(",");

        if (string_list[0].compareTo("") == 0) // 최근 본 리스트가 없는 경우
            return list;

        for (int i = 0; i < string_list.length; i++)
            list.add(Recipe.recipeList[Integer.parseInt(string_list[i])]);

        return list;
    }

    private View.OnClickListener onClickItem = new View.OnClickListener() {
        // 레시피 소개 액티비티 전환
        @Override
        public void onClick(View v) { // 완성도 정렬 레시피
            Intent intent = new Intent(getContext(), RecipeIntroduction.class);
            intent.putExtra("recipe", itemList.get((int) v.getTag()));
            startActivity(intent);
        }
    };

    public View.OnClickListener onClickOrigin = new View.OnClickListener() {
        // 레시피 소개 액티비티 전환
        @Override
        public void onClick(View v) { // id값 정렬 레시피
            Intent intent = new Intent(getContext(), RecipeIntroduction.class);
            intent.putExtra("recipe", Recipe.recipeList[(int) v.getTag()]);
            startActivity(intent);
        }
    };

    private View.OnClickListener onClickMenu = new View.OnClickListener() {
        // 전체 레시피 메뉴 프레그먼트 전환
        @Override
        public void onClick(View v) { // 전체보기
            RecipeListFragment newFragment = new RecipeListFragment();
            FragmentTransaction transaction = ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_home, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    };
}

