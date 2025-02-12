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

public class AddBook extends AppCompatActivity {
    public String postRegister;


    public String postResult;
    TextView bookISBN;
    EditText bookTitle;
    EditText bookAuthor;
    EditText bookGenre;
    String res = "0000000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        postRegister = getResources().getString(R.string.url) + "/MobLib/add_book.php";
        bookTitle = findViewById(R.id.book_title);
        bookAuthor = findViewById(R.id.book_author);
        bookGenre = findViewById(R.id.book_genre);

        bookISBN = findViewById(R.id.book_isbn);
        Intent stock = getIntent();
        res = stock.getStringExtra(MenuStock.SCAN_RESULT);
        bookISBN.setText(res);
    }

    public void scan(View v) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.setOrientationLocked(false);
        scanIntegrator.initiateScan();
    }

    public void returnToMain(View v) {
        switch(v.getId()){
            case (R.id.cancel):
                finish();
                break;
            case (R.id.insert):
                String msg = "";
                if (bookISBN.toString().length() == 0) {
                    msg = "ERROR: book isbn is not set";
                } else if (bookTitle.getText().toString().length() == 0) {
                    msg = "ERROR: title is not set";
                } else if (bookAuthor.getText().toString().length() == 0) {
                    msg = "ERROR: author is not set";
                } else if (bookGenre.getText().toString().length() == 0) {
                    msg = "ERROR: genre is not set";
                } else {
                    try {
                        postAddBook(postRegister);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                exitMessage(msg, true);
                break;
            default:
                throw new RuntimeException("Unknown ID exception");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null && resultCode == RESULT_OK && scanningResult.getFormatName().equals("CODABAR")) {
            res = scanningResult.getContents();
            bookISBN.setText(scanningResult.getContents());
        } else {
            exitMessage("No scan data or invalid scan data received!(addbook)", true);
        }
    }

    public void postAddBook(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("isbn", bookISBN.getText().toString())
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
                exitMessage("failed to connect to webserver", true);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();

                AddBook.this.runOnUiThread(() -> {
                    try {
                        String msg = "Successfully added book into catalog";
                        JSONObject json = new JSONObject(myResponse);
                        String result = json.getString("message");
                        switch(result) {
                            case("Insertion successful"):
                                break;
                            case("Insertion failed"):
                                msg = "Book already listed onto database";
                                break;
                            default:
                                msg = "EMPTY STRING";
                                break;
                        }
                        postResult = msg;

                        exitMessage(msg, true);
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
