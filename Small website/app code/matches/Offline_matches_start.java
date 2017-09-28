package poject.matches;

import poject.Pat.MainActivity;
import poject.Pat.R;
import poject.Pat.UpDate_Coache_View;
import poject.Pat.View_coaches;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Offline_matches_start extends ActionBarActivity  {
	Spinner dropdown;
	private String name = "Offline";
	//database db = new database(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.view_teams);
	dropdown = (Spinner)findViewById(R.id.spinner1);
	String[] items = new String[]{"All","U7", "U8", "U9", "U10", "U11", "U13", "U15", "U16", "U19"};
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
	dropdown.setAdapter(adapter);
	 Intent myIntent = getIntent(); 
     name = myIntent.getStringExtra("Activity");
	}
	
	public void ViewTeams(View view) 
	{
		
			  String text = dropdown.getSelectedItem().toString();
				Intent in = new Intent(getApplicationContext(),Offline_Matches.class);
				in.putExtra("age", text);		
				startActivity(in);;
		
	}
	public void Back(View view) 
	{
		Intent in = new Intent(getApplicationContext(),MainActivity.class);	
		startActivity(in);;
	}
	

}