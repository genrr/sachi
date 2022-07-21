package main;

public class GameData {

	static final int MARK_UPGRADE_MULT = 2;
	static final int QUEEN_MARK_MULT = 2;
	
	//values which determine how many PLIES it takes for certain actions to happen after a specific mark is placed on the board
	public static int queenMarkTimer = 4;
	public static int scarletMarkTimer = 4;
	public static int spikeMarkTimer = 4;
	//how many plies is defense mark active on the board
	public static int defenseMarkTimer = 3;
	
	//costs
	public static int queenMarkCost;
	public static int spikeMarkCost = 1;
	public static int defenseMarkCost = 2; 
	
	public static int markUpgradeCost(int color)
	{
		if(color == 1) {
			return MARK_UPGRADE_MULT*App2.p1countersPerTurn.get();
		}
		else if(color == 2)
		{
			return MARK_UPGRADE_MULT*App2.p2countersPerTurn.get();
		}
		
		return 0;
	}
	
	public static int markPlacementCost(int color)
	{
		if(color == 1) {
			return App2.p1countersPerTurn.get();
		}
		else if(color == 2)
		{
			return App2.p2countersPerTurn.get();
		}
		
		return 0;
	}
	
	public static int getQueenMarkCost(int color) {
//		
//		if(color == 1) {
//			return QUEEN_MARK_MULT * App2.p1countersPerTurn.get() - queenMarkCost;
//		}
//		else if(color == 2)
//		{
//			return QUEEN_MARK_MULT * App2.p2countersPerTurn.get() - queenMarkCost;
//		}


		return 1;
	}
	
	public static int getSpikeMarkCost(int color) {
		return 1;
	}
	
	public static int getDefenseMarkCost(int color)
	{
		return 1;
	}
	
	public static int getTeleportMarkCost(int color)
	{
		return 1;
	}
	
}
