package pl.carrifyandroid.Screens.Maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.otto.Subscribe;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.ButterKnife;
import pl.carrifyandroid.App;
import pl.carrifyandroid.Models.AttachMaps;
import pl.carrifyandroid.Models.BusLocation;
import pl.carrifyandroid.Models.Car;
import pl.carrifyandroid.Models.ClusterMarker;
import pl.carrifyandroid.Models.EndRent;
import pl.carrifyandroid.Models.RegionZone;
import pl.carrifyandroid.Models.RegionZoneCoords;
import pl.carrifyandroid.Models.Rent;
import pl.carrifyandroid.Models.RentChange;
import pl.carrifyandroid.Models.Reservation;
import pl.carrifyandroid.Models.ReservationChange;
import pl.carrifyandroid.R;
import pl.carrifyandroid.Screens.CarPreview.CarPreviewDialog;
import pl.carrifyandroid.Screens.Dialogs.WarningDialog;
import pl.carrifyandroid.Utils.EventBus;
import pl.carrifyandroid.Utils.LocationUtils;
import pl.carrifyandroid.Utils.StorageHelper;

import static android.widget.Toast.LENGTH_LONG;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<ClusterMarker>,
        ClusterManager.OnClusterInfoWindowClickListener<ClusterMarker>,
        ClusterManager.OnClusterItemClickListener<ClusterMarker>,
        ClusterManager.OnClusterItemInfoWindowClickListener<ClusterMarker> {

    @Inject
    MapsManager mapsManager;
    @Inject
    StorageHelper storageHelper;

    private ClusterManager<ClusterMarker> mClusterManager;
    private GoogleMap mMap;
    private Location myLastLocation;
    private Location myLocation;
    private LruCache<Integer, BitmapDescriptor> cache;
    private Handler carDownloadHandler = new Handler();
    private Runnable runnable;

    private boolean mapLoaded = false;
    private boolean initMe = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_maps, null, false);
        ButterKnife.bind(this, view);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        MapsInitializer.initialize(Objects.requireNonNull(getContext()).getApplicationContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        App.component.inject(this);
        new LocationUtils(Objects.requireNonNull(getActivity())).turnGPSOn(isGPSEnable -> {
        });
        EventBus.getBus().post(new AttachMaps(true));
        initCache();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapLoaded = true;
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        Objects.requireNonNull(getActivity()), R.raw.style2));

        LatLng latLngMe = new LatLng(54.406587, 18.594610);

        CameraPosition cameraPositionMe = new CameraPosition.Builder()
                .target(latLngMe)           // Sets the center of the map to location user
                .zoom(14)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        if (getActivity() != null) {
            if (mMap != null) {
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPositionMe));
            }
        }

        Objects.requireNonNull(mMap).getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mClusterManager = new ClusterManager<>(getActivity(), mMap);
        final CameraPosition[] mPreviousCameraPosition = {null};
        mMap.setOnCameraIdleListener(() -> {
            CameraPosition position = googleMap.getCameraPosition();
            if (mPreviousCameraPosition[0] == null || mPreviousCameraPosition[0].zoom != position.zoom) {
                mPreviousCameraPosition[0] = googleMap.getCameraPosition();
                mClusterManager.cluster();
            }
        });
        mClusterManager.setRenderer(new MyClusterRenderer(getActivity(), mMap,
                mClusterManager));

        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        mapsManager.getRegionZones(1);
        mapsManager.getCarsData();

        MapsInitializer.initialize(Objects.requireNonNull(getContext()).getApplicationContext());
        getCarsFromApi();
        mapsManager.checkDriverLicense();
    }

    @Subscribe
    public void meLocation(BusLocation event) {
        myLastLocation = myLocation;
        myLocation = event.getLocation();

        if (myLocation == null)
            return;

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        if (!initMe) {
            LatLng latLngMe;
            if (myLocation == null) {
                latLngMe = new LatLng(54.406587, 18.594610);
            } else {
                latLngMe = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            }

            CameraPosition cameraPositionme = new CameraPosition.Builder()
                    .target(latLngMe)             // Sets the center of the map to location user
                    .zoom(10)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder

            if (getActivity() != null) {
                if (mMap != null)
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPositionme));
                initMe = true;
            }
        }
    }

    @Subscribe
    public void onEndRent(EndRent endRent) {
        mapsManager.endRent(endRent.getRentId());
    }

    private void getCarsFromApi() {
        carDownloadHandler.postDelayed(runnable = () -> {
            mapsManager.getCarsData();
            carDownloadHandler.postDelayed(runnable, 120000);
        }, 0);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorResId, int sizeX, int sizeY) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        Objects.requireNonNull(vectorDrawable).setBounds(0, 0, sizeX, sizeY);
        Bitmap bitmap = Bitmap.createBitmap(sizeX, sizeY, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void initCache() {
        cache = new LruCache<>((int) (Runtime.getRuntime().maxMemory() / 1024 / 8));
    }

    private BitmapDescriptor getBitmapDescriptor(@DrawableRes int vectorResId, int sizeX, int sizeY) {
        BitmapDescriptor result = cache.get(vectorResId);
        if (result == null) {
            result = bitmapDescriptorFromVector(getContext(), vectorResId, sizeX, sizeY);
            cache.put(vectorResId, result);
        }
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapsManager.onAttach(this);
        EventBus.getBus().register(this);
        mapsManager.getActiveRents();
        mapsManager.getActiveReservations();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapsManager.onStop();
        EventBus.getBus().unregister(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getBus().post(new AttachMaps(false));
        super.onDestroy();
    }

    void drawRegionZones(RegionZone regionZone) {
        PolygonOptions polygonOptions = new PolygonOptions();
        List<RegionZoneCoords> points = regionZone.getRegionZoneCoordsDTO();
        for (RegionZoneCoords point : points) {
            polygonOptions.add(new LatLng(point.getLatitude(), point.getLongitude()));
        }
        polygonOptions.strokeColor(Color.parseColor(regionZone.getStrokeColor()));
        polygonOptions.strokeWidth(regionZone.getStrokeWidth());
        polygonOptions.fillColor(Color.parseColor(regionZone.getZoneColor()));
        Polygon polygon1 = mMap.addPolygon(polygonOptions);
    }


    void showCarsOnMap(List<Car> cars) {
        if (mClusterManager != null) {
            mClusterManager.clearItems();
            if (getActivity() != null) {
                for (Car car : cars) {
                    ClusterMarker item = new ClusterMarker(car);
                    mClusterManager.addItem(item);
                }
                mClusterManager.cluster();
            }
        }
    }

    void showRentResponse(Rent body, boolean validRent) {
        if (body != null)
            EventBus.getBus().post(new RentChange(validRent, body));
        if (!validRent)
            FancyToast.makeText(getContext(), "You have successfully completed your rental!", LENGTH_LONG,
                    FancyToast.SUCCESS, false).show();
    }

    void showReservationResponse(Reservation body, boolean validReservation) {
        if (body != null)
            EventBus.getBus().post(new ReservationChange(validReservation, body));
        if (!validReservation)
            FancyToast.makeText(getContext(), "Your reservation has expired!", LENGTH_LONG,
                    FancyToast.SUCCESS, false).show();
    }

    void checkDriverLicense(Integer body) {
        if (body == 0) {
            WarningDialog.showWarningDialog(getActivity());
        }
    }

    class MyClusterRenderer extends DefaultClusterRenderer<ClusterMarker> {
        MyClusterRenderer(Context context, GoogleMap map,
                          ClusterManager<ClusterMarker> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(ClusterMarker item,
                                                   MarkerOptions markerOptions) {
            super.onBeforeClusterItemRendered(item, markerOptions);


            int icon = Objects.requireNonNull(getActivity()).getResources().getIdentifier("car_icon", "drawable", getActivity().getPackageName());

            markerOptions.icon(getBitmapDescriptor(icon, 120, 45));

        }

        protected void onBeforeClusterRendered(Cluster<ClusterMarker> cluster, MarkerOptions markerOptions) {
//            float zoom = mMap.getCameraPosition().zoom;
//            int clusterSize = 4;
//            if (zoom > 10) {
//                if (cluster.getSize() == 5)
//                    clusterSize = 5;
//                if (cluster.getSize() == 6)
//                    clusterSize = 6;
//                if (cluster.getSize() == 7)
//                    clusterSize = 7;
//                if (cluster.getSize() == 8)
//                    clusterSize = 8;
//                if (cluster.getSize() == 9)
//                    clusterSize = 9;
//                if (cluster.getSize() > 9)
//                    clusterSize = 10;
//                if (cluster.getSize() > 19)
//                    clusterSize = 20;
//                if (cluster.getSize() > 29)
//                    clusterSize = 30;
//                if (cluster.getSize() > 39)
//                    clusterSize = 40;
//                if (cluster.getSize() > 49)
//                    clusterSize = 50;
//                if (cluster.getSize() > 59)
//                    clusterSize = 60;
//                if (cluster.getSize() > 69)
//                    clusterSize = 70;
//                if (cluster.getSize() > 79)
//                    clusterSize = 80;
//                if (cluster.getSize() > 89)
//                    clusterSize = 90;
//                if (cluster.getSize() > 99)
//                    clusterSize = 100;
//                if (getContext() != null && getActivity() != null) {
//                    BitmapDescriptor icon2 = getBitmapDescriptor(Objects.requireNonNull(getActivity()).getResources().getIdentifier("c" + clusterSize, "drawable", getActivity().getPackageName()), 120);
//                    if (icon2 != null)
//                        markerOptions.icon(icon2);
//                }
//            } else {
//                if (getContext() != null) {
//                    BitmapDescriptor icon2 = getBitmapDescriptor(R.drawable.group_scooter, 100);
//                    if (icon2 != null)
//                        markerOptions.icon(icon2);
//                }
//            }
        }

        @Override
        protected void onClusterItemRendered(ClusterMarker clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<ClusterMarker> cluster) {
            return cluster.getSize() > 999;
        }

    }


    @Override
    public boolean onClusterClick(Cluster<ClusterMarker> cluster) {
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<ClusterMarker> cluster) {

    }

    @Override
    public boolean onClusterItemClick(ClusterMarker clusterMarker) {
        CarPreviewDialog carPreviewDialog = new CarPreviewDialog();
        Bundle bundle = new Bundle();
        bundle.putString("name", clusterMarker.getName());
        bundle.putInt("fuelLevel", clusterMarker.getFuelLevel());
        bundle.putInt("carId", clusterMarker.getId());
        carPreviewDialog.setArguments(bundle);
        if (getFragmentManager() != null)
            carPreviewDialog.show(getFragmentManager(), "CarPreview");
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(ClusterMarker clusterMarker) {

    }
}
