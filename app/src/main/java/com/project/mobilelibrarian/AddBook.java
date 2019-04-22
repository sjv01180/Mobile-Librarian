package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AddBook extends AppCompatActivity {

    TextView studentID;
    TextView isbn;
    String res = "0000000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
    }

    public void scan(View v) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.setOrientationLocked(false);
        scanIntegrator.initiateScan();
    }

    public void returnToMain(View v) {
        switch(v.getId()){
            case (R.id.cancel):
                finish();
                break;
            case (R.id.insert):
                Toast exit = Toast.makeText(getApplicationContext(),
                        "Successfully added book to database", Toast.LENGTH_SHORT);
                exit.show();
                finish();
                break;
            default:
                throw new RuntimeException("Unknown ID exception");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            switch(scanningResult.getFormatName()) {
                case "ITF":
                    studentID = findViewById(R.id.student_id);
                    studentID.setText(scanningResult.getContents());
                    break;
                case "EAN_13":
                    isbn = findViewById(R.id.book_isbn);
                    isbn.setText(scanningResult.getContents());
                    break;
                default:
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Unknown scan data received! Try again.", Toast.LENGTH_SHORT);
                    toast.show();
                    break;

            }
            res = scanningResult.getContents();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received! ScanType = " + scanningResult.getFormatName(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
