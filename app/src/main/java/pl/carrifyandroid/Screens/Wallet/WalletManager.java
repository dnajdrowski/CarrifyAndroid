package pl.carrifyandroid.Screens.Wallet;

import android.app.Dialog;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import pl.carrifyandroid.API.API;
import pl.carrifyandroid.API.ApiModels.TopUpWalletRequest;
import pl.carrifyandroid.Models.Transaction;
import pl.carrifyandroid.Models.Wallet;
import pl.carrifyandroid.Utils.StorageHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletManager {

    private API api;
    private StorageHelper storageHelper;
    private WalletActivity walletActivity;

    @Inject
    public WalletManager(API api, StorageHelper storageHelper) {
        this.api = api;
        this.storageHelper = storageHelper;
    }

    public void onStop() {
        walletActivity = null;
    }

    public void onAttach(WalletActivity walletActivity) {
        this.walletActivity = walletActivity;
    }


    void getWallet() {
        Call<Wallet> call = api.getWalletByUserId(storageHelper.getInt("userId"), "Bearer " + storageHelper.getString("token"));
        call.enqueue(new Callback<Wallet>() {
            @Override
            public void onResponse(@NotNull Call<Wallet> call, @NotNull Response<Wallet> response) {
                if (walletActivity != null)
                    if (response.isSuccessful())
                        if (response.body() != null)
                            walletActivity.showWalletBalance(response.body(), false, null);
            }

            @Override
            public void onFailure(@NotNull Call<Wallet> call, @NotNull Throwable t) {

            }
        });
    }

    void topUpWallet(int amount, Dialog dialog) {
        Call<Wallet> call = api.topUpWalletById(new TopUpWalletRequest(storageHelper.getInt("userId"), amount), "Bearer " + storageHelper.getString("token"));
        call.enqueue(new Callback<Wallet>() {
            @Override
            public void onResponse(@NotNull Call<Wallet> call, @NotNull Response<Wallet> response) {
                if (walletActivity != null)
                    if (response.isSuccessful())
                        if (response.body() != null)
                            walletActivity.showWalletBalance(response.body(), true, dialog);
            }

            @Override
            public void onFailure(@NotNull Call<Wallet> call, @NotNull Throwable t) {

            }
        });
    }

    void getWalletOperations() {
        Call<List<Transaction>> call = api.getWalletHistoryByUserId(storageHelper.getInt("userId"), "Bearer " + storageHelper.getString("token"));
        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(@NotNull Call<List<Transaction>> call, @NotNull Response<List<Transaction>> response) {
                if (walletActivity != null)
                    if (response.isSuccessful())
                        walletActivity.fillWalletHistoryList(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<Transaction>> call, @NotNull Throwable t) {

            }
        });
    }
}
