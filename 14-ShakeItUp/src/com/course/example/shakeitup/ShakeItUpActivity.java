/*
 * This example uses a Sensor Manager to read values of internal sensors.
 * It uses two listener methods required by the SensorEventListener interface.
 * The listener is registered when it is used, and unregistered when the 
 * application stops. The listener is registered for each type of sensor
 * data used, e.g., orientation, accelerometer, temperature, ...
 * The accelerometer is used to trigger an AlertDialog when the phone is shaken.
 * 
 * Sensor.TYPE_ORIENTATION may be deprecated but it works well.
 */

package com.course.example.shakeitup;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class ShakeItUpActivity extends Activity implements SensorEventListener { 
	
	private final String tag = "SensorDemo";
	private SensorManager sm = null;
	
	TextView xViewA = null;
	TextView yViewA = null;
	TextView zViewA = null;
	TextView xViewO = null;
	TextView yViewO = null;
	TextView zViewO = null;
	TextView tViewT = null;	
	float xA=0, yA=0, zA=0, oldxA=0, oldyA=0, oldzA=0;
	double magnitude=0;
	final static float threshold = 15.0f;
	AlertDialog dialog = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);   //create a sensor manager object
        
        xViewA = (TextView) findViewById(R.id.xbox);
        yViewA = (TextView) findViewById(R.id.ybox);
        zViewA = (TextView) findViewById(R.id.zbox);
        xViewO = (TextView) findViewById(R.id.xboxo);
        yViewO = (TextView) findViewById(R.id.yboxo);
        zViewO = (TextView) findViewById(R.id.zboxo);
        tViewT = (TextView) findViewById(R.id.xboxt);  
        
        dialog = new AlertDialog.Builder(ShakeItUpActivity.this).create();
        
	      //set message, title, and icon
      	dialog.setTitle("Shaked Up"); 
      	dialog.setMessage("Are you sure that you want to quit?"); 
      //set three option buttons
    	dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() { 
    		public void onClick(DialogInterface dialog, int whichButton) { 
        	 //whatever should be done when answering "YES" goes here
    			Log.e(tag,"end Activity");
    			finish();
    		}              
    	});//setPositiveButton
    	
    	dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() { 
    		public void onClick(DialogInterface dialog, int whichButton) { 
        	 //whatever should be done when answering "NO" goes here
    			Log.e(tag,"keep truckin");
         } 
    	});//setNegativeButton
    }
    
    //listener for changes in sensor values
    public void onSensorChanged(SensorEvent event) {
    		
    		int type = event.sensor.getType();
    		
            if (type == Sensor.TYPE_ORIENTATION) {
	            xViewO.setText("Orientation X: " + event.values[0]);
	            yViewO.setText("Orientation Y: " + event.values[1]);
	            zViewO.setText("Orientation Z: " + event.values[2]);
            }
            if (type == Sensor.TYPE_ACCELEROMETER) {
	            xViewA.setText("Accel X: " + event.values[0]);
	            yViewA.setText("Accel Y: " + event.values[1]);
	            zViewA.setText("Accel Z: " + event.values[2]);
	            
	            //determine if phone shaken, calculate magnitude of acceleration over last deltaT
	            oldxA=xA;
	            oldyA=yA;
	            oldzA=zA;
	            xA=event.values[0];
	            yA=event.values[1];
	            zA=event.values[2];
	            magnitude=Math.sqrt(Math.pow(oldxA-xA,2) + Math.pow(oldyA-yA,2) + 
	            		            Math.pow(oldzA-zA,2));
	            if (magnitude > threshold) {
	            	Log.e(tag, "threshold reached, magnitude = " + magnitude);
	            	dialog.show();
	            };
            }  
            
            if (type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
	            tViewT.setText("Temperature: " + event.values[0]);
            }            
     
    }
    
    //listener for changes in sensor accuracy
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    	Log.d(tag,"onAccuracyChanged: " + sensor + ", accuracy: " + accuracy);
        
    }
 

    @Override
    protected void onResume() {
        super.onResume();
        
        //register listeners
        sm.registerListener(this, 
                sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
        
        sm.registerListener(this, 
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        
        sm.registerListener(this, 
                sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    @Override
    protected void onStop() {
        sm.unregisterListener(this);
        super.onStop();
    }    
    
    
}