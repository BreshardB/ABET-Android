/*
 * Authors: Bre'Shard Busby and Brendan O'Connor
 * Date: 12/5/14
 * Form.java
 */

package com.example.abetaccreditation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Form extends Activity implements AsyncResponse{
	
	TextView instructorName;
	TextView outcomeText;
	TableLayout tableCAC;
	TableLayout tableEAC;
	
	String major;
	StringBuilder jsonArray;
	
	JSONArray outcomes;
	JSONArray rubrics;
	
	GetCourseOutcomes task = new GetCourseOutcomes(); //Object holding asynchronous task
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
        major = "cse";
        
        instructorName = (TextView)findViewById(R.id.instructor_name);
        String name = getIntent().getStringExtra("USERNAME");
        instructorName.setText(name);
        
		tableCAC = (TableLayout)findViewById(R.id.CAC_table); //Table for CAC outcomes
		tableEAC = (TableLayout)findViewById(R.id.EAC_table); //Table for EAC outcomes
        
        task.delegate = this;
    }
	
	@Override
	protected void onResume(){
		super.onResume();
		setOutcomes();
	}
	
	//Populates the table with the necessary outcomes
	public void setOutcomes(){
		
		URL url;
		jsonArray = new StringBuilder();
		
		try {
			
			//URL to route that obtains rubrics from the database
			url = new URL("http://104.155.193.216:3000/outcomes/rubrics/C");
			task.execute(url);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	//Switch between CSE and CE outcomes
	public void toggleMajor(View view){
		if(major.compareTo("cse") == 0){
			major = "ce";
		}
		else{
			major = "cse";
		}
		setOutcomes();
	}
	
	//Asynchronous task needed to gain course outcomes and rubrics
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

	//Create and structure table and ready it for input
	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		jsonArray.append(output);
		
		try {
			
			rubrics = new JSONArray(jsonArray.toString());
			JSONArray courseRubricEAC = (JSONArray)rubrics.getJSONObject(0).getJSONArray("rubricsEAC");
			JSONArray courseRubricCAC = (JSONArray)rubrics.getJSONObject(0).getJSONArray("rubricsCAC");
			
			//Headings
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
	
	//Submit evaluation input to database
	public void SubmitEvaluation(View v){
		
		URL url;
		
		try {
			
			//URL used to send data through API to the database
			url = new URL("http://104.155.193.216:3000/evaluations/insert");
			
		    new SendData().execute(url);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//Asynchronous task to send data to database
	private class SendData extends AsyncTask<URL, String, String> {
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		protected String doInBackground(URL... urls) {
			HttpURLConnection con;
			
			String result = null;
			try {
				con = (HttpURLConnection)urls[0].openConnection();
				con.setRequestMethod("POST");
				con.setDoInput(true);
				con.setDoOutput(true);
				con.setUseCaches(false);
				con.connect();

				//Using a stream to submit JSON to circumvent lack of memory
				JsonWriter writer = new JsonWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));

				writer.setIndent("  ");
				
				//Constructing the JSON object
				writer.beginObject();
				writer.name("instructor").value("coyle");
				writer.name("semeseter").value("Fall2014");
				writer.name("course").value("cse3342");
				writer.name("CACOutcome").value("C");
				
				writer.name("CACResults");
				writer.beginArray();

				//Cycle through CAC table to gain input
				for(int i = 1; i < tableCAC.getChildCount(); i++){
					TableRow row = (TableRow)tableCAC.getChildAt(i);
					writer.beginArray();
					for(int f = 1; f < row.getChildCount(); f++){
						if(row.getChildAt(f) instanceof EditText){
							EditText edit = (EditText)row.getChildAt(f);
							writer.value(edit.getText().toString());
						}
					}
					writer.endArray();
					writer.flush();
				}
				writer.endArray();
				
				writer.name("EACOutcome").value("C");
				writer.name("EACResults");
				writer.beginArray();
				
				//Cycle through EAC table to gain inputs
				for(int i = 1; i < tableEAC.getChildCount(); i++){
					TableRow row = (TableRow)tableEAC.getChildAt(i);
					writer.beginArray();
					for(int f = 1; f < row.getChildCount(); f++){
						EditText edit = (EditText)row.getChildAt(f);
						writer.value(edit.getText().toString());
					}
					writer.endArray();
					writer.flush();
				}
				writer.endArray();
				
				EditText cacBased = (EditText)findViewById(R.id.evaluation_basis1);
				EditText eacBased = (EditText)findViewById(R.id.evaluation_basis2);
				writer.name("cacBased").value(cacBased.getText().toString());
				writer.name("eacBased").value(eacBased.getText().toString());
				
				writer.endObject(); //End creation of JSON object
				writer.close();
				
				Log.d("response", con.getResponseMessage());
		    	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
	    }

	    protected void onPostExecute(String result) {
	    	Log.d("POST", "Sent it");
	    }
	 }
}
