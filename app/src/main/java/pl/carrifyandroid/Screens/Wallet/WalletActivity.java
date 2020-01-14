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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.shashank.sony.fancytoastlib.FancyToast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.carrifyandroid.App;
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

    @Inject
    WalletManager walletManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        App.component.inject(this);
        ButterKnife.bind(this);
        walletManager.getWallet();
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
}
