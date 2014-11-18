package com.example.abetaccreditation;

import java.net.UnknownHostException;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
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
			try {
				mongo = new MongoClient("130.211.251.72", 27017);
				
				DB db = mongo.getDB("abet");
				
				DBCollection collection = db.getCollection("courseOutcomes");
				DBObject myDoc = collection.findOne();
				
				dataOutput.setText(myDoc.toString());
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
