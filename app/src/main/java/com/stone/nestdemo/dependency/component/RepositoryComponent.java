package com.stone.nestdemo.dependency.component;

import android.arch.lifecycle.ViewModelProvider;

import com.stone.nestdemo.dependency.module.RepositoryModule;
import com.stone.nestdemo.dependency.scope.RepositoryScope;
import com.stone.nestdemo.ui.MainActivity;

import dagger.Component;

@RepositoryScope
@Component(dependencies = {NetComponent.class}, modules = {RepositoryModule.class})
public interface RepositoryComponent {

    ViewModelProvider.Factory viewModelProviderFactory();

    void inject(MainActivity activity);

}
