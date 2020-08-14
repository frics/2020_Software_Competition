package kr.ac.ssu.myrecipe.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sothree.slidinguppanel.ScrollableViewHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import kr.ac.ssu.myrecipe.MainActivity;
import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.adapter.IngredientListAdapter;
import kr.ac.ssu.myrecipe.adapter.RecipeOrderListAdapter;

public class RecipeIntroduction extends AppCompatActivity {

    private SlidingUpPanelLayout slidingPaneLayout;
    private View recipeBar, ingredientBar;
    private ConstraintLayout ingredientLayout, recipeLayout;
    private ImageView backButton, scrapButton, foodimage;
    private TextView textView, recipeTitle, ingredientTitle;
    private boolean check = false;

    private NestedScrollView mScrollableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_introduction);

        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("itemlist");

        // 음식 사진 및 이름 세팅 (퍼센트 추가해야함)
        foodimage = (ImageView) this.findViewById(R.id.food_image);
        textView = (TextView) this.findViewById(R.id.food_name);
        foodimage.setImageResource(R.drawable.soup0 + recipe.num);
        textView.setText(recipe.name);

        // 정보 뷰들 동기화
        slidingPaneLayout = (SlidingUpPanelLayout) this.findViewById(R.id.sliding_layout);
        backButton = (ImageView) this.findViewById(R.id.intro_arrow);
        scrapButton = (ImageView) this.findViewById(R.id.intro_heart);
        ingredientTitle = (TextView) this.findViewById(R.id.ingredients_title);
        recipeTitle = (TextView) this.findViewById(R.id.recipe_title);
        ingredientBar = this.findViewById(R.id.ingredients_bar);
        recipeBar = this.findViewById(R.id.recipe_bar);
        recipeLayout = (ConstraintLayout) findViewById(R.id.constraint_recipe);
        ingredientLayout = (ConstraintLayout) findViewById(R.id.constraint_ingredients);

        slidingPaneLayout.setTouchEnabled(true);
        backButton.setOnClickListener(onClickIntro);
        scrapButton.setOnClickListener(onClickIntro);
        ingredientTitle.setOnClickListener(onClickIntro);
        recipeTitle.setOnClickListener(onClickIntro);

        // 반응형 UI 생성 코드
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size); // 기기의 해상도

        slidingPaneLayout.setPanelHeight((int) (size.y * 0.4 - size.x * 0.08)); // 하단 패널 크기 세팅, 기기 y축의 약 40% - x축의 8%
        // 음식 사진 크기 세팅, y축의 약 68%
        foodimage.getLayoutParams().height = (int) (size.y * 0.68);
        foodimage.getLayoutParams().width = (int) (size.y * 0.68);
        foodimage.requestLayout();

        // listview들 세팅
        ArrayList<Recipe.Ingredient> datalist = recipe.ingredient;
        RecyclerView ingredientRecyclerView = (RecyclerView) findViewById(R.id.ingredient_list);
        RecyclerView recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_list);

        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager recipeLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        ingredientRecyclerView.setLayoutManager(ingredientLayoutManager);
        recipeRecyclerView.setLayoutManager(recipeLayoutManager);

        IngredientListAdapter adapter = new IngredientListAdapter(this, datalist);
        RecipeOrderListAdapter adapter1 = new RecipeOrderListAdapter(this, recipe.recipe_order);

        ingredientRecyclerView.setAdapter(adapter);
        recipeRecyclerView.setAdapter(adapter1);

        mScrollableView = (NestedScrollView) findViewById(R.id.scrollView);
        NestedScrollableViewHelper helper = new NestedScrollableViewHelper();
        slidingPaneLayout.setScrollableViewHelper(helper);


    }

    private View.OnClickListener onClickIntro = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == backButton) // 백 버튼
                finish();
            else if (v == scrapButton) { // 스크랩 버튼
                Drawable grey = getDrawable(R.drawable.ic_grey_heart);
                Drawable red = getDrawable(R.drawable.ic_red_heart);

                if (!check) {
                    scrapButton.setImageDrawable(red);
                    check = true;
                } else {
                    scrapButton.setImageDrawable(grey);
                    check = false;
                }
            } else if (v == ingredientTitle) { // 재료 클릭 시
                ingredientBar.setVisibility(View.VISIBLE);
                ingredientLayout.setVisibility(View.VISIBLE);
                ingredientTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                recipeBar.setVisibility(View.INVISIBLE);
                recipeLayout.setVisibility(View.INVISIBLE);
                recipeTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

            } else if (v == recipeTitle) { // 레시피 클릭 시
                ingredientBar.setVisibility(View.INVISIBLE);
                ingredientLayout.setVisibility(View.INVISIBLE);
                ingredientTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                recipeBar.setVisibility(View.VISIBLE);
                recipeLayout.setVisibility(View.VISIBLE);
                recipeTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

            }
        }
    };

    public class NestedScrollableViewHelper extends ScrollableViewHelper {
        public int getScrollableViewScrollPosition(View scrollableView, boolean isSlidingUp) {
            if (mScrollableView instanceof NestedScrollView) {
                if (isSlidingUp) {
                    return mScrollableView.getScrollY();
                } else {
                    NestedScrollView nsv = ((NestedScrollView) mScrollableView);
                    View child = nsv.getChildAt(0);
                    return (child.getBottom() - (nsv.getHeight() + nsv.getScrollY()));
                }
            } else {
                return 0;
            }
        }
    }

}