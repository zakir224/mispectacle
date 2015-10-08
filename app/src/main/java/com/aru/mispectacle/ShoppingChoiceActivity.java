package com.aru.mispectacle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class ShoppingChoiceActivity extends ActionBarActivity implements SpectacleFrameFragment.OnFragmentInteractionListener{

    public static final String SPECTACLE_FRAME_CHOOSER = "spectacleframechooser";
    SpectacleFrameFragment spectacleFrameFragment;

    public ShoppingChoiceActivity() {
        spectacleFrameFragment = new SpectacleFrameFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_choice);
    }

    public void shopByFace(View view) {
        Intent photoChooserIntent = new Intent(ShoppingChoiceActivity.this,PhotoSelectorActivity.class);
        startActivity(photoChooserIntent);
//        spectacleFrameFragment.show(getSupportFragmentManager(), SPECTACLE_FRAME_CHOOSER);
    }

    public void browseFrames(View view) {

    }

    @Override
    public void onFragmentInteraction(int id) {

    }
}
