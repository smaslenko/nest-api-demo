package com.stone.nestdemo.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.stone.nestdemo.NestDemoApp;
import com.stone.nestdemo.R;
import com.stone.nestdemo.ui.viewmodel.HomeViewModel;
import com.stone.nestdemo.ui.viewpresenter.HomePresenterImpl;
import com.stone.nestdemo.ui.viewpresenter.ViewPresenterContract;

import java.util.List;

import javax.inject.Inject;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPresenterContract.HoveView {

    private static final String TAG = "HomeActivity";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    HomePresenterImpl mHomePresenter;

    private HomeViewModel mHomeViewModel;

    private NavigationView mNavigationView;

    private Spinner mSpinner;
    private DrawerLayout mDrawer;
    private ArrayAdapter<String> mSpinnerAdapter;
    private int mSpinnerItemSelected;
    private ContentLoadingProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ((NestDemoApp) getApplication()).getRepositoryComponent().inject(this);

        mHomePresenter = new HomePresenterImpl(this);

        initViews();

        initViewModel();
    }

    private void initViewModel() {
        mHomeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel.class);
        mHomeViewModel.initAll();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> refreshHome());

        mDrawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = findViewById(R.id.navView);
        mNavigationView.setNavigationItemSelectedListener(this);

        mProgressBar = findViewById(R.id.progressBar);

        initSpinner();
    }

    private void initSpinner() {
        mSpinner = mNavigationView.getHeaderView(0).findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mSpinnerItemSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void registerObserver() {
        mHomeViewModel.getStructures().observe(this, structures -> {
            mHomePresenter.onStructuresLoaded(structures);
        });
    }

    public void showProgress(boolean visible) {
        mProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void refreshHome() {
        showProgress(true);
        mHomeViewModel.refreshHome();
    }

    @Override public void updateSpinnerItems(List<String> items) {
        if (mSpinnerAdapter == null) {
            mSpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, items);
            mSpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mSpinner.setAdapter(mSpinnerAdapter);
        } else {
            mSpinnerAdapter.clear();
            mSpinnerAdapter.addAll(items);
            mSpinnerAdapter.notifyDataSetChanged();
        }
        mSpinner.setSelection(mSpinnerItemSelected);
    }

    @Override public void updateDrawerItems(List<String> items) {
        final Menu menu = mNavigationView.getMenu();
        for (String item : items) {
            menu.add(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerObserver();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
