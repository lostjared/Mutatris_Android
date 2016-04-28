package com.lostsidedead.mutatris;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.opengl.*;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.*;
import android.util.AttributeSet;
import android.util.Log;

import java.nio.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import android.os.*;
import android.view.*;
import android.graphics.*;
import android.graphics.drawable.*;

class GLRender implements  GLSurfaceView.Renderer {
	
	public boolean gameOver = false;
	
	class blockHandler extends Handler {
	    @Override
        public void handleMessage(Message msg) {
    	    if(paused == false && gameOver == false) {
    	    	grid.update();
    	    	if(grid.getGameOver() == true) {
    	    		gameOver = true;
    	    		BackgroundView.setGameOver(gameOver);
    	    	}
    	    }
   	       	this.sleep(MutGrid.GAME_SPEED);
       }

        public void sleep(long delayMillis) {
             this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
     }
	
	class updateHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if(paused == false && gameOver == false) 
				grid.moveBlocksDown();
			this.sleep(100);
		}
		
		public void sleep(long delayMillis) {
            this.removeMessages(0);
           sendMessageDelayed(obtainMessage(0), delayMillis);
       }
	}
	
	private Context context_;
	private float[] vert = new float[] {  -1.0f, -1.0f, 1.0f, // front face
			1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,

            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            -1.0f, -1.0f, -1.0f, // left side
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,

            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,

            -1.0f, 1.0f, -1.0f, // top
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            -1.0f, -1.0f, -1.0f, // bottom
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,

            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,

            1.0f, -1.0f, -1.0f, // right
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,

            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            -1.0f, -1.0f, -1.0f, // back face
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
	};
	
	
	private float [] texCoords = new float[] {
            0, 0, // front
			1, 1,
            0, 1,

            0, 0,
            1, 0,
            1, 1,

            0, 0, // left
			1, 0,
            0, 1,

             0, 1,
             1, 0,
             1, 1,

            0,0, // top
			0,1,
            1,1,

            1, 1,
            1, 0,
            0, 0,

            0, 0,// bottom
			0, 1,
            1, 1,

            1,1,
            1,0,
            0,0,

            0,0,// right
			1,0,
            0,1,

            0,1,
            1,0,
            1,1,

            0,0, // back
			1,1,
            0,1,

            0,0,
            1,0,
            1,1	            
	};
	
	public FloatBuffer fb;
	public FloatBuffer temp_float_buffer;
	public Random r = new Random();
	public static com.lostsidedead.mutatris.MutGrid grid = new MutGrid();
	public static final int GAME_WIDTH=20, GAME_HEIGHT=23;
	public blockHandler handler1 = new blockHandler();
	public updateHandler handler2 = new updateHandler();
	public BackgroundView bgv;
	private boolean paused = false;

	public GLRender(Context context) { 
		context_ = context; 
		ByteBuffer bb = ByteBuffer.allocateDirect(vert.length*4);
        bb.order(ByteOrder.nativeOrder());
        fb = bb.asFloatBuffer();
        fb.put(vert);
        fb.position(0);
        ByteBuffer tb = ByteBuffer.allocateDirect(texCoords.length* 4);
        tb.order(ByteOrder.nativeOrder());
        temp_float_buffer = tb.asFloatBuffer();
        temp_float_buffer.put(texCoords);
        temp_float_buffer.position(0);  
        paused = false;
        gameOver = false;
        setCircle(true, 1, 1, 1);
    }
	
	void setGrid() {
	     grid.sizeGrid(GAME_WIDTH, GAME_HEIGHT);
	     grid.clearGrid();   
	}
	
	public int width=0, height=0;
		
	public void onPause() {	
		paused = true;
	}
	
	public void onResume() {
		paused = false;
	}
    
	public void onSurfaceChanged(GL10 gl, int w, int h) {
       gl.glViewport(0, 0, w, h);
       this.width = w;
       this.height = h;
       gl.glMatrixMode(GL10.GL_PROJECTION);
       gl.glLoadIdentity();
       GLU.gluPerspective(gl, 45.0f, ((float)width/(float)height), 0.1f, 100.0f);
       gl.glMatrixMode(GL10.GL_MODELVIEW);
       gl.glLoadIdentity(); 
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0, 0, 0, 0);
	   	gl.glShadeModel(GL10.GL_SMOOTH);
    	gl.glEnable(GL10.GL_DEPTH_TEST);
    	gl.glClearDepthf(1.0f);
    	gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 	
		genTextures(gl, context_); 
		handler1.sleep(MutGrid.GAME_SPEED);
        handler2.sleep(100);
    }
	
	private int textid[] = new int[8];
	int texID[] = new int[1];
	
	public void genTextures(GL10 gl, Context c) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		textid[0] = loadGLTexture(gl, c, R.drawable.block1);
		textid[1] = loadGLTexture(gl, c, R.drawable.block2);
		textid[2] = loadGLTexture(gl, c, R.drawable.block3);
		textid[3] = loadGLTexture(gl, c, R.drawable.block4);
		textid[4] = loadGLTexture(gl, c, R.drawable.block5);
		textid[5] = loadGLTexture(gl, c, R.drawable.block6);
		textid[6] = loadGLTexture(gl, c, R.drawable.block7);
		textid[7] = loadGLTexture(gl, c, R.drawable.block8);
	}
		
	public int loadGLTexture(GL10 gl, Context context, int id) {
		InputStream is = context.getResources().openRawResource(id);
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(is);

		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}

		int[] texID_ = new int[1];
		
		gl.glGenTextures(1, texID_, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texID_[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		return texID_[0];
	} 
	
	private float rot_f = 0.0f;
	public int dissolve_table[][] = null;
	
	public void drawBlock(GL10 gl, Block b) {
		int bx = b.getBlockX();
		int by = b.getBlockY();
		for(int i = 0; i < 4; ++i) {
			drawSubBlock(bx, by, gl, b.pieces[i]);
		}
	}
	public void drawSubBlock(int x, int y, GL10 gl, Block.Piece p) {
		gl.glPushMatrix();
		int cx=x+p.x;
		int cy=y+p.y;
		float fx=(cx*2.5f);
		float fy=(cy*3.0f);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textid[p.color]);
		gl.glTranslatef(fx, -fy, 0);
		gl.glRotatef(25.0f, 0, 1, 0);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 36);
		gl.glPopMatrix();
	}
	
	private boolean all_circle = false;
	private float rotation_x, rotation_y, rotation_z;
	public void setCircle(boolean val, float fx, float fy, float fz) {
		all_circle = val;
		rotation_x = fx;
		rotation_y = fy;
		rotation_z = fz;
	}
	
    public void onDrawFrame(GL10 gl) {
    	
    	if(dissolve_table == null)
			dissolve_table = new int[grid.width][grid.height];
	
    	
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(-24.0f, 33.0f, -95.0f);
        gl.glRotatef(4.0f, 1, 1, 0);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb); 
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, temp_float_buffer);
        gl.glPushMatrix();
        for(int i = 0; i < GAME_WIDTH; ++i) {
        	for(int z = 0; z < GAME_HEIGHT; ++z) {
        		 if(grid.grid[i][z] == grid.BLOCK_NULL) continue;
        		    else if(grid.grid[i][z] == grid.BLOCK_DISSOLVE){
        			 gl.glBindTexture(GL10.GL_TEXTURE_2D, textid[r.nextInt(8)]);
        			 dissolve_table[i][z] ++;
        			 if(dissolve_table[i][z] > 4) {
        				 dissolve_table[i][z] = 0;
        				 grid.grid[i][z] = grid.BLOCK_NULL;
          			 }
        			 gl.glPushMatrix();
        			 gl.glTranslatef(2.5f*i, -3.0f*z, 0);
        			 gl.glRotatef(rot_f, 1, 1, 1);
        			 rot_f += 10.0f;
        			 if(rot_f > 360) rot_f = 0;
        			 gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 36);
        			 gl.glPopMatrix();
                  			 
        		 } else {
        			gl.glBindTexture(GL10.GL_TEXTURE_2D, textid[grid.grid[i][z]]);
            		gl.glPushMatrix(); 
        		 	float xPos = 2.5f*(float)i;
        		 	float yPos = 3.0f*z;
        		 	float zPos = 0;
          		 	gl.glTranslatef(xPos, -yPos, zPos);
          		 	          		 	
          		 	if(all_circle == false) gl.glRotatef(25.0f, 0, 1, 0); else {
          		 		gl.glRotatef(rot_f,rotation_x,rotation_y, rotation_z); // change this
          		 		rot_f += 7.0f;
          		 		if(rot_f > 360) rot_f = 0;
          		 	}
          		 	          		 	
          		 	gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 36);
        		 	gl.glPopMatrix();
        		 }
        	}
        }
        
        gl.glPopMatrix();
        gl.glPushMatrix();
        drawBlock(gl, grid.game_block);
        gl.glPopMatrix();
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY); 
    }   
}

