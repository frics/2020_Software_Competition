package kr.ac.ssu.myrecipe.recipe;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

// 레시피 클래스
public class Recipe implements Serializable {
    public int num; // 번호
    public String name; // 음식명
    public ImageView image; // 추후 url로 교체가능하면 교체(가능)
    public ArrayList<Ingredient> ingredient; // 재료리스트
    public ArrayList<String> recipe_order; // 조리 순서

    public String explanation; // 부가설명
    public double[] nutrition; // 영양분 리스트 (0번 인덱스부터, 칼로리, 탄수화물, 단백질, 지방, 당류, 나트륨, 콜레스테롤, 포화지방산, 트랜스지방)

    public Recipe(int num, String name, ImageView image, ArrayList<Ingredient> ingredient, ArrayList<String> recipe_order) {
        this.num = num;
        this.name = name;
        this.image = image;
        this.ingredient = ingredient;
        this.recipe_order = recipe_order;
    }

    public static class Ingredient implements Serializable {
        public String name;
        public String quantity;

        public Ingredient(String name, String quantity) {
            this.name = name;
            this.quantity = quantity;
        }
    }
}
