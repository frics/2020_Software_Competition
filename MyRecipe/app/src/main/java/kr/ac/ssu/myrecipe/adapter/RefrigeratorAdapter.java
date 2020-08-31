package kr.ac.ssu.myrecipe.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.R;

public class RefrigeratorAdapter extends RecyclerView.Adapter<RefrigeratorAdapter.ViewHolder> implements Filterable {
    public ArrayList<Data> filteredList;
    public ArrayList<Data> unFilteredlist;
    public ArrayList<ArrayList<Data>> allItems;
    private Context context;
    private OnItemClickListener mListener = null;

    public RefrigeratorAdapter(Context context, ArrayList<Data> items, ArrayList<ArrayList<Data>> allItems) {
        this.context = context;
        this.filteredList = items;
        this.unFilteredlist = items;
        this.allItems = allItems;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }
    @NonNull
    @Override
    public RefrigeratorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.refrigerator_list, parent, false) ;
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RefrigeratorAdapter.ViewHolder holder, final int position) {
        int index = position * 4;
        int id;
        String res = "@drawable/ic_tag";
        String packName = context.getPackageName();

        if(filteredList.size() > index){
            //100단위로 자름
            String resName = "@drawable/ic_tag";
            int num = filteredList.get(index).tagNumber;
            if(filteredList.get(index).tagNumber  < 10)
                resName = res + "00";
            else if(filteredList.get(index).tagNumber  < 100)
                resName = res + "0";
            id = context.getResources().getIdentifier(resName+(num), "drawable", packName);
            holder.image1.setImageResource(id);
            holder.text1.setText(filteredList.get(index).name);
            holder.layout1.setVisibility(View.VISIBLE);
        }
        else
            holder.layout1.setVisibility(View.INVISIBLE);
        if(filteredList.size() > index + 1){
            String resName = "@drawable/ic_tag";
            if(filteredList.get(index + 1).tagNumber  < 10)
                resName = res + "00";
            else if(filteredList.get(index + 1).tagNumber  < 100)
                resName = res + "0";
            int num = filteredList.get(index + 1).tagNumber;
            Log.e("test",resName + num);
            id = context.getResources().getIdentifier(resName+(num), "drawable", packName);
            holder.image2.setImageResource(id);
            holder.text2.setText(filteredList.get(index+1).name);
            holder.layout2.setVisibility(View.VISIBLE);
        }
        else
            holder.layout2.setVisibility(View.INVISIBLE);
        if(filteredList.size() > index + 2) {
            String resName = "@drawable/ic_tag";
            if(filteredList.get(index + 2).tagNumber  < 10)
                resName = res + "00";
            else if(filteredList.get(index + 2).tagNumber  < 100)
                resName = res + "0";
            int num = filteredList.get(index + 2).tagNumber;
            Log.e("test",resName + num);
            id = context.getResources().getIdentifier(resName+(num), "drawable", packName);
            holder.image3.setImageResource(id);
            holder.text3.setText(filteredList.get(index + 2).name);
            holder.layout3.setVisibility(View.VISIBLE);
        }
        else
            holder.layout3.setVisibility(View.INVISIBLE);
        if(filteredList.size() > index + 3) {
            String resName = "@drawable/ic_tag";
            if(filteredList.get(index + 3).tagNumber  < 10)
                resName = res + "00";
            else if(filteredList.get(index + 3).tagNumber  < 100)
                resName = res + "0";
            int num = filteredList.get(index + 3).tagNumber;
            Log.e("test",resName + num);
            id = context.getResources().getIdentifier(resName+(num), "drawable", packName);
            holder.image4.setImageResource(id);
            holder.text4.setText(filteredList.get(index + 3).name);
            holder.layout4.setVisibility(View.VISIBLE);
        }
        else
            holder.layout4.setVisibility(View.INVISIBLE);

    }

    public void changeData(ArrayList<Data> items){
        filteredList = items;
        unFilteredlist = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (filteredList.size() / 4) + 1;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    filteredList = unFilteredlist;
                }
                else {
                    ArrayList<Data> filteringList = new ArrayList<>();
                    for(int i = 0; i < unFilteredlist.size(); i++) {
                        if(unFilteredlist.get(i).name.toLowerCase().contains(charString.toLowerCase())) {
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
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private LinearLayout layout1;
        private ImageView image1;
        private TextView text1;
        private LinearLayout layout2;
        private ImageView image2;
        private TextView text2;
        private LinearLayout layout3;
        private ImageView image3;
        private TextView text3;
        private LinearLayout layout4;
        private ImageView image4;
        private TextView text4;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout1 = itemView.findViewById(R.id.refri_list_layout1);
            image1 = itemView.findViewById(R.id.refri_list_image1);
            image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        if(mListener != null) {
                            mListener.onItemClick(view, pos*4);
                        }
                    }
                }
            });
            text1 = itemView.findViewById(R.id.refri_list_text1);
            layout2 = itemView.findViewById(R.id.refri_list_layout2);
            image2 = itemView.findViewById(R.id.refri_list_image2);
            image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        if(mListener != null) {
                            mListener.onItemClick(view, pos*4 + 1);
                        }
                    }
                }
            });
            text2 = itemView.findViewById(R.id.refri_list_text2);
            layout3 = itemView.findViewById(R.id.refri_list_layout3);
            image3 = itemView.findViewById(R.id.refri_list_image3);
            image3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        if(mListener != null) {
                            mListener.onItemClick(view, pos*4 + 2);
                        }
                    }
                }
            });
            text3 = itemView.findViewById(R.id.refri_list_text3);
            layout4 = itemView.findViewById(R.id.refri_list_layout4);
            image4 = itemView.findViewById(R.id.refri_list_image4);
            image4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        if(mListener != null) {
                            mListener.onItemClick(view, pos*4 + 3);
                        }
                    }
                }
            });
            text4 = itemView.findViewById(R.id.refri_list_text4);
        }
    }
    public static class Data{
        public String category;
        public String tag;
        public String name;
        public int tagNumber;

        public Data(String category, String tag, String name, int tagNumber) {
            this.category = category;
            this.tag = tag;
            this.name = name;
            this.tagNumber = tagNumber;
        }

        public Data(){
        }
    }
}
