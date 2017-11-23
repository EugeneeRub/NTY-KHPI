package com.example.nty_hpi.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nty_hpi.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by Eugenee Project on 01.06.2017, а то и раньше.
 * Фрагмент для работы с UI;
 */
public class MainPageActivity extends Fragment{

    YouTubePlayerSupportFragment mYouTubePlayerSupportFragment = null;// плеер для главного видоса
    FragmentTransaction transaction = null;// для работы с фрагментом плеера

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main_page,container,false);
        setRetainInstance(true);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        /* Нижние 4 строки для инициализации верхнего левого навигационного меню */
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).setTitle("НТУ " + "\""+"ХПИ"+"\"");
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_36dp);

        /*все что этот метод делает я не пойму,
            чето юлит мулит, плюс еще эрор в логах от него показывает*/
        callYouTubePlayer();

        return v;
    }
    /* Метод инициализации плеера ютубовского*/
    public void callYouTubePlayer(){
        /* Подключаем ютуб плеер */
        mYouTubePlayerSupportFragment = YouTubePlayerSupportFragment.newInstance();
        /* Начало транзакции для плеера */
        transaction = getChildFragmentManager().beginTransaction();
        /* Подключаем плеер во youtubeLayout */
        transaction.add(R.id.youtube_layout, mYouTubePlayerSupportFragment).commit();
        /* Работа плеера, непонятне кракозябры в первом аргументе это ключ api */
        mYouTubePlayerSupportFragment.initialize("AIzaSyDWQK8_P9zxljAjdwdwhhu0kQ2cja-AeS0",
                new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                if(!b){
                    /* Какой будет плеер, стоит простой */
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    /* Загрузка нужного видео */
                    youTubePlayer.cueVideo("3gI8TZnRcO8");
                }
                youTubePlayer.setPlayerStateChangeListener(mPlayerStateChangeListener);
                youTubePlayer.setPlaybackEventListener(mPlaybackEventListener);
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                String error = youTubeInitializationResult.toString();
                Log.d("tag",error);
            }
            /*Вообще страшная штука следующие два объекта*/
            YouTubePlayer.PlayerStateChangeListener mPlayerStateChangeListener
                    = new YouTubePlayer.PlayerStateChangeListener() {
                @Override
                public void onLoading() {

                }

                @Override
                public void onLoaded(String s) {

                }

                @Override
                public void onAdStarted() {

                }

                @Override
                public void onVideoStarted() {

                }

                @Override
                public void onVideoEnded() {

                }

                @Override
                public void onError(YouTubePlayer.ErrorReason errorReason) {

                }
            };
            YouTubePlayer.PlaybackEventListener mPlaybackEventListener
                    = new YouTubePlayer.PlaybackEventListener() {

                @Override
                public void onPlaying() {

                }

                @Override
                public void onPaused() {

                }

                @Override
                public void onStopped() {

                }

                @Override
                public void onBuffering(boolean b) {

                }

                @Override
                public void onSeekTo(int i) {

                }
            };
        });
    }

    /* Очитска всего*/
    void clearAll(){
        transaction.remove(mYouTubePlayerSupportFragment);
        transaction = null;
        mYouTubePlayerSupportFragment = null;
    }

    /* Пауза видео при выходе в фон приложения*/
    @Override
    public void onPause() {
        mYouTubePlayerSupportFragment.onPause();
        super.onPause();
    }
    /* Остановка видео при выходе в фон приложения*/
    @Override
    public void onStop() {
        mYouTubePlayerSupportFragment.onStop();
        super.onStop();
    }

    /* Срабатывает при завершении работы активити,
     вызывает метод очистки всех компонентов*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearAll();
    }
}
