package com.aru.mispectacle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Toast;

import com.aru.mispectacle.exception.FaceNotDetectedException;

import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;


public class MainActivity extends ActionBarActivity implements DefaultPhotosFragment.OnFragmentInteractionListener {

    private final String TAG = "MainActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";

    private ImageView userImage;
    private Bitmap bmp;
    private Button btnChoosePhoto;
    private Mat m;
    DefaultPhotosFragment defaultPhotosFragment;
    Button tryOn;
    Button selfie;
    FaceDetectionHelper faceDetectionHelper;
    private DirectoryHelper directoryHelper;

    private Bitmap mFaceBitmap;
    private Bitmap spectacleBitmap;
    private Bitmap resultBitmap;


    private Bitmap getMFaceBitmap() {
        return mFaceBitmap;
    }


    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d("ERROR", "Unable to load OpenCV");
        }
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
        tryOn = (Button) findViewById(R.id.buttonTryOn);
        selfie = (Button) findViewById(R.id.buttonSelfie);
        btnChoosePhoto = ((Button) findViewById(R.id.buttonChoosePhoto));
        userImage = ((ImageView) findViewById(R.id.result));
        setMFaceBitmap(3);
        defaultPhotosFragment = new DefaultPhotosFragment();
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
                    setUserBitmap(mFaceBitmap);
                    directoryHelper.galleryAddPic();
//                    mCurrentPhotoPath = null;
                    directoryHelper.setmCurrentPhotoPath(null);
                    Log.d(TAG, "OnActivity result Finished");
                }
            }

        }
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
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_1));
                setUserBitmap(mFaceBitmap);
                break;
            case 2:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_2));
                setUserBitmap(mFaceBitmap);
                break;
            case 3:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_3));
                setUserBitmap(mFaceBitmap);
                break;
            case 4:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_4));
                setUserBitmap(mFaceBitmap);
                break;
            case 5:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_5));
                setUserBitmap(mFaceBitmap);
                break;
            case 6:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_6));
                setUserBitmap(mFaceBitmap);
                break;
            case 7:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_7));
                setUserBitmap(mFaceBitmap);
                break;
            case 8:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_8));
                setUserBitmap(mFaceBitmap);
                break;
            case 9:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_9));
                setUserBitmap(mFaceBitmap);
                break;
            case 10:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_10));
                setUserBitmap(mFaceBitmap);
                break;
            case 11:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_11));
                setUserBitmap(mFaceBitmap);
                break;
            default:
                mFaceBitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.people_1));
                setUserBitmap(mFaceBitmap);
                break;
        }
    }

}
