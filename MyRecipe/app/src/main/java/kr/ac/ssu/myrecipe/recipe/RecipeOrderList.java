package kr.ac.ssu.myrecipe.recipe;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kr.ac.ssu.myrecipe.RefrigerRatorDB.RefrigeratorData;
import kr.ac.ssu.myrecipe.RefrigerRatorDB.RefrigeratorDataBase;

public class RecipeOrderList {

    public static void RenewOrder(Context context) {
        RefrigeratorDataBase db;
        db = Room.databaseBuilder(context, RefrigeratorDataBase.class, "refrigerator.db").allowMainThreadQueries().build();
        List<RefrigeratorData> dbData = db.Dao().sortData();

        for (int pos = 0; pos < Recipe.TOTAL_RECIPE_NUM; pos++) { // 전체 레시피와 비교
            int total_tag_num = Recipe.recipeList[pos].tag_list.size(); // 각 레시피의 재료태그의 수
            int count = 0;

            for (int i = 0; i < total_tag_num; i++) { // 태그와 현재 냉장고 리스트를 비교하여 완성도 측정
                for (int j = 0; j < dbData.size(); j++) {
                    if (Recipe.recipeList[pos].tag_list.get(i).compareTo(dbData.get(j).getTag()) == 0)
                        count++;
                }
            }

            Recipe.recipeList[pos].percent = (int) (100 * ((double) count / total_tag_num));
        }

        ArrayList<Recipe> itemList = new ArrayList<>(Arrays.asList(Recipe.recipeList));
        Collections.sort(itemList, new DescendingPercents()); // 완성도기준 내림차순으로 정렬

        for (int pos = 0; pos < Recipe.TOTAL_RECIPE_NUM; pos++) { // ArrayList to list
            Recipe.recipeList[pos] = itemList.get(pos);
            Recipe.recipeList[pos].num = pos;
        }
    }

    static class DescendingPercents implements Comparator<Recipe> { // 정렬 기준 세팅 클래스
        @Override
        public int compare(Recipe a, Recipe b) {
            Integer A = new Integer(a.percent);
            Integer B = new Integer(b.percent);
            return B.compareTo(A);
        }
    }
}
