package com.stone.nestdemo.dependency.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.stone.nestdemo.dependency.scope.RepositoryScope;
import com.stone.nestdemo.storage.HomeDao;
import com.stone.nestdemo.storage.HomeDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @RepositoryScope
    @Provides
    Executor provideSingleThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @RepositoryScope
    @Provides
    HomeDatabase provideHomeDatabase(Application application) {
        return Room.databaseBuilder(application, HomeDatabase.class, "home-db").build();
    }

    @RepositoryScope
    @Provides
    HomeDao provideHomeDao(HomeDatabase database) {
        return database.getHomeDao();
    }
}
