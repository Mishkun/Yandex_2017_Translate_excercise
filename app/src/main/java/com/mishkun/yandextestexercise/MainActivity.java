package com.mishkun.yandextestexercise;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.mishkun.yandextestexercise.fragments.BookmarksFragment;
import com.mishkun.yandextestexercise.fragments.HistoryFragment;
import com.mishkun.yandextestexercise.fragments.HomeFragment;
import com.mishkun.yandextestexercise.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    Fragment fragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = HomeFragment.newInstance();
                    break;
                case R.id.navigation_bookmarks:
                    fragment = BookmarksFragment.newInstance(1);
                    break;
                case R.id.navigation_history:
                    fragment = HistoryFragment.newInstance();
                    break;
                case R.id.navigation_settings:
                    fragment =  new SettingsFragment();
                    break;
                default:
                    return false;
            }
            getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).commit();
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).commit();
    }

}
