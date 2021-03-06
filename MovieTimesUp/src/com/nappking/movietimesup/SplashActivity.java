package com.nappking.movietimesup;

import java.util.ArrayList;

import com.facebook.android.friendsmash.R;
import com.nappking.movietimesup.notifications.NotificationService;
import com.nappking.movietimesup.task.DownloadMoviesTask;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.WindowManager;

/**
 * Initial Splash Screen to present the app and to take the chance to download movies if
 * there are new movies available.
 * 
 * @author Nappking - pdiego
 */
public class SplashActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        // Hide the notification bar
     	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new DownloadMoviesTask(this).execute();
        checkNotifications();
    }
    
    private void checkNotifications(){
    	/**
    	 * Ojo!! hay que recuperar de preferencias si tiene las notificaciones activadas y el per�odo
    	 */
		//SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);		
		//Recuperamos intervalo de consulta para las notificaciones
		int interval = 24; //hours
		
		//Servicio de Alarma
		AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
		
		//Clase Encargada de llamar a las distintas notificaciones
	    Intent i = new Intent(getBaseContext(), NotificationService.class);
	    
	    //Agregamos las distintas notificaciones
	    Bundle myBundle=new Bundle();
	    ArrayList<Integer> notifications = new ArrayList<Integer>();	    
	    notifications.add(NotificationService.MOVIEID);
	    myBundle.putSerializable(NotificationService.NOTIFICATIONS,notifications);
	    i.putExtras(myBundle);
	    PendingIntent pi = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
		am.cancel(pi);
		am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + interval*60*60*1000,
				interval*60*60*1000, pi);
	}
}
