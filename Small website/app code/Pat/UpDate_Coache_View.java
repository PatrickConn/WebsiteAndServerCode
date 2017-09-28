
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class UpDate_Coache_View extends ListActivity {
 
    // Progress Dialog
    private ProgressDialog CDialog; 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser(); 
    ArrayList<HashMap<String, String>> CoachesList;
    // url to get all Coaches list
    private static String url = "http://patrickprojectrugby.com/listAll.php";
    String age;

 
    // Coaches JSONArray
    JSONArray Coaches = null;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_coaches);
        // Hashmap for ListView
        CoachesList = new ArrayList<HashMap<String, String>>();
        Intent i = getIntent();
		// getting Coaches id (id) from intent
		age = i.getStringExtra("age");		
        // Loading Coaches in Background Thread
        new ListAllCoaches().execute(); 
       
}
    public void Main(View View) {
    	String text = "UpDate_Coache_View";
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
            CDialog = new ProgressDialog(UpDate_Coache_View.this);
            CDialog.setMessage("Loading Coaches. Please wait...");
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
            Log.d("All Coaches: ", json.toString());
            try 
            {
            	String KEY = "test";
                // Getting Array of Coaches
                Coaches = json.getJSONArray("coaches");
                 //looping through All Coaches
                result= Coaches;
                SharedPreferences pref = getApplicationContext().getSharedPreferences("test", 0);

                //Storing the string in pref file
                SharedPreferences.Editor prefEditor = pref.edit();
                prefEditor.putString(KEY, result.toString());
                prefEditor.commit();

            
            
                    // Getting Array of Coaches
                    Coaches = json.getJSONArray("coaches");
                    // looping through All Coaches
                    for (int i = 0; i < Coaches.length() ; i++) 
                    {
                        JSONObject c = Coaches.getJSONObject(i);           		
                        // Storing each json item in variable   
                        String id = c.getString("id");
                        String name = c.getString("Name");
                        String number = c.getString("Number");  
                        String email = c.getString("Email");  
                        String Age = c.getString("Age"); 
                        String line = "";
                        // creating new HashMap
                        if (Age.equalsIgnoreCase(age) || age.equalsIgnoreCase("All")){
                        HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        map.put("nametext", "Name: ");
                        map.put("line", line);
                        map.put("id", id);
                        map.put("Name", name);
                        map.put("Number", number);
                        map.put("Email", email);
                        map.put("Age", Age);
                    	CoachesList.add(map);
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
                    ListAdapter adapter = new SimpleAdapter(UpDate_Coache_View.this, CoachesList,R.layout.coaches_entry, new String[] {"id","nametext","Name"}, new int[] {R.id.Id, R.id.Name,R.id.Age});
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
        			Intent in = new Intent(getApplicationContext(),UpDate_Coache.class);
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

