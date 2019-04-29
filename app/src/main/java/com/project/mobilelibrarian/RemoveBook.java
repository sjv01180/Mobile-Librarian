package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class RemoveBook extends AppCompatActivity {

    public final String postQuery= "http://155.42.84.51/MobLib/get_book.php";
    public final String postRemove= "http://155.42.84.51/MobLib/remove_book.php";

    TextView isbn;
    TextView prompt;

    TextView bookISBN;
    EditText bookTitle;
    EditText bookAuthor;
    EditText bookGenre;
    String res = "[book isbn]";

    public String postResult;

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
        if (scanningResult != null && scanningResult.getFormatName().equals("CODABAR")) {
            res = scanningResult.getContents();
            isbn.setText(res);
            prompt.setText("Are you sure you want to delete this book?");
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received! ScanType = " + scanningResult.getFormatName(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void postRequest(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("", bookISBN.getText().toString())
                .add("title", bookTitle.getText().toString())
                .add("author", bookAuthor.getText().toString())
                .add("genre", bookGenre.getText().toString())
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

                RemoveBook.this.runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(myResponse);
                        postResult = json.getString("message");
                        Log.d("TAG",response.body().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}
