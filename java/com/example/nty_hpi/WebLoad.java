package com.example.nty_hpi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Eugenee Project on 26.07.2017.
 * Класс для работы с своим браузером
 */
public class WebLoad extends AppCompatActivity {

    TextView mTitle;// верхний текст в тайтле
    TextView mBottomTitleUrl;// нижний текст в тайтле
    Button btnRepeat;// кнопка повтора подключения
    CConnectionToInternet connection = new CConnectionToInternet();// создание объекта для коннекта

    WebView wvLayout;// вебка для созадания браузера
    ViewFlipper mViewFlipper;// флиппер для замены слайдов

    String mUrl;// переменная для хранения ссылки

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LeakCanary.install(getApplication());
        setContentView(R.layout.activity_web_load);

        intializeComponents();// Вызов метода для инициализации всех компонентов
        connection.startOperation();// Вызов метода запуска открытия сайта
        specialForClickListeners();// Вызов метода для листнеров
    }

    /**
    * Метод созданый для хранения слушателей
    */
    void specialForClickListeners() {
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection.startOperation();
            }
        });
    }

    /**
    * Метод созданый для инициализации компонентов
    */
    void intializeComponents(){
        Intent intent = getIntent();
        mTitle = (TextView) findViewById(R.id.firstTextOnWebLoad);
        mBottomTitleUrl = (TextView) findViewById(R.id.secondTextOnWebLoad);
        mViewFlipper = (ViewFlipper) findViewById(R.id.VFForWebLoad);
        btnRepeat = (Button) findViewById(R.id.btnRepeatLoadInWEBLoad);

        mTitle.setText(intent.getStringExtra("title"));
        mUrl = intent.getStringExtra("url");
        mBottomTitleUrl.setText(mUrl);
    }

    /**
    * Класс для коннекта и проверки интернета
    * */
    class CConnectionToInternet {
        /*
        * Метод созданый для проверки интернет соединения
        */
        public boolean isOnlineInternet() {
            ConnectivityManager cm = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }

        /*
        * Метод созданый для инициализации компонентов
        */
        void startOperation() {
            if (isOnlineInternet() == true) {
                workOperation();
            }
        }
    }

    /**
    * Метод созданый для открытия веб страниц в "своем" браузере
    */
    public void workOperation(){
        mViewFlipper.showNext();
        wvLayout = (WebView) findViewById(R.id.wvInWebLoad);
        wvLayout.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                mViewFlipper.showNext();
                super.onPageFinished(view, url);
            }
        });
        wvLayout.getSettings().setJavaScriptEnabled(true);
        wvLayout.loadUrl(mUrl);
    }

    /* Очитска всего*/
    void clearAll(){
        mTitle = null;
        mBottomTitleUrl = null;

        btnRepeat = null;

        connection = null;
        mUrl = null;
        mViewFlipper = null;

        wvLayout.removeAllViews();
        wvLayout.clearCache(true);
        wvLayout.clearHistory();

        wvLayout.destroy();
        wvLayout = null;
    }

    /* Срабатывает при завершении работы активити*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearAll();

    }
}