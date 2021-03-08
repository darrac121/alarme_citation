package com.licence.projetalarme;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Random;

public class RingtonePlayingService extends Service {

    MediaPlayer media_song;
    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // fetch la valeur extra
        String state = intent.getExtras().getString("extra");
        //fetch la musique
        Integer musique_id = intent.getExtras().getInt("musique1");


        Log.e("Ringtone state:extra is", state);
        Log.e("musique1 est ", musique_id.toString());

        // Converti la valeur extra de intent
        assert  state !=null;
        switch (state){
            case "alarm on" :
                startId = 1;
                break;
            case "alarm off" :
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        //if else statements
        //S'il n'y a pas de sonnerie en cours et que l'utilisateur appuie sur ON
        //La sonnerie se lance
        if (!this.isRunning && startId == 1) {
            Log.e("Il n'y a pas de musique", "Commencé");



            this.isRunning = true;
            this.startId = 0;

            int NOTIFICATION_ID = 234;



            // joue la musique choisit dans le spinner
            if (musique_id == 0){
                media_song = MediaPlayer.create(this, R.raw.sonnerie1);
                media_song.start();


            }
            else if (musique_id == 1){
                //Création d'une instance de media player
                media_song = MediaPlayer.create(this, R.raw.sonnerie1);
                // Lance la sonnerie
                media_song.start();
            }
            else if (musique_id == 2){
                media_song = MediaPlayer.create(this, R.raw.sonnerie2);
                media_song.start();
            }
            else if (musique_id == 3){
                media_song = MediaPlayer.create(this, R.raw.sonnerie3);
                media_song.start();
            }

            //Mise en place du service de notification
            NotificationManager notify_manager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                String CHANNEL_ID = "my_channel_01";
                CharSequence name = "my_channel";
                String Description = "This is my channel";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                notify_manager.createNotificationChannel(mChannel);

                //Mise en place d'un intent qui ira dans Main activity
                Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);

                //Mise en place d'une requête
                PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0,
                        intent_main_activity, 0);


                //Paramètrage des notifications
                Notification notification_popup = new Notification.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Une alarme sonne")
                        .setContentText("Cliquez dessus !")
                        .setContentIntent(pending_intent_main_activity)
                        .setAutoCancel(true)
                        .build();

                notify_manager.notify(0, notification_popup);
            }
        }

        //S'il y a une sonnerie en cours et que l'utilisateur appuie sur OFF
        //la sonnerie s'arrête
        else if (this.isRunning && startId == 0) {
            Log.e("Il y a de la musique", "Fin");

            //Arrête la sonnerie
            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;
        }

        // Cas où l'utilisateur appuye sur des boutons aléatoirement
        //S'il n'y a pas de sonnerie et que l'utilisateur appuie sur OFF
        // Ne rien faire
        else if (!this.isRunning && startId == 0) {
            Log.e("Il y a de la musique", "Commencé");

            this.isRunning = false;
            this.startId = 0;

        }
        // S'il y a une sonnerie en cours et que l'utilisateur appuie sur ON
        // Ne rien faire
        else if (this.isRunning && startId == 1) {
            Log.e("Il y a de la musique", "Fin");

            this.isRunning = true;
            this.startId = 1;
        }
        // Récupère les événements
        else {
            Log.e("Il y a de la musique", "Fin");
        }



        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        //Fermeture de l'application

        super.onDestroy();
        this.isRunning = false;
    }



}