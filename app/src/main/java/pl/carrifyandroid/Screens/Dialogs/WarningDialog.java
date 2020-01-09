package pl.carrifyandroid.Screens.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import pl.carrifyandroid.R;
import pl.carrifyandroid.Screens.DriverLicense.DriverLicenseActivity;

public class WarningDialog {

    public static void showWarningDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_warning_no_license);

        Window window = dialog.getWindow();
        if (window == null) return;
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width - 50;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        Button warningUploadBtn = dialog.findViewById(R.id.warningUploadBtn);

        warningUploadBtn.setOnClickListener(view -> {
            dialog.dismiss();
            Intent driverLicenseIntent = new Intent(activity, DriverLicenseActivity.class);
            activity.startActivity(driverLicenseIntent);
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        dialog.show();
    }

}
