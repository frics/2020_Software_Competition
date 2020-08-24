package kr.ac.ssu.myrecipe.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

import kr.ac.ssu.myrecipe.Camera.CameraActivity;
import kr.ac.ssu.myrecipe.MainActivity;
import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.adapter.IngredientListAdapter;
import kr.ac.ssu.myrecipe.adapter.MainRecipeListAdapter;

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

        itemList = new ArrayList<>(Arrays.asList(Recipe.recipeList));

        setSearchBar(view);
        setRecyclerView(view);
        setFloatingActionButton(view);

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
            Intent intent = new Intent(getContext(), RecipeIntroduction.class);
            intent.putExtra("recipe", itemList.get((int) v.getTag()));
            startActivity(intent);
        }
    };

}

