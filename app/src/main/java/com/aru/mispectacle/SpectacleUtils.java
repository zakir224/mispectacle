package com.aru.mispectacle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;

/**
 * Created by Md Zakir Hossen on 4/14/2015.
 */
public class SpectacleUtils {

    private static final String TAG = "spectacle_utils";

    public static Bitmap matToBitmap(Mat src,int width,int height) {
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, newBitmap);

        return newBitmap;
    }

    public static float getAngle(Point source,Point target) {
        Point p;
//        if(target.x > source.x){
//            p = target;
//            target = source;
//            source = p;
//        }

        float angle = (float) Math.toDegrees(Math.atan2(target.y - source.y, target.x - source.x));
        Log.i(TAG, "Angle = " + angle);
//        if(angle < 0) {
//            angle += 360;
//            angle = angle % 90;
//        }

        if(angle > 90) {
            angle = angle % 90;
            if(angle > 0)
                angle =  angle - 90;
        }


        Log.i(TAG,"Angle = "+angle);
        return angle;
    }


    public static double getDistance(Point p1,Point p2) {
        Log.i(TAG,"Point 1 "+p1.x + " " + p1.y);
        Log.i(TAG,"Point 2 "+p2.x + " " + p2.y);
        return Math.sqrt((p2.x-p1.x)*(p2.x-p1.x) + (p2.y-p1.y)* (p2.y-p1.y));
    }

//    public static double distance(double x1,double y1,double x2,double y2)
//    {
//        return sqrt(pow((x2-x1),2)+pow((y2-y1),2));
//    }
//
//    public static void move_fighter(double xf,double yf,double x,double y,double dis)
//    {
//        double adj,oppo,v = 25.0;
//        double cos,sin,dx,dy;
//
//        oppo = y-*yf;
//        adj= x-*xf;
//        cos = adj / dis;
//        sin = oppo / dis;
//        dx = 15.0 * cos;
//        dy = 15.0 * sin;
//    }


    public static Bitmap placeSpectacle(Bitmap bmp1, Bitmap bmp2, int top, int left, float angle,Context c) {
        Matrix matrix = new Matrix();


        matrix.postRotate(angle);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bmp2,0,0,bmp2.getWidth(),bmp2.getHeight(),matrix,true);

        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, 0, 0, null);
        if(angle<0 || angle>1)
            canvas.drawBitmap(rotatedBitmap, left, top*((90+angle)/100)+dpToPx(5,c), null);
        else
            canvas.drawBitmap(rotatedBitmap, left, top, null);

        return bmOverlay;
    }

    static int dpToPx(int dp, Context c) {
        float density = c.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


}
