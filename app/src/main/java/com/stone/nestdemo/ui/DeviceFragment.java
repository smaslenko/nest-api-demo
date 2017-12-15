package com.stone.nestdemo.ui;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.stone.nestdemo.NestDemoApp;
import com.stone.nestdemo.R;
import com.stone.nestdemo.ui.viewmodel.DeviceViewModel;
import com.stone.nestdemo.ui.viewpresenter.DevicePresenterImpl;
import com.stone.nestdemo.ui.viewpresenter.ViewPresenterContract;

import javax.inject.Inject;

public class DeviceFragment extends Fragment implements ViewPresenterContract.DeviceView, View.OnClickListener {

    private static final String ARG_DEVICE_ID = "arg_device_id";

    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private DeviceViewModel mDeviceViewModel;
    private ViewPresenterContract.DevicePresenter mDevicePresenter;

    private TextView mNameTv;
    private TextView mTemperatureTv;
    private TextView mOutsideTemperatureTv;
    private TextView mHumidityTv;
    private ImageButton mPlusBtn;
    private ImageButton mMinusBtn;
    private View mProgressLayout;

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

    @SuppressLint("ClickableViewAccessibility") @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device, container, false);

        mNameTv = view.findViewById(R.id.nameText);
        mTemperatureTv = view.findViewById(R.id.temperatureText);
        mHumidityTv = view.findViewById(R.id.humidityText);
        mOutsideTemperatureTv = view.findViewById(R.id.outsideTempText);

        mPlusBtn = view.findViewById(R.id.plusButton);
        mMinusBtn = view.findViewById(R.id.minusButton);

        mPlusBtn.setOnClickListener(this);
        mMinusBtn.setOnClickListener(this);

        mProgressLayout = view.findViewById(R.id.progressLayout);
        // this overlay layout will consume all touch events
        mProgressLayout.setOnTouchListener((view1, motionEvent) -> true);

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
    public void updateWeather(String city, String temperature) {
        mOutsideTemperatureTv.setText(temperature);
    }

    @Override
    public void showProgress(boolean visible) {
        mProgressLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String errorMessage) {
    }

    @Override
    public void updateHumidity(String humidity) {
        mHumidityTv.setText(humidity);
    }

    @Override
    public void showTemperatureExceedMessage() {
        Toast toast = Toast.makeText(getActivity(), getActivity().getString(R.string.temperature_exceed_message), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onClick(View view) {
        if (view == mPlusBtn) {
            mDevicePresenter.temperaturePlus();
        }
        if (view == mMinusBtn) {
            mDevicePresenter.temperatureMinus();
        }
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
