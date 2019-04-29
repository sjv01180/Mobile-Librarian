package com.project.mobilelibrarian;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CheckinResult extends AppCompatActivity {

    TextView studentID;
    TextView isbn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        Intent fromMain = getIntent();

        studentID = findViewById(R.id.student_id);
        isbn = findViewById(R.id.book_isbn);

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                finish();
            }
        }.start();
    }
}
