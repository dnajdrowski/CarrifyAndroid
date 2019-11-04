package pl.carrifyandroid.Screens.SplashScreen;

import android.util.Log;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import pl.carrifyandroid.API.API;
import pl.carrifyandroid.Models.TestUser;
import pl.carrifyandroid.Utils.StorageHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SplashManager {

    StorageHelper storageHelper;
    private API api;
    private SplashActivity splashActivity;

    @Inject
    public SplashManager(API api, StorageHelper storageHelper) {
        this.api = api;
        this.storageHelper = storageHelper;
    }

    public void onAttach(SplashActivity splashActivity) {
        this.splashActivity = splashActivity;
    }

    public void onStop() {
        this.splashActivity = null;
    }

    public void test(String name) {
        Call<TestUser> call = api.testMethod(name);
        call.enqueue(new Callback<TestUser>() {
            @Override
            public void onResponse(@NonNull Call<TestUser> call, @NonNull Response<TestUser> response) {
                if (response.isSuccessful()) {
                    if (splashActivity != null) {
                        splashActivity.showToast(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TestUser> call, @NonNull Throwable t) {
                Timber.d("Quick Profile Manager %s", t.getLocalizedMessage());
            }
        });

    }

}
