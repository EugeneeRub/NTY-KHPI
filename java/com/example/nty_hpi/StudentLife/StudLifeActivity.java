package com.example.nty_hpi.StudentLife;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.nty_hpi.CChangeOperationFAB;
import com.example.nty_hpi.CConteinerForList;
import com.example.nty_hpi.R;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eugenee Project on 24.07.2017.
 * Главная активити "Студенческая жизнь"
 */
public class StudLifeActivity extends AppCompatActivity {

    FloatingActionButton fab = null;// фаба для прокрутки или списка
    TextView tvBottomTitle = null;// заголовок в bottomSheet
    Fragment mFragment = null;// фрагмент запуска StudentLifeActivity
    BottomSheetBehavior mBehavior = null;// выезжающий layout снизу
    CoordinatorLayout mCoordinatorLayout = null;// layout для задания поведения bottomSheet
    CoordinatorLayout cancleImage = null;// layout кнопка отмены
    ScrollView mScrollView = null;// скролл, этим все сказано
    CChangeOperationFAB operation = null;// класс для работы с фабом

    /*Компоненты для списка*/
    CConteinerForList conteiner = new CConteinerForList();

    FragmentManager fm = getSupportFragmentManager();// менеджер фрагмента
    Bundle bundle = null;// бандл для передачи инфы в фрагмент

    String[] array = {
            "Профсоюзная организация студентов"
    };// массив для работы списка

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_life);
        LeakCanary.install(getApplication());// ловим на утечки

        initializeComponens();// Вызов метода инициализации компонентов;
        createList();// Вызов метода создания списка;
        checkForBundle(savedInstanceState);// Вызов метода проверки бандла

        methodForClickListeners();// Вызов метода слушателей

    }

    /**
     * Метод ининициализации компонентов
     */
    public void initializeComponens() {
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.viewForMSA);
        mBehavior = BottomSheetBehavior.from(mCoordinatorLayout);
        cancleImage = (CoordinatorLayout) findViewById(R.id.clForImageInMSA);
        tvBottomTitle = (TextView) findViewById(R.id.titleOnMSA);
        tvBottomTitle.setText("Список");

        (conteiner.mListView) = (ListView) findViewById(R.id.lvForMSA);
        fab = (FloatingActionButton) findViewById(R.id.fabOnStudLife);
        fab.setTag(R.drawable.ic_more_horiz_white_24dp);// Говорим что первая каринка это список

        mFragment = new StudentLifeFragment();

        operation = new CChangeOperationFAB(fab);
        operation.startFABWork();// запускаем метод работы над Фабом
    }

    /**
     * Метод создания списка
     */
    public void createList() {
        (conteiner.data) = new ArrayList<>();
        for (String str : array) {
            (conteiner.m) = new HashMap<>();
            (conteiner.m).put("nameOfStudLife", str);
            (conteiner.data).add((conteiner.m));
        }
        String[] from = {"nameOfStudLife"};
        int[] to = {R.id.text_item_for_bottom_sheet};
        (conteiner.adapter) = new SimpleAdapter(this, (conteiner.data), R.layout.item_for_bottom_sheet, from, to);
        (conteiner.mListView).setAdapter((conteiner.adapter));
    }

    /**
     * Метод проверки бандла
     */
    public void checkForBundle(Bundle savedInstanceState){
        if (savedInstanceState != null) {
            bundle = savedInstanceState.getBundle("bundle");
            mFragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.frameForStudLife,mFragment).commit();
        }else {
            bundle = new Bundle();
            bundle.putString("title","Студенческая жизнь");
            bundle.putInt("text",R.string.StudentLife);
            bundle.putInt("image",R.drawable.studen_life);
            mFragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.frameForStudLife,mFragment).commit();
        }
    }

    /**
     * Метод очистки комонентов
     */
    void clearAll(){
        fab = null;
        array = null;

        (conteiner.m) = null;
        (conteiner.data) = null;

        fm.beginTransaction().remove(mFragment);//удаляем фрагмент из очереди
        fm = null;
        mFragment = null;

        operation = null;
        mBehavior = null;

        cancleImage = null;

        mScrollView = null;

        (conteiner.mListView) = null;
    }

    /**
     * Метод для ClickListener-ов
     */
    public void methodForClickListeners() {
        /* ClickListener для фаба */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer integer = (Integer) fab.getTag();
                switch (integer) {
                    case R.drawable.ic_more_horiz_white_24dp:
                        fab.animate().scaleX(0).scaleY(0).setDuration(300).start();
                        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        Log.d("tag", "Open menu");
                        break;
                    case R.drawable.ic_keyboard_arrow_up_black_24dp:
                        mScrollView = (ScrollView) findViewById(R.id.srollViewOnStudLife);
                        int currentScroll = mScrollView.getScrollY();
                        if (currentScroll > 0) {
                            mScrollView.smoothScrollTo(0, 0);
                        }
                        Log.d("tag", "Up to top");
                        break;
                }
            }
        });

        /* ClickListener для layout-а закрытия bottomSheet */
        cancleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        /* Уте-то от следит за расположением bottomSheet */
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                TextView tv1 = (TextView) findViewById(R.id.tv1InStudLife);

                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    fab.animate().scaleX(1).scaleY(1).setDuration(300).start();
                    tv1.setTextIsSelectable(true);
                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    tv1.setTextIsSelectable(false);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        /* ClickListener для списка */
        (conteiner.mListView).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        createFragment("Профсоюзная организация студентов",
                                R.string.ProfSous, R.drawable.profspilka);
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

    /* Метод создания фрагмента */
    void createFragment(String title,Integer text,Integer image){
        mFragment = new StudentLifeFragment();
        bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("text", text);
        bundle.putInt("image", image);
        mFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.frameForStudLife, mFragment).commit();
    }

    /* Срабатывает перед удалением для сохранения данных*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBundle("bundle",bundle);
        super.onSaveInstanceState(outState);
    }

    /* Срабатывает при завершении работы активити*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
            clearAll();
    }

    /* Срабатывает при нажатии кнопки "назад" */
    @Override
    public void onBackPressed() {
        if(mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else{
            bundle.remove("title");
            bundle.remove("text");
            bundle.remove("image");
            bundle.clear();

            finish();
        }
    }
}