public class GLView extends GLSurfaceView   {
	public GLRender render;
	
	public GLView(Context context) {
        super(context);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        render = new GLRender(context);
        setRenderer (render);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY); 
        
    }
	
	public void setGrid() {
		render.setGrid();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		render.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		render.onResume();	
	}
	
	
	public void onDestroy() {

	}
	
    public GLView(Context context, AttributeSet attrs) {
		super(context, attrs);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
		render = new GLRender(context);
        setRenderer (render);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}
    
    
    public MutGrid getGrid() { return render.grid; }
    
    public float rX = 0;
    
 

    
    public boolean onTouchEvent(final MotionEvent me) {
    	
		int Space = 45/2;
		if(me.getAction() == MotionEvent.ACTION_DOWN && me.getX() > Space && me.getX() < Space+32 && me.getY() > this.getHeight()-45 && me.getY() < this.getHeight() ) {
			getGrid().moveLeft();
			return true;
		}
		
		if(me.getAction() == MotionEvent.ACTION_DOWN && me.getX() > this.getWidth()-45 && me.getX() < this.getWidth() && me.getY() > this.getHeight()-45 && me.getY() < this.getHeight()) {
			getGrid().moveRight();
			return true;
		}
		
		if(me.getAction() == MotionEvent.ACTION_DOWN && me.getX() > this.getWidth()/2-Space && me.getX() < this.getWidth()/2-Space+32 && me.getY() > this.getHeight()-45 && me.getY() < this.getHeight()) {
			getGrid().shiftColors();
			return true;
		}
		
		return onSwipe(me);
	
    }
    
    public void randDirection() {
      	Random r = new Random();
        switch(r.nextInt(3)) {
        case 0:
        	render.setCircle(true, 1,0,0);
        	break;
        case 1:
        	render.setCircle(true, 1,1,0);
        	break;
        case 2:
        	render.setCircle(true, 1,1,1);
        	break;
        }
    }
        
    public void onBottomToTopSwipe() {
    	getGrid().shiftColors();
    	randDirection();
    }
    
    public void onLeftToRightSwipe() {

    	getGrid().moveRight();
    	randDirection();
    }
    
    public void onRightToLeftSwipe() {
    	
    	getGrid().moveLeft();
    	randDirection();
    }
    
    static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;
    
    
    public boolean onSwipe(final MotionEvent event) {
        switch(event.getAction()){
        case MotionEvent.ACTION_DOWN: {
            downX = event.getX();
            downY = event.getY();
            return true;
        }
        case MotionEvent.ACTION_UP: {
            upX = event.getX();
            upY = event.getY();

            float deltaX = downX - upX;
            float deltaY = downY - upY;
                        
            	if(Math.abs(deltaY) > 25){
            			if(deltaY > 0) { this.onBottomToTopSwipe(); return true; }
            	} else {  }
            
            	if(Math.abs(deltaX) > MIN_DISTANCE){
                	if(deltaX < 0) { this.onLeftToRightSwipe(); return true; }
                	if(deltaX > 0) { this.onRightToLeftSwipe(); return true; }
            	} else {  }
            
            	return false;
        	}
        case MotionEvent.ACTION_MOVE: {
        	float cX = event.getX();
        	float cY = event.getY();
        	float distanceX = downX - cX;
        	if(Math.abs(distanceX) > 25) {
        		if(distanceX > 0) { getGrid().moveLeft(); 	downX = cX;	render.setCircle(true, 1, 1, 0); }
        		if(distanceX < 0) { getGrid().moveRight();	downX = cX; render.setCircle(true, 0, 1, 0); }
        	} else {
        		float distanceY = downY - cY;
        		if(Math.abs(distanceY) > 50) {
        			if(distanceY < 0) {
        				getGrid().moveDown();
        				downY = cY;
        				randDirection();
        			}	
           		}
           	}
        		
        	}
        }
        return false;
    }
}
