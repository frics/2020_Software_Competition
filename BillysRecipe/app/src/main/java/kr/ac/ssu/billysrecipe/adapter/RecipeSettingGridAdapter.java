package kr.ac.ssu.billysrecipe.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.ssu.billysrecipe.R;

public class RecipeSettingGridAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<String> itemList;
    LayoutInflater inf;

    public RecipeSettingGridAdapter(Context context, int layout, ArrayList<String> itemList) {
        this.context = context;
        this.layout = layout;
        this.itemList = itemList;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inf.inflate(layout, null);

        TextView name = convertView.findViewById(R.id.tag_name);

        name.setText(itemList.get(position));

        return convertView;
    }
}