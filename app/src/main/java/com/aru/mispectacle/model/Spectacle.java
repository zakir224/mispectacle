package com.aru.mispectacle.model;

/**
 * Created by Md Zakir Hossen on 10/16/2015.
 */
public class Spectacle {

    public static final String COLUMN_SPECTACLE_ID = "spectacleId";
    public static final String COLUMN_SPECTACLE_BRAND = "spectacleBrand";
    public static final String COLUMN_SPECTACLE_PRICE = "spectaclePrice";
    public static final String COLUMN_SPECTACLE_CATEGORY = "spectacleCategoryId";

    private long spectacleId;
    private String spectacleBrand;
    private double spectaclePrice;
    private long spectacleIdCategoryId;

    public long getSpectacleIdCategoryId() {
        return spectacleIdCategoryId;
    }

    public void setSpectacleIdCategoryId(long spectacleIdCategoryId) {
        this.spectacleIdCategoryId = spectacleIdCategoryId;
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
}
