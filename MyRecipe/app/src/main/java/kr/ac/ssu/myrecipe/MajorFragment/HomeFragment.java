package kr.ac.ssu.myrecipe.MajorFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.adapter.RecipeListAdapter;
import kr.ac.ssu.myrecipe.adapter.MyListDecoration;
import kr.ac.ssu.myrecipe.recipe.Recipe;
import kr.ac.ssu.myrecipe.recipe.RecipeIntroduction;

public class HomeFragment extends Fragment {
    private ArrayList<Recipe> itemList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /* URL 로딩 관련 코드 였던것..
        ImageView imageview = (ImageView)view.findViewById(R.id.test);
        String url = "https://www.foodsafetykorea.go.kr/uploadimg/cook/10_00017_1.png";
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.basic)
                .error(R.drawable.basic)
                .into(imageview);


       /* ViewPager pager = (ViewPager)view.findViewById(R.id.pager);
        CustomAdapter adapter = new CustomAdapter(getLayoutInflater());

        pager.setAdapter(adapter);
*/
        RecyclerView listview = view.findViewById(R.id.main_listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false);
        listview.setLayoutManager(layoutManager);

        itemList = new ArrayList<>();
        ArrayList<Recipe.Ingredient> tmp = new ArrayList<>();
        Recipe.Ingredient ingredient= new Recipe.Ingredient("고구마", "100g(2/3개)");
        tmp.add(ingredient);
        ingredient= new Recipe.Ingredient("설탕", "2g(1/3 작은술)");
        tmp.add(ingredient);
        ingredient= new Recipe.Ingredient("찹쌀가루", "3g(2/3 작은술)");
        tmp.add(ingredient);
        ingredient= new Recipe.Ingredient("물", "200ml(1컵)");
        tmp.add(ingredient);
        ingredient= new Recipe.Ingredient("잣", "8g(8알)");
        tmp.add(ingredient);

        Recipe recipe = new Recipe(0, "육개장", null, tmp);
        itemList.add(recipe);
        recipe = new Recipe(1,"어묵탕", null, tmp);
        itemList.add(recipe);
        recipe = new Recipe(2,"떡국", null, tmp);
        itemList.add(recipe);
        recipe = new Recipe(3,"쇠고기가지볶음", null, tmp);
        itemList.add(recipe);

        RecipeListAdapter m_adapter = new RecipeListAdapter(getContext(), itemList, onClickItem);
        listview.setAdapter(m_adapter);

        MyListDecoration decoration = new MyListDecoration();
        listview.addItemDecoration(decoration);


        return view;
    }

    private View.OnClickListener onClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = (String) v.getTag();
            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getContext(), RecipeIntroduction.class);
            intent.putExtra("itemlist", itemList.get(Integer.parseInt(str)));
            startActivity(intent);

        }
    };
}

