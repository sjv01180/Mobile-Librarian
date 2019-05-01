package com.project.mobilelibrarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class BookCatalog extends AppCompatActivity {
    public static final String BOOK_ISBN = "com.project.mobilelibrarian.BookCatalog.BOOK_ISBN";
    public static final String BOOK_TITLE = "com.project.mobilelibrarian.BookCatalog.BOOK_TITLE";
    public static final String BOOK_AUTHOR = "com.project.mobilelibrarian.BookCatalog.BOOK_AUTHOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        addTableData();
    }

    private TextView setTextView(String text, float font, int paddingRight) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(TypedValue.COMPLEX_UNIT_PX, font);
        t.setPadding(0, 0, paddingRight, 0);
        return t;
    }

    private void addTableData() {
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

        for(int i = 0; i < 10; i++){
            TableRow data = new TableRow(this);

            TextView dataISBN = setTextView("[ISBN]", tableFont, rightPadding);
            TextView dataTitle = setTextView("[title]", tableFont, rightPadding);
            TextView dataAuthor = setTextView("[OwnerID]", tableFont, rightPadding);

            data.addView(dataISBN);
            data.addView(dataTitle);
            data.addView(dataAuthor);

            data.setOnClickListener(view -> {
                Intent details = new Intent(BookCatalog.this, BookDetails.class);
                details.putExtra(BOOK_ISBN, dataISBN.getText().toString());
                details.putExtra(BOOK_TITLE, dataTitle.getText().toString());
                details.putExtra(BOOK_AUTHOR, dataAuthor.getText().toString());
                startActivity(details);
            });

            table.addView(data);
        }
    }
}
