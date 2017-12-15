package com.stone.nestdemo.repository;

import android.support.annotation.NonNull;
import android.text.TextUtils;

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

        private final SaveObjectProvider<T> mSaveObjectProvider;
        private final RedirectProvider mRedirectProvider;

        BaseCallback(SaveObjectProvider<T> saveObjectProvider, RedirectProvider redirectProvider) {
            mSaveObjectProvider = saveObjectProvider;
            mRedirectProvider = redirectProvider;
        }

        @Override
        public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
            if (response.isSuccessful()) {
                if (mSaveObjectProvider != null) {
                    mSaveObjectProvider.onCanSave(response.body());
                }
                mRepositoryStatusManager.setSuccess("" + response.raw());

            } else {
                if (!handle307(response)) {
                    mRepositoryStatusManager.setError("" + response.raw());
                }
            }
        }

        private boolean handle307(Response<T> response) {
            if (response.code() == 307) {
                String location = response.headers().get("Location");

                if (!TextUtils.isEmpty(location)) {
                    if (mRedirectProvider != null) {
                        mRepositoryStatusManager.setRedirection(location);
                        mRedirectProvider.onRedirectRequired(location);
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
            mRepositoryStatusManager.setError("" + t.getMessage());
        }
    }

    public interface SaveObjectProvider<T> {
        void onCanSave(T obj);
    }

    public interface RedirectProvider {
        void onRedirectRequired(String url);
    }

}
