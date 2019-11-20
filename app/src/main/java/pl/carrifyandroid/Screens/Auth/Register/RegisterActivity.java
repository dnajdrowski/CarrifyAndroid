package pl.carrifyandroid.Screens.Auth.Register;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.carrifyandroid.API.ApiModels.AuthRequest;
import pl.carrifyandroid.App;
import pl.carrifyandroid.R;

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

    @Inject
    RegisterManager registerManager;

    private String action = "WE3ceg6";
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
        if (getIntent() != null)
            registerPhoneNumber.setText(getIntent().getStringExtra("phoneNumber"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick({R.id.registerButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.registerButton:
                if (registerPhoneNumber.getText().toString().length() == 0) {
                    Toast.makeText(this, "Wrong phone number!", LENGTH_SHORT).show();
                    return;
                }
                phoneNumber = registerPhoneNumber.getText().toString();

                if (registerPersonalNumber.getText().toString().length() == 0) {
                    Toast.makeText(this, "Wrong personal number!", LENGTH_SHORT).show();
                    return;
                }
                personalNumber = registerPersonalNumber.getText().toString();

                if (registerEmail.getText().toString().length() == 0) {
                    Toast.makeText(this, "Wrong email address!", LENGTH_SHORT).show();
                    return;
                }
                email = registerEmail.getText().toString();

                if (registerPassword.getText().toString().length() == 0) {
                    Toast.makeText(this, "Wrong password!", LENGTH_SHORT).show();
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

    public void performAction(AuthRequest body) {
        Toast.makeText(this, "WELCOME TO NEW USER", LENGTH_SHORT).show();
    }

    public void showErrorResponse(String messageFromErrorBody) {
        Toast.makeText(this, messageFromErrorBody, LENGTH_SHORT).show();
    }
}
