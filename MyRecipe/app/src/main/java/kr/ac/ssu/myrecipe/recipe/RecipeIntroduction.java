package kr.ac.ssu.myrecipe.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import kr.ac.ssu.myrecipe.R;

public class RecipeIntroduction extends AppCompatActivity {
    private SlidingUpPanelLayout slidingPaneLayout;
    private View recipeBar, ingredientBar;
    private ConstraintLayout ingredientLayout, recipeLayout, slidingpanel;
    private ImageView backButton, scrapButton;
    private TextView recipeTitle, ingredientTitle;
    private boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_introduction);

        Intent intent = getIntent();

        Recipe recipe = (Recipe) intent.getSerializableExtra("itemlist");

        ImageView imageview = (ImageView) this.findViewById(R.id.food_image);
        TextView textView = (TextView) this.findViewById(R.id.food_name);

        imageview.setImageResource(R.drawable.soup0 + recipe.num);
        textView.setText(recipe.name);

        slidingPaneLayout = (SlidingUpPanelLayout) this.findViewById(R.id.sliding_layout);
        backButton = (ImageView) this.findViewById(R.id.intro_arrow);
        scrapButton = (ImageView) this.findViewById(R.id.intro_heart);
        ingredientTitle = (TextView) this.findViewById(R.id.ingredients_title);
        recipeTitle = (TextView) this.findViewById(R.id.recipe_title);
        ingredientBar = this.findViewById(R.id.ingredients_bar);
        recipeBar = this.findViewById(R.id.recipe_bar);
        recipeLayout = (ConstraintLayout) findViewById(R.id.constraint_recipe);
        ingredientLayout = (ConstraintLayout) findViewById(R.id.constraint_ingredients);

        slidingpanel = (ConstraintLayout) findViewById(R.id.sliding_panel);
        slidingpanel.setEnabled(false);

        slidingPaneLayout.setTouchEnabled(true);
        backButton.setOnClickListener(onClickIntro);
        scrapButton.setOnClickListener(onClickIntro);
        ingredientTitle.setOnClickListener(onClickIntro);
        recipeTitle.setOnClickListener(onClickIntro);
    }

    private View.OnClickListener onClickIntro = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == backButton)
                finish();
            else if (v == scrapButton) {
                Drawable grey = getDrawable(R.drawable.ic_grey_heart);
                Drawable red = getDrawable(R.drawable.ic_red_heart);
                if (!check) {
                    scrapButton.setImageDrawable(red);
                    check = true;
                } else {
                    scrapButton.setImageDrawable(grey);
                    check = false;
                }
            } else if (v == ingredientTitle) {
                ingredientBar.setVisibility(View.VISIBLE);
                ingredientLayout.setVisibility(View.VISIBLE);
                ingredientTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                recipeBar.setVisibility(View.INVISIBLE);
                recipeLayout.setVisibility(View.INVISIBLE);
                recipeTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

            } else if (v == recipeTitle) {
                ingredientBar.setVisibility(View.INVISIBLE);
                ingredientLayout.setVisibility(View.INVISIBLE);
                ingredientTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                recipeBar.setVisibility(View.VISIBLE);
                recipeLayout.setVisibility(View.VISIBLE);
                recipeTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

            }
        }
    };
}