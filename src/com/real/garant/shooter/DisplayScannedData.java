package com.real.garant.shooter;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayScannedData extends Activity{

	
	TextView scannedData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.displaydata);
		
		
		Bundle extras = getIntent().getExtras();
		scannedData = (TextView) findViewById(R.id.scannedData);
		String data = extras.getString("scannedData");
		scannedData.setText(data);
	}
	
	
	

}
