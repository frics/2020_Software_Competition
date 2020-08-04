package kr.ac.ssu.myrecipe;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import kr.ac.ssu.myrecipe.ui.AccountFragment;
import kr.ac.ssu.myrecipe.ui.HomeFragment;
import kr.ac.ssu.myrecipe.ui.RefrigeratorFragment;
import kr.ac.ssu.myrecipe.ui.UndefinedFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private UndefinedFragment undefinedFragment = new UndefinedFragment();
    private RefrigeratorFragment refrigeratorFragment = new RefrigeratorFragment();
    private AccountFragment accountFragment = new AccountFragment();
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true);

        // FAB 기본 세팅 (수정필요)
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "카메라", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //프래그먼트 전환 기능
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemReselectedListener(new ItemSelectedListener());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemReselectedListener{

        @Override
        public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
            switch(menuItem.getItemId())
            {
                case R.id.navigation_home:
                    transaction.replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss();
                    break;
                case R.id.navigation_undefined:
                    transaction.replace(R.id.nav_host_fragment, undefinedFragment).commitAllowingStateLoss();
                    break;
                case R.id.navigation_refrigerator:
                    transaction.replace(R.id.nav_host_fragment, refrigeratorFragment).commitAllowingStateLoss();
                    break;
                case R.id.navigation_account:
                    transaction.replace(R.id.nav_host_fragment, accountFragment).commitAllowingStateLoss();
                    break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("레시피를 검색합니다.");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

/*
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

 */
}


