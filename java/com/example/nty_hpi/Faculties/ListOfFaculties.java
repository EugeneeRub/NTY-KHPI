package com.example.nty_hpi.Faculties;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.nty_hpi.CConteinerForList;
import com.example.nty_hpi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ListOfFaculties extends Fragment {

    ListOfFaculties(){
        this.setRetainInstance(true);
    }// должно помочь при поворотах

    final String TEXT= "text";// перемменная для создания списка
    View v;// вьюшка для работы с компонентами

    CConteinerForList myElementsInListConteiner = new CConteinerForList();// хранит компоненты по списку

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_list_of_faculties, null);
        setHasOptionsMenu(true);
        intializeComponents();// вызов метода инициализации компонентов
        createListFromJsonFile();// вызов метода создания списка

        AllClickListenersInThisClass();// вызов метода ClickListener-ов
        return v;
    }

    /* Метод инициализации компонентов*/
    private void intializeComponents(){
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbarInListOfActivity);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setTitle("Факультеты");

        myElementsInListConteiner.data = new ArrayList<Map<String,String>>();
    }

    /* Метод создания списка*/
    private void createListFromJsonFile(){
        /* Проходим по файлу получая названия факультетов и записываем их в список*/
        InputStream inputStream = getResources().openRawResource(R.raw.file);
        Scanner scanner = new Scanner(inputStream);
        final StringBuilder builder = new StringBuilder();
        while(scanner.hasNextLine()){
            builder.append(scanner.nextLine());
        }
        try {
            JSONObject object = new JSONObject(builder.toString());
            JSONArray array = (JSONArray) object.getJSONArray("faculties");
            /*Создание списка*/
            for(int i = 0; i < array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                (myElementsInListConteiner.m) = new HashMap<String,String>();
                (myElementsInListConteiner.m).put(TEXT,(String) obj.get("nameOfFacult"));
                (myElementsInListConteiner.data).add((myElementsInListConteiner.m));
            }
            String []from = {TEXT};
            int []to = {R.id.text_item};
            (myElementsInListConteiner.adapter) = new SimpleAdapter(getActivity(),(myElementsInListConteiner.data),R.layout.list_item,from,to);
            (myElementsInListConteiner.mListView) = (ListView) v.findViewById(R.id.listOfFac);
            (myElementsInListConteiner.mListView).setAdapter((myElementsInListConteiner.adapter));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* Метод для ClickListener-ов*/
    private void AllClickListenersInThisClass(){
        /* Реакция при нажатии на элемент списка */
        (myElementsInListConteiner.mListView).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new FacultiesScreen();
                Bundle bundle = new Bundle();
                /* Запись в хранилище позиции для дальнейшего использования во вызванном фрагменте */
                TextView tv = (TextView) view.findViewById(R.id.text_item);
                String str = tv.getText().toString();

                /* цикл здесь не просто так, он был добавлен только из-за поиска,
                * потому что, не хотел выдавать нужный id списка, пришлось сдлать так*/
                for (int i = 0; i < (myElementsInListConteiner.data).size(); i++){
                    Map<String, String> timeObj = (myElementsInListConteiner.data).get(i);
                    String newString = timeObj.get(TEXT);
                    if(str.equals(newString)){
                        bundle.putInt("id",i);
                        break;
                    }
                }
                /* Подтврждение записи */
                fragment.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.mainInFacultiesFrameLayout,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    /* Создано для поиска по элементам списка */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_for_faculties, menu);

        MenuItem menuItem = menu.findItem(R.id.faculties_menu_search);
        SearchView searchView = new SearchView(getActivity());
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                (myElementsInListConteiner.adapter).getFilter().filter(newText);
                return false;
            }
        });
        menuItem.setActionView(searchView);
    }

    /*Полная очитска перед заакрытием фрагмента*/
    public void clearAll(){
        v.destroyDrawingCache();
        v = null;
        (myElementsInListConteiner.mListView) = null;
        (myElementsInListConteiner.adapter) = null;
        (myElementsInListConteiner.data) = null;
        (myElementsInListConteiner.m) = null;
    }

    /*Отрабатывает за момент до закрытия */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearAll();
    }

}