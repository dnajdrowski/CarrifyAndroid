package pl.carrifyandroid.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ClusterMarker implements ClusterItem {

    private LatLng mPosition;
    private int id;
    private String name;
    private int fuelLevel;
    private String registrationNumber;
    private int serviceMode;
    private int mileage;
    private int carState;

    public ClusterMarker(Car car) {
        this.mPosition = new LatLng(car.getLastLocation().getLatitude(), car.getLastLocation().getLongitude());
        this.id = car.getId();
        this.name = car.getName();
        this.fuelLevel = car.getFuelLevel();
        this.registrationNumber = car.getRegistrationNumber();
        this.serviceMode = car.getServiceMode();
        this.mileage = car.getMileage();
        this.carState = car.getCarState();
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
