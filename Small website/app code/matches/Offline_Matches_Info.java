package poject.matches;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import poject.Pat.JSONParser;
import poject.Pat.MainActivity;
import poject.Pat.R;
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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Offline_Matches_Info extends Activity {

	private String Age;

	String[] items;
	private Spinner dropdown;
	ArrayAdapter<String> adapter;
	// Progress Dialog
    JSONArray result= null;
    JSONArray array = null;
	private TextView addname;
	private TextView soccer1;
	private TextView soccer2;
	private String name = "temp";
	private String Aresoccer;
	private String Theresoccer;
	private String year;
	private String id;
	private String month;
	private String day;
	TextView Date;
	private TextView agegrop;
	String pid;
	// Progress Dialog
	private ProgressDialog pDialog;
	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	String id2;
	
	private String age;

	private String agegroup;

	private String LOG_TAG;

	private boolean test;
	

	// JSON Node names
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_matches_info);
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
		id2 = i.getStringExtra("id");
		agegroup = i.getStringExtra("age");
		
		new GetCoachesInfo().execute();
}//onCreate
	
	
	public void Back(View View) {
		 Intent i = new Intent(getApplicationContext(), Offline_Matches.class);
		 i.putExtra("age", agegroup);	
		 startActivity(i);
	}
	public void Main(View View) {
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
	}

class GetCoachesInfo extends AsyncTask<String, String, String> {

		


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Offline_Matches_Info.this);
			pDialog.setMessage("Loading Matches details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		protected String doInBackground(String... params) {
				
			 try 
	            {            
	            	
				 String KEY = "matches";
                 
                 SharedPreferences pref = getApplicationContext().getSharedPreferences("matches", 3);
                 //Getting the JSON from pref
                 String storedCollection = pref.getString(KEY, null);
                 //Parse the string to populate your collection.
                  array = new JSONArray(storedCollection);
                           
		               for (int i = 0; i < array.length() ; i++) 
		               {
		                   JSONObject c = array.getJSONObject(i);           		
		                   // Storing each json item in variable   
		                   id =  c.getString("Id");
		                   if(id2.equalsIgnoreCase(id))
		                   {
		                	   
		                         name = c.getString("Name");  
		                         Aresoccer = c.getString("Aresoccer");  
		                         Theresoccer = c.getString("Theresoccer");
		                         age = c.getString("Age");  
		                         year = c.getString("Year");  
		                         month = c.getString("Month");  
		                         day = c.getString("Day");  
								
		                  
		                   }
		                   // adding HashList to ArrayList
		               }
	            }
					catch (JSONException e) 
					{	e.printStackTrace();	}//catch
				return null;
				}//doInBackground
	
			protected void onPostExecute(String file_url) {
				addname = (TextView) findViewById(R.id.MName);
				agegrop = (TextView) findViewById(R.id.MAge);
				soccer1 = (TextView) findViewById(R.id.MOurTeam);
				soccer2 = (TextView) findViewById(R.id.MThereTeam);
				Date = (TextView) findViewById(R.id.MDate);
				addname.setText(name);
				agegrop.setText(age);
				soccer1.setText(Aresoccer);
				soccer2.setText(Theresoccer);
				String temp = "-";
				String date = null;
				date = day;
				date += temp;
				date += month;
				date += temp;
				date += year;
				Date.setText(date);	
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


