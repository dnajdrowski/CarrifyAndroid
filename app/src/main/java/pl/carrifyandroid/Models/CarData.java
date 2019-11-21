package pl.carrifyandroid.Models;

public class CarData {

    private String lastService;
    private String lastSync;
    private int carState;
    private int fuelLevel;
    private int id;
    private int mileage;
    private String name;
    private String registrationNumber;
    private int serviceMode;
    private double latitude;
    private double longitude;

    public CarData(String lastService, String lastSync, int carState, int fuelLevel, int id, int mileage, String name, String registrationNumber, int serviceMode, double latitude, double longitude) {
        this.lastService = lastService;
        this.lastSync = lastSync;
        this.carState = carState;
        this.fuelLevel = fuelLevel;
        this.id = id;
        this.mileage = mileage;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.serviceMode = serviceMode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLastService() {
        return lastService;
    }

    public void setLastService(String lastService) {
        this.lastService = lastService;
    }

    public String getLastSync() {
        return lastSync;
    }

    public void setLastSync(String lastSync) {
        this.lastSync = lastSync;
    }

    public int getCarState() {
        return carState;
    }

    public void setCarState(int carState) {
        this.carState = carState;
    }

    public int getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(int fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(int serviceMode) {
        this.serviceMode = serviceMode;
    }
}
