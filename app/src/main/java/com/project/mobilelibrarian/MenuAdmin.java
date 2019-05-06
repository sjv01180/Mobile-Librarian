package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MenuAdmin extends AppCompatActivity {

    public static final String SCAN_RESULT = "com.project.mobilelibrarian.MESSAGE";
    public static Intent activity;
    public String scanType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);
    }

    public void logout(View v) {
        exitMessage("Logged out of account!", true);
    }

    public void scan(View v) {
        switch(v.getId()) {
            case (R.id.add_patron):
                activity = new Intent(MenuAdmin.this, AddPatron.class);
                break;
            default:
                throw new RuntimeException("Unknown ID");

        }
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.setOrientationLocked(false);
        scanIntegrator.setPrompt("To begin, Scan a patron's student ID or faculty ID");
        scanIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null && resultCode == RESULT_OK && scanningResult.getFormatName().equals("ITF")) {
            activity.putExtra(SCAN_RESULT, scanningResult.getContents());
            startActivity(activity);
        } else {
            exitMessage("No scan data or invalid scan data received! Try again", false);
        }
    }

    public void exitMessage(String msg, Boolean isFinished) {
        Toast exit = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        exit.show();
        if(isFinished) finish();
    }
}
