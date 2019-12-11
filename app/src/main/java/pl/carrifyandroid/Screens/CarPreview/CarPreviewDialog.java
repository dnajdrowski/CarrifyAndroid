package pl.carrifyandroid.Screens.CarPreview;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.carrifyandroid.App;
import pl.carrifyandroid.Models.Rent;
import pl.carrifyandroid.Models.RentChange;
import pl.carrifyandroid.R;
import pl.carrifyandroid.Utils.EventBus;

import static android.widget.Toast.LENGTH_LONG;

public class CarPreviewDialog extends DialogFragment {

    @Inject
    CarPreviewManager carPreviewManager;

    @BindView(R.id.rentButton)
    MaterialButton rentButton;
    @BindView(R.id.car_registration_number)
    TextView carRegistrationNumber;
    @BindView(R.id.car_fuel)
    TextView carFuel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        App.component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_car_preview, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {

            carRegistrationNumber.setText(getArguments().getString("name"));
            carFuel.setText("Fuel level: " + getArguments().getInt("fuelLevel"));
        }
        return view;
    }

    @OnClick({R.id.rentButton})
    void onRentButtonClicked() {
        if (getArguments() != null) {
            carPreviewManager.setNewRent(getArguments().getInt("carId"));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        carPreviewManager.onStop();
        EventBus.getBus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        carPreviewManager.onAttach(this);
        EventBus.getBus().register(this);
        if (getDialog() == null || getDialog().getWindow() == null)
            return;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = getDialog().getWindow();
        Display display = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (width * 0.95);
        params.height = (int) (height * 0.35);
        window.setAttributes(params);
    }

    void showNewResponse(Rent body) {
        EventBus.getBus().post(new RentChange(true, body));
        FancyToast.makeText(getContext(), getString(R.string.rent_successful), LENGTH_LONG,
                FancyToast.SUCCESS, false).show();
        dismiss();
    }

    void showErrorResponse(String messageFromErrorBody) {
        FancyToast.makeText(getContext(), messageFromErrorBody, LENGTH_LONG,
                FancyToast.ERROR, false).show();
    }
}
