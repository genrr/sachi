package main;

import java.util.ArrayList;

/** 
 * @author genrr
 * 
 */

public class Utils {

	
	/*
	 * coordinate transform from [0-10] to [a-k]
	 */
	
public static String charConv2(int pos) {
		
		int y = pos % 11;
		
		String s = "";
		
		switch(y) {
		case 0:
			s += 'a';
			break;
		case 1:
			s += 'b';
			break;
		case 2:
			s += 'c';
			break;
		case 3:
			s += 'd';
			break;
		case 4:
			s += 'e';
			break;
		case 5:
			s += 'f';
			break;
		case 6:
			s += 'g';
			break;
		case 7:
			s += 'h';
			break;
		case 8:
			s += 'i';
			break;
		case 9:
			s += 'j';
			break;
		case 10:
			s += 'k';
			break;
		default:
			s += 'x';
		}

		
		return s;
	}
	
	public static String charConv(int pos) {
		
		int y = pos % 11;
		
		String s = "";
		
		switch(y) {
		case 0:
			s += 'a';
			break;
		case 1:
			s += 'b';
			break;
		case 2:
			s += 'c';
			break;
		case 3:
			s += 'd';
			break;
		case 4:
			s += 'e';
			break;
		case 5:
			s += 'f';
			break;
		case 6:
			s += 'g';
			break;
		case 7:
			s += 'h';
			break;
		case 8:
			s += 'i';
			break;
		case 9:
			s += 'j';
			break;
		case 10:
			s += 'k';
			break;
		default:
			s += 'x';
		}
		
		s += String.valueOf((int)(pos/11.0) + 1);
		
		return s;
	}
	
	public static String pieceName(int type)
	{

		switch(type)
		{
		case 1:
			return "Soldier";
		case 2:
			return "Swordsman";
		case 3:
			return "Vanguard";
		case 4:
			return "Scythe";
		case 5:
			return "Prince";
		case 6:
			return "Guardian";
		case 7:
			return "Spearman";
		case 8:
			return "General";
		case 9:
			return "Queen";
		default:
			return null;
		}
	}
	
	//769110100 as 45-9-1-101-0-0
	
	//id(gives color & type, queen colors from parity) - front square(gives position and rotation, can outside board!) - hp counters
	//45 - 112 - 0, aka 451120
	
	
	//id, type, color, position, lvl, whenPlaced
	//id, type, color, position, rotation, hp
	
	public static boolean matchColor(int t, int t2)
	{
		return t % 2 == t2 % 2 ? true : false;
	}
	
	public static boolean isColor(int t, int color)
	{
		return t % 2 == (color-1) ? true : false;
	}
	
	public static int getColor(int t)
	{
		return t % 2 + 1;
	}

	public static int getPieceRotation(int t)
	{
		int temp = Math.floorDiv(t, 46*121*4);
		int temp2 =  Math.floorDiv(t - 46*121*4*temp, 46*121);
		
		return temp2*90;
		
	}
	
	public static int doPieceRotation(int t, int angle)
	{
		int temp = angle;
		
		if(angle == 90)
		{
			temp = 1;
		}
		else if(angle == 270)
		{
			temp = 3;
		}
		else if(angle == 180)
		{
			temp = 2;
		}
		
		t += 46*121*temp;
		
		return t;
	}
	
	public static int getMarkLvl(int t) 
	{
		return Math.floorDiv(t, 14*121);
	}
	
	public static int getPieceHP(int t)
	{
		return Math.floorDiv(t, 46*121*4);
	}
	
	public static int getPieceType(int t)
	{
		int temp = Math.floorDiv(t, 46*121);
		int id = Math.floorDiv(t - 46*121*temp, 121);
		return parseType(id);
	}
	
	public static MarkType getMarkType(int t)
	{
		t = -t;
		return MarkType.values()[ Math.floorDiv(t-1, 2) % 7];
	}
	
	public static int getPos(int t)
	{
		
		if(t < 0)
		{
			t = -t;
			return Math.floorDiv((t+1), 14);
		}
		else 
		{
			return t % 121;
		}
	}
	
	public static int setPos(int t, int pos)
	{
		t -= t % 121;
		t += pos;
		return t;
	}
	
	public static int getMarkInitPlies(int t)
	{
		int x = 0;
		
		while((t - 229*x) % 127 != 0)
		{
			x++;
		}
		
		t -= 127*x;
		
		return t / 131;
	}
	
	public static int modifyPieceHP(int t, int x)
	{
		t += 46*121*4*x;
		return t;
	}
	
	public static int modifyMarkHP(int t, int x)
	{
		t += 14*121*x;
		return t;
	}
	
	public static int parseType(int id)
	{
		int type;
        
        String s = "237568";
        String s2 = "114111";
   
        if(id < 23)
        {
        	System.out.println("id = "+id);
            int i = (int) Math.ceil(id / 2.0);
            System.out.println("i = "+i);
            type = Integer.parseInt(s.charAt(i < 6 ? i : 11 - i) + "");
        }
        else if(id == 45 || id == 46)
        {
            type = 9;
        }
        else
        {
        
            id -= 22;
            int i = (int) Math.ceil(id / 2.0);
            type = Integer.parseInt(s2.charAt(i < 6 ? i : 11 - i) + "");
        }
        
        return type;
	}
	
