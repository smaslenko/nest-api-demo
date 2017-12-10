package com.stone.nestdemo.dependency.component;

import com.stone.nestdemo.network.ApiClientManager;
import com.stone.nestdemo.ui.MainActivity;
import com.stone.nestdemo.dependency.module.AppModule;
import com.stone.nestdemo.dependency.module.NetModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(MainActivity activity);
    ApiClientManager getClientManager();
}
