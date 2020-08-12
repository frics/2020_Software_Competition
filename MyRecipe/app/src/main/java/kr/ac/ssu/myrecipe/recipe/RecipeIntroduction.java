package kr.ac.ssu.myrecipe.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import kr.ac.ssu.myrecipe.R;

public class RecipeIntroduction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_introduction);

        Intent intent = getIntent();

        Recipe recipe = (Recipe)intent.getSerializableExtra("itemlist");

        ImageView imageview = (ImageView)this.findViewById(R.id.food_image);
        TextView textView = (TextView)this.findViewById(R.id.food_name);

        imageview.setImageResource(R.drawable.soup0+recipe.num);
        textView.setText(recipe.name);
    }
}