package kr.ac.ssu.billysrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.ac.ssu.billysrecipe.R;

public class DietListAdapter extends RecyclerView.Adapter<DietListAdapter.ViewHolder> {
    private ArrayList<Data> items;
    private Context context;
    public DietListAdapter(ArrayList<Data> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public DietListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.diet_item, parent, false);
        DietListAdapter.ViewHolder vh = new DietListAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull DietListAdapter.ViewHolder holder, int position) {
        if(position == 0){
            holder.icon.setImageResource(R.drawable.ic_sunrise);
            holder.mealtime.setText("아침");
            holder.view.setVisibility(View.VISIBLE);
        }
        else if(position == 1){
            holder.icon.setImageResource(R.drawable.ic_sun);
            holder.mealtime.setText("점심");
            holder.view.setVisibility(View.VISIBLE);
        }
        else{
            holder.icon.setImageResource(R.drawable.ic_moon);
            holder.mealtime.setText("저녁");
            holder.view.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(items.get(position).photo)
                .error(R.drawable.basic)
                .into(holder.photo);
        holder.calorie.setText(items.get(position).kcal + "kcal");
        holder.name.setText(items.get(position).name);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void changeData(ArrayList<DietListAdapter.Data> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public int getTotalCalories(){
        return items.get(0).kcal + items.get(1).kcal + items.get(2).kcal;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView mealtime;
        ImageView photo;
        TextView name;
        TextView calorie;
        View view;

        ViewHolder(View itemView) {
            super(itemView) ;

            icon = itemView.findViewById(R.id.diet_item_icon);
            mealtime = itemView.findViewById(R.id.diet_item_mealtime);
            photo = itemView.findViewById(R.id.diet_item_photo);
            name = itemView.findViewById(R.id.diet_item_name);
            calorie = itemView.findViewById(R.id.diet_item_calorie);
            view = itemView.findViewById(R.id.diet_item_view);
        }
    }

    public static class Data{
        private String photo;   //수정필요
        private String name;
        private int kcal;

        public Data(String photo, String name, int kcal) {
            this.photo = photo;
            this.name = name;
            this.kcal = kcal;
        }

        public Data(){}

        public String getPhoto() {
            return photo;
        }

        public String getName() {
            return name;
        }

        public int getKcal() {
            return kcal;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setKcal(int kcal) {
            this.kcal = kcal;
        }
    }
}
