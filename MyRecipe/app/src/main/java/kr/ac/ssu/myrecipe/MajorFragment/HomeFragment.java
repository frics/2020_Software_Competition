package kr.ac.ssu.myrecipe.MajorFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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
    private ArrayList<Recipe> itemList;
    private TextView listButton;
    private CardView recipeView;
    private RecyclerView listView;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        itemList = new ArrayList<>(Arrays.asList(Recipe.recipeList));

        setViewById(view);
        setAdapter();

        return view;
    }

    private void setViewById(View view) { // View에 id 세팅
        listButton = view.findViewById(R.id.plus);
        listButton.setOnClickListener(onClickMenu);
        recipeView = view.findViewById(R.id.recipe_view);
        listView = view.findViewById(R.id.main_listview);
    }

    private void setAdapter() { // RecyclerView 어댑터 세팅
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        listView.setLayoutManager(layoutManager);
        RecipeListAdapter m_adapter = new RecipeListAdapter(getContext(), itemList, onClickItem);
        listView.setAdapter(m_adapter);
        MyListDecoration decoration = new MyListDecoration();
        listView.addItemDecoration(decoration);
    }

    private View.OnClickListener onClickItem = new View.OnClickListener() {
        // 레시피 소개 액티비티 전환
        @Override
        public void onClick(View v) {
            String str = (String) v.getTag();
            Intent intent = new Intent(getContext(), RecipeIntroduction.class);
            intent.putExtra("recipe", itemList.get(Integer.parseInt(str)));
            startActivity(intent);
        }
    };

    private View.OnClickListener onClickMenu = new View.OnClickListener() {
        // 전체 레시피 메뉴 프레그먼트 전환
        @Override
        public void onClick(View v) {
            ((MainActivity) getActivity()).replaceFragment(RecipeListFragment.newInstance());
            recipeView.setVisibility(View.INVISIBLE);
        }
    };
}

