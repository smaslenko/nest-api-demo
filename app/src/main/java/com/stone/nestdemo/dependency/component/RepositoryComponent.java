package com.stone.nestdemo.dependency.component;

import com.stone.nestdemo.dependency.module.RepositoryModule;
import com.stone.nestdemo.dependency.scope.RepositoryScope;
import com.stone.nestdemo.ui.DeviceFragment;
import com.stone.nestdemo.ui.HomeActivity;

import dagger.Component;

@RepositoryScope
@Component(dependencies = {NetComponent.class}, modules = {RepositoryModule.class})
public interface RepositoryComponent {

    void inject(HomeActivity activity);
    void inject(DeviceFragment deviceFragment);
}
