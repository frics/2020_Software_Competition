package kr.ac.ssu.billysrecipe.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.room.Room;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListData;
import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListDataBase;
import kr.ac.ssu.billysrecipe.recipe.Recipe;

public class RecipePagerAdapter extends PagerAdapter {
    private ScrapListDataBase db;
    private Drawable grey, red;
    private ImageView scrabButton;

    private LayoutInflater inflater;
    private ArrayList<Recipe> itemList;
    private Context context;
    private View.OnClickListener onClickItem;

    public RecipePagerAdapter(Context context, ArrayList<Recipe> itemList, View.OnClickListener onClickItem) {
        this.context = context;
        this.itemList = itemList;
        this.onClickItem = onClickItem;
        this.grey = context.getDrawable(R.drawable.ic_grey_heart_circle);
        this.red = context.getDrawable(R.drawable.ic_red_heart_circle);
    }

    @Override
    public int getCount() {
        return Recipe.RANK_RECIPE_NUM;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((CardView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.viewpager_recipe, container, false);
        v.setTag(itemList.get(position).num);
        v.setOnClickListener(onClickItem);

        ImageView imageView = v.findViewById(R.id.viewpager_food_image);
        scrabButton = v.findViewById(R.id.viewpager_scrap_button);
        TextView scrabText = v.findViewById(R.id.viewpager_scrap_num);
        TextView nameText = v.findViewById(R.id.viewpager_food_name);

        Glide.with(context)
                .load(itemList.get(position).image_url)
                .error(R.drawable.basic)
                .into(imageView);
        nameText.setText(itemList.get(position).name);

        // 스크랩 여부 확인 후 뷰 세팅
        db = Room.databaseBuilder(context, ScrapListDataBase.class, "scraplist.db").allowMainThreadQueries().build();
        ScrapListData data = db.Dao().findData(itemList.get(position).num + 1);


        if (data.getScraped() == 1) {
            scrabButton.setImageDrawable(red);
            scrabButton.setTag(true);
        } else {
            scrabButton.setImageDrawable(grey);
            scrabButton.setTag(false);
        }

        final int pos = position;
        scrabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrapListData data = db.Dao().findData(itemList.get(pos).num + 1);

                if ((boolean) v.getTag()) {
                    ImageView tmp = (ImageView) v;
                    tmp.setImageDrawable(grey);
                    v.setTag(false);
                    data.setScraped(0);
                } else {
                    ImageView tmp = (ImageView) v;
                    tmp.setImageDrawable(red);
                    v.setTag(true);
                    data.setScraped(1);
                }
                db.Dao().update(data);
            }
        });

        String str;
        if (data.getTotalNum() < 1000)
            str = data.getTotalNum() + "";
        else
            str = ((double) (data.getTotalNum() - (data.getTotalNum() % 100)) / 1000) + "k";

        scrabText.setText(str);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }

}