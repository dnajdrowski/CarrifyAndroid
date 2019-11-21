package pl.carrifyandroid;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.material.navigation.NavigationView;
import com.johnnylambada.location.LocationProvider;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.carrifyandroid.Models.BusLocation;
import pl.carrifyandroid.Screens.Maps.MapsFragment;
import pl.carrifyandroid.Utils.EventBus;
import pl.carrifyandroid.Utils.StorageHelper;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Inject
    StorageHelper storageHelper;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private RecyclerView menuRecycler;
    private TextView navUsername;
    private ImageView avatar;

    private boolean attachMaps = false;
    private boolean attachLoc = false;
    private static boolean active = false;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Handler h3 = new Handler();
    private Runnable runnable3;
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

        menuRecycler = navigationView.findViewById(R.id.menuRecycler);

        View headerView = navigationView.getHeaderView(0);
        //username
        navUsername = headerView.findViewById(R.id.username);
        //avatar
        avatar = headerView.findViewById(R.id.avatar);
        Glide.with(MainActivity.this).load(R.drawable.car_avatar).circleCrop().into(avatar);
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
                    EventBus.get().post(new BusLocation(location));
                    if (location != null)
                        Timber.d(location.getLatitude() + "," + location.getLongitude());
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
                        EventBus.get().post(new BusLocation(location));
                    }
                    if (location != null)
                        Timber.d(location.getLatitude() + "," + location.getLongitude());
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
        h3.postDelayed(runnable3 = () -> {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (!attachLoc) {
                    Toast.makeText(this, "We can't find your location, please turn on GPS!", Toast.LENGTH_SHORT).show();
                    //Intent localizeIntent = new Intent(this, MapsLocalizeActivity.class);
                    //startActivityForResult(localizeIntent, 1547);
                    attachLoc = true;
                }
            } else
                if (!attachMaps)
                    initLoc();
            h3.postDelayed(runnable3, delLoc);
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
    protected void onResume() {
        super.onResume();
        EventBus.get().register(this);
        active = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.get().unregister(this);
        active = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}
