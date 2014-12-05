package com.example.abetaccreditation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Form extends Activity implements AsyncResponse{
	
	TextView instructorName;
	TextView outcomeText;
	
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
		TextView outcome1 = (TextView)findViewById(R.id.outcome1);
		TextView outcome2 = (TextView)findViewById(R.id.outcome2);
		TextView outcome3 = (TextView)findViewById(R.id.outcome3);
		
		outcome1.setText(major);
		outcome2.setText(major);
		outcome3.setText(major);
		
		URL url;
		jsonArray = new StringBuilder();
		
		try {
			url = new URL("http://104.155.193.216:3000/outcomes");
			task.execute(url);
			
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
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
}
