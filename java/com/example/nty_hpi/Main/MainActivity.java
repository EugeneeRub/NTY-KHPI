package com.example.nty_hpi.Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;

import com.example.nty_hpi.BugReportActivity;
import com.example.nty_hpi.Faculties.MainFaculties;
import com.example.nty_hpi.Megdyn_Svazy.MegdynarSvajzyActivity;
import com.example.nty_hpi.StudentLife.StudLifeActivity;
import com.example.nty_hpi.R;
import com.example.nty_hpi.WebLoad;
import com.squareup.leakcanary.LeakCanary;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentTransaction fmTrans = null;// компонент для фрагмента
    Fragment fragment = null;// фрагмент для запуска UI
    DrawerLayout drawer = null;// вот это вот для бокового меню
    ActionBarDrawerToggle toggle = null;// и это тоже для него

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LeakCanary.install(getApplication());// следит за утечками(----УБРАТЬ ДО РЕЛИЗА ПРИЛОЖЕНИЯ ВЕЗДЕ----)
        intializeAndLoadComponents();// Вызов метода для инициализации и загрузки нужных компонентов
    }

    /* Метод для инициализации и загрузки нужных компонентов*/
    public void intializeAndLoadComponents(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = new MainPageActivity();
        fmTrans = getSupportFragmentManager().beginTransaction();
        fmTrans.replace(R.id.mainFrameLayout,fragment);
        fmTrans.commit();

        /* непонятная инициализация бокового меню*/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    /* Очитска всего*/
    void clearAll(){
        fmTrans.remove(fragment);
        fmTrans = null;
        fragment = null;

        drawer = null;
        toggle = null;
    }

    /* Для айтемов в боковом меню*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Отрабатывает при нажатии назад*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            /* если открыта боковая мменюшка, то закрывает ее*/
            drawer.closeDrawer(GravityCompat.START);
        } else {
            /* иначе закрываает приложение*/
            super.onBackPressed();
        }
    }

    /* Отрабатывает при нажатии на элемент бокового меню*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        /*ну сам выбор элемента списка и его непосредственная работа*/
        int id = item.getItemId();
        fmTrans = getSupportFragmentManager().beginTransaction();
        switch (id){
            case R.id.nav_main_page:
                fragment = new MainPageActivity();
                break;
            case R.id.nav_news:

                break;
            case R.id.nav_faculties:
                Intent intent = new Intent(this,MainFaculties.class);
                startActivity(intent);
                break;
            case R.id.nav_stud_life:
                Intent intent1 = new Intent(this,StudLifeActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_abitur:
                Intent intent2 = new Intent(this, WebLoad.class);
                intent2.putExtra("title","Абитуриенту");
                intent2.putExtra("url","http://vstup.kpi.kharkov.ua/");
                startActivity(intent2);
                break;
            case R.id.nav_map:
                Intent intent3 = new Intent(this, WebLoad.class);
                intent3.putExtra("title","Карта ХПИ");
                intent3.putExtra("url","https://goo.gl/maps/c4VMbSsVm532");
                startActivity(intent3);
                break;
            case R.id.nav_MSA:
                Intent intent4 = new Intent(this,MegdynarSvajzyActivity.class);
                startActivity(intent4);
                break;
            case R.id.nav_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/html");
                shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<p>Некоторый текст для отправки в пртложения</p>"));
                startActivity(Intent.createChooser(shareIntent,"Share using"));
                break;
            case R.id.nav_send:

                break;
            case R.id.nav_bug_report:
                Intent intent5 = new Intent(this,BugReportActivity.class);
                startActivity(intent5);
                break;
        }
        /*если был запущен фрагмент то запускает его, иначе пропускает и закрывает боковое меню*/
        if(fragment != null) {
            fmTrans.replace(R.id.mainFrameLayout, fragment);
            fmTrans.commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    /* Срабатывает при завершении работы активити*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
            clearAll();
    }
}
