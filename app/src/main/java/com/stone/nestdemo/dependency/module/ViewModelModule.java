package com.stone.nestdemo.dependency.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.stone.nestdemo.ui.viewmodel.DeviceViewModel;
import com.stone.nestdemo.ui.viewmodel.HomeViewModel;
import com.stone.nestdemo.ui.viewmodel.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindHomeViewModel(HomeViewModel homeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DeviceViewModel.class)
    abstract ViewModel bindDeviceViewModel(DeviceViewModel deviceViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
