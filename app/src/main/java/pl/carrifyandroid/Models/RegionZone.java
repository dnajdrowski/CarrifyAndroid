package pl.carrifyandroid.Models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RegionZone {

    private int id;
    private int strokeWidth;
    private String strokeColor;
    private String zoneColor;
    private String createdAt;
    private List<RegionZoneCoords> regionZoneCoordsDTO;

}
