package poject.Pat;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import poject.matches.Add_matches;
import poject.matches.Main_matches;
import poject.matches.View_matches;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class login extends ActionBarActivity{
	 String  name;
	String coaches = "coaches";
	String player = "player";
	private EditText User;
	private EditText Pass;
	Intent NEW;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		User = (EditText) findViewById(R.id.CoachesId);  
		Pass = (EditText) findViewById(R.id.addname); 
		 Intent myIntent = getIntent(); 
	     name = myIntent.getStringExtra("Activity");
		
	}
	
	public void onClick(View view) {		
		if(name.equals("Add_new_coaches")){	
			 NEW = new Intent(this, Add_new_coaches.class);
			  
		}
		if(name.equals("UpDate_Coache_View")){
			 String text = "UpDate_Coache_View";
			  NEW = new Intent(this, View_Coaches_start.class);
			  NEW.putExtra("Activity", text);	
			
			
		}
		if(name.equals("Add_new_player")){	
			NEW = new Intent(this, Add_new_player.class);
			 
		}
		if(name.equals("UpDate_Player_View")){	
			  String text = "UpDate_Player_View";
			  NEW = new Intent(this, View_Coaches_start.class);
			  NEW.putExtra("Activity", text);	
			
	
		}
		if(name.equals("Add_matches")){	
			 NEW = new Intent(this, Add_matches.class);
			  
		}
		if(name.equals("UpDate_Matches")){
			String text = "UpDate_Matches";
			  Intent NEW = new Intent(this,View_matches.class);
			  NEW.putExtra("Activity", text);	
			  startActivity(NEW);
			
			
		}
		if(name.equals("Add_maessage")){
			String text = "UpDate_Matches";
			  Intent NEW = new Intent(this,Group.class);
			  NEW.putExtra("Activity", text);	
			  startActivity(NEW);
			
			
		}
		
		new	GetPlayersInfo().execute();
	}
	public void Back(View view) {
		if(name.equals("Add_new_coaches")){	
			 NEW = new Intent(this, Mian_coaches.class);
			  
		}
		if(name.equals("UpDate_Coache_View")){
			 String text = "UpDate_Coache_View";
			  NEW = new Intent(this, Mian_coaches.class);
			  NEW.putExtra("Activity", text);	
			
			
		}
		if(name.equals("Add_new_player")){	
			NEW = new Intent(this, Main_players.class);
			 
		}
		if(name.equals("UpDate_Player_View")){	
			  String text = "UpDate_Player_View";
			  NEW = new Intent(this, Main_players.class);
			  NEW.putExtra("Activity", text);	
			
	}
		if(name.equals("Add_matches")){	
			 NEW = new Intent(this, Main_matches.class);
			  
		}
		if(name.equals("UpDate_Matches")){
			String text = "UpDate_Matches";
			  Intent NEW = new Intent(this,Main_matches.class);
			  NEW.putExtra("Activity", text);	
			  startActivity(NEW);
			
			
		}
		if(name.equals("Add_maessage")){
			String text = "UpDate_Matches";
			  Intent NEW = new Intent(this,Main_Masseage.class);
			  NEW.putExtra("Activity", text);	
			  startActivity(NEW);
			
			
		}
		if(name.equals(null)){
			String text = "UpDate_Matches";
			  Intent NEW = new Intent(this,MainActivity.class);
			  NEW.putExtra("Activity", text);	
			  startActivity(NEW);
			
			
		}
	}
	
	
	
	class GetPlayersInfo extends AsyncTask<String, String, String> {



		private ProgressDialog pDialog;
		private String Login;
		private static final String urlGet = "http://patrickprojectrugby.com/Login.php";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(login.this);
			pDialog.setMessage("Loading Users details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		protected String doInBackground(String... params) {
			  String user = User.getText().toString();
	            String pass = Pass.getText().toString();
	          
	            
	            // Building Parameters
				List<NameValuePair> query = new ArrayList<NameValuePair>();
				query.add(new BasicNameValuePair("User", user));
				query.add(new BasicNameValuePair("Pass", pass));
				
				// getting coaches details by making HTTP request
				// Note that coaches details url will use GET request
				JSONObject json = JSONParser.makeHttpRequest(urlGet, "GET", query);
				//Log.d("Geting Users ", json.toString());
					try {
						JSONArray productObj = json.getJSONArray("coaches"); // JSON Array							
						// get first coaches object from JSON Array
						JSONObject c = productObj.getJSONObject(0);
						// coaches with this pid found
						Login = c.getString("Login");
						// display coaches data in EditText
						}//try 
						catch (JSONException e) 
						{	e.printStackTrace();	}//catch
					return null;
					}//doInBackground
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
					if (Login.equals("1"))
					{
						String KEY = "login";
	                    // Getting Array of Coaches
	                   
	                     //looping through All Coaches
	                    String result = "1";
	                    SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);

	                    //Storing the string in pref file
	                    SharedPreferences.Editor prefEditor = pref.edit();
	                    prefEditor.putString(KEY, result);
	                    prefEditor.commit();

						PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("login", "1").commit();
						pDialog = new ProgressDialog(login.this);
						pDialog.setMessage("You hava log in");
						pDialog.setCancelable(false);
						pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
						    @Override
						    public void onClick(DialogInterface dialog, int which) {
						    	pDialog.dismiss();
						  
						    	startActivity(NEW);
						    }
						});
						pDialog.show();
						
					}else{ 
					
					pDialog = new ProgressDialog(login.this);
					pDialog.setMessage("You hava felsd to log in");
					pDialog.setCancelable(false);
					pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					    	pDialog.dismiss();
					    	NEW = new Intent();
					    	startActivity(NEW);
					    }
					});
					pDialog.show();
					
					
					}
				}//onPostExecute
			}//GetPlayersInfo
	

}