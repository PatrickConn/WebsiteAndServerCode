package poject.Pat;



import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import poject.matches.Main_matches;
import poject.matches.Offline_matches_start;
import poject.matches.View_matches;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;



public class MainActivity extends ActionBarActivity {
private static String LOG_TAG;
boolean test;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	
		}

	 public void onClick1(View view) {
			PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("login", "0").commit();
			
		 test = isNetworkAvailable();
		 if(test == false)
		 {
			  String text = "coaches";
			 Intent in = new Intent(getApplicationContext(),Offline_Start.class);
		   in.putExtra("age", text);		
			startActivity(in);;
			 Toast.makeText(getApplicationContext(), "Sorry you have no internet connection",
					   Toast.LENGTH_LONG).show();
		 }
		 else {
			 Intent NEW = new Intent(this, Mian_coaches.class);
			  startActivity(NEW);
			 
		}
		  }

	public void onClick2(View view) {
		PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("login", "0").commit();
		
		test = isNetworkAvailable();
		 if(test == false){
			  String text = "player";
				 Intent in = new Intent(getApplicationContext(),Offline_Start.class);
			   in.putExtra("age", text);		
				startActivity(in);;
				 Toast.makeText(getApplicationContext(), "Sorry you have no internet connection",
						   Toast.LENGTH_LONG).show();
		 }
		else {
			  Intent NEW = new Intent(this, Main_players.class);
			  startActivity(NEW);
			 
		}
	}
	public void onClick3(View view) {
		PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("login", "0").commit();
		
		test = isNetworkAvailable();
		 if(test == false){
			  String text = "Offline";
				 Intent in = new Intent(getApplicationContext(),Offline_matches_start.class);
			   in.putExtra("Activity", text);		
				startActivity(in);;
				 Toast.makeText(getApplicationContext(), "Sorry you have no internet connection",
						   Toast.LENGTH_LONG).show();
		 }
		 else {
			 Intent NEW = new Intent(this, Main_matches.class);
			  startActivity(NEW);
			 
		}
	}
	public void onClick8(View view) {
		PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("login", "0").commit();
		
		test = isNetworkAvailable();
		 if(test == false){
			  String text = "group";
				 Intent in = new Intent(getApplicationContext(),Offline_chat.class);
			   in.putExtra("age", text);		
				startActivity(in);;
				 Toast.makeText(getApplicationContext(), "Sorry you have no internet connection",
						   Toast.LENGTH_LONG).show();
		 }
		 else {
			 Intent NEW = new Intent(this, Main_Masseage.class);
			  startActivity(NEW);
			 
		}
	}
	public void LogOut(View view) {
				String KEY = "login";
                // Getting Array of Coaches
               
                 //looping through All Coaches
                String result = "0";
                SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);

                //Storing the string in pref file
                SharedPreferences.Editor prefEditor = pref.edit();
                prefEditor.putString(KEY, result);
                prefEditor.commit();
                Toast.makeText(getApplicationContext(), "You have log out",
                		   Toast.LENGTH_LONG).show();
				}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	public boolean hasActiveInternetConnection() {
	    if (isNetworkAvailable()) {
	        try {
	            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
	            urlc.setRequestProperty("User-Agent", "Test");
	            urlc.setRequestProperty("Connection", "close");
	            urlc.setConnectTimeout(1500); 
	            urlc.connect();
	            return (urlc.getResponseCode() == 200);
	        } catch (IOException e) {
	            Log.e(LOG_TAG, "Error checking internet connection", e);
	        }
	    } else {
	        Log.d(LOG_TAG, "No network available!");
	    }
	    return false;
	}
	
}
