package kr.ac.ssu.billysrecipe.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import kr.ac.ssu.billysrecipe.ScrapFragment;
import kr.ac.ssu.billysrecipe.ShopingListFragment;

public class AccountVPAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> items;

    public AccountVPAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        items = new ArrayList<>();
        items.add(new ScrapFragment());
        items.add(new ShopingListFragment());

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
