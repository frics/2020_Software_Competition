package kr.ac.ssu.myrecipe.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import kr.ac.ssu.myrecipe.MainActivity;
import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.RefrigerRatorDB.RefrigeratorData;
import kr.ac.ssu.myrecipe.RefrigerRatorDB.RefrigeratorDataBase;
import kr.ac.ssu.myrecipe.recipe.Recipe;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.ViewHolder> {

    private ArrayList<Recipe.Ingredient> itemList;
    private Recipe recipe;
    private Context context;

    public IngredientListAdapter(Context context, ArrayList<Recipe.Ingredient> itemList, Recipe recipe) {
        this.context = context;
        this.itemList = itemList;
        this.recipe = recipe;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.ingredient_item_tv, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe.Ingredient item = itemList.get(position);

        RefrigeratorDataBase db;
        db = Room.databaseBuilder(context, RefrigeratorDataBase.class, "refrigerator.db").allowMainThreadQueries().build();
        List<RefrigeratorData> dbData = db.Dao().sortData();

        holder.check_button.setTag(false);
        holder.check_button.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.grey)));
        holder.check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((boolean)view.getTag() == true) {
                    view.setTag(false);
                    view.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.grey)));
                }
                else{
                    view.setTag(true);
                    view.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.colorPrimary)));
                }

            }
        });

        Log.d("TAG", "야 범인잡아라 ! ! !");
        for (int i = 0; i < dbData.size(); i++) {
            Log.d("TAG", "onBindViewHolder: " + recipe.tag_list.get(position));
            if (dbData.get(i).getTag().compareTo(recipe.tag_list.get(position)) == 0) {
                Log.d("TAG", "onBindViewHolder: " + recipe.tag_list.get(position));
                holder.check_button.setTag(true);
                holder.check_button.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.colorPrimary)));
                break;
            }
        }

        holder.ingredient_name.setText(item.name); // 재료명 세팅
        holder.ingredient_quantity.setText(item.quantity); // 재료양 세팅
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public FloatingActionButton check_button;
        public TextView ingredient_name, ingredient_quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            check_button = itemView.findViewById(R.id.check_button);
            ingredient_name = itemView.findViewById(R.id.ingredient_name);
            ingredient_quantity = itemView.findViewById(R.id.ingredient_quantity);
        }
    }
}