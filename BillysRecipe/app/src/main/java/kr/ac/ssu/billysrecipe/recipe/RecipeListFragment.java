package kr.ac.ssu.billysrecipe.recipe;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import kr.ac.ssu.billysrecipe.MainActivity;
import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.adapter.MainRecipeListAdapter;

public class RecipeListFragment extends Fragment {

    private ArrayList<Recipe> itemList;
    private EditText editText;
    private ImageView clearButton;
    private MainRecipeListAdapter adapter;
    private RecyclerView RecyclerView;

    public static RecipeListFragment newInstance() {
        return new RecipeListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        itemList = new ArrayList<>();

        MainActivity.isRecipeSetting = true;

        // 테이블에서 매핑
        for (int i = 0; i < Recipe.TOTAL_RECIPE_NUM; i++)
            for (int j = 0; j < Recipe.TOTAL_RECIPE_NUM; j++) {
                if (Recipe.orderTable[i] == Recipe.recipeList[j].num) {
                    itemList.add(Recipe.recipeList[j]);
                    break;
                }
            }

        setSearchBar(view);
        setRecyclerView(view);
        setFloatingActionButton(view);
        setHasOptionsMenu(true);
        return view;
    }


    private void setSearchBar(View view) {
        editText = (EditText) view.findViewById(R.id.edittext);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String inText = textView.getText().toString();
                // Do Something...
                textView.setInputType(EditorInfo.TYPE_NULL);

                return true;
            }
        });

        clearButton = (ImageView) view.findViewById(R.id.clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                editText.clearFocus();
            }
        });
    }

    private void setRecyclerView(View view) {
        RecyclerView = view.findViewById(R.id.main_recipe_list);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false);
        RecyclerView.setLayoutManager(LayoutManager);
        adapter = new MainRecipeListAdapter(view.getContext(), itemList, onClickItem);
        RecyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recipe_list_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        if (MainActivity.isRecipeSetting) {
            for (int i = 0; i < menu.size(); i++) {
                if (menu.getItem(i).getTitle().toString().compareTo("list_setting") == 0) {
                    menu.getItem(i).setEnabled(true);
                    menu.getItem(i).setVisible(true);
                }
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (MainActivity.isRecipeSetting) {
            Intent intent = new Intent(getContext(), RecipeSettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFloatingActionButton(View view) {
        // FAB 기본 세팅 (수정필요)
        FloatingActionButton fab = view.findViewById(R.id.go_to_top);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private View.OnClickListener onClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 레시피 소개 액티비티 전환
            Intent intent = new Intent(getContext(), RecipeIntroduction.class);
            intent.putExtra("recipe", Recipe.recipeList[(int) v.getTag()]);
            startActivity(intent);
        }
    };

}

