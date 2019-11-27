package pl.carrifyandroid.Screens.Auth.Register;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shashank.sony.fancytoastlib.FancyToast;

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

    @BindView(R.id.registerPersonalNumber) TextInputEditText registerPersonalNumber;
    @BindView(R.id.registerEmail) TextInputEditText registerEmail;
    @BindView(R.id.registerPassword) TextInputEditText registerPassword;
    @BindView(R.id.registerPhoneNumber) TextInputEditText registerPhoneNumber;
    @BindView(R.id.registerButton) MaterialButton registerButton;
    @BindView(R.id.registerConstraintLayout) ConstraintLayout registerConstraintLayout;
    @BindView(R.id.registerEmailLayout) TextInputLayout registerEmailLayout;
    @BindView(R.id.registerPasswordLayout) TextInputLayout registerPasswordLayout;
    @BindView(R.id.registerPhoneLayout) TextInputLayout registerPhoneLayout;
    @BindView(R.id.registerPersonalNumberLayout) TextInputLayout registerPersonalNumberLayout;

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

    @OnClick(R.id.registerButton)
    public void onRegisterButtonClicked() {
        registerPasswordLayout.setError(null);
        registerPersonalNumberLayout.setError(null);
        registerEmailLayout.setError(null);
        if(registerPhoneNumber.getText() != null)
            phoneNumber = registerPhoneNumber.getText().toString();
        if(registerPersonalNumber.getText() != null)
             personalNumber = registerPersonalNumber.getText().toString();
        if(registerEmail.getText() != null)
            email = registerEmail.getText().toString();
        if(registerPassword.getText() != null)
            password = registerPassword.getText().toString();

        registerManager.registerRequest(password, personalNumber, email, phoneNumber);
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

    public void performRegisterAction() {
        FancyToast.makeText(this, getString(R.string.register_success),
                LENGTH_SHORT, FancyToast.SUCCESS, false).show();
        registerPasswordLayout.setError(null);
        registerPersonalNumberLayout.setError(null);
        registerEmailLayout.setError(null);
        registerManager.loginRequest(password, personalNumber, email, phoneNumber);
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
            finish();
        }
    }

    public void showErrorResponse(String messageFromErrorBody) {
        if(getString(R.string.wrong_password).equals(messageFromErrorBody)) {
            registerPasswordLayout.setError(messageFromErrorBody);
        } else if(getString(R.string.wrong_email_address).equals(messageFromErrorBody) ||
                getString(R.string.email_in_use).equals(messageFromErrorBody)) {
            registerEmailLayout.setError(messageFromErrorBody);
        } else if(getString(R.string.wrong_personal_number).equals(messageFromErrorBody) ||
                getString(R.string.personal_number_in_use).equals(messageFromErrorBody)) {
            registerPersonalNumberLayout.setError(messageFromErrorBody);
        }
    }
}
