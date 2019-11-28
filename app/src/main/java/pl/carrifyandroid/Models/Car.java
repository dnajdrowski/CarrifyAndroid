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

    private int carState;
    private int fuelLevel;
    private int id;
    private CarLocationLogs lastLocation;
    private String lastService;
    private int mileage;
    private String name;
    private String registrationNumber;
    private int serviceMode;

}
