package pl.carrifyandroid.Screens.Auth.Register;

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

public class RegisterManager {

    StorageHelper storageHelper;
    private API api;
    private RegisterActivity registerActivity;

    @Inject
    public RegisterManager(API api, StorageHelper storageHelper) {
        this.api = api;
        this.storageHelper = storageHelper;
    }

    void onAttach(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    void onStop() {
        this.registerActivity = null;
    }

    void registerRequest(String password, String personalNumber, String email, String phone) {
        Call<AuthRequest> call = api.loginRequest(new AuthRequest("WE3ceg6", password, personalNumber, email, phone));
        call.enqueue(new Callback<AuthRequest>() {
            @Override
            public void onResponse(@NonNull Call<AuthRequest> call, @NotNull Response<AuthRequest> response) {
                if (registerActivity != null)
                    if (response.isSuccessful()) {
                        registerActivity.performRegisterAction();
                    } else
                        registerActivity.showErrorResponse(ErrorHandler.getMessageFromErrorBody(response.errorBody()));
            }

            @Override
            public void onFailure(@NotNull Call<AuthRequest> call, @NonNull Throwable t) {
                Timber.d("Carrify Splash Manager %s", t.getLocalizedMessage());
            }

        });
    }

    void loginRequest(String password, String personalNumber, String email, String phone) {
        Call<AuthRequest> call = api.loginRequest(new AuthRequest("Po23cVe", password, personalNumber, email, phone));
        call.enqueue(new Callback<AuthRequest>() {
            @Override
            public void onResponse(@NonNull Call<AuthRequest> call, @NotNull Response<AuthRequest> response) {
                if (registerActivity != null)
                    if (response.isSuccessful()) {
                        registerActivity.performLoginAction(response.body());
                    } else
                        registerActivity.showErrorResponse(ErrorHandler.getMessageFromErrorBody(response.errorBody()));
            }

            @Override
            public void onFailure(@NotNull Call<AuthRequest> call, @NonNull Throwable t) {
                Timber.d("Carrify Splash Manager %s", t.getLocalizedMessage());
            }
        });
    }

}
