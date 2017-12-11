package com.stone.nestdemo.dependency.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.stone.nestdemo.ui.viewmodel.HomeViewModel;
import com.stone.nestdemo.ui.viewmodel.HomeViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindUserViewModel(HomeViewModel userViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(HomeViewModelFactory factory);
}
