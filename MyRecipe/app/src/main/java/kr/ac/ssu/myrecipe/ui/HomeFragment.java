package kr.ac.ssu.myrecipe.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.bumptech.glide.Glide;

import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.adapter.CustomAdapter;
import kr.ac.ssu.myrecipe.loader.ImageLoadTask;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /* URL 로딩 관련 코드 였던것..
        ImageView imageview = (ImageView)view.findViewById(R.id.test);
        String url = "http://file.okdab.com/UserFiles/searching/recipe/065300.jpg";
        Glide.with(this).load(url)
                .error(R.drawable.basic)
                .into(imageview);
*/

        ViewPager pager = (ViewPager)view.findViewById(R.id.pager);
        CustomAdapter adapter = new CustomAdapter(getLayoutInflater());

        pager.setAdapter(adapter);



        return view;
    }
}

