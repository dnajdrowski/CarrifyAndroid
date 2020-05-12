package pl.carrifyandroid.Screens.Wallet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.carrifyandroid.App;
import pl.carrifyandroid.Models.Coupon;
import pl.carrifyandroid.Models.Transaction;
import pl.carrifyandroid.Models.UsedCoupon;
import pl.carrifyandroid.Models.Wallet;
import pl.carrifyandroid.R;

public class WalletActivity extends AppCompatActivity {

    @BindView(R.id.walletAddMoneyBtn)
    MaterialButton walletAddMoneyBtn;
    @BindView(R.id.walletBack)
    ImageButton walletBack;
    @BindView(R.id.walletBalanceTxt)
    TextView walletBalanceTxt;
    @BindView(R.id.walletApplyCodeBtn)
    MaterialButton walletApplyCodeBtn;
    @BindView(R.id.walletTabs)
    TabLayout walletTabs;
    @BindView(R.id.walletHistoryLayout)
    ConstraintLayout walletHistoryLayout;
    @BindView(R.id.couponsHistoryLayout)
    ConstraintLayout couponsHistoryLayout;
    @BindView(R.id.walletHistoryRecycler)
    RecyclerView walletHistoryRecycler;
    @BindView(R.id.couponsHistoryRecycler)
    RecyclerView couponsHistoryRecycler;
    @Inject
    WalletManager walletManager;
    WalletHistoryAdapter walletHistoryAdapter;
    CouponsHistoryAdapter couponsHistoryAdapter;

    private boolean couponsLoaded = false;
    private boolean operationsLoaded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        App.component.inject(this);
        ButterKnife.bind(this);
        initWalletOperationsRecycler();
        initCouponHistoryRecycler();
        walletManager.getWallet();
        walletTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (Objects.requireNonNull(tab.getText()).toString()) {
                    case "Wallet":
                        walletHistoryLayout.setVisibility(View.GONE);
                        couponsHistoryLayout.setVisibility(View.GONE);
                        break;
                    case "Coupons":
                        if (!couponsLoaded) {
                            walletManager.getCouponsHistory();
                            couponsLoaded = true;
                        }
                        couponsHistoryLayout.setVisibility(View.VISIBLE);
                        walletHistoryLayout.setVisibility(View.GONE);
                        break;
                    case "Operations":
                        if (!operationsLoaded) {
                            walletManager.getWalletOperations();
                            operationsLoaded = true;
                        }
                        couponsHistoryLayout.setVisibility(View.GONE);
                        walletHistoryLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void initWalletOperationsRecycler() {
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        walletHistoryRecycler.setLayoutManager(horizontalLayoutManager);
        walletHistoryAdapter = new WalletHistoryAdapter(this);
        walletHistoryRecycler.setAdapter(walletHistoryAdapter);
        walletHistoryRecycler.setHasFixedSize(true);
        walletHistoryRecycler.setItemAnimator(null);
    }

    public void initCouponHistoryRecycler() {
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        couponsHistoryRecycler.setLayoutManager(horizontalLayoutManager);
        couponsHistoryAdapter = new CouponsHistoryAdapter(this);
        couponsHistoryRecycler.setAdapter(couponsHistoryAdapter);
        couponsHistoryRecycler.setHasFixedSize(true);
        couponsHistoryRecycler.setItemAnimator(null);
    }

    @OnClick({R.id.walletBack, R.id.walletApplyCodeBtn, R.id.walletAddMoneyBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.walletBack:
                finish();
                break;
            case R.id.walletApplyCodeBtn:
                showAddCouponDialog();
                break;
            case R.id.walletAddMoneyBtn:
                showTopUpWalletDialog();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        walletManager.onAttach(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        walletManager.onStop();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void showWalletBalance(Wallet body, boolean showToast, Dialog dialog) {
        walletBalanceTxt.setText(String.format("%.2f", (float) (body.getAmount() / 100)) + " PLN");
        if (showToast) {
            if (dialog != null)
                dialog.dismiss();
            FancyToast.makeText(this, getString(R.string.updated_wallet), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
        }

    }

    private void showAddCouponDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_coupon);

        Window window = dialog.getWindow();
        if (window == null) return;
        Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width - 50;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        MaterialButton useCouponBtn = dialog.findViewById(R.id.useCouponBtn);
        MaterialButton cancelCouponBtn = dialog.findViewById(R.id.cancelCouponBtn);
        EditText couponCodeTxt = dialog.findViewById(R.id.couponCodeTxt);
        TextInputLayout couponCodeTxtLayout = dialog.findViewById(R.id.couponCodeTxtLayout);

        couponCodeTxtLayout.setError(null);

        cancelCouponBtn.setOnClickListener(view -> dialog.dismiss());

        useCouponBtn.setOnClickListener(view -> {
            String code = couponCodeTxt.getText().toString();
            if (code.length() == 0) {
                FancyToast.makeText(this, getString(R.string.wrong_code), FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show();
                return;
            }
            couponCodeTxtLayout.setError(null);
            walletManager.useCoupon(code, dialog);
        });

        dialog.show();
    }

    private void showTopUpWalletDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_wallet_add_money);

        Window window = dialog.getWindow();
        if (window == null) return;
        Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width - 50;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        MaterialButton topUpWalletBtn = dialog.findViewById(R.id.topUpWalletBtn);
        MaterialButton cancelWalletBtn = dialog.findViewById(R.id.cancelWalletBtn);
        EditText topUpWalletTxt = dialog.findViewById(R.id.topUpWalletTxt);
        TextInputLayout topUpWalletTxtLayout = dialog.findViewById(R.id.topUpWalletTxtLayout);

        topUpWalletTxtLayout.setError(null);

        cancelWalletBtn.setOnClickListener(view -> dialog.dismiss());

        topUpWalletBtn.setOnClickListener(view -> {
            String amount = topUpWalletTxt.getText().toString();
            if (!amount.matches("[0-9]*")) {
                FancyToast.makeText(this, getString(R.string.wrong_amount), FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show();
                topUpWalletTxt.setText("");
                return;
            }
            topUpWalletTxtLayout.setError(null);
            walletManager.topUpWallet((Integer.parseInt(amount) * 100), dialog);
        });

        dialog.show();
    }

    public void fillWalletHistoryList(List<Transaction> body) {
        walletHistoryAdapter.clear();
        if (body.size() > 0)
            walletHistoryAdapter.setList(body);
        else
            FancyToast.makeText(this, getString(R.string.no_wallet_history), FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show();
    }

    public void fillCouponsHistoryList(List<Coupon> body) {
        if (body.size() > 0)
            couponsHistoryAdapter.setList(body);
        else
            FancyToast.makeText(this, getString(R.string.no_coupons_history), FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show();

    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void usedCouponResponse(UsedCoupon body, Dialog dialog) {
        walletBalanceTxt.setText(String.format("%.2f", (float) (body.getAmount() / 100)) + " PLN");
        if (dialog != null)
            dialog.dismiss();
        FancyToast.makeText(this, getString(R.string.coupon_used_success), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
    }
}
