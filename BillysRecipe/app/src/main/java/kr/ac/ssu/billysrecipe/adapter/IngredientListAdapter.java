package kr.ac.ssu.billysrecipe.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import kr.ac.ssu.billysrecipe.Camera.GetReceiptActivity;
import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorData;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorDataBase;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.ThreadTask;
import kr.ac.ssu.billysrecipe.ServerConnect.GetTag;
import kr.ac.ssu.billysrecipe.ShoppingListDB.ShoppingListData;
import kr.ac.ssu.billysrecipe.ShoppingListDB.ShoppingListDataBase;
import kr.ac.ssu.billysrecipe.ShoppingListDB.ThreadTask2;
import kr.ac.ssu.billysrecipe.recipe.Recipe;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.ViewHolder> {

    private ArrayList<Recipe.Ingredient> itemList;
    private Recipe recipe;
    private Context context;
    private ArrayList<String> shoppingList;

    public IngredientListAdapter(Context context, ArrayList<Recipe.Ingredient> itemList, Recipe recipe) {
        this.context = context;
        this.itemList = itemList;
        this.recipe = recipe;
        this.shoppingList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.ingredient_item_tv, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Recipe.Ingredient item = itemList.get(position);

        RefrigeratorDataBase db;
        ShoppingListDataBase db2;
        db = Room.databaseBuilder(context, RefrigeratorDataBase.class, "refrigerator.db").allowMainThreadQueries().build();
        db2 = Room.databaseBuilder(context, ShoppingListDataBase.class, "shoppinglist.db").allowMainThreadQueries().build();
        List<RefrigeratorData> dbData = db.Dao().sortData();
        List<ShoppingListData> dbData2 = db2.Dao().sortData();

        holder.check_button.setTag(false);
        holder.check_button.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.grey)));
        holder.check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((boolean)view.getTag() == true) {
                    view.setTag(false);
                    view.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.grey)));
                    shoppingList.remove(recipe.tag_list.get(position));
                }
                else{
                    view.setTag(true);
                    view.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.colorPrimaryLight)));
                    shoppingList.add(recipe.tag_list.get(position));
                }
            }
        });

        Log.d("TAG", "야 범인잡아라 ! ! !");
        for (int i = 0; i < dbData.size(); i++) {
            Log.d("TAG", "onBindViewHolder: " + recipe.tag_list.get(position));
            if (dbData.get(i).getTag().compareTo(recipe.tag_list.get(position)) == 0) {
                Log.d("TAG", "onBindViewHolder: " + recipe.tag_list.get(position));
                holder.check_button.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.colorPrimary)));
                holder.check_button.setClickable(false);
                break;
            }
        }
        for(int i = 0; i < dbData2.size(); i++){
            if(dbData2.get(i).getTag().compareTo(recipe.tag_list.get(position)) == 0){
                holder.check_button.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.colorPrimaryLight)));
                holder.check_button.setClickable(false);
            }
        }

        holder.ingredient_name.setText(item.name); // 재료명 세팅
        holder.ingredient_quantity.setText(item.quantity); // 재료양 세팅
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public FloatingActionButton check_button;
        public TextView ingredient_name, ingredient_quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            check_button = itemView.findViewById(R.id.check_button);
            ingredient_name = itemView.findViewById(R.id.ingredient_name);
            ingredient_quantity = itemView.findViewById(R.id.ingredient_quantity);
        }
    }

    // 장바구니 추가 메소드
    public void addShoppingList() {
        final ArrayList<TagListAdapter.Item> taglist = new ArrayList<>();;
        final ArrayList<ShoppingListData> shopdata = new ArrayList<>();
        GetTag.OnTaskCompleted listener = new GetTag.OnTaskCompleted() {
            //태그 리스트 정상
            @Override
            public void onTaskCompleted(String str) {
                ShoppingListDataBase db = Room.databaseBuilder(context, ShoppingListDataBase.class, "shoppinglist.db").build();
                for(int i = 0; i < shoppingList.size(); i++) {
                    for(int j = 0; j < taglist.size(); j++){
                        if(shoppingList.get(i).equals(taglist.get(j).getTag())){
                            ShoppingListData data = new ShoppingListData();
                            data.setName(shoppingList.get(i));
                            data.setTag(taglist.get(j).getTag());
                            data.setTagNumber(taglist.get(j).getTagNumber());
                            shopdata.add(data);
                            break;
                        }
                    }
                }
                //Shopinglist 백그라운드 저장
                ThreadTask2.OnTaskCompleted listener = new ThreadTask2.OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(String str) {
                        Toast.makeText(context,"장바구니에 추가되었습니다!",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onTaskFailure(String str) {
                        Log.e("IngredientAdapter","ThreadTask2 Failure!");
                    }
                };
                new ThreadTask2(db.Dao(), listener).execute(shopdata);
            }
            @Override
            public void onTaskFailure(String str) {
                Log.e("IngredientAdapter","GetTag Failure!");
            }
        };
        //백그라운드 수행
        new GetTag(taglist,listener).execute();
    }
}