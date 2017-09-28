
package poject.matches;


import java.util.ArrayList;
import java.util.HashMap;
import poject.Pat.*;

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


public class Offline_Matches extends ListActivity {
 
    // Progress Dialog
    private ProgressDialog CDialog;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> MatchesList;
    // url to get all Coaches list
    // Coaches JSONArray
    JSONArray Matches = null;
    JSONArray result= null;
    JSONArray array = null;
    String age;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_matches);
        // Hashmap for ListView
        MatchesList = new ArrayList<HashMap<String, String>>();
        Intent i = getIntent();
		// getting Matches id (id) from intent
		age = i.getStringExtra("age");
	
        // Loading Matches in Background Thread
		
		
        new ListAllMatches().execute();        
}
    public void Main(View View) {
 		 Intent i = new Intent(getApplicationContext(), Offline_matches_start.class);
         startActivity(i);
 	}

  	// Background Async Task to Load all Matches by making HTTP Request  
    class ListAllMatches extends AsyncTask<String, String, String> {
        // Before starting background thread Show Progress Dialog
        @Override
        protected void onPreExecute() 
        {
            super.onPreExecute();
            CDialog = new ProgressDialog(Offline_Matches.this);
            CDialog.setMessage("Loading Matches. Please wait...");
            CDialog.setIndeterminate(false);
            CDialog.setCancelable(false);
            CDialog.show();
        }      
        // getting All Matches from url
  
        @SuppressLint("NewApi")
		protected String doInBackground(String... args) 
        {
          
      
            try 
            {            
            	String KEY = "matches";
                  
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("matches", 3);
                    //Getting the JSON from pref
                    String storedCollection = pref.getString(KEY, null);
                    //Parse the string to populate your collection.
                     array = new JSONArray(storedCollection);
                              
                    for (int i = 0; i < array.length() ; i++) 
                    {
                    	  JSONObject c = array.getJSONObject(i);           		
                          // Storing each json item in variable   
                          String id = c.getString("Id");
                          String name = c.getString("Name");  
                          String number = c.getString("Aresoccer");  
                          String email = c.getString("Theresoccer");
                          String Age = c.getString("Age");  
                          String test = "  ";
                          String home = "Home  ";
                          String test2 = " - ";
                        // creating new HashMap
                       
                        if (Age.equalsIgnoreCase(age) || age.equalsIgnoreCase("All")){
                        // creating new HashMap
                        	   HashMap<String, String> map = new HashMap<String, String>();
                               // adding each child node to HashMap key => value
                        	   map.put("id", id);
                               map.put("test", test);
                               map.put("test2", test2);
                               map.put("home", home);
                               map.put("Name", name);
                               map.put("Aresoccer", number);
                               map.put("Theresoccer", email);
                               map.put("Age", Age);
                           	MatchesList.add(map);
                    	
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
        	
            // dismiss the dialog after getting all Matches
            CDialog.dismiss();  
            // updating UI from Background Thread
            runOnUiThread(new Runnable() 
            {
               

				public void run() 
                {
					
     
					 ListAdapter adapter = new SimpleAdapter(Offline_Matches.this, MatchesList,R.layout.coaches_entry, new String[] {"id","home","Aresoccer","test2","Theresoccer","test","Name"}, new int[] {R.id.Id, R.id.Name,R.id.Age,R.id.test1,R.id.test2,R.id.test3,R.id.test4});
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
        			Intent in = new Intent(getApplicationContext(),Offline_Matches_Info.class);
        			// sending pid to next activity
        			in.putExtra("id", id2);		
        			in.putExtra("age", age);
        				
        			// starting new activity and expecting some response back
        			startActivity(in);
        		}	
        	});


        	
        } 
      
    }
    
}

