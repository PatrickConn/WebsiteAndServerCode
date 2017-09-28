package poject.Pat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Offline_Players_View extends Activity {

	
	String pid;
	// Progress Dialog
	private ProgressDialog pDialog;
	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	String id2;
	private TextView CoachesName;
	private TextView phoneNumber;
	private TextView CoachesEmail;
	JSONArray array = null;
	private String agegroup;
	private boolean test;
	private String LOG_TAG;
	
	// JSON Node names
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_player_info);
		test = isNetworkAvailable();
		 if(test == true)
		 {
			 Intent in = new Intent(getApplicationContext(),MainActivity.class);	
			 startActivity(in);;
			 Toast.makeText(getApplicationContext(), "You have internet connection",
					   Toast.LENGTH_LONG).show();
		 }
		Intent i = getIntent();
		// getting Coaches id (id) from intent
		agegroup = i.getStringExtra("age");
		id2 = i.getStringExtra("id");
		new GetPlayersInfo().execute();
}//onCreate
	
	
	public void Back(View View) {
		 Intent i = new Intent(getApplicationContext(), Offline_Players.class);
		 i.putExtra("age", agegroup);	
		  startActivity(i);
	}
	public void Main(View View) {
		 Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
	}

class GetPlayersInfo extends AsyncTask<String, String, String> {

		private String name;
		private String number;
		private String email;
		private String Address;
		private String Age;


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Offline_Players_View.this);
			pDialog.setMessage("Loading Coaches details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		protected String doInBackground(String... params) {
				
			 try 
	            {            
	            	
					   String KEY = "players";
					   SharedPreferences pref = getApplicationContext().getSharedPreferences("players", 1);
		               //Getting the JSON from pref
		               String storedCollection = pref.getString(KEY, null);
		               //Parse the string to populate your collection.
		                array = new JSONArray(storedCollection);
		                         
		               for (int i = 0; i < array.length() ; i++) 
		               {
		                   JSONObject c = array.getJSONObject(i);           		
		                   // Storing each json item in variable   
		                   String id =  c.getString("Id");
		                   if (id.equalsIgnoreCase(id2)){
		                	   name = c.getString("Name");
								number = c.getString("Number");
								email = c.getString("Email");
								Address = c.getString("Address");
								Age = c.getString("Age");
								
		                  
		                   }
		                   // adding HashList to ArrayList
		               }
	            }
			  catch (JSONException e) 
	            {
	            }
			 return null;
					}//doInBackground
		
				protected void onPostExecute(String file_url) {
					CoachesName = (TextView) findViewById(R.id.playName);
					phoneNumber = (TextView) findViewById(R.id.playNumber);
					CoachesEmail = (TextView) findViewById(R.id.playEmail);	
					TextView address = (TextView) findViewById(R.id.Address);	
					TextView age = (TextView) findViewById(R.id.age);	
					CoachesName.setText(name);
					phoneNumber.setText(number);
					CoachesEmail.setText(email);
					address.setText(Address);
					age.setText(Age);
					pDialog.dismiss();
				}//onPostExecute
			}//GetPlayersInfo
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

