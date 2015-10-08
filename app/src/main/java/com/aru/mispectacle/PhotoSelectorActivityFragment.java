package com.aru.mispectacle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
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
 *
 */
public class PhotoSelectorActivityFragment extends DialogFragment implements View.OnClickListener {

    public static final int GALLERY_REQUEST_CODE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private View view;
    private Button btnOpenGallery;
    private Button btnOpenCamera;
    private Button btnUseExistingImage;
    private Context context;
    private OnPhotoSelectionFromGalleryListener onPhotoSelectionFromGalleryListener;
    private Uri uri;
    private ImageView imageView;
    private File dataDir;

    public PhotoSelectorActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photo_chooser, container, false);
        btnOpenCamera = (Button) view.findViewById(R.id.btnTakePhotoFromCamera);
        btnOpenGallery = (Button) view.findViewById(R.id.btnUploadFromGallery);
        btnUseExistingImage = (Button) view.findViewById(R.id.btnUseExistingImage);
        checkForExistingImage();
        getDialog().setTitle("Select Photo");
        //imageView = (ImageView) view.findViewById(R.id.imageView);
        context = getActivity();
        btnOpenCamera.setOnClickListener(this);
        btnOpenGallery.setOnClickListener(this);
        return view;
    }

    private void checkForExistingImage() {

    }

    @Override
    public void onClick(View v) {

        if (btnOpenCamera == v) {
            Toast.makeText(context, "Opening Camera....", Toast.LENGTH_SHORT).show();
            openCameraForPhoto();
        } else if (btnOpenGallery == v) {
            Toast.makeText(context, "Opening Gallery....", Toast.LENGTH_SHORT).show();
            openGalleryForPhoto();
        }
    }

    private void openCameraForPhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void openGalleryForPhoto() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select photo"), GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int a;

        if (resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            if (requestCode == GALLERY_REQUEST_CODE || requestCode == REQUEST_IMAGE_CAPTURE) {
                uri = data.getData();
                dataDir = context.getDir("stasmdata", Context.MODE_PRIVATE);
                putImageFileInLocalDir(context, new File(dataDir, "face.jpg"), uri);
            }
        }
        onPhotoSelectionFromGalleryListener.onPhotoSelect(uri);
    }

    public interface OnPhotoSelectionFromGalleryListener {
            void onPhotoSelect(Uri uri);
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

    private void putImageFileInLocalDir(Context context, File f, Uri uri) {
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
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
    }
}
