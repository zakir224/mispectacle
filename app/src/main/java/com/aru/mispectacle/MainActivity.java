package com.aru.mispectacle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.aru.mispectacle.exception.FaceNotDetectedException;

import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends ActionBarActivity implements DefaultPhotosFragment.OnFragmentInteractionListener {

    private final String TAG = "MainActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";

    private ImageView userImage;
    private Bitmap bmp;
    private Button btnChoosePhoto;
    private Mat m;
    private DefaultPhotosFragment defaultPhotosFragment;
    private Button tryOn;
    private Button selfie;
    private FaceDetectionHelper faceDetectionHelper;
    private DirectoryHelper directoryHelper;
    public native int[] FindFaceLandmarks(float ratioW, float ratioH);
    private Bitmap mFaceBitmap;
    private Bitmap spectacleBitmap;
    private Bitmap resultBitmap;
    private CannyDetector cannyDetector;
    private Button shapeBtn;
    private SeekBar seekBar;
    Bitmap cannyBitmap;
    private File dataDir = null;
    private File f_frontalface = null;
    private File f_lefteye = null;
    private File f_righteye = null;
    private File f_testface = null;
    int imageId;
    private Bitmap getMFaceBitmap() {
        return mFaceBitmap;
    }


    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d("ERROR", "Unable to load OpenCV");
        }
    }

    private boolean isDataFileInLocalDir(Context context) {
        boolean ret = false;
        try {
            dataDir = context.getDir("stasmdata", Context.MODE_PRIVATE);
            f_frontalface = new File(dataDir, "haarcascade_frontalface_alt2.xml");
            f_lefteye     = new File(dataDir, "haarcascade_mcs_lefteye.xml");
            f_righteye    = new File(dataDir, "haarcascade_mcs_righteye.xml");

            ret = f_frontalface.exists() && f_lefteye.exists() && f_righteye.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private void putDataFileInLocalDir(Context context, int id, File f) {
        //if (debug) Log.e(TAG, "putDataFileInLocalDir: "+f.toString());
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
        //if (debug) Log.e(TAG, "putDataFileInLocalDir: done!");
    }

    @Override
    public void onResume() {
        super.onResume();
        faceDetectionHelper.mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        initializeReferences();
        setListeners();
    }


    private void initializeReferences() {
        faceDetectionHelper = new FaceDetectionHelper(getBaseContext());
        directoryHelper = new DirectoryHelper(getBaseContext());
        cannyDetector = new CannyDetector();
        tryOn = (Button) findViewById(R.id.buttonTryOn);
        selfie = (Button) findViewById(R.id.buttonSelfie);
        btnChoosePhoto = ((Button) findViewById(R.id.buttonChoosePhoto));
        userImage = ((ImageView) findViewById(R.id.result));
        shapeBtn = (Button)findViewById(R.id.shape_btn);
        setMFaceBitmap(3);
        defaultPhotosFragment = new DefaultPhotosFragment();
        seekBar = (SeekBar)findViewById(R.id.seekBar);
    }

    private void setListeners() {
        selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        tryOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryOnSpectacle();
            }
        });

        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultPhotosFragment.show(getFragmentManager(), "");
            }
        });

        shapeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setCannyEdgePhoto(cannyDetector.getLowThreshold());
                //seekBar.setProgress(cannyDetector.getLowThreshold());
                if (!isDataFileInLocalDir(MainActivity.this)) {
                    putDataFileInLocalDir(MainActivity.this, R.raw.haarcascade_frontalface_alt2, f_frontalface);
                    putDataFileInLocalDir(MainActivity.this, R.raw.haarcascade_mcs_lefteye, f_lefteye);
                    putDataFileInLocalDir(MainActivity.this, R.raw.haarcascade_mcs_righteye, f_righteye);


                }
                f_testface = new File(dataDir, "face.jpg");
                putDataFileInLocalDir(MainActivity.this, imageId, f_testface);
                System.loadLibrary("native_sample");
                processing();
            }
        });


    }

    private void tryOnSpectacle() {
        try {
//            bmp = mFaceBitmap;
//            mFaceBitmap.recycle();
            m = faceDetectionHelper.getEyeMatrix(mFaceBitmap);
            bmp = SpectacleUtils.matToBitmap(m, mFaceBitmap.getWidth(), mFaceBitmap.getHeight());
            //bmp.recycle();
            setSpectacleBitmap();
            Float angle = SpectacleUtils.getAngle(faceDetectionHelper.getLeftEyeCenter(), faceDetectionHelper.getRightEyeCenter());
            setUserBitmap(SpectacleUtils.placeSpectacle(bmp, spectacleBitmap, faceDetectionHelper.getLeftEye().y + 10,
                    faceDetectionHelper.getLeftEye().x - (int) (faceDetectionHelper.getEyesDist() / 3), angle, this));


        } catch (FaceNotDetectedException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_open_cv, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mFaceBitmap);
        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mFaceBitmap != null));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mFaceBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        userImage.setImageBitmap(mFaceBitmap);
        userImage.setVisibility(
                savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            dispatchTakePictureIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setSpectacleBitmap() {
        Bitmap vv = BitmapFactory.decodeResource(getResources(), R.drawable.sp2);
        spectacleBitmap = Bitmap.createScaledBitmap(vv, faceDetectionHelper.getGlassWidth(), faceDetectionHelper.getGlassHeight(), true);
    }


    void setUserBitmap(Bitmap bitmap) {
        userImage.setImageBitmap(bitmap);
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File f = null;

        try {
//            f = setUpPhotoFile();
//            mCurrentPhotoPath = f.getAbsolutePath();
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            f = directoryHelper.setUpPhotoFile();
            directoryHelper.setmCurrentPhotoPath(f.getAbsolutePath());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
//            mCurrentPhotoPath = null;
            directoryHelper.setmCurrentPhotoPath(null);
        }


        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {
                if (directoryHelper.getmCurrentPhotoPath() != null) {
                    Log.d(TAG, "OnActivity result");
                    mFaceBitmap = directoryHelper.getPic(userImage);
                    cannyDetector.setM(mFaceBitmap);
                    setUserBitmap(mFaceBitmap);
                    directoryHelper.galleryAddPic();
//                    mCurrentPhotoPath = null;
                    directoryHelper.setmCurrentPhotoPath(null);
                    Log.d(TAG, "OnActivity result Finished");
                }
            }
        }
    }


    public void setCannyEdgePhoto(int threshold) {
        //cannyDetector.setLowThreshold(threshold);
        m = cannyDetector.detect();
        Utils.matToBitmap(m,mFaceBitmap);
        setUserBitmap(mFaceBitmap);
    }

    @Override
    public void onFragmentInteraction(int id) {
        defaultPhotosFragment.dismiss();
        setMFaceBitmap(id);
        Toast.makeText(this, "Photo " + id + " choosen", Toast.LENGTH_LONG).show();

    }


    private void setMFaceBitmap(int id) {
        switch (id) {
            case 1:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.heart_christina_1));
                cannyDetector.setM(mFaceBitmap);
                imageId = R.drawable.heart_christina_1;
                setUserBitmap(mFaceBitmap);
                break;
            case 2:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_2));
                cannyDetector.setM(mFaceBitmap);
                imageId = R.drawable.people_2;
                setUserBitmap(mFaceBitmap);
                break;
            case 3:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_3));
                cannyDetector.setM(mFaceBitmap);
                imageId = R.drawable.people_3;
                setUserBitmap(mFaceBitmap);
                break;
            case 4:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_4));
                cannyDetector.setM(mFaceBitmap);
                imageId = R.drawable.people_4;
                setUserBitmap(mFaceBitmap);
                break;
            case 5:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_5));
                cannyDetector.setM(mFaceBitmap);
                imageId = R.drawable.people_5;
                setUserBitmap(mFaceBitmap);
                break;
            case 6:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_6));
                cannyDetector.setM(mFaceBitmap);
                imageId = R.drawable.people_6;
                setUserBitmap(mFaceBitmap);
                break;
            case 7:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_7));
                cannyDetector.setM(mFaceBitmap);
                imageId = R.drawable.people_7;
                setUserBitmap(mFaceBitmap);
                break;
            case 8:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_8));
                cannyDetector.setM(mFaceBitmap);
                imageId = R.drawable.people_8;
                setUserBitmap(mFaceBitmap);
                break;
            case 9:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_9));
                cannyDetector.setM(mFaceBitmap);
                imageId = R.drawable.people_9;
                setUserBitmap(mFaceBitmap);
                break;
            case 10:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_10));
                cannyDetector.setM(mFaceBitmap);
                imageId = R.drawable.people_10;
                setUserBitmap(mFaceBitmap);
                break;
            case 11:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_11));
                cannyDetector.setM(mFaceBitmap);
                imageId = R.drawable.people_11;
                setUserBitmap(mFaceBitmap);
                break;
            default:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_1));
                cannyDetector.setM(mFaceBitmap);
                imageId = R.drawable.people_1;
                setUserBitmap(mFaceBitmap);
                break;
        }
    }
    ProgressDialog pd;
    int[] points;
    private void processing() {
        if (pd == null) pd = ProgressDialog.show(MainActivity.this, null, "Processing...");
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();

                if (mFaceBitmap != null) {
                    points = FindFaceLandmarks(0, 0);
                    //if (debug) Log.e(TAG, ""+points.length);
                    //handle possible error
                    if ((points[0] == -1) && (points[1] == -1)) {
                        Toast.makeText(MainActivity.this, "Cannot load image file /data/data/org.androidhat.stasmandroiddemo/app_stasmdata/testface.jpg", Toast.LENGTH_LONG).show();
                    } else if ((points[0] == -2) && (points[1] == -2)) {
                        Toast.makeText(MainActivity.this, "Error in stasm_search_single!", Toast.LENGTH_LONG).show();
                    } else if ((points[0] == -3) && (points[1] == -3)) {
                        Toast.makeText(MainActivity.this, "No face found in /data/data/org.androidhat.stasmandroiddemo/app_stasmdata/testface.jpg", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(getBaseContext(),"Landmark Recieved"
                                + points[0] +"," +points[1] ,Toast.LENGTH_SHORT).show();
                        getShape();
                    }
                    try {pd.dismiss(); pd = null;} catch (Exception e){}
                }

//                Message m = new Message();
//                m.what = ASMModelActivity.UPDATE_VIEW;
//                MainActivity.this.myViewUpdateHandler.sendMessage(m);
                Looper.loop();
            }
        }).start();
    }
    double width;
    double height;
    double ratio;

    Point a = new Point();
    Point b = new Point();
    Point c = new Point();
    Point d = new Point();
    Point e = new Point();
    Point f = new Point();
    Point g = new Point();
    Point h = new Point();
    Point i = new Point();
    Point one = new Point();
    Point two = new Point();
    Point three = new Point();
    Point four = new Point();
    double ab,bc,ca;


    public void getShape() {
        int left[] = {10000000,1000000};
        int right[] = {0,0};
        int top[] = {10000000,1000000};
        int bottom[] = {0,0};

        for (int i = 0; i < 16; i++) {

            if (points[i * 2] < left[0]) {
                left[0] = points[i * 2];
                left[1] = points[i * 2 + 1];
            }

            if (points[i * 2 + 1] < top[1]) {
                top[0] = points[i * 2];
                top[1] = points[i * 2 + 1];
            }

            if (points[i * 2 + 1] > bottom[1]) {
                bottom[0] = points[i * 2];
                bottom[1] = points[i * 2 + 1];
            }

            if (points[i * 2] > right[0]) {
                right[0] = points[i * 2];
                right[1] = points[i * 2 + 1];
            }
        }

        a.x =  points[10];
        a.y = points[11];
        b.x =  points[14];
        b.y = points[15];
        c.x =  points[12];
        c.y = points[13];
        d.x =  points[4];
        d.y = points[5];
        e.x =  points[6];
        e.y = points[7];
        f.x =  points[8];
        f.y = points[9];

        width = Math.sqrt((left[0] - right[0]) * (left[0] - right[0]) + (left[1] - right[1]) * (left[1] - right[1])) ;
        height = Math.sqrt((top[0] - bottom[0]) * (top[0] - bottom[0]) + (top[1] - bottom[1]) * (top[1] - bottom[1])) ;
        //myfile << "Width\t" << width  <<", height\t" << height << std::endl;
        ab = getDistance(d,f);
        bc = getDistance(f,e);
        ca = getDistance(e,d);
        double f = ((ca*ca) + (bc*bc) - (ab*ab))/(2*ca*bc);
        double ohAngle;
        ohAngle = (Math.acos(f) * 180) / 3.1416;

        ab = getDistance(a,b);
        bc = getDistance(b,c);
        ca = getDistance(c,a);
        double e = ((ca*ca) + (bc*bc) - (ab*ab))/(2*ca*bc);
        double jawAngle;
        jawAngle = (Math.acos(e) * 180) / 3.1416;

        one.x = points[0];
        one.x = points[1];
        two.x = points[10];
        two.x = points[11];
        three.x = points[14];
        three.x = points[15];
        four.x = points[24];
        four.x = points[25];
        // myfile << "Shape: ,";
        if(jawAngle > 159) {
            double cross1,cross2;
            double diagonal = Math.sqrt((height * height) + (width * width));
            cross1 = getDistance(one,three);
            cross2 = getDistance(two,four);
            double avgCross = (cross1+cross2)/2;
            double threshold = avgCross/diagonal;
            if(threshold <= .47) {
//                myfile << " Round,";
                Toast.makeText(getBaseContext(),"Round",Toast.LENGTH_LONG).show();
            }

            else if(threshold > .47){
//                myfile << " Square,";
                Toast.makeText(getBaseContext(),"Square",Toast.LENGTH_LONG).show();
            }

        } else {
            if(ohAngle <= 164){
//                myfile << " Oval,";
                Toast.makeText(getBaseContext(),"Oval",Toast.LENGTH_LONG).show();
            }

            else {
                //myfile << " Heart,";
                Toast.makeText(getBaseContext(),"Heart",Toast.LENGTH_LONG).show();
            }

        }
    }

    double getDistance(Point a, Point b)
    {
        double distance;
        distance = Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
        return distance;
    }
}
