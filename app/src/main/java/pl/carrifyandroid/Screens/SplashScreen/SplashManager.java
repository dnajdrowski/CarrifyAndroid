package pl.carrifyandroid.Screens.SplashScreen;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import pl.carrifyandroid.API.API;
import pl.carrifyandroid.API.ApiModels.JwtVerifyTokenRequest;
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

    void onAttach(SplashActivity splashActivity) {
        this.splashActivity = splashActivity;
    }

    void onStop() {
        this.splashActivity = null;
    }

    void checkValidation(String token) {
        Call<Integer> call = api.verifyToken(new JwtVerifyTokenRequest(token));
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NotNull Response<Integer> response) {
                if (splashActivity != null)
                    if (response.isSuccessful()) {
                        splashActivity.showMainActivity(response.body());
                    } else
                        splashActivity.showLoginActivity();
            }

            @Override
            public void onFailure(@NotNull Call<Integer> call, @NonNull Throwable t) {
                Timber.d("Carrify Splash Manager %s", t.getLocalizedMessage());
            }
        });
    }

}
