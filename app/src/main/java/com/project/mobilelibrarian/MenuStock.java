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
    public static Intent activty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_stock);
    }

    public void redirect(View v) {
        activty = new Intent(MenuStock.this, BookCatalog.class);
        startActivity(activty);
    }

    public void scan(View v) {
        switch(v.getId()) {
            case (R.id.add_book):
                activty = new Intent(MenuStock.this, AddBook.class);
                break;
            case (R.id.remove_book):
                activty = new Intent(MenuStock.this, RemoveBook.class);
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
        if (scanningResult != null && resultCode == RESULT_OK) {
            activty.putExtra(SCAN_RESULT, scanningResult.getContents());
            startActivity(activty);
            Log.d("SCAN RESULT", "scan successful");
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data or Improper scan data received! Try Again", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
