package com.lostsidedead.mutatris;

import java.util.Random;

import android.widget.Toast;


class Block {
	
	public int BLOCK_WIDTH=16, BLOCK_HEIGHT=16;
	public Random rand = new Random();
	public static int MAX_COLORS=4;
	class Piece {
		int x,y,color;
		public Piece() {
			x = y = color = 0;
		}
	}
	public Piece pieces[] = new Piece[4];
	
	public int block_x, block_y, block_type=0;

	public Block() {

		for(int i = 0; i < 4; ++i)
			pieces[i] = new Piece();
		
		setCoords(0, 0);
		randBlock();
	}

	public void randBlock() {
		block_type = rand.nextInt(3);
		do {
			for(int i = 0; i < 4; ++i) 
				pieces[i].color = rand.nextInt(MAX_COLORS);		
		} 
		while(pieces[0].color == pieces[1].color  && pieces[0].color == pieces[2].color && pieces[0].color == pieces[3].color);
		switch(block_type) {
		case 0: { // Square
				pieces[0].x = 0; pieces[0].y = 0;
				pieces[1].x = 1; pieces[1].y = 0;
				pieces[2].x = 0; pieces[2].y = 1;
				pieces[3].x = 1; pieces[3].y = 1;
			}
			break;
		case 1: { // horizontal line
				pieces[0].x = 0; pieces[0].y = 0;
				pieces[1].x = 1; pieces[1].y = 0;
				pieces[2].x = 2; pieces[2].y = 0;
				pieces[3].x = 3; pieces[3].y = 0;
			}
			break;
		case 2: { // vertical line
				pieces[0].x = 0; pieces[0].y = 0;
				pieces[1].x = 0; pieces[1].y = 1;
				pieces[2].x = 0; pieces[2].y = 2;
				pieces[3].x = 0; pieces[3].y = 3;		
			}		
			break;
		}
		
	}
	public void setBlockSize(int sw, int sh) { BLOCK_WIDTH=sw; BLOCK_HEIGHT=sh; }
	public int getBlockX() { return block_x; }
	public int getBlockY() { return block_y; }
	public int getBlockType() {	return block_type; }
	public void setCoords(int x, int y) { block_x=x; block_y=y; }	
}

public class MutGrid {
	public final int BLOCK_NULL=-1, BLOCK_DISSOLVE=-2;
	public int[][] grid = null;
	public int width=0, height =0;
	public Block game_block = new Block();;
	public static int GAME_SPEED=300;
	public static int START_SPEED=0;
	public static int scoreCount = 0;
	public static int score=0,lines=0;
	public static boolean game_over = false;
	public static boolean game_started = false;
	
	public MutGrid() {
		init();
	}
	
	public void init() {
		sizeGrid(GLRender.GAME_WIDTH, GLRender.GAME_HEIGHT);
	    clearGrid();
		newGame();
		GAME_SPEED=START_SPEED;
	}
	
	
			
	public void resetGameSpeed(int s) {
		GAME_SPEED=s;
		START_SPEED=s;
	}

	public void newGame() {
		GAME_SPEED=START_SPEED;
		score = lines = 0;
		game_block.randBlock();
		game_over = false;
		scoreCount = 0;
	}
	
	public void resetGame() {
		clearGrid();
		randBlock();
		score = lines = 0;
		game_over = false;
	}
	
	public int colorAt(int x, int y) {
		if(x < width && y < height)
			return grid[x][y];
		return BLOCK_NULL;
	}
	
	public void sizeGrid(int sw, int sh) {
		grid = new int[sw][sh];
		width=sw; height=sh;
		clearGrid();
		randBlock();
	}
	
	public boolean blockTest(Block b, int bx, int by) {
		for(int t=0; t<4; ++t) {
			int cx=bx+b.block_x+b.pieces[t].x;
			int cy=by+b.block_y+b.pieces[t].y;
			if(cx >= 0 && cy >= 0 && cx < width && cy < height && colorAt(cx,cy) == BLOCK_NULL) continue;
			else 
				return false;		
		}
		return true;
	}
	
	boolean getGameOver() { return game_over; }
	
	public void setPosition(int x, int y) {
	
		int tx=game_block.block_x;
		int ty=game_block.block_y;
		if(y < ty)
			y = ty;
		
		game_block.block_x=x;
		game_block.block_y=y;
		if(blockTest(game_block, 0, 0))
			return;

		game_block.block_x = tx;
		game_block.block_y = ty;
	}
	
