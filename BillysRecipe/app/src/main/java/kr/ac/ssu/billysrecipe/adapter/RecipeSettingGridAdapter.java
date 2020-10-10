package kr.ac.ssu.billysrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.recipe.Recipe;

public class RecipeSettingGridAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<String> itemList;
    LayoutInflater inf;
    TextView names[];

    public RecipeSettingGridAdapter(Context context, int layout, ArrayList<String> itemList) {
        this.context = context;
        this.layout = layout;
        this.itemList = itemList;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        this.names = new TextView[itemList.size()];
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

        names[position] = convertView.findViewById(R.id.tag_name);

        names[position].setText(itemList.get(position));

        // 기존 예외 목록에 맞춰 출력
        boolean isExist = false;
        for(int i = 0; i < Recipe.exceptionList.size(); i++)
            if(Recipe.exceptionList.get(i).compareTo(itemList.get(position)) == 0) {
                isExist = true;
                break;
            }

        if(isExist){
            names[position].setBackground(ContextCompat.getDrawable(context, R.drawable.tag_round_box));
            names[position].setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
            names[position].setTag(false);
        } else {
            names[position].setBackground(ContextCompat.getDrawable(context, R.drawable.tag_round_box_select));
            names[position].setTextColor(ContextCompat.getColor(context, R.color.white));
            names[position].setTag(true);
        }

        names[position].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView name = (TextView)view;
                if((Boolean)view.getTag()) {
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.tag_round_box));
                    name.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
                    view.setTag(false);
                    Recipe.exceptionList.add((String)name.getText());
                }
                else {
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.tag_round_box_select));
                    name.setTextColor(ContextCompat.getColor(context, R.color.white));
                    view.setTag(true);
                    Recipe.exceptionList.remove(name.getText());
                }
            }
        });

        return convertView;
    }

    public void selectAll(boolean Case) {
        for(int i = 0; i < itemList.size(); i++){
            if(Case) {
                names[i].setBackground(ContextCompat.getDrawable(context, R.drawable.tag_round_box));
                names[i].setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
                names[i].setTag(false);
                Recipe.exceptionList.add((String)names[i].getText());

            }
            else{
                names[i].setBackground(ContextCompat.getDrawable(context, R.drawable.tag_round_box_select));
                names[i].setTextColor(ContextCompat.getColor(context, R.color.white));
                names[i].setTag(true);
                Recipe.exceptionList.clear();
            }
        }
    }
}