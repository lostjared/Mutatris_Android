package com.lostsidedead.mutatris;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import android.content.*;
import android.hardware.SensorEvent;

public class Mutatris4DroidActivity extends Activity implements AccelerometerListener {
    	
	public GLView view;
	public BackgroundView bgv;
	public static Context CONTEXT;
	public static int gameLevel = 0;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	CONTEXT = this;
    	setContentView(R.layout.main); 
    	
    	view = new GLView(this);
    	bgv = (BackgroundView) findViewById(R.id.bgView1);
     
    	
    	
    	addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
    		  ViewGroup.LayoutParams.FILL_PARENT));
    	bgv.glv = view; 
    	
    	if(MutGrid.game_started == false) {
    		Mutatris4DroidActivity.gameLevel = 1;
    		GLRender.grid.resetGameSpeed(1000);
    		bgv.setScore(0);
    		Toast.makeText(Mutatris4DroidActivity.getContext(), "Welcome, Your @ Level " + Mutatris4DroidActivity.gameLevel, 1000).show();
    		MutGrid.game_started = true;
    	}
    	
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
    	Log.i("com.lostsidedead", "KEYDOWN " + keyCode);
    	
    	switch(keyCode) {
    	case KeyEvent.KEYCODE_DPAD_UP:
    		view.getGrid().shiftColors();
    		return true;
    	case KeyEvent.KEYCODE_DPAD_DOWN:
    		view.getGrid().moveDown();
    		return true;
    	case KeyEvent.KEYCODE_DPAD_LEFT:
    		view.getGrid().moveLeft();
    		return true;
    	case KeyEvent.KEYCODE_DPAD_RIGHT:
    		view.getGrid().moveRight();
    		return true;
    		default:
    			return super.onKeyDown(keyCode, event);
    	}
    	  	
    } 
    
    public static Context getContext() {
    	return CONTEXT;
    }
    
    public void onShake(float i) {    }
    
    
    public float fX=0, fY=0, fZ=0;
    public long timeOut = 0, lastShake = 0;
    
	public void onAccelerationChanged(float x, float y, float z, SensorEvent event) {
		long current_time = event.timestamp;
		if(timeOut == 0) {
			timeOut = current_time;
			lastShake = current_time;
			fX = x;
			fY = y;
			fZ = z;
		}
		long timex = current_time - timeOut;
		if(timex > 0) { 
			timeOut = current_time;
			float force= Math.abs(x+y+z-fX-fY-fZ);
			fX = x;
			fY = y;
			fZ = z;
			
			long wait_time = current_time - lastShake;
			
			if(wait_time > 1000) {
				if(force > 3.0f) {
					view.getGrid().shiftColors();	
				}
				lastShake = current_time;
			}
		}
	}    
    @Override
    protected void onPause() {
    	super.onPause();
    	view.onPause();
    }
    
    @Override 
    protected void onResume() {
    	super.onResume();
    	view.onResume();
    	 if (AccelerometerManager.isSupported()) {
             AccelerometerManager.startListening(this);
         }
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	view.onDestroy();
    	if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
    }
    
    public static void updateLevel(int game_speed) {
    	gameLevel++;
    	Toast.makeText(CONTEXT, "Your @ Level " + gameLevel + " Interval: " + game_speed, 4000).show();
    	
    }
}

