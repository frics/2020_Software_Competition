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

public class RecipeOrderListAdapter extends RecyclerView.Adapter<RecipeOrderListAdapter.ViewHolder> {

    private ArrayList<String> itemList;
    private Context context;

    public RecipeOrderListAdapter(Context context, ArrayList<String> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // context 와 parent.getContext() 는 같다.
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recipe_order_item_tv, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = itemList.get(position);
        holder.steps.setText("STEP " + (position + 1));
        holder.content.setText(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView steps, content;

        public ViewHolder(View itemView) {
            super(itemView);
            steps = itemView.findViewById(R.id.step);
            content = itemView.findViewById(R.id.content_recipe);
        }
    }
}