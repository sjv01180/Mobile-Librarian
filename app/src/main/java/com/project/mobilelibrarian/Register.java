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

    public String postRegister;
    String postResult;

    EditText user;
    EditText pass;
    EditText retypePass;
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        postRegister = getString(R.string.url) + "/MobLib/add_user.php";
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
                String msg = "";
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
                        msg = postResult;
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
                .add("pass", pass.getText().toString())
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
                        String msg = "Successfully created a user";
                        String result = json.getString("message");
                        Log.d("TAG", "post result = " + result + ", role: " + role);
                        switch(result) {
                            case("Insertion successful"):
                                break;
                            case("Insertion failed"):
                                msg = "User already exists or SQL error occurred";
                                break;
                            default:
                                msg = "EMPTY STRING";
                                break;
                        }
                        postResult = msg;
                        Toast exitPost = Toast.makeText(getApplicationContext(),
                                msg, Toast.LENGTH_SHORT);
                        exitPost.show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}
