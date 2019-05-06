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
        Intent fromMenuAdmin = getIntent();

        id = findViewById(R.id.patron_id);
        id.setText(fromMenuAdmin.getStringExtra(MenuAdmin.SCAN_RESULT));
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
                String msg;
                if(fname.getText().toString().length() == 0) {
                    msg = "ERROR: first name is not set";
                    exitMessage(msg, true);
                } else if(lname.getText().toString().length() == 0) {
                    msg = "ERROR: last name is not set";
                    exitMessage(msg, true);
                } else if(patronType.length() == 0){
                    msg = "ERROR: role type is not set";
                    exitMessage(msg, true);
                } else {
                    try {
                        postRequest(postUrl);
                        msg = postResult;
                        exitMessage(msg, false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        exitMessage("ERROR: IOException", false);
                    }
                }
                break;
            default:
                throw new RuntimeException("Unknown ID exception");
        }
    }

    public void scan(View v) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.setOrientationLocked(false);
        scanIntegrator.setPrompt("To begin, Scan a patron's student ID or faculty ID");
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null && resultCode == RESULT_OK && scanningResult.getFormatName().equals("ITF")) {
            String schoolID = scanningResult.getContents();
            id.setText(schoolID);
        } else {
            exitMessage("No scan data or invalid scan data received!", false);
        }
    }

    public void postRequest(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("id", id.getText().toString())
                .add("Fname", fname.getText().toString())
                .add("Lname", lname.getText().toString())
                .add("Ptype", patronType)
                .build();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                exitMessage("failed to connect to webserver", true);
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
                                msg = "ERROR: unknown patron type";
                                break;
                            case ("Insertion failed"):
                                msg = "ERROR: patron already listed in patron list";
                                break;
                            case ("Insertion successful"):
                                break;
                            default:
                                msg = "Unknown error: unidentified result message";
                                break;
                        }

                        postResult = msg;
                        exitMessage(msg, true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        exitMessage("ERROR: JSONException", true);
                    }
                });
            }
        });
    }

    public void exitMessage(String msg, Boolean isFinished) {
        Toast exit = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        exit.show();
        if(isFinished) finish();
    }
}