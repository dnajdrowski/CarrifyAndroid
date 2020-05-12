package pl.carrifyandroid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.johnnylambada.location.LocationProvider;
import com.squareup.otto.Subscribe;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.carrifyandroid.Models.BusLocation;
import pl.carrifyandroid.Models.EndRent;
import pl.carrifyandroid.Models.RentChange;
import pl.carrifyandroid.Screens.History.HistoryActivity;
import pl.carrifyandroid.Screens.Maps.MapsFragment;
import pl.carrifyandroid.Screens.Wallet.WalletActivity;
import pl.carrifyandroid.Utils.EventBus;
import pl.carrifyandroid.Utils.StorageHelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Inject
    StorageHelper storageHelper;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.bottom_sheet_beh)
    ConstraintLayout bottomSheet;
    @BindView(R.id.rent_time)
    TextView rentTime;
    @BindView(R.id.rent_distance)
    TextView rentDistance;
    @BindView(R.id.rent_car_registration_number)
    TextView rentCarRegNumber;
    @BindView(R.id.rent_car_name)
    TextView rentCarName;
    @BindView(R.id.park_button)
    MaterialButton parkButton;

    private TextView navUsername;
    private ImageView avatar;

    private boolean attachMaps = false;
    private boolean attachLoc = false;
    private static boolean active = false;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Handler h = new Handler();
    private Handler h2 = new Handler();
    private Runnable runnable;
    private Runnable runnable2;
    private LocationProvider locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.component.inject(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(bottomSheet);

        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        //username
        navUsername = headerView.findViewById(R.id.username);
        //avatar
        avatar = headerView.findViewById(R.id.avatar);
        Glide.with(MainActivity.this).load(R.drawable.car_avatar).circleCrop().centerInside().into(avatar);
        loopLocalization();

        MapsInitializer.initialize(getApplicationContext());
    }

    public void initLoc() {
        attachMaps = true;
        loadFragment(new MapsFragment());

        getFastLoc();

        locationProvider = new LocationProvider.Builder(this)
//                .lifecycleOwner(this)   // not necessary for activity when activity==lifecycleowner
                .locationPermission(Manifest.permission.ACCESS_FINE_LOCATION) // default FINE
                .accuracy(LocationRequest.PRIORITY_HIGH_ACCURACY) // default HIGH_ACCURACY
                .intervalMs(30000)   // 1 second

                .locationObserver(location -> {
                    EventBus.getBus().post(new BusLocation(location));
                })
                .onPermissionDeniedFirstTime(() -> {
                })
                .onPermissionDeniedAgain(() -> {
                })
                .onPermissionDeniedForever(() -> {
                })
                .build();

        locationProvider.startTrackingLocation();
    }

    public void getFastLoc() {
        locationProvider = new LocationProvider.Builder(this)
//                .lifecycleOwner(this)   // not necessary for activity when activity==lifecycleowner
                .locationPermission(Manifest.permission.ACCESS_FINE_LOCATION) // default FINE
                .accuracy(LocationRequest.PRIORITY_HIGH_ACCURACY) // default HIGH_ACCURACY
                .intervalMs(1000)   // 1 second

                .locationObserver(location -> {
                    if (attachMaps) {
                        //      Log.d(TAG, "getFastLoc: Wysylam FAST LOC");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        EventBus.getBus().post(new BusLocation(location));
                    }
                    if (locationProvider.isTrackingLocation()) {
                        locationProvider.stopTrackingLocation();
                    }
                })
                .onPermissionDeniedFirstTime(() -> {
                })
                .onPermissionDeniedAgain(() -> {
                })
                .onPermissionDeniedForever(() -> {
                })
                .build();

        locationProvider.startTrackingLocation();
    }

    public void loopLocalization() {
        int delLoc = 1000;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        h2.postDelayed(runnable2 = () -> {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (!attachLoc) {
                    Toast.makeText(this, "We can't find your location, please turn on GPS!", Toast.LENGTH_SHORT).show();
                    //Intent localizeIntent = new Intent(this, MapsLocalizeActivity.class);
                    //startActivityForResult(localizeIntent, 1547);
                    attachLoc = true;
                }
            } else if (!attachMaps)
                initLoc();
            h2.postDelayed(runnable2, delLoc);
        }, delLoc);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commitAllowingStateLoss();
    }

    public void openDrawer() {
        new Handler().post(() -> {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.START);
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history:
                startActivity(new Intent(this, HistoryActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.wallet:
                startActivity(new Intent(this, WalletActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Subscribe
    public void onRentChange(RentChange rentChange) {
        if (rentChange.isRent()) {
            bottomSheet.setVisibility(View.VISIBLE);
            h.postDelayed(runnable = () -> {
                rentTime.setText(printDifference(rentChange.getRental().getCreatedAt()));
                h.postDelayed(runnable, 1000);
            }, 1000);
            rentDistance.setText(rentChange.getRental().getDistance() + " km");
            rentCarRegNumber.setText(rentChange.getRental().getCar().getRegistrationNumber());
            rentCarName.setText(rentChange.getRental().getCar().getName());
            parkButton.setOnClickListener(view -> {
                EventBus.getBus().post(new EndRent(rentChange.getRental().getId()));
            });
        } else {
            bottomSheet.setVisibility(View.GONE);
            h.removeCallbacks(runnable);
        }
    }

    public String printDifference(String oldtime) {
        int hh = 0;
        int mm = 0;
        try {
            @SuppressLint("SimpleDateFormat")
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Warsaw"));
            Date createdAtDate = dateFormat.parse(oldtime.replaceAll("T", " "));
            Date todayDate = dateFormat.parse(dateFormat.format(new Date()));
            long timeDifference = 0;
            if (createdAtDate != null) {
                timeDifference = todayDate.getTime() - createdAtDate.getTime();
            }
            hh = (int) (TimeUnit.MILLISECONDS.toHours(timeDifference));
            mm = (int) (TimeUnit.MILLISECONDS.toMinutes(timeDifference) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDifference)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (hh == 0)
            return mm + " min";
        else {
            if (mm <= 9)
                return hh + ":0" + mm + " hour";

            return hh + ":" + mm + " hour";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getBus().register(this);
        active = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getBus().unregister(this);
        active = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
