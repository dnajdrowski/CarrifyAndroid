package pl.carrifyandroid.Screens.Auth.Login;

import androidx.annotation.NonNull;


import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import pl.carrifyandroid.API.API;
import pl.carrifyandroid.API.ApiModels.AuthRequest;
import pl.carrifyandroid.Utils.ErrorHandler;
import pl.carrifyandroid.Utils.StorageHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

class LoginManager {

    StorageHelper storageHelper;
    private API api;
    private LoginActivity loginActivity;

    @Inject
    LoginManager(API api, StorageHelper storageHelper) {
        this.api = api;
        this.storageHelper = storageHelper;
    }

    void onAttach(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    void onStop() {
        this.loginActivity = null;
    }

    void loginRequest(String action, String password, String personalNumber, String email, String phone) {
        Call<AuthRequest> call = api.loginRequest(new AuthRequest(action, password, personalNumber, email, phone));
        call.enqueue(new Callback<AuthRequest>() {
            @Override
            public void onResponse(@NonNull Call<AuthRequest> call, @NotNull Response<AuthRequest> response) {
                if (loginActivity != null)
                    if (response.isSuccessful()) {
                        loginActivity.performAction(response.body());
                    } else
                        loginActivity.showErrorResponse(ErrorHandler.getMessageFromErrorBody(response.errorBody()));
            }

            @Override
            public void onFailure(@NotNull Call<AuthRequest> call, @NonNull Throwable t) {
                Timber.d("Carrify Splash Manager %s", t.getLocalizedMessage());
            }

        });
    }

}
