package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.time.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import network.Mesh;
import network.Message;

/** 
 * @author genrr
 * 
 */

public class App2 extends Application {
	
	public static int[] board = new int[121];
	public static int[] tempBoard = new int[121];
	ArrayList<int[]> boardList = new ArrayList<int[]>();
	private Mesh mesh;
	private ArrayList<int[]> move = new ArrayList<int[]>();
	
	public static int plies = 2;
	static int h = 0;
	int[] specialPos = new int[] {-1,-1,-1,-1,-1,-1};
	
	public static SimpleIntegerProperty turn = new SimpleIntegerProperty(1); //1 for black, 2 for red
	private static int markId = -1;
	private static int pieceIdCap = 45;
	public static int startCounters = 1;
	public static int startIncome = 0;
	public static int playerColor = -1;
	private static int otherPlayerColor = -2;
	private static boolean otherPlayerReady = false;
	private static boolean victoryAchieved = false;
	public static SimpleIntegerProperty counters = new SimpleIntegerProperty(0);
	public static SimpleIntegerProperty p1counters = new SimpleIntegerProperty(startCounters);
	public static SimpleIntegerProperty p2counters = new SimpleIntegerProperty(startCounters);
	public static SimpleIntegerProperty p1countersPerTurn = new SimpleIntegerProperty(startIncome);
	public static SimpleIntegerProperty p2countersPerTurn = new SimpleIntegerProperty(startIncome);
	public static SimpleIntegerProperty showingPiece = new SimpleIntegerProperty();
	private static int[] tempCounters = new int[4]; 
	
	private static boolean gameRunning = true;
	private static boolean freePlay = true;
	private static boolean attackMode = false;
	private static boolean defenseMode = false;
	private static boolean markingMode = false;
	private static boolean queensMarkPlacementMode = false;
	private static boolean defenseMarkPlacementMode = false;
	private static boolean spikeMarkPlacementMode = false;
	private static boolean awaitingSquare = false;
	private static boolean moved = false;
	private static boolean markPlaced = false;
	private static boolean hasAttacked = false;
	private static boolean hasDefended = false;
	private static boolean hasResignedPiece = false;
	private static ArrayList<Integer> queensMarkPieceIDList = new ArrayList<Integer>();
	private static boolean isDefaultBoardRotation = true;
	private static Object[] legalMoves = null;
	private static Object[] legalRange = null;
	private static ObservableList<int[]> legalMovesObs = FXCollections.observableArrayList();
	private static ObservableList<int[]> legalRangeObs = FXCollections.observableArrayList();
	public static ObservableList<int[]> markRangeObs = FXCollections.observableArrayList();
	private static ObservableList<int[]> capturedPieces = FXCollections.observableArrayList();
	private static Pane startPane;
	public static ArrayList<int[]> movementRange;
	public static ArrayList<int[]> placementRange;
	ArrayList<int[]> ARIntersection = new ArrayList<int[]>();

	
	private static int moveSource = 0;
	private static int attackSource = 0;
	private static int defenderSource = 0;
	private static int moveTarget = 0;
	private static int markSource = 0;
	private static int resignedPieceType = 0;
	private static boolean hasQueensMarkOrQueen = false;
	private static int rotatedPieceLoc = -1;
	private static int previousRotation = 0;

	private static Pane[][] sqArray = new Pane[12][12];
	private static GridPane grid = new GridPane();
	private static TextArea console = new TextArea();
	private static TextArea pieceInfo = new TextArea();
	public static TextArea statusConsole = new TextArea();
	private static StringBuilder sb = new StringBuilder();
	private static Color gridColor1 = new Color(85.0/255,80.0/255,79.0/255,(256-87.0)/255);
	private static Color gridColor2 = new Color(1,1,1,1);

	int soldier = 1;
	int swordsman = 2;
	int vanguard = 3;
	int scythe = 4;
	int prince = 5;
	int guardian = 6;
	int spearman = 7;
	int general = 8;
	int queen = 9;

	HBox pieceMenu;
	Button rotateClockwise = new Button("rotate clockwise");
	Button rotateCounterClockwise = new Button("rotate counterclockwise");
	Button promoteSoldier = new Button("promote to General");
	Button placeQueensMark = new Button("place Queens mark");
	Button placeSpikeMark = new Button("place Spike mark");
	Button placeTeleportMark = new Button("place Teleport mark");
	Button placeFortressMark = new Button("place Fortress mark");
	Button placeScarletMark = new Button("place Scarlet mark");
	Button resignPiece = new Button("resign piece");
	
	private boolean loading = false;
	private boolean saving = false;
	private static Random rng = new Random();
	
	//values which determine how many PLIES it takes for certain actions to happen after a specific mark is placed on the board
	public static int queenMarkTimer = 4;
	public static int scarletMarkTimer = 2;
	public static int spikeMarkTimer = 4;
	//how many plies is defense mark active on the board
	public static int defenseMarkTimer = 3;
	
	//costs
	public static int queenMarkCost;
	public static int spikeMarkCost = 1;
	public static int defenseMarkCost = 2; 
	
	/*
	 * GUI element initialization
	 * GUI event handlers
	 */
	
