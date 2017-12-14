package com.stone.nestdemo.repository;

import android.support.annotation.NonNull;

import com.stone.nestdemo.dependency.scope.RepositoryScope;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Wrapper class is workaround to perform Injection
 * into the type-parametrized generic class
 */
@RepositoryScope
public class BaseCallbackWrapper {

    private RepositoryOperationStatusManager mRepositoryStatusManager;

    @Inject BaseCallbackWrapper(RepositoryOperationStatusManager repositoryStatusManager) {
        mRepositoryStatusManager = repositoryStatusManager;
    }

    public class BaseCallback<T> implements Callback<T> {

        private SaveObjectProvider<T> mSaveObjectProvider;

        BaseCallback(SaveObjectProvider<T> saveObjectProvider) {
            mSaveObjectProvider = saveObjectProvider;
        }

        @Override
        public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
            if (response.isSuccessful()) {
                if (mSaveObjectProvider != null) {
                    mSaveObjectProvider.onCanSave(response.body());
                }
                mRepositoryStatusManager.setSuccess("" + response.raw());

            } else {
                mRepositoryStatusManager.setError("" + response.raw());
            }
        }

        @Override
        public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
            mRepositoryStatusManager.setError("" + t.getMessage());
        }
    }

    public interface SaveObjectProvider<T> {
        void onCanSave(T obj);
    }

}
