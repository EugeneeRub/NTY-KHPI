package com.example.nty_hpi.StudentLife;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nty_hpi.R;

import static com.example.nty_hpi.R.id.view;

/**
 * Created by Евгений on 24.07.2017.
 * Фрагмент для отображения информации
 */
public class StudentLifeFragment extends Fragment {

    TextView tvfirsttext = null;// компонент для первого текста
    ImageView mImage1View = null;// компонент для первой картинки
    TextView toolbartext = null;// компонент для второго текста
    Bundle bundle = null;// компонент для первого текста

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_stud_life, null);
        intializeComponents(view);// Вызов метода для инициализации компонентов
        Log.d("tag","creating studentFragment");
        checkTitleAndSetInfoInComponents();// Вызов метода для проверки и запись в компоненты

        return view;
    }

    /**
     * Метод инициализации комонентов;
     */
    public void intializeComponents(View view){
        bundle = getArguments();
        toolbartext = (TextView) view.findViewById(R.id.tvInToolBarOnStudLife);
        tvfirsttext = (TextView) view.findViewById(R.id.tv1InStudLife);
        mImage1View = (ImageView) view.findViewById(R.id.imageViewInStudLife);
    }

    /**
     * Метод проверки и записи в компоненты;
     */
    public void checkTitleAndSetInfoInComponents(){
        if(bundle.getString("title").equals("Студенческая жизнь")){
            mImage1View.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else if(bundle.getString("title").equals("Профсоюзная организация студентов")) {
            mImage1View.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        toolbartext.setText(bundle.getString("title"));
        tvfirsttext.setText(bundle.getInt("text"));
        mImage1View.setImageResource(bundle.getInt("image"));
    }

    /**
     * Метод очистки, не очищает только bundle,
     * потому что он как-то странно себя ведет для меня;
     */
    void clearAll(){
        toolbartext = null;
        tvfirsttext = null;
        mImage1View = null;
    }

    /* Срабатывает при завершении работы активити; */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearAll();
    }
}
