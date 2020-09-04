package kr.ac.ssu.billysrecipe.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.ssu.billysrecipe.R;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    private ArrayList<Item> items;
    private Context context;
    private OnItemClickListener mListener = null;

    public ShoppingListAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.shopping_list_item, parent, false) ;
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String packName = context.getPackageName();
        String resName = "@drawable/ic_tag";
        int num = items.get(position).tagNumber;
        if(items.get(position).tagNumber  < 10)
            resName = resName + "00";
        else if(items.get(position).tagNumber  < 100)
            resName = resName + "0";
        int id = context.getResources().getIdentifier(resName+(num), "drawable", packName);
        holder.imageView.setImageResource(id);
        holder.textView.setText(items.get(position).name);
    }

    @Override
    public int getItemCount() {
        return items.size();
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
            imageView = (ImageView)itemView.findViewById(R.id.shopinglist_image);
            textView = (TextView)itemView.findViewById(R.id.shopinglist_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    int pos = getAdapterPosition() ;
//                    if (pos != RecyclerView.NO_POSITION) {
//                        if(mListener != null)
//                            mListener.onItemClick(view, pos);
//                    }
                    if(items.get(getAdapterPosition()).flag == 0) {
                        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        items.get(getAdapterPosition()).flag = 1;
                    }
                    else {
                        textView.setPaintFlags(0);
                        items.get(getAdapterPosition()).flag = 0;
                    }
                }
            });
        }
    }
    public static class Item{
        private String tag;
        private String name;
        private int tagNumber;
        public int flag = 0;

        public String getTag() {
            return tag;
        }

        public String getName() {
            return name;
        }

        public int getTagNumber() {
            return tagNumber;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTagNumber(int tagNumber) {
            this.tagNumber = tagNumber;
        }
    }
}