	public void randBlock() {
		game_block.randBlock();
		int pos = (width/2);
		game_block.setCoords(pos, 0);
	}
	
	public void moveLeft() {
		if(blockTest(game_block, -1, 0)) {
			game_block.block_x--;
		}			
	}
	
	public void moveDown() {
		if(blockTest(game_block, 0, 1))
			game_block.block_y++;
		else
			mergeBlock();
	
	}
		
	public void moveRight() {
		if(blockTest(game_block, 1, 0)) {
			game_block.block_x++;
		}	
	}
	
	public void shiftColors() {
		Block.Piece blocks[] = game_block.pieces;
		
		int colors[] = new int[4];
		
		for(int i = 0; i < 4; ++i)
			colors[i] = blocks[i].color;
		
		blocks[0].color = colors[3];
		blocks[1].color = colors[0];
		blocks[2].color = colors[1];
		blocks[3].color = colors[2];	
	}
	
	public void clearGrid() {
		for(int i = 0; i < width; ++i) 
			for(int z = 0; z < height; ++z)
				grid[i][z] = BLOCK_NULL;
	
		scoreCount = 0;
	}
	
	public void mergeBlock() {
		
		if(game_block.block_y < 2)
		{
			game_over = true;
			game_started = false;
		}
		
		Block.Piece blocks[] = game_block.pieces;
		int x = game_block.block_x, y = game_block.block_y;
		for(int i = 0; i < 4; ++i) 
			mergePiece(x,y,blocks[i]);
		
		randBlock();
	}
	
	public void mergePiece(int x, int y, Block.Piece piece) {
		grid[x+piece.x][y+piece.y] = piece.color;
	}
	
	public void update() {
		moveDown();
		procBlocks();
	}
	
	
	public void addScore(int n) {
		score += n;
		lines++;
		BackgroundView.setScore(score);
		scoreCount++;
		if((scoreCount%8) == 0) {
			GAME_SPEED -= 200;
			Mutatris4DroidActivity.updateLevel(GAME_SPEED);
		}
	}
	
	
	public void moveBlocksDown() {
		for(int i = 0; i < width; ++i) {
			for(int z = 0; z < height-1; ++z)
			{
				if(grid[i][z+1] == BLOCK_NULL && grid[i][z] >= 0)
				{
					grid[i][z+1] = grid[i][z];
					grid[i][z] = BLOCK_NULL;
				}
			}
		}
	}
	
	public void procBlocks() {
		for(int i = 0; i < width; ++i) {
			for(int z = 0; z < height; ++z) {
				int cur_color = grid[i][z];
				if(cur_color == BLOCK_NULL) continue;
				
				if(i+3 < width && cur_color == grid[i+1][z] && cur_color == grid[i+2][z] && cur_color == grid[i+3][z]) {
					grid[i][z] = BLOCK_DISSOLVE;
					grid[i+1][z] = BLOCK_DISSOLVE;
					grid[i+2][z] = BLOCK_DISSOLVE;
					grid[i+3][z] = BLOCK_DISSOLVE;
					addScore(4);
					if(i+4 < width && cur_color == grid[i+4][z]) {
						addScore(10);
						grid[i+4][z] = BLOCK_DISSOLVE;
					}
					
					continue;
				}
				if(z+3 < height && cur_color == grid[i][z+1] && cur_color == grid[i][z+2] && cur_color == grid[i][z+3]) {
					grid[i][z] = BLOCK_DISSOLVE;
					grid[i][z+1] = BLOCK_DISSOLVE;
					grid[i][z+2] = BLOCK_DISSOLVE;
					grid[i][z+3] = BLOCK_DISSOLVE;
					addScore(4);
					if(z+4 < height && cur_color == grid[i][z+4]) {
						grid[i][z+4] = BLOCK_DISSOLVE;
						addScore(10);
					}
					continue;
				}
				
				if(i+1 < width && z+1 < height && grid[i+1][z] == cur_color && grid[i][z+1] == cur_color && grid[i+1][z+1] == cur_color) {
					grid[i][z] = BLOCK_DISSOLVE;
					grid[i][z+1] = BLOCK_DISSOLVE;
					grid[i+1][z] = BLOCK_DISSOLVE;
					grid[i+1][z+1] = BLOCK_DISSOLVE;
					addScore(4);
					continue;
				}
				
			}
		}
	}
}
