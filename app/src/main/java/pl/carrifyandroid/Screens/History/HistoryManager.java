package pl.carrifyandroid.Screens.History;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import pl.carrifyandroid.API.API;
import pl.carrifyandroid.Models.Rent;
import pl.carrifyandroid.Utils.StorageHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class HistoryManager {

    private API api;
    private StorageHelper storageHelper;
    private HistoryActivity historyActivity;

    @Inject
    public HistoryManager(API api, StorageHelper storageHelper) {
        this.api = api;
        this.storageHelper = storageHelper;
    }

    void onStop() {
        this.historyActivity = null;
    }

    void onAttach(HistoryActivity historyActivity) {
        this.historyActivity = historyActivity;
    }

    void getRentHistoryList() {
        Call<List<Rent>> call = api.getRentHistory(storageHelper.getInt("userId"), "Bearer " + storageHelper.getString("token"));
        call.enqueue(new Callback<List<Rent>>() {
            @Override
            public void onResponse(Call<List<Rent>> call, Response<List<Rent>> response) {
                if (historyActivity != null)
                    if (response.isSuccessful())
                        historyActivity.showRentList(response.body());
            }

            @Override
            public void onFailure(Call<List<Rent>> call, Throwable t) {
                Timber.e(t.getLocalizedMessage());
            }
        });
    }
}
