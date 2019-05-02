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
    public final String postBookSelect= "http://155.42.84.51/MobLib/get_book.php";
    public final String postBookRemove= "http://155.42.84.51/MobLib/remove_book.php";

    TextView isbn;
    TextView title;
    TextView author;
    TextView genre;
    TextView prompt;

    String res = "[book isbn]";
    String postTitle = "";
    String postAuthor = "";
    String postGenre = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_book);

        Intent stock = getIntent();
        prompt = findViewById(R.id.prompt);
        isbn = findViewById(R.id.book_isbn);
        title = findViewById(R.id.book_title);
        author = findViewById(R.id.book_author);
        genre = findViewById(R.id.book_genre);

        try {
            res = stock.getStringExtra(MenuStock.SCAN_RESULT);
            postRequest(postBookSelect);
        } catch(IOException e) {
            exitMessage("Failed to query barcode through catalog");
        }

    }

    public void returnToMain(View v) {
        switch(v.getId()){
            case (R.id.cancel):
                finish();
                break;
            case (R.id.insert):
                try {
                    postRemove(postBookRemove);
                    exitMessage("Successfully deleted book from catalog");
                } catch(IOException e) {
                    exitMessage("Failed to remove book from catalog");
                }
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
        if (scanningResult != null && resultCode == RESULT_OK && scanningResult.getFormatName().equals("CODABAR")) {
            try {
                res = scanningResult.getContents();
                postRequest(postBookSelect);

                prompt.setText("Are you sure you want to delete this book?");
            } catch(IOException e) {
                exitMessage("Failed to query barcode through catalog");
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Barcode error: Non-existent or invalid barcode detected!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void postRequest(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("isbn", res)
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
                exitMessage("failed to connect to webserver");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();

                RemoveBook.this.runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(myResponse);
                        String result = json.getString("message");
                        switch(result) {
                            case("query successful"):
                                res = json.getString("BookISBN");
                                postTitle = json.getString("Title");
                                postAuthor = json.getString("Author");
                                postGenre = json.getString("Genre");

                                if(res.length() == 0 || postTitle.length() == 0 || postAuthor.length() == 0 || postGenre.length() == 0) {
                                    exitMessage("unknown book detected or catalog is empty! Check the catalog in Book Catalog");
                                    break;
                                }

                                isbn.setText(res);
                                title.setText(postTitle);
                                author.setText(postAuthor);
                                genre.setText(postGenre);
                                break;
                            case("query failed"):
                                exitMessage("Failed to find book from database. Try adding that book into the database first.");
                                break;
                            default:
                                exitMessage("Error: unknown json message string detected");
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        exitMessage("JSON Error: cannot parse json");
                    }
                });
            }
        });
    }

    public void postRemove(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("isbn", res)
                .add("title", postTitle)
                .add("author", postAuthor)
                .add("genre", postGenre)
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
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();

                RemoveBook.this.runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(myResponse);
                        String result = json.getString("message");
                        Log.d("TAG", result);
                        switch(result) {
                            case ("Removal successful"):
                                break;
                            case("Removal failed"):
                                break;
                            default:
                                exitMessage("UNKNOWN SQL ERROR");
                                break;
                        }


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
