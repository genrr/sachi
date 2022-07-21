package main;

import java.util.ArrayList;
import java.util.Arrays;

/** 
 * @author genrr
 * 
 */

public class RuleSet {


	/*
	 * Returns integer specifying the result of the move:
	 * 	0 (not a valid move)
	 * 	1 (valid move)
	 * 	2 (draw)
	 * 	3 (current player wins)
	 */
	
	
	public static int[][] validateMove(int color, int type) {
		
		int[][] t = null;
		int a = 0;
		int b = 0;
		
		switch(type) {
		//Pawn
		case 1:
			if(color == 2) {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,1,0,0,0},
								 {0,0,0,1,0,0,0},
								 {0,0,0,0,0,0,0}};
			}
			else {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,1,0,0,0},
								 {0,0,0,1,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0}};
			}
			break;
		case 2:
			t = new int[][] {{1,0,0,0,0,0,1},
							 {0,1,0,0,0,1,0},
							 {0,0,1,0,1,0,0},
							 {0,0,0,0,0,0,0},
							 {0,0,1,0,1,0,0},
							 {0,1,0,0,0,1,0},
							 {1,0,0,0,0,0,1}};

			break;
		case 3:
			if(color == 2) {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,1,0,1,0,0},
								 {0,0,0,1,0,0,0},
								 {0,0,0,0,0,0,0}};
			}
			else {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,1,0,0,0},
								 {0,0,1,0,1,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0}};
			}

			break;
		case 4:
			if(color == 2) {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,1,1,1,0,0},
								 {0,0,1,1,1,0,0},
								 {0,0,0,0,0,0,0}};
			}
			else {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,1,1,1,0,0},
								 {0,0,1,1,1,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0}};
			}
			break;
		case 5:
			t = new int[][] {{0,0,0,0,0,0,0},
							 {0,0,0,1,0,0,0},
							 {0,0,1,0,1,0,0},
							 {0,1,0,0,0,1,0},
							 {0,0,1,0,1,0,0},
							 {0,0,0,1,0,0,0},
							 {0,0,0,0,0,0,0}};

		 	break;
		case 6:
			if(color == 2) {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,1,1,0,1,1,0},
								 {0,0,1,0,1,0,0},
								 {0,0,1,0,1,0,0},
								 {0,0,0,0,0,0,0}};
			}
			else {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,1,0,1,0,0},
								 {0,0,1,0,1,0,0},
								 {0,1,1,0,1,1,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0}};
			}


			break;
		case 7:
			t = new int[][] {{0,0,0,0,0,0,0},
							 {0,0,0,0,0,0,0},
							 {0,0,1,1,1,0,0},
							 {0,0,1,0,1,0,0},
							 {0,0,1,1,1,0,0},
							 {0,0,0,0,0,0,0},
							 {0,0,0,0,0,0,0}};

			break;
		case 8:
			t = new int[][] {{0,0,0,0,0,0,0},
							 {0,0,0,0,0,0,0},
							 {0,0,0,1,0,0,0},
							 {0,0,1,0,1,0,0},
							 {0,0,0,1,0,0,0},
							 {0,0,0,0,0,0,0},
							 {0,0,0,0,0,0,0}};

			break;
		case 9:
			t = new int[][] {{0,0,0,0,0,0,0},
							 {0,0,0,1,0,0,0},
							 {0,0,0,1,0,0,0},
							 {0,1,1,0,1,1,0},
							 {0,0,0,1,0,0,0},
							 {0,0,0,1,0,0,0},
							 {0,0,0,0,0,0,0}};

			break;
		}
		
		return t;

				
	}
	
