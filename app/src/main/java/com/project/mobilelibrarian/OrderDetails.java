package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OrderDetails extends AppCompatActivity {


    TextView bookISBN;
    TextView title;
    TextView ownerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Intent orderRow = getIntent();
        bookISBN = findViewById(R.id.book_isbn);
        bookISBN.setText(orderRow.getStringExtra(BookCatalog.BOOK_ISBN));

        title = findViewById(R.id.book_title);
        title.setText(orderRow.getStringExtra(BookCatalog.BOOK_TITLE));

        ownerID = findViewById(R.id.owner_id);
        ownerID.setText(orderRow.getStringExtra(BookCatalog.BOOK_OWNER));

    }
}
