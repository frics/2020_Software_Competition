package kr.ac.ssu.myrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.recipe.Recipe;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private ArrayList<Recipe> itemList;
    private Context context;
    private View.OnClickListener onClickItem;

    public RecipeListAdapter(Context context, ArrayList<Recipe> itemList, View.OnClickListener onClickItem) {
        this.context = context;
        this.itemList = itemList;
        this.onClickItem = onClickItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_tv, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe item = itemList.get(position);
        holder.nameText.setText(item.name);
        holder.styleText.setText(item.style);
        holder.itemView.setTag(item.num);
        Glide.with(context)
                .load(item.image_url)
                .error(R.drawable.basic)
                .into(holder.imageview);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText, styleText;
        public ImageView imageview;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(onClickItem);
            nameText = itemView.findViewById(R.id.recipe_name);
            styleText = itemView.findViewById(R.id.recipe_style);
            imageview = itemView.findViewById(R.id.img_viewpager_childimage);
        }
    }
}