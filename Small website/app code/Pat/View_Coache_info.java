package poject.Pat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class View_Coache_info extends Activity {

	
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
	private String agegroup;
	private boolean test;
	private String LOG_TAG;
	
	private static final String urlGet = "http://patrickprojectrugby.com/Getcoaches.php";
	// JSON Node names
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_coaches_info);
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
		
		Intent in = new Intent(getApplicationContext(),View_coaches.class);
		in.putExtra("age", agegroup);			
		startActivity(in);;
	}
	public void Main(View View) {
		 Intent i = new Intent(getApplicationContext(), Mian_coaches.class);
        startActivity(i);
	}

class GetCoachesInfo extends AsyncTask<String, String, String> {

		private String name;
		private String number;
		private String email;


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(View_Coache_info.this);
			pDialog.setMessage("Loading Coaches details. Please wait...");
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
				Log.d("Geting cocahes ", json.toString());
								try {
									
									JSONArray coaches = json.getJSONArray("coaches"); // JSON Array							
									// get first coaches object from JSON Array
									JSONObject c = coaches.getJSONObject(0);
									// coaches with this pid found
									// Edit Text
									name = c.getString("Name");
									number = c.getString("Number");
									email = c.getString("Email");
									age = c.getString("Age");
									
									// display coaches data in EditText
								
								}//try 
								catch (JSONException e) 
								{	e.printStackTrace();	}//catch
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

