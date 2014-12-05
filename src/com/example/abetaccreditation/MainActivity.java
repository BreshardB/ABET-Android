package com.example.abetaccreditation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mongodb.MongoClient;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	
	MongoClient mongo;
	
	Button testButton;
	Button loginButton;
	Button signUpButton;
	Button createFormButton;
	Button homePageButton;
	
	TextView dataOutput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		testButton = (Button)findViewById(R.id.test);
		
		dataOutput = (TextView)findViewById(R.id.data_output);
		
		testButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.test) {
			
		URL url;
			
		try {
			url = new URL("http://104.155.193.216:3000/courses/courseList/cse1341");

			new GetCourseList().execute(url, null, null);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		}
	}
	
	private class GetCourseList extends AsyncTask<URL, String, String> {
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
		    	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
	    }

	    protected void onPostExecute(String result) {
	    	dataOutput.setText(result);
	    }
	 }

	public void goToLogin(View view){
    	Intent intent = new Intent(this, Login.class);
    	startActivity(intent);
    }
    
    public void createForm(View view){
    	Intent intent = new Intent(this, Form.class);
    	startActivity(intent);
    }
}
