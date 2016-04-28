package com.lostsidedead.mutatris;


import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class  BackgroundView extends android.view.View implements View.OnTouchListener {
	Paint painter = new Paint();
	public Bitmap arrows[] = new Bitmap[3];
	public Bitmap background;
	static BackgroundView bg_v;
	public boolean gameOver = false;
	
	public BackgroundView(Context c, AttributeSet as) {
		super(c, as);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnTouchListener(this);
        Resources r = this.getContext().getResources();
        arrows[0] = loadImage(r.getDrawable(R.drawable.left), 32, 32);
        arrows[1] = loadImage(r.getDrawable(R.drawable.right), 32, 32);
        arrows[2] = loadImage(r.getDrawable(R.drawable.up), 32, 32);
        background = loadImage(r.getDrawable(R.drawable.bg), 300, 482);
        
  
        
        bg_v = this;
  	}
	
	public static int score = 0;
	
	public static void setScore(int score) {
		bg_v.score = score;
		bg_v.invalidate();
		bg_v.glv.render.setCircle(false, 0, 0, 0);
	}

	public static void setGameOver(boolean b) {
		bg_v.gameOver = b;
		GameOverScreen.score = bg_v.score;
		Intent in = new Intent(bg_v.getContext(), GameOverScreen.class);
    	bg_v.getContext().startActivity(in);
 	}
	
	@Override
	public void onDraw(Canvas c) {
		painter.setARGB(255, 255, 255, 255);
		if(gameOver == false) {
			painter.setTextSize(20);
			c.drawBitmap(background, new Rect(0, 0, 300, 482), new Rect(0, 0, this.getWidth(), this.getHeight()-50), painter);
			c.drawText("Level: " + Mutatris4DroidActivity.gameLevel + "    Score: " + score, 25, 25, painter);
			c.drawBitmap(arrows[0], 45/2, this.getHeight()-45, painter);
			c.drawBitmap(arrows[1], this.getWidth()-45, this.getHeight()-45, painter);
			c.drawBitmap(arrows[2], this.getWidth()/2-(45/2), this.getHeight()-45, painter);
		} else {
			painter.setTextSize(55);
			c.drawText("Game Over", 25, 60, painter);
		}
	}
			
	public Bitmap loadImage(Drawable d, int w, int h) {
		Bitmap newImage = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas can = new Canvas(newImage);
		d.setBounds(0, 0, w, h);
		d.draw(can);
		return newImage;
    }
	
	public GLView glv;

	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}
}

