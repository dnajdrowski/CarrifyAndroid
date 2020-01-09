package pl.carrifyandroid.Screens.Wallet;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.shashank.sony.fancytoastlib.FancyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.carrifyandroid.App;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        App.component.inject(this);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.walletBack, R.id.walletApplyCodeBtn, R.id.walletAddMoneyBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.walletBack:
                finish();
                break;
            case R.id.walletApplyCodeBtn:
            case R.id.walletAddMoneyBtn:
                FancyToast.makeText(this, "This function is not available...!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                break;
        }
    }
}
