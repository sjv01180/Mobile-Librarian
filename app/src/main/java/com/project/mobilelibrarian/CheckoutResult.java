package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

public class CheckoutResult extends AppCompatActivity {

    TextView result;
    String res = "If you see this, then it didn't work";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Intent scan = getIntent();
        result = findViewById(R.id.result);
        result.setText(scan.getStringExtra(MainActivity.SCAN_RESULT));
    }

    protected void scan(View v) {

    }

}
