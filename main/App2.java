package main;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class App2 extends Application {
	
	public static int[][][] board = new int[12][12][6];
	public static int[][][] tempBoard = new int[12][12][6];
	
	public static SimpleIntegerProperty turn = new SimpleIntegerProperty(1); //1 for black, 2 for red
	private static int markId = -1;
	public static int startCounters = 1;
	public static SimpleIntegerProperty halfTurns = new SimpleIntegerProperty(0);
	public static SimpleIntegerProperty counters = new SimpleIntegerProperty(0);
	public static SimpleIntegerProperty p1counters = new SimpleIntegerProperty(startCounters);
	public static SimpleIntegerProperty p2counters = new SimpleIntegerProperty(startCounters);
	public static SimpleIntegerProperty p1countersPerTurn = new SimpleIntegerProperty(0);
	public static SimpleIntegerProperty p2countersPerTurn = new SimpleIntegerProperty(0);
	public static SimpleIntegerProperty turnNumber = new SimpleIntegerProperty(0);
	public static SimpleIntegerProperty showingPiece = new SimpleIntegerProperty();
	private static int[] tempCounters = new int[4];; 
	
	private static boolean gameRunning = true;
	private static boolean attackMode = false;
	private static boolean defenseMode = false;
	private static boolean markingMode = false;
	private static boolean awaitingSquare = false;
	private static boolean isDefaultBoardRotation = true;
	private static Object[] legalMoves = null;
	private static Object[] legalRange = null;
	private static ObservableList<int[]> legalMovesObs = FXCollections.observableArrayList();
	private static ObservableList<int[]> legalRangeObs = FXCollections.observableArrayList();
	private static Pane startPane;
	ArrayList<int[]> list;
	ArrayList<int[]> list2;

	private boolean moved = false;
	private static int[] moveSource;
	private static int[] attackSource;
	private static int[] defenderSource;
	private static int[] moveTarget;
	private static int[] attackTarget;
	private static int[] defendTarget;
	private static int[] markTarget;

	private static Pane[][] sqArray = new Pane[12][12];
	private static GridPane grid = new GridPane();
	private static TextArea console = new TextArea();
	private static ScrollPane consoleContainer = new ScrollPane();
	private static StringBuilder sb = new StringBuilder();
	private static Color gridColor1 = new Color(85.0/255,80.0/255,79.0/255,(256-87.0)/255);
	private static Color gridColor2 = new Color(1,1,1,1);

	private static int pawnMaxAC = 1;
	private static int pawnMaxDC = 1;
	private static int pawnMC = 0;
	private static int pawnHP = 1;
	
	private static int swordsmanMaxAC = 4;
	private static int swordsmanMaxDC = 2;
	private static int swordsmanMC = 1;
	private static int swordsmanHP = 2;
	
	private static int vanguardMaxAC = 3;
	private static int vanguardMaxDC = 5;
	private static int vanguardMC = 2;
	private static int vanguardHP = 8;
	
	private static int scytheMaxAC = 1;
	private static int scytheMaxDC = 3;
	private static int scytheMC = 1;
	private static int scytheHP = 3;
	
	private static int princeMaxAC = 4;
	private static int princeMaxDC = 4;
	private static int princeMC = 2;
	private static int princeHP = 9;
	
	private static int guardianMaxAC = 4;
	private static int guardianMaxDC = 9;
	private static int guardianMC = 2;
	private static int guardianHP = 12;
	
	private static int spearmanMaxAC = 2;
	private static int spearmanMaxDC = 3;
	private static int spearmanMC = 2;
	private static int spearmanHP = 3;
	
	private static int generalMaxAC = 5;
	private static int generalMaxDC = 5;
	private static int generalMC = 3;
	private static int generalHP = 7;
	
	private static int queenMaxAC = 5;
	private static int queenMaxDC = 6;
	private static int queenMC = 3;
	private static int queenHP = 17;
	

	
	
	
	@Override
	public void start(Stage window) throws Exception {
		//AnchorPane fp = new AnchorPane();
		BorderPane main = new BorderPane();
		Scene scene = new Scene(main,1080,960);
		window.setScene(scene);
		window.show();
		
		//laying out all the elements
		Button start = new Button("start new game");
		Button load = new Button("load game");
		Button resign = new Button("resign");
		Button reset = new Button("reset");
		Button endTurn = new Button("end turn");
		Button exit = new Button("exit");	
		HBox menu = new HBox();
		HBox mainMenu = new HBox();
		HBox turnMenu = new HBox();
		VBox overview = new VBox();		
		VBox generalInfo = new VBox();		
		VBox pieceView = new VBox();
		VBox rightSide = new VBox(consoleContainer,overview);
		TextArea pieceInfo = new TextArea();
		pieceInfo.setPrefHeight(300);
		HBox pieceMenu = new HBox();		
		VBox markView = new VBox();	
		TextArea markInfo = new TextArea();
		HBox markMenu = new HBox();		
		Label p1counterText = new Label(String.valueOf(startCounters));
		Label p2counterText = new Label(String.valueOf(startCounters));
		Label p1countersPerTurnText = new Label("0");
		Label p2countersPerTurnText = new Label("0");
		p1countersPerTurn.set(0);
		p2countersPerTurn.set(0);
		endTurn.setPrefSize(100, 50);
		reset.setPrefSize(100, 50);
		
		//sidebar hierarchy
		generalInfo.getChildren().addAll(p1counterText,p1countersPerTurnText,p2counterText,p2countersPerTurnText,endTurn,reset);
		generalInfo.setSpacing(5);
		overview.getChildren().addAll(generalInfo,pieceView,markView);
		pieceView.getChildren().addAll(pieceInfo,pieceMenu);
		markView.getChildren().addAll(markInfo,markMenu);

		//bottom menu hierarchy
		mainMenu.getChildren().addAll(start,load,exit,resign);
		menu.getChildren().addAll(mainMenu, new Separator(), turnMenu);
		
		//sizings and anchors
		//consoleContainer.setPrefSize(500, 150);	
		consoleContainer.setContent(console);
		main.setLeft(initBoard(grid));
		main.setBottom(menu);
		main.setRight(rightSide);
		AnchorPane.setTopAnchor(menu, 750.0);
		AnchorPane.setLeftAnchor(consoleContainer, 750.0);
		AnchorPane.setTopAnchor(overview, 450.0);
		AnchorPane.setLeftAnchor(overview, 750.0);
		AnchorPane.setTopAnchor(pieceView, 800.0);
		AnchorPane.setLeftAnchor(pieceView, 750.0);
		
		//add everything to root node
		//fp.getChildren().addAll(initBoard(grid),menu,console,overview);

		
		turn.addListener((observableValue,oldValue,newValue)-> {
			
			System.out.println((newValue.intValue() == 1 ? "Black" : "Red")+ " turn started");
			
			if(newValue.intValue() == 1) {
				p1counters.set(p1counters.get() + p1countersPerTurn.get());
			}
			else {
				p2counters.set(p2counters.get() + p2countersPerTurn.get());
			}
			
			savePos(board);
			
		});
	
		
		
		//listener for tracking changes to legalMovesObs array, 
		//legalMoves gives all legal squares for the selected piece, each of which is marked on the board temporarily
		
		legalMovesObs.addListener(new ListChangeListener<int[]>() {

			@Override
			public void onChanged(Change arg0) {
				while(arg0.next()) {
					List<int[]> n = arg0.getAddedSubList();
					//get legal moves in an array, used to check moves legality after drag-and-drop was completed in redraw()
					legalMoves = n.toArray();
					System.out.println("lgt after adding: "+legalMoves.length);
					
					for (int[] is : n) {
						int x = is[0];
	 					int y = is[1];
						if(squareContent(x,y) != null) {
							//((Rectangle)(sqArray[y][x].getChildren().get(0))).setStyle("-fx-background-image: ");
							//((Rectangle)(sqArray[y][x].getChildren().get(0))).setFill(new Color(1.0, 0.3, 0.4, 1));
						}
						else {
							((ImageView)(sqArray[y][x].getChildren().get(1))).setImage(new Image(getClass().getClassLoader().getResource("resources/move_marker.png").toExternalForm(),64,64,true,true));
						}
					}
					
					List<int[]> n2 = arg0.getRemoved();
					for (int[] is : n2) {
						int x = is[0];
	 					int y = is[1];
	 					if(squareContent(x,y) != null) {
								//((Rectangle)(sqArray[y][x].getChildren().get(0))).setFill(new Color(85.0/255,80.0/255,79.0/255,(256-87.0)/255));
						}
						else {
							((ImageView)(sqArray[y][x].getChildren().get(1))).setImage(null);
						}
					}
				}

			}
			
			
		});
		
		
		//listener for tracking changes to legalRangeObs array,
		
		legalRangeObs.addListener(new ListChangeListener<int[]>() {

			@Override
			public void onChanged(Change arg0) {
				while(arg0.next()) {
					List<int[]> n = arg0.getAddedSubList();
					
					legalRange = n.toArray();
					
					System.out.println(n.size()+" is size");
					
					for (int[] is : n) {
						int x = is[0];
	 					int y = is[1];

	 					((ImageView)(sqArray[y][x].getChildren().get(1))).setImage(new Image(getClass().getClassLoader().getResource("resources/mark_marker.png").toExternalForm(),64,64,true,true));

					}
					List<int[]> n2 = arg0.getRemoved();
					for (int[] is : n2) {
						int x = is[0];
	 					int y = is[1];
	 					if(squareContent(x,y) != null) {
								//((Rectangle)(sqArray[y][x].getChildren().get(0))).setFill(new Color(85.0/255,80.0/255,79.0/255,(256-87.0)/255));
						}
						else {
							((ImageView)(sqArray[y][x].getChildren().get(1))).setImage(null);
						}
					}
				}
				
			}
			
		});
				
		
		scene.addEventHandler(KeyEvent.KEY_PRESSED, key -> {
			if(key.getCode() == KeyCode.SHIFT) {
				attackMode = true;
			}
			else if(key.getCode() == KeyCode.ALT) {
				markingMode = true;
			}
			else if(key.getCode() == KeyCode.CONTROL) {
				defenseMode = true;
			}

		});
		
		scene.addEventHandler(KeyEvent.KEY_RELEASED, key -> {
			if(key.getCode() == KeyCode.SHIFT) {
				attackMode = false;
			}
			else if(key.getCode() == KeyCode.ALT) {
				markingMode = false;
			}
			else if(key.getCode() == KeyCode.CONTROL) {
				defenseMode = false;
			}

		});

		
		p1counters.addListener((observableValue,oldValue,newValue) -> {
			p1counterText.setText("Black counters: "+String.valueOf(newValue));
		});
		
		p2counters.addListener((observableValue,oldValue,newValue) -> {
			p2counterText.setText("Red counters: "+String.valueOf(newValue));
		});
		
		p1countersPerTurn.addListener((observableValue,oldValue,newValue) -> {
			p1countersPerTurnText.setText("Black counters / turn: "+String.valueOf(newValue));
		});
		
		p2countersPerTurn.addListener((observableValue,oldValue,newValue) -> {
			p2countersPerTurnText.setText("Red counters / turn: "+String.valueOf(newValue));
		});
		
		reset.setOnAction(e -> {

			if(legalMovesObs.size() > 0) {
				legalMovesObs.clear();
			}
			resetPlayerCounters();
			arrayCopy(tempBoard,board);
			redraw(grid);
		});
		
		endTurn.setOnAction(e -> {
			setTurn(moveSource,moveTarget,markTarget,
					attackSource, attackTarget, defenderSource, defendTarget);
		});
		
		resign.setOnAction(e -> {
			resign(turn.intValue());
		});
		
		exit.setOnAction(e -> {
			Platform.exit();
		});
		
		
		
		showingPiece.addListener((observableValue,oldValue,newValue) -> {
			String s = "";
			switch(newValue.intValue()) {
			case 1:
				s = "Pawn \nAttack Cost: "+pawnMaxAC+"\nDefense Cost: "+pawnMaxDC+"\nMove cost: "+pawnMC+"\nValue: "+1;
				break;
			case 2:
				s = "Swordsman \nAttack Cost: "+swordsmanMaxAC+"\nDefense Cost: "+swordsmanMaxDC+"\nMove cost: "+swordsmanMC+"\nValue: "+4; 
				break;
			case 3:
				s = "Vanguard \nAttack Cost: "+vanguardMaxAC+"\nDefense Cost: "+vanguardMaxDC+"\nMove cost: "+vanguardMC+"\nValue: "+6; 
				break;
			case 4:
				s = "Scythe \nAttack Cost: "+scytheMaxAC+"\nDefense Cost: "+scytheMaxDC+"\nMove cost: "+scytheMC+"\nValue: "+5;
				break;
			case 5:
				s = "Prince \nAttack Cost: "+princeMaxAC+"\nDefense Cost: "+princeMaxDC+"\nMove cost: "+princeMC+"\nValue: "+6;
				break;
			case 6:
				s = "Guardian \nAttack Cost: "+guardianMaxAC+"\nDefense Cost: "+guardianMaxDC+"\nMove cost: "+guardianMC+"\nValue: 9";
				break;
			case 7:
				s = "Spearman \nAttack Cost: "+spearmanMaxAC+"\nDefense Cost: "+spearmanMaxDC+"\nMove cost: "+spearmanMC+"\nValue: 5";
				break;
			case 8:
				s = "General \nAttack Cost: "+generalMaxAC+"\nDefense Cost: "+generalMaxDC+"\nMove cost: "+generalMC+"\nValue: 15";
				break;
			}
			pieceInfo.setText(s);
		});

		
		
		console.textProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue,
					Object newValue) {
				console.setScrollTop(Double.MAX_VALUE);
			}
		});
		
		
	}
	
	private void savePos(int[][][] b) {
		tempCounters[0] = p1counters.get();
		tempCounters[1] = p1countersPerTurn.get();
		tempCounters[2] = p2counters.get();
		tempCounters[3] = p2countersPerTurn.get();
		arrayCopy(b,tempBoard);
		
	}
	
	private void resetPlayerCounters() {
		p1counters.set(tempCounters[0]);
		p1countersPerTurn.set(tempCounters[1]);
		p2counters.set(tempCounters[2]);
		p2countersPerTurn.set(tempCounters[3]);
		moved = false;
	}
	
	private void resign(int turn) {
		gameRunning = false;
		
		String s = "\n";
		s += turn == 1 ? "Red " : "Black ";
		s += "won by resignation";
		sb.append(s);
		console.setText(sb.toString());
		
	}
	
	private GridPane initBoard(GridPane grid) {
		
		//id, type, color, x, y, char, counters, hp, ac, dc, mc

		board[0][0] = new int[] {1,2,2,0,0,70,4,4, swordsmanMaxAC, swordsmanMaxDC, swordsmanMC};
		board[0][1] = new int[] {2,3,2,0,1,86,6,8, vanguardMaxAC, vanguardMaxDC, vanguardMC};
		board[0][2] = new int[] {3,7,2,0,2,83,5,3, spearmanMaxAC, spearmanMaxDC, spearmanMC};
		board[0][3] = new int[] {4,5,2,0,3,82,6,5, princeMaxAC, princeMaxDC, princeMC};
		board[0][4] = new int[] {5,6,2,0,4,71,9,12, guardianMaxAC, guardianMaxDC, guardianMC};
		board[0][5] = new int[] {6,8,2,0,5,75,15,6, generalMaxAC, generalMaxDC, generalMC};
		board[0][6] = new int[] {7,6,2,0,6,71,9,12, guardianMaxAC, guardianMaxDC, guardianMC};
		board[0][7] = new int[] {8,5,2,0,7,82,6,5, princeMaxAC, princeMaxDC, princeMC};
		board[0][8] = new int[] {9,7,2,0,8,83,5,3, spearmanMaxAC, spearmanMaxDC, spearmanMC};
		board[0][9] = new int[] {10,3,2,0,9,86,6,8, vanguardMaxAC, vanguardMaxDC, vanguardMC};
		board[0][10] = new int[] {11,2,2,0,10,70,4,4, swordsmanMaxAC, swordsmanMaxDC, swordsmanMC};
		board[0][11] = null;
		
		board[10][0] = new int[] {34,2,1,10,0,102,4,4, swordsmanMaxAC, swordsmanMaxDC, swordsmanMC};
		board[10][1] = new int[] {35,3,1,10,1,118,6,8, vanguardMaxAC, vanguardMaxDC, vanguardMC};
		board[10][2] = new int[] {36,7,1,10,2,115,5,3, spearmanMaxAC, spearmanMaxDC, spearmanMC};
		board[10][3] = new int[] {37,5,1,10,3,114,6,5, princeMaxAC, princeMaxDC, princeMC};
		board[10][4] = new int[] {38,6,1,10,4,103,9,12, guardianMaxAC, guardianMaxDC, guardianMC};
		board[10][5] = new int[] {39,8,1,10,5,107,15,6, generalMaxAC, generalMaxDC, generalMC};
		board[10][6] = new int[] {40,6,1,10,6,103,9,12, guardianMaxAC, guardianMaxDC, guardianMC};
		board[10][7] = new int[] {41,5,1,10,7,114,6,5, princeMaxAC, princeMaxDC, princeMC};
		board[10][8] = new int[] {42,7,1,10,8,115,5,3, spearmanMaxAC, spearmanMaxDC, spearmanMC};
		board[10][9] = new int[] {43,3,1,10,9,118,6,8, vanguardMaxAC, vanguardMaxDC, vanguardMC};
		board[10][10] = new int[]{44,2,1,10,10,102,4,4, swordsmanMaxAC, swordsmanMaxDC, swordsmanMC};
		board[11][11] = null;
		
		
		
		for(int i = 0; i<11; i++) {
			if(i == 3 || i == 7) {
				board[1][i] = new int[] {12+i,4,2,1,i,72,5,6,scytheMaxAC,scytheMaxDC,scytheMC};
				board[9][i] = new int[] {23+i,4,1,9,i,104,5,6,scytheMaxAC,scytheMaxDC,scytheMC};
				continue;
			}
			
			board[1][i] = new int[] {12+i,1,2,1,i,80,1,1,pawnMaxAC, pawnMaxDC, pawnMC};
			board[9][i] = new int[] {23+i,1,1,9,i,112,1,1,pawnMaxAC, pawnMaxDC, pawnMC};
			
		}
		
		for(int i = 0; i<11; i++) {
			for(int j = 2; j<9; j++) {
				board[j][i] = null;
			}
		}
		
		
		tempCounters = new int[] {1,0,1,0};
		arrayCopy(board,tempBoard);
		
		return redraw(grid);
		
		
	}
	
	private GridPane redraw(GridPane grid) {
		int i;
		int j;
		int i2 = 0;
		int j2 = 0;

		grid.getChildren().clear();

		for(i = isDefaultBoardRotation ? 0 : 11; i<12 && i > -1; i = isDefaultBoardRotation ? i + 1 : i - 1, i2++) {
			for(j = isDefaultBoardRotation ? 0 : 11; j<12 && j > -1; j = isDefaultBoardRotation ? j + 1 : j - 1, j2 = (j2+1)%12) {

				sqArray[i][j] = new Pane();
				Rectangle canvas = null;
				ImageView piece = new ImageView();

				
				if(i == 11) {
					Label l = new Label(""+(j+1));
					sqArray[i][j].getChildren().add(l);

				}
				else if(j == 11) {
					Label l = new Label(""+charConv(i));
					sqArray[i][j].getChildren().add(l);

				}
				
				if(i%2 == 0) {
					if(j%2 == 1) {
						canvas = new Rectangle(64,64,gridColor1);
					}
					else if (j%2 == 0) {
						canvas = new Rectangle(64,64,gridColor2);

					}
				}
				else {
					if(j%2 == 0) {
						canvas = new Rectangle(64,64,gridColor1);
					}
					else if (j%2 == 1) {
						canvas = new Rectangle(64,64,gridColor2);
					}
				}

				
				
				int temp = j;
				int temp2 = i;

				if(i != 11 && j != 11) {
					String id = board[j][i] == null ? "-" : (String.valueOf(board[j][i][5]));
					id += "_"+j+"_"+i+"_";
					sqArray[i][j].setId(id);
					sqArray[i][j].getChildren().add(canvas);
					sqArray[i][j].getChildren().add(piece);
				}
				

				if(squareContent(temp,temp2) != null) {
					if(squareContent(temp,temp2).equals("piece_m")) {
						piece.setImage(new Image(getClass().getClassLoader().getResource("resources/mark_area_r.png").toExternalForm(),64,64,true,true));	
					}
					else if(squareContent(temp, temp2).equals("piece_M")) {
						piece.setImage(new Image(getClass().getClassLoader().getResource("resources/mark_area_b.png").toExternalForm(),64,64,true,true));
					}
					else if(squareContent(temp,temp2).equals("piece_d")) {
						piece.setImage(new Image(getClass().getClassLoader().getResource("resources/mark_def.png").toExternalForm(),64,64,true,true));
					}
					else if(squareContent(temp,temp2).equals("piece_D")) {
						piece.setImage(new Image(getClass().getClassLoader().getResource("resources/mark_def.png").toExternalForm(),64,64,true,true));
					}
					else {
						piece.setImage(new Image(getClass().getClassLoader().getResource("resources/"+squareContent(temp,temp2)+".png").toExternalForm(),64,64,true,true));
					}
				}
				
				Pane p = sqArray[i][j];

				
				p.setOnMousePressed(e -> p.setMouseTransparent(true));
				p.setOnMouseReleased(e -> p.setMouseTransparent(false));
				
				p.setOnMousePressed(e -> {
					if(!gameRunning) {
						return;
					}
					
					((Rectangle)(p.getChildren().get(0))).setFill(new Color(0.3, 0.3, 0.3, 1));
					
					int t = temp;
					int t2 = temp2;
						
					//a == null OR a.color = turn, a not null AND a.color == turn, a == null,
					if(board[t][t2] == null || board[t][t2][2] == turn.get() || (board[t][t2][2] != turn.get() && attackMode)) {

						//reset legal moves / ranges
						legalMovesObs.clear();
						legalMoves = null;

						//iterate 11x11 board squares, get squares which represent legal moves / range for this piece
						if(board[t][t2] != null) {
							showingPiece.set(board[t][t2][1]);
							if(board[t][t2][0] > 0) {
								iterateBoard(p,t,t2,sqArray);
							}
						}
						
						//only reset legal range if piece was not selected
						if(!awaitingSquare) {
							legalRange = null;
							
							if(markingMode || attackMode || defenseMode) {
								getRange();					
								startPane = p;
								awaitingSquare = true;
							}
							//is moving
							else {
								if(!moved) {
									getLegalSquares();
								}
							}
						}
						//piece was selected, is current (temp,temp2) pane contained in legalRange, if so,
						//place mark OR attack OR defend
						else if(isLegal(temp,temp2,legalRange)){
							System.out.println("attackMode "+attackMode);
							if(markingMode) {
								if(((turn.get()==1 && p1counters.get() >= p1countersPerTurn.get()) ||
									(turn.get()==2 && p2counters.get() >= p2countersPerTurn.get())) 
									&& board[t][t2] == null) {
									
									placeMark(t,t2,grid);
									markTarget = new int[] {temp,temp2};
									
								}
								
							}
							else if(attackMode){
								System.out.println("£");
								parseId(startPane.getId(),1);
								System.out.println("#"+p1counters.get()+"#"+attackSource[0]);
								if((turn.get()==1 && p1counters.get() >= board[attackSource[1]][attackSource[2]][8]) ||
								   (turn.get()==2 && p2counters.get() >= board[attackSource[1]][attackSource[2]][8])
								    && !RuleSet.iterateRange(attackSource[1], attackSource[2], temp, temp2, board, true, true, true)) {
									
									attack(temp,temp2);
									((ImageView) ((Pane) sqArray[temp][temp2]).getChildren().get(1)).setImage(null);
									attackTarget = new int[] {temp,temp2};
								}
							}
							else if(defenseMode) {
								parseId(startPane.getId(),2);
								
								if((turn.get()==1 && p1counters.get() >= board[defenderSource[1]][defenderSource[2]][9]) ||
										(turn.get()==2 && p2counters.get() > board[defenderSource[1]][defenderSource[2]][9])) {
									defend(temp,temp2,piece);
									defendTarget = new int[] {temp,temp2};
								}
								
							}
							awaitingSquare = false;
							
						}
						else if(board[temp][temp2] != null && board[temp][temp2][2] == turn.get()) {
							System.out.println("%");
							if(markingMode || attackMode || defenseMode) {
								legalRangeObs.clear();
								getRange();		
								startPane = p;
							}
							else {
								if(!moved) {
									legalMovesObs.clear();
									getLegalSquares();
								}
								
							}
						}

					}

				});	
				
				p.setOnDragDetected((MouseEvent event) -> {
					if(!gameRunning) {
						return;
					}
					Dragboard db = p.startDragAndDrop(TransferMode.ANY);
					//db.setDragView(new Image(piece.getImage().getUrl(),64,64,true,true));
		            ClipboardContent content = new ClipboardContent();
		            content.putString(((ImageView) p.getChildren().get(1)).getImage().getUrl());
		            db.setContent(content);
		            event.consume();
				});
				
				p.setOnDragOver(new EventHandler<DragEvent>() {
					public void handle(DragEvent event) {
						if(event.getGestureSource() != p && event.getDragboard().hasString()) {
							event.acceptTransferModes(TransferMode.MOVE);
						}
						event.consume();
					}
				});
				
				p.setOnDragDropped((DragEvent event) -> {
					Dragboard db = event.getDragboard();
					if(db.hasString() && isLegal(temp,temp2,legalMoves)) {
						parseId(((Pane)event.getGestureSource()).getId(),0);
						

						if((turn.get()==1 && p1counters.get() >= board[moveSource[1]][moveSource[2]][10]) ||
								(turn.get()==2 && p2counters.get() >= board[moveSource[1]][moveSource[2]][10])) {
						
							Image newPiece = new Image(db.getString(),64,64,true,true);
							((ImageView) p.getChildren().get(1)).setImage(newPiece);
							((ImageView) ((Pane) event.getGestureSource()).getChildren().get(1)).setImage(null);
							event.setDropCompleted(true);
							
							makeMove(temp,temp2,grid);
							moveTarget = new int[] {temp,temp2};
						}
						
					}
					else {
						event.setDropCompleted(false);
					}
					event.consume();
				});
				
				
				
				
/*
 * p.setOnDragDetected(e -> { ((Rectangle)(p.getChildren().get(0))).setFill(new
 * Color(0.53, 0.59, 0.57, 1)); p.startFullDrag();
 * 
 * if(squareContent(temp,temp2) != null) { System.out.println("drag started"); }
 * e.consume(); });
 * 
 * 
 * p.setOnMouseDragEntered(e -> { Pane n = (Pane)(e.getGestureSource()); Image s
 * = ((ImageView) n.getChildren().get(1)).getImage(); int a =
 * Integer.parseInt(""+n.getId().charAt(0)); int b =
 * Integer.parseInt(""+n.getId().charAt(1));
 * ((sqArray[temp2][temp].getChildren().get(0))).setViewOrder(5);
 * ((Rectangle)(sqArray[temp2][temp].getChildren().get(0))).setFill(new
 * Color(0.2, 0.6, 0.4, 1));
 * ((sqArray[temp2][temp].getChildren().get(1))).setViewOrder(4);
 * ((ImageView)(sqArray[temp2][temp].getChildren().get(1))).setImage(s);
 * ((ImageView)(sqArray[a][b].getChildren().get(1))).setImage(null);
 * System.out.println("moving"); e.consume(); });
 * 
 * p.setOnMouseDragReleased(e -> { Pane n = (Pane)(e.getGestureSource());
 * 
 * System.out.println("drag dropped "+n.getId()); int a =
 * Integer.parseInt(""+n.getId().charAt(0)); int b =
 * Integer.parseInt(""+n.getId().charAt(1));
 * 
 * moveValidator(a, b, temp, temp2); e.consume(); });
 */


		
				GridPane.setConstraints(sqArray[i][j],i2,j2);
				grid.getChildren().add(sqArray[i][j]);

				
			}
		}
		console.setText(sb.toString());
		console.appendText("");

		return grid;
	}
	

	private boolean isLegal(int paneX, int paneY, Object[] list) {
		//System.out.println("lgth:"+legalMoves.length);
		
		int x,y;

		if(list == null) {
			return false;
		}
		
		
		for(int i = 0; i<list.length; i++) {
			x = ((int[])list[i])[0];
			y = ((int[])list[i])[1];
			System.out.println(x+" "+y+" "+paneX+" "+paneY);
			if(x == paneX && y == paneY) {
				System.out.println("is legal range");
				return true;
			}
		}
		return false;
	}
	
	
	private void parseId(String id,int mode){
		
		
		
		if(mode == 0) {
			moveSource = new int[3];
		}
		else if(mode == 1) {
			attackSource = new int[3];
		}
		else if(mode == 2) {
			defenderSource = new int[3];
		}
		
		
		int i = 0;
		int j = 0;
		String temp = "";

		
		while(i < id.length()) {
			if(id.charAt(i) == '_') {
				if(mode == 0) {
					moveSource[j] = Integer.parseInt(temp);
				}
				else if(mode == 1) {
					attackSource[j] = Integer.parseInt(temp);
				}
				else if(mode == 2) {
					defenderSource[j] = Integer.parseInt(temp);
				}
				
				temp = "";
				j++;
				i++;
				continue;
			}
			temp += id.charAt(i);
			i++;
		}
	}
	
	
	private void makeMove(int tX, int tY,GridPane grid) {
		

		int[] piece = board[moveSource[1]][moveSource[2]];
		
		//subtract move cost
		if(piece[2] == 1) {
			p1counters.set(p1counters.get() - piece[10]);
		}
		else if(piece[2] == 2) {
			p2counters.set(p2counters.get() - piece[10]);
		}
		
		//make the move from start coordinates parsed from start Pane id string
		board[tX][tY] = board[moveSource[1]][moveSource[2]];
		board[moveSource[1]][moveSource[2]] = null;
		redraw(grid);

		moved = true;
	}
	
	
	private void placeMark(int x, int y,GridPane grid) {
		int markChar;
		if(turn.get() == 1) {
			markChar = 109;
		}
		else {
			markChar = 77;
		}
		
		if(turn.get() == 1) {
			p1counters.set(p1counters.get()-p1countersPerTurn.get());
			p1countersPerTurn.set(p1countersPerTurn.get()+1);
		}
		else {
			p2counters.set(p2counters.get()-p2countersPerTurn.get());
			p2countersPerTurn.set(p2countersPerTurn.get()+1);
		}
		System.out.println("#");
		board[x][y] = new int[] {markId,-1,turn.get(),x,y,markChar,1};
		redraw(grid);
		markId--;
		
		if(RuleSet.checkAreas(turn.get(), board)) {
			gameRunning = false;
		}
	}
	
	
	private void attack(int x, int y) {
		//increment counters by piece value
		if(board[x][y][2]==1) {
			p2counters.set(p2counters.get()+board[x][y][6]);
		}
		else {
			p1counters.set(p1counters.get()+board[x][y][6]);
		}
		board[x][y] = null;
		redraw(grid);
	}
	
	//placing defense mark at square for one turn
	
	private void defend(int x, int y,ImageView p) {
		int markChar;
		if(turn.get() == 1) {
			markChar = 100;
		}
		else {
			markChar = 68;
		}
		board[x][y] = new int[] {markId,-3,turn.get(),x,y,markChar,1};
		p.setImage(new Image(getClass().getClassLoader().getResource("resources/mark_def.png").toExternalForm(),64,64,true,true));
	}
	
	
	/*
	 * turn = 1
	 * turnNo = 0
	 * halfTurns = 0
	 * 
	 * endTUrn ->
	 * 
	 * halfTurns = 0, 0 % 2 == 0
	 * 		turnNumber 0 -> 1
	 * sb append "1."
	 * sb append " Black"
	 * turn = 1 -> 1 % 2 = 1 + 1 = 2
	 * halfTurns 0 -> 1
	 * sb append " - move" \n
	 * --
	 * sb in total: "1. Black - move \n"
	 * 
	 * 
	 * halfTurns = 1, 1 % 2 != 0
	 * sb append "Turn 1."
	 * sb append " Red"
	 * turn 2 -> 2 % 2 = 0 + 1 = 1
	 * halfTurns 1 -> 2
	 * sb append " - move" \n
	 * --
	 * sb in total: "1. Red - move \n"
	 * 
	 * 
	 * halfTurns 2, 2%2 == 0
	 * 		turnNumber 1 -> 2
	 * sb append "Turn 2. "
	 * sb.append "Black"
	 * turn = 1 -> 1 % 2 = 1 + 1 = 2
	 * halfTurns = 2 -> 3
	 * sb append " - move \n"
	 * --
	 * sb in total: "Turn 2. Black - move \n"
	 * 
	 * 
	 * 		
	 * 
	 */
	
	private void setTurn(int[] movSource, int[] movTarget, int[] markTgt, 
			int[] attSource, int[] attTarget, int[] defSource, int[] defTarget) {
		
		int pieceChar = 0;
		int sX = 0;
		int sY = 0;
		int tX = 0;
		int tY = 0;
		int markX = 0;
		int markY = 0;
		int attackersChar = 0;
		int attSX = 0; 
		int attSY = 0;
		int attTX = 0;
		int attTY = 0;
		int defendersChar = 0;
		int defSX = 0;
		int defSY = 0;
		int defTX=0;
		int defTY=0;
		
		if(movSource != null) {
			pieceChar = movSource[0];
			sX = movSource[1];
			sY = movSource[2];
		}
		
		if(movTarget != null) {
			tX = movTarget[0];
			tY = movTarget[1];
		}
		
		if(markTgt != null) {
			markX = markTgt[0];
			markY = markTgt[1];
		}
		
		if(attSource != null) {
			attackersChar = attSource[0];
			attSX = attSource[1];
			attSY = attSource[2];
			
		}
		if(attTarget != null) {
			attTX = attTarget[0];
			attTY = attTarget[1];
		}
		if(defSource != null) {
			defendersChar = defSource[0];
			defSX = defSource[1];
			defSY = defSource[2];
			
		}
		if(defTarget != null) {
			attTX = defTarget[0];
			attTY = defTarget[1];
		}

		if(halfTurns.get() % 2 == 0) {		
			turnNumber.set(turnNumber.get()+1);
		}
		sb.append("\nTurn "+turnNumber.get()+". ");
		
		turn.set((turn.get() % 2)+1);
		halfTurns.set(halfTurns.getValue()+1);


		if(moveSource != null) {
			sb.append(" - "+(char)pieceChar+charConv(sY)+(sX+1)+" -> "+charConv(tY)+(tX+1)+"\n");
		}
		if(markTarget != null) {
			sb.append("mark placed at "+charConv(markY)+(markX+1)+"\n");
		}
		if(attSource != null) {
			sb.append("attacked "+charConv(attTY)+(attTX+1)+" by "+(char)attackersChar+" at "+charConv(attSY)+(attSX+1)+"\n");
		}
		if(defSource != null) {
			sb.append("defended "+charConv(defTY)+(defTX+1)+" by "+(char)defendersChar+" at "+charConv(defSY)+(defSX+1)+"\n");
		}
		
		if(!gameRunning) {
			sb.append((turn.get()-1 == 1 ? " Black " : "Red ")+"won by area capture");
		}
		else {
			sb.append("\n"+(turn.get()==1 ? "Black" : "Red"));
		}
		
		
		console.textProperty().set(sb.toString());
		console.setText(sb.toString());
		
		System.out.println("current turn "+turn+", turn no: "+turnNumber.get()+" halfturns: "+halfTurns.get()+"\n");

		moveSource = null;
		moveTarget = null;
		markTarget = null;
		attackSource = null;
		attackTarget = null;
		defenderSource = null;
		defendTarget = null;
		moved = false;

	}
	
	
	private String squareContent(int x, int y) {
		int[] element = board[x][y];

		
		if(element == null || x == 11 || y == 11) {
			return null;
		}

		
		return "piece_"+ (char) element[5];

	}
	
	
	private char charConv(int y) {
		switch(y) {
		case 0:
			return 'a';
		case 1:
			return 'b';
		case 2:
			return 'c';
		case 3:
			return 'd';
		case 4:
			return 'e';
		case 5:
			return 'f';
		case 6:
			return 'g';
		case 7:
			return 'h';
		case 8:
			return 'i';
		case 9:
			return 'j';
		case 10:
			return 'k';
		default:
			return 'x';
		}
	}
	
	private void iterateBoard(Pane p,int i,int j, Pane[][] sqArray) {

		int[][] matrix = RuleSet.validateMove(board[i][j][2], board[i][j][1]);
		int[][] matrix2 = RuleSet.validateRange( board[i][j][2], board[i][j][1]);
		list = new ArrayList<int[]>();
		list2 = new ArrayList<int[]>();
		
		
		
		if(board[i][j][1] == 7) {
			ArrayList<int[]> set = RuleSet.iterateRange(i, j, board, true, true);
			for (int h = 0; h < set.size();h++) {
				list2.add(set.get(h));
			}
		}
		
		for(int x = 0; x<11; x++) {
			for(int y = 0; y<11; y++) {
				try {

					//pieces use a matrix from RuleSet class, in which elements '1' mark the legal squares in relation to current piece

					if(board[i][j][1] != 7 && matrix2[3-(i-x)][3-(j-y)] == 1) {
						list2.add(new int[] {x,y});
					}
					

					//test for legality of the target square
					
					if(matrix[3-(i-x)][3-(j-y)] == 1 && board[x][y] == null) {
						list.add(new int[] {x,y});
					}
					
					//(i,j) = (1,0)
					//(x,y) = (0,0) = 1 for pawn
					//(3-1-0,3-0-0) = (2,3)
					
					//(i,j) = (7,7)
					//(x,y) = (0,0)
					//(3-(7-0),3-(7-0)) = (-4,-4)
					
					//(i,j) = (10,10)
					//(x,y) = (7,7)
					//(3-(10-7),3-(10-7) = (0,0)
					

					
				} catch (ArrayIndexOutOfBoundsException e2) {
					
				}
			}
		}

		
	}
	
	/*
	 * Adds filled list2 of legal squares (of pieces Range) to observable list legalRangeObs,
	 * triggering listener, which adds visual cues to those squares
	 */
	
	private void getRange() {
		legalRangeObs.addAll(list2);
		
	}
	
	/*
	 * Adds filled list of legal moves to observable list legalMovesObs,
	 * triggering listener, which adds visual cues to those squares
	 */
	
	private void getLegalSquares() {
		legalMovesObs.addAll(list);
	}
	
	
	/*
	 * Copies all elements from b1 into b2
	 */
	
	public void arrayCopy(int[][][] b1, int[][][] b2) {
		for(int i = 0; i < 12;i++) {
			for(int j = 0;j < 12;j++) {
				for(int k = 0; k < b1[0][0].length; k++) {
					b2[j][i] = b1[j][i];
				}
			}
		}
	}
	


	public static void main(String[] args) {
		launch(args);

	}

}