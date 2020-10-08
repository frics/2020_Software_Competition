package kr.ac.ssu.billysrecipe.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorData;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorDataBase;
import kr.ac.ssu.billysrecipe.adapter.RecipeListAdapter;
import kr.ac.ssu.billysrecipe.adapter.RecipeSettingCategoryListAdapter;

public class RecipeSettingActivity extends AppCompatActivity {
    private TextView choice_button, okay_button;
    private RecyclerView categoryListView;
    RefrigeratorDataBase db;
    boolean Case;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_setting);

        db = Room.databaseBuilder(getApplicationContext(), RefrigeratorDataBase.class, "refrigerator.db").allowMainThreadQueries().build();
        List<RefrigeratorData> dbData = db.Dao().sortData();
        ArrayList<String> itemList = new ArrayList<>();

        // 데이터베이스에 존재하는 카테고리 종류 선별
        for (int i = 0; i < dbData.size(); i++) {

            boolean isExist = false;
            String category_name = dbData.get(i).getCategory();
            Log.d("TAG", "onCreate: "+ category_name);

            for (int j = 0; j < itemList.size(); j++) {
                if (itemList.get(j).compareTo(category_name) == 0) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist)
                itemList.add(category_name);
        }
        Log.d("TAG", "onCreate: "+ itemList.size());

        categoryListView = findViewById(R.id.setting_category_listView);
        LinearLayoutManager settingLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        categoryListView.setLayoutManager(settingLayoutManager);

        final RecipeSettingCategoryListAdapter categoryListAdapter =
                new RecipeSettingCategoryListAdapter(getApplicationContext(), itemList);
        categoryListView.setAdapter(categoryListAdapter);

         Case = true;
        choice_button = findViewById(R.id.select_all_button);
        choice_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryListAdapter.selectAll(Case);
                Case = !Case;
            }
        });
        okay_button = findViewById(R.id.finish_button);
        okay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeOrderList.RenewOrder(getApplicationContext());
                finish();
            }
        });

    }
}