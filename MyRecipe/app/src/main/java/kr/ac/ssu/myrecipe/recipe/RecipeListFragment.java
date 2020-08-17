package kr.ac.ssu.myrecipe.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.Camera.CameraActivity;
import kr.ac.ssu.myrecipe.MainActivity;
import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.adapter.IngredientListAdapter;
import kr.ac.ssu.myrecipe.adapter.MainRecipeListAdapter;

public class RecipeListFragment extends Fragment {

    private ArrayList<Recipe> itemList;
    EditText editText;
    MainRecipeListAdapter adapter;

    public static RecipeListFragment newInstance() {
        return new RecipeListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

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
        recipe = new Recipe(4, "쇠고기가지볶음", null, tmp, tmp1);
        itemList.add(recipe);
        recipe = new Recipe(5, "쇠고기가지볶음", null, tmp, tmp1);
        itemList.add(recipe);

        final RecyclerView RecyclerView = (RecyclerView) view.findViewById(R.id.main_recipe_list);

        LinearLayoutManager LayoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false);
        RecyclerView.setLayoutManager(LayoutManager);

        adapter = new MainRecipeListAdapter(view.getContext(), itemList, onClickItem);
        RecyclerView.setAdapter(adapter);

        editText = (EditText) view.findViewById(R.id.edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // FAB 기본 세팅 (수정필요)
        FloatingActionButton fab = view.findViewById(R.id.go_to_top);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.smoothScrollToPosition(0);
            }
        });

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

