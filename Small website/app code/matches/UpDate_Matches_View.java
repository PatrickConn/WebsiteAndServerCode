
package poject.matches;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import poject.Pat.JSONParser;
import poject.Pat.R;
import poject.Pat.UpDate_Players;
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
import android.widget.Toast;
//import android.support.v7.appcompat.R;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class UpDate_Matches_View extends ListActivity {
 
    // Progress Dialog
    private ProgressDialog CDialog;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> MatchesList;
    // url to get all Matches list
    private static String url;
    // Matches JSONArray
    JSONArray Matches = null;
    JSONArray result= null;
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
		
			url = "http://patrickprojectrugby.com/ViewAllMatches.php";
		
        new ListAllMatches().execute();        
}
    public void Main(View View) {
 		 Intent i = new Intent(getApplicationContext(), View_matches.class);
        startActivity(i);
       
 	}

  	// Background Async Task to Load all Matches by making HTTP Request  
    class ListAllMatches extends AsyncTask<String, String, String> {
        // Before starting background thread Show Progress Dialog
        @Override
        protected void onPreExecute() 
        {
            super.onPreExecute();
            CDialog = new ProgressDialog(UpDate_Matches_View.this);
            CDialog.setMessage("Loading Matches. Please wait...");
            CDialog.setIndeterminate(false);
            CDialog.setCancelable(false);
            CDialog.show();
        }      
        // getting All Matches from url
  
        protected String doInBackground(String... args) 
        {
            // Building Parameters
            List<NameValuePair> query = new ArrayList<NameValuePair>();
            query.add(new BasicNameValuePair("Age", age));
            // getting JSON string from URL
            JSONObject json = JSONParser.makeHttpRequest(url, "GET", query);
            // Check your log cat for JSON reponse
            Log.d("All Matches: ", json.toString());
            try 
            {              
            	String KEY = "matches";
                // Getting Array of Coaches
             	Matches = json.getJSONArray("matches");
                 //looping through All Coaches
                result= Matches;
                SharedPreferences pref = getApplicationContext().getSharedPreferences("matches", 3);

                //Storing the string in pref file
                SharedPreferences.Editor prefEditor = pref.edit();
                prefEditor.putString(KEY, result.toString());
                prefEditor.commit();
                  
                
                    // looping through All Matches
                    for (int i = 0; i < Matches.length() ; i++) 
                    {
                        JSONObject c = Matches.getJSONObject(i);           		
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
                            
                        HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                       
                        map.put("test", test);
                        map.put("test2", test2);
                        map.put("home", home);
                        map.put("id", id);
                        map.put("Name", name);
                        map.put("Aresoccer", number);
                        map.put("Theresoccer", email);
                        map.put("Age", Age);
                    	MatchesList.add(map);
                        // adding HashList to ArrayList
                        }else {}
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
                    ListAdapter adapter = new SimpleAdapter(UpDate_Matches_View.this, MatchesList,R.layout.coaches_entry, new String[] {"id","home","Aresoccer","test2","Theresoccer","test","Name"}, new int[] {R.id.Id, R.id.Name,R.id.Age,R.id.test1,R.id.test2,R.id.test3,R.id.test4});
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
        			Intent in = new Intent(getApplicationContext(),UpDate_Matches.class);
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

