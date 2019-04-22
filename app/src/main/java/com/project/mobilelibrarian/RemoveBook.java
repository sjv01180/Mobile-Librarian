package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class RemoveBook extends AppCompatActivity {

    TextView isbn;
    TextView prompt;
    String res = "[book isbn]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_book);
        isbn = findViewById(R.id.book_isbn);
        prompt = findViewById(R.id.prompt);
        isbn.setText(res);
    }

    public void returnToMain(View v) {
        switch(v.getId()){
            case (R.id.cancel):
                finish();
                break;
            case (R.id.insert):
                Toast exit = Toast.makeText(getApplicationContext(),
                        "Successfully deleted book from catalog", Toast.LENGTH_SHORT);
                exit.show();
                finish();
                break;
            default:
                throw new RuntimeException("Unknown ID exception");
        }
    }

    public void scan(View v) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.setOrientationLocked(false);
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            res = scanningResult.getContents();
            isbn.setText(res);
            prompt.setText("Are you sure you want to delete this book?");
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received! ScanType = " + scanningResult.getFormatName(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