//	private static boolean checkBlocking(int sx, int sy, int tx, int ty, int[][][] board, int pieceType, boolean isMoving) {
//		
//		int pieceColor = board[sx][sy][2];
//		
//
//		
//		boolean blockOwn = false;
//		boolean blockEnemy = false;
//		
//		if(pieceType == 1) {
//			if(isMoving) {
//				return iterateRange(sx,sy,tx,ty,board,true,true);
//			}
//			else {
//				return false;
//			}
//		}
//		else if(pieceType == 2) {
//			if(isMoving) {
//				return iterateRange(sx,sy,tx,ty,board,true,true);
//			}
//			else {
//				return false;
//			}
//		}
//		else if(pieceType == 3) {
//			if(isMoving) {
//				return iterateRange(sx,sy,tx,ty,board,true,true);
//			}
//			else {
//				return iterateRange(sx,sy,tx,ty,board,false,true);
//			}
//		}
//		else if(pieceType == 4) {
//			if(isMoving) {
//				return iterateRange(sx,sy,tx,ty,board,false,true);
//			}
//			else {
//				return iterateRange(sx,sy,tx,ty,board,false,true);
//			}
//		}
//		else if(pieceType == 5) {
//			return false;
//		}
//		else if(pieceType == 6) {
//			if(isMoving) {
//				return false;
//			}
//			else {
//				return iterateRange(sx,sy,tx,ty,board,false,false);
//			}
//		}
//		else if(pieceType == 7) {
//			if(isMoving) {
//				return false;
//			}
//			else {
//				return iterateRange(sx,sy,tx,ty,board,true,true);
//			}
//		}
//		else if(pieceType == 8) {
//			if(isMoving) {
//				return false;
//			}
//			else {
//				return iterateRange(sx,sy,tx,ty,board,false,true);
//			}
//		}
//		else if(pieceType == 9) {
//			if(isMoving) {
//				return iterateRange(sx,sy,tx,ty,board,true,false);
//			}
//			else {
//				return iterateRange(sx,sy,tx,ty,board,true,true);
//			}
//		}
//		
////		return false;
//	}
//	
	public static boolean iterateRange(int source, int target, int[] board, boolean own, boolean enemy, boolean def) {
		int sx = (int)(source/11.0) - 1;
		int sy = source % 11 - 1;
		int tx = (int)(target/11.0) - 1;
		int ty = target % 11 - 1;
		int tempX = sx;
		int tempY = sy;
		int tempPos = 0;
		
		//logic:
		// (0,0) -> (1,2)
		// tempX = 0, tempY = 0, (0,0)
		// tempX = 0+sign(2-0) = 1, tempY = 0+sign(2-0) = 1, (1,1)
		// tempX = 1+sign(2-1) = 2, tempY = 1+sign(2-1) = 2, (2,2)
		
		
		while(tempX != tx && tempY != ty && Math.max(Math.abs(tempY-ty),Math.abs(tempX-tx)) != 1) {
			tempX += Math.signum(tx-tempX);
			tempY += Math.signum(ty-tempY);
			tempPos = tempX * 11 + tempY;
			
			System.out.println("tempX "+tempX+" tempY "+tempY);
			
			if(board[tempPos] != 0) {
				//if(def == (board[tempX][tempY][0] == -3)) {
					if(own && enemy) {
						return true;
					}
					else if(own && !enemy){
						if(Utils.matchColor(board[tempPos], board[source])) {
							return true;
						}
					}
					else if(!own && enemy) {
						if(!Utils.matchColor(board[tempPos], board[source])) {
							return true;
						}
					}	
				//}
			}
		}
		return false;
	}

	public static ArrayList<int[]> iterateRange(int sx, int sy, int[] board, boolean own, boolean enemy) {
		int tempX = sx;
		int tempY = sy;
		int tx = 0;
		int ty = 0;
		int pos = sx * 11 + sy;
		
		ArrayList<int[]> t = new ArrayList<int[]>();
		
		//logic:
		// (0,0) -> (1,2)
		// tempX = 0, tempY = 0, (0,0)
		// tempX = 0+sign(2-0) = 1, tempY = 0+sign(2-0) = 1, (1,1)
		// tempX = 1+sign(2-1) = 2, tempY = 1+sign(2-1) = 2, (2,2)
		
		//System.out.println("sx "+sx+" sy "+sy+board[sx][sy][1]);

		int rotation = Utils.getPieceRotation(board[pos]);
		
		if(Utils.getPieceType(board[pos]) == 7) 
		{

			if(Utils.isColor(board[pos],1))
			{
				if(rotation == 0)
				{
					tx = 0;
					ty = sy;
				}
				else if(rotation == 180)
				{
					tx = 10;
					ty = sy;
				}
				else if(rotation == 90)
				{
					tx = sx;
					ty = 10;
				}
				else if(rotation == 270)
				{
					tx = sx;
					ty = 0;
				}
			}
			else if(Utils.isColor(board[pos],2))
			{
				if(rotation == 0)
				{
					tx = 10;
					ty = sy;
				}
				else if(rotation == 180)
				{
					tx = 0;
					ty = sy;
				}
				else if(rotation == 90)
				{
					tx = sx;
					ty = 0;
				}
				else if(rotation == 270)
				{
					tx = sx;
					ty = 10;
				}
			}
			
			
			
			
		}
		
		
		while(tempX != tx || tempY != ty) {
			tempX += Math.signum(tx-tempX);
			tempY += Math.signum(ty-tempY);
			System.out.println(ty+" ty");
			System.out.println(tempX+" t"+tempY);
			
			int boardPos = tempX * 11 + tempY;
			
			if(board[boardPos] != 0) {
				//if(!Utils.isPieceColor(board[boardPos], Utils.getPieceColor(board[pos]))) {
					t.add(new int[] {tempX,tempY});
				//}
				if(own && enemy) {
					break;
				}
				else if(own && !enemy){
					if(Utils.isColor(board[boardPos], Utils.getColor(board[pos]))) {
						break;
					}
				}
				else if(!own && enemy) {
					if(!Utils.isColor(board[boardPos], Utils.getColor(board[pos]))) {
						break;
					}
				}


			}
			t.add(new int[] {tempX,tempY});
		}
		return t;
	}
	
	public static int[][] validateRange(int color, int type) {
		int[][] t = null;
		
		System.out.println(color+", "+type);
		
		switch(type) {
		case 1:
			if(color == 1) {
					t = new int[][] {{0,0,0,0,0,0,0},
									 {0,0,0,0,0,0,0},
									 {0,0,0,1,0,0,0},
									 {0,0,0,0,0,0,0},
									 {0,0,0,0,0,0,0},
									 {0,0,0,0,0,0,0},
									 {0,0,0,0,0,0,0}};
			}
			else {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,1,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0}};
			}

			break;
		case 2:
			if(color == 1) {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,1,0,1,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0}};
			}
			else {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,1,0,1,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0}};
			}
			break;
		case 3:
			if(color == 1) {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,1,1,1,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0}};
			}
			else {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,1,1,1,0,0},
								 {0,0,0,0,0,0,0}};
			}
			
			break;
		case 4:
			if(color == 1) {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,1,1,1,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0}};
			}
			else {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,1,1,1,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0}};
			}

			break;
		case 5:
			t = new int[][] {{0,0,0,0,0,0,0},
							 {0,0,0,0,0,0,0},
							 {0,0,1,1,1,0,0},
							 {0,0,1,0,1,0,0},
							 {0,0,1,1,1,0,0},
							 {0,0,0,0,0,0,0},
							 {0,0,0,0,0,0,0}};
			break;
		case 6:
			if(color == 1) {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,1,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,1,0,0,0,1,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0}};
			}
			else {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,1,0,0,0,1,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,1,0,0,0},
								 {0,0,0,0,0,0,0}};
			}
			break;
		case 7:
			t = new int[0][0];
				 break;
		case 8:
			t = new int[][] {{0,0,0,0,0,0,0},
							 {0,1,1,1,1,1,0},
							 {0,1,0,0,0,1,0},
							 {0,1,0,0,0,1,0},
							 {0,1,0,0,0,1,0},
							 {0,1,1,0,1,1,0},
							 {0,0,0,0,0,0,0}};
				 break;
		case 9:
			if(color == 1) {
				t = new int[][] {{0,0,0,1,0,0,0},
								 {0,0,0,1,0,0,0},
								 {0,0,0,1,0,0,0},
								 {0,1,1,0,1,1,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0}};
			}
			else {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,1,1,0,1,1,0},
								 {0,0,0,1,0,0,0},
								 {0,0,0,1,0,0,0},
								 {0,0,0,1,0,0,0}};
			}
			
			break;
		}
		
		return t;
	}
	
	/*
	 * Rotates matrix(clockwise(dir = 1), or counterclockwise(dir = -1))
	 * 
	 */
	
	public static int[][] rotateMatrix(int[][] matrix, int dir)
	{
		//i -> j, j = 10-i
		//(0,0) -> (0,10):  
		//(1,1) -> (1,9): 1,9
		//(0,10) -> (10,10)
		
		//counterclockwise:
		//j -> i, i = 10 - j
		//(0,0) -> (10,0)
		//(1,9) -> (1,1)
		//(4,3) -> 
		
		//(10 - 1) = 9
		
		//r[j][10-i] = matrix[i][j];
		
		int[][] r = new int[7][7];
		
		
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if(dir == 1)
				{
					r[j][6 - i] = matrix[i][j];
				}
				else if(dir == -1)
				{
					r[6-j][i] = matrix[i][j];
				}
				
			}
	
		}
		
		return r;
	}
	
	
	/*
	 * Check for game-winning condition, where current player made a rectangle which 
	 * surrounds all the pieces of other player 
	 * 
	 * checkArea returns false, if we found an enemy piece outside of the area 
	 * 
	 * 
	 */
	
	
	
	public static boolean checkAreas(int turn, int[] board) {
		
		int skippedAreas = 0;
		int pos = 0;
		ArrayList<int[]> t = new ArrayList<int[]>();
		int[] area;
		
		for(int j = 0; j<11; j++) {
			for(int i = 0; i<11; i++) {
				
				pos = i*11 + j;
				
				if(board[pos] != 0 && board[pos] < 0 && Utils.isColor(board[pos],turn)) {
					System.out.println("mark 1: "+i+" "+j);
					area = getArea(i,j,board,turn,i);
					if(area != null) {
						t.add(area);
					}
					
				}
			}
			
		}
		
		if(t.size() == 0) 
		{
			return false;
		}
		
		
		System.out.println(t.size());
		for (int[] is : t) 
		{
			
			System.out.println(Arrays.toString(is));
			label1:
			for(int j = 0; j<11; j++) 
			{
				for(int i = 0; i<11; i++) 
				{
					pos = i*11 + j;
					if(board[pos] != 0 && !Utils.isColor(board[pos], turn) && board[pos] > 0) 
					{
						//if we find a thing outside the current area -> skip current area
						if(i < Math.min(is[0],is[1]) || i > Math.max(is[0],is[1]) ||
								   j < Math.min(is[2],is[3]) || j > Math.max(is[2],is[3])) 
						{
							System.out.println("i "+i+" j "+j+" i_0 "+is[0]+" i_1 "+is[1]+" i_2 "+is[2]+" is_3 "+is[3]);
							skippedAreas++;
							break label1;
						}
						
					}
				}
				
			}
		}
		
		if(skippedAreas == t.size())
		{
			return false;
		}
		else
		{
			return true;
		}
			
			
		
		
		
	}
	
	public static int[] getArea(int startI, int startJ,int[] board, int turn, int initialI) {

		int tempI = -1;
		int tempJ = -1;
		int j = startJ;
		int pos = 0;
		int pos2 = 0;
		int pos3 = 0;

		for(int i = 0; i<11; i++) {
			
			pos = i * 11 + j;
			
			//mark found of the current player
			if(board[pos] != 0 && board[pos] < 0 && Utils.isColor(board[pos], turn) && i != startI) {

				System.out.println("mark 2 "+i+" "+j+" "+board[pos]);
				tempI = i;

				for(int x = 0; x < 11; x++) {
					
					pos2 = tempI * 11 + x;
					
					if(board[pos2] != 0 && board[pos2] < 0 && Utils.isColor(board[pos2], turn) &&
							x != j) {
						System.out.println("mark 3 "+tempI+" "+x+" "+board[pos2]);
						tempJ = x;
						
						pos3 = initialI * 11 + x;

						if(board[pos3] != 0 && board[pos3] < 0 && Utils.isColor(board[pos3], turn)) {
							System.out.println("mark 4 "+initialI+" "+tempJ+" "+board[pos3]);
							return new int[] {Math.min(initialI, tempI), Math.max(initialI, tempI), Math.min(j, tempJ), Math.max(j, tempJ)};
									//0,8,10,2
									//(0,0),(4,0),(0,3),(4,3)
									//(x1,y1),(x2,y1),(x1,y2), (x2,y2)
						}
					}
				}

			}
		}

		
		return null;
		
	}
	
	
}
