package kr.ac.ssu.myrecipe;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // FAB 기본 세팅 (수정필요)
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "카메라", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        BottomNavigationView navView = findViewById(R.id.nav_view);
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
                    Log.v("check","undefined");
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
                R.id.navigation_home, R.id.navigation_undefined, R.id.navigation_refrigerator, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.fragment_layout);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

}