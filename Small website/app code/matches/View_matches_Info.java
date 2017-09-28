package poject.matches;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import poject.Pat.JSONParser;
import poject.Pat.MainActivity;
import poject.Pat.Mian_coaches;
import poject.Pat.R;
import poject.Pat.View_Coaches_start;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class View_matches_Info extends Activity {

	private String Age;

	String[] items;
	private Spinner dropdown;
	ArrayAdapter<String> adapter;
	// Progress Dialog

	private TextView addname;
	private TextView soccer1;
	private TextView soccer2;
	private String name;
	private String Aresoccer;
	private String Theresoccer;
	private String year;
	private String id;
	private String month;
	private String day;
	TextView Date;
	private TextView agegrop;
	String pid;
	private String agegroup;
	// Progress Dialog
	private ProgressDialog pDialog;
	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	String id2;	
	private String age;
	private String LOG_TAG;
	private boolean test;
	  JSONArray Matches = null;
	
	private static final String urlGet = "http://patrickprojectrugby.com/ViewAllMatches.php";
	// JSON Node names
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_matches_info);
		test = isNetworkAvailable();
		 if(test == false)
		 {
			 Intent in = new Intent(getApplicationContext(),MainActivity.class);	
			 startActivity(in);;
			 Toast.makeText(getApplicationContext(), "Sorry you have no internet connection",
					   Toast.LENGTH_LONG).show();
		 }
		Intent i = getIntent();
		// getting Coaches id (id) from intent
		id2 = i.getStringExtra("id");
		agegroup = i.getStringExtra("age");
		new GetCoachesInfo().execute();
}//onCreate
	
	
	public void Back(View View) {
		Intent in = new Intent(getApplicationContext(),View_matches_View.class);
		in.putExtra("age", agegroup);			
		startActivity(in);;
	}
	public void Main(View View) {
		 Intent i = new Intent(getApplicationContext(), Main_matches.class);
        startActivity(i);
	}

class GetCoachesInfo extends AsyncTask<String, String, String> {

		


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(View_matches_Info.this);
			pDialog.setMessage("Loading Matches details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		protected String doInBackground(String... params) {
				
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("id", id2));
			// getting coaches details by making HTTP request
			// Note that coaches details url will use GET request
			JSONObject json = JSONParser.makeHttpRequest(urlGet, "GET", params1);
			Log.d("Geting players ", json.toString());
				try {
										
					// get first coaches object from JSON Array
				    // looping through All Matches
					Matches = json.getJSONArray("matches");
					  for (int i = 0; i < Matches.length() ; i++) 
	                    {
	                     JSONObject c = Matches.getJSONObject(i);          
					// coaches with this pid found
					// Edit Text
					    id = c.getString("Id");
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
					    else {
							
						}
	                    }   
					// display coaches data in EditText
					}//try 
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


