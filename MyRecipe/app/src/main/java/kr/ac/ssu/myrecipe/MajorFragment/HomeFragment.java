package kr.ac.ssu.myrecipe.MajorFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.sothree.slidinguppanel.ScrollableViewHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import kr.ac.ssu.myrecipe.MainActivity;
import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.adapter.RecipeListAdapter;
import kr.ac.ssu.myrecipe.adapter.MyListDecoration;
import kr.ac.ssu.myrecipe.recipe.Recipe;
import kr.ac.ssu.myrecipe.recipe.RecipeIntroduction;
import kr.ac.ssu.myrecipe.recipe.RecipeListFragment;

public class HomeFragment extends Fragment {
    static public String recent_recipes;
    private ArrayList<Recipe> itemList;
    private TextView listButton;
    private NestedScrollView homeScrollView;
    private RecyclerView totalListView, popularListView, recentListView;
    public RecipeListAdapter recentAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

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
        recentAdapter = new RecipeListAdapter(getContext(), recent_list, onClickItem);
        recentListView.setAdapter(recentAdapter);
        recentListView.addItemDecoration(decoration);
    }

    private void makeItemList(){
        itemList = new ArrayList<>(Arrays.asList(Recipe.recipeList));

    }

    private void setViewById(View view) { // View에 id 세팅
        listButton = view.findViewById(R.id.plus);
        listButton.setOnClickListener(onClickMenu);
        homeScrollView = view.findViewById(R.id.homeScrollView);
        totalListView = view.findViewById(R.id.total_recipe_listview);
        popularListView = view.findViewById(R.id.popular_listview);
        recentListView = view.findViewById(R.id.recent_listview);

    }

    public void setAdapter() { // RecyclerView 어댑터 세팅
        MyListDecoration decoration = new MyListDecoration();

        LinearLayoutManager totalLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        totalListView.setLayoutManager(totalLayoutManager);
        RecipeListAdapter totalAdapter = new RecipeListAdapter(getContext(), itemList, onClickItem);
        totalListView.setAdapter(totalAdapter);
        totalListView.addItemDecoration(decoration);

        LinearLayoutManager popularLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        popularListView.setLayoutManager(popularLayoutManager);
        RecipeListAdapter popularAdapter = new RecipeListAdapter(getContext(), itemList, onClickItem);
        popularListView.setAdapter(popularAdapter);
        popularListView.addItemDecoration(decoration);

    }

    private ArrayList<Recipe> getRecentRecipeList() {
        ArrayList<Recipe> list = new ArrayList<>();
        String[] string_list = recent_recipes.split(",");

        if (string_list[0].compareTo("") == 0)
            return list;

        for (int i = 0; i < string_list.length; i++) {
            list.add(itemList.get(Integer.parseInt(string_list[i])));
        }

        return list;
    }

    private View.OnClickListener onClickItem = new View.OnClickListener() {
        // 레시피 소개 액티비티 전환
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), RecipeIntroduction.class);
            intent.putExtra("recipe", itemList.get((int) v.getTag()));
            startActivity(intent);
        }
    };

    private View.OnClickListener onClickMenu = new View.OnClickListener() {
        // 전체 레시피 메뉴 프레그먼트 전환
        @Override
        public void onClick(View v) {
            RecipeListFragment newFragment = new RecipeListFragment();
            FragmentTransaction transaction = ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_home, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    };
}

