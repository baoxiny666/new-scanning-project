package com.tglh.newscanningproject.scanning.entity;

import lombok.Data;

@Data
public class Location {
    private String  longitude;
    private String latitude;

    private String locationTotal;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocationTotal() {
        return this.longitude + "," +this.latitude;
    }


}
