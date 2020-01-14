package pl.carrifyandroid.Screens.DriverLicense;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.carrifyandroid.App;
import pl.carrifyandroid.R;
import pl.carrifyandroid.Utils.BitmapUtils;
import timber.log.Timber;

public class DriverLicenseActivity extends AppCompatActivity {

    @BindView(R.id.licensePhotoBtn)
    MaterialButton licensePhotoBtn;
    @BindView(R.id.driverLicenseDescription)
    TextView driverLicenseDescription;
    @BindView(R.id.driverLicenseExample)
    ImageView driverLicenseExample;
    @BindView(R.id.progressDriverLicenseLayout)
    ConstraintLayout progressDriverLicenseLayout;

    @Inject
    BitmapUtils bitmapUtils;
    @Inject
    DriverLicenseManager driverLicenseManager;

    private File frontPageFile;
    private File reversePageFile;
    private static final int REQUEST_PICTURE_CAPTURE = 12135;
    private int type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_license);
        App.component.inject(this);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.licensePhotoBtn, R.id.licenseBack})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.licensePhotoBtn:
                new RxPermissions(this)
                        .request(Manifest.permission.CAMERA)
                        .subscribe(granted -> {
                            if (granted)
                                captureImage();
                        });
                break;
            case R.id.licenseBack:
                finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE_CAPTURE) {
            Timber.d("requestCode = " + requestCode + " - resultCode = " + requestCode);
            if (type == 0) {
                Bitmap bitmap = bitmapUtils.decodeBitmapFromFile(frontPageFile);
                if (bitmap != null) {
                    frontPageFile = bitmapUtils.setBitmapToFile(bitmapUtils.getResizedBitmap(bitmap, 800, 600));
                    type = 1;
                    Glide.with(this).load(R.drawable.driver_license_reverse).into(driverLicenseExample);
                    licensePhotoBtn.setText(getString(R.string.take_photo_reverse_page));
                    driverLicenseDescription.setText(getString(R.string.driver_license_description2));
                }
            } else if (type == 1) {
                Bitmap bitmap = bitmapUtils.decodeBitmapFromFile(reversePageFile);
                if (bitmap != null) {
                    reversePageFile = bitmapUtils.setBitmapToFile(bitmapUtils.getResizedBitmap(bitmap, 800, 600));
                    driverLicenseManager.uploadDriverLicensePhotos(frontPageFile, reversePageFile);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        driverLicenseManager.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        driverLicenseManager.onAttach(this);
    }

    public void showSuccessDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_upload_success);

        Window window = dialog.getWindow();
        if (window == null) return;
        Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width - 50;
        params.height = (int) (height * 0.6);
        window.setAttributes(params);

        Button understoodBtn = dialog.findViewById(R.id.understoodBtn);

        understoodBtn.setOnClickListener(view -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }

    private void captureImage() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            if (type == 0) {
                frontPageFile = null;
                try {
                    frontPageFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (frontPageFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this, "carrify.fileprovider", frontPageFile);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(pictureIntent, REQUEST_PICTURE_CAPTURE);
                }
            } else if (type == 1) {
                reversePageFile = null;
                try {
                    reversePageFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (reversePageFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this, "carrify.fileprovider", reversePageFile);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(pictureIntent, REQUEST_PICTURE_CAPTURE);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "CARRIFY_IMG_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".png", storageDir);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
