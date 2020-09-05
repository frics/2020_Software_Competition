package kr.ac.ssu.billysrecipe.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListData;
import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListDataBase;
import kr.ac.ssu.billysrecipe.ServerConnect.SendScrap;
import kr.ac.ssu.billysrecipe.recipe.Recipe;

public class MainRecipeListAdapter extends RecyclerView.Adapter<MainRecipeListAdapter.ViewHolder> implements Filterable {

    private Recipe item;
    private ScrapListDataBase db;
    private Drawable grey, red;

    private Context context;
    private View.OnClickListener onClickItem;
    private ArrayList<Recipe> unFilteredList;
    private ArrayList<Recipe> FilteredList;

    public MainRecipeListAdapter(Context context, ArrayList<Recipe> itemList, View.OnClickListener onClickItem) {
        this.context = context;
        this.unFilteredList = itemList;
        this.FilteredList = itemList;
        this.onClickItem = onClickItem;
        this.grey = context.getDrawable(R.drawable.ic_grey_heart);
        this.red = context.getDrawable(R.drawable.ic_red_heart);
    }

    @Override
    public MainRecipeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.main_recipe_list_item_tv, parent, false);

        return new MainRecipeListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainRecipeListAdapter.ViewHolder holder, int position) {
        item = FilteredList.get(position);

        holder.itemView.setTag(item.num); // 태그설정
        Glide.with(context) // 이미지 설정
                .load(item.image_url)
                .error(R.drawable.basic)
                .into(holder.food_image);
        holder.food_name.setText(item.name); // 음식명 설정
        holder.food_percent.setText(item.percent + "%"); // 퍼센트 설정

        // 스크랩 여부 확인 후 뷰 세팅
        db = Room.databaseBuilder(context, ScrapListDataBase.class, "scraplist.db").allowMainThreadQueries().build();
        ScrapListData data = db.Dao().findData(item.num + 1);
        if (data.getScraped() == 1) {
            holder.scrapButton.setImageDrawable(red);
            holder.scrapButton.setTag(true);
        } else {
            holder.scrapButton.setImageDrawable(grey);
            holder.scrapButton.setTag(false);
        }

        // 재료리스트 삽입
        String lists = new String();
        Recipe.Ingredient ingredient = new Recipe.Ingredient(null, null);

        for (int i = 0; i < item.ingredient.size(); i++) {
            lists = lists.concat(item.ingredient.get(i).name + ", ");
        }

        lists = lists.substring(0, lists.length() - 2);
        holder.ingredients.setText(lists);
    }

    @Override
    public int getItemCount() {
        return FilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();

                if (charString.isEmpty()) { // 검색명이 없는 경우
                    FilteredList = unFilteredList;
                } else { // 검색명이 존재하는 경우 search 진행
                    ArrayList<Recipe> filteringList = new ArrayList<>(); // 필터링중인 리스트

                    for (Recipe recipe : unFilteredList) { // 필터링안된 리스트를 순환하며
                        if (recipe.name.toLowerCase().contains(charString.toLowerCase())) { // 검색어가 포함되면 필터링 리스트에 추가
                            filteringList.add(recipe);
                        }
                    }

                    FilteredList = filteringList; // 완료되면 필터링된 리스트에 추가
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = FilteredList;
                return filterResults; // 결과물을 세팅 후 리턴
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) { // 검색 완료된 것 세팅
                FilteredList = (ArrayList<Recipe>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView food_image, scrapButton;
        private TextView food_name, food_percent, ingredients;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(onClickItem);
            food_image = itemView.findViewById(R.id.main_food_image);
            scrapButton = itemView.findViewById(R.id.main_scrap);

            scrapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        item = FilteredList.get(pos);
                    }
                    ScrapListData data = db.Dao().findData(item.num + 1);
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

                    SendScrap sendScrap = new SendScrap(context, data);
                    sendScrap.execute();

                }
            });

            food_name = itemView.findViewById(R.id.main_food_title);
            food_percent = itemView.findViewById(R.id.main_food_percent);
            ingredients = itemView.findViewById(R.id.main_food_ingredients);
        }
    }

}