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
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.ac.ssu.billysrecipe.R;

public class TodayGridAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<String> itemList;
    LayoutInflater inf;

    public TodayGridAdapter(Context context, int layout, ArrayList<String> itemList) {
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
        final ImageView iv = convertView.findViewById(R.id.today_food_image);
        final ConstraintLayout layout = convertView.findViewById(R.id.constraint_layout);
        GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.background_rounding);
        iv.setBackground(drawable);
        iv.setClipToOutline(true);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(layout, "rotationY", 0, 720);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Glide.with(context)
                                .load(itemList.get(position))
                                .error(R.drawable.basic)
                                .into(iv);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                animator.setDuration(2000);
                animator.start();
            }
        });
        /*
        Glide.with(context)
                .load(itemList.get(position))
                .error(R.drawable.basic)
                .into(iv);
         */
        return convertView;
    }
}