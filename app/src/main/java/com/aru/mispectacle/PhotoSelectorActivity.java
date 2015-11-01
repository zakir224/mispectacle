package com.aru.mispectacle;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PhotoSelectorActivity extends ActionBarActivity implements PhotoSelectorActivityFragment.OnPhotoSelectionFromGalleryListener{

    private File dataDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_chooser);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PhotoSelectorActivityFragment())
                    .commit();
        }
    }

//    @Override
    public void onPhotoSelect(Uri uri) {

        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
//        dataDir = getBaseContext().getDir("stasmdata", Context.MODE_PRIVATE);
//        putDataFileInLocalDir(getBaseContext(),new File(dataDir,"face.jpg"),uri);

//        ImageView imageView = (ImageView) findViewById(R.id.image);
//        File filePath = getFileStreamPath(fileName);
//        imageView.setImageDrawable(Drawable.createFromPath(filePath.toString()));
    }

    private void putDataFileInLocalDir(Context context, File f,Uri uri) {
        //if (debug) Log.e(TAG, "putDataFileInLocalDir: "+f.toString());
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            //InputStream is = context.getResources().openRawResource(id);
            FileOutputStream os = new FileOutputStream(f);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            Toast.makeText(getBaseContext(), "Image Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Image saving failed", Toast.LENGTH_SHORT).show();
        }
        //if (debug) Log.e(TAG, "putDataFileInLocalDir: done!");
    }

//    @Override
//    public void onFragmentInteraction(int id) {
//        Toast.makeText(getBaseContext(), id+ " Clicked", Toast.LENGTH_SHORT).show();
//    }
}
