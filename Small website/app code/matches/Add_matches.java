package poject.matches;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import poject.Pat.JSONParser;
import poject.Pat.Mian_coaches;
import poject.Pat.R;
import poject.matches.Add_matches1.DatePickerFragment;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

public class Add_matches extends ActionBarActivity{
	EditText addname;
	NumberPicker soccer1;
	NumberPicker soccer2;
	Spinner dropdown;
	Button btnCalendar;
	 private int mYear, mMonth, mDay;
	int year,month,day;
	String fullYear;
	
	private static String url = "http://patrickprojectrugby.com/AddMatches.php";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_matches);
		dropdown = (Spinner)findViewById(R.id.spinner1);
		String[] items = new String[]{"U7", "U8", "U9", "U10", "U11", "U13", "U15", "U16", "U19"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		dropdown.setAdapter(adapter);
		addname = (EditText) findViewById(R.id.editText1);
		soccer1 = (NumberPicker) findViewById(R.id.numberPicker1);
		soccer2 = (NumberPicker) findViewById(R.id.numberPicker2);
		soccer1.setMaxValue(120);
		soccer1.setMinValue(0);
		soccer2.setMaxValue(120);
		soccer2.setMinValue(0);
		
      
	}
	public void onClick1(View v) {

	  
	        // Process to get Current Date
	        final Calendar c = Calendar.getInstance();
	        mYear = c.get(Calendar.YEAR);
	        mMonth = c.get(Calendar.MONTH);
	        mDay = c.get(Calendar.DAY_OF_MONTH);

	        // Launch Date Picker Dialog
	        DatePickerDialog dpd = new DatePickerDialog(this,
	                new DatePickerDialog.OnDateSetListener() {

	                    @Override
	                    public void onDateSet(DatePicker view, int yearOf,
	                            int monthOfYear, int dayOfMonth) {
	                        // Display Selected date in textbox
	                    	;
	                 
	                	year = yearOf;
                    	month = monthOfYear+1;
                    	day =dayOfMonth;
	                    }
	                }, mYear, mMonth, mDay);
	        dpd.show();
	}
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}

    public void onClick(View view) 
	{
        // creating new addnewcoaches in background thread
        new AddNewMatches().execute(); 
	}
class AddNewMatches extends AsyncTask<String, String, String> {
private ProgressDialog cDialog;
@Override
protected void onPreExecute() {
    super.onPreExecute();
    cDialog = new ProgressDialog(Add_matches.this);
    cDialog.setMessage("Add Matches..");
    cDialog.setIndeterminate(false);
    cDialog.setCancelable(true);
    cDialog.show();
}     
@SuppressWarnings("deprecation")
protected String doInBackground(String... args) {
	Spinner mySpinner=(Spinner) findViewById(R.id.spinner1);
	String text = mySpinner.getSelectedItem().toString();
	
	
	 
	String name = addname.getText().toString();
    String Aresoccer = String.valueOf(soccer1.getValue()); 
    String Theresoccer = String.valueOf(soccer2.getValue()); 
    String age = dropdown.getSelectedItem().toString();
    // Building Parameters
	List<NameValuePair> query = new ArrayList<NameValuePair>();
	query.add(new BasicNameValuePair("name", name));
	query.add(new BasicNameValuePair("Aresoccer", Aresoccer));
	query.add(new BasicNameValuePair("Theresoccer", Theresoccer));
	query.add(new BasicNameValuePair("year",String.valueOf(year)));
	query.add(new BasicNameValuePair("month", String.valueOf(month)));
	query.add(new BasicNameValuePair("day", String.valueOf(day)));
	query.add(new BasicNameValuePair("Age", age));
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
            Intent i = new Intent(getApplicationContext(), Main_matches.class);
            startActivity(i);        
            // closing this screen
            finish();
        } 
        else 
        {
            // failed to create user
            Log.d("failed to add Matches", json.toString());
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
public void Back(View view) {
	
  Intent NEW = new Intent(this, Main_matches.class);
  startActivity(NEW);
 
  }
public void Main(View view) {
	  Intent NEW = new Intent(this, Main_matches.class);
	  startActivity(NEW);
	 
	  }
public void view(View view)
{
	 String age = dropdown.getSelectedItem().toString();
	Intent in = new Intent(getApplicationContext(),View_matches_View.class);
	in.putExtra("age", age);		
	startActivity(in);;
}

}
