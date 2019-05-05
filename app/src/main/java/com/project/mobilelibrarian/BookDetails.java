package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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

public class BookDetails extends AppCompatActivity {
    public static final String postGetLastDate = "http://155.42.84.51/MobLib/get_last_checkout.php";

    TextView bookISBN;
    TextView title;
    TextView author;
    TextView genre;
    TextView coDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Intent bookRow = getIntent();
        bookISBN = findViewById(R.id.book_isbn);
        bookISBN.setText(bookRow.getStringExtra(BookCatalog.BOOK_ISBN));

        title = findViewById(R.id.book_title);
        title.setText(bookRow.getStringExtra(BookCatalog.BOOK_TITLE));

        author = findViewById(R.id.book_author);
        author.setText(bookRow.getStringExtra(BookCatalog.BOOK_AUTHOR));

        genre = findViewById(R.id.book_genre);
        genre.setText(bookRow.getStringExtra(BookCatalog.BOOK_GENRE));

        try{
            postGrabDate(postGetLastDate);
        } catch (IOException e){
            e.printStackTrace();
            exitMessage("ERROR: IOException");
        }


    }

    public void postGrabDate(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("isbn", bookISBN.getText().toString())
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
                exitMessage("failed to connect to web server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();

                BookDetails.this.runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(myResponse);
                        Log.d("TAG", "res: " + json.toString());
                        coDate = findViewById(R.id.book_co_date);
                        String date = json.getString("CO_Date");
                        coDate.setText(formatDate(date));

                    } catch (JSONException j) {
                        coDate = findViewById(R.id.book_co_date);
                        coDate.setText("No C/O Date");
                    }
                });
            }
        });
    }

    public String formatDate(String sqlDate) {
        DateTime proxyDate = new DateTime(sqlDate);
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");

        return dateFormatter.print(proxyDate);
    }

    public void exitMessage(String msg) {
        Toast exit = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        exit.show();
        finish();
    }

}
