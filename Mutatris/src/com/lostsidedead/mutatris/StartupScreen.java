package com.lostsidedead.mutatris;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.view.View.*;
import android.content.*;


public class StartupScreen extends Activity {
	
	public Button start_btn;
	public Spinner spin_ctrl;
	public SurfaceView sfv;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.start);
		start_btn = (Button) findViewById(R.id.button1);
		spin_ctrl = (Spinner) findViewById(R.id.spin02);

		start_btn.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(StartupScreen.this, Mutatris4DroidActivity.class);
				int pos = spin_ctrl.getSelectedItemPosition();
				switch(pos) { 
				case 0:
					MutGrid.START_SPEED=1000;
					Block.MAX_COLORS=4;
					break;
				case 1:
					MutGrid.START_SPEED=750;
					Block.MAX_COLORS=5;
					break;
				case 2:
					MutGrid.START_SPEED=400;
					Block.MAX_COLORS=7;
					break;
				}
				MutGrid.GAME_SPEED = MutGrid.START_SPEED;
				MutGrid.game_started = false;
				GLRender.grid = new MutGrid();
				GLRender.grid.init();
				MutGrid.score = 0;
				MutGrid.scoreCount = 0;
				//BackgroundView.setScore(0);
				startActivity(i);
			}
		});
	}
	
	

}
