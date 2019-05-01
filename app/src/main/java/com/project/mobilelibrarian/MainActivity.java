package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {
    public final String postLogin= "http://155.42.84.51/MobLib/user_login.php";
    String postResult = "";

    public static Intent activity;

    public EditText user;
    public EditText pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
    }

    public void clearInput(View v) {
        user.setText("");
        pass.setText("");
    }

    public void redirect(View v) {
        switch(v.getId()) {
            case (R.id.register):
                activity = new Intent(MainActivity.this, Register.class);
                startActivity(activity);
                break;
            case (R.id.login):
                String msg = "";
                Toast exit;
                if(user.getText().toString().length() == 0) {
                    msg = "Error: username field is empty";
                }

                else if (pass.getText().toString().length() == 0) {
                    msg = "Error: password field is empty";
                }
                else {
                    try {
                        postRequest(postLogin);
                        msg = postResult;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(activity == null) {
                    exit = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                    exit.show();
                }
                break;
            default:
                throw new RuntimeException("Unknown ID");

        }
    }

    public void postRequest(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("name", user.getText().toString())
                .add("pass", pass.getText().toString())
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

                MainActivity.this.runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(myResponse);
                        String result = json.getString("message");
                        postResult = String.format("Login successful. Welcome %s!", user.getText().toString());
                        clearInput(null);
                        switch(result) {
                            case ("CIRC"):
                                activity = new Intent(MainActivity.this, MenuCirc.class);
                                startActivity(activity);
                                break;
                            case("STOCK"):
                                activity = new Intent(MainActivity.this, MenuStock.class);
                                startActivity(activity);
                                break;
                            default:
                                postResult = "ERROR: incorrect user or password";
                                break;
                        }
                        Toast exitPost = Toast.makeText(getApplicationContext(),
                                postResult, Toast.LENGTH_SHORT);
                        exitPost.show();

                        Log.d("TAG", "result: " + postResult);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}
