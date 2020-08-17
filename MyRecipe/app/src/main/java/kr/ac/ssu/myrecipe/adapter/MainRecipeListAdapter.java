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

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.recipe.Recipe;

public class MainRecipeListAdapter extends RecyclerView.Adapter<MainRecipeListAdapter.ViewHolder>
        implements Filterable {

    private Context context;
    private View.OnClickListener onClickItem;
    ArrayList<Recipe> unFilteredlist;
    ArrayList<Recipe> filteredList;

    public MainRecipeListAdapter(Context context, ArrayList<Recipe> itemList, View.OnClickListener onClickItem) {
        this.context = context;
        this.unFilteredlist = itemList;
        this.filteredList = itemList;
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
        Recipe item = filteredList.get(position);
        holder.itemView.setTag(Integer.toString(position));
        holder.food_image.setImageResource(R.drawable.soup0 + position);
        holder.food_name.setText(item.name);
        holder.food_percent.setText("100%");
        holder.ingredients.setText("고구마, 설탕, 찹쌀가루, 물, 잣");
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) { // 검색명이 없는 경우
                    filteredList = unFilteredlist;
                } else { // 검색명이 존재하는 경우 search 진행
                    ArrayList<Recipe> filteringList = new ArrayList<>(); // 필터링중인 리스트
                    for (Recipe recipe : unFilteredlist) { // 필터링안된 리스트를 순환하며
                        if (recipe.name.toLowerCase().contains(charString.toLowerCase())) { // 검색어가 포함되면 필터링 리스트에 추가
                            filteringList.add(recipe);
                        }
                    }
                    filteredList = filteringList; // 완료되면 필터링된 리스트에 추가
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults; // 결과물을 세팅 후 리턴
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<Recipe>) results.values;
                notifyDataSetChanged();
            }
        };
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