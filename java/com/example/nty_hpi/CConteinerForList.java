package com.example.nty_hpi;

import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Eugenee Project on 30.07.2017.
 * Сделал для упращения кода, 4 этих компонента используются в 60% кода
 */

public class CConteinerForList{

    public CConteinerForList(){
        mListView = null;
        adapter = null;
        data = null;
        m = null;
    }

    public ListView mListView;// самый главный компонент создания списка
    public SimpleAdapter adapter;// адаптер для его записи
    public ArrayList<Map<String, String>> data;// коллекция для хранения
    public Map<String,String> m;// коллекция для промежуточных данных
}
