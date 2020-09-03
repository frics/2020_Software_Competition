package kr.ac.ssu.myrecipe;

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

import kr.ac.ssu.myrecipe.MajorFragment.HomeFragment;
import kr.ac.ssu.myrecipe.ScrapListDB.ScrapListData;
import kr.ac.ssu.myrecipe.ScrapListDB.ScrapListDataBase;
import kr.ac.ssu.myrecipe.ShoppingListDB.ShoppingListData;
import kr.ac.ssu.myrecipe.ShoppingListDB.ShoppingListDataBase;
import kr.ac.ssu.myrecipe.adapter.ScrapListAdapter;
import kr.ac.ssu.myrecipe.recipe.Recipe;
import kr.ac.ssu.myrecipe.recipe.RecipeIntroduction;

public class ScrapFragment extends Fragment {

    private RecyclerView scrapListView;
    private ScrapListDataBase db;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scrap, container, false);

        setViewById(view);
        setAdapter();

        return view;
    }

    private void setViewById(View view) {
        scrapListView = view.findViewById(R.id.scrap_list);
    }

    private void setAdapter() {
        // 임시 구조 세팅
        db = Room.databaseBuilder(getContext(), ScrapListDataBase.class, "scraplist.db").allowMainThreadQueries().build();
        List<ScrapListData> dbData = db.Dao().getAll();
        ArrayList<Integer> tmp = new ArrayList<>();

    /*    ScrapListData data = new ScrapListData();
        for(int i = 0 ; i < 300; i++) {
            data.setId(i);
            data.setTotalNum(1540);
            data.setScraped(0);
            db.Dao().update(data);
        }
*/
        Log.d("TAG", "setAdapter: "+dbData.size());
        for(int i = 0 ; i < dbData.size(); i++) {
         //   Log.d("TAG", "setAdapter: " + dbData.get(i).getId());
            if (dbData.get(i).getScraped() == 1 ) {
                tmp.add(dbData.get(i).getId()-1);
                Log.d("TAG", "setAdapter: " + dbData.get(i).getId());
            }
        }
      //  Log.d("TAG", "setAdapter: "+tmp.size());

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
