package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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

public class BookCatalog extends AppCompatActivity {
    public static final String postGetBooks = "http://155.42.84.51/MobLib/get_books.php";

    public static final String BOOK_ISBN = "com.project.mobilelibrarian.BookCatalog.BOOK_ISBN";
    public static final String BOOK_TITLE = "com.project.mobilelibrarian.BookCatalog.BOOK_TITLE";
    public static final String BOOK_AUTHOR = "com.project.mobilelibrarian.BookCatalog.BOOK_AUTHOR";
    public static final String BOOK_GENRE = "com.project.mobilelibrarian.BookCatalog.BOOK_GENRE";

    String postGenre = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        try {
            postRequest(postGetBooks);
        } catch (IOException e) {
            exitMessage("ERROR: cannot access catalog at the moment");
        }
    }

    public void postRequest(String postUrl) throws IOException {
        Request request = new Request.Builder()
                .url(postUrl)
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

                BookCatalog.this.runOnUiThread(() -> {
                    try {
                        JSONArray json = new JSONArray(myResponse);
                        //JSONArray resultArray = json.getJSONArray("result");
                        addTableData(json, json.length());

                    } catch (JSONException j) {
                        j.printStackTrace();
                        exitMessage("ERROR: failed to parse JSON from web server");
                    }
                });
            }
        });
    }

    private void addTableData(JSONArray resArray, int size) {
        float tableFont = getResources().getDimension(R.dimen.tableFont);
        int rightPadding = (int) (getResources().getDimension(R.dimen.padding_body) / getResources().getDisplayMetrics().density);
        //int rightPaddingTable = (int) (getResources().getDimension(R.dimen.padding_body) / getResources().getDisplayMetrics().density);

        TableLayout table = findViewById(R.id.table);
        table.removeAllViews();

        TableRow header = new TableRow(this);

        TextView labelISBN = setTextView("Book ISBN", tableFont, rightPadding);
        TextView labelTitle = setTextView("Title", tableFont, rightPadding);
        TextView labelAuthor = setTextView("OwnerID", tableFont, rightPadding);

        header.addView(labelISBN);
        header.addView(labelTitle);
        header.addView(labelAuthor);

        table.addView(header);

        for(int i = 0; i < size; i++){
            TableRow data = new TableRow(this);

            try {
                JSONObject resObj = resArray.getJSONObject(i);

                postGenre = resObj.getString("Genre");

                TextView dataISBN = setTextView(resObj.getString("BookISBN"), tableFont, rightPadding);
                TextView dataTitle = setTextView(resObj.getString("Title"), tableFont, rightPadding);
                TextView dataAuthor = setTextView(resObj.getString("Author"), tableFont, rightPadding);

                data.addView(dataISBN);
                data.addView(dataTitle);
                data.addView(dataAuthor);

                data.setOnClickListener(view -> {
                    Intent details = new Intent(BookCatalog.this, BookDetails.class);
                    details.putExtra(BOOK_ISBN, dataISBN.getText().toString());
                    details.putExtra(BOOK_TITLE, dataTitle.getText().toString());
                    details.putExtra(BOOK_AUTHOR, dataAuthor.getText().toString());
                    details.putExtra(BOOK_GENRE, postGenre);
                    startActivity(details);
                });
            } catch (JSONException j) {
                j.printStackTrace();
                exitMessage("ERROR: failed to parse JSON from web server");
            }

            table.addView(data);
        }
    }

    private TextView setTextView(String text, float font, int paddingRight) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(TypedValue.COMPLEX_UNIT_PX, font);
        t.setPadding(0, 0, paddingRight, 0);
        return t;
    }

    public void exitMessage(String msg) {
        Toast exit = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        exit.show();
        finish();
    }
}