	public static void modifyCounters(int actionType, int color, int type, boolean teleport)
	{
		int x = 0;
		
		if(actionType == 0)
		{
			x = -PieceData.moveCost(type,teleport);
		}
		else if(actionType == 1)
		{
			x = -PieceData.pieceAC(type);
		}
		else if(actionType == 2)
		{
			x = -PieceData.pieceDC(type);
		}
		else if(actionType == 3)
		{
			x = PieceData.pieceValue(type);
		}
		else if(actionType == 4)
		{
			if(color == 2) {
				App2.p2countersPerTurn.set(App2.p2countersPerTurn.get()-1);
			}
			else if(color == 1) {
				App2.p1countersPerTurn.set(App2.p1countersPerTurn.get()-1);
			}
			return;
		}
		else if(actionType == 5)
		{
			if(type == 1)
			{
				if(color == 1) {
					App2.p1counters.set(App2.p1counters.get() - App2.p1countersPerTurn.get());
					App2.p1countersPerTurn.set(App2.p1countersPerTurn.get() + 1);
				}
				else if(color == 2) {
					App2.p2counters.set(App2.p2counters.get() - App2.p2countersPerTurn.get());
					App2.p2countersPerTurn.set(App2.p2countersPerTurn.get() + 1);
				}
				return;
			}
			else if(type == 2)
			{
				if(color == 1)
				{
					x = -computeQueenMarkCost(App2.p1countersPerTurn.get());
				}
				else if(color == 2)
				{
					x = -computeQueenMarkCost(App2.p2countersPerTurn.get());
				}
			}
			
			
		}
		else if(actionType == 6)
		{
			if(color == 1)
			{
				x = -2 * App2.p1countersPerTurn.get();
			}
			else if(color == 2)
			{
				x = -2 * App2.p2countersPerTurn.get();
			}
		}
		
		

		if(color == 1) {
			App2.p1counters.set(App2.p1counters.get() + x);
		}
		else if(color == 2) {
			App2.p2counters.set(App2.p2counters.get() + x);
		}

	}
	
	public static int computeQueenMarkCost(int countersPerTurn) {
		int cost = (2 * countersPerTurn - App2.queenMarkCost);
		
		return 1;
	}
	
	/*
	 * iterate whole board and fill legal moves and legal range arrays, depending on when matrix,matrix2 returned from RuleSet
	 * contain '1' at current square
	 */
	
	public static void iterateBoard(int pos, int[] board) {
		int[][] matrix = null;
		int[][] matrix2 = null;
		int[][] matrix3 = null;
		
		boolean markRange = false;
		
		if(board[pos] < 0)
		{
			markRange = true;
			matrix3 = RuleSet.validateRange(1,5);
		}
		
		if(!markRange) {
			matrix = RuleSet.validateMove(board[pos], board[pos]);
			matrix2 = RuleSet.validateRange( board[pos], board[pos]);
		}
		

		int i = (int)(pos/11.0);
		int j = pos % 11;
		
		App2.movementRange = new ArrayList<int[]>();
		App2.placementRange = new ArrayList<int[]>();
		
		if(!markRange) {
			if(getPieceType(board[pos]) != 7)
			{
				//apply rotations
				if(Utils.getPieceRotation(board[pos]) == 90)
				{
					matrix = RuleSet.rotateMatrix(matrix, 1);
					matrix2 = RuleSet.rotateMatrix(matrix2, 1);
				}
				else if(Utils.getPieceRotation(board[pos]) == 270)
				{
					matrix = RuleSet.rotateMatrix(matrix, -1);
					matrix2 = RuleSet.rotateMatrix(matrix2, -1);
				}
				else if(Utils.getPieceRotation(board[pos]) == 180)
				{
					matrix = RuleSet.rotateMatrix(RuleSet.rotateMatrix(matrix, 1), 1);
					matrix2 = RuleSet.rotateMatrix(RuleSet.rotateMatrix(matrix2, 1), 1);
				}
			}
			
			else {
				ArrayList<int[]> set = RuleSet.iterateRange(i, j, board, true, true);
				for (int h = 0; h < set.size();h++) {
					App2.placementRange.add(set.get(h));
				}
			}
		}
		
		
		for(int x = 0; x<11; x++) {
			for(int y = 0; y<11; y++) {
				try {
					//pieces use a matrix from RuleSet class, in which elements '1' mark the legal squares in relation to current piece
					if(!markRange) {
						if(Utils.getPieceType(board[pos]) != 7 && matrix2[3-(i-x)][3-(j-y)] == 1) 
						{
							App2.placementRange.add(new int[] {x,y});
						}		
	
						//test for legality of the target square
						
						if(matrix[3-(i-x)][3-(j-y)] == 1 && board[x * 11 + y] == 0) 
						{
							App2.movementRange.add(new int[] {x,y});
						}
						

					}
					else {
						if(matrix3[3-(i-x)][3-(j-y)] == 1)
						{
							App2.markRangeObs.add(new int[] {x,y});
						}
					}
				} 
				catch (ArrayIndexOutOfBoundsException e2) 
				{
					
				}
			}
		}
		
		for(int h = 0; h<121; h++) 
		{
			if(board[h] < 0 && Utils.getMarkType(board[h]) == MarkType.Teleport) 
			{
				for(int c = 0; c<121; c++)
				{
					if(h != c && board[c] < 0 && Utils.getMarkType(board[c]) == MarkType.Teleport)
					{
						App2.movementRange.add(new int[] {(int)(h/11.0), h % 11});
						App2.movementRange.add(new int[] {(int)(c/11.0), c % 11});
					}
				}
			}
		}
		

		
	}
	
	public static int makeId(int type, int color, int position)
	{
		if(type == 9)
		{
			if(color == 1) 
			{
				return 45;
			}
			else
			{
				return 46; 
			}
		}
		if(position < 22)
		{
			return 2*(position + 1);
		}
		else
		{
			return 2*(120-position)+1;
		}
	
	}

	public static int pullData(int i, String string) {
		
		
		
		return 0;
	}
}
