package com.stone.nestdemo.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.stone.nestdemo.dependency.scope.RepositoryScope;

import javax.inject.Inject;

@RepositoryScope
public class RepositoryOperationStatusManager {

    private static final String TAG = "RepositoryOperationStatusManager";

    public enum Status {
        Loading,
        Success,
        Error;

        private String message;

        public String getMessage() {
            return message;
        }

        public Status setMessage(String message) {
            this.message = message;
            return this;
        }
    }

    private MutableLiveData<Status> mStatus;

    @Inject RepositoryOperationStatusManager() {
        mStatus = new MutableLiveData<>();
    }

    LiveData<Status> getStatus() {
        return mStatus;
    }

    void setLoading(@SuppressWarnings("SameParameterValue") String message) {
        setStatus(Status.Loading, "Status LOADING: " + message);
    }

    void setSuccess(String message) {
        setStatus(Status.Success, "Status SUCCESS: " + message);
    }

    void setError(String message) {
        setStatus(Status.Error, "Status ERROR: " + message);
    }

    private void setStatus(Status status, String message) {
        Log.d(TAG, message);
        mStatus.postValue(status.setMessage(message));
    }
}
