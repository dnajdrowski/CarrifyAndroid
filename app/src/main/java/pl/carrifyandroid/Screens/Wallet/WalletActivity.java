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
import pl.carrifyandroid.Models.Transaction;
import pl.carrifyandroid.Models.Wallet;
import pl.carrifyandroid.R;

public class WalletActivity extends AppCompatActivity implements WalletHistoryAdapter.ItemClickListener {

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
    @BindView(R.id.walletHistoryRecycler)
    RecyclerView walletHistoryRecycler;
    @Inject
    WalletManager walletManager;
    WalletHistoryAdapter walletHistoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        App.component.inject(this);
        ButterKnife.bind(this);
        walletManager.getWallet();
        walletTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (Objects.requireNonNull(tab.getText()).toString()) {
                    case "Wallet":
                    case "Card":
                        walletHistoryLayout.setVisibility(View.GONE);
                        break;
                    case "History":
                        walletManager.getWalletOperations();
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
        initRecyclerView();
    }

    public void initRecyclerView() {
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        walletHistoryRecycler.setLayoutManager(horizontalLayoutManager);
        walletHistoryRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        walletHistoryAdapter = new WalletHistoryAdapter(this);
        walletHistoryAdapter.setClickListener(this);
        walletHistoryRecycler.setAdapter(walletHistoryAdapter);
        walletHistoryRecycler.setHasFixedSize(true);
        walletHistoryRecycler.setItemAnimator(null);
    }

    @OnClick({R.id.walletBack, R.id.walletApplyCodeBtn, R.id.walletAddMoneyBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.walletBack:
                finish();
                break;
            case R.id.walletApplyCodeBtn:
                FancyToast.makeText(this, "This function is not available...!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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

    public void showTopUpWalletDialog() {
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

    @Override
    public void onItemClick(View view, int position) {

    }

    public void fillWalletHistoryList(List<Transaction> body) {
        walletHistoryAdapter.clear();
        if (body.size() > 0)
            walletHistoryAdapter.setList(body);
        else
            FancyToast.makeText(this, "Unfortunately, you do not have any operations on wallet.", FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show();
    }
}
