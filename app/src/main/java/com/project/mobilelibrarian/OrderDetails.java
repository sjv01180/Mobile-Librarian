package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Text;

public class OrderDetails extends AppCompatActivity {

    TextView bookISBN;
    TextView title;
    TextView ownerID;
    TextView author;
    TextView oName;
    TextView role;
    TextView genre;
    TextView coDate;
    TextView ciDate;
    TextView dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Intent orderRow = getIntent();
        String fullName = orderRow.getStringExtra(OrderCatalog.EXTRA_FNAME) + " " + orderRow.getStringExtra(OrderCatalog.EXTRA_LNAME);

        bookISBN = findViewById(R.id.book_isbn);
        bookISBN.setText(orderRow.getStringExtra(OrderCatalog.EXTRA_ISBN));

        title = findViewById(R.id.book_title);
        title.setText(orderRow.getStringExtra(OrderCatalog.EXTRA_TITLE));

        ownerID = findViewById(R.id.owner_id);
        ownerID.setText(orderRow.getStringExtra(OrderCatalog.EXTRA_OWNER_ID));

        author = findViewById(R.id.book_author);
        author.setText(orderRow.getStringExtra(OrderCatalog.EXTRA_AUTHOR));

        genre = findViewById(R.id.book_genre);
        genre.setText(orderRow.getStringExtra(OrderCatalog.EXTRA_GENRE));

        oName = findViewById(R.id.owner_name);
        oName.setText(fullName);

        role = findViewById(R.id.role);
        role.setText(orderRow.getStringExtra(OrderCatalog.EXTRA_ROLE));

        coDate = findViewById(R.id.co_date);
        coDate.setText(formatDate(orderRow.getStringExtra(OrderCatalog.EXTRA_CO_DATE)));

        String ciString = orderRow.getStringExtra(OrderCatalog.EXTRA_CI_DATE);
        ciDate = findViewById(R.id.ci_date);
        ciDate.setText(formatDate(ciString));

        dueDate = findViewById(R.id.due_date);
        dueDate.setText(formatDate(orderRow.getStringExtra(OrderCatalog.EXTRA_DUE_DATE)));

    }

    public String formatDate(String sqlDate) {
        DateTime proxyDate = new DateTime(sqlDate);
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        String strDate =  dateFormatter.print(proxyDate);

        if(strDate.equals("01/01/1000")) {
            return "Not checked in";
        }

        return strDate;
    }
}