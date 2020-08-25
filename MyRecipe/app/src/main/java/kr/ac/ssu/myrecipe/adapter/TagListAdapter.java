package kr.ac.ssu.myrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import kr.ac.ssu.myrecipe.R;


public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.ViewHolder> implements Filterable {
    public ArrayList<TagListAdapter.Item> filteredList;
    private ArrayList<TagListAdapter.Item> unFilteredlist;
    private OnItemClickListener mListener = null ;
    private AlertDialog dialog;
    private Context context;
    //ReceiptList

    public TagListAdapter(Context context, AlertDialog dialog) {
        this.context = context;
        this.dialog = dialog;

        filteredList = new ArrayList<Item>();
        filteredList.add(new Item("과일", "자두"));
        filteredList.add(new Item("과일", "사과"));
        filteredList.add(new Item("과일", "자몽"));
        filteredList.add(new Item("채소", "두부"));
        filteredList.add(new Item("채소", "콩나물"));
        unFilteredlist = filteredList;
        //list초기화
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    filteredList = unFilteredlist;
                } else {
                    ArrayList<TagListAdapter.Item> filteringList = new ArrayList<>();
                    for(int i = 0; i < unFilteredlist.size(); i++) {
                        if(unFilteredlist.get(i).tag.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(unFilteredlist.get(i));
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //filteredList = (ArrayList<Item>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public TagListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tag_dialog_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TagListAdapter.ViewHolder holder, int position) {
        holder.textView.setText(filteredList.get(position).tag);
        if(filteredList.get(position).category.equals("채소"))
            holder.imageView.setImageResource(R.drawable.category_2);
        //카테고리 연동 코드
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.tag_row_image);
            textView = (TextView)itemView.findViewById(R.id.tag_row_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        if(mListener != null)
                            mListener.onItemClick(view, pos);
                    }
                }
            });
        }
    }

    public static class Item{
        public String category;
        public String tag;

        public Item(String category, String tag) {
            this.category = category;
            this.tag = tag;
        }
    }
}
