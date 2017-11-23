package com.example.nty_hpi.Faculties;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.nty_hpi.R;
import com.example.nty_hpi.WebLoad;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Eugenee Project on 29.06.2017.
 * Фрагмент для работы с экраном кафедры
 */

public class CafedryScreen extends Fragment {

    CafedryScreenConteiner mConteiner;// контейнер для данных
    ViewFlipper viewflipperincafedryscreen;// флиппер для смены окон

    /*Компоненты для активити*/
    TextView facultiesTextNameInCafedryCcreen;
    TextView name;
    ImageView imageViewPeople;
    Button openWebLoad;
    Button buttonSiteInCafedryScreen;
    TextView facultiesAboutTextInCafedryScreen;

    android.support.v7.widget.Toolbar toolbar;// тулбар
    ConteinerOfElements lastConteiner;// контейнер для передачи нужных данных в новый поток
    org.jsoup.nodes.Document document;// документ для выкачки данных

    Elements content;// элементы для записи в String для "О факультете"

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cafedry_screen, container, false);
        toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbarInCafedryScreen);
        initializeAndSetComponents(view);// Вызов метода инициализации данных
        AllClickListenersInThisClass();// Вызов метода слушателей
        setInConteinerOfElements();// Вызов метода записи в контейнер

        startMethod();//запуск потока

        return view;
    }

    /**
     * Метод инициализации и некоторой записи данных
     * */
    void initializeAndSetComponents(View v){
        Bundle bundle = getArguments();
        FacultiesScreen.ConteinerForCafedry _conteiner = (FacultiesScreen.ConteinerForCafedry) bundle.get("object");
        mConteiner =  new CafedryScreenConteiner(_conteiner);

        openWebLoad = (Button) v.findViewById(R.id.btnRepeatLoadInCafedryScreen);
        viewflipperincafedryscreen = (ViewFlipper) v.findViewById(R.id.viewflipperInCafedryCcreen);
        facultiesTextNameInCafedryCcreen = (TextView) v.findViewById(R.id.facultiesTextNameInCafedryCcreen);
        imageViewPeople = (ImageView) v.findViewById(R.id.imageViewPeopleInCafedryCcreen);
        buttonSiteInCafedryScreen = (Button) v.findViewById(R.id.buttonSiteInCafedryCcreen);
        facultiesAboutTextInCafedryScreen = (TextView) v.findViewById(R.id.facultiesAboutTextInCafedryCcreen);

        name = (TextView) v.findViewById(R.id.floatingTextInToolBar);

        facultiesTextNameInCafedryCcreen.setText("Зав.кафедры: " + mConteiner.nameOfZavCafedry);
        Picasso.with(v.getContext()).load((String) mConteiner.photoZavCafedryURL).into(imageViewPeople);
    }

    /**
     * Метод со всеми слушателями
     * */
    private void AllClickListenersInThisClass(){
        /* Листнер для кнопки повтора загрузки*/
        openWebLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMethod();
            }
        });

         /* Листнер для запуска браузера с сайтом*/
        buttonSiteInCafedryScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConteiner.siteOfCafedry != "") {
                    Intent intent = new Intent(getActivity(), WebLoad.class);
                    intent.putExtra("title", mConteiner.nameOfCafedry);
                    intent.putExtra("url", mConteiner.siteOfCafedry);
                    startActivity(intent);
                } else {
                    Snackbar.make(v, "Сайт отсутсвтует", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Метод записи в контейнер информации
     * которая будет переходить в другой поток
     * */
    private void setInConteinerOfElements(){
        lastConteiner = new ConteinerOfElements();

        lastConteiner.mViewFlipper = viewflipperincafedryscreen;
        lastConteiner.url = mConteiner.aboutFac;
        lastConteiner.nameOfCafedry = mConteiner.nameOfCafedry;
        lastConteiner.countOfBadText = mConteiner.countOfBadText;
    }

    /**
     * Класс поток для работы с выкачкой html с сайта
     * */
    class myLoaderFromSite extends AsyncTask<Void,Void,Void>{
        String mString = "";
        String firstCheck = "[style=\"text-align: justify;\"]";
        String secondCheck = ".page p";
        String thirdCheck = ".entry-classic p";
        String fourthCheck = "[style=\"margin: 0px; text-align: justify;\"]";

        ConteinerOfElements mConteiner = new ConteinerOfElements();

        public myLoaderFromSite(ConteinerOfElements _conteiner) {
            mConteiner = _conteiner;
        }

        @Override
        protected Void doInBackground(Void... params) {
                document = null;

                Log.d("tag", "Перед коннектом");
            try {
                document = Jsoup.connect(mConteiner.url)
                        .ignoreContentType(true)
                        .followRedirects(true)
                        .post();
                content = document.select(firstCheck);

                if (content.size() == 0) {
                    content = null;
                    content = document.select(secondCheck);
                } else if (content.size() == 0) {
                    content = null;
                    content = document.select(thirdCheck);
                } else if (content.size() == 0 ||
                        mConteiner.nameOfCafedry.equals("Теоретические основы электротехники") ||
                        mConteiner.nameOfCafedry.equals("Мультимедийные информационные технологии и системы")) {
                    content = null;
                    content = document.select(fourthCheck);
                }

                 for (Element element : content) {
                    if((mConteiner.countOfBadText) == 0) {
                        mString += "\t" + element.text();
                        mString += '\n';
                        Log.d("tag", "Adding text");
                    }else if((mConteiner.countOfBadText) == 1){
                        (mConteiner.countOfBadText)--;
                    }else if((mConteiner.countOfBadText) == 2){
                        (mConteiner.countOfBadText)--;
                    }else if(mConteiner.countOfBadText == 3){
                        (mConteiner.countOfBadText)--;
                    }else if((mConteiner.countOfBadText) == 4){
                        (mConteiner.countOfBadText)--;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            facultiesAboutTextInCafedryScreen.setText(mString);
            viewflipperincafedryscreen.showNext();

            /*Будет начинаться двигаться текст*/
            name.setText(mConteiner.nameOfCafedry);
            name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            name.setSingleLine(true);
            name.setMarqueeRepeatLimit(-1);
            name.setSelected(true);

            /*Специальная очистка чтобы не забивать данные телефона*/
            mString = null;
            mConteiner.countOfBadText = 0;
            mConteiner.mViewFlipper = null;
            mConteiner.nameOfCafedry = null;
            mConteiner.url = null;
            mConteiner = null;

            document = null;
            content.remove();
            content = null;
        }
    }

    /**
     * Метод запуска потока
     * */
    void startMethod() {
        if (isOnline() == true) {
            viewflipperincafedryscreen.showNext();
            myLoaderFromSite loaderFromSite = new myLoaderFromSite(lastConteiner);
            loaderFromSite.execute();
        }
    }

    /**
     * Метод для проверки наличчия интернета
     * */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Класс контейнер для потока,
     * который хранит для него данные
     * */
    class ConteinerOfElements{
        public ViewFlipper mViewFlipper;
        public String url;
        public String nameOfCafedry;
        public int countOfBadText;

        public ConteinerOfElements(ViewFlipper viewFlipper, String url, String nameOfCafedry,int countOfBadText) {
            mViewFlipper = viewFlipper;
            this.url = url;
            this.nameOfCafedry = nameOfCafedry;
            this.countOfBadText = countOfBadText;
        }

        public ConteinerOfElements() {
            mViewFlipper = null;
            url = "";
            nameOfCafedry = "";
        }
    }

    /**
     * Класс контейнер для всех компонентов,
     * который хранит для них данные
     * */
    class CafedryScreenConteiner{
        public String nameOfCafedry = "";
        public String nameOfZavCafedry = "";
        public String photoZavCafedryURL = "";
        public String siteOfCafedry = "";
        public String aboutFac = "";
        public int countOfBadText = 0;

        CafedryScreenConteiner(FacultiesScreen.ConteinerForCafedry conteiner){
            nameOfCafedry =  conteiner.nameOfCafedry;
            nameOfZavCafedry =  conteiner.nameOfZavCafedry;
            photoZavCafedryURL =  conteiner.photoZavCafedryURL;
            siteOfCafedry =  conteiner.siteOfCafedry;
            aboutFac =  conteiner.aboutFac;
            countOfBadText = conteiner.countOfBadText;
        }
    }

    /* Метод очистки данных перед закрытием приложения */
    void clearAll(){
        mConteiner = null;

        viewflipperincafedryscreen.removeAllViews();
        viewflipperincafedryscreen = null;

        facultiesTextNameInCafedryCcreen = null;
        name = null;
        imageViewPeople = null;
        openWebLoad = null;
        buttonSiteInCafedryScreen = null;
        facultiesAboutTextInCafedryScreen = null;

        toolbar = null;

        lastConteiner = null;

    }

    /* Отрабатывает перед полным уничтожением или закрытием приложения */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearAll();
    }

}
