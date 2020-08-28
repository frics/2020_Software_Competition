package kr.ac.ssu.myrecipe.database;

import android.util.Log;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import kr.ac.ssu.myrecipe.recipe.Recipe;

public class OpenRecipeListCSV {

    public static void readDataFromCsv(InputStreamReader inputStreamReader) throws IOException {
        // csv 파일을 읽기 위한 Reader 객체 세팅
        BufferedReader reader = new BufferedReader(inputStreamReader);
        CSVReader csvReader = new CSVReader(reader);

        Recipe.InitList(); // 내부 레시피 DB 초기화

        int count = 1;
        String[] nextLine;

        while ((nextLine = csvReader.readNext()) != null) {
            for (int i = 0; i < nextLine.length; i++) {
                switch (count) // 필드별 분류
                {
                    case 1: // id값
                        Recipe.recipeList[i].num = i;
                        break;
                    case 2: // 음식명
                        Recipe.recipeList[i].name = nextLine[i];
                        break;
                    case 3: // 조리 방법
                        Recipe.recipeList[i].style = nextLine[i];
                        break;
                    case 4: // 칼로리
                        Recipe.recipeList[i].nutrition[0] = Double.parseDouble(nextLine[i]);
                        break;
                    case 5: // 탄수화물
                        Recipe.recipeList[i].nutrition[1] = Double.parseDouble(nextLine[i]);
                        break;
                    case 6: // 단백질
                        Recipe.recipeList[i].nutrition[2] = Double.parseDouble(nextLine[i]);
                        break;
                    case 7: // 지방
                        Recipe.recipeList[i].nutrition[3] = Double.parseDouble(nextLine[i]);
                        break;
                    case 8: // 나트륨
                        Recipe.recipeList[i].nutrition[4] = Double.parseDouble(nextLine[i]);
                        break;
                    case 9: // 음식이미지 URL
                        Recipe.recipeList[i].image_url = nextLine[i];
                        break;
                    case 10: // 재료리스트
                        String tmp = nextLine[i];
                        int length = tmp.length();

                        tmp = tmp.substring(0, length); // 큰따옴표 제거
                        String[] ingredients = tmp.split(","); // ','를 기준으로 split

                        /*Log.d("holo", "count" + count + " " + i + " " + nextLine[i]); 디버그용 로그문
                        for (int a = 0; a < ingredients.length; a++)
                            Log.d("hoo", ingredients[a]);
                         */

                        for (int j = 0; j < ingredients.length; j++) { // 재료명과 재료양을 split
                            int pos = ingredients[j].length();

                            while (ingredients[j].charAt(--pos) != ' ') ; // 재료명과 재료양을 구분

                            Recipe.Ingredient ingredient = new Recipe.Ingredient(ingredients[j].substring(0, pos),
                                    ingredients[j].substring(pos + 1, ingredients[j].length())); // ' '를 기준으로 Ingredient 객체에 삽입

                            //Log.d("hook", ingredient.name + "//" + ingredient.quantity);

                            // 공백 제거 및 DB에 삽입
                            ingredient.name = ingredient.name.trim();
                            ingredient.quantity = ingredient.quantity.trim();

                            if(ingredient.quantity.charAt(0) == '(' && ingredient.quantity.charAt(ingredient.quantity.length()-1) == ')')
                                ingredient.quantity = ingredient.quantity.substring(1,ingredient.quantity.length()-1);

                            Recipe.recipeList[i].ingredient.add(ingredient);
                        }
                        break;
                }

                // 레시피 순서 입력
                if (count > 10 && nextLine[i].compareTo("") != 0) {
                    Recipe.recipeList[i].recipe_order.add(nextLine[i]);
                }
            }

            if (count++ > 26)
                break;
        }
    }
}
