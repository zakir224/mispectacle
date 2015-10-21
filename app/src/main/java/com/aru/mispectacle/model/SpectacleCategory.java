package com.aru.mispectacle.model;

/**
 * Created by Md Zakir Hossen on 10/08/2015.
 */
public class SpectacleCategory {

    public static final String COLUMN_CATEGORY_ID = "categoryId";
    public static final String COLUMN_CATEGORY_NAME = "categoryName";
    private long spectacleCategoryId;
    private String spectacleCategoryName;

    public SpectacleCategory() {
    }

    public String getSpectacleCategoryName() {
        return spectacleCategoryName;
    }

    public void setSpectacleCategoryName(String spectacleCategoryName) {
        this.spectacleCategoryName = spectacleCategoryName;
    }

    public long getSpectacleCategoryId() {
        return spectacleCategoryId;
    }

    public void setSpectacleCategoryId(long spectacleCategoryId) {
        this.spectacleCategoryId = spectacleCategoryId;
    }
}
