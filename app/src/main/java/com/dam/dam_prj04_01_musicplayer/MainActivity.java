package com.dam.dam_prj04_01_musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    /** Ajout automatique de la lecture audio */

    private static final String TAG = "MainActivity";

    MediaPlayer mediaPlayer = new MediaPlayer();

    /*********** Méthods de fonctionnement de l'application ********************/
    public void play(View view){
        mediaPlayer.start();
        Log.i(TAG, "Lecture du morceau");
    }

    public void pause(View view){
        mediaPlayer.pause();
        Log.i(TAG, "sur pause");
    }

    public void volume(){
        //Associatyion de la seekabr au Java
        SeekBar sbVolume = findViewById(R.id.sbVolume);

        //Intialiser le manager en tant que service
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        //Volume max du terminal (pas notification, ni alarme, mais de la musique)
        int volumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //Valorisation de cette valeur au max de la seekbar
        sbVolume.setMax(volumeMax);

        //Définition du volume courant (celui réglé sur le terminal au démarrage de l'application)
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        sbVolume.setProgress(currentVolume);

        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(TAG, "onProgressChanged" + Integer.toString(progress));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    } //End Volume method


    private void position() {
        //Association de la seekabr au Java
        SeekBar sbPosition = findViewById(R.id.sbPosition);

        //Definir la valeur Max
        sbPosition.setMax(mediaPlayer.getDuration());

        //Part 1 gestion du déplacement du curseur par l'utilisateur
        sbPosition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(TAG, "position dans le morceau : " + Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pause(sbPosition);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(sbPosition.getProgress());
                play(sbPosition);
            }

        });

        // PART 02, Lors de la lecture de la musique, gestion du déplacement du curseur par l'application
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //Déplacement automatique
                sbPosition.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 100);  //Déplacement tous les 300 ms sans délai

    }

    /******************* Cycle de vie *****************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Methode 1 :
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        mediaPlayer.start(); */

        mediaPlayer = MediaPlayer.create(this, R.raw.sound);

        volume();
        position();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }
}
