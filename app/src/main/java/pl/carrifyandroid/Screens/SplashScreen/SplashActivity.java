package pl.carrifyandroid.Screens.SplashScreen;

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
import pl.carrifyandroid.Models.TestUser;
import pl.carrifyandroid.R;

public class SplashActivity extends AppCompatActivity {

    @Inject
    SplashManager splashManager;
    @BindView(R.id.sendTestRequest)
    Button sendTestRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        App.component.inject(this);
        splashManager.test("test");
    }

    @OnClick({R.id.sendTestRequest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sendTestRequest:
                splashManager.test("sendTestRequest");
        }
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

    public void showToast(TestUser body) {
        Toast.makeText(this, body.getSurname(), Toast.LENGTH_SHORT).show();
    }
}
