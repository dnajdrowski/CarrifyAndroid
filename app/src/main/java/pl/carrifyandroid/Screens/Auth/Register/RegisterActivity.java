package pl.carrifyandroid.Screens.Auth.Register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.carrifyandroid.API.ApiModels.AuthRequest;
import pl.carrifyandroid.App;
import pl.carrifyandroid.MainActivity;
import pl.carrifyandroid.R;
import pl.carrifyandroid.Utils.KeyboardHider;
import pl.carrifyandroid.Utils.StorageHelper;

import static android.widget.Toast.LENGTH_SHORT;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.registerPersonalNumber)
    EditText registerPersonalNumber;
    @BindView(R.id.registerEmail)
    EditText registerEmail;
    @BindView(R.id.registerPassword)
    EditText registerPassword;
    @BindView(R.id.registerPhoneNumber)
    EditText registerPhoneNumber;
    @BindView(R.id.registerButton)
    Button registerButton;
    @BindView(R.id.registerConstraintLayout)
    ConstraintLayout registerConstraintLayout;

    @Inject
    RegisterManager registerManager;
    @Inject
    StorageHelper storageHelper;

    private String password = "";
    private String email = "";
    private String personalNumber = "";
    private String phoneNumber = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        App.component.inject(this);
        if (getIntent() != null) {
            registerPhoneNumber.setText(getIntent().getStringExtra("phoneNumber"));
            registerPhoneNumber.setEnabled(false);
        }
        KeyboardHider.setupUI(registerConstraintLayout, this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick({R.id.registerButton})
    public void onViewClicked(View view) {
        String action = "WE3ceg6";
        switch (view.getId()) {
            case R.id.registerButton:
                if (registerPhoneNumber.getText().toString().length() == 0) {
                    Toast.makeText(this, getString(R.string.wrong_phone_number), LENGTH_SHORT).show();
                    return;
                }
                phoneNumber = registerPhoneNumber.getText().toString();

                if (registerPersonalNumber.getText().toString().length() == 0) {
                    Toast.makeText(this, getString(R.string.personal_number), LENGTH_SHORT).show();
                    return;
                }
                personalNumber = registerPersonalNumber.getText().toString();

                if (registerEmail.getText().toString().length() == 0) {
                    Toast.makeText(this, getString(R.string.wrong_email_address), LENGTH_SHORT).show();
                    return;
                }
                email = registerEmail.getText().toString();

                if (registerPassword.getText().toString().length() == 0) {
                    Toast.makeText(this, getString(R.string.wrong_password), LENGTH_SHORT).show();
                    return;
                }
                password = registerPassword.getText().toString();

                registerManager.registerRequest(action, password, personalNumber, email, phoneNumber);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        registerManager.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerManager.onAttach(this);
    }

    public void performRegisterAction(AuthRequest authRequest) {
        Toast.makeText(this, getString(R.string.register_success), LENGTH_SHORT).show();
        registerManager.loginRequest("Po23cVe", password, personalNumber, email, phoneNumber);
    }

    public void performLoginAction(AuthRequest authRequest) {
        if ("a7Cg8xc".equals(authRequest.getAction())) {
            if (authRequest.getToken() != null)
                storageHelper.setString("token", authRequest.getToken());
            if (authRequest.getUserId() != null)
                storageHelper.setString("userId", authRequest.getUserId());
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    public void showErrorResponse(String messageFromErrorBody) {
        Toast.makeText(this, messageFromErrorBody, LENGTH_SHORT).show();
    }
}
