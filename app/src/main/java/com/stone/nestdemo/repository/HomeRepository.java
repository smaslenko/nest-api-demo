package com.stone.nestdemo.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.stone.nestdemo.dependency.scope.RepositoryScope;
import com.stone.nestdemo.network.NetClientManager;
import com.stone.nestdemo.network.request.TargetTemperatureRequest;
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
    private MutableLiveData<Weather> mWeatherData;

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

    public LiveData<String> subscribeTimeZone(String structureId) {
        // return a LiveData directly from the database.
        return mHomeDao.loadTimeZone(structureId);
    }

    public LiveData<RepositoryOperationStatusManager.Status> subscribeRepositoryOperationStatus() {
        return mRepositoryStatusManager.getStatus();
    }

    public void putTemperature(String deviceId, double temperature, String url) {
        mRepositoryStatusManager.setLoading("PUT Temperature: " + temperature);
        TargetTemperatureRequest request = new TargetTemperatureRequest(temperature);
        Call<TargetTemperatureRequest> call = mNetClientManager.putTemperature(deviceId, request, url);
        call.enqueue(mBaseCallbackWrapper.new BaseCallback<>(
            obj -> {
                dbUpdateTemperature(deviceId, obj.getTargetTemperature());
            },
            redirectUrl -> {
                putTemperature(deviceId, temperature, redirectUrl);
            }));
    }

    public void loadHome() {
        mRepositoryStatusManager.setLoading("GET Home");
        Call<Home> call = mNetClientManager.getHome();
        call.enqueue(mBaseCallbackWrapper.new BaseCallback<>(this::dbSaveHome, newRequest -> {
        }));
    }

    public void loadThermostat(String deviceId) {
        mRepositoryStatusManager.setLoading("GET Thermostat");
        Call<Thermostat> call = mNetClientManager.getThermostat(deviceId);
        call.enqueue(mBaseCallbackWrapper.new BaseCallback<>(this::dbSaveThermostat, null));
    }

    public LiveData<Weather> subscribeWeather(String city, boolean forceRefresh) {
        if(mWeatherData == null || forceRefresh) {
            mRepositoryStatusManager.setLoading("GET Weather");
            mWeatherData = new MutableLiveData<>();
            Call<Weather> call = mNetClientManager.getWeather(city);
            call.enqueue(mBaseCallbackWrapper.new BaseCallback<>(mWeatherData::postValue, null));
        }
        return mWeatherData;
    }

    public void checkHomeExistsAndLoad() {
        mExecutor.execute(() -> {
            List<Structure> structures = mHomeDao.checkStructures();
            if (structures == null || structures.isEmpty()) {
                loadHome();
            }
        });
    }

    private void dbSaveHome(Home home) {
        mExecutor.execute(() -> {
            DaoUtil.saveHome(mHomeDao, home);
        });
    }

    private void dbSaveThermostat(Thermostat thermostat) {
        mExecutor.execute(() -> {
            mHomeDao.saveThermostat(thermostat);
        });
    }

    private void dbUpdateTemperature(String deviceId, double temperature) {
        mExecutor.execute(() -> {
            mHomeDao.updateTemperature(deviceId, temperature);
        });
    }
}
