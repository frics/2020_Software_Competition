package kr.ac.ssu.billysrecipe.recipe;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorData;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorDataBase;

public class RecipeOrderList { // 레시피 리스트 컨트롤 클래스

    public static void RenewOrder(Context context) {
        int pos;
        RefrigeratorDataBase db;
        db = Room.databaseBuilder(context, RefrigeratorDataBase.class, "refrigerator.db").allowMainThreadQueries().build();
        List<RefrigeratorData> dbData = db.Dao().sortData();

        for (int j = 0; j < Recipe.TOTAL_RECIPE_NUM; j++) { // 전체 레시피와 비교

            // 완성도 정렬 테이블에서 레시피 찾음
            for(pos =0; pos < Recipe.TOTAL_RECIPE_NUM; pos++)
                if(Recipe.orderTable[pos] == Recipe.recipeList[j].num)
                    break;

            int total_tag_num = Recipe.recipeList[pos].tag_list.size(); // 각 레시피의 재료태그의 수
            int count = 0;

            for (int i = 0; i < total_tag_num; i++) { // 태그와 현재 냉장고 리스트를 비교하여 완성도 측정
                for (int k = 0; k < dbData.size(); k++) {
                    if (Recipe.recipeList[pos].tag_list.get(i).compareTo(dbData.get(k).getTag()) == 0)
                        count++;
                }
            }

            Recipe.recipeList[pos].percent = (int) (100 * ((double) count / total_tag_num));

          /*  for (int i = 0; i < Recipe.TOTAL_RECIPE_NUM; i++) {
                if (Recipe.recipeList[i].name.compareTo(Recipe.myRecipeList[j].name) == 0) {
                    Recipe.recipeList[i].percent = Recipe.myRecipeList[j].percent;
                    break;
                }
            }
           */

        }

        ArrayList<Recipe> itemList = new ArrayList<>(Arrays.asList(Recipe.recipeList));
        Collections.sort(itemList, new DescendingPercents()); // 완성도기준 내림차순으로 정렬

        for (int j = 0; j < Recipe.TOTAL_RECIPE_NUM; j++) {  // ArrayList to list
            Recipe.orderTable[j] = itemList.get(j).num;
        //    Log.d("TAG", j+"/"+itemList.get(j).num+"/"+itemList.get(j).percent);
        }
    }

    public static void InitRank() throws CloneNotSupportedException {
        // 서버통신코드필요..
        // 수정 수정 수정
        for (int i = 0; i < 30; i++) {
            Recipe.RankingList[i] = (Recipe) Recipe.recipeList[i * 2].clone();
        }
        Recipe.RankingList[2] = (Recipe) Recipe.recipeList[199].clone();

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
