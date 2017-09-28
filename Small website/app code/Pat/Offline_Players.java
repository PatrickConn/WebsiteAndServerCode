
package poject.Pat;


import java.util.ArrayList;
import java.util.HashMap;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class Offline_Players extends ListActivity {
 
    // Progress Dialog
    private ProgressDialog CDialog;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> PlayersList;
    // url to get all Coaches list
    // Coaches JSONArray
    JSONArray Players = null;
    JSONArray result= null;
    JSONArray array = null;
    String age;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_player);
        // Hashmap for ListView
        PlayersList = new ArrayList<HashMap<String, String>>();
        Intent i = getIntent();
		// getting Players id (id) from intent
		age = i.getStringExtra("age");
		     // Loading Players in Background Thread
		
		
        new ListAllPlayers().execute();        
}
    public void Main(View View) {
    	String text = "player";
		 Intent i = new Intent(getApplicationContext(), Offline_Start.class);
		 i.putExtra("age", text);
        startActivity(i);
 	}

  	// Background Async Task to Load all Players by making HTTP Request  
    class ListAllPlayers extends AsyncTask<String, String, String> {
        // Before starting background thread Show Progress Dialog
        @Override
        protected void onPreExecute() 
        {
            super.onPreExecute();
            CDialog = new ProgressDialog(Offline_Players.this);
            CDialog.setMessage("Loading Players. Please wait...");
            CDialog.setIndeterminate(false);
            CDialog.setCancelable(false);
            CDialog.show();
        }      
        // getting All Players from url
  
        @SuppressLint("NewApi")
		protected String doInBackground(String... args) 
        {
          
      
            try 
            {            
            	String KEY = "players";
                  
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("players", 1);
                    //Getting the JSON from pref
                    String storedCollection = pref.getString(KEY, null);
                    //Parse the string to populate your collection.
                     array = new JSONArray(storedCollection);
                              
                    for (int i = 0; i < array.length() ; i++) 
                    {
                        JSONObject c = array.getJSONObject(i);           		
                        // Storing each json item in variable   
                        // Storing each json item in variable   
                        String id = c.getString("Id");
                        String name = c.getString("Name");  
                        String spase = "   ";
                        String Age = c.getString("Age");  
                        // creating new HashMap
                       
                        if (Age.equalsIgnoreCase(age) || age.equalsIgnoreCase("All")){
                        // creating new HashMap
                        	 HashMap<String, String> map = new HashMap<String, String>();
                             // adding each child node to HashMap key => value
                        	 map.put("Nametext", "Name: ");
                             map.put("id", id);
                             map.put("Name", name);
                             map.put("spase", spase);
                             map.put("Age", Age);
                         	PlayersList.add(map);
                    	
                        // adding HashList to ArrayList
                        }else{}
                        
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
        	
            // dismiss the dialog after getting all Players
            CDialog.dismiss();  
            // updating UI from Background Thread
            runOnUiThread(new Runnable() 
            {
               

				public void run() 
                {
					
     
                    ListAdapter adapter = new SimpleAdapter(Offline_Players.this, PlayersList,R.layout.coaches_entry, new String[] {"id","Nametext","Name","spase","Age"}, new int[] {R.id.Id, R.id.Name,R.id.Age,R.id.test1,R.id.test2});
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
        			Intent in = new Intent(getApplicationContext(),Offline_Players_View.class);
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

