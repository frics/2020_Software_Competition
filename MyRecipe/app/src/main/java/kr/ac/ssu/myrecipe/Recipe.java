package kr.ac.ssu.myrecipe;

import android.widget.ImageView;

import java.util.ArrayList;

// 레시피 클래스
public class Recipe {
    public int num; // 번호
    public String name; // 음식명
    public ImageView image; // 추후 url로 교체가능하면 교체
    public ArrayList<String> ingredient; // 재료

    public Recipe(int num, String name, ImageView image, ArrayList<String> ingredient) {
        this.num = num;
        this.name = name;
        this.image = image;
        this.ingredient = ingredient;
    }
}
