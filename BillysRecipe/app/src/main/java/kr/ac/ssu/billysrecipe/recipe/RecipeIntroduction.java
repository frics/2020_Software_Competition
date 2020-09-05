package kr.ac.ssu.billysrecipe.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.sothree.slidinguppanel.ScrollableViewHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.ssu.billysrecipe.MajorFragment.HomeFragment;
import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListData;
import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListDataBase;
import kr.ac.ssu.billysrecipe.ServerConnect.SendScrap;
import kr.ac.ssu.billysrecipe.adapter.IngredientListAdapter;
import kr.ac.ssu.billysrecipe.adapter.RecipeOrderListAdapter;

public class RecipeIntroduction extends AppCompatActivity {

    private boolean check = false;
    private int maxWidth; // 영양성분 그래프 뷰 길이
    private Recipe recipe;
    private ScrapListDataBase db;
    private Drawable grey, red;

    // 데이터 표시 뷰
    private View recipeBar, ingredientBar, kcalBar, carbonBar, proteinBar, fatBar, sodiumBar, max_bar;
    private TextView percentText, textView, recipeTitle, ingredientTitle, kcalText, carbonText, proteinText, fatText, sodiumText;
    private TextView kcalTextBar, carbonTextBar, proteinTextBar, fatTextBar, sodiumTextBar;
    private ImageView backButton, scrapButton, foodimage;

