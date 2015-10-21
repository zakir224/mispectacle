package com.aru.mispectacle.model;

/**
 * Created by Md Zakir Hossen on 10/16/2015.
 */
public class FaceShape {

    public static final String COLUMN_FACE_SHAPE_ID = "shapeId";
    public static final String COLUMN_FACE_SHAPE_NAME = "shapeName";

    public enum Shape{

    }

    private long shapeId;
    private String shapeName;

    public FaceShape() {

    }

    public String getShapeName() {
        return shapeName;
    }

    public void setShapeName(String shapeName) {
        this.shapeName = shapeName;
    }

    public long getShapeId() {
        return shapeId;
    }

    public void setShapeId(long shapeId) {
        this.shapeId = shapeId;
    }
}
