package poject.Pat;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class View_chat  extends ListActivity{
	Date now ;
	Date alsoNow;
	String nowAsString;
	ProgressDialog CDialog;
	 JSONArray chat;
	private JSONArray result;
	ArrayList<HashMap<String, String>> ChatList;
	 String url = "http://patrickprojectrugby.com/listChat.php";
	 String urladd = "http://patrickprojectrugby.com/Addchat.php";
	 EditText ChatText ;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_chat);
		ChatList = new ArrayList<HashMap<String, String>>();
		ChatText = (EditText) findViewById(R.id.Chat); 
		   new ListChats().execute(); 
		   
       }
	  

	  	// Background Async Task to Load all coaches by making HTTP Request  
 class ListChats extends AsyncTask<String, String, String> {
	      
			// Before starting background thread Show Progress Dialog
	        @Override
	        protected void onPreExecute() 
	        {
	            super.onPreExecute();
	            CDialog = new ProgressDialog(View_chat.this);
	            CDialog.setMessage("Loading Chat. Please wait...");
	            CDialog.setIndeterminate(false);
	            CDialog.setCancelable(false);
	            CDialog.show();
	        }      
	        // getting All Coaches from url
	  
	        
			protected String doInBackground(String... args) 
	        {
	            // Building Parameters
	            List<NameValuePair> query = new ArrayList<NameValuePair>();
	            query.add(new BasicNameValuePair("Age", "All"));
	            // getting JSON string from URL
	            JSONObject json = JSONParser.makeHttpRequest(url, "GET", query);
	            // Check your log cat for JSON reponse
	            Log.d("All chats: ", json.toString());
	            
	            try 
	            {            
	            	String KEY = "chat";
	                // Getting Array of Coaches
	            	chat = json.getJSONArray("chat");
	                 //looping through All Coaches
	                result= chat;
	                SharedPreferences pref = getApplicationContext().getSharedPreferences("chat", 0);

	                //Storing the string in pref file
	                SharedPreferences.Editor prefEditor = pref.edit();
	                prefEditor.putString(KEY, result.toString());
	                prefEditor.commit();
	                    // Getting Array of Coaches
	            		chat = json.getJSONArray("chat");
	                          
	                    for (int i = 0; i < chat.length() ; i++) 
	                    {
	                        JSONObject c = chat.getJSONObject(i);           		
	                        // Storing each json item in variable
	                        String date = c.getString("Date");
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
	                    ListAdapter adapter = new SimpleAdapter(View_chat.this, ChatList,R.layout.grope, new String[] {"Id","Chat"}, new int[] {R.id.Id,R.id.test7});
	               
	                    setListAdapter(adapter);
	                }
	            }); 
	        }   
	    }

	    
	}
