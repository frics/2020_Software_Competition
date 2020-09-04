package kr.ac.ssu.billysrecipe.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListData;
import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListDataBase;
import kr.ac.ssu.billysrecipe.recipe.Recipe;

public class ScrapListAdapter extends RecyclerView.Adapter<ScrapListAdapter.ViewHolder> {

    int item;
    private ScrapListDataBase db;
    private Drawable grey, red;

    private ArrayList<Integer> itemList;
    private Context context;
    private View.OnClickListener onClickItem;

    public ScrapListAdapter(Context context, ArrayList<Integer> itemList, View.OnClickListener onClickItem) {
        this.context = context;
        this.itemList = itemList;
        this.onClickItem = onClickItem;
        this.grey = context.getDrawable(R.drawable.ic_grey_heart);
        this.red = context.getDrawable(R.drawable.ic_red_heart);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.scrap_list_item_tv, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        item = itemList.get(position);
        Log.d("TAG", "onBindViewHolder: " + item);
        holder.nameText.setText(Recipe.recipeList[item].name);
        holder.percentText.setText(Recipe.recipeList[item].percent + "%");
        holder.itemView.setTag(Recipe.recipeList[item].num);
        Glide.with(context)
                .load(Recipe.recipeList[item].image_url)
                .error(R.drawable.basic)
                .into(holder.imageview);

        // 스크랩 여부 확인 후 뷰 세팅
        db = Room.databaseBuilder(context, ScrapListDataBase.class, "scraplist.db").allowMainThreadQueries().build();
        ScrapListData data = db.Dao().findData(Recipe.recipeList[item].num + 1);
        if (data.getScraped() == 1) {
            holder.scrapButton.setTag(true);
            holder.scrapButton.setImageDrawable(red);
        }
        else {
            holder.scrapButton.setTag(false);
            holder.scrapButton.setImageDrawable(grey);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText, percentText;
        public ImageView imageview, scrapButton;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(onClickItem);
            nameText = itemView.findViewById(R.id.scrap_list_food_name);
            percentText = itemView.findViewById(R.id.scrap_list_food_percent);
            imageview = itemView.findViewById(R.id.scrap_list_food_image);
            scrapButton = itemView.findViewById(R.id.scrap_list_scrap_button);
            scrapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        item = itemList.get(pos);
                    }

                    ScrapListData data = db.Dao().findData(item+1);

                    if ((boolean) v.getTag()) {
                        scrapButton.setImageDrawable(grey);
                        v.setTag(false);
                        data.setScraped(0);
                    } else {
                        scrapButton.setImageDrawable(red);
                        v.setTag(true);
                        data.setScraped(1);
                    }
                    db.Dao().update(data);

                }
            });
        }
    }
}