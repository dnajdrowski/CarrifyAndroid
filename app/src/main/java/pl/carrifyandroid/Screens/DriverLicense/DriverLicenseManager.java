package pl.carrifyandroid.Screens.DriverLicense;

import android.provider.Settings;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.carrifyandroid.API.API;
import pl.carrifyandroid.Models.UploadResponse;
import pl.carrifyandroid.Utils.StorageHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class DriverLicenseManager {

    private StorageHelper storageHelper;
    private DriverLicenseActivity driverLicenseActivity;
    private API api;

    @Inject
    public DriverLicenseManager(StorageHelper storageHelper, API api) {
        this.storageHelper = storageHelper;
        this.api = api;
    }

    public void onStop() {
        driverLicenseActivity = null;
    }

    public void onAttach(DriverLicenseActivity driverLicenseActivity) {
        this.driverLicenseActivity = driverLicenseActivity;
    }

    void uploadDriverLicensePhotos(File frontPageFile, File reversePageFile) {
        RequestBody reqFrontPageFile = RequestBody.create(frontPageFile, MediaType.parse("image/*"));
        RequestBody reqReversePageFile = RequestBody.create(reversePageFile, MediaType.parse("image/*"));
        MultipartBody.Part frontPageImageBody = MultipartBody.Part.createFormData("front", frontPageFile.getName(), reqFrontPageFile);
        MultipartBody.Part reversePagImageBody = MultipartBody.Part.createFormData("reverse", reversePageFile.getName(), reqReversePageFile);

        Call<Integer> call = api.uploadDriverLicensePhotos(storageHelper.getInt("userId"), frontPageImageBody, reversePagImageBody, "Bearer " + storageHelper.getString("token"));
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NotNull @NonNull Call<Integer> call, @NotNull @NonNull Response<Integer> response) {
                if (response.isSuccessful())
                    if (driverLicenseActivity != null)
                        driverLicenseActivity.showSuccessDialog();
            }

            @Override
            public void onFailure(@NotNull @NonNull Call<Integer> call, @NotNull @NonNull Throwable t) {
                Timber.d("Quick upload Manager %s", t.getLocalizedMessage());
            }
        });
    }
}
