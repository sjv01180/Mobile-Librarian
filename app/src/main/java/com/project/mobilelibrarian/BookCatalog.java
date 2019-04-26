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
    public static final String BOOK_ISBN = "com.project.mobilelibrarian.BOOK_ISBN";
    public static final String BOOK_TITLE = "com.project.mobilelibrarian.BOOK_TITLE";
    public static final String BOOK_OWNER = "com.project.mobilelibrarian.BOOK_OWNER";

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
        int rightPadding = (int) (getResources().getDimension(R.dimen.padding) / getResources().getDisplayMetrics().density);

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
            TextView dataOwner = setTextView("[OwnerID]", tableFont, rightPadding);

            data.addView(dataISBN);
            data.addView(dataTitle);
            data.addView(dataOwner);

            data.setOnClickListener(view -> {
                Intent details = new Intent(BookCatalog.this, BookDetails.class);
                details.putExtra(BOOK_ISBN, dataISBN.getText());
                details.putExtra(BOOK_TITLE, dataTitle.getText());
                details.putExtra(BOOK_OWNER, dataOwner.getText());
                startActivity(details);
            });

            table.addView(data);
        }
    }
}
