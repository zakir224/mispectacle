package com.aru.mispectacle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import org.opencv.*;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;


public class TryOnActivity extends ActionBarActivity {

    Bitmap bitmapSpectacle;
    private ImageView userPhoto;
    private ImageView spectacle;
    private int mFaceWidth = 200;
    private int mFaceHeight = 200;
    private static final int MAX_FACES = 1;
    private static String TAG = "MI_SPECTACLE";
    private Bitmap mFaceBitmap;
    private static boolean DEBUG = true;
    LinkedHashMap<Integer, Bitmap> imageBitmaps;
//    ImageView mImageView;

    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    private ImageView mImageView;
    private File mCascadeFile;
    private CascadeClassifier faceCascadeClassifier;
    private CascadeClassifier eyeCascadeClassifier;
    Bitmap bmp;
//    private Bitmap mImageBitmap;
    public int glasswidth;
    public int glassheight;
    private Mat mRgba;
    private Mat mGray;
    private VideoView mVideoView;
    private Uri mVideoUri;

    private String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d("ERROR", "Unable to load OpenCV");
        }
    }

    public BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OPENCV", "OpenCV loaded successfully");
                    // Load native library after(!) OpenCV initialization
//                    System.loadLibrary("detection_based_tracker");

                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
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


                        is = getResources().openRawResource(R.raw.haarcascade_eye_tree_eyeglasses);
                        cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
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
                    //mOpenCvCameraView.enableView();
//                    Toast.makeText(getApplication(),'d',Toast.LENGTH_LONG).show();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                    Log.e(TAG, "Failed to load Opencv: ");
                } break;
            }
        }
    };

    private String getAlbumName() {
        return getString(R.string.album_name);
    }
    Mat m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_on);
        mImageView = (ImageView) findViewById(R.id.userPhoto);

        Log.e("Tryon", "width" + mFaceWidth + "height " + mFaceHeight);
        //mImageView.setImageBitmap(mFaceBitmap);
        //mFaceBitmap.recycle();

        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFace();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
        //mFaceBitmap.recycle();
    }

    public float getAngle(Point source,Point target) {
        Point p;
        if(target.x > source.x){
            p = target;
            target = source;
            source = p;
        }

        float angle = (float) Math.toDegrees(Math.atan2(target.y - source.y, target.x - source.x));
        Log.i(TAG,"Angle = "+angle);
        if(angle < 0){
            angle += 360;
            angle = angle % 90;
        }
        if(angle>90){
            angle = angle % 90;
            if(angle > 0)
                angle =  angle-90;
        }


        Log.i(TAG,"Angle = "+angle);
        return angle;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mFaceBitmap);
        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mFaceBitmap != null) );
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mFaceBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        mImageView.setImageBitmap(mFaceBitmap);
        mImageView.setVisibility(
                savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );

    }

    private Bitmap overlayMark(Bitmap bmp1, Bitmap bmp2, int top, int left) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, 0, 0, null);
        canvas.drawBitmap(bmp2, left, top, null);
        return bmOverlay;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_try_on, menu);


        return true;
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File f = null;

        try {
            f = setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            mCurrentPhotoPath = null;
        }


        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            try {
                if (storageDir.mkdir()) {
                    System.out.println("Directory created");
                } else {
                    System.out.println("Directory is not created");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {
                if (mCurrentPhotoPath != null) {
                    setPic();
                    galleryAddPic();
                    mCurrentPhotoPath = null;
                }
            }

        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        //Bitmap b = Bitmap.createScaledBitmap(bitmap,600, 400, true);//scaleImage(imageBitmaps.get(mBitmap));
        Bitmap b = bitmap.copy(Bitmap.Config.RGB_565, true);
        mImageView.setImageBitmap(b);
        mFaceBitmap = b;
        mFaceWidth = mFaceBitmap.getWidth();
        mFaceHeight = mFaceBitmap.getHeight();
        mImageView.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            dispatchTakePictureIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setFace() {
        FaceDetector fd;
        FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACES];
        PointF eyescenter = new PointF();
        float eyesdist = 0.0f;
        int[] fpx = null;
        int[] fpy = null;
        int count = 0;

        try {
            fd = new FaceDetector(mFaceWidth, mFaceHeight, MAX_FACES);
            count = fd.findFaces(mFaceBitmap, faces);
            Log.e(TAG, "NUMBER OF FACES " +count);
        } catch (Exception e) {
            Log.e(TAG, "setFace(): " + e.toString());
            return;
        }

        // check if we detect any faces
        if (count > 0) {
            fpx = new int[count * 2];
            fpy = new int[count * 2];

            for (int i = 0; i < count; i++) {
                try {
                    faces[i].getMidPoint(eyescenter);
                    eyesdist = faces[i].eyesDistance();

                    // set up left eye location
                    fpx[2 * i] = (int) (eyescenter.x - eyesdist / 2);
                    fpy[2 * i] = (int) eyescenter.y;

                    // set up right eye location
                    fpx[2 * i + 1] = (int) (eyescenter.x + eyesdist / 2);
                    fpy[2 * i + 1] = (int) eyescenter.y;

                    if (DEBUG)
                        Log.e(TAG, "setFace(): face " + i + ": confidence = " + faces[i].confidence()
                                + ", eyes distance = " + faces[i].eyesDistance()
                                + ", pose = (" + faces[i].pose(FaceDetector.Face.EULER_X) + ","
                                + faces[i].pose(FaceDetector.Face.EULER_Y) + ","
                                + faces[i].pose(FaceDetector.Face.EULER_Z) + ")"
                                + ", eyes midpoint = (" + eyescenter.x + "," + eyescenter.y + ")");
                } catch (Exception e) {
                    Log.e(TAG, "setFace(): face " + i + ": " + e.toString());
                }
            }
        }


        Bitmap v = BitmapFactory.decodeResource(getResources(), R.drawable.sp1);
        Bitmap b = Bitmap.createScaledBitmap(v, ((int) eyesdist * 2) + (int) eyesdist / 6, (int) eyesdist / 2 + 10, true);

        mImageView.setImageBitmap(overlayMark(mFaceBitmap, b, (int) eyescenter.y - 20, fpx[0] - (int) eyesdist / 2 - (int) eyesdist / 13));
//        Log.d("coords","Eye distance = "+eyesdist+" width = "+(int)eyesdist * 2+" image width =" + spectacle.getLayoutParams().width);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        //bitmapSpectacle.recycle();
    }

    private Bitmap scaleImage(Bitmap bitmap) {

        // Get current dimensions AND the desired bounding box
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = dpToPx(250);
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use


        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use

        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        Log.i("Test", "done");

        return scaledBitmap;
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
