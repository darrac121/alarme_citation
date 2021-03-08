package com.licence.projetalarme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Pour faire notre alarme manager
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;
    int choix_musique;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.context = this;

        // Initialisation de notre gestionnaire d'alarme
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Initialisation de notre horloge
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        // Initialisation du champ de texte qui se met à jour
        update_text = (TextView) findViewById(R.id.update_text);

        // Création d'une instance d'un calendrier
        Calendar calendar = Calendar.getInstance();



        //Initialisation du bouton start
        Button alarm_on = (Button) findViewById(R.id.alarm_on);

        //Création d'un intent pour la classe Alarme receiver
        Intent my_intent = new Intent(this.context, Alarm_Receiver.class);

        //Création d'un onClick listener pour lancer l'alarme
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Paramètrages de l'instance calendrier avec heure et minute sélectionnée
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                //Recupère la valeur en chaîne de caractère de l'heure et des minutes
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                //Conversion des variables int en string
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                /*if (hour > 12){
                    hour_string = String.valueOf(hour - 12);
                }*/

                if (minute <10){
                    //10:7 --> 10:07
                    minute_string = "0" + String.valueOf(minute);
                }
                // Méthode pour changer le texte
                set_alarm_text("Alarme réglé pour : " + hour_string + ":" + minute_string);

                // Put in extra sting into my_intent
                //Dit à l'alrme que nous avons appuyé sur On
                my_intent.putExtra("extra", "alarm on");

                //mettre le long dans intent pour que le spinner correspond
                my_intent.putExtra("musique", choix_musique);

                //Créer une requête de intent qui retarde le intent
                //Jusqu'au temps choisis sur le calendrier
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                //Règlage de l'alarme manager
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pending_intent);
            }
        });

        //creation du spinner dans le main citation
        Spinner spinner = (Spinner) findViewById(R.id.spinner_citation);
        //creer une liste pour le spinner dans string.xml dans le dosssier layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.citation_array, android.R.layout.simple_spinner_item);
        //spécifie élément du layout a utiliser
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //applique au spinner
        spinner.setAdapter(adapter);

        //creation du spinner musique dans le main
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner_musique);
        //creer une liste pour le spinner dans string.xml dans le dosssier layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.musique_array, android.R.layout.simple_spinner_item);
        //spicifie élément du layout a utiliser
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //applique au spinner
        spinner2.setAdapter(adapter2);

        // choisit la musique selection
        spinner2.setOnItemSelectedListener(this);


        //Initialisation du bouton stop
        Button alarm_off = (Button) findViewById(R.id.alarm_off);
        //Création d'un onClick listener pour stopper l'alarme
        alarm_off.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Méthode pour changer le texte
                set_alarm_text("Alarme désactivée !");

                //Annule l'alarme
                alarm_manager.cancel(pending_intent);

                // put extra string into my_intent
                //Dit à l'alarme que nous avons appuyé sur OFF
                my_intent.putExtra("extra", " alarm off");

                //mettre le long dans alarm aussi
                my_intent.putExtra("musique1", choix_musique);
                Log.e("la musique est", String.valueOf(choix_musique));


                //Stop la sonnerie
                sendBroadcast(my_intent);
            }
        });
    }

    private void set_alarm_text(String output) {
        update_text.setText(output);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       //joue la musique qu'on a choisit dans le spinner
        choix_musique = (int) id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}