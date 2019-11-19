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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import pl.carrifyandroid.API.ApiModels.AuthRequest;
import pl.carrifyandroid.App;
import pl.carrifyandroid.R;
import pl.carrifyandroid.Screens.Auth.Register.RegisterActivity;

import static android.widget.Toast.LENGTH_SHORT;


public class LoginActivity extends AppCompatActivity {

    @Inject
    LoginManager loginManager;

    @BindView(R.id.loginPassword)
    EditText loginPassword;
    @BindView(R.id.loginPhone)
    EditText loginPhone;

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
    }

    @OnClick({R.id.loginButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                switch (action) {
                    case "H421sCa":
                        if (loginPhone.getText().toString().length() != 9) {
                            Toast.makeText(this, "Number must contain 9 digits!", LENGTH_SHORT).show();
                            return;
                        }
                        phoneNumber = loginPhone.getText().toString();
                        break;
                    case "Po23cVe":
                        if (loginPassword.getText().toString().length() < 3) {
                            Toast.makeText(this, "Password must contain atleast 3 letters or digits!", LENGTH_SHORT).show();
                            return;
                        }
                        password = loginPassword.getText().toString();
                        break;
                    case "WE3ceg6":
                        break;
                }

                loginManager.loginRequest(action, password, personalNumber, email, phoneNumber);
                break;
        }
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
                loginPassword.setVisibility(View.VISIBLE);
                break;
            case "WE3ceg6":
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                registerIntent.putExtra("phoneNumber", phoneNumber);
                startActivity(registerIntent);
                break;
        }
        Toast.makeText(this, authRequest.getAction(), LENGTH_SHORT).show();
    }

    public void showErrorResponse(String messageFromErrorBody) {
        Toast.makeText(this, messageFromErrorBody, LENGTH_SHORT).show();
    }
}
