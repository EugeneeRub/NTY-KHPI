package com.example.nty_hpi.Megdyn_Svazy;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.nty_hpi.CChangeOperationFAB;
import com.example.nty_hpi.R;
import com.example.nty_hpi.WebLoad;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eugenee Project on 20.07.2017.
 * Главная активити "Международных связей"
 */
public class MegdynarSvajzyActivity extends AppCompatActivity {

    FloatingActionButton fab = null;// нижняя кнопка навигации
    TextView tvBottomTitle = null;// титулка в bottomSheet
    Fragment mFragment = null;// фрагмент для запуска окна
    ScrollView mScrollView = null;// скролл для хождения по активити
    CChangeOperationFAB operation = null;// класс для смены работы фаба

    BottomSheetBehavior mBehavior = null;// нижний layout для списка
    CoordinatorLayout mCoordinatorLayout = null;// layout для bottomSheet
    CoordinatorLayout cancleImage = null;// layout для закрытия bottomSheet

    /*Компоненты для списка*/
    ListView mListView = null;
    SimpleAdapter mAdapter = null;
    ArrayList<Map<String, String>> data = null;
    Map<String, String> m = null;

    FragmentManager fm = null;// менеджер для фрагмента
    Bundle bundle = null;// бандл для данны, которые пойдут во фрагмент

    int curentPosition = -1;// текущая позиция элемента списка


    String[] array = {
            "Отдел международных связей",
            "Международные проекты",
            "Участие в международных организациях и программах",
            "Международные гранты, программы, стипендии",
            "Группа фандрейзинга",
            "Международный обмен",
            "Украинско-Американский бизнес-центр",
            "Украинско-Французcкий Центр",
            "Erasmus+",
            "Блог отдела международных связей"
    };// массив для списка

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LeakCanary.install(getApplication());
        setContentView(R.layout.activity_megdynar_svajzy);

        initializeComponens();// Вызов метода инициализации компонентов;
        createList();// Вызов метода создания списка;
        checkForBundle(savedInstanceState);// Вызов метода проверки бандла

