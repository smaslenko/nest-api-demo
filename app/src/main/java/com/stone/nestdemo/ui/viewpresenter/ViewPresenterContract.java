package com.stone.nestdemo.ui.viewpresenter;

import android.arch.lifecycle.LifecycleOwner;

import com.stone.nestdemo.ui.viewmodel.BaseViewModel;
import com.stone.nestdemo.ui.viewmodel.DeviceViewModel;
import com.stone.nestdemo.ui.viewmodel.HomeViewModel;

import java.util.List;

public interface ViewPresenterContract {

    interface BaseView<V extends BaseViewModel> {

        V viewModel();

        LifecycleOwner lifecycleOwner();

        void updateWeather(String city, String temperature);

        void showProgress(boolean visible);

        void showError(String errorMessage);
    }

    interface HomePresenter {

        void loadHome();

        void observeStructures();

        void onStructureSelected(int position);

        void onDeviceSelected(int position);

        int getSelectedStructurePosition();

        int getSelectedDevicePosition();
    }

    interface HomeView extends BaseView<HomeViewModel> {

        void updateSpinnerItems(List<String> items);

        void updateDrawerItems(List<String> items);

        void selectStructure(int position);

        void selectDevice(int position);

        void showDeviceFragment(String deviceId);

        void removeFragment();

        /**
         * Opens ore closes navigation Drawer
         *
         * @param visible open/close boolean trigger
         * @return true if Drawer was open before this method call
         */
        boolean showDrawer(boolean visible);
    }

    interface DevicePresenter {

        void observeDevice();

        void temperaturePlus();

        void temperatureMinus();
    }

    interface DeviceView extends BaseView<DeviceViewModel> {

        void updateName(String name);

        void updateTemperature(String temperature);

        void updateHumidity(String humidity);

        void showTemperatureExceedMessage();
    }

}
