package pl.carrifyandroid.Screens.Maps;

import javax.inject.Inject;

import pl.carrifyandroid.API.API;
import pl.carrifyandroid.Utils.StorageHelper;

public class MapsManager {
    StorageHelper storageHelper;
    private API api;
    private MapsFragment mapsFragment;

    @Inject
    public MapsManager(API api, StorageHelper storageHelper) {
        this.api = api;
        this.storageHelper = storageHelper;
    }

    void onAttach(MapsFragment mapsFragment) {
        this.mapsFragment = mapsFragment;
    }

    void onStop() {
        this.mapsFragment = null;
    }

}
