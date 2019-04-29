package com.project.mobilelibrarian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {

    public final String postRegister = "http://155.42.84.51/MobLib/add_user.php";
    public String postResult = "";

    EditText user;
    EditText pass;
    EditText retypePass;
    public String role = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        retypePass = findViewById(R.id.retype_password);
    }

    public void setRole(View v) {
        boolean selected = ((RadioButton) v).isChecked();

        switch(v.getId()) {
            case (R.id.radio_circ):
                if(selected) role = "CIRC";
                break;
            case (R.id.radio_stock):
                if(selected) role = "STOCK";
                break;
            default:
                throw new RuntimeException("Unknown ID exception");
        }
    }

    public void returnToMain(View v) {
        switch(v.getId()) {
            case (R.id.cancel):
                finish();
                break;
            case (R.id.register):
                String msg = "Successfully added a user to database: " + user.getText().toString();
                if (user.getText().toString().length() == 0) {
                    msg = "ERROR: username is not set";
                } else if (pass.getText().toString().length() == 0) {
                    msg = "ERROR: password is not set";
                } else if (!retypePass.getText().toString().equals(pass.getText().toString())) {
                    msg = "ERROR: password does not match retyped password";
                } else if (role.length() == 0) {
                    msg = "ERROR: role isn't set";
                } else {
                    try {
                        postRequest(postRegister);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Toast exit = Toast.makeText(getApplicationContext(),
                        msg, Toast.LENGTH_SHORT);
                exit.show();
                break;
            default:
                throw new RuntimeException("Unknown ID exception");
        }
    }

    public void postRequest(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("name", user.getText().toString())
                .add("pass", md5(pass.getText().toString()))
                .add("role", role)
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

                Register.this.runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(myResponse);
                        String result = json.getString("message");
                        postResult = result;
                        Toast exit = Toast.makeText(getApplicationContext(),
                                "output: " + postResult, Toast.LENGTH_SHORT);
                        exit.show();
                        Log.d("TAG", postResult);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
