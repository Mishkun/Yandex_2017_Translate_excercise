package com.mishkun.yandextestexercise;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mishkun.yandextestexercise.di.HasComponent;
import com.mishkun.yandextestexercise.di.components.DaggerMainActivityComponent;
import com.mishkun.yandextestexercise.di.components.MainActivityComponent;
import com.mishkun.yandextestexercise.di.modules.MainActivityModule;
import com.mishkun.yandextestexercise.presentation.views.fragments.BookmarksFragment;
import com.mishkun.yandextestexercise.presentation.views.fragments.HistoryFragment;
import com.mishkun.yandextestexercise.presentation.views.fragments.HomeFragment;
import com.mishkun.yandextestexercise.presentation.views.fragments.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HasComponent<MainActivityComponent>{

    Fragment fragment;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private MainActivityComponent mainActivityComponent;

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
                    fragment = new SettingsFragment();
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
        ButterKnife.bind(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).commit();
        setupComponent();
    }

    private void setupComponent() {
        mainActivityComponent = DaggerMainActivityComponent
                .builder()
                .applicationComponent(((AndroidApplication) getApplication()).getApplicationComponent())
                .mainActivityModule(new MainActivityModule(this))
                .build();
    }

    @Override
    public MainActivityComponent getComponent() {
        return mainActivityComponent;
    }
}
