package kr.ac.ssu.myrecipe.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;

import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.recipe.Recipe;

public class ScrapListAdapter extends RecyclerView.Adapter<ScrapListAdapter.ViewHolder> {

    private ArrayList<Integer> itemList;
    private Context context;
    private View.OnClickListener onClickItem;

    public ScrapListAdapter(Context context, ArrayList<Integer> itemList, View.OnClickListener onClickItem) {
        this.context = context;
        this.itemList = itemList;
        this.onClickItem = onClickItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.scrap_list_item_tv, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int item = itemList.get(position);
        Log.d("TAG", "onBindViewHolder: " + item);
        holder.nameText.setText(Recipe.recipeList[item].name);
        holder.percentText.setText(Recipe.recipeList[item].percent + "%");
        holder.itemView.setTag(Recipe.recipeList[item].num);
        Glide.with(context)
                .load(Recipe.recipeList[item].image_url)
                .error(R.drawable.basic)
                .into(holder.imageview);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText, percentText;
        public ImageView imageview;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(onClickItem);
            nameText = itemView.findViewById(R.id.scrap_list_food_name);
            percentText = itemView.findViewById(R.id.scrap_list_food_percent);
            imageview = itemView.findViewById(R.id.scrap_list_food_image);
        }
    }
}