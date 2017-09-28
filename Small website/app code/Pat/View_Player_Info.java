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
import android.widget.TextView;
import android.widget.Toast;

public class View_Player_Info extends Activity {

	
	String pid;
	// Progress Dialog
	private ProgressDialog pDialog;
	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	String id2;
	private TextView CoachesName;
	private TextView phoneNumber;
	private TextView CoachesEmail;
	private String name;
	private String number;
	private String email;
	private String Address;
	private String Age;
	private String agegroup;
	private Boolean test;
	private String LOG_TAG;
	
	private static final String urlGet = "http://patrickprojectrugby.com/getPlayer.php";
	// JSON Node names
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_player_info);
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
	
	
	
	public void Main(View View) {
		 Intent i = new Intent(getApplicationContext(), Main_players.class);
        startActivity(i);
	}

class GetCoachesInfo extends AsyncTask<String, String, String> {

	


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(View_Player_Info.this);
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
				Log.d("Geting Player ", json.toString());
								try {
									
									JSONArray players = json.getJSONArray("players"); // JSON Array							
									
									JSONObject c = players.getJSONObject(0);
									
									name = c.getString("Name");
									number = c.getString("Number");
									email = c.getString("Email");
									Address = c.getString("Address");
									Age = c.getString("Age");
									
									// display coaches data in EditText
								
								}//try 
								catch (JSONException e) 
								{	e.printStackTrace();	}//catch
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
			}//GetCoachesInfo
public void Back(View View) {
	
	Intent in = new Intent(getApplicationContext(),View_player.class);
	in.putExtra("age", agegroup);			
	startActivity(in);;
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

