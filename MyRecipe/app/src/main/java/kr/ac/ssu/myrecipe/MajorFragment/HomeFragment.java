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
                LinearLayoutManager.HORIZONTAL, false);
        listview.setLayoutManager(layoutManager);

        itemList = new ArrayList<>();
        ArrayList<Recipe.Ingredient> tmp = new ArrayList<>();
        Recipe.Ingredient ingredient = new Recipe.Ingredient("고구마", "100g(2/3개)");
        tmp.add(ingredient);
        ingredient = new Recipe.Ingredient("설탕", "2g(1/3 작은술)");
        tmp.add(ingredient);
        ingredient = new Recipe.Ingredient("찹쌀가루", "3g(2/3 작은술)");
        tmp.add(ingredient);
        ingredient = new Recipe.Ingredient("물", "200ml(1컵)");
        tmp.add(ingredient);
        ingredient = new Recipe.Ingredient("잣", "8g(8알)");
        tmp.add(ingredient);

        ArrayList<String> tmp1 = new ArrayList<>();
        tmp1.add("고구마는 깨끗이 씻어서 껍질을 벗기고 4cm 정도로 잘라준다.");
        tmp1.add("찜기에 고구마를 넣고 20~30분 정도 삶아 주고, 블렌더나 체를 이용하여 잘 으깨어 곱게 만든다.");
        tmp1.add("고구마와 물을 섞어 끓이면서 찹쌀가루로 농도를 맞추고 설탕을 넣어 맛을 낸다.");
        tmp1.add("잣을 팬에 노릇하게 볶아 다져서 고구마 죽에 섞는다. 기호에 따라 고구마를 튀겨 얹어 먹어도 좋다");

        Recipe recipe = new Recipe(0, "육개장", null, tmp, tmp1);
        itemList.add(recipe);
        recipe = new Recipe(1, "어묵탕", null, tmp, tmp1);
        itemList.add(recipe);
        recipe = new Recipe(2, "떡국", null, tmp, tmp1);
        itemList.add(recipe);
        recipe = new Recipe(3, "쇠고기가지볶음", null, tmp, tmp1);
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

