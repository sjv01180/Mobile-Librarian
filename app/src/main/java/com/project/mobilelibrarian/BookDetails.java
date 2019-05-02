package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BookDetails extends AppCompatActivity {


    TextView bookISBN;
    TextView title;
    TextView author;
    TextView genre;

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

    }
}