	@Override
	public void start(Stage window) throws Exception {
		System.out.println("this App2 ref: "+this);
		
		BorderPane main = new BorderPane();
		Scene scene = new Scene(main,1250,960);
		window.setScene(scene);
		window.show();
	
		//laying out all the elements
		Button start = new Button("host new game");
		Button connect = new Button("connect to game");
		Button chooseColor1 = new Button("black");
		Button chooseColor2 = new Button("red");
		Button startGame = new Button("start");
		Button randomColor = new Button("random");
		Button load = new Button("load game");
		Button save = new Button("save game");
		Button resetGame = new Button("reset game");
		Button resign = new Button("resign");
		Button reset = new Button("reset");
		Button endTurn = new Button("end turn");
		Button exit = new Button("exit");	
		Label turnInfoLabel = new Label("Black");
		HBox menu = new HBox();
		HBox mainMenu = new HBox();
		HBox turnMenu = new HBox();
		VBox overview = new VBox();		
		VBox generalInfo = new VBox();		
		VBox pieceView = new VBox();
		VBox rightSide = new VBox(console,overview);
		pieceInfo.setPrefHeight(300);
		pieceMenu = new HBox();		
		VBox markView = new VBox();	
		HBox markMenu = new HBox();		
		Label p1counterText = new Label("Black counters: "+String.valueOf(startCounters));
		Label p2counterText = new Label("Red counters: "+String.valueOf(startCounters));
		Label p1countersPerTurnText = new Label("Black counters per turn: "+String.valueOf(startIncome));
		Label p2countersPerTurnText = new Label("Red counters per turn: "+String.valueOf(startIncome));
		chooseColor1.setVisible(false);
		chooseColor2.setVisible(false);
		randomColor.setVisible(false);
		startGame.setVisible(false);
		p1countersPerTurn.set(0);
		p2countersPerTurn.set(0);
		
		mainMenu.setSpacing(5);
		overview.setSpacing(5);
		generalInfo.setSpacing(5);
		startGame.setPrefSize(65, 100);
		endTurn.setPrefSize(100, 50);
		reset.setPrefSize(100, 50);
		start.setFocusTraversable(false);
		connect.setFocusTraversable(false);
		load.setFocusTraversable(false);
		save.setFocusTraversable(false);
		resetGame.setFocusTraversable(false);
		exit.setFocusTraversable(false);
		resign.setFocusTraversable(false);
		endTurn.setFocusTraversable(false);
		reset.setFocusTraversable(false);
		resignPiece.setFocusTraversable(false);
		promoteSoldier.setFocusTraversable(false);
		rotateClockwise.setFocusTraversable(false);
		rotateCounterClockwise.setFocusTraversable(false);
		placeQueensMark.setFocusTraversable(false);
		placeScarletMark.setFocusTraversable(false);
		console.setFocusTraversable(false);
		pieceInfo.setFocusTraversable(false);
		statusConsole.setFocusTraversable(false);
		
		pieceMenu.getChildren().addAll(resignPiece);
		
		//sidebar hierarchy
		generalInfo.getChildren().addAll(p1counterText,p1countersPerTurnText,p2counterText,p2countersPerTurnText,endTurn,reset,turnInfoLabel);	
		overview.getChildren().addAll(generalInfo,pieceView,markView);
		pieceView.getChildren().addAll(pieceInfo,pieceMenu);
		markView.getChildren().addAll(statusConsole,markMenu);

		//bottom menu hierarchy
		mainMenu.getChildren().addAll(start,connect,chooseColor1,chooseColor2,randomColor, resetGame, save,load,exit,resign,startGame);
		menu.getChildren().addAll(mainMenu, new Separator(), turnMenu);
		
		
		saveGameState();
		console.textProperty().set(sb.toString());
    	sb.append("-------------------------------\nTurn 1:\n");
    	console.setText(sb.toString());
		
		window.setOnCloseRequest(e -> {
			if(mesh != null) {
				mesh.close();
			}
			Platform.exit();
		});
		
		
		for(int i = 0; i < 12; i++) {
			for(int j = 0; j < 12; j++) {
				
				sqArray[i][j] = new Pane();
				Pane p = sqArray[i][j];
				p.setOnMousePressed(e -> p.setMouseTransparent(true));
				p.setOnMouseReleased(e -> p.setMouseTransparent(false));
				int boardPos = j * 11 + i;
				int temp = j;
				int temp2 = i;
				
				Rectangle canvas = null;
				ImageView piece = new ImageView();
				ImageView squareEffectFrame = new ImageView();
				Label hpCounter = new Label();
				
				if(i%2 == 0) 
				{
					if(j%2 == 1) 
					{
						canvas = new Rectangle(64,64,gridColor1);
					}
					else if (j%2 == 0)
					{
						canvas = new Rectangle(64,64,gridColor2);

					}
				}
				else {
					if(j%2 == 0)
					{
						canvas = new Rectangle(64,64,gridColor1);
					}
					else if (j%2 == 1) 
					{
						canvas = new Rectangle(64,64,gridColor2);
					}
				}
				
				if(i != 11 && j != 11)
				{
					sqArray[i][j].getChildren().add(canvas);
					sqArray[i][j].getChildren().add(piece);
					sqArray[i][j].getChildren().add(hpCounter);
					sqArray[i][j].getChildren().add(squareEffectFrame);
					hpCounter.setAlignment(Pos.BOTTOM_RIGHT);
				}			
				

				p.setOnMousePressed(e -> {
		
					if(!gameRunning) {
						return;
					}
			
					((Rectangle)(p.getChildren().get(0))).setFill(new Color(0.3, 0.3, 0.3, 1));
					
					int t = temp;
					int t2 = temp2;
				
					
					//a == null OR (freeplay AND selected turn-colored piece) OR (online AND selected own piece) OR
					//freeplay AND selected opponents-turn-colored piece AND attackmode OR
					//online AND selected opponents piece AND attackmode
					
					/*
					 * a == null ||
					 * (freePlay && ((a.color == turn.get()) || attackMode)) ||
					 * a.color == playerColor || attackMode
					 */
					if(board[boardPos] == 0 || (freePlay && Utils.isColor(board[boardPos],turn.get())) ||
							(!freePlay && Utils.isColor(board[boardPos], playerColor)) ||
							(freePlay && !Utils.isColor(board[boardPos],turn.get()) && attackMode) ||
							(!freePlay && !Utils.isColor(board[boardPos], playerColor) && attackMode)) {
						
						//(a && b) || (!a && c) || (d && ((a && !b) || (!a && !c)))
						
						System.out.println("t "+t+" t2 "+t2+" awaiting = "+awaitingSquare+" legalrange = "+(legalRange != null ? legalRange.length : "null"));
						
						//reset legal moves / ranges
						legalMovesObs.clear();
						legalMoves = null;
					
						//iterate 11x11 board squares, get squares which represent legal moves / range for this piece
						if(board[boardPos] != 0) {
							showingPiece.set(boardPos);
							if(board[boardPos] > 0) {
								Utils.iterateBoard(boardPos,board);
							}
						}
						
						//only reset legal range if piece was not selected
						if(!awaitingSquare) {
							System.out.println("clicked on "+boardPos+" ("+t+", "+t2+") while not awaiting square");
							legalRange = null;
							getRange();					
							startPane = p;
							awaitingSquare = true;
		
							if(!moved) {
								getLegalSquares();
							}
							
						}
						//piece was selected, is current (temp,temp2) pane contained in legalRange, if so,
						//place mark OR attack OR defend
						else if(isLegal(t,t2,legalRange)){
							
							System.out.println("clicked on "+boardPos+" ("+t+", "+t2+") while awaiting square AND its legal square!");
							
							if(markingMode) 
							{
								if(!markPlaced)
								{
									if(((turn.get()==1 && p1counters.get() >= p1countersPerTurn.get()) || (turn.get()==2 && p2counters.get() >= p2countersPerTurn.get())) ) 
									{
										if(board[boardPos] == 0)
										{
											if(markSource != attackSource)
											{
												placeMark(t * 11 + t2, MarkType.Area);
											}
											else
											{
												statusConsole.appendText("cannot place a mark with a piece that has attacked this turn!\n");
											}											
										}
										else if(board[boardPos] < 0 && Utils.isColor(board[boardPos], turn.get()))
										{
											if(Utils.getMarkLvl(board[boardPos]) < 5)
											{
												if(Utils.getMarkType(board[boardPos]) != MarkType.Queen)
												{
													if(((turn.get()==1 && p1counters.get() >= 2*p1countersPerTurn.get()) || (turn.get()==2 && p2counters.get() >= 2*p2countersPerTurn.get())) )
													{
														upgradeMark(boardPos);
													}
													else
													{
														statusConsole.appendText("not enough counters for mark upgrade!\n");
													}													
												}
												else
												{
													statusConsole.appendText("queen mark is not upgradeable!\n");
												}												
											}
											else
											{
												statusConsole.appendText("mark is at maximum level!\n");
											}											
										}
										else
										{
											statusConsole.appendText("target square not empty!\n");
										}										
									}
									else {
										statusConsole.appendText("not enough counters!\n");
									}
								}
								else
								{
									statusConsole.appendText("mark already placed!\n");
								}
								
							}
							else if(attackMode)
							{
		
								//System.out.println("#"+p1counters.get()+"#"+attackSource[0]+"#"+attackSource[1]+"#"+attackSource[2]);
								if(board[boardPos] != 0)
								{
										if((turn.get()==1 && p1counters.get() >= PieceData.pieceAC(Utils.getPieceType(board[attackSource]))) ||
									   (turn.get()==2 && p2counters.get() >= PieceData.pieceAC(Utils.getPieceType(board[attackSource]))))
										{
											if(!RuleSet.iterateRange(attackSource, boardPos, board, true, true, true))
											{
												if(!hasAttacked)
												{
													if(moveTarget != attackSource)
													{
														if(markSource != attackSource)
														{
															rangedAction(RangedType.Attack, attackSource, t * 11 + t2);
															((ImageView) ((Pane) sqArray[t][t2]).getChildren().get(1)).setImage(null);
														}
														else
														{
															statusConsole.appendText("cannot attack with a piece that placed a mark during the turn!\n");
														}
														
													}
													else
													{
														statusConsole.appendText("cannot attack with a piece that was moved during the turn!\n");
													}
												
												}
												else
												{
													statusConsole.appendText("already attacked during the turn!\n");
												}
												
											}
											else
											{
												statusConsole.appendText("a piece blocking the attack!\n");
											}
									}
									else
									{
										statusConsole.appendText("not enough counters!\n");
									}
								}
								else
								{
									statusConsole.appendText("target square is empty!\n");
								}
		
		
							}
							else if(defenseMode) {
								//parseId(startPane.getId(),2);
								
								if((turn.get()==1 && p1counters.get() >= PieceData.pieceDC(Utils.getPieceType(board[defenderSource]))) ||
										(turn.get()==2 && p2counters.get() > PieceData.pieceDC(Utils.getPieceType(board[defenderSource])))) {
									
									if(!hasDefended) 
									{
										if(Utils.isColor(board[defenderSource], turn.get()) && Utils.isColor(board[boardPos], turn.get()))
										{
											rangedAction(RangedType.Defend, defenderSource, t * 11 + t2);
										}
										else
										{
											statusConsole.appendText("defend source or target the wrong color!\n");
										}
										
									}
									else
									{
										statusConsole.appendText("have already defended a piece during this turn!\n");
									}
									
								}
								else
								{
									statusConsole.appendText("not enough counters!\n");
								}
								
							}
							else if(queensMarkPlacementMode)
							{
								System.out.println("queens mark placement");
								for(int[] is : ARIntersection)
								{
									
									System.out.println("temp : "+t+" temp2 : "+t2+" is[1] : "+is[1]+" is[0] : "+is[0]);
									if(board[boardPos] == 0 && is[0] == t && is[1] == t2)
									{
										for(int m = 0; m<121; m++)
										{
												if(board[m] != 0 && Utils.isColor(board[m], turn.get()) && 
														((board[m] < 0 && Utils.getMarkType(board[m]) == MarkType.Queen) || (Utils.getPieceType(board[m]) == queen)))
												{
													hasQueensMarkOrQueen = true;
												}
										}
										if(!hasQueensMarkOrQueen)
										{
		
											if((turn.get() == 1 && p1counters.get() >= Utils.computeQueenMarkCost(p1countersPerTurn.get())) || 
											(turn.get() == 2 && p2counters.get() >= Utils.computeQueenMarkCost(p2countersPerTurn.get())))
											{
												placeMark(boardPos, MarkType.Queen);
											}
											else
											{
												statusConsole.appendText("not enough counters!\n");
											}
											
										}
										else
										{
											statusConsole.appendText("only one Queen or Queen's Mark can exist on the board at the same time!\n");
										}
									}
								}							
							}
							else if(board[boardPos] == 0)
							{
								if(spikeMarkPlacementMode)
								{
									System.out.println("spike mark placement!");
									if((turn.get() == 1 && p1counters.get() >= spikeMarkCost) ||
											turn.get() == 2 && p2counters.get() >= spikeMarkCost) {
										System.out.print("placing spike mark!\n");
										placeMark(boardPos, MarkType.Spike);
									}
									else {
										statusConsole.appendText("not enough counters!\n");
									}
								}
								else if(defenseMarkPlacementMode)
								{
									System.out.println("defense mark placement!");
									if((turn.get() == 1 && p1counters.get() >= defenseMarkCost) ||
											turn.get() == 2 && p2counters.get() >= defenseMarkCost) {
										System.out.print("placing defense mark!\n");
										placeMark(boardPos, MarkType.Defense);
									}
									else {
										statusConsole.appendText("not enough counters!\n");
									}
								}
								
								
							}
							
							awaitingSquare = false;
							
						}
						else if(board[boardPos] != 0 && Utils.isColor(board[boardPos], turn.get())) 
						{
							
							System.out.println("clicked on "+boardPos+" ("+t+", "+t2+") while awaiting square AND square has own piece in it!");
		
							legalRangeObs.clear();
							getRange();		
							startPane = p;
		
		
							if(!moved) {
								legalMovesObs.clear();
								getLegalSquares();
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
					int t = temp;
					int t2 = temp2;
					if(db.hasString() && isLegal(t,t2,legalMoves)) 
					{
						moveSource = showingPiece.get();						
		
						if((turn.get() == 1 && p1counters.get() >= PieceData.moveCost(Utils.getPieceType(board[moveSource]),false)) ||
								(turn.get() == 2 && p2counters.get() >= PieceData.moveCost(Utils.getPieceType(board[moveSource]),false))) 
						{
							if(moveSource != attackSource)
							{
								
								Image newPiece = new Image(db.getString(),64,64,true,true);
								((ImageView) p.getChildren().get(1)).setImage(newPiece);
								((ImageView) ((Pane) event.getGestureSource()).getChildren().get(1)).setImage(null);
								event.setDropCompleted(true);
								
								if(board[t * 11 + t2] != 0)
								{
									modifyAction(ModifyType.Teleport, moveSource, t * 11 + t2);
								}
								else
								{
									modifyAction(ModifyType.Move, moveSource, t * 11 + t2);
								}							
								
								moveTarget = t * 11 + t2;
							}
							else
							{
								statusConsole.appendText("cannot move with a piece that has attacked this turn!\n");
							}							
						}
						else
						{
							statusConsole.appendText("not enough counters!\n");
						}
						
					}
					else {
						event.setDropCompleted(false);
					}
					event.consume();
				});
				
			}
		}
		
		
		//sizings
		console.setPrefSize(200, 200);
		main.setLeft(initBoard(grid));
		main.setBottom(menu);
		main.setRight(rightSide);
		BorderPane.setMargin(rightSide, new Insets(25));
		
		/*
		 * Connection pop-up
		 */
		
		Stage window3 = new Stage();
		HBox cBoxCont = new HBox();
		Scene connectBox = new Scene(cBoxCont,600,260);
		TextField addressField = new TextField();
		Button submit = new Button("submit");
		cBoxCont.getChildren().addAll(addressField, submit);
		window3.setScene(connectBox);
		
		connectBox.addEventHandler(KeyEvent.KEY_PRESSED, key -> {
			if(key.getCode() == KeyCode.ESCAPE)
			{
				window3.close();
			}
		});
		
		connectBox.addEventHandler(KeyEvent.KEY_PRESSED, key -> {
			if(key.getCode() == KeyCode.ENTER)
			{
				try {
		            String address = addressField.getText();
		            statusConsole.appendText("Connecting to server at "+address+":2000\n");
		            mesh.connect(address, 2000);
		        } catch(Exception e1) {
		            e1.printStackTrace();
		        }
				window3.close();
			}
		});
		
		submit.setOnAction(e -> {
			try {
	            String address = addressField.getText();
	            statusConsole.appendText("Connecting to server at "+address+":2000\n");
	            mesh.connect(address, 2000);
	        } catch(Exception e1) {
	            e1.printStackTrace();
	        }
			window3.close();
		});


		/*
		 * save/load dialog
		 */
		
		Stage window4 = new Stage();
		ScrollPane saveGamePane = new ScrollPane();
		VBox cont = new VBox();
		Pane[] slots = new Pane[15];
		FileReader freader;
		
		for(int i = 0; i<15; i++)
		{			
			slots[i] = new Pane();
			Rectangle slotBg = new Rectangle(250,100,new Color(1,1,1,1));
			slots[i].setPrefSize(260, 100);
			Label slotText = new Label();
			slotText.setPadding(new Insets(25));
			VBox.setMargin(slots[i], new Insets(10));
			slots[i].getChildren().addAll(slotBg,slotText);
			cont.getChildren().add(slots[i]);
			
			int index = i;
			
			slots[i].setOnMouseEntered(e -> {
				((Rectangle)(slots[index].getChildren().get(0))).setFill(new Color(0.1,0.1,0.1,1));
			});
			
			slots[i].setOnMouseExited(e -> {
				((Rectangle)(slots[index].getChildren().get(0))).setFill(new Color(1,1,1,1));
			});
			
			try 
			{
				freader = new FileReader("savegame-"+ i +".txt");
				BufferedReader br = new BufferedReader(freader);
				String s = "";
				try 
				{
					s = br.readLine();
					slotText.setText(s);
				} 
				catch (IOException e1) {
					statusConsole.appendText("could not read file at index "+i+"!\n");
				}
			}
			catch(FileNotFoundException e)
			{
				slotText.setText("EMPTY");
			}


			slots[i].setOnMouseClicked(e -> {
				if(loading) 
				{
					loadGame(index, slots[index]);
				}
				else if(saving)
				{
					saveGame(index, slots[index]);
				}
			});
			
		}

		saveGamePane.setContent(cont);
		Scene saveGame = new Scene(saveGamePane, 300, 600);
		window4.setScene(saveGame);
		
		saveGame.addEventHandler(KeyEvent.KEY_PRESSED, key -> {
			if(key.getCode() == KeyCode.ESCAPE)
			{
				window4.close();
			}
		});
		
		window4.setOnCloseRequest(e -> {
			saving = false;
			loading = false;
		});
		
		
		/*
		 * notification Scene
		 */
		Stage window2 = new Stage();
		BorderPane nPanel = new BorderPane();	
		Label question = new Label("Save game?");
		Button affBtn = new Button("Yes");
		Button decBtn = new Button("No");
		BorderPane.setMargin(affBtn, new Insets(50));
		BorderPane.setMargin(decBtn, new Insets(50));
		BorderPane.setMargin(question, new Insets(10));
		nPanel.setLeft(affBtn);
		nPanel.setRight(decBtn);
		nPanel.setTop(question);
		BorderPane.setAlignment(question, Pos.CENTER);
		Scene notifyScreen = new Scene(nPanel,320,150);
		window2.setScene(notifyScreen);
		affBtn.setOnAction(e -> {
				saving = true;
				window4.show();
				window2.hide();
				resetWholeGame();
			}
		);
		decBtn.setOnAction(e -> {
				window2.hide();
				resetWholeGame();
			}
		);
		
		notifyScreen.addEventHandler(KeyEvent.KEY_PRESSED, key -> {
			if(key.getCode() == KeyCode.ESCAPE)
			{
				window2.close();
				resetWholeGame();
			}
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
					//System.out.println("length of added move list: "+legalMoves.length);
					
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
					//System.out.println("length of removed move list: "+n2.size());
					for (int[] is : n2) {
						int x = is[0];
	 					int y = is[1];
	 					if(squareContent(x,y) != null) {
								//((Rectangle)(sqArray[y][x].getChildren().get(0))).setFill(new Color(85.0/255,80.0/255,79.0/255,(256-87.0)/255));
						}
						else {
							System.out.println(x+" "+y+" was removed from moverange");
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
				for(int i = 0; i<11; i++) {
					for(int j = 0; j<11; j++) {
						if(board[j] == 0) {
//							System.out.println("j "+j);
//							if(((ImageView)(sqArray[j][i].getChildren().get(1))).getImage() != null) {
//								//System.out.println("setting "+i+" "+j+" to null");
//								((ImageView)(sqArray[j][i].getChildren().get(1))).setImage(null);
//							}
						}
						else {
							if(Math.pow(-1, i+j) == 1) {
								((Rectangle)(sqArray[j][i].getChildren().get(0))).setFill(gridColor2);
							}
							else {
								((Rectangle)(sqArray[j][i].getChildren().get(0))).setFill(gridColor1);
							}
							
						}
					}
				}
				
				while(arg0.next()) {
					List<int[]> n = arg0.getAddedSubList();
					
					legalRange = n.toArray();
					
					//System.out.println("length of added range list: "+legalRange.length);
					
					for (int[] is : n) {
						int x = is[0];
	 					int y = is[1];
	 					
	 					System.out.println("added "+x+", "+y+" smpm : "+spikeMarkPlacementMode+" "+ARIntersection.size());
	 					
	 					if(squareContent(x,y) == null) {
		 					if(attackMode) {
		 						((ImageView)(sqArray[y][x].getChildren().get(1))).setImage(new Image(getClass().getClassLoader().getResource("resources/att_marker.png").toExternalForm(),64,64,true,true));
		 					}
		 					else if(markingMode){
		 						((ImageView)(sqArray[y][x].getChildren().get(1))).setImage(new Image(getClass().getClassLoader().getResource("resources/mark_marker.png").toExternalForm(),64,64,true,true));
		 					}
		 					else if(queensMarkPlacementMode)
		 					{
		 						for(int i = 0; i<ARIntersection.size(); i++ )
		 						{
		 							if(ARIntersection.get(i)[0] == y && ARIntersection.get(i)[1] == x) {
		 								System.out.println("y, x = "+y+", "+x);
		 								((ImageView)(sqArray[y][x].getChildren().get(1))).setImage(new Image(getClass().getClassLoader().getResource("resources/queen_marker.png").toExternalForm(),64,64,true,true));
		 							}
		 								
		 						}
		 						
		 					}
		 					else if(board[showingPiece.get()] != 0)
		 					{
		 						if(Utils.getPieceType(board[showingPiece.get()]) == prince && spikeMarkPlacementMode)
		 						{
		 							System.out.println("spike fx at y, x = "+y+", "+x);
			 						((ImageView)(sqArray[y][x].getChildren().get(1))).setImage(new Image(getClass().getClassLoader().getResource("resources/spike_marker.png").toExternalForm(),64,64,true,true));
		 						}
		 						else if(Utils.getPieceType(board[showingPiece.get()]) == guardian && defenseMarkPlacementMode)
		 						{
		 							((ImageView)(sqArray[y][x].getChildren().get(1))).setImage(new Image(getClass().getClassLoader().getResource("resources/defense_marker.png").toExternalForm(),64,64,true,true));
		 						}
		 						
		 					}
	 					}
	 					else {
	 						if(attackMode) {
	 							((Rectangle)(sqArray[y][x].getChildren().get(0))).setFill(new Color(255.0/255,80.0/255,79.0/255,(256-87.0)/255));	 					
	 						}
	 						else if(defenseMode && Utils.isColor(board[y*11+x], turn.get()))
	 						{
	 							((Rectangle)(sqArray[y][x].getChildren().get(0))).setFill(new Color(65.0/255,235.0/255,10.0/255,(256-87.0)/255));
	 						}
	 					}
					}
					
					List<int[]> n2 = arg0.getRemoved();
					//System.out.println("length of removed range list: "+n2.size());
					for (int[] is : n2) {
						int x = is[0];
	 					int y = is[1];
	 					if(squareContent(x,y) != null) {
								((Rectangle)(sqArray[y][x].getChildren().get(0))).setFill(new Color(85.0/255,80.0/255,79.0/255,(256-87.0)/255));
						}
						else {
							System.out.println(x+" "+y+" was removed");
							((ImageView)(sqArray[y][x].getChildren().get(1))).setImage(null);
						}
					}
				}
				
			}
			
		});
			
		//listener for tracking changes for mark ranges (spike mark, defense mark)
		
		markRangeObs.addListener(new ListChangeListener<int[]>(){

					@Override
				public void onChanged(Change<? extends int[]> arg0) {
					
					while(arg0.next()) {
						List<int[]> n = (List<int[]>) arg0.getAddedSubList();
						for (int[] is : n) {
							int x = is[0];
							int y = is[1];
							
							if(defenseMarkPlacementMode)
							{
								((ImageView)(sqArray[y][x].getChildren().get(3))).setImage(new Image(getClass().getClassLoader().getResource("resources/defense_fx.png").toExternalForm(),64,64,true,true));
							}
							else if(spikeMarkPlacementMode)
							{
								((ImageView)(sqArray[y][x].getChildren().get(3))).setImage(new Image(getClass().getClassLoader().getResource("resources/spike_attack_fx.png").toExternalForm(),64,64,true,true));
							}
							
						}
						
						List<int[]> n2 = (List<int[]>) arg0.getRemoved();
						//System.out.println("length of removed range list: "+n2.size());
						for (int[] is : n2) {
							int x = is[0];
		 					int y = is[1];
		 					
		 					((ImageView)(sqArray[y][x].getChildren().get(3))).setImage(null);
		 					
						}
					}
				}
				
			});
		
		//TODO: update stats of captured pieces while pieces are captured
		capturedPieces.addListener(new ListChangeListener<int[]>(){

			@Override
			public void onChanged(Change<? extends int[]> arg0) {
				
				while(arg0.next()) {
					List<int[]> n = (List<int[]>) arg0.getAddedSubList();
					for (int[] is : n) {
						
					}
				}
			}
			
		});
			
	
		scene.addEventHandler(KeyEvent.KEY_PRESSED, key -> {
			if(key.getCode() == KeyCode.SHIFT) {
				defenseMarkPlacementMode = false;
				queensMarkPlacementMode = false;
				spikeMarkPlacementMode = false;
				attackMode = true;
				attackSource = showingPiece.get();
				legalMovesObs.clear();
				getRange();
			}
			else if(key.getCode() == KeyCode.ALT) {
				defenseMarkPlacementMode = false;
				queensMarkPlacementMode = false;
				spikeMarkPlacementMode = false;
				markingMode = true;
				markSource = showingPiece.get();
				legalMovesObs.clear();
				getRange();
			}
			else if(key.getCode() == KeyCode.CONTROL) {
				defenseMarkPlacementMode = false;
				queensMarkPlacementMode = false;
				spikeMarkPlacementMode = false;
				defenseMode = true;
				defenderSource = showingPiece.get();
				legalMovesObs.clear();
				getRange();
			}

		});
		
		scene.addEventHandler(KeyEvent.KEY_RELEASED, key -> {
			if(key.getCode() == KeyCode.SHIFT) {
				if(!hasAttacked)
				{
					attackSource = -1;
				}
				attackMode = false;
				legalRangeObs.clear();
				
				getRange();
				getLegalSquares();
			}
			else if(key.getCode() == KeyCode.ALT) {
				if(!markPlaced)
				{
					markSource = -1;
				}
				markingMode = false;
				legalRangeObs.clear();

				getRange();
				getLegalSquares();
			}
			else if(key.getCode() == KeyCode.CONTROL) {
				defenseMode = false;
				legalRangeObs.clear();

				getRange();
				getLegalSquares();
			}

		});
		
		scene.addEventHandler(KeyEvent.KEY_PRESSED, key -> {
			if(key.getCode() == KeyCode.LEFT)
			{
				plies--;
				//board = boardList.get((plies-2) < 0 ? 0 : plies-2);
				//turn.set((turn.get() - 1) % 2 );
				redraw(grid);
			}
			else if(key.getCode() == KeyCode.RIGHT)
			{
				plies++;
				//board = boardList.get((plies-2) > boardList.size() - 1 ? boardList.size() -1 : plies-2);
				//turn.set((turn.get() - 1) % 2 );
				redraw(grid);
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
		
		turn.addListener((observableValue, oldValue, newValue) -> {
			
			if(newValue.intValue() == 1)
			{
				turnInfoLabel.setText("Black");
			}
			else if(newValue.intValue() == 2)
			{
				turnInfoLabel.setText("Red");
			}
			
			if(specialPos[0] != -1) {
				
				if(Utils.getMarkInitPlies(board[specialPos[0]]) <= plies - queenMarkTimer)
				{
					board[specialPos[0]] = instantiatePiece(queen, turn.get(), specialPos[0], 0, PieceData.pieceHP(queen));
					pieceIdCap++;
					specialPos[0] = -1;
					redraw(grid);
				}

			}
			else if(specialPos[1] != -1)
			{

				System.out.println(Utils.getMarkInitPlies(board[specialPos[1]]) - (plies - spikeMarkTimer)+" plies until activation!");
				if(Utils.getMarkInitPlies(board[specialPos[1]]) <= plies - spikeMarkTimer)
				{
					activateSpikeMark(specialPos[1]);
					specialPos[1] = -1;
				}
			}
			else if(specialPos[2] != -1)
			{
				if(Utils.getMarkInitPlies(board[specialPos[2]]) + 1 < plies)
				{
					if(plies <= Utils.getMarkInitPlies(board[specialPos[2]]) + 3)
					{
						activateDefenseMark(specialPos[2]);
					}
					else
					{
						//finally, destroy the mark itself after 3 plies
						board[specialPos[2]] = 0;
						specialPos[2] = -1;
						redraw(grid);
						markRangeObs.clear();
					}
				}
				
					
			}
			else if(specialPos[3] != -1)
			{
				if(Utils.getMarkInitPlies(board[specialPos[3]]) <= plies - scarletMarkTimer)
				{
					rangedAction(RangedType.ScarletChannel, specialPos[3], 0);
					specialPos[3] = -1;
				}
			}
			else if(specialPos[4] != -1)
			{
				//fractal mark action, black hole effects
				//placeMark(specialPos)
			}
			//specialpos[5] is set after teleport mark has something inside it
			else if(specialPos[5] != -1)
			{
				if(Utils.getMarkInitPlies(board[specialPos[5]]) <= plies - 1)
				{
					teleport(specialPos[5]);
					specialPos[5] = -1;
				}
			}		
		});
		
		
		
		start.setOnAction(e -> {
			mesh = new Mesh(2000,this);
			statusConsole.appendText("server started\n");
			mesh.start();
			chooseColor1.setVisible(true);
			chooseColor2.setVisible(true);
			randomColor.setVisible(true);
			freePlay = false;
		});
		
		connect.setOnAction(e -> {
			mesh = new Mesh(2001,this);
			statusConsole.appendText("client started\n");
			mesh.start();			
			window3.show();	
			chooseColor1.setVisible(true);
			chooseColor2.setVisible(true);
			randomColor.setVisible(true);
			freePlay = false;
		});
		
		chooseColor1.setOnAction(e -> {
			playerColor = 1;
			isDefaultBoardRotation = true;
			redraw(grid);
			statusConsole.appendText("Black selected\n");
			startGame.setVisible(true);
		});
		
		chooseColor2.setOnAction(e -> {
			playerColor = 2;
			isDefaultBoardRotation = false;
			redraw(grid);
			statusConsole.appendText("Red selected\n");
			startGame.setVisible(true);
		});
		
		randomColor.setOnAction(e -> {
			playerColor = rng.nextInt(2)+1;
			Message msgObject;
			if(playerColor == 1) {
				otherPlayerColor = 2;
				msgObject = new Message(2, Message.Tyyppi.RNG);
			}
			else {
				otherPlayerColor = 1;
				msgObject = new Message(1, Message.Tyyppi.RNG);
			}
			msgObject.setSender(this.toString());
			mesh.broadcast(msgObject);
			statusConsole.appendText(playerColor == 1 ? "Black" : "Red" + "rolled\n");
		});
		
		startGame.setOnAction(e -> {
			
			System.out.println(playerColor+" "+otherPlayerColor);
			
			if(playerColor < 0 || otherPlayerColor < 0) {
				System.out.println(playerColor+" "+otherPlayerColor);
				statusConsole.appendText("waiting for other player\n");
			}
			
	        if(otherPlayerReady) {
	        	if(playerColor == otherPlayerColor) {
	        		statusConsole.appendText("opponent chose the same color!\n");
	        	}
	        	else {
	        		Message msgObject = new Message(true, Message.Tyyppi.READY);
	        		msgObject.setSender(this.toString());
	        		mesh.broadcast(msgObject);
		        	statusConsole.appendText("game started!\n");
					chooseColor1.setVisible(false);
					chooseColor2.setVisible(false);
					randomColor.setVisible(false);
					
					
	        	}
	        }
	        else {
	        	Message msgObject = new Message(playerColor, Message.Tyyppi.MSG);
		        msgObject.setSender(this.toString());
		        System.out.println("sending ready confirmation for "+playerColor);
		        statusConsole.appendText("you're ready!\n");
		        mesh.broadcast(msgObject);
		        chooseColor1.setVisible(false);
				chooseColor2.setVisible(false);
				randomColor.setVisible(false);
	        }
			
		});
		
		
		resignPiece.setOnAction(e -> {
			
			int s = showingPiece.get();
			resignPiece(s);
			
		});
		
		rotateCounterClockwise.setOnAction(e -> {
			
			int s = showingPiece.get();
			
			//some piece has been rotated, but piece that is currently selected and to be rotated is not the piece previously rotated?
			if(rotatedPieceLoc != -1 && board[s] != board[rotatedPieceLoc])
			{
				statusConsole.appendText("only one piece can be rotated!\n");
				return;
			}
			
			System.out.println("dir "+Utils.getPieceRotation(board[s])+" h: "+h);
			
			h = Utils.getPieceRotation(board[s]);


			double x = Math.cos((h*Math.PI*2)/360.0);
			double y = Math.sin((h*Math.PI*2)/360.0);
			x = Math.round(x);
			y = Math.round(y);

			int dir = 0;
			
			if(x == 1 && y == 0)
			{
				dir = 270;
			}
			else if(x == 0 && y == 1)
			{
				dir = 0;
			}
			else if(x == -1 && y == 0)
			{
				dir = 90;
			}
			else if(x == 0 && y == -1)
			{
				dir = 180; 
			}
			
			if(rotatedPieceLoc == -1)
			{
				previousRotation = Utils.getPieceRotation(board[s]);
			}
			
			Utils.doPieceRotation(board[s],dir);
			rotatedPieceLoc = s;
			legalMovesObs.clear();
			Utils.iterateBoard(s,board);
			getLegalSquares();
			
			System.out.println("dir "+Utils.getPieceRotation(board[s])+" h: "+h);
		});
		
		rotateClockwise.setOnAction(e -> {
			int s = showingPiece.get();
			
			//some piece has been rotated, but piece that is currently selected and to be rotated is not the piece previously rotated!
			if(rotatedPieceLoc != -1 && board[s] != board[rotatedPieceLoc])
			{
				statusConsole.appendText("only one piece can be rotated!\n");
				return;
			}
			
			System.out.println("dir "+Utils.getPieceRotation(board[s])+" h: "+h);
			
			h = Utils.getPieceRotation(board[s])+180;

			double x = Math.cos((h*Math.PI*2)/360.0);
			double y = Math.sin((h*Math.PI*2)/360.0);
			x = Math.round(x);
			y = Math.round(y);
			int dir = 0;
			
			if(x == 1 && y == 0)
			{
				dir = 270;
			}
			else if(x == 0 && y == 1)
			{
				dir = 0;
			}
			else if(x == -1 && y == 0)
			{
				dir = 90;
			}
			else if(x == 0 && y == -1)
			{
				dir = 180; 
			}
			
			
			if(rotatedPieceLoc == -1)
			{
				previousRotation = Utils.getPieceRotation(board[s]);
			}
			
	
			Utils.doPieceRotation(board[s],dir);
			rotatedPieceLoc = s;
			legalMovesObs.clear();
			Utils.iterateBoard(s,board);
			getLegalSquares();
		});
		
				
		resetGame.setOnAction(e -> {
			chooseColor1.setVisible(true);
			chooseColor2.setVisible(true);
			randomColor.setVisible(true);
			
			window2.show();
			
		});
		
		reset.setOnAction(e -> {

			if(legalMovesObs.size() > 0) {
				legalMovesObs.clear();
			}
			p1counters.set(tempCounters[0]);
			p1countersPerTurn.set(tempCounters[1]);
			p2counters.set(tempCounters[2]);
			p2countersPerTurn.set(tempCounters[3]);
			move.clear();
			resetGameData();
			arrayCopy(tempBoard,board);
			redraw(grid);
		});
		
		//when 'end turn' is pressed, add any changes to rotation to move vector, send move vector, advance turn and print moves to console
		endTurn.setOnAction(e -> {
			if(gameRunning && (freePlay || (playerColor == turn.get()))) {
				if(victoryAchieved)
				{
					gameRunning = false;
				}
				if(rotatedPieceLoc != -1)
				{
					int rotationChange = previousRotation - Utils.getPieceRotation(board[rotatedPieceLoc]);
					
					if(rotationChange == 270)
					{
						rotationChange = -90;
					}
					else if(rotationChange == -270) 
					{
						rotationChange = 90;
					}

					move.add(new int[] {0,4,rotatedPieceLoc,rotationChange});
				}
				
				setTurn(move);
				sendMove(move);
				move.clear();
				
				
			}
		});
		
		resign.setOnAction(e -> {
			if(mesh != null)
			{
				Message msgObject = new Message(turn.get(), Message.Tyyppi.BANAANI);
		        msgObject.setSender(this.toString());
		        System.out.println("sending resignation announcement to other player!");
		        mesh.broadcast(msgObject);
			}
			
			resign(turn.intValue());
		});
		
		save.setOnAction(e -> {
			window4.show();
			saving = true;
		});

		load.setOnAction(e -> {
			window4.show();
			loading = true;
		});
		
		exit.setOnAction(e -> {
			if(mesh != null) {
				mesh.close();
			}
			Platform.exit();
		});
		
		
		
		showingPiece.addListener((observableValue,oldValue,newValue) -> {
			
			pieceMenu.getChildren().clear();
	
			int h = newValue.intValue();
			
			System.out.println("selected: "+newValue);
			
			int piece = board[h];
			int type = Utils.getPieceType(board[h]);
			
			String s = "";
			
			s = Utils.pieceName(type)+" ("+PieceData.pieceChar(type)+") \nAttack Cost: "+PieceData.pieceAC(type)+"\nDefense Cost: "+PieceData.pieceDC(type)+"\nMove cost: "+PieceData.moveCost(type,false)+"\nValue: "+PieceData.pieceValue(type);
			
			switch(type) {
			case 1:
				pieceMenu.getChildren().addAll(resignPiece,promoteSoldier);
				break;
			case 2:
				pieceMenu.getChildren().addAll(resignPiece,rotateClockwise,rotateCounterClockwise);
				break;
			case 3:
				pieceMenu.getChildren().addAll(resignPiece,placeTeleportMark,rotateClockwise,rotateCounterClockwise);
				break;
			case 4:
				pieceMenu.getChildren().addAll(resignPiece,rotateClockwise,rotateCounterClockwise);
				break;
			case 5:
				pieceMenu.getChildren().addAll(resignPiece,placeSpikeMark);
				break;
			case 6:
				pieceMenu.getChildren().addAll(resignPiece,placeFortressMark,rotateClockwise,rotateCounterClockwise);
				break;
			case 7:
				pieceMenu.getChildren().addAll(resignPiece,rotateClockwise,rotateCounterClockwise);
				break;
			case 8:
				pieceMenu.getChildren().addAll(resignPiece);
				break;
			case 9:
				pieceMenu.getChildren().addAll(resignPiece,placeScarletMark,rotateClockwise,rotateCounterClockwise);
			}
			
			pieceInfo.setText(s);
			
			for (int i : queensMarkPieceIDList) {
				if(piece == i)
				{
					pieceMenu.getChildren().addAll(placeQueensMark);
				}
			}

		});
		
		placeQueensMark.setOnAction(e -> {
			//Utils.iterateBoard(s,board);
			
			attackMode = false;
			markingMode = false;
			queensMarkPlacementMode = true;
			//legalRangeObs.clear();
			getRange();
			for(int i = 0; i<legalRangeObs.size(); i++)
			{
				System.out.println(Arrays.toString(legalRangeObs.get(i))+" element");
			}
			
			//legalMovesObs.clear();
			
			
		});
		
		placeSpikeMark.setOnAction(e -> {
			attackMode = false;
			markingMode = false;
			spikeMarkPlacementMode = true;
			getRange();

			
		});
		
		placeFortressMark.setOnAction(e -> {
			attackMode = false;
			markingMode = false;
			defenseMarkPlacementMode = true;
			getRange();
		});
		
		placeScarletMark.setOnAction(e -> {
			
		});
		
		placeTeleportMark.setOnAction(e -> {
			
		});
		

		promoteSoldier.setOnAction(e -> {

			for(int i = 0; i<121; i++)
			{
				//check if general still on the board
				if(board[i] != 0 && Utils.getPieceType(board[i]) == general && Utils.isColor(board[i], turn.get()))
				{
					statusConsole.appendText("General still exists on the board!\n");
					return;
				}

			}
			
			int s = showingPiece.get();
			
			modifyAction(ModifyType.Promote, s, s);
			move.add(new int[] {0,5,s,s});
			redraw(grid);

		});

		
		
		console.textProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue,
					Object newValue) {
				console.setScrollTop(Double.MAX_VALUE);
			}
		});
	}
	
	
	private void saveGameState() {
		turn.addListener((observableValue,oldValue,newValue)-> {
					
			//System.out.println((newValue.intValue() == 1 ? "Black" : "Red")+ " turn started");
			
			if(newValue.intValue() == 1) {
				p1counters.set(p1counters.get() + p1countersPerTurn.get());
			}
			else {
				p2counters.set(p2counters.get() + p2countersPerTurn.get());
			}
			
			savePos(board);
			
			});
	
	}
	
	public void confirm(int mode, int data) {
		if(mode == 1) {
			statusConsole.appendText("other player is ready...\n");
			otherPlayerColor = data;
			otherPlayerReady = true;
		}
		else if(mode == 2 && data == 1) {
			statusConsole.appendText("game started!\n");
			console.appendText("\nTurn 1.\nBlack");
		}
	}
	
	
	public void setColor(int color) {
		statusConsole.appendText(color == 1 ? "black" : "red" + "rolled!\n");
		playerColor = color;
		otherPlayerColor = color == 1 ? 2 : 1;
	}
	
	/*
	 * save current position for the reset() method
	 */
	
	private void savePos(int[] b) {
		tempCounters[0] = p1counters.get();
		tempCounters[1] = p1countersPerTurn.get();
		tempCounters[2] = p2counters.get();
		tempCounters[3] = p2countersPerTurn.get();
		arrayCopy(b,tempBoard);
		
		System.out.println("saving state!");
		
	}
	
	/*
	 * reset all game data for the reset()
	 */
	
	private void resetGameData() {
		
		moveSource = -1;
		moveTarget = -1;
		markSource = -1;
		attackSource = -1;
		defenderSource = -1;
		rotatedPieceLoc = -1;
		previousRotation = 0;
		moved = false;
		markPlaced = false;
		hasQueensMarkOrQueen = false;
		victoryAchieved = false;
		hasAttacked = false;
		hasDefended = false;
		hasResignedPiece = false;
		resignedPieceType = 0;
		queensMarkPieceIDList.clear();
		ARIntersection.clear();
		
		for(int x = 0; x<5; x++)
		{
			//special position set on the same turn, revert it back to -1
			if(specialPos[x] != -1 && Utils.getMarkInitPlies(board[specialPos[x]]) == plies)
			{
				specialPos[x] = -1;
			}
		}
	}
	
	/*
		Reset board and stats to initial positions
	 */
	
	private void resetWholeGame() {
		specialPos = new int[] {-1,-1,-1,-1,-1,-1};
		resetGameData();
		initBoard(grid);
		p1counters.set(startCounters);
		p1countersPerTurn.set(startIncome);
		p2counters.set(startCounters);
		p2countersPerTurn.set(startIncome);
		turn.set(1);
		sb.delete(0, sb.length());
		plies = 2;
		otherPlayerReady = false;
		console.clear();
		sb.append("-----------------\nTurn 1:\n");
		playerColor = -1;
		otherPlayerColor = -2;
		gameRunning = true;
	}
	
	
	/*
	 * resign game
	 */
	
	public void resign(int turn) {
		gameRunning = false;
		
		String s = "\n";
		s += turn == 1 ? "Red " : "Black ";
		s += "won by resignation";
		sb.append(s);
		console.setText(sb.toString());
	
	}
	
	
	/*
	 * Load game
	 */
	
	private void loadGame(int slotNo, Pane p) {
		
		clearBoard();
		
		FileReader freader = null;
		try 
		{
			freader = new FileReader("savegame-"+ slotNo +".txt");
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(freader);
		String s;
		int lineCounter = 0;
		
		specialPos[0] = -1;
		specialPos[1] = -1;
		specialPos[2] = -1;
		specialPos[3] = -1;
		specialPos[4] = -1;
		
		try 
		{
			while((s = br.readLine()) != null)
			{
				
				String[] line = s.split(" ");
				
				if(lineCounter == 1)
				{
					plies = Integer.parseInt(line[0]);
					turn.set(Integer.parseInt(line[1]));
				}
				else if(lineCounter == 2)
				{
					p1counters.set(Integer.parseInt(line[0]));
					p1countersPerTurn.set(Integer.parseInt(line[1]));
					p2counters.set(Integer.parseInt(line[2]));
					p2countersPerTurn.set(Integer.parseInt(line[3]));
				}
				else if(lineCounter != 0)
				{
					int t;
					int pos;

					t = Integer.parseInt(s);
					pos = Utils.getPos(t);

//					if(t[0] < 0 && t[1] > 1)
//					{
//						specialPos[t[1]-2] = t[3];
//					}
					
					//assign vector into board at t[3], t[4]
					board[pos]= t;
				}
				System.out.println(Arrays.toString(line));
				
				lineCounter++;
			}
			//game data should be at the initial values
			resetGameData();
			//triggering the save of board into tempBoard now because tempBoard might be at default value(starting position)
			savePos(board);
			//checking for queens mark placement condition
			testQueensMarkCondition(turn.get());
			
			sb.delete(0, sb.length());
			sb.append("Turn "+(int)(plies/2.0)+":\n");
			console.setText(sb.toString());
			redraw(grid);
			statusConsole.appendText("successfully loaded game "+((Label)p.getChildren().get(1)).getText()+"\n");
			freader.close();
			br.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	
	/*
	 * Save game
	 */
	
	private void saveGame(int slotNo, Pane p) {
		
		System.out.println(LocalTime.now());
		String s = "";
		String header = LocalDate.now().toString()+" "+LocalTime.now().getHour()+":"+LocalTime.now().getMinute()+":"+LocalTime.now().getSecond()+" Turn: "+(int)(plies/2.0)+" "+(turn.get() == 1 ? "Black" : "Red");

		s += header + "\n";
		s += plies+" "+turn.get()+"\n"+tempCounters[0]+" "+tempCounters[1]+" "+tempCounters[2]+" "+tempCounters[3]+"\n";
		System.out.println(tempCounters[0]+" "+tempCounters[1]+" "+tempCounters[2]+" "+tempCounters[3]);
		
		//revert back to state at the start of the turn
		arrayCopy(tempBoard,board);

		
		
		try(FileWriter outFile = new FileWriter("savegame-"+ slotNo +".txt",false);
				BufferedWriter bWriter = new BufferedWriter(outFile))
		{
			
			for(int i = 0; i<121; i++)
			{
				if(board[i] != 0) {
					s += String.valueOf(board[i]);
					s += "\n";
				}
			}

			bWriter.write(s);	
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		((Label)p.getChildren().get(1)).setText(header);
		statusConsole.appendText("saved game in slot "+(slotNo+1)+"!\n");
	}
	
	
	public void handleMessage(Message msg) {
		ArrayList<int[]> receivedMove = (ArrayList<int[]>)msg.getPayload();
		System.out.println("received move at board, contains "+receivedMove.size()+" elements!");

		
		int[] m = null;
		
		for(int i = 0; i<receivedMove.size(); i++)
		{
			m = receivedMove.get(i);
			
			System.out.println("submove = "+Arrays.toString(m));
			
			switch(m[0]) {
			case 0:
				modifyAction(ModifyType.values()[m[1]], m[2], m[3]);
				break;
			case 1:
				if(board[m[3]] != 0)
				{
					upgradeMark(m[3]);
				}
				else
				{
					placeMark(m[2], MarkType.values()[m[1]]);
				}
				
				break;
			case 2:
				rangedAction(RangedType.values()[m[1]], m[2], m[3]);
				break;
			}
		}

		move.clear();

		
		if(RuleSet.checkAreas(turn.get(), board))
		{
			gameRunning = false;
		}
		
		
		//advance turn after opponents move was made
		setTurn(receivedMove);

		
		redraw(grid);
		
		
	}

	
	/*
	 * Called when ending turn to send the move to other player by socket connection
	 */
	
	public void sendMove(ArrayList<int[]> move)
	{
		
		if(mesh == null) 
		{
			return;
		}

		ArrayList<int[]> m = new ArrayList<int[]>();
		
		for(int i = 0; i<move.size(); i++)
		{
			m.add(move.get(i));
		}
		
        Message msgObject = new Message(m, Message.Tyyppi.MOVE);
        msgObject.setSender(this.toString());
        System.out.println("sending move(App2): " +Arrays.toString(move.get(0))+" length "+move.size());
        mesh.broadcast(msgObject);
		
	}
	
	
	private GridPane redraw(GridPane grid) 
	{
		int i;
		int j;
		int i2 = 0;
		int j2 = 0;

		grid.getChildren().clear();

		for(i = isDefaultBoardRotation ? 0 : 11; i<12 && i > -1; i = isDefaultBoardRotation ? i + 1 : i - 1, i2++) {
			for(j = isDefaultBoardRotation ? 0 : 11; j<12 && j > -1; j = isDefaultBoardRotation ? j + 1 : j - 1, j2 = (j2+1)%12) {
				ImageView piece = null;
				Label hpCounter = null;
				
				if(i == 11) 
				{
					Label l = new Label(""+(j+1));
					if(j+1 == 12)
					{
						l.setText("");
					}
					
					//TODO multiple labels added over time
					sqArray[i][j].getChildren().add(l);

				}
				else if(j == 11) 
				{
					Label l = new Label(""+Utils.charConv2(i));
					sqArray[i][j].getChildren().add(l);

				}
				else 
				{
					piece = (ImageView)sqArray[i][j].getChildren().get(1);
					hpCounter = (Label)sqArray[i][j].getChildren().get(2);
					piece.setImage(null);
				}
				

				int boardPos = j * 11 + i;
				int temp = j;
				int temp2 = i;


				if(squareContent(temp,temp2) != null)
				{
					
					//piece.setImage(new Image(getClass().getClassLoader().getResource("resources/"+squareContent(temp,temp2)+".png").toExternalForm(),64,64,true,true));
					
					if(squareContent(temp,temp2).equals("mark_a_b")) 
					{
						piece.setImage(new Image(getClass().getClassLoader().getResource("resources/mark_a_b.png").toExternalForm(),64,64,true,true));
						if(Utils.getMarkLvl(board[boardPos]) > 0)
						{
							hpCounter.setText(String.valueOf(Utils.getMarkLvl(board[boardPos])));
						}
						else
						{
							hpCounter.setText("");
						}
					}
					else if(squareContent(temp,temp2).equals("mark_a_r")) 
					{
						piece.setImage(new Image(getClass().getClassLoader().getResource("resources/mark_a_r.png").toExternalForm(),64,64,true,true));
						if(Utils.getMarkLvl(board[boardPos]) > 0)
						{
							hpCounter.setText(String.valueOf(Utils.getMarkLvl(board[boardPos])));
						}
						else
						{
							hpCounter.setText("");
						}
					}
					else {
						piece.setImage(new Image(getClass().getClassLoader().getResource("resources/"+squareContent(temp,temp2)+".png").toExternalForm(),64,64,true,true));
						if(board[boardPos] < 0)
						{
							if(Utils.getMarkLvl(board[boardPos]) > 0)
							{
								hpCounter.setText(String.valueOf(Utils.getMarkLvl(board[boardPos])));
							}
							else
							{
								hpCounter.setText("");
							}
						}
						else if(board[boardPos] > 0) 
						{
							if(Utils.getPieceHP(board[boardPos]) > 0)
							{
								hpCounter.setText(String.valueOf(Utils.getPieceHP(board[boardPos])));
							}
							else
							{
								hpCounter.setText("");
							}
						}
						
					}
					

				}
				else if(i != 11 && j != 11)
				{
					hpCounter.setText("");
				}

	
				GridPane.setConstraints(sqArray[i][j],i2,j2);
				grid.getChildren().add(sqArray[i][j]);
			
			}
		}
		console.setText(sb.toString());
		console.appendText("");
		
		if(move.size() > 0)
			System.out.println(Arrays.toString(move.get(0)));

		return grid;
	}
	


	/*
	 * is (paneX,paneY) contained in legal squares?
	 */
	
	private boolean isLegal(int paneX, int paneY, Object[] list) {
		
		int x,y;

		if(list == null) {
			return false;
		}
	
		for(int i = 0; i<list.length; i++) {
			x = ((int[])list[i])[0];
			y = ((int[])list[i])[1];
			System.out.println(x+" "+y+" "+paneX+" "+paneY);
			if(x == paneX && y == paneY) {
				return true;
			}
		}
		return false;
	}
	


	/*
	 * Place mark at (x,y) and update board with redraw()
	 * 
	 * 	
	 * 				area mark
	 * 				defense mark
	 * 				spike mark
	 * 				black hole mark
	 * 				teleport mark
	 * 				queens mark
	 * 				scarlet mark
	 */
	
	private void placeMark(int pos, MarkType type) {
		
		//id, type, color, position, lvl, whenPlaced
		board[pos] = instantiateMark(type.ordinal(),turn.get(),pos,0);
		redraw(grid);
		markPlaced = true;
		
		if(type == MarkType.Area)
		{
			Utils.modifyCounters(5, turn.get(), 1, false);
			testQueensMarkCondition(pos,turn.get());
		}
		else if(type == MarkType.Queen)
		{
			Utils.modifyCounters(5, turn.get(), 2, false);
			board[pos] = addData(board[pos], 0, plies);
			specialPos[0] = pos;
			hasQueensMarkOrQueen = true;
		}
		else if(type == MarkType.Spike)
		{
			specialPos[1] = pos;
			board[pos] = addData(board[pos], 0, plies);
			//get mark neighborhood using iterateBoard and fill markRange observableList 
			Utils.iterateBoard(pos, board);
		}
		else if(type == MarkType.Defense)
		{
			specialPos[2] = pos;
			board[pos] = addData(board[pos], 0, plies);
			Utils.iterateBoard(pos, board);
		}
		else if(type == MarkType.Scarlet)
		{
			specialPos[3] = pos;
			board[pos] = addData(board[pos], 0, plies);
		}
		else if(type == MarkType.Fractal)
		{
			specialPos[4] = pos;
			board[pos] = addData(board[pos], 0, plies);
		}
		
		move.add(new int[] {1,type.ordinal(),pos,pos});
		
		//check for winning condition
		if(RuleSet.checkAreas(turn.get(), board)) {
			victoryAchieved = true;
		}
	}
	
	/*
	 * Add additional data to piece 
	 * 
	 * piece = piece * p1 + data * p2, where p1, p2 prime
	 */
	
	private int addData(int piece, int dataType, int data) {
		
		int[] keyPrimesPlies = new int[] {127,131,137};
		int[] keyPrimesPiece = new int[] {139,149,151};
		
		if(piece % 229 != 0)
			piece *= 229;
		
		for(int i = 0; i<3; i++) {
			if(dataType == 0)
			{
				if(piece % keyPrimesPlies[i] != 0) 
				{
					piece += keyPrimesPlies[i]*data;
					break;
				}
			}
			else if(dataType == 1)
			{
				if(piece % keyPrimesPiece[i] != 0) 
				{
					piece += keyPrimesPiece[i]*data;
					break;
				}
			}
		}


		return piece;
	}


	/*
	 * Upgrade mark at (x,y)
	 * 
	 */
	
	private void upgradeMark(int pos)
	{
		Utils.modifyCounters(6, turn.get(), pos, false);
		Utils.modifyMarkHP(board[pos], 1);
		move.add(new int[] {1,7,pos,pos});
		redraw(grid);
	}
	
	
	
	private void testQueensMarkCondition(int color)
	{
		for(int i = 0; i<121; i++)
		{
			//test queens mark condition for all own marks
			if(board[i] != 0 && board[i] < 0 && Utils.isColor(board[i], color))
			{
				testQueensMarkCondition(i, color);
			}
		}
	}
	
	/*
	 * Test if there exists an area(a rectangle formed by 4 area marks) for which player whose turn it is can reach with at least one piece
	 * eligible pieces id's are added to queensMarkPieceIDList
	 * areaInterior will get all squares in the area (but not the border)
	 * queenAreaLvl will denote the smallest level of the 4 marks forming the area
	 */
	
	private void testQueensMarkCondition(int pos, int color) {
		
		ARIntersection.clear();
		
		int x = (int)(pos/11.0);
		int y = pos % 11;
		
		int[] area = RuleSet.getArea(x, y, board, color, x);
			
		if(area != null)
		{
			int corner1 = area[0] * 11 +area[2];
			int corner2 = area[1] * 11 +area[2];
			int corner3 = area[0] * 11 +area[3];
			int corner4 = area[1] * 11 +area[3];
		
			queenMarkCost = Math.min(Math.min(Utils.getMarkLvl(board[corner1]), Utils.getMarkLvl(board[corner2])), 
					Math.min(Utils.getMarkLvl(board[corner3]), Utils.getMarkLvl(board[corner4])));

			for(int i = 0; i<121; i++)
			{
				//own piece found?
				if(board[i] != 0 && Utils.isColor(board[i], color) && board[i] > 0)
				{
					//get movement range of found piece
					Utils.iterateBoard(i,board);
					
					//test whether pieces movement range overlaps with area
					for (int j2 = 0; j2 < placementRange.size(); j2++) 
					{
						System.out.println("area: "+area[0]+" "+area[1]+" "+area[2]+" "+area[3]);
						System.out.println("coord of a piece "+placementRange.get(j2)[0]+", "+placementRange.get(j2)[1]);
						if(placementRange.get(j2)[0] > area[0] && placementRange.get(j2)[0] < area[1] && placementRange.get(j2)[1] > area[2] && placementRange.get(j2)[1] < area[3])
						{
							//add overlapping squares to area-range intersection list
							if(!ARIntersection.contains(placementRange.get(j2))) 
							{
								ARIntersection.add(placementRange.get(j2));
							}
							
							//add the piece that can place a mark in the intersection of the 'area' and range, to list
							if(!queensMarkPieceIDList.contains(board[i]))
							{
								queensMarkPieceIDList.add(board[i]);
								System.out.println("adding piece "+board[i]);
							}								
						}
					}						
				}
			}			
		}
	}

	
	
	private void resignPiece(int pos)
	{
		resignedPieceType = Utils.getPieceType(board[pos]);
//		
//		if(pos == rotatedPieceLoc)
//		{
//			rotatedPieceLoc = 0;
//		}

		if(moveTarget == pos)
		{
			statusConsole.appendText("cannot resign a moved piece!\n");
			return;
		}
		
		if(hasResignedPiece)
		{
			statusConsole.appendText("already resigned a piece during this turn!\n");
			return;
		}
		if(Utils.getPieceType(board[pos]) == queen)
		{
			statusConsole.appendText("cannot resign a Queen!\n");
			return;
		}
		if(Utils.getPieceType(board[pos]) == general)
		{
			statusConsole.appendText("cannot resign your General!\n");
			return;
		}
		
		hasResignedPiece = true;
		Utils.modifyCounters(3, turn.get(), 0, false);
		board[pos] = 0;
		
		move.add(new int[] {0,6,pos,pos,resignedPieceType});

		redraw(grid);
	}
	

	private void activateSpikeMark(int startValue) {
		Object[] H = markRangeObs.toArray();
		
		
		for (int i = 0; i<H.length; i++) {
			int[] t = (int[]) H[i];
			int x = t[0];
			int y = t[1];
			int pos = x*11 + y;
			if(board[pos] != 0)
			{
				//attack the square 'pos'!
				rangedAction(RangedType.Attack, startValue, pos);
				
			}

		}
		
		//finally, destroy the mark itself
		board[startValue] = 0;
		redraw(grid);
		markRangeObs.clear();
		move.add(new int[] {2,2,startValue,startValue});
		
	}
	
	
	private void activateDefenseMark(int startValue) {
		Object[] H = markRangeObs.toArray();
		
		
		for (int i = 0; i<H.length; i++) {
			int[] t = (int[]) H[i];
			int x = t[0];
			int y = t[1];
			int pos = x*11 + y;
			if(board[pos] > 0 && Utils.getPieceHP(board[pos]) == 0)
			{
				//defend the square 'pos'!
				rangedAction(RangedType.Defend, startValue, pos);
				
			}

		}
		
		ArrayList<Integer> b = new ArrayList<Integer>();

		move.add(new int[] {2,3,startValue,startValue});
		
	}

	private void teleport(int location1)
	{
		int piece = Utils.pullData(board[location1],"piece");
		for(int i = 0; i<121; i++) {
			if(board[i] < 0 && Utils.getMarkType(board[i]) == MarkType.Teleport)
			{
				Utils.iterateBoard(i, board);
				Object[] H = markRangeObs.toArray();
				
				for(int j = 0; j<H.length; j++)
				{
					if(board[j] == 0)
					{
						board[j] = piece;
						move.add(new int[] {});
						redraw(grid);
						return;
					}
				}
				
				
			}
		}
		
	}

	/*
	 * 
	 * 		attack / spike mark attack / scarlet channel attack
	 * 		defend / defense mark defense
	 * 
	 *		
	 *		black hole mark ranged move action
	 */
	

	public void rangedAction(RangedType type, int startValue, int endValue)
	{
		int piece1 = board[startValue];
		int piece2 = board[endValue];
		

		switch(type.ordinal())
		{
		case 0:
			
			hasAttacked = true;
			if(piece1 < 0)
			{
				Utils.modifyCounters(1, Utils.getColor(piece1), 0, false);
			}
			else if(piece1 > 0)
			{
				Utils.modifyCounters(1, Utils.getColor(piece1), Utils.getPieceType(piece1), false);
			}
			

			//if target is captured, increment opposing players Counters by captured piece's value
			
			//target is a piece:
			if(piece2 > 0)
			{
				System.out.println("hp "+Utils.getPieceHP(piece2));
				if(Utils.getPieceHP(piece2) == 0)
				{
					Utils.modifyCounters(3,Utils.getColor(piece1),Utils.getPieceType(piece2),false);
					if(endValue == moveTarget || endValue == markSource || endValue == defenderSource)
					{
						
					}
					board[endValue] = 0;
				}
				else
				{
					Utils.modifyPieceHP(piece2, -1);
					
				}
			}
			//target is a mark:
			else if(piece2 < 0){
				if(Utils.getMarkLvl(piece2) == 0)
				{
					Utils.modifyCounters(4, Utils.getColor(piece2), 0, false);
					
					board[endValue] = 0;
				}
				else
				{
					Utils.modifyMarkHP(piece2, -1);
				}
			}
			
			if(RuleSet.checkAreas(turn.get(), board))
			{
				victoryAchieved = true;
			}
			
			
			break;
		case 1:
			hasDefended = true;
			if(piece1 > 0)
			{
				Utils.modifyCounters(2, Utils.getColor(piece1), Utils.getPieceType(piece1), false);
			}
			else if(piece1 < 0)
			{
				Utils.modifyCounters(2, Utils.getColor(piece1), 0, false);
			}
			
			//increment targets hp counter
			Utils.modifyPieceHP(piece2, 1);
			break;

		}

		testQueensMarkCondition(turn.get());
		
		move.add(new int[] {2,type.ordinal(),startValue,endValue, Utils.getPieceType(piece1)});
		
		redraw(grid);
	}





	/*
	 * for pieces:
			moving		:	(x,y) change
			teleporting :	(x,y) change
			rotating	:	angle change
			resigning	:	hp change
			soldier promotion	: type & hp change

	 */
	
	
	public void modifyAction(ModifyType type, int pos, int endValue)
	{
		int piece = board[pos];

		
		
		if(type == ModifyType.Move || type == ModifyType.Teleport)
		{
			boolean teleporting = type == ModifyType.Move ? false : true;
			
			Utils.modifyCounters(0, turn.get(), Utils.getPieceType(piece), teleporting);

			moved = true;
			if(teleporting)
			{
				board[endValue] = addData(board[endValue] , 0, plies);
				board[endValue] = addData(board[endValue] , 1, board[pos]);
			}
			

			//update piece coordinates
			Utils.setPos(piece, endValue);
			
			//remove defending hp counters
			if(Utils.getPieceHP(piece) > PieceData.pieceHP(Utils.getPieceType(piece)))
			{
				Utils.modifyPieceHP(piece, -Utils.getPieceHP(piece) + PieceData.pieceHP(Utils.getPieceType(piece)));
			}
			
			//if we have moved a rotated piece, then update rotated piece location to tX,tY
			if(pos == rotatedPieceLoc)
			{
				rotatedPieceLoc = endValue;
			}

			//make the move from start coordinates parsed from start Pane id string
			board[endValue] = piece;
			board[pos] = 0;
			redraw(grid);
		}
		else if(type == ModifyType.Promote)
		{
			board[pos] = instantiatePiece(general, turn.get(), pos, 0, PieceData.pieceHP(general));
			pieceIdCap++;
		}
		else if(type == ModifyType.Resign)
		{
			resignPiece(pos);
		}
		else if(type == ModifyType.Rotate)
		{
			Utils.doPieceRotation(board[pos], Utils.getPieceRotation(board[pos]) + endValue);
		}
		
		
		legalMoves = null;
		legalRange = null;
		
		if(movementRange != null) {
			movementRange.clear();
		}
		
		if(placementRange != null) {
			placementRange.clear();
		}
		
		testQueensMarkCondition(turn.get());
		getLegalSquares();
		getRange();
		
		if(type != ModifyType.Resign)
		{
			showingPiece.set(endValue);
		}
		
		move.add(new int[] {0,type.ordinal(),pos,endValue,Utils.getPieceType(piece)});
	}
	
	
	/*
	 * handle printing move data into console after turn changes
	 */
	
	private void setTurn(ArrayList<int[]> move) {
		
		double turnNo;

		//int[] {moveType, moveSubtype, pos, variable}
		//moving from 46 to 56: {type.modify, type.moving, 46, 56(=targetPosition)}
		//placing teleport mark at 27: {type.placemark, type.teleport, 27, 27(=redundant)}
		//rotating piece at 67 by 180: {type.modify, type.rotate, 67, 180(=angle change)}
		//attacked from 56 to 33: {type.ranged, type.attack, 56, 33}
		//spike mark activated from 67: {type.ranged, type.spike, 67, redundant}
		
		plies++;
		turnNo = plies/2.0;
		turn.set((turn.get() % 2)+1);
		int[] subMove;
		int type;
		
		for(int i = 0; i<move.size(); i++)
		{
			subMove = move.get(i);
			int source = subMove[2];
			int target = subMove[3];
			int pieceType = 0;
			if(subMove.length == 5)
			{
				pieceType = subMove[4];
			}
			
			type = subMove[1];
			
			System.out.println("submove = "+Arrays.toString(subMove));
			
			switch(subMove[0])
			{
				
				case 0:

					switch(type) {
					
						case 0:
							sb.append(Utils.pieceName(pieceType)+" to "+Utils.charConv(target)+"\n");
							break;
						case 1:
							sb.append("teleported "+Utils.pieceName(pieceType)+" from "+Utils.charConv(source)+" to "+Utils.charConv(target));
							break;
						case 2:
							sb.append("rotated "+Utils.pieceName(pieceType)+" at "+Utils.charConv(source)+" by "+target+"\n");
							break;
						case 3:
							sb.append("resigned "+Utils.pieceName(pieceType)+" previously at "+Utils.charConv(source)+"\n");
							break;
						case 5:
							sb.append("promoted new General at "+Utils.charConv(source)+"\n");
							break;
					}
					break;
				case 1:
					
					switch(type) {
					
						case 0:
							sb.append("area mark placed at "+Utils.charConv(source)+"\n");
							break;
						case 1:
							sb.append("teleport mark placed at "+Utils.charConv(source)+"\n");
							break;
						case 2:
							sb.append("spike mark placed at "+Utils.charConv(source)+"\n");
							break;
						case 3:
							sb.append("defense mark placed at "+Utils.charConv(source)+"\n");
							break;
						case 4:
							sb.append("placed Queens Mark at "+Utils.charConv(source)+"\n");
							break;
						case 5:
							sb.append("fractal mark placed at "+Utils.charConv(source)+"\n");
							break;
						case 6:
							sb.append("scarlet mark placed at "+Utils.charConv(source)+"\n");
							break;
						case 7:
							sb.append("mark upgraded at "+Utils.charConv(source)+"\n");
							break;
						
					}
					
					break;
				case 2:
					if(type == 0) 
					{
						if(board[source] == 0) 
						{
							sb.append("attacked "+Utils.charConv(target)+" by spike mark at "+Utils.charConv(source)+"\n");
						}
						else
						{
							sb.append("attacked "+Utils.charConv(target)+" by "+Utils.pieceName(pieceType)+" at "+Utils.charConv(source)+"\n");
						}
						
					}
					else if(type == 1)
					{
						if(board[source] == 0) 
						{
							sb.append("defended "+Utils.charConv(target)+" by defense mark at "+Utils.charConv(source)+"\n");
						}
						else
						{
							sb.append("defended "+Utils.charConv(subMove[3])+" by "+Utils.pieceName(pieceType)+" at "+Utils.charConv(source)+"\n");
						}
						
					}
					else if(type == 2)
					{
						sb.append("spike mark at "+Utils.charConv(source)+" activated!\n");
					}
					else if(type == 3)
					{
						sb.append("defense mark at "+Utils.charConv(source)+" activated!\n");
					}
					
					break;
					
			}
		}


		if(move.size() == 0)
		{
			sb.append("pass\n");
		}
		
		if(!gameRunning) {
			sb.append((turn.get()-1 == 1 ? "Black " : "Red ")+"won by area capture!");
		}
		else 
		{
			if(turnNo != 1)
			{
				if(turnNo % 1 == 0)
				{
					sb.append("\n-------------------------------\nTurn "+(int)turnNo+". \n\n");
				}
				else
				{
					sb.append("––\n");
				}
			}
		}
		
		console.setText(sb.toString());
		console.appendText("");		
		resetGameData();			
		legalMoves = null;
		legalRange = null;
		
		if (movementRange != null) 
			movementRange.clear();
		if (placementRange != null)
			placementRange.clear();
		
		getRange();
		getLegalSquares();
		boardList.add(board);
		System.out.println(boardList.size());
		savePos(board);
		
	}
	

	
	/*
	 * form necessary string for a piece or mark to get the corresponding image from resources/
	 */
	
	private String squareContent(int x, int y) {
		if(x == 11 || y == 11)
		{
			return null;
		}
		
		int element = board[x * 11 + y];

		
		if(element == 0) {
			return null;
		}

		char subType = 0;
		String type = "piece";
		
		//piece_qm_b
		if(element < 0)
		{
			type = "mark";
			subType = Utils.getMarkType(element).toString().toLowerCase().charAt(0);
			System.out.println("subtype"+subType);
		}
		else
		{
			System.out.println(element);
			subType = PieceData.pieceChar(Utils.getPieceType(element));
		}
		
	
		return type + "_" + subType + '_' + (Utils.getColor(element) == 1 ? 'b' : 'r');

	}
	

	
	/*
	 * Adds filled list2 of legal squares (of pieces Range) to observable list legalRangeObs,
	 * triggering listener, which adds visual cues to those squares
	 */
	
	private void getRange() {
		if(placementRange != null) {
			legalRangeObs.addAll(placementRange);
		}
		
	}
	
	/*
	 * Adds filled list of legal moves to observable list legalMovesObs,
	 * triggering listener, which adds visual cues to those squares
	 */
	
	private void getLegalSquares() {
		//System.out.println("changed"+movementRange.size());
		if(movementRange != null) {
			legalMovesObs.addAll(movementRange);
		}
		
	}
	
	
	/*
	 * get starting position of the game
	 */
	
	private GridPane initBoard(GridPane grid) {
		
		//id, type, color, pos, angle, hp of each piece
		
		String s = "237568";

		for(int i = 0, z1 = 0, z2 = 0, z3 = 2, z4 = 1; i<121; i++, z1 = i/11, z2 = i % 11, z3 += 2, z4 += 2) {
			int type = 0;
			if(i >= 22 && i < 99) {
				board[i] = 0;
				continue;
			}
			if(z1 == 0 || z1 == 10) {
				if(z2 < 5)
				{
					type = Integer.parseInt(""+s.charAt(z2));
				}
				else
				{
					type = Integer.parseInt(""+s.charAt(10-z2));
				}
				
			}
			else
			{
				if(z2 == 3 || z2 == 7)
				{
					type = 4;
				}
				else
				{
					type = 1;
				}
			}
			board[i] = instantiatePiece(type,i/11 < 3 ? 2 : 1,i,0,PieceData.pieceHP(type)); //new int[] {i+1,type,i/11 < 3 ? 2 : 1,i,0,PieceData.pieceHP(type)};
		}
		


		
		tempCounters = new int[] {1,0,1,0};
		arrayCopy(board,tempBoard);
		boardList.add(board);
		
		
		return redraw(grid);
		
		
	}
	
	/*
	 * empty board
	 */
	public void clearBoard()
	{
		for(int i = 0; i<121; i++) {
			board[i] = 0;
		}
	}
	
	
	/*
	 * Copies all elements from b1 into b2
	 */
	
	public void arrayCopy(int[] b1, int[] b2) {
		for(int i = 0; i < 121; i++)
		{	
			b2[i] = b1[i];

		}

	}
	
	public int instantiatePiece(int type, int color, int position, int rotation, int hp)
	{

		return 121 * Utils.makeId(type,color,position) + position + 46 * 121 * rotation + 46 * 121 * 4 * hp;
	}
	
	public int instantiateMark(int type, int color, int position, int lvl)
	{
		int x;
		if(color == 1)
		{
			x = type*2+1;
		}
		else
		{
			x = 2*(type+1);
		}
		
		x += 14 * position;
		
		x += 14 * 121 * lvl;
		
		return -x;
	}


	public static void main(String[] args) {
		launch(args);

	}

}



