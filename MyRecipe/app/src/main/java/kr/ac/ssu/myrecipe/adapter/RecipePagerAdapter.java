package kr.ac.ssu.myrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.recipe.Recipe;

public class RecipePagerAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    private ArrayList<Recipe> itemList;
    private Context context;
    private View.OnClickListener onClickItem;

    public RecipePagerAdapter(Context context, ArrayList<Recipe> itemList, View.OnClickListener onClickItem) {
        this.context = context;
        this.itemList = itemList;
        this.onClickItem = onClickItem;
    }

    @Override
    public int getCount() {
        return Recipe.RANK_RECIPE_NUM;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ConstraintLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.viewpager_recipe, container, false);

        v.setTag(itemList.get(position).num);
        ImageView imageView = v.findViewById(R.id.viewpager_food_image);
        TextView nameText = v.findViewById(R.id.viewpager_food_name);
        v.setOnClickListener(onClickItem);
        Glide.with(context)
                .load(itemList.get(position).image_url)
                .error(R.drawable.basic)
                .into(imageView);
        nameText.setText(itemList.get(position).name);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}