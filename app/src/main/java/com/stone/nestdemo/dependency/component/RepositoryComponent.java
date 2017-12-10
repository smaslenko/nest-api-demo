package com.stone.nestdemo.dependency.component;

import com.stone.nestdemo.dependency.module.RepositoryModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RepositoryModule.class})
public interface RepositoryComponent {

}
