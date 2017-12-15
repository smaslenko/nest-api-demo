package com.stone.nestdemo.ui.viewpresenter;

import com.stone.nestdemo.network.response.Weather;
import com.stone.nestdemo.ui.viewmodel.BaseViewModel;

abstract class BasePresenter<V extends ViewPresenterContract.BaseView<M>, M extends BaseViewModel> {

    final String TAG = this.getClass().getSimpleName();

    V mView;

    BasePresenter(V view) {
        mView = view;
    }

    M viewModel() {
        return mView.viewModel();
    }

    void subscribeWeather(String timeZone, boolean forceRefresh) {
        String[] parts = timeZone.split("/");
        String city;
        if (parts.length > 1) {
            city = parts[1];
        } else {
            city = parts[0];
        }
        viewModel().subscribeWeather(city, forceRefresh).observe(mView.lifecycleOwner(), this::weatherLoaded);
    }

    void subscribeRepositoryOperationStatus() {
        viewModel().subscribeRepositoryStatus().observe(mView.lifecycleOwner(), status -> {
            if (status != null) {
                switch (status) {
                    case Error:
                        mView.showProgress(false);
                        mView.showError(status.getMessage());
                        break;
                    case Loading:
                        mView.showProgress(true);
                        break;
                    case Success:
                        mView.showProgress(false);
                        break;
                }
            }
        });
    }

    private void weatherLoaded(Weather weather) {
        int temp = weather.getMain().getTemp().intValue();
        String tempStr = temp + "°";
        mView.updateWeather(weather.getName(), tempStr);
    }
}
