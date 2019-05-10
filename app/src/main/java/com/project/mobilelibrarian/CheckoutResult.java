package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckoutResult extends AppCompatActivity {
    public String postFindID;
    public String postFindBook;
    public String postCanCheck;
    public String postCanReserve;
    public String postCheck;
    public String postReserve;

    DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
    DateTime co_date = new DateTime();
    DateTime due_date = new DateTime();

    String checkout = dateFormatter.print(co_date);
    String deadline;

    TextView campusID;
    TextView isbn;
    TextView title;
    TextView author;
    TextView genre;
    TextView checkoutDate;
    TextView dueDate;

    String resID;
    String resISBN;
    String postTitle;
    String postAuthor;
    String postGenre;

    public String idType;

    public String scanType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        postFindID = getString(R.string.url) + "/MobLib/find_id.php";
        postFindBook = getString(R.string.url) + "/MobLib/get_book.php";
        postCheck = getString(R.string.url) + "/MobLib/add_checks.php";
        postReserve = getString(R.string.url) + "/MobLib/add_reserves.php";
        postCanCheck = getString(R.string.url) + "/MobLib/can_check.php";
        postCanReserve = getString(R.string.url) + "/MobLib/can_reserve.php";
        Intent fromMain = getIntent();

        try {
            resID = fromMain.getStringExtra(MenuCirc.SCAN_RESULT);
            campusID = findViewById(R.id.campus_id);
            campusID.setText(resID);
            postQueryID(postFindID);
        } catch (IOException e) {
            exitMessage("ERROR: IO EXCEPTION", true);
        }

        isbn = findViewById(R.id.book_isbn);
        title = findViewById(R.id.book_title);
        author = findViewById(R.id.book_author);
        genre = findViewById(R.id.book_genre);

        checkoutDate = findViewById(R.id.co_date);
        checkoutDate.setText(checkout);

    }

    public void returnToMain(View v) {
        switch(v.getId()){
            case (R.id.cancel):
                finish();
                break;
            case (R.id.checkout):
                if (resID.isEmpty()) {
                    exitMessage("ERROR: CampusID isn't set. Please scan a student or faculty ID", false);
                } else if (resISBN.isEmpty()) {
                    exitMessage("ERROR: Book data isn't set. Please scan campus ISBN", false);
                } else {
                    try {
                        switch (idType) {
                            case ("STUDENT"):
                                postDetermineEligibility(postCanCheck);
                                break;
                            case ("FACULTY"):
                                postDetermineEligibility(postCanReserve);
                                break;
                            default:
                                exitMessage("ERROR: UNIDENTIFIED ID TYPE FOUND", true);
                                break;
                        }
                    } catch (IOException e) {
                        exitMessage("ERROR: IO EXCEPTION", true);
                    }
                }
                break;
            default:
                throw new RuntimeException("Unknown ID exception");
        }
    }

    public void scan(View v) {
        switch(v.getId()) {
            case (R.id.scan_id):
                scanType = "ITF";
                break;
            case (R.id.scan_book):
                scanType = "CODABAR";
                break;
            default:
                throw new RuntimeException("Unknown ID");

        }
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.setOrientationLocked(false);
        switch(scanType) {
            case ("ITF"):
                scanIntegrator.setPrompt("Scan a patron's student ID or faculty ID");
                break;
            case ("CODABAR"):
                scanIntegrator.setPrompt("Scan a book's campus ISBN located on the last page of the book");
                break;
            default:
                exitMessage("Error: unknown id found", true);
                break;
        }
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null && resultCode == RESULT_OK) {
            switch(scanningResult.getFormatName()) {
                case "ITF":
                    try {
                        resID = scanningResult.getContents();
                        postQueryID(postFindID);
                    } catch (IOException e) {
                        exitMessage("ERROR: IO EXCEPTION", true);
                    }
                    break;
                case "CODABAR":
                    try {
                        resISBN = scanningResult.getContents();
                        postQueryBook(postFindBook);
                    } catch (IOException e) {
                        exitMessage("ERROR: IO EXCEPTION", true);
                    }
                    break;
                default:
                    exitMessage("Unknown scan data received! Try again", false);
                    break;
            }
        } else {
            exitMessage("Non-existent or invalid scan data received! Try again", false);
        }
    }

    public void postQueryID(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("id", resID)
                .build();

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

                CheckoutResult.this.runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(myResponse);
                        String result = json.getString("message");
                        if(result.equals("UNKNOWN")) {
                            exitMessage("ERROR: Unknown id type found",false);
                        } else {
                            idType = result;
                            setDeadlineDate(idType);
                            campusID = findViewById(R.id.campus_id);
                            campusID.setText(resID);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public void postQueryBook(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("isbn", resISBN)
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

                CheckoutResult.this.runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(myResponse);
                        String result = json.getString("message");
                        switch(result) {
                            case("query successful"):
                                resISBN = json.getString("BookISBN");
                                postTitle = json.getString("Title");
                                postAuthor = json.getString("Author");
                                postGenre = json.getString("Genre");

                                if(resISBN.length() == 0 || postTitle.length() == 0 || postAuthor.length() == 0 || postGenre.length() == 0) {
                                    exitMessage("unknown book detected or catalog is empty! Check the catalog in Book Catalog", true);
                                    break;
                                }

                                isbn = findViewById(R.id.book_isbn);
                                title = findViewById(R.id.book_title);
                                author = findViewById(R.id.book_author);
                                genre = findViewById(R.id.book_genre);

                                isbn.setText(resISBN);
                                title.setText(postTitle);
                                author.setText(postAuthor);
                                genre.setText(postGenre);
                                break;
                            case("query failed"):
                                isbn.setText(resISBN);
                                resISBN = "";
                                exitMessage("Failed to find book from database. Try adding that book into the database first.", false);
                                break;
                            default:
                                exitMessage("Error: unknown json message string detected", true);
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        exitMessage("JSON Error: cannot parse json", true);
                    }
                });
            }
        });
    }

    public void postDetermineEligibility(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("id", resID)
                .build();

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

                CheckoutResult.this.runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(myResponse);
                        String result = json.getString("message");
                        switch(result) {
                            case ("no"):
                                exitMessage("Patron has not checked in their orders.",true);
                                break;
                            case ("yes"):
                                switch (idType) {
                                    case ("STUDENT"):
                                        postInsertOrder(postCheck);
                                        break;
                                    case ("FACULTY"):
                                        postInsertOrder(postReserve);
                                        break;
                                    default:
                                        exitMessage("ERROR: UNIDENTIFIED ID TYPE FOUND", true);
                                        break;
                                }
                                break;
                            default:
                                exitMessage("POST ERROR",true);
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        exitMessage("JSON Error", true);
                    } catch (IOException i) {
                        i.printStackTrace();
                        exitMessage("IO Error", true);
                    }
                });
            }
        });
    }

    public void postInsertOrder(String postUrl) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("isbn", resISBN)
                .add("id", resID)
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

                CheckoutResult.this.runOnUiThread(() -> {
                    try {
                        String msg;
                        JSONObject json = new JSONObject(myResponse);
                        String result = json.getString("message");
                        switch(result) {
                            case("Insertion successful"):
                                msg = "Successfully checked-out a book";
                                break;
                            case("Insertion failed"):
                                msg = "Failed to check out book";
                                break;
                            default:
                                msg = "EMPTY STRING";
                                break;
                        }

                        exitMessage(msg, true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public void setDeadlineDate(String id_type) {
        dueDate = findViewById(R.id.due_date);
        switch(id_type) {
            case ("STUDENT"):
                deadline = dateFormatter.print(due_date.plusWeeks(2));
                dueDate.setText(deadline);
                break;
            case ("FACULTY"):
                deadline = dateFormatter.print(due_date.plusWeeks(3));
                dueDate.setText(deadline);
                break;
            default:
                exitMessage("ERROR: DATETIME EXCEPTION", true);
        }
    }

    public void exitMessage(String msg, Boolean isFinished) {
        Toast exit = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        exit.show();
        if(isFinished) finish();
    }
}
