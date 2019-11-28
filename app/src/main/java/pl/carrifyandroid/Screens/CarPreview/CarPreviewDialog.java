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
import pl.carrifyandroid.R;

import static android.widget.Toast.LENGTH_LONG;

public class CarPreviewDialog extends DialogFragment {

    @Inject
    CarPreviewManager carPreviewManager;

    @BindView(R.id.rentButton)
    MaterialButton rentButton;
    @BindView(R.id.carRegistrationNumber)
    TextView carRegistrationNumber;
    @BindView(R.id.carFuel)
    TextView carFuel;

    private int fuelLevel = 0;
    private String name = "";
    private int carId = 0;

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
            carId = getArguments().getInt("carId");
            fuelLevel = getArguments().getInt("fuelLevel");
            name = getArguments().getString("name");
            carRegistrationNumber.setText("Car name: " + name);
            carFuel.setText("Fuel level: " + fuelLevel + "%");
        }
        return view;
    }

    @OnClick({R.id.rentButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rentButton:
                carPreviewManager.setNewRent(carId);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        carPreviewManager.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        carPreviewManager.onAttach(this);
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
        FancyToast.makeText(getContext(), getString(R.string.rent_successful), LENGTH_LONG,
                FancyToast.SUCCESS, false).show();
    }

    void showErrorResponse(String messageFromErrorBody) {
        FancyToast.makeText(getContext(), messageFromErrorBody, LENGTH_LONG,
                FancyToast.ERROR, false).show();
    }
}
