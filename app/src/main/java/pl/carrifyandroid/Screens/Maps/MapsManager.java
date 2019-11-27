package pl.carrifyandroid.Screens.Maps;

import java.util.List;

import javax.inject.Inject;

import pl.carrifyandroid.API.API;
import pl.carrifyandroid.Models.Car;
import pl.carrifyandroid.Models.RegionZone;
import pl.carrifyandroid.Utils.StorageHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

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

    void getRegionZones(int id) {
        Call<RegionZone> call = api.getRegionZones(id, "Bearer " + storageHelper.getString("token"));
        call.enqueue(new Callback<RegionZone>() {
            @Override
            public void onResponse(Call<RegionZone> call, Response<RegionZone> response) {
                if (mapsFragment != null)
                    if (response.isSuccessful())
                        mapsFragment.drawRegionZones(response.body());

            }

            @Override
            public void onFailure(Call<RegionZone> call, Throwable t) {
                Timber.d(t.getLocalizedMessage());
            }
        });
    }

    void getCarsData() {
        Call<List<Car>> call = api.getCarsData("Bearer " + storageHelper.getString("token"));
        call.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (mapsFragment != null)
                    if (response.isSuccessful()) {
                        mapsFragment.showCarsOnMap(response.body());
                    }


            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
               Timber.e(t);
            }
        });
    }

}
