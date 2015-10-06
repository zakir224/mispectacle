package com.aru.mispectacle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class PhotoSelectorActivityFragment extends Fragment implements View.OnClickListener {

    public static final int GALLERY_REQUEST_CODE = 1;
    private View view;
    private Button btnOpenGallery;
    private Button btnOpenCamera;
    private Context context;
    private OnPhotoSelectionFromGalleryListener onPhotoSelectionFromGalleryListener;
    private Uri uri;
            
    public PhotoSelectorActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photo_chooser, container, false);
        btnOpenCamera = (Button) view.findViewById(R.id.btnTakePhotoFromCamera);
        btnOpenGallery = (Button) view.findViewById(R.id.btnUploadFromGallery);
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
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
//                // Log.d(TAG, String.valueOf(bitmap));
//
//                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
//                imageView.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
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
}
