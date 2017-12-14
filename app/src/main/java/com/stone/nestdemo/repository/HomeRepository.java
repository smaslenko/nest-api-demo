package com.stone.nestdemo.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.stone.nestdemo.dependency.scope.RepositoryScope;
import com.stone.nestdemo.network.NetClientManager;
import com.stone.nestdemo.network.response.Home;
import com.stone.nestdemo.network.response.Weather;
import com.stone.nestdemo.storage.HomeDao;
import com.stone.nestdemo.storage.model.Device;
import com.stone.nestdemo.storage.model.Structure;
import com.stone.nestdemo.storage.model.Thermostat;
import com.stone.nestdemo.utils.DaoUtil;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;

@RepositoryScope
public class HomeRepository {

    private final NetClientManager mNetClientManager;
    private final HomeDao mHomeDao;
    private final Executor mExecutor;

    @Inject
    BaseCallbackWrapper mBaseCallbackWrapper;

    @Inject
    RepositoryOperationStatusManager mRepositoryStatusManager;

    @Inject HomeRepository(NetClientManager netClientManager, HomeDao dao, Executor executor) {
        mNetClientManager = netClientManager;
        mHomeDao = dao;
        mExecutor = executor;
    }

    public LiveData<List<Structure>> subscribeStructures() {
        // return a LiveData directly from the database.
        return mHomeDao.loadAllStructures();
    }

    public LiveData<List<Device>> subscribeDevicesInStructure(String structureId) {
        // return a LiveData directly from the database.
        return DaoUtil.loadDevicesInStructure(mHomeDao, structureId);
    }

    public LiveData<Thermostat> subscribeThermostat(String deviceId) {
        // return a LiveData directly from the database.
        return mHomeDao.loadThermostat(deviceId);
    }

    public LiveData<RepositoryOperationStatusManager.Status> subscribeRepositoryOperationStatus() {
        return mRepositoryStatusManager.getStatus();
    }

    public void loadHome() {
        mRepositoryStatusManager.setLoading("Home...");
        Call<Home> call = mNetClientManager.getHome();
        call.enqueue(mBaseCallbackWrapper.new BaseCallback<>(this::saveHome));
    }

    public void loadThermostat(String deviceId) {
        mRepositoryStatusManager.setLoading("Thermostat...");
        Call<Thermostat> call = mNetClientManager.getThermostat(deviceId);
        call.enqueue(mBaseCallbackWrapper.new BaseCallback<>(this::saveThermostat));
    }

    public LiveData<Weather> loadWeather(String city) {
        mRepositoryStatusManager.setLoading("Weather...");
        final MutableLiveData<Weather> data = new MutableLiveData<>();
        Call<Weather> call = mNetClientManager.getWeather(city);
        call.enqueue(mBaseCallbackWrapper.new BaseCallback<>(data::postValue));
        return data;
    }

    public void checkHomeExistsAndLoad() {
        mExecutor.execute(() -> {
            List<Structure> structures = mHomeDao.checkStructures();
            if (structures == null || structures.isEmpty()) {
                loadHome();
            }
        });
    }

    private void saveHome(Home home) {
        mExecutor.execute(() -> {
            DaoUtil.saveHome(mHomeDao, home);
        });
    }

    private void saveThermostat(Thermostat thermostat) {
        mExecutor.execute(() -> {
            mHomeDao.saveThermostat(thermostat);
        });
    }
}
