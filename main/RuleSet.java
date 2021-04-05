package main;

import java.util.ArrayList;

public class RuleSet {

	/*
	 * Returns integer specifying the result of the move:
	 * 	0 (not valid move)
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
							 {0,0,0,0,0,0,0},
							 {0,0,0,1,0,0,0},
							 {0,0,1,0,1,0,0},
							 {0,0,0,1,0,0,0},
							 {0,0,0,0,0,0,0},
							 {0,0,0,0,0,0,0}};

		 	break;
		case 6:
			if(color == 2) {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,1,0,1,0,0},
								 {0,0,0,1,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0}};
			}
			else {
				t = new int[][] {{0,0,0,0,0,0,0},
								 {0,0,0,0,0,0,0},
								 {0,0,0,1,0,0,0},
								 {0,0,1,0,1,0,0},
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
	public static boolean iterateRange(int sx, int sy, int tx, int ty, int[][][] board, boolean own, boolean enemy, boolean def) {
		int tempX = sx;
		int tempY = sy;
		
		//logic:
		// (0,0) -> (1,2)
		// tempX = 0, tempY = 0, (0,0)
		// tempX = 0+sign(2-0) = 1, tempY = 0+sign(2-0) = 1, (1,1)
		// tempX = 1+sign(2-1) = 2, tempY = 1+sign(2-1) = 2, (2,2)
		
		
		while(tempX != tx && tempY != ty) {
			tempX += Math.signum(tx-tempX);
			tempY += Math.signum(ty-tempY);
			
			System.out.println(tempX+" t"+tempY);
			
			if(board[tempX][tempY] != null) {
				if(def == (board[tempX][tempY][0] == -3)) {
					if(own && enemy) {
						return true;
					}
					else if(own && !enemy){
						if(board[tempX][tempY][2] == board[sx][sy][2]) {
							return true;
						}
					}
					else if(!own && enemy) {
						if(board[tempX][tempY][2] != board[sx][sy][2]) {
							return true;
						}
					}	
				}
			}
		}
		return false;
	}

	public static ArrayList<int[]> iterateRange(int sx, int sy, int[][][] board, boolean own, boolean enemy) {
		int tempX = sx;
		int tempY = sy;
		int tx = 0;
		int ty = 0;
		
		ArrayList<int[]> t = new ArrayList<int[]>();
		
		//logic:
		// (0,0) -> (1,2)
		// tempX = 0, tempY = 0, (0,0)
		// tempX = 0+sign(2-0) = 1, tempY = 0+sign(2-0) = 1, (1,1)
		// tempX = 1+sign(2-1) = 2, tempY = 1+sign(2-1) = 2, (2,2)
		
		System.out.println("sx "+sx+" sy "+sy+board[sx][sy][1]);
		
		if(board[sx][sy][1] == 7) {
			if(board[sx][sy][2] == 1) {
				tx = 0;
				ty = sy;
			}
			else {
				tx = 10;
				ty = sy;
			}
		}
		
		
		while(tempX != tx || tempY != ty) {
			tempX += Math.signum(tx-tempX);
			tempY += Math.signum(ty-tempY);
			System.out.println(ty+" ty");
			System.out.println(tempX+" t"+tempY);
			
			if(board[tempX][tempY] != null) {
				//opponent piece detected
				if(board[tempX][tempY][2] != board[sx][sy][2]) {
					t.add(new int[] {tempX,tempY});
				}
				
				if(own && enemy) {
					break;
				}
				else if(own && !enemy){
					if(board[tempX][tempY][2] == board[sx][sy][2]) {
						break;
					}
				}
				else if(!own && enemy) {
					if(board[tempX][tempY][2] != board[sx][sy][2]) {
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
	 * Check for game-winning condition, where current player made a rectangle which 
	 * surrounds all the pieces of other player 
	 * 
	 * checkArea returns false, if we found an enemy piece outside of the area 
	 * 
	 * 
	 */
	
	
	
	public static boolean checkAreas(int turn, int[][][] board) {
		
		ArrayList<int[]> t = new ArrayList<int[]>();
		int[] area;
		System.out.println("#");
		for(int j = 0; j<11; j++) {
			for(int i = 0; i<11; i++) {
				if(board[i][j] != null && board[i][j][0] < 0 && board[i][j][2] == turn) {
					System.out.println("mark 1: "+i+" "+j);
					area = getArea(i,j,board,turn,i);
					if(area != null) {
						t.add(area);
					}
					
				}
			}
			
		}
		
		System.out.println(t.size());
		
		for(int j = 0; j<11; j++) {
			for(int i = 0; i<11; i++) {
				if(board[i][j] != null && board[i][j][2] != turn) {
					for (int[] is : t) {
						
						if(i < Math.min(is[0],is[1]) ||
								i > Math.max(is[0],is[1]) ||
								j < Math.min(is[2],is[3]) ||
								j > Math.max(is[2],is[3])) {
							System.out.println("i "+i+" j "+j+" i_0 "+is[0]+" i_1 "+is[1]+" i_2 "+is[2]+" is_3 "+is[3]);
							return false;
						}
					}
					
				}
			}
		}
		if(t.size() == 0) {
			return false;
		}
		
		return true;
		
	}
	
	private static int[] getArea(int startI, int startJ,int[][][] board, int turn, int initialI) {

		int tempI = -1;
		int tempJ = -1;
		int j = startJ;

		for(int i = startI+1; i<11; i++) {
			
			//mark found of the current player
			if(board[i][j] != null && board[i][j][0] < 0 && board[i][j][2] == turn) {

				System.out.println("mark 2 "+i+" "+j+" "+board[i][j][0]);
				tempI = i;

				for(int x = 0; x < 11; x++) {
					if(board[tempI][x] != null && board[tempI][x][0] < 0 && board[tempI][x][2] == turn &&
							x != j) {
						System.out.println("mark 3 "+tempI+" "+x+" "+board[tempI][x][0]);
						tempJ = x;

						if(board[initialI][tempJ] != null && board[initialI][tempJ][0] < 0 && board[initialI][tempJ][2] == turn) {
							System.out.println("mark 4 "+initialI+" "+tempJ+" "+board[initialI][tempJ][0]);
							return new int[] {initialI,tempI,j,tempJ};
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
