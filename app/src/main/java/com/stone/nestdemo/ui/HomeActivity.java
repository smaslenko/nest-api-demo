package com.stone.nestdemo.ui;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.stone.nestdemo.NestDemoApp;
import com.stone.nestdemo.R;
import com.stone.nestdemo.ui.viewmodel.HomeViewModel;
import com.stone.nestdemo.ui.viewpresenter.HomePresenterImpl;
import com.stone.nestdemo.ui.viewpresenter.ViewPresenterContract;

import java.util.List;

import javax.inject.Inject;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPresenterContract.HomeView {

    private static final String BUNDLE_KEY_STRUCTURE_POS = "bundle_key_structure_pos";
    private static final String BUNDLE_KEY_DEVICE_POS = "bundle_key_device_pos";
    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private ViewPresenterContract.HomePresenter mHomePresenter;
    private HomeViewModel mHomeViewModel;
    private NavigationView mNavigationView;
    private Spinner mSpinner;
    private TextView mWeatherText;
    private DrawerLayout mDrawer;
    private ArrayAdapter<String> mSpinnerAdapter;
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

    @Override
    protected void onResume() {
        super.onResume();
        // let's wait for structures list retrieved from db
        mHomePresenter.observeStructures();
    }

    @Override
    public HomeViewModel viewModel() {
        return mHomeViewModel;
    }

    @Override
    public LifecycleOwner lifecycleOwner() {
        return this;
    }

    @Override
    public void showError(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void selectStructure(int position) {
        mSpinner.setSelection(position);
        mHomePresenter.onStructureSelected(position);
    }

    @Override
    public void selectDevice(int position) {
        mNavigationView.getMenu().getItem(position).setChecked(true);
        onNavigationItemSelected(mNavigationView.getMenu().getItem(position));
    }

    @Override
    public void showDeviceFragment(String deviceId) {
        DeviceFragment fragment = DeviceFragment.newInstance(deviceId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.home_container, fragment).commit();
    }

    @Override
    public boolean showDrawer(boolean visible) {
        boolean isOpen = mDrawer.isDrawerOpen(GravityCompat.START);
        if (visible) {
            if (!isOpen) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        } else {
            if (isOpen) {
                mDrawer.closeDrawer(GravityCompat.START);
            }
        }
        return isOpen;
    }

    public void showProgress(boolean visible) {
        mProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateSpinnerItems(List<String> items) {
        if (mSpinnerAdapter == null) {
            mSpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, items);
            mSpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mSpinner.setAdapter(mSpinnerAdapter);
        } else {
            mSpinnerAdapter.clear();
            mSpinnerAdapter.addAll(items);
            mSpinnerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateDrawerItems(List<String> items) {
        final Menu menu = mNavigationView.getMenu();
        menu.clear();
        for (int i = 0; i < items.size(); i++) {
            menu.add(Menu.NONE, Menu.NONE, i, items.get(i)).setCheckable(true);
        }
    }

    @Override
    public void updateWeather(String temperature) {
        mWeatherText.setText(temperature);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation drawer item clicks
        mHomePresenter.onDeviceSelected(item.getOrder());
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!showDrawer(false)) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            mHomePresenter.loadHome();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveSelectedPositions(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreSelectedPositions(savedInstanceState);
    }

    private void saveSelectedPositions(Bundle outState) {
        outState.putInt(BUNDLE_KEY_STRUCTURE_POS, mHomePresenter.getSelectedStructurePosition());
        outState.putInt(BUNDLE_KEY_DEVICE_POS, mHomePresenter.getSelectedDevicePosition());
    }

    private void restoreSelectedPositions(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            int structurePos = savedInstanceState.getInt(BUNDLE_KEY_STRUCTURE_POS);
            int devicePos = savedInstanceState.getInt(BUNDLE_KEY_DEVICE_POS);

            if (mSpinner.getChildCount() > 0) {
                // LiveData already fetched and set to Spinner
                mSpinner.setSelection(structurePos);
            } else {
                // LiveData is not fetched yet,
                // let Presenter handle pending positions
                mHomePresenter.onStructureSelected(structurePos);
            }

            if (mNavigationView.getMenu().size() > 0) {
                // LiveData already fetched and set to Drawer
                mNavigationView.getMenu().getItem(devicePos).setChecked(true);
            } else {
                // LiveData is not fetched yet,
                // let Presenter handle pending positions
                mHomePresenter.onDeviceSelected(devicePos);
            }
        }
    }

    private void initViewModel() {
        mHomeViewModel = ViewModelProviders.of(this, mViewModelFactory).get(HomeViewModel.class);
        mHomeViewModel.init();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = findViewById(R.id.navView);
        mNavigationView.setNavigationItemSelectedListener(this);

        mProgressBar = findViewById(R.id.progressBar);

        initHeader();
    }

    private void initHeader() {
        mSpinner = mNavigationView.getHeaderView(0).findViewById(R.id.spinner);
        SpinnerInteractionListener listener = new SpinnerInteractionListener();
        mSpinner.setOnTouchListener(listener);
        mSpinner.setOnItemSelectedListener(listener);
        selectStructure(0);

        mWeatherText = mNavigationView.getHeaderView(0).findViewById(R.id.weatherText);
    }

    /**
     * There are many different events that can trigger the onItemSelected method (e.g. on device rotation it may be called twice ),
     * they can affect some logic in Presenter.
     * This custom listener is workaround for that behavior: it reacts only on user intended actions
     */
    private class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

        boolean mUserSelect = false;

        @SuppressLint("ClickableViewAccessibility") @Override
        public boolean onTouch(View v, MotionEvent event) {
            mUserSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (mUserSelect) {
                mHomePresenter.onStructureSelected(position);
                mUserSelect = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

    }

}
