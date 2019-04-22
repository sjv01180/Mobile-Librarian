package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    public static final String SCAN_RESULT = "com.project.mobilelibrarian.MESSAGE";
    public static Intent activty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void redirect(View v) {
        switch(v.getId()) {
            case (R.id.add_book):
                activty = new Intent(MainActivity.this, AddBook.class);
                break;
            case (R.id.order_history):
                activty = new Intent(MainActivity.this, BookCatalog.class);
                break;
            case (R.id.remove_book):
                activty = new Intent(MainActivity.this, RemoveBook.class);
                break;
            default:
                throw new RuntimeException("Unknown ID");

        }
        startActivity(activty);
    }

    public void scan(View v) {
        switch(v.getId()) {
            case (R.id.check_out):
                activty = new Intent(MainActivity.this, CheckoutResult.class);
                break;
            case (R.id.check_in):
                activty = new Intent(MainActivity.this, CheckinResult.class);
                break;
            default:
                throw new RuntimeException("Unknown ID");

        }
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.setOrientationLocked(false);
        scanIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            activty.putExtra(SCAN_RESULT, scanningResult.getContents());
            startActivity(activty);
            Log.d("SCAN RESULT", "scan successful");
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received! Try Again", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
