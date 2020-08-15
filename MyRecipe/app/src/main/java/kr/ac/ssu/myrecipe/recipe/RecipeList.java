package kr.ac.ssu.myrecipe.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.adapter.IngredientListAdapter;
import kr.ac.ssu.myrecipe.adapter.MainRecipeListAdapter;

public class RecipeList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Intent intent = getIntent();
        ArrayList<Recipe> recipes = (ArrayList<Recipe>) intent.getSerializableExtra("itemlist");

        RecyclerView RecyclerView = (RecyclerView) findViewById(R.id.main_recipe_list);

        LinearLayoutManager LayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        RecyclerView.setLayoutManager(LayoutManager);

        MainRecipeListAdapter adapter = new MainRecipeListAdapter(this, recipes);
        RecyclerView.setAdapter(adapter);
    }
}