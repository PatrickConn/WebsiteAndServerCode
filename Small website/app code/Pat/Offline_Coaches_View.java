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

public class Offline_Coaches_View extends Activity {

	
	String pid;
	// Progress Dialog
	private ProgressDialog pDialog;
	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	String id2;
	private TextView CoachesName;
	private TextView phoneNumber;
	private TextView CoachesEmail;
	private TextView CAge;
	private String age;
	 JSONArray array = null;
	private String agegroup;
	private boolean test;
	private String LOG_TAG;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_coaches_info);
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
		new GetCoachesInfo().execute();
}//onCreate
	
	
	public void Back(View View) {
		
		 Intent i = new Intent(getApplicationContext(), Offline_Coaches.class);
		 i.putExtra("age", agegroup);			
         startActivity(i);
	}
	public void Main(View View) {
		 Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
	}

class GetCoachesInfo extends AsyncTask<String, String, String> {

		private String name;
		private String number;
		private String email;


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Offline_Coaches_View.this);
			pDialog.setMessage("Loading Coaches details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		protected String doInBackground(String... params) {
			  try 
	            {            
	            	
					   String KEY = "test";
					   SharedPreferences pref = getApplicationContext().getSharedPreferences("test", 0);
		               //Getting the JSON from pref
		               String storedCollection = pref.getString(KEY, null);
		               //Parse the string to populate your collection.
		                array = new JSONArray(storedCollection);
		                         
		               for (int i = 0; i < array.length() ; i++) 
		               {
		                   JSONObject c = array.getJSONObject(i);           		
		                   // Storing each json item in variable   
		                   String id =  c.getString("id");
		                   if (id.equalsIgnoreCase(id2)){
		                   name = c.getString("Name");  
		                   number = c.getString("Number");  
		                   email = c.getString("Email");
		                   age = c.getString("Age");  
		                  
		                   }
		                   // adding HashList to ArrayList
		               }
	            }
			  catch (JSONException e) 
	            {
	                e.printStackTrace();
	            }
					return null;
					}//doInBackground
		
				protected void onPostExecute(String file_url) {
					CoachesName = (TextView) findViewById(R.id.Name5);
					phoneNumber = (TextView) findViewById(R.id.Number5);
					CoachesEmail = (TextView) findViewById(R.id.Email5);	
					CAge = (TextView) findViewById(R.id.TextView01);	
					CoachesName.setText(name);
					phoneNumber.setText(number);
					CoachesEmail.setText(email);	
					CAge.setText(age);
					pDialog.dismiss();
				}//onPostExecute
			}//GetCoachesInfo
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

