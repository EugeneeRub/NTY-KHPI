package com.example.nty_hpi.Faculties;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.nty_hpi.CConteinerForList;
import com.example.nty_hpi.R;
import com.example.nty_hpi.WebLoad;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FacultiesScreen extends Fragment {

    // Компоненты для создания UI;
    TextView comissia;
    TextView nameOfDecan;
    TextView aboutFac;
    TextView name;
    ImageView photoDecanaURL;
    Button siteOfFac;
    Button openRepeatBtn;

    Integer counterForExeption = 0;

    ViewFlipper flipper;// Flipper используется для изменения вида при загрузке данных;

    View v = null;//Сама вьюшка для доступа к компоннентам;

    MyElementsThatNeedForInitialize myElements = new MyElementsThatNeedForInitialize();// Специально созданный класс для облегчения кода

    /* Часть компонентов для инициализации текстовой инфы в UI; */
    InputStream inputStream;
    Scanner scanner;
    StringBuilder builder;
    JSONObject object;
    JSONArray array;

    /* Часть компонентов для инициализации списка в BottomSheet; */
    CConteinerForList mConteiner = new CConteinerForList();

    BottomSheetBehavior mBehavior;// BottomSheet для списка кафедр;

    Button btnOpenCafedr;// Кнопка для открытия BottomShee;

    CoordinatorLayout clForImage;// Layout который используется для закрытия BottomSheet;

    CoordinatorLayout mCoordinatorLayout;// Layout который используется для инициализации BottomSheet(от чего он будет скакать);

    /* Знаю только что должен помогать при сохранении данных при поворотах, но чет не помогает(лучше наверное не трогать); */
    FacultiesScreen() {
        this.setRetainInstance(true);
    }

    /*
    * МЕТОД View onCreateView(...);
    *
    * Инициализируем наш View;
    * Вызываем метод initializePeremennih(v) для инициализации всех данных;
    * Открывается блок try-catch
    * Вызываем метод takeJSONFile(v) для записи данных в наши компоненты;
    * Вызываем метод createSpisForCafedr() для создания списка с именами кафедр;
    * Закрывается блок try-catch;
    * Проверка myElements.strUri, если ссылка пуста то выводит одно,
    * иначе запускает метод для открытия потока и загрузки данных;
    * Зе енд;
    * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_faculties_screen, container, false);
        initializePeremennih(v);
        try {
            takeJSONFile();
            setJSONInfoToObjects(); //Вызов метода для записи данных в объекты
            createSpisForCafedr();
        } catch (JSONException e) {
            counterForExeption++;
            e.printStackTrace();
        }
        /*Даже если выскочит ексепшин все будет нормально, просто не будет качать нужную инфу*/
        if ((myElements.strUri).equals("") || counterForExeption == 1) {
            flipper.showNext();// переход на следующий вид окна
            flipper.showNext();// переход на следующий вид окна
            aboutFac.setText("Данная информация отсутствует на сайте");
            (myElements.flag) = false;
        } else {
            startMethod();
        }

        AllClickListenersInThisClass();// Вызов метода который хранит в себе ClickListener-ы;
        return v;
    }

    /*
    * МЕТОД void initializePeremennih(View v);
    * Инициализация всех компонентов для дальнейшей работы;
    * Объявляем флаг как true;
    * */
    private void initializePeremennih(View v) {
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbarInFacultiesScreen);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainFaculties) getActivity()).setOnBackPressedListener(new BaseBackPressedListener(getActivity()));

        aboutFac = (TextView) v.findViewById(R.id.facultiesAboutText);
        comissia = (TextView) v.findViewById(R.id.facultiesTextComission);
        name = (TextView) v.findViewById(R.id.floatingTextInFacultiesToolBar);
        photoDecanaURL = (ImageView) v.findViewById(R.id.imageViewPeople);
        nameOfDecan = (TextView) v.findViewById(R.id.facultiesTextName);
        siteOfFac = (Button) v.findViewById(R.id.buttonSite);
        openRepeatBtn = (Button) v.findViewById(R.id.btnRepeatLoadInFacultiesScreen);

        btnOpenCafedr = (Button) v.findViewById(R.id.buttonCafedr);

        mCoordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.view);

        mBehavior = BottomSheetBehavior.from(mCoordinatorLayout);
        (mConteiner.mListView) = (ListView) v.findViewById(R.id.listForCafedr);
        clForImage = (CoordinatorLayout) v.findViewById(R.id.clForImage);

        flipper = (ViewFlipper) v.findViewById(R.id.viewflipper);

        (myElements.flag) = true;
    }

    /*
    * МЕТОД void AllClickListenersInThisClass();
    * В методе инициализируем все нужные OnClickListeners и даем им спокойно работать;
    **/
    public void AllClickListenersInThisClass() {

        btnOpenCafedr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        /* Кнопка для повторной проверки присутствия инета и запуска активити; */
        openRepeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMethod();
            }
        });
        /* Кнопка для открытия браузера; */
        siteOfFac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((myElements.urlOFSite) != "") {
                    Intent intent2 = new Intent(getActivity(), WebLoad.class);
                    intent2.putExtra("title", (myElements.nameOfFac));
                    intent2.putExtra("url", (myElements.urlOFSite));
                    startActivity(intent2);
                } else {
                    Toast.makeText(getActivity(), "нет сайта", Toast.LENGTH_SHORT);
                }
            }
        });
        /* При нажатии раскрывается BottomSheet; */
        clForImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        /* При нажатии на него будет открываться нужная кафедра; */
        (mConteiner.mListView).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new CafedryScreen();
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                (myElements.flag) = true;
                Bundle bundle = new Bundle();
                JSONObject object = null;
                try {
                    object = array.getJSONObject(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bundle.putParcelable("object", new ConteinerForCafedry(object));
                fragment.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.mainInFacultiesFrameLayout, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    /*
    * МЕТОД void takeJSONFile(View v);
    * В методе инициализируем все нужные OnClickListeners и даем им спокойно работать;
    * */
    public void takeJSONFile() {
        try {
            inputStream = getResources().openRawResource(R.raw.file);
            scanner = new Scanner(inputStream);
            builder = new StringBuilder();
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    * МЕТОД void setJSONtoObjects();
    * В методе записуются все данные из JSON файла в нужные нам объекты;
    * */
    public void setJSONInfoToObjects() {
        try {
            object = new JSONObject(builder.toString());
            JSONArray array = (JSONArray) object.getJSONArray("faculties");
            Bundle bundle = getArguments();
            (myElements.positionOfItem) = bundle.getInt("id");
            object = array.getJSONObject((myElements.positionOfItem));

            (myElements.urlOFSite) = (String) object.get("siteOfFac");
            (myElements.countOfBadText) = (int) object.get("countOfBadText");
            (myElements.nameOfFac) = (String) object.get("nameOfFacult");
            nameOfDecan.setText("Декан: " + (CharSequence) object.get("nameOfDecan"));
            getActivity().setTitle((myElements.nameOfFac));
            Picasso.with(v.getContext()).load((String) object.get("photoDecanaURL")).into(photoDecanaURL);
            comissia.setText("\t" + (String) object.get("comissia"));
            (myElements.strUri) = (String) object.get("aboutFac");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    * КЛАСС class myTask
    * Вся грязная работа для записи в компонент "О факультете";
    * Лучше читать код;
    * */
    public class myTask extends AsyncTask<Void, Void, Void> {
        String mString = "";
        String firstCheck = "[align=\"justify\"]";
        String secondCheck = "[style=\"text-align: justify;\"]";
        String thirdCheck = "[style=\"text-align: justify\"]";

        Conteiner mConteiner = new Conteiner();
        Elements content;
        org.jsoup.nodes.Document document;

        public myTask(Conteiner specialConteiner) {
            mConteiner.flipper = specialConteiner.flipper;
            mConteiner.urlSite = specialConteiner.urlSite;
            mConteiner.countOfBadText = specialConteiner.countOfBadText;
            mConteiner.nameOfFac = specialConteiner.nameOfFac;
        }

        @Override
        protected Void doInBackground(Void... params) {
            document = null;
            try {
                Log.d("tag", "Перед коннектом");
                document = Jsoup.connect(mConteiner.urlSite)
                        .ignoreContentType(true)
                        .followRedirects(true)
                        .post();
                Log.d("tag", "После коннекта");
                content = document.select(firstCheck);

                    /* Проверка для использования Стринг чеков;*/
                if (content.size() == 0) {
                    content = null;
                    content = document.select(secondCheck);
                }
                if ((mConteiner.nameOfFac).equals("Электроэнергетический (Э)")) {
                    content = null;
                    content = document.select(secondCheck);
                }
                if ((mConteiner.nameOfFac).equals("Электромашиностроительный (ЭМС)")) {
                    content = null;
                    content = document.select(thirdCheck);
                }

                // Цикл для записи в стринг;
                for (Element element : content) {
                    if ((mConteiner.countOfBadText) == 0) {
                        mString += "\t" + element.text();
                        mString += '\n';
                        Log.d("tag", "Adding text");
                    } else if ((mConteiner.countOfBadText) == 1) {
                        (mConteiner.countOfBadText)--;
                    } else if ((mConteiner.countOfBadText) == 2) {
                        (mConteiner.countOfBadText)--;
                    } else if (mConteiner.countOfBadText == 3) {
                        (mConteiner.countOfBadText)--;
                    } else if ((mConteiner.countOfBadText) == 4) {
                        (mConteiner.countOfBadText)--;
                    }
                }
                (myElements.flag) = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mString != null){
                aboutFac.setText(mString);
            }
            (mConteiner.flipper).showNext();

            /*Будет начинаться двигаться текст*/
            try {
                name.setText((String) object.get("nameOfFacult"));
                if (name.getText().toString().length() > 30) {
                    timerForFloatingText timerForFloatingText = new timerForFloatingText();
                    timerForFloatingText.execute();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mString = null;

            mConteiner.countOfBadText = 0;
            mConteiner.flipper = null;
            mConteiner.nameOfFac = null;
            mConteiner.urlSite = null;
            mConteiner = null;

            document = null;
            if(content != null){
                content.remove();
                content = null;
            }

        }

        @Override
        protected void onCancelled() {
            // Эта собацюра не хочет работать
            super.onCancelled();
        }
    }

    /*
    * МЕТОД void createSpisForCafedr();
    * Самое обычное создание списка;
    **/
    private void createSpisForCafedr() throws JSONException {
        final String TEXT = "text";
        (mConteiner.data) = new ArrayList<Map<String, String>>();
        array = (JSONArray) object.getJSONArray("cafedri");


        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            (mConteiner.m) = new HashMap<String, String>();
            (mConteiner.m).put(TEXT, (String) obj.get("nameOfCafedry"));
            (mConteiner.data).add((mConteiner.m));
        }

        String[] from = {TEXT};
        int[] to = {R.id.text_item_for_bottom_sheet};
        (mConteiner.adapter) = new SimpleAdapter(getActivity(), (mConteiner.data), R.layout.item_for_bottom_sheet, from, to);
        (mConteiner.mListView).setAdapter((mConteiner.adapter));

    }

    // Интерфейс для кнопки Back (специально для фрагментов);
    public interface OnBackPressedListner {
        public void doBack();
    }

    // Сама работа для кнопки Back в переопределенном методе doBack
    public class BaseBackPressedListener implements OnBackPressedListner {
        FragmentActivity activity;

        public BaseBackPressedListener(FragmentActivity fragmentActivity) {
            this.activity = fragmentActivity;
        }

        @Override
        public void doBack() {
            Log.d("tag", "Stop Async Task");
            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                if ((myElements.flag) == false) {
                    (myElements.flag) = true;
                    activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        }
    }

    /*
     * Специальный контейнер для хранения инфы
     * */
    class Conteiner {
        public Conteiner(ViewFlipper flipper, String urlSite, int countOfBadText, String nameOfFac) {
            this.flipper = flipper;
            this.urlSite = urlSite;
            this.countOfBadText = countOfBadText;
            this.nameOfFac = nameOfFac;
        }

        public Conteiner() {
            flipper = null;
            urlSite = "";
            countOfBadText = 0;
            nameOfFac = "";
        }

        ViewFlipper flipper;
        String urlSite;
        int countOfBadText;
        String nameOfFac;
    }

    /*
    * Похоже делал в алкогольном опьянении(Хотя не пью)
    * Специально сделал для того чтобы можно было отправлять обьект через фрагменты;
    * Тут все данные для CafdryScreen.java которые он получит;
    * */
    class ConteinerForCafedry implements Parcelable {

        public String nameOfCafedry = "";
        public String nameOfZavCafedry = "";
        public String photoZavCafedryURL = "";
        public String siteOfCafedry = "";
        public String aboutFac = "";
        public int countOfBadText = 0;

        protected ConteinerForCafedry(Parcel in) {
        }

        public final Creator<ConteinerForCafedry> CREATOR = new Creator<ConteinerForCafedry>() {
            @Override
            public ConteinerForCafedry createFromParcel(Parcel in) {
                return new ConteinerForCafedry(in);
            }

            @Override
            public ConteinerForCafedry[] newArray(int size) {
                return new ConteinerForCafedry[size];
            }
        };

        public ConteinerForCafedry(JSONObject object) {
            try {
                nameOfCafedry = (String) object.get("nameOfCafedry");
                nameOfZavCafedry = (String) object.get("nameOfZavCafedry");
                photoZavCafedryURL = (String) object.get("photoZavCafedryURL");
                siteOfCafedry = (String) object.get("siteOfCafedry");
                aboutFac = (String) object.get("aboutFac");
                countOfBadText = (int) object.get("countOfBadTextInCafedr");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeStringArray(new String[]{nameOfCafedry, nameOfZavCafedry, photoZavCafedryURL,
                    siteOfCafedry, aboutFac});// Непонятно что творит
        }
    }

    /* МЕТОД void startMethod();
     * Пороверяет подключение вызывая метод isOnline();
     * Запускает поток если есть инет, иначе нужно повторять механизм вызова;
     **/
    void startMethod() {
        //WebLoad.CGetInetConection conection = new WebLoad.CGetInetConection();
        if (isOnline() == true) {
            flipper.showNext();
            /* Запуск потока */
            Conteiner conteiner = new Conteiner(flipper, (myElements.strUri), (myElements.countOfBadText), (myElements.nameOfFac));
            new myTask(conteiner).execute();
        }
    }

    /* МЕТОД boolean isOnline();
     * Получает от устройства текущее подключение к интернету;
     * Возвращает результат, тру если есть инет, фолс если нет;
     **/
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /*Метод для полной очистки перед удалеием мвьюшки*/
    void clearAll(){
        comissia = null;
        nameOfDecan = null;
        aboutFac = null;
        name = null;
        photoDecanaURL = null;
        siteOfFac = null;
        openRepeatBtn = null;

        flipper.removeAllViews();
        flipper = null;

        v.destroyDrawingCache();
        v = null;

        inputStream = null;
        builder = null;

        object = null;
        array = null;

        (mConteiner.adapter) = null;
        (mConteiner.mListView) = null;
        (mConteiner.data) = null;

        btnOpenCafedr = null;

        clForImage.destroyDrawingCache();
        clForImage.removeAllViews();
        clForImage = null;

        mCoordinatorLayout.destroyDrawingCache();
        mCoordinatorLayout.removeAllViews();
        mCoordinatorLayout = null;
    }

    /* КЛАСС class MyElementsThatNeedForInitialize;
     * Хранятся данные для работы с компонентами;
     **/
    class MyElementsThatNeedForInitialize {
        String strUri = "";// Ссылка для получение инфы "О факультете";
        boolean flag;// Флаг для того чтобы нельзя было нажимать кнопку Back;
        String urlOFSite;// Ссылка для
        int positionOfItem;// Позиция нахождения объекта
        int countOfBadText;// Кол-во плохих строк которые пропустяться
        String nameOfFac;// Переменная для хранения названия факультета
    }

    /* Поток-класс timerForFloatingText;
     * Просто делает паузу для того чтобы пользователь мог увидеть,
     * как текст начинает ползти после запуска активити;
     **/
    class timerForFloatingText extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            /*Будет начинаться двигаться текст*/
            name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            name.setSingleLine(true);
            name.setMarqueeRepeatLimit(-1);
            name.setSelected(true);
        }
    }

    /* Очищаем все гадость*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearAll();
    }
}

