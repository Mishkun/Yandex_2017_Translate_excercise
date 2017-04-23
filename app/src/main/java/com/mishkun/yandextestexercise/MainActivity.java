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
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ID_HOME_FRAGMENT = 400;
    private static final int ID_BOOKMARKS_FRAGMENT = 979;
    Fragment fragment;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private int fragment_id;
    private MainActivityComponent mainActivityComponent;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = HomeFragment.newInstance();
                    fragment_id = ID_HOME_FRAGMENT;
                    break;
                case R.id.navigation_bookmarks:
                    fragment = BookmarksFragment.newInstance();
                    fragment_id = ID_BOOKMARKS_FRAGMENT;
                    break;
                default:
                    return false;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
            return true;
        }

    };


    @Override
    public void onSaveInstanceState(Bundle state) {
        state.putInt(KEY_CURRENT_FRAGMENT, fragment_id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupComponent();
        ButterKnife.bind(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState != null) {
            fragment_id = savedInstanceState.getInt(KEY_CURRENT_FRAGMENT);
            switch (fragment_id) {
                case ID_HOME_FRAGMENT:
                    fragment = HomeFragment.newInstance();
                    break;
                case ID_BOOKMARKS_FRAGMENT:
                    fragment = BookmarksFragment.newInstance();
                    navigation.setSelectedItemId(R.id.navigation_bookmarks);
                    break;
                default:
                    fragment = HomeFragment.newInstance();
                    fragment_id = ID_HOME_FRAGMENT;
                    break;
            }
        } else {
            fragment = HomeFragment.newInstance();
            fragment_id = ID_HOME_FRAGMENT;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

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
