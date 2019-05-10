package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderCatalog extends AppCompatActivity {
    public String postGetChecks;
    public String postGetReserves;

    public static final String EXTRA_ISBN = "com.project.mobilelibrarian.EXTRA_1";
    public static final String EXTRA_TITLE = "com.project.mobilelibrarian.EXTRA_2";
    public static final String EXTRA_AUTHOR = "com.project.mobilelibrarian.EXTRA_3";
    public static final String EXTRA_GENRE = "com.project.mobilelibrarian.EXTRA_4";
    public static final String EXTRA_OWNER_ID = "com.project.mobilelibrarian.EXTRA_5";
    public static final String EXTRA_CO_DATE = "com.project.mobilelibrarian.EXTRA_6";
    public static final String EXTRA_CI_DATE = "com.project.mobilelibrarian.EXTRA_7";
    public static final String EXTRA_DUE_DATE = "com.project.mobilelibrarian.EXTRA_8";
    public static final String EXTRA_FNAME = "com.project.mobilelibrarian.EXTRA_9";
    public static final String EXTRA_LNAME = "com.project.mobilelibrarian.EXTRA_10";
    public static final String EXTRA_ROLE = "com.project.mobilelibrarian.EXTRA_11";

    Switch switchTable;
    public String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        postGetChecks = getString(R.string.url) + "/MobLib/get_checks.php";
        postGetReserves = getString(R.string.url) + "/MobLib/get_reserves.php";
        switchTable = findViewById(R.id.table_switch);
        switchTable.setChecked(false);

        try {
            role = "Student";
            postGrabOrders(postGetChecks);
        } catch (IOException e) {
            exitMessage("ERROR: cannot access catalog at the moment");
        }

        switchTable.setOnCheckedChangeListener((compoundButton, b) -> {
            try {
                if(!switchTable.isChecked()) {
                        role = "Student";
                        switchTable.setText("Student Checkouts");
                        postGrabOrders(postGetChecks);
                } else {
                        role = "Faculty";
                        switchTable.setText("Faculty Reserves");
                        postGrabOrders(postGetReserves);
                }
            } catch (IOException e) {
                exitMessage("ERROR: IOException");
            }
        });
    }

    public void postGrabOrders(String postUrl) throws IOException {
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

                OrderCatalog.this.runOnUiThread(() -> {
                    try {
                        JSONArray json = new JSONArray(myResponse);
                        addTableData(json, json.length());

                    } catch (JSONException j) {
                        j.printStackTrace();
                        exitMessage("ERROR: failed to parse JSON from web server");
                    }
                });
            }
        });
    }

    private void addTableData(JSONArray resArr, int size) {
        float tableFont = getResources().getDimension(R.dimen.tableFont);
        int rightPadding = (int) (getResources().getDimension(R.dimen.padding) / getResources().getDisplayMetrics().density);
        int rightPaddingTable = (int) (getResources().getDimension(R.dimen.padding_body) / getResources().getDisplayMetrics().density);

        TableLayout table = findViewById(R.id.table);
        table.removeAllViews();

        TableRow header = new TableRow(this);

        TextView labelISBN = setTextView("Book ISBN", tableFont, rightPadding);
        TextView labelTitle = setTextView("Title", tableFont, rightPadding);
        TextView labelOwner = setTextView("OwnerID", tableFont, rightPadding);

        header.addView(labelISBN);
        header.addView(labelTitle);
        header.addView(labelOwner);

        table.addView(header);

        for(int i = 0; i < size; i++) {
            TableRow data = new TableRow(this);
            try {
                TextView dataOwner;

                JSONObject resObj = resArr.getJSONObject(i);

                TextView dataISBN = setTextView(resObj.getString("BookISBN"), tableFont, rightPaddingTable);
                TextView dataTitle = setTextView(resObj.getString("Title"), tableFont, rightPaddingTable);

                String id;
                if(resObj.has("Sid")) {
                    id = resObj.getString("Sid");
                } else if(resObj.has("Fid")) {
                    id = resObj.getString("Fid");
                } else {
                    id = null;
                    exitMessage("ERROR: id doesn't exist in JSON object");
                }
                dataOwner = setTextView(id, tableFont, rightPaddingTable);

                data.addView(dataISBN);
                data.addView(dataTitle);


                data.addView(dataOwner);

                String coDate = resObj.getString("CO_Date");
                String ciDate = resObj.getString("CI_Date");
                String dueDate = resObj.getString("Due_Date");
                String author = resObj.getString("Author");
                String genre = resObj.getString("Genre");
                String fName = resObj.getString("Fname");
                String lName = resObj.getString("Lname");

                data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent details = new Intent(OrderCatalog.this, OrderDetails.class);
                        details.putExtra(EXTRA_ISBN, dataISBN.getText().toString());
                        details.putExtra(EXTRA_TITLE, dataTitle.getText().toString());
                        details.putExtra(EXTRA_OWNER_ID, dataOwner.getText().toString());
                        details.putExtra(EXTRA_AUTHOR, author);
                        details.putExtra(EXTRA_GENRE, genre);
                        details.putExtra(EXTRA_CO_DATE, coDate);
                        details.putExtra(EXTRA_CI_DATE, ciDate);
                        details.putExtra(EXTRA_DUE_DATE, dueDate);
                        details.putExtra(EXTRA_FNAME, fName);
                        details.putExtra(EXTRA_LNAME, lName);
                        details.putExtra(EXTRA_ROLE, role);
                        startActivity(details);
                    }
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
