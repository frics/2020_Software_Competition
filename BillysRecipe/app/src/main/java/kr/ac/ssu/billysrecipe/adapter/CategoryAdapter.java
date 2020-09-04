package kr.ac.ssu.billysrecipe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.ssu.billysrecipe.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<String> categoryList;
    private Context context;
    private int click_position;
    private OnItemClickListener mListener = null;

    public CategoryAdapter(ArrayList<String> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
        click_position = 0;
    }

    public void setClick_position(int click_position) {
        this.click_position = click_position;
    }

    public int getClick_position() {
        return click_position;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.refrigerator_category, parent, false) ;
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapter.ViewHolder holder, final int position) {
        //처음 그려질때는 자동으로 첫번째 전체 카테고리 활성화되도록 구현
        if(position == click_position)
            holder.category.setTextColor(Color.parseColor("#000000"));
        else
            holder.category.setTextColor(Color.GRAY);
        holder.category.setText(categoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView category;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.refri_category_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        if(mListener != null) {
                            mListener.onItemClick(view, pos);
                        }
                    }
                }
            });
        }
    }

}
