package com.example.abetaccreditation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Form extends Activity implements AsyncResponse{
	
	TextView instructorName;
	TextView outcomeText;
	TableLayout abetTable;
	
	String major;
	StringBuilder jsonArray;
	
	JSONArray outcomes;
	JSONArray rubrics;
	
	GetCourseOutcomes task = new GetCourseOutcomes();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
        major = "cse";
        
        instructorName = (TextView)findViewById(R.id.instructor_name);
        String name = getIntent().getStringExtra("USERNAME");
        instructorName.setText(name);
        
        task.delegate = this;
    }
	
	@Override
	protected void onResume(){
		super.onResume();
		setOutcomes();
	}
	
	public void setOutcomes(){
		
		URL url;
		jsonArray = new StringBuilder();
		
		try {
			/*url = new URL("http://104.155.193.216:3000/outcomes");
			task.execute(url);*/
			
			url = new URL("http://104.155.193.216:3000/outcomes/rubrics/C");
			task.execute(url);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void toggleMajor(View view){
		if(major.compareTo("cse") == 0){
			major = "ce";
		}
		else{
			major = "cse";
		}
		setOutcomes();
	}
	
	private class GetCourseOutcomes extends AsyncTask<URL, Void, String> {
		
		public AsyncResponse delegate = null;
		
		@Override
		protected String doInBackground(URL... urls) {
			HttpURLConnection con;
			
			String result = null;
			try {
				con = (HttpURLConnection)urls[0].openConnection();
				InputStream in = new BufferedInputStream(con.getInputStream());
		    	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		    	 
		    	StringBuilder jsonString = new StringBuilder();
		    	 
		    	String line = null;
		    	while ((line = reader.readLine()) != null) {
		    		jsonString.append(line);
		    	}
		    	result = jsonString.toString();
		    	reader.close();
		    	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
	    }

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			delegate.processFinish(result);
		}
	}

	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		jsonArray.append(output);
		
		try {
			/*outcomes = new JSONArray(jsonArray.toString());
			
			JSONArray CACoutcomes = (JSONArray)outcomes.getJSONObject(0).getJSONArray("CACOutcomes");
			JSONArray EACoutcomes = (JSONArray)outcomes.getJSONObject(0).getJSONArray("EACOutcomes");
			
			Log.d("index", outcomes.getJSONObject(0).toString());
			
			outcomeText = (TextView)findViewById(R.id.outcome_text);
			outcomeText.setText(outcomes.getJSONObject(0).getString("descriptionCAC"));*/
			//Log.d("outcome", outcomes.getJSONObject(0).toString());
			
			rubrics = new JSONArray(jsonArray.toString());
			JSONArray courseRubricEAC = (JSONArray)rubrics.getJSONObject(0).getJSONArray("rubricsEAC");
			JSONArray courseRubricCAC = (JSONArray)rubrics.getJSONObject(0).getJSONArray("rubricsCAC");
			
			abetTable = (TableLayout)findViewById(R.id.abet_table);
			
			TableRow headings = new TableRow(this);
			headings.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
			//Columns headings for the table
			TextView hDescription = new TextView(this.getApplicationContext());
			headings.addView(hDescription);
			
			TextView hUnsatisfactory = new TextView(this.getApplicationContext());	
			hUnsatisfactory.setText("Unsatisfactory");
			headings.addView(hUnsatisfactory);
			
			TextView hDeveloping = new TextView(this.getApplicationContext());
			hDeveloping.setText("Developing");
			headings.addView(hDeveloping);
			
			TextView hSatisfactory = new TextView(this.getApplicationContext());
			hSatisfactory.setText("Satisfactory");
			headings.addView(hSatisfactory);
			
			TextView hExemplary = new TextView(this.getApplicationContext());
			hExemplary.setText("Exemplary");
			headings.addView(hExemplary);
			
			abetTable.addView(headings);
			
			
			for(int i = 0; i < courseRubricEAC.length(); i++){
				TableRow row = new TableRow(this);
				row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				
				TextView description = new TextView(this);
				description.setText(courseRubricEAC.getString(i));
				row.addView(description);
				
				//Table cells for entering values
				EditText eUnsatisfactory = new EditText(this);
				row.addView(eUnsatisfactory);
				
				EditText eDeveloping = new EditText(this);
				row.addView(eDeveloping);
				
				EditText eSatisfactory = new EditText(this);
				row.addView(eSatisfactory);
				
				EditText eExemplary = new EditText(this);
				row.addView(eExemplary);
				
				abetTable.addView(row);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
}
