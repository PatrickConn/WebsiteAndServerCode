package poject.Pat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
 
@SuppressWarnings("deprecation")
public class Add_new_coaches extends Activity {
 
    // Progress Dialog
    private ProgressDialog cDialog;
 
    JSONParser jsonParser = new JSONParser();
    EditText Cname;
    EditText Cnumber;
    EditText Cemail;
    EditText Cpass;
    Spinner dropdown;
    String age;

	private boolean test;

	private String LOG_TAG;
    // url to create new coach
    private static String url = "http://patrickprojectrugby.com/AddCoache.php";
 
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_coaches);
        // Edit Text
        Cname = (EditText) findViewById(R.id.addname);
		Cnumber = (EditText) findViewById(R.id.addnumber1);
		Cemail = (EditText) findViewById(R.id.addemail2);  
		Cpass = (EditText) findViewById(R.id.EditText01); 
		 test = isNetworkAvailable();
		 if(test == false)
		 {
			 Intent in = new Intent(getApplicationContext(),MainActivity.class);	
			 startActivity(in);;
		 }
	 dropdown = (Spinner)findViewById(R.id.spinner1);
		String[] items = new String[]{"U7", "U8", "U9", "U10", "U11", "U13", "U15", "U16", "U19"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		dropdown.setAdapter(adapter);
    }
 
            public void onClick(View view) 
        	{
                // creating new addnewcoaches in background thread
                new AddNewCoache().execute(); 
        	}
    class AddNewCoache extends AsyncTask<String, String, String> {
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            cDialog = new ProgressDialog(Add_new_coaches.this);
            cDialog.setMessage("Add Coache..");
            cDialog.setIndeterminate(false);
            cDialog.setCancelable(true);
            cDialog.show();
        }     
    	protected String doInBackground(String... args) {
            String name = Cname.getText().toString();
            String number = Cnumber.getText().toString();
            String email = Cemail.getText().toString();
            age = dropdown.getSelectedItem().toString();
            String Pass = Cpass.getText().toString();
            
            // Building Parameters
			List<NameValuePair> query = new ArrayList<NameValuePair>();
			query.add(new BasicNameValuePair("name", name));
			query.add(new BasicNameValuePair("number", number));
			query.add(new BasicNameValuePair("email", email));
			query.add(new BasicNameValuePair("age", age));
			query.add(new BasicNameValuePair("Pass", Pass));
            // getting JSON Object
            // Note that create coach url accepts POST method     
            JSONObject json = JSONParser.makeHttpRequest(url, "POST", query);    
            // check log cat from response
            Log.d("Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    // successfully created a user
                    Intent i = new Intent(getApplicationContext(), Mian_coaches.class);
                    startActivity(i);        
                    // closing this screen
                    finish();
                } 
                else 
                {
                    // failed to create user
                    Log.d("failed to add Coache", json.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
		return null;
        }
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
        	cDialog.dismiss();
        }
    }
    public void onClick2(View view) {
		  Intent NEW = new Intent(this, Mian_coaches.class);
		  startActivity(NEW);
		 
		  }
    public void onClick3(View view) {
    		 
    
			Intent in = new Intent(getApplicationContext(),MainActivity.class);
				
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

	



