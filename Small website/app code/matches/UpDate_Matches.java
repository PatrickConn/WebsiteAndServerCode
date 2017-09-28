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
import poject.Pat.R;
import poject.matches.Add_matches1.DatePickerFragment;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;


public class UpDate_Matches extends Activity {

	
	private String Age;
	String id2;
	String[] items;
	private Spinner dropdown;
	ArrayAdapter<String> adapter;
	// Progress Dialog
	private ProgressDialog pDialog;
	private EditText addname;
	private NumberPicker soccer1;
	private NumberPicker soccer2;
	private String name;
	private String number;
	private String email;
	private String year;
	private String age;
	private String month;
	private String day;
	private String yearadd;
	private String monthadd;
	private String dayadd;
	int hit = 0;
    Calendar c; 
	Button btnCalendar;
	 private int mYear, mMonth, mDay;
	
	String fullYear;
	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	private String agegroup;
	private String LOG_TAG;
	private boolean test;
	private static final String urlGet = "http://patrickprojectrugby.com/Getmatches.php";
	private static final String urlUP = "http://patrickprojectrugby.com/UpDate_Matches.php";
	private static final String urlDel = "http://patrickprojectrugby.com/Delete_matches.php";
	// JSON Node names
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_matches);
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
		new GetPlayersInfo().execute();
		dropdown = (Spinner)findViewById(R.id.spinner1);
		items = new String[]{"U7", "U8", "U9", "U10", "U11", "U13", "U15", "U16", "U19"};
		adapter =  new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, items);
		dropdown.setAdapter(adapter);
		pDialog.dismiss();		
}//onCreate
	public void onClick1(View v) {

		
        // Process to get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = Integer.parseInt(year);
        mMonth = Integer.parseInt(month);
        mDay = Integer.parseInt(day);
        c.set(Integer.parseInt(year),Integer.parseInt(month)-1, Integer.parseInt(day));
        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int yearOf,
                            int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                    	;
                    	  hit = 1;	
                	yearadd = String.valueOf(yearOf);
                	monthadd = String.valueOf(monthOfYear);
                	dayadd =String.valueOf(dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
}
	public void Delete(View View) {
		new DeleteMatches().execute();
	}
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	public void UpDate(View View) {
		new UpDateMatches().execute();
	}
	public void Back(View View) {
		Intent in = new Intent(getApplicationContext(),UpDate_Matches_View.class);
		in.putExtra("age", agegroup);			
		startActivity(in);;
	}
	public void Main(View View) {
		 Intent i = new Intent(getApplicationContext(), Main_matches.class);
        startActivity(i);
	}

	class GetPlayersInfo extends AsyncTask<String, String, String> {



		

		private String id;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(UpDate_Matches.this);
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
						JSONArray productObj = json.getJSONArray("matches"); // JSON Array							
						// get first coaches object from JSON Array
						JSONObject c = productObj.getJSONObject(0);
						// coaches with this pid found
						// Edit Text
						    id = c.getString("Id");
	                         name = c.getString("Name");  
	                        number = c.getString("Aresoccer");  
	                        email = c.getString("Theresoccer");
	                         age = c.getString("Age");  
	                         year = c.getString("Year");  
	                         month = c.getString("Month");  
	                         day = c.getString("Day");  
	                        
						// display coaches data in EditText
						}//try 
						catch (JSONException e) 
						{	e.printStackTrace();	}//catch
					return null;
					}//doInBackground
		
				protected void onPostExecute(String file_url) {
					addname = (EditText) findViewById(R.id.editText1);
					soccer1 = (NumberPicker) findViewById(R.id.numberPicker1);
					soccer2 = (NumberPicker) findViewById(R.id.numberPicker2);
					int index = 0;
					for (int i = 0; i < items.length; i++) 
					{
						if (items[i].equals(age))
						{index = i;}
					}	
					dropdown.setSelection(index);
					addname.setText(name);
					soccer1.setMaxValue(120);
					soccer1.setMinValue(0);
					soccer1.setValue(Integer.parseInt(number));
					soccer2.setMaxValue(120);
					soccer2.setMinValue(0);
					soccer2.setMinValue(Integer.parseInt(email));
					 c = Calendar.getInstance();
				    c.set(Calendar.DAY_OF_MONTH,Integer.parseInt(day));
			        c.set(Calendar.MONTH, Integer.parseInt(month) );
			        c.set(Calendar.YEAR, Integer.parseInt(year));
			       
				}//onPostExecute
			}//GetPlayersInfo
	class UpDateMatches extends AsyncTask<String, String, String> {

		private EditText Caddress;
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(UpDate_Matches.this);
			pDialog.setMessage("Updateing Matches ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Saving product
		 * */
		@SuppressWarnings("deprecation")
		protected String doInBackground(String... args) {

			Spinner mySpinner=(Spinner) findViewById(R.id.spinner1);
			String text = mySpinner.getSelectedItem().toString();			
			String name = addname.getText().toString();
		    String Aresoccer = String.valueOf(soccer1.getValue()); 
		    String Theresoccer = String.valueOf(soccer2.getValue()); 
		    String age = dropdown.getSelectedItem().toString();
		    if(hit == 0){
		    	yearadd = year;
 		    	monthadd = month;	
		    	dayadd = day;
		    }
		    // Building Parameters
			List<NameValuePair> query = new ArrayList<NameValuePair>();
			query.add(new BasicNameValuePair("Id", id2));
			query.add(new BasicNameValuePair("Name", name));
			query.add(new BasicNameValuePair("Aresoccer", Aresoccer));
			query.add(new BasicNameValuePair("Theresoccer", Theresoccer));
			query.add(new BasicNameValuePair("Year", yearadd));
			query.add(new BasicNameValuePair("Month", monthadd));
			query.add(new BasicNameValuePair("Day", dayadd));
			query.add(new BasicNameValuePair("Age", age));
			// sending modified data through http request
			// Notice that update coaches url accepts POST method
			JSONParser.makeHttpRequest(urlUP, "POST", query);
			// check json success tag
			 
			return null;
		}
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once coaches updated
		
			
			pDialog.dismiss();
				Intent in = new Intent(getApplicationContext(),UpDate_Matches_View.class);
				in.putExtra("age", agegroup);		
				startActivity(in);;
		}
	}
	
	class DeleteMatches extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(UpDate_Matches.this);
			pDialog.setMessage("Deleteing Matches ...");
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
			Intent in = new Intent(getApplicationContext(),UpDate_Matches_View.class);
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

