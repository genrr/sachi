package main;

/** 
 * @author genrr
 * 
 */

public class PieceData {

	private static int pawnAC = 1;
	private static int pawnDC = 1;
	private static int pawnMC = 0;
	private static int pawnValue = 1;
	
	private static int swordsmanAC = 2;
	private static int swordsmanDC = 3;
	private static int swordsmanMC = 1;
	private static int swordsmanValue = 3;
	
	private static int vanguardAC = 2;
	private static int vanguardDC = 2;
	private static int vanguardMC = 2;
	private static int vanguardValue = 5;
	
	private static int scytheAC = 2;
	private static int scytheDC = 4;
	private static int scytheMC = 1;
	private static int scytheValue = 3;
	
	private static int princeAC = 2;
	private static int princeDC = 4;
	private static int princeMC = 2;
	private static int princeValue = 6;
	
	private static int guardianAC = 3;
	private static int guardianDC = 1;
	private static int guardianMC = 2;
	private static int guardianValue = 6;
	
	private static int spearmanAC = 2;
	private static int spearmanDC = 6;
	private static int spearmanMC = 1;
	private static int spearmanValue = 5;
	
	private static int generalAC = 4;
	private static int generalDC = 0;
	private static int generalMC = 3;
	private static int generalValue = 9;
	
	private static int queenAC = 2;
	private static int queenDC = 4;
	private static int queenMC = 3;
	private static int queenValue = 8;
	
	static int[] moveCost = {0,pawnMC,swordsmanMC,vanguardMC,scytheMC,princeMC,guardianMC,spearmanMC,generalMC,queenMC};
	static int[] value = {0,pawnValue,swordsmanValue,vanguardValue,scytheValue,princeValue,guardianValue,spearmanValue,generalValue,queenValue};
	static int[] pieceAC = {0,pawnAC,swordsmanAC, vanguardAC, scytheAC, princeAC, guardianAC, spearmanAC, generalAC, queenAC};
	static int[] pieceDC = {0,pawnDC,swordsmanDC,vanguardDC,scytheDC,princeDC,guardianDC,spearmanDC,generalDC,queenDC};
	
	
	public static int moveCost(int type, boolean isTeleporting)
	{
		if(isTeleporting)
		{
			return 4;
		}
		else {
			return moveCost[type];
		}
		
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

	public static char pieceChar(int type) {
		switch(type) {
		case 1:
			return 'p';
		case 2:
			return 'f';
		case 3:
			return 'v';
		case 4:
			return 'h';
		case 5:
			return 'r';
		case 6: 
			return 'g';
		case 7:
			return 's';
		case 8:
			return 'k';
		case 9:
			return 'q';
		default:
			return 0;
		
		}
	}
}
