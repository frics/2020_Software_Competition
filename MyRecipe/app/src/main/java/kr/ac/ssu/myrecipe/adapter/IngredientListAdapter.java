package kr.ac.ssu.myrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.recipe.Recipe;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.ViewHolder> {

    private ArrayList<Recipe.Ingredient> itemList;
    private Context context;

    public IngredientListAdapter(Context context, ArrayList<Recipe.Ingredient> itemList) {
        this.context = context;
        this.itemList = itemList;
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
        holder.ingredient_name.setText(item.name); // 재료명 세팅
        holder.ingredient_quantity.setText(item.quantity); // 재료양 세팅
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredient_name, ingredient_quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            ingredient_name = itemView.findViewById(R.id.ingredient_name);
            ingredient_quantity= itemView.findViewById(R.id.ingredient_quantity);
        }
    }
}