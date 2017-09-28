package poject.Pat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



public class Main_Masseage extends ActionBarActivity {
	
	String login = "1";
	private String LOG_TAG;
	private boolean test;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_grope_chat);
		 String KEY = "login";
		 SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);
         //Getting the JSON from pref
        login = pref.getString(KEY, "0");
        
		}

	
	public void onClick1(View view) {
		test = isNetworkAvailable();
		 if(test == true){
				if(login.equals("0"))
				{
					 String text = "Add_maessage";
					  Intent NEW = new Intent(this, login.class);
					  NEW.putExtra("Activity", text);	
					  startActivity(NEW);
				}
				else if (login.equals("1")) {
					Intent NEW = new Intent(this, Group.class);
					  startActivity(NEW);
				}
		 }
		 else {
			 Intent NEW = new Intent(this, MainActivity.class);
			  startActivity(NEW);
			
		}
		 
	}
	

	public void onClick2(View view) {
		test = isNetworkAvailable();
		if(test == true){
			
		
		  Intent NEW = new Intent(this, View_chat.class);
		  startActivity(NEW);
		}
		else {
			 Intent NEW = new Intent(this, MainActivity.class);
			  startActivity(NEW);
			  Toast.makeText(getApplicationContext(), "Sorry you have lost your internet connection",
					   Toast.LENGTH_LONG).show();
		}	
	}
	public void onClick3(View view) {
		
		  Intent NEW = new Intent(this, MainActivity.class);
		  startActivity(NEW);
		
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
