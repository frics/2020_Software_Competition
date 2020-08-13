package kr.ac.ssu.myrecipe.recipe;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

// 레시피 클래스
public class Recipe implements Serializable {
    public int num; // 번호
    public String name; // 음식명
    public ImageView image; // 추후 url로 교체가능하면 교체
    public ArrayList<Ingredient> ingredient; // 재료리스트

    public Recipe(int num, String name, ImageView image, ArrayList<Ingredient> ingredient) {
        this.num = num;
        this.name = name;
        this.image = image;
        this.ingredient = ingredient;
    }

    public static class Ingredient implements Serializable{
        public String name;
        public String quantity;

        public Ingredient(String name, String quantity){
            this.name = name;
            this.quantity = quantity;
        }
    }
}
