package com.aru.mispectacle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;

import java.util.LinkedHashMap;
import java.util.Map;


public class DefaultPhotosActivity extends ActionBarActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_photos);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_default_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple mImageView.
     */
    public static class PlaceholderFragment extends Fragment {

        LinkedHashMap<ImageView, Integer> imageViews;

        public PlaceholderFragment() {
            imageViews = new LinkedHashMap<>();






        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_default_photos, container, false);

            imageViews.put((ImageView) rootView.findViewById(R.id.def_img_1),1);
            imageViews.put((ImageView) rootView.findViewById(R.id.def_img_2),2);
            imageViews.put((ImageView) rootView.findViewById(R.id.def_img_3),3);
            imageViews.put((ImageView) rootView.findViewById(R.id.def_img_4),4);

            for(final Map.Entry<ImageView,Integer> entry : imageViews.entrySet()) {
                entry.getKey().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Intent mIntent = new Intent(getActivity(), TryOnActivity.class);
                        mIntent.putExtra("bmp_img", entry.getValue());
                        startActivity(mIntent);

                        return true;
                    }
                });
            }
            return rootView;
        }
    }
}
