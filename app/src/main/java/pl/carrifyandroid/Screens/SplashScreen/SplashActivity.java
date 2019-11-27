package pl.carrifyandroid.Screens.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.carrifyandroid.App;
import pl.carrifyandroid.MainActivity;
import pl.carrifyandroid.R;
import pl.carrifyandroid.Screens.Auth.Login.LoginActivity;
import pl.carrifyandroid.Utils.StorageHelper;

public class SplashActivity extends AppCompatActivity {

    @Inject
    SplashManager splashManager;
    @Inject
    StorageHelper storageHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        App.component.inject(this);
        splashManager.checkValidation(storageHelper.getString("token"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        splashManager.onAttach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        splashManager.onStop();
    }

    public void showLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void showMainActivity(Integer body) {
        storageHelper.setInteger("userId", body);
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
