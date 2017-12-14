package com.stone.nestdemo.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stone.nestdemo.NestDemoApp;
import com.stone.nestdemo.R;
import com.stone.nestdemo.ui.viewmodel.DeviceViewModel;
import com.stone.nestdemo.ui.viewpresenter.DevicePresenterImpl;
import com.stone.nestdemo.ui.viewpresenter.ViewPresenterContract;

import javax.inject.Inject;

public class DeviceFragment extends Fragment implements ViewPresenterContract.DeviceView {

    private static final String ARG_DEVICE_ID = "arg_device_id";

    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private DeviceViewModel mDeviceViewModel;
    private ViewPresenterContract.DevicePresenter mDevicePresenter;

    private TextView mNameTv;
    private TextView mTemperatureTv;
    private TextView mOutsideTemperatureTv;
    private TextView mHumidityTv;

    public DeviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param deviceId Parameter 1.
     * @return A new instance of fragment DeviceFragment.
     */
    public static DeviceFragment newInstance(String deviceId) {
        DeviceFragment fragment = new DeviceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DEVICE_ID, deviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            String deviceId = getArguments().getString(ARG_DEVICE_ID);

            mDeviceViewModel = ViewModelProviders.of(this, mViewModelFactory).get(DeviceViewModel.class);
            mDevicePresenter = new DevicePresenterImpl(this, deviceId);
            mDevicePresenter.observeDevice();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device, container, false);

        mNameTv = view.findViewById(R.id.nameText);
        mTemperatureTv = view.findViewById(R.id.temperatureText);
        mHumidityTv = view.findViewById(R.id.humidityText);
        mOutsideTemperatureTv = view.findViewById(R.id.outsideTempText);

        return view;
    }

    @Override
    public void updateName(String name) {
        mNameTv.setText(name);
    }

    @Override
    public void updateTemperature(String temperature) {
        mTemperatureTv.setText(temperature);
    }

    @Override
    public void updateWeather(String temperature) {
        mOutsideTemperatureTv.setText(temperature);
    }

    @Override
    public void updateHumidity(String humidity) {
        mHumidityTv.setText(humidity);
    }

    @Override
    public DeviceViewModel viewModel() {
        return mDeviceViewModel;
    }

    @Override
    public LifecycleOwner lifecycleOwner() {
        return this;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((NestDemoApp) getActivity().getApplication()).getRepositoryComponent().inject(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
