package com.aru.mispectacle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.aru.mispectacle.exception.FaceNotDetectedException;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Md Zakir Hossen on 4/14/2015.
 */
public class FaceDetectionHelper {

    private final String TAG = "OPENCV";
    private static final Scalar FACE_RECT_COLOR = new Scalar(255, 0, 0, 0);
    private int glassWidth;
    private int glassHeight;
    private Mat mRgba;
    private Mat mGray;
    private int eyesDist;
    private Rect leftEye = new Rect();
    private Rect rightEye = new Rect();
    private File mCascadeFile;
    private CascadeClassifier faceCascadeClassifier;
    private CascadeClassifier eyeCascadeClassifier;
    private Point leftEyeCenter;
    private Point rightEyeCenter;
    private Rect[] eyesArray;
    Context context;

    public int getGlassWidth() {
        return glassWidth;
    }

    public void setGlassWidth(int glassWidth) {
        this.glassWidth = glassWidth;
    }

    public int getGlassHeight() {
        return glassHeight;
    }

    public void setGlassHeight(int glassHeight) {
        this.glassHeight = glassHeight;
    }

    public Point getLeftEyeCenter() {
        return leftEyeCenter;
    }

    public void setLeftEyeCenter(Point leftEyeCenter) {
        this.leftEyeCenter = leftEyeCenter;
    }

    public Point getRightEyeCenter() {
        return rightEyeCenter;
    }

    public void setRightEyeCenter(Point rightEyeCenter) {
        this.rightEyeCenter = rightEyeCenter;
    }

    public Rect getLeftEye() {
        return leftEye;
    }

    public void setLeftEye(Rect leftEye) {
        this.leftEye = leftEye;
    }

    public Rect getRightEye() {
        return rightEye;
    }

    public void setRightEye(Rect rightEye) {
        this.rightEye = rightEye;
    }

    public int getEyesDist() {
        return eyesDist;
    }

    public void setEyesDist(int eyesDist) {
        this.eyesDist = eyesDist;
    }


    FaceDetectionHelper(Context context) {
        this.context = context;
    }

    public Mat getEyeMatrix(Bitmap bitmap) throws FaceNotDetectedException {

        MatOfRect eyes = new MatOfRect();
        mGray = new Mat();
        mRgba = new Mat();

        Utils.bitmapToMat(bitmap, mRgba);
        Utils.bitmapToMat(bitmap, mGray);

        MatOfRect faces = new MatOfRect();
        if (faceCascadeClassifier != null)
            faceCascadeClassifier.detectMultiScale(mGray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                    new Size(0, 0), new Size());

        MatOfRect eyesR = new MatOfRect();
        Rect[] facesArray = faces.toArray();

        if (facesArray.length < 1)
            throw new FaceNotDetectedException("No face could be detected. Please try another photo or Rotate the image");
        else {
            Mat faceROI = mRgba.submat(facesArray[0]);

            //Core.rectangle(mRgba, facesArray[0].tl(), facesArray[0].br(), FACE_RECT_COLOR, 3);

            eyeCascadeClassifier.detectMultiScale(mGray, eyes, 1.1, 2, 0, new Size(50, 40), new Size());
            eyesArray = eyes.toArray();

            if (eyesArray.length < 2)
                throw new FaceNotDetectedException("Couldn't identify eyes. Please try again or Rotate the Photo");
            else {
                Log.i(TAG, "Eye " + 0 + "-" + eyesArray[0].x + " " + eyesArray[0].y);
                Log.i(TAG, "Eye " + 1 + "-" + eyesArray[1].x + " " + eyesArray[1].y);


                if (eyesArray[0].x > eyesArray[1].x) {
                    leftEye = eyesArray[1];
                    rightEye = eyesArray[0];
                    leftEyeCenter = new Point(eyesArray[1].x + eyesArray[1].width * 0.5, eyesArray[1].y + eyesArray[1].height * 0.5);
                    rightEyeCenter = new Point(eyesArray[0].x + eyesArray[0].width * 0.5, eyesArray[0].y + eyesArray[0].height * 0.5);
                } else {
                    leftEye = eyesArray[0];
                    rightEye = eyesArray[1];
                    leftEyeCenter = new Point(eyesArray[0].x + eyesArray[0].width * 0.5, eyesArray[0].y + eyesArray[0].height * 0.5);
                    rightEyeCenter = new Point(eyesArray[1].x + eyesArray[1].width * 0.5, eyesArray[1].y + eyesArray[1].height * 0.5);
                }

                Log.i(TAG, "LeftEye -" + leftEye.x + " " + leftEye.y);
                Log.i(TAG, "RightEye -" + rightEye.x + " " + rightEye.y);

                eyesDist = rightEye.x - leftEye.x;

//            if(facesArray.length > 0) {
//                glassWidth = facesArray[0].width;
//                Toast.makeText(context,"Face Found",Toast.LENGTH_LONG).show();
//            } else
                glassWidth = (int)(( eyesDist * 2) + eyesDist / 4.5);
                glassHeight = eyesDist / 2 + 10;

//                glassWidth = (int) SpectacleUtils.getDistance(new Point(leftEye.x, leftEye.y),
//                        new Point(rightEye.x + rightEye.width, rightEye.y+ rightEye.height));
                Log.d(TAG, "glass width/height " + glassWidth + " " + glassHeight);


                return mRgba;
            }
        }

    }


    public BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(context) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i("OPENCV", "OpenCV loaded successfully");

                    try {
                        // load cascade file from application resources
                        InputStream is = context.getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        faceCascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (faceCascadeClassifier.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            faceCascadeClassifier = null;
                        } else
                            Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

                        cascadeDir.delete();

                        is = context.getResources().openRawResource(R.raw.haarcascade_eye_tree_eyeglasses);
                        cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "haarcascade_eye_tree_eyeglasses.xml");
                        os = new FileOutputStream(mCascadeFile);

                        buffer = new byte[4096];

                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        eyeCascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (eyeCascadeClassifier.empty()) {
                            Log.e(TAG, "Failed to load eye cascade classifier");
                            eyeCascadeClassifier = null;
                        } else
                            Log.i(TAG, "Loaded eye cascade classifier from " + mCascadeFile.getAbsolutePath());


                        cascadeDir.delete();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }
                }
                break;
                default: {
                    super.onManagerConnected(status);
                    Log.e(TAG, "Failed to load Opencv: ");
                }
                break;
            }
        }
    };
}
