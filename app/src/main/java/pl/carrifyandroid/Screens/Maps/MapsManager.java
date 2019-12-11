package pl.carrifyandroid.Screens.Maps;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import pl.carrifyandroid.API.API;
import pl.carrifyandroid.Models.Car;
import pl.carrifyandroid.Models.RegionZone;
import pl.carrifyandroid.Models.Rent;
import pl.carrifyandroid.Utils.StorageHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MapsManager {

    private StorageHelper storageHelper;
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
            public void onResponse(@NotNull Call<RegionZone> call, @NotNull Response<RegionZone> response) {
                if (mapsFragment != null)
                    if (response.isSuccessful())
                        mapsFragment.drawRegionZones(response.body());

            }

            @Override
            public void onFailure(@NotNull Call<RegionZone> call, @NotNull Throwable t) {
                Timber.d(t.getLocalizedMessage());
            }
        });
    }

    void getCarsData() {
        Call<List<Car>> call = api.getCarsData("Bearer " + storageHelper.getString("token"));
        call.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(@NotNull Call<List<Car>> call, @NotNull Response<List<Car>> response) {
                if (mapsFragment != null)
                    if (response.isSuccessful()) {
                        mapsFragment.showCarsOnMap(response.body());
                    }


            }

            @Override
            public void onFailure(@NotNull Call<List<Car>> call, @NotNull Throwable t) {
                Timber.e(t);
            }
        });
    }

    void getActiveRents() {
        Call<Rent> call = api.getActiveRents(storageHelper.getInt("userId"), "Bearer " + storageHelper.getString("token"));
        call.enqueue(new Callback<Rent>() {
            @Override
            public void onResponse(@NotNull Call<Rent> call, @NotNull Response<Rent> response) {
                if (mapsFragment != null)
                    if (response.isSuccessful())
                        mapsFragment.showRentResponse(response.body(), true);
            }

            @Override
            public void onFailure(@NotNull Call<Rent> call, @NotNull Throwable t) {
                Timber.e(t);
            }
        });
    }

    void endRent(int id) {
        Call<Rent> call = api.endRent(id, "Bearer " + storageHelper.getString("token"));
        call.enqueue(new Callback<Rent>() {
            @Override
            public void onResponse(Call<Rent> call, Response<Rent> response) {
                if (mapsFragment != null)
                    if (response.isSuccessful())
                        mapsFragment.showRentResponse(response.body(), false);
            }

            @Override
            public void onFailure(Call<Rent> call, Throwable t) {

            }
        });
    }
}
