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

    void loadWeather(String timeZone) {
        String[] parts = timeZone.split("/");
        String city;
        if (parts.length > 1) {
            city = parts[1];
        } else {
            city = parts[0];
        }
        viewModel().loadWeather(city).observe(mView.lifecycleOwner(), weather -> weatherLoaded(weather, city));
    }

    private void weatherLoaded(Weather weather, String city) {
        int tempInt = weather.getMain().getTemp().intValue();
        String temp = city + " " + tempInt + "°";
        mView.updateWeather(temp);
    }
}
