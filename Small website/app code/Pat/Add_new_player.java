package poject.Pat;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
 
@SuppressWarnings("deprecation")
public class Add_new_player extends Activity {
 
    // Progress Dialog
    private ProgressDialog cDialog;
 
    JSONParser jsonParser = new JSONParser();
    EditText Cname;
    EditText Cnumber;
    EditText Cemail;
    EditText Cadderss;
    Spinner dropdown;
    // url to create new coach
    private static String url = "http://patrickprojectrugby.com/AddPlayer.php";
 
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_player);
        // Edit Text
        Cname = (EditText) findViewById(R.id.addname);
		Cnumber = (EditText) findViewById(R.id.addnumber1);
		Cemail = (EditText) findViewById(R.id.addemail2);  
		Cadderss = (EditText) findViewById(R.id.EditText01); 
		
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
            cDialog = new ProgressDialog(Add_new_player.this);
            cDialog.setMessage("Add Player..");
            cDialog.setIndeterminate(false);
            cDialog.setCancelable(true);
            cDialog.show();
        }     
    	protected String doInBackground(String... args) {
            String name = Cname.getText().toString();
            String number = Cnumber.getText().toString();
            String email = Cemail.getText().toString();
            String age = dropdown.getSelectedItem().toString();
            String address = Cadderss.getText().toString();
            
            // Building Parameters
			List<NameValuePair> query = new ArrayList<NameValuePair>();
			query.add(new BasicNameValuePair("name", name));
			query.add(new BasicNameValuePair("number", number));
			query.add(new BasicNameValuePair("email", email));
			query.add(new BasicNameValuePair("age", age));
			query.add(new BasicNameValuePair("address", address));
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
                    Intent i = new Intent(getApplicationContext(), Main_players.class);
                    startActivity(i);        
                    // closing this screen
                    finish();
                } 
                else 
                {
                    // failed to create user
                    Log.d("failed to add player", json.toString());
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
		  Intent NEW = new Intent(this, Main_players.class);
		  startActivity(NEW);
		 
		  }
    public void onClick3(View view) {
    	
    	  
			Intent in = new Intent(getApplicationContext(),MainActivity.class);
			
			startActivity(in);;
		  }

}

	



