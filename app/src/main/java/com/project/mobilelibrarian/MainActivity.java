package com.project.mobilelibrarian;

import android.content.Intent;
import android.content.res.Resources;
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
    public String postLogin;
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
        postLogin = getString(R.string.url) + "/MobLib/user_login.php";
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
                if(user.getText().toString().length() == 0) {
                    msg = "Error: username field is empty";
                }

                else if (pass.getText().toString().length() == 0) {
                    msg = "Error: password field is empty";
                }
                else {
                    try {
                        postLogin(postLogin);
                        msg = postResult;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                exitMessage(msg,false);
                break;
            default:
                throw new RuntimeException("Unknown ID");

        }
    }

    public void postLogin(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("name", user.getText().toString())
                .add("pass", pass.getText().toString())
                .build();

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
                            case("ADMIN"):
                                activity = new Intent(MainActivity.this, MenuAdmin.class);
                                startActivity(activity);
                                break;
                            default:
                                postResult = "ERROR: incorrect user or password";
                                break;
                        }
                        exitMessage(postResult, false);

                    } catch (JSONException e) {
                        e.printStackTrace();
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
