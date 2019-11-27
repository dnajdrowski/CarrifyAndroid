package pl.carrifyandroid.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Car {

    private int id;
    private String name;
    private int fuelLevel;
    private String registrationNumber;
    private int serviceMode;
    private int mileage;
    private int carState;
    private CarLocationLogs lastLocation;

}
