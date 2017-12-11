package com.stone.nestdemo;

import android.app.Application;

import com.stone.nestdemo.dependency.component.DaggerNetComponent;
import com.stone.nestdemo.dependency.component.DaggerRepositoryComponent;
import com.stone.nestdemo.dependency.component.NetComponent;
import com.stone.nestdemo.dependency.component.RepositoryComponent;
import com.stone.nestdemo.dependency.module.AppModule;
import com.stone.nestdemo.dependency.module.NetModule;
import com.stone.nestdemo.network.ApiClientManager;

public class NestDemoApp extends Application {

    private NetComponent mNetComponent;
    private RepositoryComponent mRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
            .appModule(new AppModule(this))
            .netModule(new NetModule(ApiClientManager.BASE_URL, ApiClientManager.ACCESS_TOKEN))
            .build();

        mRepositoryComponent = DaggerRepositoryComponent.builder()
            .netComponent(mNetComponent)
            .build();
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }

    public RepositoryComponent getRepositoryComponent() {
        return mRepositoryComponent;
    }
}
