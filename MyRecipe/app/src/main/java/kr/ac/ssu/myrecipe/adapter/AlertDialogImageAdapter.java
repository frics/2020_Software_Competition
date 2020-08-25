package kr.ac.ssu.myrecipe.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import kr.ac.ssu.myrecipe.R;

public class AlertDialogImageAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Boolean visable;

    public AlertDialogImageAdapter(Context context,boolean visable) {
        layoutInflater = LayoutInflater.from(context);
        this.visable = visable;
    }

    @Override
    public int getCount() {
        return iconList.length;
    }

    @Override
    public Object getItem(int position) {
        return iconList[position];
    }
    public Object getText(int position){
        return textList[position];
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        AlertDialogViewHolder alertDialogViewHolder;

        if (convertView == null) {
            // This is an alertDialog, therefore it has no root
            convertView = layoutInflater.inflate(R.layout.alert_dialog_items, null);

            DisplayMetrics metrics = convertView.getResources().getDisplayMetrics();
            int screenWidth = metrics.widthPixels;

            convertView.setLayoutParams(new GridView.LayoutParams(screenWidth / 6, screenWidth / 6));
            alertDialogViewHolder = new AlertDialogViewHolder();
            alertDialogViewHolder.icon = convertView.findViewById(R.id.image_choose_icon_entry);
            alertDialogViewHolder.text = convertView.findViewById(R.id.image_choose_text_entry);
            convertView.setTag(alertDialogViewHolder);
        } else {
            alertDialogViewHolder = (AlertDialogViewHolder) convertView.getTag();
        }

        alertDialogViewHolder.icon.setAdjustViewBounds(true);
        alertDialogViewHolder.icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        alertDialogViewHolder.icon.setPadding(8, 8, 8, 8);
        alertDialogViewHolder.icon.setImageResource(iconList[position]);
        alertDialogViewHolder.text.setText(textList[position]);
        if(position == 15 && visable == false){
            alertDialogViewHolder.icon.setVisibility(View.GONE);
            alertDialogViewHolder.text.setVisibility(View.GONE);
        }
        return convertView;
    }

    // This is your source for your icons, fill it with your own
    public Integer[] iconList = {
            R.drawable.category_1, R.drawable.category_2,
            R.drawable.category_3, R.drawable.category_4,
            R.drawable.category_5, R.drawable.category_6,
            R.drawable.category_7, R.drawable.category_8,
            R.drawable.category_9, R.drawable.category_10,
            R.drawable.category_11, R.drawable.category_12,
            R.drawable.category_13, R.drawable.category_14,
            R.drawable.category_15, R.drawable.ic_question_24dp
    };
    public String[] textList = {
            "과일", "채소","쌀/잡곡","견과/건과",
            "축산/계란", "수산물/건어물","생수/음료","커피/차",
            "초콜릿/시리얼", "면/통조림","반찬/샐러드","냉동/간편요리",
            "유제품", "가루/오일","소스","카테고리 없음"
    };
    private class AlertDialogViewHolder {
        ImageView icon;
        TextView text;
    }
}

