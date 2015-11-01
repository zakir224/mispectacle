package com.aru.mispectacle;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.aru.mispectacle.SpectacleFrameListFragment.OnSpectacleSelectedListener;
import com.aru.mispectacle.db.SpectacleDataSource;


public class SplashActivity extends ActionBarActivity implements OnSpectacleSelectedListener{

    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv = ((ImageView)findViewById(R.id.result));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SpectacleDataSource spectacleDataSource = new SpectacleDataSource(getBaseContext());
                spectacleDataSource.open();
                startActivity(new Intent(SplashActivity.this, SpectacleListActivity.class));

            }
        },1000);

    }

    @Override
    public void onSpectacleSelected(String id) {

    }
}
