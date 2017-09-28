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

import poject.matches.UpDate_Matches_View;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UpDate_Coache extends Activity {

	EditText CName;
	EditText CNumber;
	EditText CEmail;
	EditText CPass;
	private String name;
	private String number;
	private String email;
	private String pass;

	String pid;
	private String id2;
	private Spinner dropdown;
	private String[] items ;
	private String age;
	private ArrayAdapter<String> adapter ;
	// Progress Dialog
	private ProgressDialog pDialog;
	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	private String agegroup;
	private boolean test = true;
	private String LOG_TAG;
	private static final String urlGet = "http://patrickprojectrugby.com/Getcoaches.php";
	private static final String urlUP = "http://patrickprojectrugby.com/UpDate_Coaches.php";
	private static final String urlDel = "http://patrickprojectrugby.com/Delete_coaches.php";
	// JSON Node names
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_coache);
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
		dropdown = (Spinner)findViewById(R.id.spinner2);
		items = new String[]{"U7", "U8", "U9", "U10", "U11", "U13", "U15", "U16", "U19"};
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		dropdown.setAdapter(adapter);
		new GetCoachesInfo().execute();
		pDialog.dismiss();
}//onCreate
	
	public void DeleteCoache(View View) {
		new DeleteCoaches().execute();
	}
	public void Main(View View) {
		 	
		 Intent i = new Intent(getApplicationContext(), Mian_coaches.class);
		 i.putExtra("Login", 1);
         startActivity(i);
	}
	
	public void UpDateCoache(View View) {
		new UpdatingCoache().execute();
	}
	public void Back(View View) {
		Intent in = new Intent(getApplicationContext(),UpDate_Coache_View.class);
		in.putExtra("age", agegroup);			
		startActivity(in);;
	}

	class GetCoachesInfo extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(UpDate_Coache.this);
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
						JSONArray productObj = json.getJSONArray("coaches"); // JSON Array							
						// get first coaches object from JSON Array
						JSONObject c = productObj.getJSONObject(0);
						// coaches with this pid found
						// Edit Text
						name = c.getString("Name");
						number = c.getString("Number");
						email = c.getString("Email");
						age = c.getString("Age");
						pass = c.getString("Pass");
						// display coaches data in EditText			
						}//try 
						catch (JSONException e) 
						{	e.printStackTrace();	}//catch
					return null;
					}//doInBackground
		
				protected void onPostExecute(String file_url) {
					CName = (EditText) findViewById(R.id.CName);
					CNumber = (EditText) findViewById(R.id.CNumber);
					CEmail = (EditText) findViewById(R.id.CEmail);
					CPass = (EditText) findViewById(R.id.CPass);
					int index = 0;
					for (int i = 0; i < items.length; i++) {
						if (items[i].equals(age))
						{index = i;}
					}	
					dropdown.setSelection(index);
					CName.setText(name);
					CNumber.setText(number);
					CEmail.setText(email);	
					CPass.setText(pass);
				}//onPostExecute
			}//GetCoachesInfo
	class UpdatingCoache extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(UpDate_Coache.this);
			pDialog.setMessage("Updateing Coache ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@SuppressWarnings("deprecation")
		protected String doInBackground(String... args) {
			// getting updated data from EditTexts
			CName = (EditText) findViewById(R.id.CName);
			CNumber = (EditText) findViewById(R.id.CNumber);
			CEmail = (EditText) findViewById(R.id.CEmail);
			CPass = (EditText) findViewById(R.id.CPass);
			String age = dropdown.getSelectedItem().toString();
			String name = CName.getText().toString();
			String number = CNumber.getText().toString();
			String email = CEmail.getText().toString();
			String pass = CPass.getText().toString();
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", id2));
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("number", number));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("pass", pass));
			params.add(new BasicNameValuePair("age", age));
			
			// sending modified data through http request
			// Notice that update coaches url accepts POST method
			JSONParser.makeHttpRequest(urlUP, "POST", params);
			// check json success tag
			String text = dropdown.getSelectedItem().toString();
			Intent in = new Intent(getApplicationContext(),UpDate_Coache_View.class);
			in.putExtra("age", agegroup);		
			startActivity(in);;
			return null;
		}
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once coaches updated
		EditText txtnum = (EditText) findViewById(R.id.CNumber);
			txtnum.setText(id2);			
			pDialog.dismiss();
		}
	}
	
	class DeleteCoaches extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(UpDate_Coache.this);
			pDialog.setMessage("Deleteing Coaches ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@SuppressWarnings("deprecation")
		protected String doInBackground(String... args) {			
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", id2));
			// sending modified data through http request
			// Notice that update coaches url accepts POST method
			JSONParser.makeHttpRequest(urlDel, "POST", params);
			 String text = dropdown.getSelectedItem().toString();
				Intent in = new Intent(getApplicationContext(),UpDate_Coache_View.class);
				in.putExtra("age", agegroup);		
				startActivity(in);;
			return null;
		}
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once coaches updated			
			pDialog.dismiss();
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
	
}//update class

