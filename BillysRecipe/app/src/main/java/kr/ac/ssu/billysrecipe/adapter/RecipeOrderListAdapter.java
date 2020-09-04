package kr.ac.ssu.billysrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.ssu.billysrecipe.R;

public class RecipeOrderListAdapter extends RecyclerView.Adapter<RecipeOrderListAdapter.ViewHolder> {

    private ArrayList<String> itemList;
    private Context context;

    public RecipeOrderListAdapter(Context context, ArrayList<String> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recipe_order_item_tv, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = itemList.get(position);
        holder.steps.setText("STEP " + (position + 1)); // "STEP"의 넘버값 세팅
        holder.content.setText(item); // 조리방법 내용 세팅
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView steps, content;

        public ViewHolder(View itemView) {
            super(itemView);
            steps = itemView.findViewById(R.id.step);
            content = itemView.findViewById(R.id.content_recipe);
        }
    }
}