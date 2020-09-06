package kr.ac.ssu.billysrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorData;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorDataBase;
import kr.ac.ssu.billysrecipe.recipe.Recipe;

public class RecipeSettingCategoryListAdapter extends RecyclerView.Adapter<RecipeSettingCategoryListAdapter.ViewHolder> {

    private HashMap<String, Integer> category_hashMap;
    private ArrayList<String> itemList;
    private RefrigeratorDataBase db;
    private Context context;

    public RecipeSettingCategoryListAdapter(Context context, ArrayList<String> itemList) {
        this.context = context;
        this.itemList = itemList;

        db = Room.databaseBuilder(context, RefrigeratorDataBase.class, "refrigerator.db").allowMainThreadQueries().build();

        category_hashMap = new HashMap<>();
        category_hashMap.put("과일", 1);
        category_hashMap.put("채소", 2);
        category_hashMap.put("쌀/잡곡", 3);
        category_hashMap.put("견과/건과", 4);
        category_hashMap.put("축산/계란", 5);
        category_hashMap.put("수산물/건어", 6);
        category_hashMap.put("생수/음료", 7);
        category_hashMap.put("커피/차", 8);
        category_hashMap.put("초콜릿/시리얼", 9);
        category_hashMap.put("면/통조림", 10);
        category_hashMap.put("반찬/샐러드", 11);
        category_hashMap.put("냉동/간편요리", 12);
        category_hashMap.put("유제품", 13);
        category_hashMap.put("가루/오일", 14);
        category_hashMap.put("소스", 15);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recipe_setting_item_tv, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String packName = context.getPackageName();
        String item = itemList.get(position);
        int id = context.getResources().getIdentifier("category_" + category_hashMap.get(item), "drawable", packName);

        holder.nameText.setText(item);
        holder.imageview.setImageResource(id);

        List<RefrigeratorData> dataList = db.Dao().findDataByTag(item);
        ArrayList<String> tagList = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++)
            tagList.add(dataList.get(i).getTag());

        RecipeSettingGridAdapter recipeSettingGridAdapter = new RecipeSettingGridAdapter(context, R.layout.tag_row, tagList);
        holder.gridView.setAdapter(recipeSettingGridAdapter);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public ImageView imageview;
        public GridView gridView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.category_name);
            imageview = itemView.findViewById(R.id.category_image);
            gridView = itemView.findViewById(R.id.category_gridView);
        }
    }
}