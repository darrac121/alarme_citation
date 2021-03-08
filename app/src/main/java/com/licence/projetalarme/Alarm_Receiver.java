package com.licence.projetalarme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Alarm_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Nous somme receveur", "Excellent");

        // Fetch extra strings from the intent
        String get_your_string = intent.getExtras().getString("extra");

        Log.e("Quel est la clé ?", get_your_string);

        //fetch extra long dans intent
        Integer get_choix_musique = intent.getExtras().getInt("musique1");

        Log.e("l id musique est", Integer.toString(get_choix_musique));


        //Création d'un intent pour faire le service de sonnerie
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        // passe l'extra string du main activity au Ringtone Playing service
        service_intent.putExtra("extra", get_your_string);
        //choisit la musique par rapport au int du spinner
        service_intent.putExtra("musique1", get_choix_musique);


        //Lancement du service de sonnerie
        context.startService(service_intent);
    }
}
