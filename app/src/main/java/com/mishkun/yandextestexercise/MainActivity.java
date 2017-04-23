package com.mishkun.yandextestexercise;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.mishkun.yandextestexercise.di.HasComponent;
import com.mishkun.yandextestexercise.di.components.DaggerMainActivityComponent;
import com.mishkun.yandextestexercise.di.components.MainActivityComponent;
import com.mishkun.yandextestexercise.presentation.views.fragments.BookmarksFragment;
import com.mishkun.yandextestexercise.presentation.views.fragments.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HasComponent<MainActivityComponent> {

    private static final String KEY_CURRENT_FRAGMENT = "CURRENT_FRAGMENT";
    private static final String KEY_HOME_FRAGMENT_STATE = "HOME_FRAGMENT_STATE";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ID_HOME_FRAGMENT = 400;
    private static final int ID_BOOKMARKS_FRAGMENT = 979;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private Fragment homeFragment;
    private Fragment bookmarksFragment;
    private int fragment_id;
    private MainActivityComponent mainActivityComponent;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    getSupportFragmentManager().beginTransaction().replace(R.id.content, homeFragment).commit();
                    fragment_id = ID_HOME_FRAGMENT;
                    break;
                case R.id.navigation_bookmarks:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, bookmarksFragment).commit();
                    fragment_id = ID_BOOKMARKS_FRAGMENT;
                    break;
                default:
                    return false;
            }
            return true;
        }

    };


    @Override
    public void onSaveInstanceState(Bundle state) {
        Bundle fragmentStateBundle = new Bundle();
        homeFragment.onSaveInstanceState(fragmentStateBundle);
        state.putInt(KEY_CURRENT_FRAGMENT, fragment_id);
        state.putBundle(KEY_HOME_FRAGMENT_STATE, fragmentStateBundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupComponent();
        ButterKnife.bind(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        bookmarksFragment = BookmarksFragment.newInstance();
        if (savedInstanceState != null) {
            fragment_id = savedInstanceState.getInt(KEY_CURRENT_FRAGMENT);
            switch (fragment_id) {
                case ID_HOME_FRAGMENT:
                    Bundle fragmentArgs = savedInstanceState.getBundle(KEY_HOME_FRAGMENT_STATE);
                    homeFragment = HomeFragment.newInstance(fragmentArgs);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, homeFragment).commit();
                    break;
                case ID_BOOKMARKS_FRAGMENT:
                    navigation.setSelectedItemId(R.id.navigation_bookmarks);
                    homeFragment = HomeFragment.newInstance("", "ru", "en");
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, homeFragment).commit();
                    break;
            }
        } else {
            homeFragment = HomeFragment.newInstance("", "ru", "en");
            getSupportFragmentManager().beginTransaction().replace(R.id.content, homeFragment).commit();
            fragment_id = ID_HOME_FRAGMENT;
        }
    }

    private void setupComponent() {
        Log.d(TAG, "setupComponent:");
        mainActivityComponent = DaggerMainActivityComponent
                .builder()
                .applicationComponent(((AndroidApplication) getApplication()).getApplicationComponent())
                .build();
        mainActivityComponent.inject(this);
    }

    @Override
    public MainActivityComponent getComponent() {
        return mainActivityComponent;
    }
}
