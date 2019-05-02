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

public class MenuCirc extends AppCompatActivity {

    public static final String SCAN_RESULT = "com.project.mobilelibrarian.MESSAGE";
    public static Intent activity;
    public String scanType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_circ);
    }

    public void redirect(View v) {
        activity = new Intent(MenuCirc.this, OrderCatalog.class);
        startActivity(activity);
    }

    public void logout(View v) {
        exitMessage("Logged out of account!", true);
    }

    public void scan(View v) {
        switch(v.getId()) {
            case (R.id.check_out):
                scanType = "ITF";
                activity = new Intent(MenuCirc.this, AddBook.class);
                break;
            case (R.id.check_in):
                scanType = "CODABAR";
                activity = new Intent(MenuCirc.this, RemoveBook.class);
                break;
            default:
                throw new RuntimeException("Unknown ID");

        }
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.setOrientationLocked(false);
        switch(scanType) {
            case ("ITF"):
                scanIntegrator.setPrompt("To begin, Scan a patron's student ID or faculty ID");
                break;
            case ("CODABAR"):
                scanIntegrator.setPrompt("To begin, Scan a book's campus ISBN located on the last page of the book");
                break;
            default:
                exitMessage("Error: unknown id found", false);
                break;
        }
        scanIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null && resultCode == RESULT_OK) {
            if(scanningResult.getFormatName().equals(scanType)) {
                activity.putExtra(SCAN_RESULT, scanningResult.getContents());
                startActivity(activity);
                Log.d("SCAN RESULT", "scan successful");
            } else {
                exitMessage("Invalid scan data recieved! Try again", false);
            }
        } else {
            exitMessage("No scan data scan data received! Try again", false);
        }
    }

    public void exitMessage(String msg, Boolean isFinished) {
        Toast exit = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        exit.show();
        if(isFinished) finish();
    }
}
