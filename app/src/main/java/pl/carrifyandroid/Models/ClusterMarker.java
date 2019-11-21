package pl.carrifyandroid.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {

    private LatLng mPosition;
    private String lastService;
    private String lastSync;
    private int carState;
    private int fuelLevel;
    private int id;
    private int mileage;
    private String name;
    private String registrationNumber;
    private int serviceMode;

    public ClusterMarker(LatLng mPosition, String lastService, String lastSync, int carState, int fuelLevel, int id, int mileage, String name, String registrationNumber, int serviceMode) {
        this.mPosition = mPosition;
        this.lastService = lastService;
        this.lastSync = lastSync;
        this.carState = carState;
        this.fuelLevel = fuelLevel;
        this.id = id;
        this.mileage = mileage;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.serviceMode = serviceMode;
    }

    public String getLastService() {
        return lastService;
    }

    public String getLastSync() {
        return lastSync;
    }

    public int getCarState() {
        return carState;
    }

    public int getFuelLevel() {
        return fuelLevel;
    }

    public int getId() {
        return id;
    }

    public int getMileage() {
        return mileage;
    }

    public String getName() {
        return name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public int getServiceMode() {
        return serviceMode;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
