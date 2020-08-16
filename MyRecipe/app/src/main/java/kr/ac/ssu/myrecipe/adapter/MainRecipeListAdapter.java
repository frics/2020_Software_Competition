package kr.ac.ssu.myrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.recipe.Recipe;

public class MainRecipeListAdapter extends RecyclerView.Adapter<MainRecipeListAdapter.ViewHolder> {

    private ArrayList<Recipe> itemList;
    private Context context;
    private View.OnClickListener onClickItem;

    public MainRecipeListAdapter(Context context, ArrayList<Recipe> itemList, View.OnClickListener onClickItem) {
        this.context = context;
        this.itemList = itemList;
        this.onClickItem = onClickItem;
    }

    @Override
    public MainRecipeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // context 와 parent.getContext() 는 같다.
        View view = LayoutInflater.from(context)
                .inflate(R.layout.main_recipe_list_item_tv, parent, false);

        return new MainRecipeListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainRecipeListAdapter.ViewHolder holder, int position) {
        Recipe item = itemList.get(position);
        holder.itemView.setTag(Integer.toString(position));
        holder.food_image.setImageResource(R.drawable.soup0 + position);
        holder.food_name.setText(item.name);
        holder.food_percent.setText("100%");
        holder.ingredients.setText("고구마, 설탕, 찹쌀가루, 물, 잣");
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView food_image;
        public TextView food_name, food_percent, ingredients;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(onClickItem);
            food_image = itemView.findViewById(R.id.main_food_image);
            food_name = itemView.findViewById(R.id.main_food_title);
            food_percent = itemView.findViewById(R.id.main_food_percent);
            ingredients = itemView.findViewById(R.id.main_food_ingredients);
        }
    }
}