package pl.carrifyandroid.Screens.Auth.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.shashank.sony.fancytoastlib.FancyToast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import pl.carrifyandroid.API.ApiModels.AuthRequest;
import pl.carrifyandroid.App;
import pl.carrifyandroid.MainActivity;
import pl.carrifyandroid.R;
import pl.carrifyandroid.Screens.Auth.Register.RegisterActivity;
import pl.carrifyandroid.Utils.KeyboardHider;
import pl.carrifyandroid.Utils.StorageHelper;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;


public class LoginActivity extends AppCompatActivity {

    @Inject
    LoginManager loginManager;
    @Inject
    StorageHelper storageHelper;

    @BindView(R.id.loginPassword)
    EditText loginPassword;
    @BindView(R.id.loginPhone)
    EditText loginPhone;
    @BindView(R.id.loginConstraintLayout)
    ConstraintLayout loginConstraintLayout;
    @BindView(R.id.loginPhoneLayout)
    TextInputLayout loginPhoneLayout;
    @BindView(R.id.loginPasswordLayout)
    TextInputLayout loginPasswordLayout;

    private String action = "H421sCa";
    private String password = "";
    private String email = "";
    private String personalNumber = "";
    private String phoneNumber = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        App.component.inject(this);
        KeyboardHider.setupUI(loginConstraintLayout, this);
    }

    @OnClick(R.id.loginButton)
    public void onLoginButtonClicked() {
        switch (action) {
            case "H421sCa":
                phoneNumber = loginPhone.getText().toString();
                break;
            case "Po23cVe":
                password = loginPassword.getText().toString();
                break;
            case "WE3ceg6":
                return;
        }
        loginManager.loginRequest(action, password, personalNumber, email, phoneNumber);
    }

    @OnTextChanged(R.id.loginPhone)
    void onTextChanged(CharSequence text) {
        if (text.length() == 9) {
            hideKeyboard();
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginManager.onAttach(this);
        action = "H421sCa";
        password = "";
        email = "";
        personalNumber = "";
        phoneNumber = "";
        loginPasswordLayout.setVisibility(View.GONE);
        loginPasswordLayout.setError(null);
        loginPhoneLayout.setError(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        loginManager.onStop();
    }

    public void performAction(AuthRequest authRequest) {
        action = authRequest.getAction();
        switch (action) {
            case "Po23cVe":
                loginPasswordLayout.setVisibility(View.VISIBLE);
                loginPhone.setEnabled(false);
                loginPasswordLayout.setError(null);
                loginPhoneLayout.setError(null);
                FancyToast.makeText(this, getString(R.string.phone_success),
                        LENGTH_LONG, FancyToast.INFO, false).show();
                break;
            case "WE3ceg6":
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                registerIntent.putExtra("phoneNumber", phoneNumber);
                startActivity(registerIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case "a7Cg8xc":
                if (authRequest.getToken() != null)
                    storageHelper.setString("token", authRequest.getToken());
                if (authRequest.getUserId() != null)
                    storageHelper.setString("userId", authRequest.getUserId());
                FancyToast.makeText(this, getString(R.string.login_success), LENGTH_LONG,
                        FancyToast.SUCCESS, false).show();
                loginPasswordLayout.setError(null);
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
        }
    }

    public void showErrorResponse(String messageFromErrorBody) {
        if (getString(R.string.wrong_phone_number).equals(messageFromErrorBody)) {
            loginPhoneLayout.setError(messageFromErrorBody);
        } else if (getString(R.string.wrong_password).equals(messageFromErrorBody)) {
            loginPasswordLayout.setError(messageFromErrorBody);
        } else if(getString(R.string.invalid_password).equals(messageFromErrorBody)){
            loginPasswordLayout.setError(messageFromErrorBody);
        }
    }
}
