package com.example.nty_hpi.Megdyn_Svazy;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nty_hpi.R;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Eugenee Project on 19.07.2017.
 * Фрагмент для работы с UI
 */
public class SvazyFragment extends Fragment {

    TextView tvfirsttext = null;// первый текст
    TextView tvsecondtext = null;// второй текст
    ImageView mImage1View = null;// третий текст
    TextView toolbartext = null;// четвертый текст

    TextView tvthirdtext = null;// первая картинка
    TextView tvfouthtext = null;// вторая картинка
    ImageView mImage2View = null;// третья картинка
    ImageView mImage3View = null;// четвертая картинка

    Bundle bundle = null;// бандл для хранения инфы

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_megdynar_svajzy,null);
        intializeComponents(v);// Вызов метода для инициализации всех компонентов
        setInTextAndImage();// Вызов метода для загрузки компонентов
        toolbartext.setText(bundle.getString("toolbarText"));

        return v;
    }

    /**
     * Метод инициализации компонентов
     */
    public void intializeComponents( View v){
        bundle = getArguments();

        toolbartext = (TextView) v.findViewById(R.id.tvInToolBarOnSA);
        tvfirsttext = (TextView) v.findViewById(R.id.tv1InMegdynarSvazy);

        mImage1View = (ImageView) v.findViewById(R.id.imageViewInMegdynarSvazy);
        mImage1View.setImageResource(R.drawable.intrel);
        mImage1View.setScaleType(ImageView.ScaleType.CENTER_CROP);
        tvsecondtext = (TextView) v.findViewById(R.id.tv2InMegdynarSvazy);

        mImage2View = (ImageView) v.findViewById(R.id.SecondimageViewInMegdynarSvazy);
        tvthirdtext = (TextView) v.findViewById(R.id.tv3InMegdynarSvazy);
        mImage3View = (ImageView) v.findViewById(R.id.ThirdimageViewInMegdynarSvazy);
        tvfouthtext = (TextView) v.findViewById(R.id.tv4InMegdynarSvazy);
    }

    /**
     * Метод создания интерфейса
     */
    void setInTextAndImage(){
        int currentId = bundle.getInt("id");

        /*Говорим что перед началом размер текста
         и ресурс картинки будет равен 0, чтобы не зосарять экран*/
        tvfirsttext.setTextSize(0);
        tvsecondtext.setTextSize(0);
        tvthirdtext.setTextSize(0);
        tvfouthtext.setTextSize(0);

        mImage1View.setImageResource(0);
        mImage2View.setImageResource(0);
        mImage3View.setImageResource(0);

        int display_mode = getResources().getConfiguration().orientation;
        Log.d("tag","start working SvazyFragment");

        /* Происходит выбор для запуска нужного метода, который будет создавать нужный UI */
        switch (currentId){
            case 0:
                zeroID();
                break;
            case 1:
                firstID(display_mode);// display_mode для проверки положения экрана
                break;
            case 2:
                secondID(display_mode);// display_mode для проверки положения экрана
                break;
            case 5:
                fifthID(display_mode);// display_mode для проверки положения экрана
                break;
            case 6:
                sixthID();
                break;
            case 7:
                seventhID();
                break;
            case 8:
                eightID(display_mode);// display_mode для проверки положения экрана
                break;
        }
    }

    /*начало методов для switch-case*/
    public void zeroID() {
        tvfirsttext.setTextSize(14);
        tvsecondtext.setTextSize(14);
        tvfirsttext.setText(bundle.getInt("text1"));
        mImage1View.setImageResource(bundle.getInt("image1"));
        tvsecondtext.setText(bundle.getInt("text2"));
    }
    //--------------------------------------------------------------------------------------------//
    public void firstID(int display_mode) {
        tvsecondtext.setTextSize(14);

        if (display_mode == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("tag", "change image config in ORIENTATION_LANDSCAPE");
            mImage1View.getLayoutParams().height = 500;
            mImage1View.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            Log.d("tag", "change image config in ORIENTATION_PORTREIT");
            mImage1View.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        toolbartext.setText(bundle.getString("toolbarText"));
        mImage1View.setImageResource(bundle.getInt("image1"));
        tvsecondtext.setText(bundle.getInt("text2"));
    }
    //--------------------------------------------------------------------------------------------//
    public void secondID(int display_mode) {
        if(display_mode == Configuration.ORIENTATION_LANDSCAPE){
            Log.d("tag","change image config in ORIENTATION_LANDSCAPE");
            mImage1View.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else{
            Log.d("tag","change image config in ORIENTATION_PORTREIT");
            mImage1View.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        tvfirsttext.setTextSize(14);
        tvsecondtext.setTextSize(14);
        tvthirdtext.setTextSize(14);
        tvfouthtext.setTextSize(14);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvthirdtext.getLayoutParams();
        layoutParams.setMargins(14,0,14,14);
        tvthirdtext.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) tvfouthtext.getLayoutParams();
        layoutParams2.setMargins(14,14,14,14);
        tvfouthtext.setLayoutParams(layoutParams2);

        LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) mImage3View.getLayoutParams();
        layoutParams3.setMargins(16,0,16,0);
        mImage3View.setLayoutParams(layoutParams3);

        toolbartext.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        toolbartext.setSingleLine(true);
        toolbartext.setMarqueeRepeatLimit(-1);
        toolbartext.setSelected(true);

        mImage1View.setImageResource(bundle.getInt("image1"));

        mImage2View.setImageResource(bundle.getInt("image2"));
        mImage3View.setImageResource(bundle.getInt("image3"));
        tvfirsttext.setText(bundle.getInt("text1"));
        tvsecondtext.setText(bundle.getInt("text2"));
        tvthirdtext.setText(bundle.getInt("text3"));
        tvfouthtext.setText(bundle.getInt("text4"));
    }
    //--------------------------------------------------------------------------------------------//
    public void fifthID(int display_mode) {
        tvsecondtext.setTextSize(14);

        if(display_mode == Configuration.ORIENTATION_LANDSCAPE){
            Log.d("tag","change image config in ORIENTATION_LANDSCAPE");
            mImage1View.getLayoutParams().height = 550;
            mImage1View.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else{
            Log.d("tag","change image config in ORIENTATION_PORTREIT");
            mImage1View.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        mImage1View.setImageResource(bundle.getInt("image1"));
        tvsecondtext.setText(bundle.getInt("text2"));
    }
    //--------------------------------------------------------------------------------------------//
    public void sixthID () {
        tvfirsttext.setTextSize(14);

        toolbartext.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        toolbartext.setSingleLine(true);
        toolbartext.setMarqueeRepeatLimit(-1);
        toolbartext.setSelected(true);

        tvfirsttext.setText(bundle.getInt("text1"));
    }
    //--------------------------------------------------------------------------------------------//
    public void seventhID() {
        tvfirsttext.setTextSize(14);

        toolbartext.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        toolbartext.setSingleLine(true);
        toolbartext.setMarqueeRepeatLimit(-1);
        toolbartext.setSelected(true);

        tvfirsttext.setText(bundle.getInt("text1"));
    }
    //--------------------------------------------------------------------------------------------//
    public void eightID(int display_mode){
        tvsecondtext.setTextSize(14);
        if(display_mode == Configuration.ORIENTATION_LANDSCAPE){
            Log.d("tag","change image config in ORIENTATION_LANDSCAPE");
            mImage1View.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else{
            Log.d("tag","change image config in ORIENTATION_PORTREIT");
            mImage1View.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        mImage1View.setImageResource(bundle.getInt("image1"));
        tvsecondtext.setText(bundle.getInt("text2"));
    }
    /*конец методов для switch-case*/

    /* Очитска всего*/
    void clearAll(){

        tvfirsttext = null;
        tvsecondtext = null;
        mImage1View = null;
        toolbartext = null;
        tvthirdtext = null;
        tvfouthtext = null;
        mImage2View = null;
        mImage3View = null;
    }

    /* Срабатывает при завершении работы активити*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearAll();
    }
}
