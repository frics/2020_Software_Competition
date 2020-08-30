package kr.ac.ssu.myrecipe.recipe;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import kr.ac.ssu.myrecipe.MainActivity;
import kr.ac.ssu.myrecipe.RefrigerRatorDB.RefrigeratorData;
import kr.ac.ssu.myrecipe.RefrigerRatorDB.RefrigeratorDataBase;
import kr.ac.ssu.myrecipe.recipe.Recipe;

public class RecipeOrder {
    public static ArrayList<RecipeOrder> OrderList;

    public int num;
    public int percent;



    public RecipeOrder(int num){
        this.num = num;
        this.percent = 0;
    }

    public static void RenewOrder(Context context){
        RefrigeratorDataBase db;

        db = Room.databaseBuilder(context, RefrigeratorDataBase.class, "refrigerator.db").allowMainThreadQueries().build();

        List<RefrigeratorData> dbData = db.Dao().sortData();
        for(int i = 0; i < dbData.size(); i++) {
            Log.d("TAG", dbData.get(i).getTag());
        }
    }

    public static void InitList() {
        OrderList = new ArrayList<>();
        RecipeOrder recipeOrder;
        for(int i = 0; i < Recipe.TOTAL_RECIPE_NUM; i++){
            recipeOrder = new RecipeOrder(i);
            OrderList.add(recipeOrder);
        }
    }
}
