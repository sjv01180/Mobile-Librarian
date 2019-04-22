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
    public static Intent scanReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scan(View v) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.setOrientationLocked(false);
        scanIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            switch(scanningResult.getFormatName())
            {
                case ("ITF"):
                    scanReciever = new Intent(MainActivity.this, CheckoutResult.class);
                    scanReciever.putExtra(SCAN_RESULT, scanContent);
                    startActivity(scanReciever);
                    break;
                case ("EAN_13"):
                    scanReciever = new Intent(MainActivity.this, CheckinResult.class);
                    scanReciever.putExtra(SCAN_RESULT, scanContent);
                    startActivity(scanReciever);
                    break;
                default:
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Unknown scan data! Try again", Toast.LENGTH_LONG);
                    toast.show();
                    break;
            }
            Log.d("SCAN RESULT", "scan successful");
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received! Try Again", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
