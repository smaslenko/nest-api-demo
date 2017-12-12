package com.stone.nestdemo.ui.viewpresenter;

import com.stone.nestdemo.storage.model.Structure;

import java.util.List;

public interface ViewPresenterContract {

    public interface HomePresenter {

        void loadHome();

        void loadThermostats(String structureId);

        void loadCameras(String structureId);

        void onStructuresLoaded(List<Structure> structures);

        void onStructureSelected(int position);
    }

    public interface HoveView {

        void updateSpinnerItems(List<String> items);

        void updateDrawerItems(List<String> items);

        void showProgress(boolean visible);
    }

}
