package kr.ac.ssu.myrecipe.recipe;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable, Cloneable { // 레시피 클래스

    // 레시피리스트 및 랭킹리스트
    public static final int TOTAL_RECIPE_NUM = 1200, RANK_RECIPE_NUM = 30;
    public static Recipe[] recipeList, RankingList;
    public static int[] orderTable;

    public int num; // 번호
    public String name; // 음식명
    public String style; // 음식 조리 종류
    public double[] nutrition; // 영양분 리스트 (0번 인덱스부터, 칼로리, 탄수화물, 단백질, 지방, 나트륨)
    public String image_url; // 음식사진 url
    public ArrayList<Ingredient> ingredient; // 재료리스트
    public ArrayList<String> tag_list; // 재료 태그리스트
    public ArrayList<String> recipe_order; // 조리 순서
    public int percent; // 완성도

    // 디폴트 생성자
    public Recipe() {
        this.nutrition = new double[5];
        this.ingredient = new ArrayList<>();
        this.tag_list = new ArrayList<>();
        this.recipe_order = new ArrayList<>();
    }

    // 재료명과 재료양을 기록하는 Ingredient 클래스
    public static class Ingredient implements Serializable {
        public String name; // 재료명
        public String quantity; // 재료양

        public Ingredient(String name, String quantity) {
            this.name = name;
            this.quantity = quantity;
        }
    }

    public static void InitList() { // 레시피리스트 초기화 함수
        recipeList = new Recipe[TOTAL_RECIPE_NUM];
        orderTable = new int[TOTAL_RECIPE_NUM];
        RankingList = new Recipe[RANK_RECIPE_NUM];

        for (int i = 0; i < TOTAL_RECIPE_NUM; i++) {
            recipeList[i] = new Recipe();
            orderTable[i] = i;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
