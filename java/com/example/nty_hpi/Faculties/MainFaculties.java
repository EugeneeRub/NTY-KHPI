package com.example.nty_hpi.Faculties;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.nty_hpi.R;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Eugenee Project on 20.06.2017.
 * Главная активити для работы со списком и содержимым элементов этих item;
 */
public class MainFaculties extends AppCompatActivity {

    FacultiesScreen.OnBackPressedListner onBackPressedListener = null;// компонент нажатия назад
    ListOfFaculties mFragment = null;// фрагмент для запуска списка
    private String FRAGMENT_INSTANCE_NAME = "fragment";// не знаю для чего, где-то нужно
    FragmentManager fm = null;// менеджр для работы с фрагментоами

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LeakCanary.install(getApplication());// чекает на утечки
        setContentView(R.layout.activity_main_faculties);
        /* начало транзакции и установки фрагмента*/
        fm = getSupportFragmentManager();
        mFragment = (ListOfFaculties) fm.findFragmentByTag(FRAGMENT_INSTANCE_NAME);

        /* странная проверка, но трогать пока не буду*/
        if(mFragment == null) {
            mFragment = new ListOfFaculties();
            fm.beginTransaction().replace(R.id.mainInFacultiesFrameLayout, mFragment,FRAGMENT_INSTANCE_NAME).commit();
        }
    }

    /* Работает для кнопки назад для фрагментов */
    public void setOnBackPressedListener(FacultiesScreen.OnBackPressedListner onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    /* Очитска всего*/
    public void clearAll(){
        onBackPressedListener = null;
        fm.beginTransaction().remove(mFragment);
        fm = null;
        mFragment = null;
    }

    /* Срабатывает при завершении работы активити,
     вызывает метод очистки всех компонентов*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearAll();
    }

    /* Ну при нажатии на назад кнопку будет закрывать фрагменты
     или если они закрыты то вообще выйдет из активити*/
    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            /*тут для фрагментов назад работает*/
            onBackPressedListener.doBack();
            onBackPressedListener = null;
        } else {
            /*тут уже выходит из самой активити*/
            super.onBackPressed();
        }
    }
}
