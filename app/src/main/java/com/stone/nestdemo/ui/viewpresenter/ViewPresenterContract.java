package com.stone.nestdemo.ui.viewpresenter;

import android.arch.lifecycle.LifecycleOwner;

import com.stone.nestdemo.ui.viewmodel.HomeViewModel;

import java.util.List;

public interface ViewPresenterContract {

    public interface HomePresenter {

        void loadHome();

        void observeStructures();

        void onStructureSelected(int position);

        void onDeviceSelected(int position);

        int getSelectedStructurePosition();

        int getSelectedDevicePosition();
    }

    public interface HomeView {

        void updateSpinnerItems(List<String> items);

        void updateDrawerItems(List<String> items);

        void showProgress(boolean visible);

        HomeViewModel homeViewModel();

        LifecycleOwner lifecycleOwner();

        void showError(String message);

        void selectStructure(int position);

        void selectDevice(int position);

        /**
         * Opens ore closes navigation Drawer
         *
         * @param visible open/close boolean trigger
         * @return true if Drawer was open before this method call
         */
        boolean showDrawer(boolean visible);
    }

}
