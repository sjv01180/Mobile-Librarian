package com.project.mobilelibrarian;

import android.content.Intent;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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

public class CheckinResult extends AppCompatActivity {
    public String postGetCheck;
    public String postGetReserve;
    public String postUpdateChecks;
    public String postUpdateReserves;
    public String postFindBook;
    String isbn;
    String patronRole;


    TextView bookISBN;
    TextView title;
    TextView patronID;
    TextView author;
    TextView patronName;
    TextView role;
    TextView genre;
    TextView coDate;
    TextView ciDate;
    TextView dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        postGetCheck = getString(R.string.url) + "/MobLib/get_check.php";
        postGetReserve = getString(R.string.url) + "/MobLib/get_reserve.php";
        postUpdateChecks = getString(R.string.url) + "/MobLib/update_checks.php";
        postUpdateReserves = getString(R.string.url) + "/MobLib/update_reserves.php";
        postFindBook = getString(R.string.url) + "/MobLib/find_book.php";

        try {
            Intent fromMain = getIntent();
            isbn = fromMain.getStringExtra(MenuCirc.SCAN_RESULT);
            postOrder(postFindBook);
        } catch (IOException e) {
            exitMessage("ERROR: IOException", true);
        }

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                finish();
            }
        }.start();
    }

    public void postOrder(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("isbn", isbn)
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
                exitMessage("failed to connect to web server", true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();

                CheckinResult.this.runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(myResponse);
                        String result = json.getString("message");
                        switch(result) {
                            case ("STUDENT"):
                                postOrder(postUpdateChecks);
                                break;
                            case ("FACULTY"):
                                postOrder(postUpdateReserves);
                                break;
                            case("student update successful"):
                                postGrabOrder(postGetCheck);
                                break;
                            case("faculty update successful"):
                                postGrabOrder(postGetReserve);
                                break;
                            default:
                                exitMessage("ERROR: Invalid book found or book has not been checked out (yet). Scan a valid book to continue", true);
                        }

                    } catch (JSONException j) {
                        j.printStackTrace();
                        exitMessage("ERROR: failed to parse JSON from web server", true);
                    } catch (IOException e) {
                        exitMessage("ERROR: IOException", true);
                    }
                });
            }
        });
    }

    public void postGrabOrder(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("isbn", isbn)
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
                exitMessage("failed to connect to web server", true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();

                CheckinResult.this.runOnUiThread(() -> {
                    try {
                        JSONObject jsonObj = new JSONObject(myResponse);

                        patronID = findViewById(R.id.patron_id);
                        bookISBN = findViewById(R.id.book_isbn);
                        title = findViewById(R.id.book_title);
                        author = findViewById(R.id.book_author);
                        genre = findViewById(R.id.book_genre);
                        patronName = findViewById(R.id.patron_name);
                        coDate = findViewById(R.id.co_date);
                        dueDate = findViewById(R.id.due_date);
                        ciDate = findViewById(R.id.ci_date);

                        if(jsonObj.has("Sid")) {
                            patronRole = "Student";
                            role = findViewById(R.id.role);
                            role.setText(patronRole);
                            patronID.setText(jsonObj.getString("Sid"));
                        } else if (jsonObj.has("Fid")) {
                            patronRole = "Faculty";
                            role = findViewById(R.id.role);
                            role.setText(patronRole);
                            patronID.setText(jsonObj.getString("Fid"));
                        } else {
                            exitMessage("ERROR: ID not found in json String", true);
                        }

                        bookISBN.setText(jsonObj.getString("BookISBN"));
                        title.setText(jsonObj.getString("Title"));
                        author.setText(jsonObj.getString("Author"));
                        genre.setText(jsonObj.getString("Genre"));
                        patronName.setText(jsonObj.getString("Fname") + " " + jsonObj.getString("Lname"));
                        coDate.setText(formatDate(jsonObj.getString("CO_Date")));
                        dueDate.setText(formatDate(jsonObj.getString("Due_Date")));
                        ciDate.setText(formatDate(jsonObj.getString("CI_Date")));

                    } catch (JSONException j) {
                        j.printStackTrace();
                        exitMessage("ERROR: failed to parse JSON from web server", true);
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

    public void exitMessage(String msg, boolean isFinished) {
        Toast exit = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        exit.show();
        if(isFinished) finish();
    }
}
