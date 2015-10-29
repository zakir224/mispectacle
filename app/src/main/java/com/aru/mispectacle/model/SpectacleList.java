package com.aru.mispectacle.model;

/**
 * Created by Md Zakir Hossen on 10/16/2015.
 */
public class SpectacleList {

    public static final String TABLE_NAME = "spectacleList";
    public static final String COLUMN_CATEGORY_ID = "categoryId";
    public static final String COLUMN_GENDER_ID = "genderId";
    public static final String COLUMN_SHAPE_ID = "shapeId";


    private long categoryId;
    private long genderId;
    private long shapeId;


    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getGenderId() {
        return genderId;
    }

    public void setGenderId(long genderId) {
        this.genderId = genderId;
    }

    public long getShapeId() {
        return shapeId;
    }

    public void setShapeId(long shapeId) {
        this.shapeId = shapeId;
    }
}
