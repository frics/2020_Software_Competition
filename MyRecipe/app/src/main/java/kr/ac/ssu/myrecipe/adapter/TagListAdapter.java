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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.IconData;
import kr.ac.ssu.myrecipe.R;


public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.ViewHolder> implements Filterable {
    public static final int ADD = 1;
    public static final int RECEIPT = 0;
    public ArrayList<TagListAdapter.Item> filteredList;
    private ArrayList<TagListAdapter.Item> unFilteredlist;
    private OnItemClickListener mListener = null ;
    private Context context;
    private int flag;

    public TagListAdapter(Context context, ArrayList<TagListAdapter.Item> items, int flag){
        this.context = context;
        this.filteredList = items;
        this.unFilteredlist = items;
        this.flag = flag;
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
        if(flag == RECEIPT)
            holder.imageView.setImageResource(IconData.textToicon.get(filteredList.get(position).category));
        else if(flag == ADD){
            //전체 xml파일이 없으므로 100단위로 끊음
            String res = "@drawable/ic_tag";
            String packName = context.getPackageName();
            String resName = "@drawable/ic_tag";
            if(filteredList.get(position).tagNumber  < 10)
                resName = res + "00";
            else if(filteredList.get(position).tagNumber  < 100)
                resName = res + "0";
            int num = filteredList.get(position).tagNumber;
            int id = context.getResources().getIdentifier(resName+num, "drawable", packName);
            holder.imageView.setImageResource(id);
        }
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
        private String category;
        private String tag;
        private int tagNumber;

        public Item(String category, String tag, int tagNumber) {
            this.category = category;
            this.tag = tag;
            this.tagNumber = tagNumber;
        }

        public Item(){}

        public String getCategory() {
            return category;
        }

        public String getTag() {
            return tag;
        }

        public int getTagNumber() {
            return tagNumber;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public void setTagNumber(int tagNumber) {
            this.tagNumber = tagNumber;
        }
    }
}
