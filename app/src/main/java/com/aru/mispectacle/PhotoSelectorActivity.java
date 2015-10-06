package com.aru.mispectacle;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public class PhotoSelectorActivity extends ActionBarActivity implements PhotoSelectorActivityFragment.OnPhotoSelectionFromGalleryListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_chooser);
    }

    @Override
    public void onPhotoSelect(Uri uri) {

        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
    }
}
