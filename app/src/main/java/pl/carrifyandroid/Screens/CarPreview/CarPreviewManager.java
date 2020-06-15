package pl.carrifyandroid.Screens.CarPreview;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import pl.carrifyandroid.API.API;
import pl.carrifyandroid.API.ApiModels.NewRentRequest;
import pl.carrifyandroid.Models.Rent;
import pl.carrifyandroid.Models.Reservation;
import pl.carrifyandroid.Utils.ErrorHandler;
import pl.carrifyandroid.Utils.StorageHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class CarPreviewManager {

    StorageHelper storageHelper;
    private API api;
    private CarPreviewDialog carPreviewDialog;

    @Inject
    public CarPreviewManager(API api, StorageHelper storageHelper) {
        this.api = api;
        this.storageHelper = storageHelper;
    }

    void onAttach(CarPreviewDialog carPreviewDialog) {
        this.carPreviewDialog = carPreviewDialog;
    }

    void onStop() {
        this.carPreviewDialog = null;
    }

    void setNewRent(int carId) {
        Call<Rent> call = api.addNewRent(new NewRentRequest(carId, storageHelper.getInt("userId")), "Bearer " + storageHelper.getString("token"));
        call.enqueue(new Callback<Rent>() {
            @Override
            public void onResponse(@NonNull Call<Rent> call, @NotNull Response<Rent> response) {
                if (carPreviewDialog != null)
                    if (response.isSuccessful())
                        carPreviewDialog.showNewResponse(response.body());
                    else
                        carPreviewDialog.showErrorResponse(ErrorHandler.getMessageFromErrorBody(response.errorBody()));
            }

            @Override
            public void onFailure(@NotNull Call<Rent> call, @NonNull Throwable t) {
                Timber.d("Carrify Splash Manager %s", t.getLocalizedMessage());
            }
        });
    }

    void setNewReservation(int carId) {
        Call<Reservation> call = api.addNewReservation(new NewRentRequest(carId, storageHelper.getInt("userId")), "Bearer " + storageHelper.getString("token"));
        call.enqueue(new Callback<Reservation>() {

            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                if (carPreviewDialog != null)
                    if (response.isSuccessful())
                        carPreviewDialog.showNewReservationResponse(response.body());
                    else
                        carPreviewDialog.showErrorResponse(ErrorHandler.getMessageFromErrorBody(response.errorBody()));
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                Timber.d("Carrify Splash Manager %s", t.getLocalizedMessage());
            }
        });
    }

}
