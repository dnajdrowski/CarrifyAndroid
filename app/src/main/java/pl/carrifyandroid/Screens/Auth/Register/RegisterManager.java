package pl.carrifyandroid.Screens.Auth.Register;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import pl.carrifyandroid.API.API;
import pl.carrifyandroid.API.ApiModels.JwtVerifyTokenRequest;
import pl.carrifyandroid.Utils.ErrorHandler;
import pl.carrifyandroid.Utils.StorageHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class RegisterManager {

    StorageHelper storageHelper;
    private API api;
    private RegisterActivity registerActivity;

    @Inject
    public RegisterManager(API api, StorageHelper storageHelper) {
        this.api = api;
        this.storageHelper = storageHelper;
    }

    public void onAttach(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    public void onStop() {
        this.registerActivity = null;
    }

    void checkValidation(String token) {
        Call<Integer> call = api.verifyToken(new JwtVerifyTokenRequest(token), "Bearer " + token);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NotNull Response<Integer> response) {
                if (registerActivity != null)
                    if (response.isSuccessful()) {
                        //registerActivity.progressResponse(response.body());
                    } else {

                    }
                       // registerActivity.showErrorResponse(ErrorHandler.getMessageFromErrorBody(response.errorBody()));
            }

            @Override
            public void onFailure(@NotNull Call<Integer> call, @NonNull Throwable t) {
                Timber.d("Carrify Splash Manager %s", t.getLocalizedMessage());
            }

        });
    }

}
