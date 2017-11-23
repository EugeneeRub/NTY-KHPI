package com.example.nty_hpi;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Eugenee Project on 20.07.2017.
 * Активити для репорта ошибок
 */
public class BugReportActivity extends AppCompatActivity {
    EditText mEditText;//место для ввода ошибок
    TextView mTextView;//отправка нашего репорта

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LeakCanary.install(getApplication());
        setContentView(R.layout.report_bag_activity);

        mEditText = (EditText) findViewById(R.id.textForBug);
        mTextView = (TextView) findViewById(R.id.textSend);

        setTitle("Bug Report");

        specialForClickListeners();
    }

    /* Метод для листнеров*/
    void specialForClickListeners(){
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to = "rubezinevgenij@gmail.com";
                String messege = mEditText.getText().toString();
                String title = "BUG REPORT";

                mEditText.setText("");

                Intent emailSend = new Intent(Intent.ACTION_SEND);
                emailSend.putExtra(Intent.EXTRA_EMAIL, new String []{to});
                emailSend.putExtra(Intent.EXTRA_SUBJECT, title);
                emailSend.putExtra(Intent.EXTRA_TEXT, messege);

                emailSend.setType("message/rfc822");
                startActivity(Intent.createChooser(emailSend,"Выберите почту :"));
            }
        });
    }

    /* Очитска всего*/
    void clearAll(){
        mEditText = null;
        mTextView = null;
    }

    /* Срабатывает при завершении работы активити*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearAll();
    }
}
