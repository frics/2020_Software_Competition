package kr.ac.ssu.myrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.recipe.Recipe;

public class MainRecipeListAdapter extends RecyclerView.Adapter<MainRecipeListAdapter.ViewHolder> implements Filterable {

    private Context context;
    private View.OnClickListener onClickItem;
    private ArrayList<Recipe> unFilteredList;
    private ArrayList<Recipe> FilteredList;

    public MainRecipeListAdapter(Context context, ArrayList<Recipe> itemList, View.OnClickListener onClickItem) {
        this.context = context;
        this.unFilteredList = itemList;
        this.FilteredList = itemList;
        this.onClickItem = onClickItem;
    }

    @Override
    public MainRecipeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.main_recipe_list_item_tv, parent, false);

        return new MainRecipeListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainRecipeListAdapter.ViewHolder holder, int position) {
        Recipe item = FilteredList.get(position);

        holder.itemView.setTag(item.num); // 태그설정
        Glide.with(context) // 이미지 설정
                .load(item.image_url)
                .error(R.drawable.basic)
                .into(holder.food_image);
        holder.food_name.setText(item.name); // 음식명 설정
        holder.food_percent.setText(item.percent+"%"); // 퍼센트 설정

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
        private ImageView food_image;
        private TextView food_name, food_percent, ingredients;

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