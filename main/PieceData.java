package main;

public class PieceData {

	private static int pawnAC = 1;
	private static int pawnDC = 1;
	private static int pawnMC = 0;
	private static int pawnValue = 1;
	
	private static int swordsmanAC = 4;
	private static int swordsmanDC = 2;
	private static int swordsmanMC = 1;
	private static int swordsmanValue = 4;
	
	private static int vanguardAC = 3;
	private static int vanguardDC = 5;
	private static int vanguardMC = 2;
	private static int vanguardValue = 6;
	
	private static int scytheAC = 1;
	private static int scytheDC = 3;
	private static int scytheMC = 1;
	private static int scytheValue = 5;
	
	private static int princeAC = 4;
	private static int princeDC = 4;
	private static int princeMC = 2;
	private static int princeValue = 6;
	
	private static int guardianAC = 4;
	private static int guardianDC = 9;
	private static int guardianMC = 2;
	private static int guardianValue = 9;
	
	private static int spearmanAC = 2;
	private static int spearmanDC = 3;
	private static int spearmanMC = 2;
	private static int spearmanValue = 5;
	
	private static int generalAC = 5;
	private static int generalDC = 0;
	private static int generalMC = 3;
	private static int generalValue = 15;
	
	private static int queenAC = 5;
	private static int queenDC = 6;
	private static int queenMC = 3;
	private static int queenValue = 32;
	
	static int[] moveCost = {0,pawnMC,swordsmanMC,vanguardMC,scytheMC,princeMC,guardianMC,spearmanMC,generalMC,queenMC};
	static int[] value = {0,pawnValue,swordsmanValue,vanguardValue,scytheValue,princeValue,guardianValue,spearmanValue,generalValue,queenValue};
	static int[] pieceAC = {0,pawnAC,swordsmanAC, vanguardAC, scytheAC, princeAC, guardianAC, spearmanAC, generalAC, queenAC};
	static int[] pieceDC = {0,pawnDC,swordsmanDC,vanguardDC,scytheDC,princeDC,guardianDC,spearmanDC,generalDC,queenDC};
	
	
	public static int moveCost(int type)
	{
		return moveCost[type];
	}
	
	public static int pieceValue(int type)
	{
		return value[type];
	}
	
	public static int pieceAC(int type)
	{
		return pieceAC[type];
	}
	
	public static int pieceDC(int type)
	{
		return pieceDC[type];
	}
	
	public static int pieceHP(int type)
	{
		if(type == 8 || type == 9)
		{
			return 2;
		}
		else
		{
			return 0;
		}
	}
}
