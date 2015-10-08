package com.aru.mispectacle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A placeholder fragment containing a simple view.
 */
public class PhotoSelectorActivityFragment extends DialogFragment implements View.OnClickListener {

    public static final int GALLERY_REQUEST_CODE = 1;
    private View view;
    private Button btnOpenGallery;
    private Button btnOpenCamera;
    private Context context;
    private OnPhotoSelectionFromGalleryListener onPhotoSelectionFromGalleryListener;
    private Uri uri;
    ImageView imageView;
            
    public PhotoSelectorActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photo_chooser, container, false);
        btnOpenCamera = (Button) view.findViewById(R.id.btnTakePhotoFromCamera);
        btnOpenGallery = (Button) view.findViewById(R.id.btnUploadFromGallery);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        context = getActivity();
        btnOpenCamera.setOnClickListener(this);
        btnOpenGallery.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        if(btnOpenCamera == v){
            Toast.makeText(context,"Opening Camera....",Toast.LENGTH_SHORT).show();
        } else if(btnOpenGallery == v) {
            Toast.makeText(context,"Opening Gallery....",Toast.LENGTH_SHORT).show();
            openGalleryForPhoto();
        }
    }

    private void openGalleryForPhoto() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"Select photo"), GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST_CODE &&
                resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            onPhotoSelectionFromGalleryListener.onPhotoSelect(uri);
            File dataDir = context.getDir("stasmdata", Context.MODE_PRIVATE);
            putDataFileInLocalDir(context,new File(dataDir,"face.jpg"),uri);
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
//                // Log.d(TAG, String.valueOf(bitmap));
//
//                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
//                imageView.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            File f=new File(dataDir,"face.jpg");
            try {
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                imageView.setImageBitmap(b);

                context.startActivity(new Intent(context,MainActivity.class));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public interface OnPhotoSelectionFromGalleryListener{
        public void onPhotoSelect(Uri uri);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onPhotoSelectionFromGalleryListener = (OnPhotoSelectionFromGalleryListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " needs to implement onPhotoSelectionFromGalleryListener");
        }
    }

    private void putDataFileInLocalDir(Context context, File f,Uri uri) {
        //if (debug) Log.e(TAG, "putDataFileInLocalDir: "+f.toString());
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            //InputStream is = context.getResources().openRawResource(id);
            FileOutputStream os = new FileOutputStream(f);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            Toast.makeText(context, "Image Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Image saving failed", Toast.LENGTH_SHORT).show();
        }
        //if (debug) Log.e(TAG, "putDataFileInLocalDir: done!");
    }
}
