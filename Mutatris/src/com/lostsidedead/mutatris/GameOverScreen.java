package com.lostsidedead.mutatris;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.view.View.*;
import android.content.*;

public class GameOverScreen extends Activity {
	public Button btn;
	public static Context context;
	public TextView txt;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameover);
		context = this;
		btn = (Button) findViewById(R.id.button1);
		txt = (TextView) findViewById(R.id.txtView);
		btn.setOnClickListener( new OnClickListener() {
		
			public void onClick(View v) {
				Intent i = new Intent(GameOverScreen.this, StartupScreen.class);
				GameOverScreen.this.startActivity(i);
			}		
			
		});
		showScore();
	}	
	
	public static int score = 0;
	
	public void showScore() {
		Toast.makeText(this, "Game Over, Game Level " + Mutatris4DroidActivity.gameLevel + "   Your Score: " + score, 6000).show();
		if(score > 100)
			txt.setText("Level: " + Mutatris4DroidActivity.gameLevel + " Score: " + score + " Excellent");
		else	
			txt.setText("Level: " + Mutatris4DroidActivity.gameLevel + " Your Score: " + score);
	}
}