    // 데이터 표시 레이아웃
    private ConstraintLayout ingredientLayout, recipeLayout;
    private SlidingUpPanelLayout slidingPaneLayout;
    private NestedScrollView mScrollableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_introduction);

        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra("recipe");

        setMainView();
        setPanelView();
        makeResponsiveUI();
        pushRecentRecipeList();
    }

    private void setMainView() { // 소개 화면 메인 뷰 세팅
        grey = getDrawable(R.drawable.ic_grey_heart);
        red = getDrawable(R.drawable.ic_red_heart);

        foodimage = this.findViewById(R.id.food_image);
        scrapButton = this.findViewById(R.id.intro_heart);
        textView = this.findViewById(R.id.food_name);
        percentText = this.findViewById(R.id.food_percentage);
        Glide.with(this)
                .load(recipe.image_url)
                .error(R.drawable.basic)
                .into(foodimage);
        textView.setText(recipe.name);
        percentText.setText(recipe.percent + "%");

        // 스크랩 여부 확인 후 뷰 세팅
        db = Room.databaseBuilder(this, ScrapListDataBase.class, "scraplist.db").allowMainThreadQueries().build();
        ScrapListData data = db.Dao().findData(recipe.num+1);
        if(data.getScraped() == 1) {
            scrapButton.setImageDrawable(red);
            scrapButton.setTag(true);
        }
        else {
            scrapButton.setImageDrawable(grey);
            scrapButton.setTag(false);
        }
    }

    private void makeResponsiveUI() { // 메인 레시피 반응형 UI 생성 함수
        // 반응형 UI 생성 코드
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size); // 기기의 해상도

        slidingPaneLayout.setPanelHeight((int) (size.y * 0.4 - size.x * 0.08)); // 하단 패널 크기 세팅, 기기 y축의 약 40% - x축의 8%
        // 음식 사진 크기 세팅, y축의 약 68%
        foodimage.getLayoutParams().height = (int) (size.y * 0.68);
        foodimage.getLayoutParams().width = (int) (size.y * 0.68);
        foodimage.requestLayout();
    }

    private void setPanelView() {
        // 정보 뷰들 동기화
        slidingPaneLayout = this.findViewById(R.id.sliding_layout);
        backButton = this.findViewById(R.id.intro_arrow);
        ingredientTitle = this.findViewById(R.id.ingredients_title);
        recipeTitle = this.findViewById(R.id.recipe_title);
        ingredientBar = this.findViewById(R.id.ingredients_bar);
        recipeBar = this.findViewById(R.id.recipe_bar);
        recipeLayout = findViewById(R.id.constraint_recipe);
        ingredientLayout = findViewById(R.id.constraint_ingredients);

        kcalText = findViewById(R.id.content_kcal);
        carbonText = findViewById(R.id.content_carbon);
        proteinText = findViewById(R.id.content_protein);
        fatText = findViewById(R.id.content_fat);
        sodiumText = findViewById(R.id.content_sodium);

        kcalTextBar = findViewById(R.id.kcal_quantity);
        carbonTextBar = findViewById(R.id.carbon_quantity);
        proteinTextBar = findViewById(R.id.protein_quantity);
        fatTextBar = findViewById(R.id.fat_quantity);
        sodiumTextBar = findViewById(R.id.sodium_quantity);

        kcalBar = findViewById(R.id.kcal_bar);
        carbonBar = findViewById(R.id.carbon_bar);
        proteinBar = findViewById(R.id.protein_bar);
        fatBar = findViewById(R.id.fat_bar);
        sodiumBar = findViewById(R.id.sodium_bar);

        slidingPaneLayout.setTouchEnabled(true);
        backButton.setOnClickListener(onClickIntro);
        scrapButton.setOnClickListener(onClickIntro);
        ingredientTitle.setOnClickListener(onClickIntro);
        recipeTitle.setOnClickListener(onClickIntro);

        makePanelUI();
    }

    private void makePanelUI() {
        ArrayList<Recipe.Ingredient> datalist = recipe.ingredient;
        RecyclerView ingredientRecyclerView = findViewById(R.id.ingredient_list);
        RecyclerView recipeRecyclerView = findViewById(R.id.recipe_list);

        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager recipeLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        ingredientRecyclerView.setLayoutManager(ingredientLayoutManager);
        recipeRecyclerView.setLayoutManager(recipeLayoutManager);

        IngredientListAdapter adapter = new IngredientListAdapter(this, datalist, recipe);
        RecipeOrderListAdapter adapter1 = new RecipeOrderListAdapter(this, recipe.recipe_order);

        ingredientRecyclerView.setAdapter(adapter);
        recipeRecyclerView.setAdapter(adapter1);

        mScrollableView = findViewById(R.id.scrollView);
        NestedScrollableViewHelper helper = new NestedScrollableViewHelper();
        slidingPaneLayout.setScrollableViewHelper(helper);

        kcalText.setText(Integer.toString((int) recipe.nutrition[0]));
        carbonText.setText(Double.toString(recipe.nutrition[1]));
        proteinText.setText(Double.toString(recipe.nutrition[2]));
        fatText.setText(Double.toString(recipe.nutrition[3]));
        sodiumText.setText(Double.toString(recipe.nutrition[4]));

        kcalTextBar.setText(Integer.toString((int) recipe.nutrition[0]) + "kcal");
        carbonTextBar.setText(Double.toString(recipe.nutrition[1]) + "g");
        proteinTextBar.setText(Double.toString(recipe.nutrition[2]) + "g");
        fatTextBar.setText(Double.toString(recipe.nutrition[3]) + "g");
        sodiumTextBar.setText(Double.toString(recipe.nutrition[4]) + "mg");
    }

    private void pushRecentRecipeList() {
        int i, num = 0;
        boolean isExist = false;

        // 최근 본 리스트 수정
        SharedPreferences pref = getSharedPreferences("recentList", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String recentlist = pref.getString("recentlist", "");
        String string[] = recentlist.split(",");

        for (i = 0; i < Recipe.TOTAL_RECIPE_NUM; i++)
            if (recipe.name.compareTo(Recipe.recipeList[i].name) == 0) {
                num = Recipe.recipeList[i].num;
                break;
            }

        if (string[0].compareTo("") != 0) {
            if (string.length > 9) { // 최대 리스트 개수를 초과한 경우
                for (i = 0; i < string.length; i++) { // 중복 여부 검사
                    if (Integer.parseInt(string[i]) == num) {
                        isExist = true;
                        break;
                    }
                }

                if (isExist) {  // 중복된 경우
                    if (recentlist.lastIndexOf(string[i]) + 3 > recentlist.length())
                        recentlist = recentlist.substring(0, recentlist.indexOf(string[i])) + recentlist.substring(recentlist.lastIndexOf(string[i]) + 2);
                    else
                        recentlist = recentlist.substring(0, recentlist.indexOf(string[i])) + recentlist.substring(recentlist.lastIndexOf(string[i]) + 3);
                } else { // 중복되지 않은 경우
                    recentlist = recentlist.substring(0, recentlist.length() - 1);
                    while (!recentlist.endsWith(","))
                        recentlist = recentlist.substring(0, recentlist.length() - 1);
                }
            } else { // 최대 리스트 개수 미만인 경우
                for (i = 0; i < string.length; i++) { // 중복 여부 검사
                    if (Integer.parseInt(string[i]) == num) {
                        isExist = true;
                        break;
                    }
                }

                if (isExist) {  // 중복된 경우
                    if (recentlist.lastIndexOf(string[i]) + 3 > recentlist.length())
                        recentlist = recentlist.substring(0, recentlist.indexOf(string[i])) + recentlist.substring(recentlist.lastIndexOf(string[i]) + 2);
                    else
                        recentlist = recentlist.substring(0, recentlist.indexOf(string[i])) + recentlist.substring(recentlist.lastIndexOf(string[i]) + 3);
                }
            }
        }

        recentlist = num + "," + recentlist;
        HomeFragment.recent_recipes = recentlist;
        HomeFragment.recent_recipes = HomeFragment.recent_recipes.replaceAll(",,",",");

        editor.putString("recentlist", HomeFragment.recent_recipes);
        editor.commit();
    }

    private View.OnClickListener onClickIntro = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == backButton) // 백 버튼
                finish();
            else if (v == scrapButton) { // 스크랩 버튼

                ScrapListData data = db.Dao().findData(recipe.num+1);

                if ((boolean)v.getTag()) {
                    scrapButton.setImageDrawable(grey);
                    v.setTag(false);
                    data.setScraped(0);
                } else {
                    scrapButton.setImageDrawable(red);
                    v.setTag(true);
                    data.setScraped(1);
                }
                db.Dao().update(data);

                SendScrap sendScrap = new SendScrap(getApplicationContext(), data);
                sendScrap.execute();

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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) { // 영양성분 그래프 세팅 함수
        max_bar = findViewById(R.id.real_graph);
        maxWidth = max_bar.getWidth();

        for (int i = 0; i < 5; i++) { // 영양성분 0 그램 시 바 길이 조정
            if (recipe.nutrition[i] == 0)
                recipe.nutrition[i] = 1;
        }

        kcalBar.getLayoutParams().width = (int) (maxWidth * (recipe.nutrition[0] / 2000)); // 남자 2500 여자 2000
        kcalBar.requestLayout();
        carbonBar.getLayoutParams().width = (int) (maxWidth * (recipe.nutrition[1] / 350)); // 남자 438 여자 350
        carbonBar.requestLayout();
        proteinBar.getLayoutParams().width = (int) (maxWidth * (recipe.nutrition[2] / 55)); // 남자 70 여자 55
        proteinBar.requestLayout();
        fatBar.getLayoutParams().width = (int) (maxWidth * (recipe.nutrition[3] / 42)); // 남자 60 여자 42
        fatBar.requestLayout();
        sodiumBar.getLayoutParams().width = (int) (maxWidth * (recipe.nutrition[4] / 2000)); // 공통 2000
        sodiumBar.requestLayout();
    }
}