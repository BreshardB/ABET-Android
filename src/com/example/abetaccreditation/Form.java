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
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Form extends Activity implements AsyncResponse{
	
	TextView instructorName;
	TextView outcomeText;
	TableLayout tableCAC;
	TableLayout tableEAC;
	
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
			
			tableCAC = (TableLayout)findViewById(R.id.CAC_table);
			tableEAC = (TableLayout)findViewById(R.id.EAC_table);
			
			TableRow headingsCAC = new TableRow(this);
			headingsCAC.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			TableRow headingsEAC = new TableRow(this);
			headingsEAC.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
			//Columns headings for the table
			TextView hDescriptionCAC = new TextView(this.getApplicationContext());
			headingsCAC.addView(hDescriptionCAC);
			TextView hDescriptionEAC = new TextView(this.getApplicationContext());
			headingsEAC.addView(hDescriptionEAC);
			
			TextView hUnsatisfactoryCAC = new TextView(this.getApplicationContext());	
			hUnsatisfactoryCAC.setText("Unsatisfactory");
			headingsCAC.addView(hUnsatisfactoryCAC);
			TextView hUnsatisfactoryEAC = new TextView(this.getApplicationContext());	
			hUnsatisfactoryEAC.setText("Unsatisfactory");
			headingsEAC.addView(hUnsatisfactoryEAC);
			
			TextView hDevelopingCAC = new TextView(this.getApplicationContext());
			hDevelopingCAC.setText("Developing");
			headingsCAC.addView(hDevelopingCAC);
			TextView hDevelopingEAC = new TextView(this.getApplicationContext());
			hDevelopingEAC.setText("Developing");
			headingsEAC.addView(hDevelopingEAC);
			
			TextView hSatisfactoryCAC = new TextView(this.getApplicationContext());
			hSatisfactoryCAC.setText("Satisfactory");
			headingsCAC.addView(hSatisfactoryCAC);
			TextView hSatisfactoryEAC = new TextView(this.getApplicationContext());
			hSatisfactoryEAC.setText("Satisfactory");
			headingsEAC.addView(hSatisfactoryEAC);
			
			TextView hExemplaryCAC = new TextView(this.getApplicationContext());
			hExemplaryCAC.setText("Exemplary");
			headingsCAC.addView(hExemplaryCAC);
			TextView hExemplaryEAC = new TextView(this.getApplicationContext());
			hExemplaryEAC.setText("Exemplary");
			headingsEAC.addView(hExemplaryEAC);
			
			
			//Adding descriptions to EAC table
			if(courseRubricEAC.length() != 0){
				tableEAC.addView(headingsEAC);
				
				for(int i = 0; i < courseRubricEAC.length(); i++){
					TableRow row = new TableRow(this);
					row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
					
					TextView description = new TextView(this);
					description.setText(courseRubricEAC.getString(i));
					row.addView(description);
					
					//Table cells for entering values
					EditText eUnsatisfactory = new EditText(this);
					eUnsatisfactory.setInputType(InputType.TYPE_CLASS_NUMBER);
					row.addView(eUnsatisfactory);
					
					EditText eDeveloping = new EditText(this);
					row.addView(eDeveloping);
					
					EditText eSatisfactory = new EditText(this);
					row.addView(eSatisfactory);
					
					EditText eExemplary = new EditText(this);
					row.addView(eExemplary);
					
					tableEAC.addView(row);
					
				}
			}
			
			
			//Adding descriptions to CAC table	
			if(courseRubricCAC.length() != 0){	
				tableCAC.addView(headingsCAC);
				
				for(int i = 0; i < courseRubricCAC.length(); i++){
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
					
					tableCAC.addView(row);
					
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
}
