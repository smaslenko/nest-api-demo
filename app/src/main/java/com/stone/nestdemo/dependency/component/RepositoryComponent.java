package com.stone.nestdemo.dependency.component;

import com.stone.nestdemo.dependency.module.RepositoryModule;
import com.stone.nestdemo.dependency.scope.RepositoryScope;
import com.stone.nestdemo.ui.MainActivity;

import dagger.Component;

@RepositoryScope
@Component(dependencies = {NetComponent.class}, modules = {RepositoryModule.class})
public interface RepositoryComponent {

    void inject(MainActivity activity);

}
