package com.aru.mispectacle;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegistrationActivity extends ActionBarActivity {

    public static final String REGISTER_USER_NAME = "user_name";
    public static final String REGISTER_USER_GENDER = "user_name";
    public static final String REGISTER_USER_PHOTO = "user_name";
    private EditText etRegisterUserName;
    private Spinner spinnerRegisterGender;
    private Button btnRegisterUploadPhoto;
    private Button btnRegisterSubmit;
    SharedPreferences sharedPreferencesRegister;
    String strRegisterName;
    boolean isPhotoUploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getUiReferences();
        setupListeners();
    }

    private void setupListeners() {
        btnRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitRegister();
            }
        });
    }

    private void commitRegister() {
        if(inputValid()){
            updateSharedPreference();
        }
    }

    private void updateSharedPreference() {
        SharedPreferences.Editor editor = sharedPreferencesRegister.edit();
        editor.putString(REGISTER_USER_NAME,strRegisterName);
    }

    private boolean inputValid() {
        strRegisterName = etRegisterUserName.getText().toString();

        if(strRegisterName.isEmpty())
            return false;
        if (!isPhotoUploaded)
            return false;

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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

    public void getUiReferences() {
        etRegisterUserName = (EditText)findViewById(R.id.et_register_name);
        spinnerRegisterGender = (Spinner)findViewById(R.id.spinner_register_gender);
        btnRegisterUploadPhoto = (Button)findViewById(R.id.btn_register_upload_pp);
        btnRegisterSubmit = (Button)findViewById(R.id.btn_register_submit);
        sharedPreferencesRegister = getPreferences(Context.MODE_PRIVATE);
    }
}
