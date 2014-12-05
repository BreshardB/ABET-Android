package com.example.abetaccreditation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONException;

public class Login extends Activity{
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
	
	public void checkInput(View view){
		boolean success;
		EditText usernameField = (EditText)findViewById(R.id.username);
		EditText passwordField = (EditText)findViewById(R.id.password);
		String username = usernameField.getText().toString();
		String password = passwordField.getText().toString();
		
		Context context = getApplicationContext();
		CharSequence text;
		int duration = Toast.LENGTH_LONG;
		// check database to see if username is valid
		boolean usernameExists = true;
		
		boolean passwordMatches = true;
		if(!usernameExists){
			
			text = "Username does not exist, please try again";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			usernameField.setText("");
			passwordField.setText("");
		}
		else{
			// username exists, check database to see if password is correct
			if(!passwordMatches){
				text = "Incorrect password, please try again";
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				passwordField.setText("");
			}
			else{
				Intent intent = new Intent(this, Form.class);
				intent.putExtra("USERNAME", usernameField.getText().toString());
				startActivity(intent);
			}
		}
	}
	
}
