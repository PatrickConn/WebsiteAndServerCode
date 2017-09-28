package poject.Pat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import poject.Pat.R;
import poject.Pat.database;
import poject.matches.Main_matches;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class View_Coaches_start extends ActionBarActivity  {
	Spinner dropdown;
	String name;
	private boolean test;
	private String LOG_TAG;
	//database db = new database(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.view_teams);
	dropdown = (Spinner)findViewById(R.id.spinner1);
	String[] items = new String[]{"All","U7", "U8", "U9", "U10", "U11", "U13", "U15", "U16", "U19"};
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
	dropdown.setAdapter(adapter);
	 Intent myIntent = getIntent(); 
     name = myIntent.getStringExtra("Activity");
	}
	
	public void ViewTeams(View view) 
	{
		test = isNetworkAvailable();
		 if(test == true)
		 {
					if(name.equals("View_coaches")){	
						String text = dropdown.getSelectedItem().toString();
						Intent in = new Intent(getApplicationContext(),View_coaches.class);
						in.putExtra("age", text);		
						startActivity(in);;
					}
					if(name.equals("UpDate_Coache_View")){	
						
						  String text = dropdown.getSelectedItem().toString();
							Intent in = new Intent(getApplicationContext(),UpDate_Coache_View.class);
							in.putExtra("age", text);		
							startActivity(in);;
					}
					if(name.equals("View_player")){	
						 String text = dropdown.getSelectedItem().toString();
							Intent in = new Intent(getApplicationContext(),View_player.class);
							in.putExtra("age", text);		
							startActivity(in);;
					}
					if(name.equals("UpDate_Player_View")){	
						 String text = dropdown.getSelectedItem().toString();
							Intent in = new Intent(getApplicationContext(),UpDate_Player_View.class);
							in.putExtra("age", text);		
							startActivity(in);;
					}
		 }else {
			 Intent NEW = new Intent(this, MainActivity.class);
			  startActivity(NEW);
			  Toast.makeText(getApplicationContext(), "Sorry you have lost your internet connection",
					   Toast.LENGTH_LONG).show();
		}
	}
	public void Back(View view) 
	{
		test = isNetworkAvailable();
		 if(test == true)
		 {
		if(name.equals("View_coaches")){	
			Intent in = new Intent(getApplicationContext(),Mian_coaches.class);	
			startActivity(in);;
		}
		if(name.equals("UpDate_Coache_View")){	
				Intent in = new Intent(getApplicationContext(),Mian_coaches.class);	
				startActivity(in);;
		}
		if(name.equals("View_player")){	
				Intent in = new Intent(getApplicationContext(),Main_players.class);
				startActivity(in);;
		}
		if(name.equals("UpDate_Player_View")){	
				Intent in = new Intent(getApplicationContext(),Main_players.class);
				startActivity(in);;
		}
		if(name.equals("UpDate_Matches")){	
			Intent in = new Intent(getApplicationContext(),Main_matches.class);
			startActivity(in);;
		}
		if(name.equals("View_Matches")){	
			Intent in = new Intent(getApplicationContext(),Main_matches.class);
			startActivity(in);;
		}
		 }else {
			 Intent NEW = new Intent(this, MainActivity.class);
			  startActivity(NEW);
			  Toast.makeText(getApplicationContext(), "Sorry you have lost your internet connection",
					   Toast.LENGTH_LONG).show();
		}
		
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