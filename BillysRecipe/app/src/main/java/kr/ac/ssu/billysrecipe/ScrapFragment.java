package kr.ac.ssu.billysrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListData;
import kr.ac.ssu.billysrecipe.ScrapListDB.ScrapListDataBase;
import kr.ac.ssu.billysrecipe.adapter.ScrapListAdapter;
import kr.ac.ssu.billysrecipe.recipe.Recipe;
import kr.ac.ssu.billysrecipe.recipe.RecipeIntroduction;

public class ScrapFragment extends Fragment {

    private RecyclerView scrapListView;
    private ScrapListDataBase db;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scrap, container, false);

        setViewById(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter();
    }

    private void setViewById(View view) {
        scrapListView = view.findViewById(R.id.scrap_list);
    }

    private void setAdapter() {
        db = Room.databaseBuilder(getContext(), ScrapListDataBase.class, "scraplist.db").allowMainThreadQueries().build();
        List<ScrapListData> dbData = db.Dao().getAll();
        ArrayList<Integer> tmp = new ArrayList<>();

        for(int i = 0 ; i < dbData.size(); i++) {
            if (dbData.get(i).getScraped() == 1 ) {
                tmp.add(dbData.get(i).getId()-1);
            }
        }
/*
        for(int i = 0 ; i < Recipe.TOTAL_RECIPE_NUM; i++){
            if(Recipe.orderTable)
        }
*/
        LinearLayoutManager scrapLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);

        scrapListView.setLayoutManager(scrapLayoutManager);
        ScrapListAdapter scrapAdapter = new ScrapListAdapter(getContext(), tmp, onClickOrigin);
        scrapListView.setAdapter(scrapAdapter);
    }

    private View.OnClickListener onClickOrigin = new View.OnClickListener() {
        // 레시피 소개 액티비티 전환
        @Override
        public void onClick(View v) { // id값 정렬 레시피
            Intent intent = new Intent(getContext(), RecipeIntroduction.class);
            intent.putExtra("recipe", Recipe.recipeList[(int) v.getTag()]);
            startActivity(intent);
        }
    };
}
