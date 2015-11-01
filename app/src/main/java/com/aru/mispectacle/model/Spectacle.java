package com.aru.mispectacle.model;

/**
 * Created by Md Zakir Hossen on 10/16/2015.
 */
public class Spectacle {

    public static final String TABLE_NAME = "spectacleId";
    public static final String COLUMN_SPECTACLE_ID = "spectacleId";
    public static final String COLUMN_SPECTACLE_BRAND = "spectacleBrand";
    public static final String COLUMN_SPECTACLE_PRICE = "spectaclePrice";
    public static final String COLUMN_SPECTACLE_CATEGORY_ID = "spectacleCategoryId";
    public static final String COLUMN_SPECTACLE_GENDER_ID = "spectacleGenderId";
    public static final String COLUMN_SPECTACLE_LOCATION_URI = "locationUri";


    private long spectacleId;
    private String spectacleBrand;
    private double spectaclePrice;
    private long spectacleCategoryId;
    private String spectacleLocationUri;
    private long spectacleGenderId;

    public Spectacle() {

    }

    public Spectacle(String spectacleBrand, long spectacleCategoryId, double spectaclePrice) {
        this.spectacleBrand = spectacleBrand;
        this.spectacleCategoryId = spectacleCategoryId;
        this.spectaclePrice = spectaclePrice;
    }

    public long getSpectacleCategoryId() {
        return spectacleCategoryId;
    }

    public void setSpectacleCategoryId(long spectacleCategoryId) {
        this.spectacleCategoryId = spectacleCategoryId;
    }

    public double getSpectaclePrice() {
        return spectaclePrice;
    }

    public void setSpectaclePrice(double spectaclePrice) {
        this.spectaclePrice = spectaclePrice;
    }

    public String getSpectacleBrand() {
        return spectacleBrand;
    }

    public void setSpectacleBrand(String spectacleBrand) {
        this.spectacleBrand = spectacleBrand;
    }

    public long getSpectacleId() {
        return spectacleId;
    }

    public void setSpectacleId(long spectacleId) {
        this.spectacleId = spectacleId;
    }

    public String getSpectacleLocationUri() {
        return spectacleLocationUri;
    }

    public void setSpectacleLocationUri(String spectacleLocationUri) {
        this.spectacleLocationUri = spectacleLocationUri;
    }

    public long getSpectacleGenderId() {
        return spectacleGenderId;
    }

    public void setSpectacleGenderId(long spectacleGenderId) {
        this.spectacleGenderId = spectacleGenderId;
    }
}
