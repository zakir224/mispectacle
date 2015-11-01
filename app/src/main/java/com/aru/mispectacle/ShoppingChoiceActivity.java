package com.aru.mispectacle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class ShoppingChoiceActivity extends ActionBarActivity implements PhotoSelectorActivityFragment.OnPhotoSelectionFromGalleryListener{

    public static final String SPECTACLE_FRAME_CHOOSER = "spectacleframechooser";
    public static final String PHOTO_SAVED_FROM_GALLERY = "photo_saved_from_gallery";
    SpectacleFrameListFragment spectacleFrameListFragment;
    PhotoSelectorActivityFragment photoSelectorActivityFragment;

    public ShoppingChoiceActivity() {
        spectacleFrameListFragment = new SpectacleFrameListFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_choice);
        photoSelectorActivityFragment = new PhotoSelectorActivityFragment();
    }

    public void shopByFaceShape(View view) {
        photoSelectorActivityFragment.show(getSupportFragmentManager(),SPECTACLE_FRAME_CHOOSER);

    }

    public void browseFrames(View view) {

    }

    @Override
    public void onPhotoSelect(Uri uri) {
        photoSelectorActivityFragment.dismiss();
        Intent faceShapeIntent = new Intent(ShoppingChoiceActivity.this,MainActivity.class);
        faceShapeIntent.putExtra(PHOTO_SAVED_FROM_GALLERY,true);
        startActivity(faceShapeIntent);
    }
}
