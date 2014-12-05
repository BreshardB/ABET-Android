package com.example.abetaccreditation;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
				url = new URL("http://104.155.193.216:3000/evaluations/insert");
	
				new SendData().execute(url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private class SendData extends AsyncTask<URL, String, String> {
		protected String doInBackground(URL... urls) {
			HttpURLConnection con;
			
			String result = null;
			try {
				con = (HttpURLConnection)urls[0].openConnection();
				con.setRequestMethod("POST");
				con.setDoInput(true);
				con.setDoOutput(true);
				//con.setRequestProperty("Content-Type", "application/json");
				con.setUseCaches(false);
				//con.setRequestProperty("Accept", "application/json");
				con.connect();
				
				JSONObject obj = new JSONObject();
				obj.put("user", "coyle");
				obj.put("password", "teacher");
				
		    	/*OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
		    	wr.write(obj.toString());
		    	wr.flush();
		    	wr.close();*/
				DataOutputStream dos = new DataOutputStream(con.getOutputStream());
				dos.writeBytes("{'name': 'coyle'}");
				dos.flush();
				dos.close();
				
				Log.d("response", con.getResponseMessage());
		    	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
	    }

	    protected void onPostExecute(String result) {
	    	Log.d("POST", "Sent it");
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
