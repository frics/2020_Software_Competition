package kr.ac.ssu.myrecipe.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.bumptech.glide.Glide;

import java.net.URL;
import java.util.ArrayList;

import kr.ac.ssu.myrecipe.MainActivity;
import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.adapter.CustomAdapter;
import kr.ac.ssu.myrecipe.adapter.MyAdapter;
import kr.ac.ssu.myrecipe.adapter.MyListDecoration;
import kr.ac.ssu.myrecipe.loader.ImageLoadTask;
import kr.ac.ssu.myrecipe.loader.Recipe;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /* URL 로딩 관련 코드 였던것..
        ImageView imageview = (ImageView)view.findViewById(R.id.test);
        String url = "http://file.okdab.com/UserFiles/searching/recipe/065300.jpg";
        Glide.with(this)
                .asGif()
                .load(url)
                .placeholder(R.drawable.basic)
                .error(R.drawable.basic)
                .into(imageview);
*/

       /* ViewPager pager = (ViewPager)view.findViewById(R.id.pager);
        CustomAdapter adapter = new CustomAdapter(getLayoutInflater());

        pager.setAdapter(adapter);
*/
        RecyclerView listview = view.findViewById(R.id.main_listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false);
        listview.setLayoutManager(layoutManager);

        ArrayList<Recipe> itemList = new ArrayList<>();
        Recipe recipe = new Recipe(0, "육개장", null, null);
        itemList.add(recipe);
        recipe = new Recipe(1,"어묵탕", null, null);
        itemList.add(recipe);
        recipe = new Recipe(2,"떡국", null, null);
        itemList.add(recipe);
        recipe = new Recipe(3,"쇠고기가지볶음", null, null);
        itemList.add(recipe);


        MyAdapter m_adapter = new MyAdapter(getContext(), itemList, onClickItem);
        listview.setAdapter(m_adapter);

        MyListDecoration decoration = new MyListDecoration();
        listview.addItemDecoration(decoration);


        return view;
    }

    private View.OnClickListener onClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = (String) v.getTag();
            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
        }
    };
}

