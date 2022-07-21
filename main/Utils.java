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

		switch (y) {
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

		String s = charConv2(pos);

		s += String.valueOf((int) (pos / 11.0) + 1);

		return s;
	}

	public static String pieceName(int type) {

		switch (type) {
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

	// 769110100 as 45-9-1-101-0-0

	// id(gives color & type, queen colors from parity) - front square(gives
	// position and rotation, can outside board!) - hp counters
	// 45 - 112 - 0, aka 451120

	// id, type, color, position, lvl, whenPlaced
	// id, type, color, position, rotation, hp

	public static boolean matchColor(int t, int t2) {
		// t = getData(0,t);
		return getColor(t) == getColor(t2) ? true : false;
	}

	public static boolean isColor(int t, int color) {
		// t = getData(0,t);
		return getColor(t) == color ? true : false;
	}

	public static int getColor(int t) {

		// t = getData(0,t);

		if (t < 0) {
			return 2 - Math.abs(t) % 2;
		}

		int temp = Math.floorDiv(t, 47 * 121);
		int id = Math.floorDiv(t - 47 * 121 * temp, 121);
		return 2 - id % 2;
	}

	public static int getPieceRotation(int t) {
		// t = getData(0,t);
		int temp = Math.floorDiv(t, 47 * 121 * 4);
		int temp2 = Math.floorDiv(t - 47 * 121 * 4 * temp, 47 * 121);
		return temp2 * 90;

	}

	public static int doPieceRotation(int t, int angle) {
		// t = getData(0,t);
		int temp = angle / 90;


		t += 47*121*(-getPieceRotation(t)/90 + temp);

		return t;
	}

	public static int getMarkLvl(int t) {
		// t = getData(0,t);
		t = -t;
		int temp = Math.floorDiv(t, 121 * 14 * 5);
		int temp2 = t - 121 * 14 * 5 * temp;
		return Math.floorDiv(temp2, 14 * 121);
	}

	public static MarkType getMarkType(int t) {
		t = -t;
		// t = getData(0,t);
		return MarkType.values()[Math.floorDiv(t - 1, 2) % 7];
	}

	public static int getMarkInitPlies(int t) {
		t = -t;
		return Math.floorDiv(t, 14 * 121 * 5);
	}

	public static int modifyMarkLvl(int t, int x) {
		// t = getData(0,t);
		t -= 14 * 121 * x;
		return t;
	}

	public static int setMarkInitPlies(int t, int x) {
		t -= 14 * 121 * 5 * x;
		return t;
	}

	public static int getPieceHP(int t) {
		// t = getData(0,t);
		return Math.floorDiv(t, 47 * 121 * 4);
	}

	public static int getPieceType(int t) {
		// t = getData(0,t);
		int temp = Math.floorDiv(t, 47 * 121);
		int id = Math.floorDiv(t - 47 * 121 * temp, 121);
		return parseType(id);
	}

	public static int getPos(int t) {
		// t = getData(0,t);
		if (t < 0) {
			t = -t;
			int temp = Math.floorDiv(t, 14*121);
			int temp2 = t - 14*121*temp;
			return Math.floorDiv((temp2 + 1), 14);
		} 
		else {
			return t % 121;
		}
	}

	public static int setPos(int t, int pos) {
		// t = getData(0,t);
		t -= t % 121;
		t += pos;
		return t;
	}

	public static int modifyPieceHP(int t, int x) {
		// t = getData(0,t);
		t += 47 * 121 * 4 * x;
		return t;
	}

	public static int parseType(int id) {
		int type;

		String s = "237568";
		String s2 = "114111";

		if(id == 0)
		{
			System.out.println("piece "+id+" has malformed format!");
			return 0;
		}
		if (id < 23) {
			//System.out.println("id = " + id);
			int i = (int) Math.floor((id - 1) / 2.0);
			//System.out.println("i = " + i);
			type = Integer.parseInt(s.charAt(i < 6 ? i : 10 - i) + "");
		} 
		else if (id == 45 || id == 46) {
			type = 9;
		} 
		else {

			id -= 22;
			int i = (int) Math.floor((id - 1) / 2.0);
			type = Integer.parseInt(s2.charAt(i < 6 ? i : 10 - i) + "");
		}

		return type;
	}

	public static void modifyCounters(int actionType, int color, int type, boolean teleport) {
		int x = 0;

		if (actionType == 0) {
			x = -PieceData.moveCost(type, teleport);
		} 
		else if (actionType == 1) {
			x = -PieceData.pieceAC(type);
		} 
		else if (actionType == 2) {
			x = -PieceData.pieceDC(type);
		} 
		else if (actionType == 3) {
			x = PieceData.pieceValue(type);
		} 
		else if (actionType == 4) {
			if (color == 2) {
				App2.p2countersPerTurn.set(App2.p2countersPerTurn.get() - 1);
			} 
			else if (color == 1) 
			{
				App2.p1countersPerTurn.set(App2.p1countersPerTurn.get() - 1);
			}
			return;
		} 
		else if (actionType == 5) {
			if (type == 0) {
				if (color == 1) {
					App2.p1counters.set(App2.p1counters.get() - App2.p1countersPerTurn.get());
					App2.p1countersPerTurn.set(App2.p1countersPerTurn.get() + 1);
				} 
				else if (color == 2) 
				{
					App2.p2counters.set(App2.p2counters.get() - App2.p2countersPerTurn.get());
					App2.p2countersPerTurn.set(App2.p2countersPerTurn.get() + 1);
				}
				return;
			}
			else if(type == 1) {
				x = -GameData.getTeleportMarkCost(color);
			}
			else if(type == 2) {
				x = -GameData.getSpikeMarkCost(color);
			}
			else if(type == 3) {
				x = -GameData.getDefenseMarkCost(color);
			}
			else if (type == 4) {

				x = -GameData.getQueenMarkCost(color);
	
			}

		} 
		else if (actionType == 6) {
			if (color == 1) {
				x = -2 * App2.p1countersPerTurn.get();
			} 
			else if (color == 2) 
			{
				x = -2 * App2.p2countersPerTurn.get();
			}
		}

		if (color == 1) {
			App2.p1counters.set(App2.p1counters.get() + x);
		} 
		else if (color == 2) 
		{
			App2.p2counters.set(App2.p2counters.get() + x);
		}

	}



	/*
	 * iterate whole board and fill legal moves and legal range arrays, depending on
	 * when matrix,matrix2 returned from RuleSet contain '1' at current square
	 */

	public static void iterateBoard(int pos, int[] board) {
		int[][] matrix = null;
		int[][] matrix2 = null;
		int[][] matrix3 = null;

		boolean markRange = false;

		if (board[pos] < 0) {
			markRange = true;
			matrix3 = RuleSet.validateRange(1, 5);
		}

		if (!markRange) {
			matrix = RuleSet.validateMove(Utils.getColor(board[pos]), Utils.getPieceType(board[pos]));
			matrix2 = RuleSet.validateRange(Utils.getColor(board[pos]), Utils.getPieceType(board[pos]));
		}

		int i = (int) (pos / 11.0);
		int j = pos % 11;

		App2.movementRange = new ArrayList<int[]>();
		App2.placementRange = new ArrayList<int[]>();

		if (!markRange) {
			if (getPieceType(board[pos]) != 7) {
				// apply rotations
				if (Utils.getPieceRotation(board[pos]) == 90) {
					matrix = RuleSet.rotateMatrix(matrix, 1);
					matrix2 = RuleSet.rotateMatrix(matrix2, 1);
				} else if (Utils.getPieceRotation(board[pos]) == 270) {
					matrix = RuleSet.rotateMatrix(matrix, -1);
					matrix2 = RuleSet.rotateMatrix(matrix2, -1);
				} else if (Utils.getPieceRotation(board[pos]) == 180) {
					matrix = RuleSet.rotateMatrix(RuleSet.rotateMatrix(matrix, 1), 1);
					matrix2 = RuleSet.rotateMatrix(RuleSet.rotateMatrix(matrix2, 1), 1);
				}
			}

			else {
				ArrayList<int[]> set = RuleSet.iterateRange(i, j, board, true, true);
				for (int h = 0; h < set.size(); h++) {
					App2.placementRange.add(set.get(h));
				}
			}
		}

		for (int x = 0; x < 11; x++) {
			for (int y = 0; y < 11; y++) {
				try {
					// pieces use a matrix from RuleSet class, in which elements '1' mark the legal
					// squares in relation to current piece
					if (!markRange) {
						if (Utils.getPieceType(board[pos]) != 7 && matrix2[3 - (i - x)][3 - (j - y)] == 1) {
							App2.placementRange.add(new int[] { x, y });
						}

						// test for legality of the target square

						if (matrix[3 - (i - x)][3 - (j - y)] == 1 && board[x * 11 + y] == 0) {
							App2.movementRange.add(new int[] { x, y });
						}

					} else {
						if (matrix3[3 - (i - x)][3 - (j - y)] == 1) {
							App2.markRangeObs.add(new int[] { x, y });
						}
					}
				} catch (ArrayIndexOutOfBoundsException e2) {

				}
			}
		}

		for (int h = 0; h < 121; h++) {
			if (board[h] < 0 && Utils.getMarkType(board[h]) == MarkType.Teleport) {
				for (int c = 0; c < 121; c++) {
					if (h != c && board[c] < 0 && Utils.getMarkType(board[c]) == MarkType.Teleport) {
						App2.movementRange.add(new int[] { (int) (h / 11.0), h % 11 });
						App2.movementRange.add(new int[] { (int) (c / 11.0), c % 11 });
					}
				}
			}
		}

	}

	public static int makeId(int type, int color, int position) {
		if (type == 9) {
			if (color == 1) {
				return 45;
			} else if (color == 2) {
				return 46;
			}
		}
		if (position < 22) {
			return 2 * (position + 1);
		} else {
			return 2 * (120 - position) + 1;
		}

	}

	public static int getData(int slotIndex, int W) {
		System.out.println("starting data fetch for: " + W + " from slot " + slotIndex);

		int mod1 = W % 13;
		int mod2 = W % 19;

		if (slotIndex == 1) {
			System.out.println("data = " + W);
			// W = W - mod1;
			int x = 0;

			while ((W - 13 * x) % 19 != 0) {
				x++;
			}
			System.out.println("data = " + W);
			W = (W - 13 * x) / 19;
			System.out.println("data = " + W);
		} else if (slotIndex == 0) {
			System.out.println("data = " + W);
			// W = W - mod2;
			int x = 0;

			while ((W - 19 * x) % 13 != 0) {
				x++;
			}
			System.out.println("data = (W - 19*x) " + (W - 19 * x) + " W " + W);
			W = (W - 19 * x) / 13;
			System.out.println("data = " + W);
		}

		return W;
	}

	public static int pullData(int i, String string) {

		return 0;
	}

	public static boolean hasCounters(int color, String string) {
		
		int cost = 0;
		
		if(string.equals("mark upgrade"))
		{
			cost = GameData.markUpgradeCost(color);
		}
		else if(string.equals("mark placement"))
		{
			cost = GameData.markPlacementCost(color);
		}
		else if(string.equals("queen mark placement"))
		{
			cost = GameData.getQueenMarkCost(color);
		}
		else if(string.equals("teleport mark placement"))
		{
			cost = GameData.getTeleportMarkCost(color);
		}
		
		if (color == 1) {
			
			if(App2.p1counters.get() >= cost)
			{
				return true;
			}
		}
		else if(color == 2)
		{
			if(App2.p2counters.get() >= cost)
			{
				return true;
			}
		}
		
		return false;
	}
}
