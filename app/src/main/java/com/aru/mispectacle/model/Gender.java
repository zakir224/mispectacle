package com.aru.mispectacle.model;

/**
 * Created by Md Zakir Hossen on 10/16/2015.
 */
public class Gender {

    public static final String TABLE_NAME = "gender";
    public static final String COLUMN_GENDER_ID = "genderId";
    public static final String COLUMN_GENDER_NAME = "genderName";


    private long genderId;
    private String genderName;

    public Gender() {

    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public long getGenderId() {
        return genderId;
    }

    public void setGenderId(long genderId) {
        this.genderId = genderId;
    }


}
