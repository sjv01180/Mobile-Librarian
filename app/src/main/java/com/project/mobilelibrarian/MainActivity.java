package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    public String postResult = "";

    public static Intent activity;

    public TextView user;
    public TextView pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
    }

    public void redirect(View v) {
        switch(v.getId()) {
            case (R.id.register):
                activity = new Intent(MainActivity.this, Register.class);
                startActivity(activity);
                break;
            case (R.id.login):
                try {
                    postRequest(postLogin);
                    String msg = postResult;
                    switch (msg) {
                        case "CIRC":
                            activity = new Intent(MainActivity.this, MenuCirc.class);
                            startActivity(activity);
                            break;
                        case "STOCK":
                            activity = new Intent(MainActivity.this, MenuStock.class);
                            startActivity(activity);
                            break;
                        default:
                            Toast error = Toast.makeText(getApplicationContext(),
                                    "ERROR: " + postResult, Toast.LENGTH_SHORT);
                            error.show();
                            break;
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new RuntimeException("Unknown ID");

        }
    }

    public void postRequest(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("name", user.getText().toString())
                .add("pass", md5(pass.getText().toString()))
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
                        postResult = json.getString("message");
                        Toast exit = Toast.makeText(getApplicationContext(),
                                "output: " + postResult, Toast.LENGTH_SHORT);
                        exit.show();
                        Log.d("TAG",postResult);

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