        methodForClickListeners();// Вызов метода слушателе
    }

    /**
     * Метод ининициализации компонентов
     */
    public void initializeComponens(){
        fm = getSupportFragmentManager();
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.viewForMSA);
        mBehavior = BottomSheetBehavior.from(mCoordinatorLayout);
        cancleImage = (CoordinatorLayout) findViewById(R.id.clForImageInMSA);
        tvBottomTitle = (TextView) findViewById(R.id.titleOnMSA);
        mScrollView = (ScrollView) findViewById(R.id.srollView);
        tvBottomTitle.setText("Список");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setTag(R.drawable.ic_more_horiz_white_24dp);// говорим что будет первым список

        mListView = (ListView) findViewById(R.id.lvForMSA);
        mFragment = new SvazyFragment();

        operation = new CChangeOperationFAB(fab);
        operation.startFABWork();
    }

    /**
     * Метод создания списка
     */
    public void createList(){
        data = new ArrayList<Map<String, String>>();
        for (String str : array) {
            m = new HashMap<>();
            m.put("nameOfMSA", str);
            data.add(m);
        }
        String[] from = {"nameOfMSA"};
        int[] to = {R.id.text_item_for_bottom_sheet};
        mAdapter = new SimpleAdapter(this, data, R.layout.item_for_bottom_sheet, from, to);
        mListView.setAdapter(mAdapter);
    }

    /**
     * Метод проверки бандла
     */
    public void checkForBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            int curent = savedInstanceState.getInt("myObj");
            if (curent == -1) {
                callMainFirstMethod();
                mFragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.frameForSvazy, mFragment).commit();
            } else {
                mListView.performItemClick(mListView.getAdapter().getView(curent, null, null), curent, mListView.getAdapter().getItemId(curent));
            }
        } else {
            callMainFirstMethod();
            mFragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.frameForSvazy, mFragment).commit();
        }
    }

    /* Метод вызывается при создании активити или повороте экрана,
     только для главного экрана при запуске*/
    private void callMainFirstMethod() {
        bundle = new Bundle();
        bundle.putInt("id", 0);
        bundle.putString("toolbarText", "Международные связи");
        bundle.putInt("image1", R.drawable.intrel);
        bundle.putInt("text1", R.string.first_text_in_megdSvajzpage);
        bundle.putInt("text2", R.string.second_text_in_megdSvajzpage);
    }

    /* Методдля ClickListener-ов*/
    public void methodForClickListeners() {
        /* слушатель для фаба*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer integer = (Integer) fab.getTag();
                switch (integer) {
                    case R.drawable.ic_more_horiz_white_24dp:
                        /* будет появляться список*/
                        fab.animate().scaleX(0).scaleY(0).setDuration(300).start();
                        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        Log.d("tag", "Open menu");
                        break;
                    case R.drawable.ic_keyboard_arrow_up_black_24dp:
                        /* будет поднимать в начало*/
                        mScrollView = (ScrollView) findViewById(R.id.srollView);
                        int currentScroll = mScrollView.getScrollY();
                        if(currentScroll > 0){
                            mScrollView.smoothScrollTo(0,0);
                        }
                        Log.d("tag", "Up to top");
                        break;
                }
            }
        });

        /* слушатель для layout-а картинки отмены*/
        cancleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        /* получение информации о поведении bottomSheet*/
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                TextView tv1 = (TextView) findViewById(R.id.tv1InMegdynarSvazy);
                TextView tv2 = (TextView) findViewById(R.id.tv2InMegdynarSvazy);
                TextView tv3 = (TextView) findViewById(R.id.tv3InMegdynarSvazy);
                TextView tv4 = (TextView) findViewById(R.id.tv4InMegdynarSvazy);

                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    fab.animate().scaleX(1).scaleY(1).setDuration(300).start();
                    tv1.setTextIsSelectable(true);
                    tv2.setTextIsSelectable(true);
                    tv3.setTextIsSelectable(true);
                    tv4.setTextIsSelectable(true);
                }else if (BottomSheetBehavior.STATE_EXPANDED == newState){
                    tv1.setTextIsSelectable(false);
                    tv2.setTextIsSelectable(false);
                    tv3.setTextIsSelectable(false);
                    tv4.setTextIsSelectable(false);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick (AdapterView < ? > parent, View view,int position, long id){

                switch (position) {
                    case 0:
                        zeroID();
                        break;
                    case 1:
                        firstID();
                        break;
                    case 2:
                        secondID();
                        break;
                    case 3:
                        thirdID();
                        break;
                    case 4:
                        fourthID();
                        break;
                    case 5:
                        fifthID();
                        break;
                    case 6:
                        sixthID();
                        break;
                    case 7:
                        seventhID();
                        break;
                    case 8:
                        eightID();
                        break;
                    case 9:
                        ninthID();
                        break;
                }
                if (mFragment != null) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    /*начало методов для switch-case*/
    public void zeroID() {
        curentPosition = 0;
        bundle = new Bundle();
        bundle.putString("toolbarText", "Отдел международных связей");
        bundle.putInt("id", 1);
        bundle.putInt("image1", R.drawable.flagi_stran_mira);
        bundle.putInt("text2", R.string.second_text_for_otdelMegdSvajzpage);
        mFragment = new SvazyFragment();
        mFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.frameForSvazy, mFragment).commit();
    }
    //--------------------------------------------------------------------------------------------//
    public void firstID() {
        Intent intent = new Intent(MegdynarSvajzyActivity.this, WebLoad.class);
        intent.putExtra("title", "Международные проекты");
        intent.putExtra("url", "http://www.ec.kharkiv.edu/mp.html");
        startActivity(intent);
    }
    //--------------------------------------------------------------------------------------------//
    public void secondID() {
        curentPosition = 2;
        bundle = new Bundle();
        bundle.putString("toolbarText", "Участие в международных организациях и программах");
        bundle.putInt("id", 2);
        bundle.putInt("image1", R.drawable.eua);
        bundle.putInt("image2", R.drawable.bsun);
        bundle.putInt("image3", R.drawable.second_eua);
        bundle.putInt("text1", R.string.first_text_for_MegdunarodnieProgram);
        bundle.putInt("text2", R.string.second_text_for_MegdunarodnieProgram);
        bundle.putInt("text3", R.string.third_text_for_MegdunarodnieProgram);
        bundle.putInt("text4", R.string.fourth_text_for_MegdunarodnieProgram);
        mFragment = new SvazyFragment();
        mFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.frameForSvazy, mFragment).commit();
    }
    //--------------------------------------------------------------------------------------------//
    public void thirdID() {
        Intent intent0 = new Intent(MegdynarSvajzyActivity.this, WebLoad.class);
        intent0.putExtra("title", "Международные гранты, программы, стипендии");
        intent0.putExtra("url", "http://www.ec.kharkiv.edu/gsk.html");
        startActivity(intent0);
    }
    //--------------------------------------------------------------------------------------------//
    public void fourthID() {
        Intent intent1 = new Intent(MegdynarSvajzyActivity.this, WebLoad.class);
        intent1.putExtra("title", "Группа фандрейзинга");
        intent1.putExtra("url", "http://www.ec.kharkiv.edu/");
        startActivity(intent1);
    }
    //--------------------------------------------------------------------------------------------//
    public void fifthID() {
        curentPosition = 5;
        bundle = new Bundle();
        bundle.putString("toolbarText", "Международный обмен");
        bundle.putInt("id", 5);
        bundle.putInt("image1", R.drawable.people_photo);
        bundle.putInt("text2", R.string.first_text_for_MegdunarodniyObmen);
        mFragment = new SvazyFragment();
        mFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.frameForSvazy, mFragment).commit();
    }
    //--------------------------------------------------------------------------------------------//
    public void sixthID() {
        curentPosition = 6;
        bundle = new Bundle();
        bundle.putString("toolbarText", "Украинско-Американский бизнес-центр");
        bundle.putInt("id", 6);
        bundle.putInt("text1", R.string.UKR_USA_business);
        mFragment = new SvazyFragment();
        mFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.frameForSvazy, mFragment).commit();
    }
    //--------------------------------------------------------------------------------------------//
    public void seventhID() {
        curentPosition = 7;
        bundle = new Bundle();
        bundle.putString("toolbarText", "Украинско-Французcкий Центр");
        bundle.putInt("id", 7);
        bundle.putInt("text1", R.string.UKR_FRANCE_center);
        mFragment = new SvazyFragment();
        mFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.frameForSvazy, mFragment).commit();
    }
    //--------------------------------------------------------------------------------------------//
    public void eightID() {
        curentPosition = 8;
        bundle = new Bundle();
        bundle.putString("toolbarText", "Erasmus+");
        bundle.putInt("id", 8);
        bundle.putInt("image1", R.drawable.erasmus);
        bundle.putInt("text2", R.string.ERASMUS);
        mFragment = new SvazyFragment();
        mFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.frameForSvazy, mFragment).commit();
    }
    //--------------------------------------------------------------------------------------------//
    public void ninthID() {
        Intent intent2 = new Intent(MegdynarSvajzyActivity.this, WebLoad.class);
        intent2.putExtra("title", "Блог отдела международных связей");
        intent2.putExtra("url", "http://blogs.kpi.kharkov.ua/omsru/");
        startActivity(intent2);
    }
    /*конец методов для switch-case*/

    /* Очистка всего*/
    void clearAll(){
        fab = null;
        array = null;
        tvBottomTitle = null;

        mCoordinatorLayout.removeAllViews();
        mCoordinatorLayout = null;

        m = null;
        data = null;
        mAdapter = null;

        fm.beginTransaction().remove(mFragment);
        fm = null;
        mFragment = null;

        operation = null;
        mBehavior = null;
        cancleImage = null;

        mScrollView = null;

        mListView = null;
    }

    /* Отрабатывает при повороте экрана, сохраняя текущую позицию, элемента списка*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("myObj", curentPosition);
        super.onSaveInstanceState(outState);
    }

    /* Отрабатывает при разрушении экрана или после поворота уничтожается, вызывает очистку*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
            clearAll();
    }

    /* Отрабатывает при нажатии на кнопку back*/
    @Override
    public void onBackPressed() {
        if(mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            /* Если BottomSheet раскрыта, закрывает ее*/
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else{
            /* Если BottomSheet не раскрыта, закрывает активити*/
            bundle.remove("title");
            bundle.remove("text");
            bundle.remove("image");
            bundle.clear();
            bundle = null;

            finish();
        }
    }
}

