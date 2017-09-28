package poject.Pat;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;


public class Offline_chat  extends ListActivity{
	Date now ;
	Date alsoNow;
	String nowAsString;
	ProgressDialog CDialog;
	JSONArray chat;
	ArrayList<HashMap<String, String>> ChatList;
	 JSONArray result= null;
	  JSONArray array = null;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_chat);
		ChatList = new ArrayList<HashMap<String, String>>();
	
		   new ListChats().execute(); 
		   
       }
	  

	  	// Background Async Task to Load all coaches by making HTTP Request  
 class ListChats extends AsyncTask<String, String, String> {
	      
			// Before starting background thread Show Progress Dialog
	        @Override
	        protected void onPreExecute() 
	        {
	            super.onPreExecute();
	            CDialog = new ProgressDialog(Offline_chat.this);
	            CDialog.setMessage("Loading Chat. Please wait...");
	            CDialog.setIndeterminate(false);
	            CDialog.setCancelable(false);
	            CDialog.show();
	        }      
	        // getting All Coaches from url
	  
	        @SuppressLint("NewApi")
			protected String doInBackground(String... args) 
	        {
	           
	            
	            try 
	            {            
	            	String KEY = "chat";
	                  
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("chat", 0);
                    //Getting the JSON from pref
                    String storedCollection = pref.getString(KEY, null);
                    //Parse the string to populate your collection.
                     array = new JSONArray(storedCollection);
	                    // Getting Array of Coaches
	            		       
	                    for (int i = 0; i < array.length() ; i++) 
	                    {
	                        JSONObject c = array.getJSONObject(i);           		
	                        // Storing each json item in variable
	                        
	                        String id = c.getString("Id");
	                        String text = c.getString("Chat");
	                        text += "\n";
	                        text += c.getString("Date");
	                        // creating new HashMap
	                        HashMap<String, String> map = new HashMap<String, String>();
	                        // adding each child node to HashMap key => value
	                        map.put("Id", id);
	                        map.put("Chat", text);
	                    	ChatList.add(map);
	                        // adding HashList to ArrayList        
	                    }
	            } 
	            catch (JSONException e) 
	            {
	                e.printStackTrace();
	            }
	            return null;
	        }
	        
	      
			protected void onPostExecute(String file_url) 
			{
	            // dismiss the dialog after getting all Coaches
	            CDialog.dismiss();  
	            // updating UI from Background Thread
	            runOnUiThread(new Runnable() 
	            {
					public void run() 
	                {
	                    ListAdapter adapter = new SimpleAdapter(Offline_chat.this, ChatList,R.layout.grope, new String[] {"Id","Chat"}, new int[] {R.id.Id,R.id.test7});
	               
	                    setListAdapter(adapter);
	                }
	            }); 
	        }   
	    }

	    
	}
