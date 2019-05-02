package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddPatron extends AppCompatActivity {
    public final String postUrl= "http://155.42.84.51/MobLib/add_patron.php";
    public String postResult = "";

    String patronType = "";
    TextView id;
    TextView fname;
    TextView lname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patron);
        id = findViewById(R.id.id);
        fname = findViewById(R.id.f_name);
        lname = findViewById(R.id.l_name);
    }

    public void setRole(View v) {
        boolean selected = ((RadioButton) v).isChecked();

        switch(v.getId()) {
            case (R.id.radio_student):
                if(selected) patronType = "STUDENT";
                break;
            case (R.id.radio_faculty):
                if(selected) patronType = "FACULTY";
                break;
            default:
                throw new RuntimeException("Unknown ID exception");
        }
    }

    public void returnToMain(View v) {
        switch(v.getId()){
            case (R.id.cancel):
                finish();
                break;
            case (R.id.add_user):
                String msg = "Successfully added a patron: " + fname.getText().toString() + " " + lname.getText().toString();

                if(fname.getText().toString().length() == 0){
                    msg = "ERROR: first name is not set";
                }

                else if(lname.getText().toString().length() == 0){
                    msg = "ERROR: last name is not set";
                }

                else if(patronType.length() == 0){
                    msg = "ERROR: role type is not set";
                }

                else {
                    try {
                        postRequest(postUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
                msg = postResult;
                Toast exit = Toast.makeText(getApplicationContext(),
                        msg, Toast.LENGTH_SHORT);
                exit.show();
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
        if (scanningResult != null ) {
            if(scanningResult.getFormatName().equals("ITF")) {
                String schoolID = scanningResult.getContents();
                id.setText(schoolID);
            }

        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received! ScanType = " + scanningResult.getFormatName(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void postRequest(String postUrl) throws IOException {

        RequestBody formBody = new FormBody.Builder()
                .add("id", id.getText().toString())
                .add("Fname", fname.getText().toString())
                .add("Lname", lname.getText().toString())
                .add("Ptype", patronType)
                .build();

        //RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
                .url(postUrl)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                postResult = "failed to connect to webserver";
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();

                AddPatron.this.runOnUiThread(() -> {
                    String msg = "Successfully inserted patron into database";
                    try {
                        JSONObject json = new JSONObject(myResponse);
                        String result = json.getString("message");
                        switch (result) {
                            case ("UNKNOWN PATRON TYPE"):
                                msg = "insertion failed: unknown patron type";
                                break;
                            case ("Insertion failed"):
                                msg = "insertion failed: sql statement error (user already inserted)";
                                break;
                            case ("Insertion successful"):
                                break;
                            default:
                                msg = "Unknown error: unidentified result message";
                                break;
                        }

                        postResult = msg;
                        Toast exit = Toast.makeText(getApplicationContext(),
                                postResult, Toast.LENGTH_SHORT);
                        exit.show();
                        Log.d("TAG",response.body().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public void exitMessage(String msg) {
        Toast exit = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        exit.show();
        finish();
    }
}