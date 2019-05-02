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

public class MenuStock extends AppCompatActivity {

    public static final String SCAN_RESULT = "com.project.mobilelibrarian.MESSAGE";
    public static Intent activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_stock);
    }

    public void redirect(View v) {
        activity = new Intent(MenuStock.this, BookCatalog.class);
        startActivity(activity);
    }

    public void logout(View v) {
        exitMessage("Logged out of account!", true);
    }

    public void scan(View v) {
        switch(v.getId()) {
            case (R.id.add_book):
                activity = new Intent(MenuStock.this, AddBook.class);
                break;
            case (R.id.remove_book):
                activity = new Intent(MenuStock.this, RemoveBook.class);
                break;
            default:
                throw new RuntimeException("Unknown ID");

        }
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.setOrientationLocked(false);
        scanIntegrator.setPrompt("To begin, Scan the Barcode located on the last page of the book");
        scanIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null && resultCode == RESULT_OK && scanningResult.getFormatName().equals("CODABAR")) {
            activity.putExtra(SCAN_RESULT, scanningResult.getContents());
            startActivity(activity);
            Log.d("SCAN RESULT", "scan successful");
        } else {
            exitMessage("No scan data or Improper scan data received! Try Again", false);
        }
    }

    public void exitMessage(String msg, Boolean isFinished) {
        Toast exit = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        exit.show();
        if(isFinished) finish();
    }
}
