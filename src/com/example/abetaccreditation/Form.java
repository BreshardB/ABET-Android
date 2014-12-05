package com.example.abet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Form extends Activity{
	String major;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
        major = "cse";
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
}
