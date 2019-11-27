package pl.carrifyandroid.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RegionZoneCoords {

    private int id;
    private double latitude;
    private double longitude;
    private String createdAt;

}
