
package poject.Pat;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class UpDate_Player_View extends ListActivity {
 
    // Progress Dialog
    private ProgressDialog CDialog;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> PlayersList;
    // url to get all Coaches list
    private static String url;
    // Coaches JSONArray
    JSONArray Players = null;
    String age;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_player);
        // Hashmap for ListView
        PlayersList = new ArrayList<HashMap<String, String>>();
        Intent i = getIntent();
		// getting Coaches id (id) from intent
		age = i.getStringExtra("age");
        // Loading Coaches in Background Thread
		
			url = "http://patrickprojectrugby.com/listAllplayers.php";
		
		
		
        new ListAllCoaches().execute();        
}
    public void Main(View View) {
    	String text = "UpDate_Player_View";
		 Intent i = new Intent(getApplicationContext(), View_Coaches_start.class);
		 i.putExtra("Activity", text);
      startActivity(i);
	}

  	// Background Async Task to Load all coaches by making HTTP Request  
    class ListAllCoaches extends AsyncTask<String, String, String> {
        private JSONArray result;
		// Before starting background thread Show Progress Dialog
        @Override
        protected void onPreExecute() 
        {
            super.onPreExecute();
            CDialog = new ProgressDialog(UpDate_Player_View.this);
            CDialog.setMessage("Loading Plyaers. Please wait...");
            CDialog.setIndeterminate(false);
            CDialog.setCancelable(false);
            CDialog.show();
        }      
        // getting All Coaches from url
  
        protected String doInBackground(String... args) 
        {
            // Building Parameters
            List<NameValuePair> query = new ArrayList<NameValuePair>();
            query.add(new BasicNameValuePair("Age", age));
            // getting JSON string from URL
            JSONObject json = JSONParser.makeHttpRequest(url, "GET", query);
            // Check your log cat for JSON reponse
            Log.d("All Plyaers: ", json.toString());
            try 
            {      
            	String KEY = "players";
                // Getting Array of Coaches
            	Players = json.getJSONArray("players");
                 //looping through All Coaches
                result= Players;
                SharedPreferences pref = getApplicationContext().getSharedPreferences("players", 1);

                //Storing the string in pref file
                SharedPreferences.Editor prefEditor = pref.edit();
                prefEditor.putString(KEY, result.toString());
                prefEditor.commit();
                    // Getting Array of Coaches
            	Players = json.getJSONArray("players");
                    // looping through All Coaches
                    for (int i = 0; i < Players.length() ; i++) 
                    {
                        JSONObject c = Players.getJSONObject(i);           		
                        // Storing each json item in variable   
                        String id = c.getString("Id");
                        String name = c.getString("Name");  
                        String spase = "   ";
                        String Age = c.getString("Age");  
                        // creating new HashMap
                        if (Age.equalsIgnoreCase(age) || age.equalsIgnoreCase("All")){
                        HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        map.put("nametext", "Name:");
                        map.put("id", id);
                        map.put("Name", name);
                       map.put("spase", spase);
                        map.put("Age", Age);
                    	PlayersList.add(map);
                        }
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
                    ListAdapter adapter = new SimpleAdapter(UpDate_Player_View.this, PlayersList,R.layout.coaches_entry, new String[] {"id","nametext","Name","spase","Age"}, new int[] {R.id.Id, R.id.Name,R.id.Age,R.id.test1,R.id.test2});
                    // updating listview
                    setListAdapter(adapter);	
                }
            }); 
            ListView list = getListView();
            
        	list.setOnItemClickListener(new OnItemClickListener() 
        	{
        		@Override
        		public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        		{
        			// getting values from selected ListItem
        			TextView id1 = ((TextView) view.findViewById(R.id.Id));
        			String id2 = id1.getText().toString();
        			// Starting new intent
        			Intent in = new Intent(getApplicationContext(),UpDate_Players.class);
        			// sending pid to next activity
        			in.putExtra("age", age);
        			in.putExtra("id", id2);		
        			// starting new activity and expecting some response back
        			startActivity(in);
        		}	
        	});
        } 
      
    }
    
}

