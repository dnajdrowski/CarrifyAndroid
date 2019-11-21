package pl.carrifyandroid.Models;

import android.location.Location;

public class BusLocation {
    private Location location;

    public BusLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
