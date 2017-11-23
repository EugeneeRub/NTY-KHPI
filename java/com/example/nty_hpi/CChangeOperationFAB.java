package com.example.nty_hpi;

import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

/**
 * Created by Eugenee Project
 * Класс для замены картинки и действий в FAB
 */
public class CChangeOperationFAB {

    FloatingActionButton fab;

    public CChangeOperationFAB(FloatingActionButton _fab) {
        fab = _fab;
    }

    /**
     * При зажатии фаба, происходит замена картинки и действия
     */
    public void startFABWork(){

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Integer integer = (Integer) fab.getTag();
                switch (integer) {
                    case R.drawable.ic_more_horiz_white_24dp:
                        fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        /**
                         * прячу в tag чтобы в clickListeners получить
                         * текущую картинку и уже работать с ней;
                         */
                        fab.setTag(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        Log.d("tag", "Change on up image");
                        break;
                    case R.drawable.ic_keyboard_arrow_up_black_24dp:
                        fab.setImageResource(R.drawable.ic_more_horiz_white_24dp);
                        fab.setTag(R.drawable.ic_more_horiz_white_24dp);
                        Log.d("tag", "Change on info image");
                        break;
                }
                return true;
            }
        });
    }
}
