package com.stone.nestdemo.dependency.component;

import android.app.Application;

import com.stone.nestdemo.dependency.module.AppModule;
import com.stone.nestdemo.dependency.module.NetModule;
import com.stone.nestdemo.network.NetClientManager;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {

    // region
    // objects exposed to the dependent (RepositoryComponent) components
    Application application();

    NetClientManager apiClientManager();

    // endregion

}
