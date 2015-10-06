package com.aru.mispectacle;

import android.app.Activity;
import android.os.Bundle;

import java.io.*;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import android.app.ProgressDialog;
import android.util.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.os.Looper;

public class ASMModelActivity extends Activity {
    private final String TAG = "StasmAndroidDemo";
    private static final int UPDATE_VIEW = 0x101;
    private boolean debug = false;
    private ProgressDialog pd = null;
    public static int screenWW;
    public static int screenHH;
    private SampleView sv;
    private int mWidth;
    private int mHeight;
    private Bitmap mImage = null;   //current bitmap, used when save
    private Mat mRgba;

    private File dataDir = null;
    private File f_frontalface = null;
    private File f_lefteye = null;
    private File f_righteye = null;
    private File f_testface = null;
    private float ratioW = 0;
    private float ratioH = 0;
    private Button process;

    public native int[] FindFaceLandmarks(float ratioW, float ratioH);

    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d("ERROR", "Unable to load OpenCV");
        } else {

        }
    }

//    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
//        @Override
//        public void onManagerConnected(int status) {
//            switch (status) {
//                case LoaderCallbackInterface.SUCCESS:
//                    if (debug) Log.e(TAG, "OpenCV loaded successfully");
//                    // Load native library after(!) OpenCV initialization
//                    System.loadLibrary("native_sample");
//                    sv.setEnabled(true);
//                    processing();
//                    break;
//                default:
//                    super.onManagerConnected(status);
//                    break;
//            }
//        }
//    };

    private boolean isDataFileInLocalDir(Context context) {
        boolean ret = false;
        try {
            dataDir = context.getDir("stasmdata", Context.MODE_PRIVATE);
            f_frontalface = new File(dataDir, "haarcascade_frontalface_alt2.xml");
            f_lefteye = new File(dataDir, "haarcascade_mcs_lefteye.xml");
            f_righteye = new File(dataDir, "haarcascade_mcs_righteye.xml");

            ret = f_frontalface.exists() && f_lefteye.exists() && f_righteye.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private void putDataFileInLocalDir(Context context, int id, File f) {
        if (debug) Log.e(TAG, "putDataFileInLocalDir: " + f.toString());
        try {
            InputStream is = context.getResources().openRawResource(id);
            FileOutputStream os = new FileOutputStream(f);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (debug) Log.e(TAG, "putDataFileInLocalDir: done!");
    }

    private Handler myViewUpdateHandler = new Handler() {
        // @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ASMModelActivity.UPDATE_VIEW:
                    sv.invalidate();
                    try {
                        pd.dismiss();
                        pd = null;
                    } catch (Exception e) {
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setTheme(android.R.style.Theme_Light);
        setContentView(R.layout.main);
        setTitle("Mi-spectacle");

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int ori = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        screenWW = display.getWidth();
        screenHH = display.getHeight();
        if (debug)
            Log.e(getString(R.string.app_name), ori + " screenWW=" + display.getWidth() + " screenHH=" + display.getHeight());

        if (!isDataFileInLocalDir(ASMModelActivity.this)) {
            putDataFileInLocalDir(ASMModelActivity.this, R.raw.haarcascade_frontalface_alt2, f_frontalface);
            putDataFileInLocalDir(ASMModelActivity.this, R.raw.haarcascade_mcs_lefteye, f_lefteye);
            putDataFileInLocalDir(ASMModelActivity.this, R.raw.haarcascade_mcs_righteye, f_righteye);

            f_testface = new File(dataDir, "face.jpg");
            putDataFileInLocalDir(ASMModelActivity.this, R.drawable.people_1, f_testface);
        }

        sv = (SampleView) findViewById(R.id.sv);
        Initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_5, this, mLoaderCallback);
    }

    private void processing() {
        if (pd == null) pd = ProgressDialog.show(ASMModelActivity.this, null, "Processing...");
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();

                if (mImage != null) {
                    int[] points = FindFaceLandmarks(ratioW, ratioH);
                    if (debug) Log.e(TAG, "" + points.length);
                    //handle possible error
                    if ((points[0] == -1) && (points[1] == -1)) {
                        Toast.makeText(ASMModelActivity.this, "Cannot load image file /data/data/org.androidhat.stasmandroiddemo/app_stasmdata/testface.jpg", Toast.LENGTH_LONG).show();
                    } else if ((points[0] == -2) && (points[1] == -2)) {
                        Toast.makeText(ASMModelActivity.this, "Error in stasm_search_single!", Toast.LENGTH_LONG).show();
                    } else if ((points[0] == -3) && (points[1] == -3)) {
                        Toast.makeText(ASMModelActivity.this, "No face found in /data/data/org.androidhat.stasmandroiddemo/app_stasmdata/testface.jpg", Toast.LENGTH_LONG).show();
                    } else {
                        sv.setBM(mImage, points);
                    }
                }

                Message m = new Message();
                m.what = ASMModelActivity.UPDATE_VIEW;
                ASMModelActivity.this.myViewUpdateHandler.sendMessage(m);
                Looper.loop();
            }
        }).start();
    }

    void Initialize() {
        try {
            Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.people_1);
            if (debug) Log.e(TAG, "Original Image: " + temp.getWidth() + "X" + temp.getHeight());

            int finalImgWidth = screenWW;
            int finalImgHeight = (temp.getHeight() * finalImgWidth) / temp.getWidth();
            mImage = Bitmap.createScaledBitmap(temp, finalImgWidth, finalImgHeight, true);
            mWidth = mImage.getWidth();
            mHeight = mImage.getHeight();
            ratioW = (float) mWidth / temp.getWidth();
            ratioH = (float) mHeight / temp.getHeight();
            if (debug)
                Log.e(TAG, "Scaled Image: " + mWidth + "X" + mHeight + " " + ratioW + " " + ratioH);
            process = (Button) findViewById(R.id.process_btn);
            process.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.loadLibrary("native_sample");
                    processing();
                }
            });
            sv = (SampleView) findViewById(R.id.sv);
            sv.setBM(mImage);
            sv.invalidate();
            temp.recycle();
        } catch (Exception e) {
            if (debug) Log.e(TAG, "Initialize():" + e.toString());
        } catch (OutOfMemoryError oe) {
        }
    }
}
