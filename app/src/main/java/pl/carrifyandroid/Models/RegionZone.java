package pl.carrifyandroid.Models;

import java.util.List;

public class RegionZone {

    private int id;
    private String createdAt;
    private String strokeColor;
    private List<RegionZoneCoords> regionZoneCoords;
    private int strokeWidth;
    private String zoneColor;

    public RegionZone(int id, String createdAt, String strokeColor, List<RegionZoneCoords> regionZoneCoords, int strokeWidth, String zoneColor) {
        this.id = id;
        this.createdAt = createdAt;
        this.strokeColor = strokeColor;
        this.regionZoneCoords = regionZoneCoords;
        this.strokeWidth = strokeWidth;
        this.zoneColor = zoneColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public List<RegionZoneCoords> getRegionZoneCoords() {
        return regionZoneCoords;
    }

    public void setRegionZoneCoords(List<RegionZoneCoords> regionZoneCoords) {
        this.regionZoneCoords = regionZoneCoords;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public String getZoneColor() {
        return zoneColor;
    }

    public void setZoneColor(String zoneColor) {
        this.zoneColor = zoneColor;
    }
}
