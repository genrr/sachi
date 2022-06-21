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

public class App2 extends Application {
	
	public static int[][][] board = new int[12][12][];
	public static int[][][] tempBoard = new int[12][12][];
	ArrayList<int[][][]> boardList = new ArrayList<int[][][]>();
	private Mesh mesh;
	private int[] move;
	
	public static int plies = 2;
	static int h = 0;
	
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
	public static SimpleStringProperty showingPiece = new SimpleStringProperty();
	private static int[] tempCounters = new int[4]; 
	
	private static boolean gameRunning = true;
	private static boolean freePlay = true;
	private static boolean attackMode = false;
	private static boolean defenseMode = false;
	private static boolean markingMode = false;
	private static boolean queensMarkPlacementMode = false;
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
	private static Pane startPane;
	ArrayList<int[]> movementRange;
	ArrayList<int[]> placementRange;
	ArrayList<int[]> areaInterior = new ArrayList<int[]>();

	
	private static int[] moveSource = new int[] {-1, -1,-1};
	private static int[] attackSource = new int[] {-1, -1,-1};
	private static int[] defenderSource = new int[] {-1,-1,-1};
	private static int[] moveTarget = new int[] {-1, -1};
	private static int[] attackTarget = new int[] {-1, -1};
	private static int[] defendTarget = new int[] {-1,-1};
	private static int[] markSource = new int[] {-1,-1,-1};
	private static int[] markTarget = new int[] {-1, -1};
	private static int resignedPieceType = 0;
	private static int[] resignedPieceLoc = new int[] {-1,-1};
	private static int[] generalPromotionLoc = new int[] {-1,-1};
	private static int[] queensMarkTarget = new int[] {-1,-1};
	private static boolean hasQueensMarkOrQueen = false;
	private int queenAreaLvl;
	private static int[] rotatedPieceLoc = new int[] {-1,-1};
	private static int rotationAngle;

	private static Pane[][] sqArray = new Pane[12][12];
	private static GridPane grid = new GridPane();
	private static TextArea console = new TextArea();
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
	Button placeScarletMark = new Button("place Scarlet mark");
	
	private boolean loading = false;
	private boolean saving = false;
	private static Random rng = new Random();
	
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
		
		window.setOnCloseRequest(e -> {
			if(mesh != null) {
				mesh.close();
			}
			Platform.exit();
		});
		
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
		TextArea pieceInfo = new TextArea();
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
		Button resignPiece = new Button("resign piece");
		
		pieceMenu.getChildren().addAll(resignPiece);
		
		//sidebar hierarchy
		generalInfo.getChildren().addAll(p1counterText,p1countersPerTurnText,p2counterText,p2countersPerTurnText,endTurn,reset,turnInfoLabel);	
		overview.getChildren().addAll(generalInfo,pieceView,markView);
		pieceView.getChildren().addAll(pieceInfo,pieceMenu);
		markView.getChildren().addAll(statusConsole,markMenu);

		//bottom menu hierarchy
		mainMenu.getChildren().addAll(start,connect,chooseColor1,chooseColor2,randomColor, resetGame, save,load,exit,resign,startGame);
		menu.getChildren().addAll(mainMenu, new Separator(), turnMenu);
		
		//sizings and anchors
		console.setPrefSize(200, 200);
		main.setLeft(initBoard(grid));
		main.setBottom(menu);
		main.setRight(rightSide);
		BorderPane.setMargin(rightSide, new Insets(25));
		
		saveGameState();
		console.textProperty().set(sb.toString());
    	sb.append("-------------------------------\nTurn 1:\n");
    	console.setText(sb.toString());
		
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
							//System.out.println(x+" "+y);
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
						if(board[i][j] == null) {
							if(((ImageView)(sqArray[j][i].getChildren().get(1))).getImage() != null) {
								//System.out.println("setting "+i+" "+j+" to null");
								((ImageView)(sqArray[j][i].getChildren().get(1))).setImage(null);
							}
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
	 					
	 					if(board[x][y] == null) {
		 					if(attackMode) {
		 						((ImageView)(sqArray[y][x].getChildren().get(1))).setImage(new Image(getClass().getClassLoader().getResource("resources/att_marker.png").toExternalForm(),64,64,true,true));
		 					}
		 					else if(markingMode){
		 						((ImageView)(sqArray[y][x].getChildren().get(1))).setImage(new Image(getClass().getClassLoader().getResource("resources/mark_marker.png").toExternalForm(),64,64,true,true));
		 					}
	 					}
	 					else {
	 						if(attackMode) {
	 							((Rectangle)(sqArray[y][x].getChildren().get(0))).setFill(new Color(255.0/255,80.0/255,79.0/255,(256-87.0)/255));	 					
	 						}
	 						else if(defenseMode)
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
							//System.out.println(x+" "+y);
							((ImageView)(sqArray[y][x].getChildren().get(1))).setImage(null);
						}
					}
				}
				
			}
			
		});
				
		
		scene.addEventHandler(KeyEvent.KEY_PRESSED, key -> {
			if(key.getCode() == KeyCode.SHIFT) {
				queensMarkPlacementMode = false;
				attackMode = true;
				parseId(startPane.getId(),1);
				getRange();
			}
			else if(key.getCode() == KeyCode.ALT) {
				queensMarkPlacementMode = false;
				markingMode = true;
				parseId(startPane.getId(), 3);
				System.out.println("mark source set: "+markSource[1]+" "+markSource[2]);
				getRange();
			}
			else if(key.getCode() == KeyCode.CONTROL) {
				queensMarkPlacementMode = false;
				defenseMode = true;
				parseId(startPane.getId(),2);
				getRange();
			}

		});
		
		scene.addEventHandler(KeyEvent.KEY_RELEASED, key -> {
			if(key.getCode() == KeyCode.SHIFT) {
				if(!hasAttacked)
				{
					attackSource = new int[] {-1,-1,-1};
				}
				attackMode = false;
				legalMovesObs.clear();
				legalMoves = null;
				getRange();
				getLegalSquares();
			}
			else if(key.getCode() == KeyCode.ALT) {
				if(!markPlaced)
				{
					markSource = new int[] {-1,-1,-1};
				}
				markingMode = false;

				getRange();
				getLegalSquares();
			}
			else if(key.getCode() == KeyCode.CONTROL) {
				defenseMode = false;

				getRange();
				getLegalSquares();
			}

		});
		
		scene.addEventHandler(KeyEvent.KEY_PRESSED, key -> {
			if(key.getCode() == KeyCode.PAGE_UP)
			{
				
			}
			else if(key.getCode() == KeyCode.PAGE_DOWN)
			{
				board = boardList.get(boardList.size()-2);
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
			
			//parseId(startPane.getId(),0);
			String s = showingPiece.get();
			int x = Integer.parseInt(s.substring(0,2));
			int y = Integer.parseInt(s.substring(2,4));
			resignPiece(x, y);
			
		});
		
		rotateClockwise.setOnAction(e -> {
			String s = showingPiece.get();
			int i = Integer.parseInt(s.substring(0,2));
			int j = Integer.parseInt(s.substring(2,4));
			
			//some piece has been rotated, but piece that is currently selected and to be rotated is not the piece previously rotated?
			if(rotatedPieceLoc[0] != -1 && board[i][j][0] != board[rotatedPieceLoc[0]][rotatedPieceLoc[1]][0])
			{
				statusConsole.appendText("only one piece can be rotated!\n");
				return;
			}
			
			System.out.println("dir "+board[i][j][6]+" h: "+h);
			h -= 90;
			double x = Math.cos((h*Math.PI*2)/360.0);
			double y = Math.sin((h*Math.PI*2)/360.0);
			x = Math.round(x);
			y = Math.round(y);
			System.out.println("x "+x+" y "+y);
			int dir = 0;
			
			if(x == 1 && y == 0)
			{
				dir = 0;
			}
			else if(x == 0 && y == 1)
			{
				dir = -90;
			}
			else if(x == -1 && y == 0)
			{
				dir = 180;
			}
			else if(x == 0 && y == -1)
			{
				dir = 90; 
			}
			board[i][j][6] = dir;
			rotatedPieceLoc = new int[] {i, j};
			rotationAngle = dir;
			legalMovesObs.clear();
			iterateBoard(i,j);
			getLegalSquares();
		});
		
		rotateCounterClockwise.setOnAction(e -> {
			String s = showingPiece.get();
			int i = Integer.parseInt(s.substring(0,2));
			int j = Integer.parseInt(s.substring(2,4));
			
			//some piece has been rotated, but piece that is currently selected and to be rotated is not the piece previously rotated!
			if(rotatedPieceLoc[0] != -1 && board[i][j][0] != board[rotatedPieceLoc[0]][rotatedPieceLoc[1]][0])
			{
				statusConsole.appendText("only one piece can be rotated!\n");
				return;
			}
			
			System.out.println("dir "+board[i][j][6]+" h: "+h);
			h += 90;
			double x = Math.cos((h*Math.PI*2)/360.0);
			double y = Math.sin((h*Math.PI*2)/360.0);
			x = Math.round(x);
			y = Math.round(y);
			int dir = 0;
			
			if(x == 1 && y == 0)
			{
				dir = 0;
			}
			else if(x == 0 && y == 1)
			{
				dir = -90;
			}
			else if(x == -1 && y == 0)
			{
				dir = 180;
			}
			else if(x == 0 && y == -1)
			{
				dir = 90; 
			}
			board[i][j][6] = dir;
			rotatedPieceLoc = new int[] {i,j};
			rotationAngle = dir;
			legalMovesObs.clear();
			iterateBoard(i,j);
			getLegalSquares();
		});
		
		placeQueensMark.setOnAction(e -> {
			attackMode = false;
			markingMode = false;
			getRange();
			queensMarkPlacementMode = true;
			legalMovesObs.clear();
			legalRangeObs.clear();
			String s = showingPiece.get();
			int x = Integer.parseInt(s.substring(0,2));
			int y = Integer.parseInt(s.substring(2,4));
			
			iterateBoard(x, y);
			
			for(int[] i : areaInterior)
			{
				for(int[] j : placementRange)
				{
					if(i[0] == j[0] && i[1] == j[1])
					{
						((ImageView)(sqArray[i[1]][i[0]].getChildren().get(1))).setImage(new Image(getClass().getClassLoader().getResource("resources/queen_marker.png").toExternalForm(),64,64,true,true));
					}
				}
				
			}
			
		});
		
		resetGame.setOnAction(e -> {
			gameRunning = true;
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
			resetGameData();
			arrayCopy(tempBoard,board);
			redraw(grid);
		});
		
		//when 'end turn' is pressed, advance turn + send move forward
		endTurn.setOnAction(e -> {
			if(gameRunning && (freePlay || (playerColor == turn.get()))) {
				if(victoryAchieved)
				{
					gameRunning = false;
				}
				
				setTurn(moveSource[1],moveSource[2], moveTarget[0], moveTarget[1], 
						markTarget[0], markTarget[1],
						attackSource[1], attackSource[2], attackTarget[0], attackTarget[1],
						defenderSource[1], defenderSource[2], defendTarget[0], defendTarget[1],
						queensMarkTarget[0], queensMarkTarget[1],
						resignedPieceType, resignedPieceLoc[0], resignedPieceLoc[1],
						rotatedPieceLoc[0], rotatedPieceLoc[1], rotationAngle, queenAreaLvl, generalPromotionLoc[0], generalPromotionLoc[1]);
				sendMove(move);
				
				
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
			
			int x = Integer.parseInt(newValue.substring(0,2));
			int y = Integer.parseInt(newValue.substring(2,4));
			
			System.out.println("x : "+x+" y : "+y);
			
			int id = board[x][y][0];
			int type = board[x][y][1];
			
			String s = "";
			
			s = pieceType((char)board[x][y][5])+" ("+(char)board[x][y][5]+") \nAttack Cost: "+PieceData.pieceAC(type)+"\nDefense Cost: "+PieceData.pieceDC(type)+"\nMove cost: "+PieceData.moveCost(type)+"\nValue: "+PieceData.pieceValue(type);
			
			switch(type) {
			case 1:
				pieceMenu.getChildren().addAll(resignPiece,promoteSoldier);
				break;
			case 2:
				pieceMenu.getChildren().addAll(resignPiece,rotateClockwise,rotateCounterClockwise);
				break;
			case 3:
				pieceMenu.getChildren().addAll(resignPiece,rotateClockwise,rotateCounterClockwise);
				break;
			case 4:
				pieceMenu.getChildren().addAll(resignPiece,rotateClockwise,rotateCounterClockwise);
				break;
			case 5:
				pieceMenu.getChildren().addAll(resignPiece);
				break;
			case 6:
				pieceMenu.getChildren().addAll(resignPiece,rotateClockwise,rotateCounterClockwise);
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
				if(id == i)
				{
					pieceMenu.getChildren().addAll(placeQueensMark);
				}
			}

		});
		
		promoteSoldier.setOnAction(e -> {

			for(int i = 0; i<11; i++)
			{
				for(int j = 0; j<11; j++)
				{
					//general still on the board
					if(board[j][i] != null && board[j][i][1] == 8 && board[j][i][2] == turn.get())
					{
						statusConsole.appendText("General still exists on the board!\n");
						return;
					}
				}
			}
			
			String s = showingPiece.get();
			int x = Integer.parseInt(s.substring(0,2));
			int y = Integer.parseInt(s.substring(2,4));
			
			board[x][y] = new int[] {pieceIdCap,general,turn.get(),x,y,107, 0, PieceData.pieceHP(8)};
			generalPromotionLoc[0] = x;
			generalPromotionLoc[1] = y;
			redraw(grid);
			pieceIdCap++;
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
	
	private void savePos(int[][][] b) {
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
		
		moveSource =  new int[] {-1, -1,-1};
		moveTarget  = new int[] {-1, -1};
		markSource = new int[] {-1,-1,-1};
		markTarget  = new int[] {-1, -1};
		attackSource  = new int[] {-1, -1,-1};
		attackTarget = new int[] {-1, -1};
		defenderSource = new int[] {-1,-1,-1};
		defendTarget = new int[] {-1,-1};
		resignedPieceLoc = new int[] {-1,-1};
		queensMarkTarget = new int[] {-1,-1};
		rotatedPieceLoc = new int[] {-1,-1};
		generalPromotionLoc = new int[] {-1,-1};
		rotationAngle = 0;
		moved = false;
		markPlaced = false;
		hasQueensMarkOrQueen = false;
		hasAttacked = false;
		hasDefended = false;
		hasResignedPiece = false;
		resignedPieceType = 0;
		queensMarkPieceIDList.clear();
		areaInterior.clear();
	}
	
	/*
		Reset board and stats to initial positions
	 */
	
	private void resetWholeGame() {
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
//				else if(lineCounter == 3)
//				{
//					moved = Integer.parseInt(line[0]) == 0 ? false : true;
//					markPlaced = Integer.parseInt(line[1]) == 0 ? false : true;
//					hasAttacked = Integer.parseInt(line[2]) == 0 ? false : true;
//					hasDefended = Integer.parseInt(line[3]) == 0 ? false : true;
//					hasResignedPiece = Integer.parseInt(line[4]) == 0 ? false : true;
//					hasQueensMarkOrQueen = Integer.parseInt(line[5]) == 0 ? false : true;
//				}
				else if(lineCounter != 0)
				{
					int[] t = new int[line.length];
					for(int k = 0; k<line.length; k++)
					{
						t[k] = Integer.parseInt(""+line[k]);
					}
					//assign vector into board at t[3], t[4]
					board[t[3]][t[4]] = t;
				}
				System.out.println(Arrays.toString(line));
				
				lineCounter++;
			}
			//game data should be at the initial values
			resetGameData();
			//triggering the save of board into tempBoard now because tempBoard might be at default value(starting position)
			savePos(board);
			
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
		//s += (moved ? 1 : 0) +" "+ (markPlaced ? 1 : 0) +" "+ (hasAttacked ? 1 : 0) +" "+ (hasDefended ? 1 : 0) +" "+ (hasResignedPiece ? 1 : 0) +" "+ (hasQueensMarkOrQueen ? 1 : 0)+"\n";
//		s += Arrays.toString(moveSource)+"\n" + Arrays.toString(moveTarget)+"\n";
//		s += Arrays.toString(markSource)+"\n" + Arrays.toString(markTarget)+"\n";
//		s += Arrays.toString(attackSource)+"\n" + Arrays.toString(attackTarget)+"\n";
//		s += Arrays.toString(defenderSource)+"\n"+Arrays.toString(defendTarget)+"\n";
//		s += Arrays.toString(resignedPieceLoc)+"\n" + resignedPieceType;
//		s += Arrays.toString(queensMarkTarget)+"\n";
//		s += Arrays.toString(rotatedPieceLoc)+"\n";
//		s += rotationAngle;
//		 
//		for(int i = 0; i<queensMarkPieceIDList.size(); i++)
//		{
//			s += queensMarkPieceIDList.get(i)+" ";
//		}
//		
//		s += "\n";
		
		//revert back to state at the start of the turn
		arrayCopy(tempBoard,board);

		
		
		try(FileWriter outFile = new FileWriter("savegame-"+ slotNo +".txt",false);
				BufferedWriter bWriter = new BufferedWriter(outFile))
		{
			
			for(int i = 0; i<11; i++)
			{
				for(int j = 0; j<11; j++)
				{
					if(board[j][i] != null) {
						for(int k = 0; k<board[j][i].length; k++)
						{
							s += String.valueOf(board[j][i][k])+" ";
						}
						s += "\n";
					}
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
		int[] move = (int[])msg.getPayload();
		System.out.println("received move at board: "+Arrays.toString(move));

		int sX = move[0];
		int sY = move[1];
		int tX = move[2];
		int tY = move[3];
		int markX = move[4];
		int markY = move[5];
		int markColor = move[6];
		int attSX = move[7];
		int attSY = move[8];
		int attTX = move[9];
		int attTY = move[10];
		int defSX = move[11];
		int defSY = move[12];
		int defTX = move[13];
		int defTY = move[14];
		int qmX = move[15];
		int qmY = move[16];
		int resP = move[17];
		int resPX = move[18];
		int resPY = move[19];
		int rotPX = move[20];
		int rotPY = move[21];
		int rot = move[22];
		int qmlvl = move[23];
		int gpx = move[24];
		int gpy = move[25];
		
		if (tX != -1) {
			makeMove(sX, sY, tX, tY, grid);
		}
		
		if (markX != -1) {
			if(board[markX][markY] != null)
			{
				upgradeMark(markX,markY);
			}
			else 
			{
				placeMark(markX,markY,markColor);
			}
			
		}
		
		if (attTX != -1) {
			attack(attSX,attSY,attTX,attTY);
		}
		
		if(defTX != -1)
		{
			defend(defSX, defSY, defTX, defTY);
		}
		
		if(qmX != -1)
		{
			placeQueensMark(qmX, qmY, qmlvl);
		}
		
		if(resP != 0)
		{
			resignPiece(resPX, resPY);
		}
		
		if(rotPX != -1)
		{
			board[rotPX][rotPY][6] = rot;
		}
		
		if(RuleSet.checkAreas(turn.get(), board))
		{
			gameRunning = false;
		}
		
		
		//advance turn after opponents move was made
		setTurn(sX,sY,tX,tY,markX,markY,attSX,attSY,attTX,attTY,defSX,defSY,defTX,defTY,qmX,qmY,resP,resPX,resPY,rotPX,rotPY,rot,qmlvl,gpx,gpy);

		
		redraw(grid);
		
		
	}
	
	

	
	/*
	 * Called when ending turn to send the move to other player by socket connection
	 */
	
	public void sendMove(int[] move) {
		
		if(mesh == null) {
			return;
		}

        Message msgObject = new Message(move, Message.Tyyppi.MOVE);
        msgObject.setSender(this.toString());
        System.out.println("sending move(App2): " + Arrays.toString(move));
        mesh.broadcast(msgObject);
		
	}
	
	
	
	/*
	 * Convert board b into save format
	 */
	
	public void saveState(int[][][] b) {
		
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
				Label hpCounter = new Label();

				
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
					sqArray[i][j].getChildren().add(hpCounter);
					hpCounter.setAlignment(Pos.BOTTOM_RIGHT);
				}
				

				if(squareContent(temp,temp2) != null) {
					if(squareContent(temp,temp2).equals("mark_a_b")) {
						piece.setImage(new Image(getClass().getClassLoader().getResource("resources/mark_area_r.png").toExternalForm(),64,64,true,true));
						if(board[j][i][8] > 0)
						{
							hpCounter.setText(String.valueOf(board[j][i][8]));
						}
					}
					else if(squareContent(temp, temp2).equals("mark_a_r")) {
						piece.setImage(new Image(getClass().getClassLoader().getResource("resources/mark_area_b.png").toExternalForm(),64,64,true,true));
						if(board[j][i][8] > 0)
						{
							hpCounter.setText(String.valueOf(board[j][i][8]));
						}
					}
//					else if(squareContent(temp,temp2).equals("piece_d")) {
//						piece.setImage(new Image(getClass().getClassLoader().getResource("resources/mark_def.png").toExternalForm(),64,64,true,true));
//					}
//					else if(squareContent(temp,temp2).equals("piece_D")) {
//						piece.setImage(new Image(getClass().getClassLoader().getResource("resources/mark_def.png").toExternalForm(),64,64,true,true));
//					}
					else if(squareContent(temp, temp2).equals("mark_q_b"))
					{
						piece.setImage(new Image(getClass().getClassLoader().getResource("resources/queens_mark_b.png").toExternalForm(),64,64,true,true));
					}
					else if(squareContent(temp, temp2).equals("mark_q_r"))
					{
						piece.setImage(new Image(getClass().getClassLoader().getResource("resources/queens_mark_r.png").toExternalForm(),64,64,true,true));
					}
					else {
						piece.setImage(new Image(getClass().getClassLoader().getResource("resources/"+squareContent(temp,temp2)+".png").toExternalForm(),64,64,true,true));
						if(board[j][i][7] > 0)
						{
							hpCounter.setText(String.valueOf(board[j][i][7]));
						}
					}
				}
				
				Pane p = sqArray[i][j];

				
				p.setOnMousePressed(e -> p.setMouseTransparent(true));
				p.setOnMouseReleased(e -> p.setMouseTransparent(false));
				
				p.setOnMousePressed(e -> {
					
					
					//System.out.println("selected:"+(board[j][i] == null ? "null" : String.valueOf(board[j][i][0])));
					if(!gameRunning) {
						return;
					}
//					
					((Rectangle)(p.getChildren().get(0))).setFill(new Color(0.3, 0.3, 0.3, 1));
					
					int t = temp;
					int t2 = temp2;
				
					if((board[t][t2] != null)) {
						System.out.println( (board[t][t2] == null) +" "+(board[t][t2][2] == turn.get() )+" "+ (board[t][t2][2] != turn.get() && attackMode) );
					}
					
					
					//a == null OR (freeplay AND selected turn-colored piece) OR (online AND selected own piece) OR
					//freeplay AND selected opponents-turn-colored piece AND attackmode OR
					//online AND selected opponents piece AND attackmode
					
					/*
					 * a == null ||
					 * (freePlay && ((a.color == turn.get()) || attackMode)) ||
					 * a.color == playerColor || attackMode
					 */
					if(board[t][t2] == null || (freePlay && board[t][t2][2] == turn.get()) ||
							(!freePlay && board[t][t2][2] == playerColor) ||
							(freePlay && board[t][t2][2] != turn.get() && attackMode) ||
							(!freePlay && board[t][t2][2] != playerColor && attackMode)) {
						
						System.out.println("t "+t+" t2 "+t2);
						
						//reset legal moves / ranges
						legalMovesObs.clear();
						legalMoves = null;


						//iterate 11x11 board squares, get squares which represent legal moves / range for this piece
						if(board[t][t2] != null) {
							showingPiece.set((t < 10 ? "0" : "") + t + (t2 < 10 ? "0" : "") + t2);
							if(board[t][t2][0] > 0) {
								iterateBoard(t,t2);
							}
						}
						
						//only reset legal range if piece was not selected
						if(!awaitingSquare) {
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
						else if(isLegal(temp,temp2,legalRange)){
							
							if(markingMode) 
							{
								if(!markPlaced)
								{
									if(((turn.get()==1 && p1counters.get() >= p1countersPerTurn.get()) || (turn.get()==2 && p2counters.get() >= p2countersPerTurn.get())) ) 
									{
										if(board[t][t2] == null)
										{
											if(markSource[1] != attackSource[1] || markSource[2] != attackSource[2])
											{
												placeMark(t,t2,turn.get());
												markTarget = new int[] {temp,temp2};
											}
											else
											{
												statusConsole.appendText("cannot place a mark with a piece that has attacked this turn!\n");
												
												
											}
											
										}
										else if(board[t][t2][0] < 0 && board[t][t2][2] == turn.get())
										{
											if(board[t][t2][8] < 5)
											{
												if(board[t][t2][6] != 5)
												{
													if(((turn.get()==1 && p1counters.get() >= 2*p1countersPerTurn.get()) || (turn.get()==2 && p2counters.get() >= 2*p2countersPerTurn.get())) )
													{
														upgradeMark(t,t2);
														markTarget = new int[] {temp, temp2};
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
							else if(attackMode){
								
								
								//System.out.println("#"+p1counters.get()+"#"+attackSource[0]+"#"+attackSource[1]+"#"+attackSource[2]);
								if(board[temp][temp2] != null)
								{
									if((turn.get()==1 && p1counters.get() >= PieceData.pieceAC(board[attackSource[1]][attackSource[2]][1])) ||
								   (turn.get()==2 && p2counters.get() >= PieceData.pieceAC(board[attackSource[1]][attackSource[2]][1])))
									{
										if(!RuleSet.iterateRange(attackSource[1], attackSource[2], temp, temp2, board, true, true, true))
										{
											if(!hasAttacked)
											{
												if(moveTarget[0] != attackSource[1] || moveTarget[1] != attackSource[2])
												{
													if(markSource[1] != attackSource[1] || markSource[2] != attackSource[2])
													{
														attack(attackSource[1], attackSource[2], temp,temp2);
														((ImageView) ((Pane) sqArray[temp][temp2]).getChildren().get(1)).setImage(null);
														attackTarget = new int[] {temp,temp2};
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
								parseId(startPane.getId(),2);
								
								if((turn.get()==1 && p1counters.get() >= PieceData.pieceDC(board[defenderSource[1]][defenderSource[2]][1])) ||
										(turn.get()==2 && p2counters.get() > PieceData.pieceDC(board[defenderSource[1]][defenderSource[2]][1]))) {
									
									if(!hasDefended) 
									{
										if(board[defenderSource[1]][defenderSource[2]][2] == turn.get() && turn.get() == board[temp][temp2][2])
										{
											defend(defenderSource[1], defenderSource[2], temp,temp2);
											defendTarget = new int[] {temp,temp2};
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
								for(int[] is : areaInterior)
								{
									
									System.out.println("temp : "+temp+" temp2 : "+temp2+" is[1] : "+is[1]+" is[0] : "+is[0]);
									if(board[temp][temp2] == null && is[0] == temp && is[1] == temp2)
									{
										for(int m = 0; m<11; m++)
										{
											for(int n = 0; n<11; n++)
											{
												if(board[m][n] != null && board[m][n][2] == turn.get() && 
														((board[m][n][0] < 0 && board[m][n][6] == 5) || (board[m][n][1] == 9)))
												{
													hasQueensMarkOrQueen = true;
												}
											}
										}
										if(!hasQueensMarkOrQueen)
										{

											if((turn.get() == 1 && p1counters.get() >= (2 * p1countersPerTurn.get() - queenAreaLvl)) || 
											(turn.get() == 2 && p2counters.get() >= (2 * p2countersPerTurn.get() - queenAreaLvl)))
											{
												placeQueensMark(temp, temp2, queenAreaLvl);
												queensMarkTarget = new int[] {temp, temp2};
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
							
							awaitingSquare = false;
							
						}
						else if(board[temp][temp2] != null && board[temp][temp2][2] == turn.get()) 
						{

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
					if(db.hasString() && isLegal(temp,temp2,legalMoves)) 
					{
						parseId(((Pane)event.getGestureSource()).getId(),0);
						

						if((turn.get()==1 && p1counters.get() >= PieceData.moveCost(board[moveSource[1]][moveSource[2]][1])) ||
								(turn.get()==2 && p2counters.get() >= PieceData.moveCost(board[moveSource[1]][moveSource[2]][1]))) 
						{
							if(moveSource[1] != attackSource[1] || moveSource[2] != attackSource[2])
							{
								
								Image newPiece = new Image(db.getString(),64,64,true,true);
								((ImageView) p.getChildren().get(1)).setImage(newPiece);
								((ImageView) ((Pane) event.getGestureSource()).getChildren().get(1)).setImage(null);
								event.setDropCompleted(true);
								
								makeMove(moveSource[1], moveSource[2], temp,temp2,grid);
								moveTarget = new int[] {temp,temp2};
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
	
	
	private void placeQueensMark(int temp, int temp2, int lvl) {
		
		if(turn.get() == 1 )
		{
			p1counters.set(p1counters.get() - (2*p1countersPerTurn.get() - lvl));
		}
		else
		{	
			p2counters.set(p2counters.get() - (2*p2countersPerTurn.get() - lvl));
		}
		
		board[temp][temp2] = new int[] {markId,-1,turn.get(),temp,temp2,109,1,5,plies};
		
		hasQueensMarkOrQueen  = true;
		
		redraw(grid);
		
	}


	/*
	 * is (paneX,paneY) contained in legal squares?
	 */

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
				//System.out.println("is legal range");
				return true;
			}
		}
		return false;
	}
	
	
	/*
	 * depending on mode, fill source arrays using id string to be used when printing info to console
	 */
	
	private void parseId(String id,int mode){
		
		//System.out.println("parsing: "+id);
		
		if(mode == 0) {
			moveSource = new int[3];
		}
		else if(mode == 1) {
			attackSource = new int[3];
		}
		else if(mode == 2) {
			defenderSource = new int[3];
		}
		else if(mode == 3) {
			markSource = new int[3];
		}
		
		
		int i = 0;
		int j = 0;
		String temp = "";

		
		while(i < id.length()) {
			if(id.charAt(i) == '_') {
				if(mode == 0) {
					moveSource[j] = Integer.parseInt(temp);
					System.out.println("got movesource "+j+" "+moveSource[j]);
				}
				else if(mode == 1) {
					attackSource[j] = Integer.parseInt(temp);
				}
				else if(mode == 2) {
					defenderSource[j] = Integer.parseInt(temp);
				}
				else if(mode == 3) {
					markSource[j] = Integer.parseInt(temp);
							
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
	
	/*
	 * move a piece to (tX,tY) and update board with redraw()
	 */
	
	private void makeMove(int sX, int sY, int tX, int tY,GridPane grid) {
		

		int[] piece = board[sX][sY];
		
		System.out.println("piece "+board[sX][sY][0]+" at x = "+sX+" y = "+sY);
		
		//subtract move cost
		if(piece[2] == 1) {
			p1counters.set(p1counters.get() - PieceData.moveCost(piece[1]));
		}
		else if(piece[2] == 2) {
			p2counters.set(p2counters.get() - PieceData.moveCost(piece[1]));
		}
		
		//update piece coordinates
		piece[3] = tX;
		piece[4] = tY;
		
		//remove defending hp counters
		if(piece[7] > PieceData.pieceHP(piece[1]))
		{
			piece[7] = PieceData.pieceHP(piece[1]);
		}

		//make the move from start coordinates parsed from start Pane id string
		board[tX][tY] = piece;
		board[sX][sY] = null;
		redraw(grid);

		legalMoves = null;
		legalRange = null;
		
		if(movementRange != null) {
			movementRange.clear();
		}
		
		if(placementRange != null) {
			placementRange.clear();
		}
		
		showingPiece.set((tX < 10 ? "0" : "") + tX + (tY < 10 ? "0" : "") + tY);
		
		//if we have moved a rotated piece, then update rotated piece location to tX,tY
		if(sX == rotatedPieceLoc[0] && sY == rotatedPieceLoc[1])
		{
			rotatedPieceLoc[0] = tX;
			rotatedPieceLoc[1] = tY;
		}
		
		testQueensMarkCondition(turn.get());
		
		getLegalSquares();
		getRange();
		moved = true;
		
		System.out.println("now piece "+board[tX][tY][0]+" is at x = "+tX+" y = "+tY);
	}
	
	
	
	/*
	 * Place mark at (x,y) and update board with redraw()
	 */
	
	private void placeMark(int x, int y, int color) {
		int markChar = 109;
		
		if(turn.get() == 1) {
			p1counters.set(p1counters.get() - p1countersPerTurn.get());
			p1countersPerTurn.set(p1countersPerTurn.get()+1);
		}
		else {
			p2counters.set(p2counters.get() - p2countersPerTurn.get());
			p2countersPerTurn.set(p2countersPerTurn.get()+1);
		}

		board[x][y] = new int[] {markId,-1,color,x,y,markChar,1,1,0};
		redraw(grid);
		markId--;
		markPlaced = true;
		
		testQueensMarkCondition(x,y,color);
		
		
		
		//check for winning condition
		if(RuleSet.checkAreas(turn.get(), board)) {
			victoryAchieved = true;
		}
	}
	
	
	/*
	 * Upgrade mark at (x,y)
	 * 
	 */
	
	private void upgradeMark(int x, int y)
	{
		
		if(turn.get() == 1)
		{
			p1counters.set(p1counters.get()- 2 * p1countersPerTurn.get());
		}
		else
		{
			p2counters.set(p2counters.get()- 2 * p2countersPerTurn.get());
		}
		board[x][y][8]++;
		redraw(grid);
	}
	
	
	private void testQueensMarkCondition(int color)
	{
		for(int i = 0; i<11; i++)
		{
			for(int j = 0; j<11; j++)
			{
				if(board[j][i] != null && board[j][i][0] < 0 && board[j][i][2] == color)
				{
					testQueensMarkCondition(j, i, color);
				}
			}
		}
	}
	
	private void testQueensMarkCondition(int x, int y, int color) {
		
		areaInterior.clear();
		
		int[] area = RuleSet.getArea(x, y, board, color, x);
		
		
		
		if(area != null)
		{
			for(int i = 0; i<11; i++)
			{
				for(int j = 0; j<11; j++)
				{
					if(board[i][j] != null && board[i][j][2] == color && board[i][j][0] > 0)
					{
						//own piece found, get range
						iterateBoard(i, j);
						
						for (int j2 = 0; j2 < placementRange.size(); j2++) 
						{
							System.out.println("area: "+area[0]+" "+area[1]+" "+area[2]+" "+area[3]);
							System.out.println("coord of a piece "+placementRange.get(j2)[0]+", "+placementRange.get(j2)[1]);
							if(placementRange.get(j2)[0] > area[0] && placementRange.get(j2)[0] < area[1] && placementRange.get(j2)[1] > area[2] && placementRange.get(j2)[1] < area[3])
							{
								if(!areaInterior.contains(placementRange.get(j2))) 
								{
									areaInterior.add(placementRange.get(j2));
								}
								
								//found at least one own piece, that can place a mark in the interior of the 'area'
								if(!queensMarkPieceIDList.contains(board[i][j][0]))
								{
									queensMarkPieceIDList.add(board[i][j][0]);
									System.out.println("adding piece "+board[i][j][0]);
								}
								
							}
						}
						
					}
				}
			}
			
		}
		
		if(areaInterior.size() > 0)
		{
			queenAreaLvl = board[area[0]][area[2]][8];
		}
		
	}


	/*
	 * Attack piece at square (x,y)
	 */
	
	private void attack(int sx, int sy, int tx, int ty) {
		System.out.println("attacking from "+sx+", "+sy+" to "+tx+", "+ty);
		
		//subtract attack cost of the attacking piece
		if(board[tx][ty][2]==1) {
			p2counters.set(p2counters.get() - PieceData.pieceAC(board[sx][sy][1]));
			
		}
		else {
			p1counters.set(p1counters.get() - PieceData.pieceAC(board[sx][sy][1]));
			
		}
		
		hasAttacked = true;
		
		testQueensMarkCondition(turn.get());
		
		
		//if target is captured, increment opposing players Counters by captured piece's value
		//target is a piece:
		if(board[tx][ty][0] > 0)
		{
			if(board[tx][ty][7] == 0)
			{
				if(board[tx][ty][2]==1) 
				{
					p2counters.set(p2counters.get() + PieceData.pieceValue(board[tx][ty][1]));
				}
				else
				{
					p1counters.set(p1counters.get() + PieceData.pieceValue(board[tx][ty][1]));
				}
				board[tx][ty] = null;
			}
			else
			{
				board[tx][ty][7]--;
			}
		}
		//target is a mark:
		else {
			if(board[tx][ty][8] == 0)
			{
				if(board[tx][ty][2]==1) 
				{
					p2counters.set(p2counters.get() + 1);
					p1countersPerTurn.set(p1countersPerTurn.get()-1);
				}
				else
				{
					p1counters.set(p1counters.get() + 1);
					p2countersPerTurn.set(p2countersPerTurn.get()-1);
				}
				board[tx][ty] = null;
			}
			else
			{
				board[tx][ty][8]--;
			}
		}
		
		if(RuleSet.checkAreas(turn.get(), board))
		{
			victoryAchieved = true;
		}
		
		redraw(grid);
	}
	
	/*placing defense mark at square (x,y) for one turn
	 * 
	 */
	
	private void defend(int sX, int sY, int tX, int tY) 
	{
		//subtract defense cost from the defender
		if(turn.get() == 1)
		{
			p1counters.set(p1counters.get() - PieceData.pieceDC(board[sX][sY][1]));
		}
		else
		{
			p2counters.set(p2counters.get() - PieceData.pieceDC(board[sX][sY][1]));
		}
		

		hasDefended = true;
		
		//increment targets hp counter
		board[tX][tY][7]++;
		redraw(grid);
	}
	
	
	private void resignPiece(int x, int y)
	{
		resignedPieceLoc[0] = x;
		resignedPieceLoc[1] = y;
		
		resignedPieceType = board[x][y][5];
		
		if(x == rotatedPieceLoc[0] && y == rotatedPieceLoc[1])
		{
			rotatedPieceLoc[0] = -1;
			rotatedPieceLoc[1] = -1;
		}

		if(moveTarget[0] == x && moveTarget[1] == y)
		{
			statusConsole.appendText("cannot resign a moved piece!\n");
			return;
		}
		
		if(hasResignedPiece)
		{
			statusConsole.appendText("already resigned a piece during this turn!\n");
			return;
		}
		if(board[x][y][1] == 9)
		{
			statusConsole.appendText("cannot resign a Queen!\n");
			return;
		}
		if(board[x][y][1] == 8)
		{
			statusConsole.appendText("cannot resign your General!\n");
			return;
		}
		
		hasResignedPiece = true;
		
		if(turn.get() == 1)
		{
			p1counters.set(p1counters.get() + PieceData.pieceValue(board[x][y][1]));
		}
		else if(turn.get() == 2)
		{
			p2counters.set(p2counters.get() + PieceData.pieceValue(board[x][y][1]));
		}
		board[x][y] = null;

		redraw(grid);
	}
	
	/*
	 * handle printing move data into console after turn changes
	 */
	
	private void setTurn(int sX, int sY, int tX, int tY, int markX, int markY, int attSX, int attSY, int attTX, int attTY, int defSX,
			int defSY, int defTX, int defTY, int qmX, int qmY, int resP, int resPX, int resPY, int rotPX, int rotPY, int rotAngle, int qmlvl, int gpx, int gpy) {
		
		double turnNo;


		move = new int[] {sX,sY,tX,tY,markX,markY,turn.get(),attSX,attSY,attTX,attTY,defSX,defSY,defTX,defTY,qmX,qmY,resP,
				resPX,resPY,rotPX,rotPY,rotAngle,qmlvl,gpx,gpy};
		
		plies++;
		turnNo = plies/2.0;
		turn.set((turn.get() % 2)+1);
		

		if(tX != -1) {
			System.out.println("sX "+sX+" sY "+sY+" tX "+tX+" tY "+tY);
			sb.append(pieceType((char)board[tX][tY][5])+" to "+charConv(tY)+(tX+1)+"\n");
			//sb.append(" - "+(char)board[tX][tY][5]+charConv(sY)+(sX+1)+" -> "+charConv(tY)+(tX+1)+"\n");
		}
		if(markX != -1) {
			if(board[markX][markY][8] > 0)
			{
				sb.append("mark upgraded at "+charConv(markY)+(markX+1)+"\n");
			}
			else
			{
				sb.append("mark placed at "+charConv(markY)+(markX+1)+"\n");
			}
			
		}
		if(attTX != -1) {
			sb.append("attacked "+charConv(attTY)+(attTX+1)+" by "+pieceType((char)board[attSX][attSY][5])+" at "+charConv(attSY)+(attSX+1)+"\n");
		}
		if(defTX != -1) {
			sb.append("defended "+charConv(defTY)+(defTX+1)+" by "+pieceType((char)board[defSX][defSY][5])+" at "+charConv(defSY)+(defSX+1)+"\n");
		}
		if(qmX != -1)
		{
			sb.append("placed Queens Mark at "+charConv(qmY)+(qmX+1)+"\n");
		}
		if(resP != 0){
			sb.append("resigned "+pieceType((char)resP)+" previously at "+charConv(resPY)+(resPX + 1)+"\n");
		}
		if(rotPX != -1)
		{
			sb.append("rotated "+pieceType((char)board[rotPX][rotPY][5])+" at "+charConv(rotPY)+(rotPX+1)+" by "+(board[rotPX][rotPY][6]-rotAngle)+"\n");
		}
		if(gpx != -1)
		{
			sb.append("promoted new General at "+charConv(gpy)+(gpx+1)+"\n");
		}
		if(tX == -1 && markX == -1 && attTX == -1 && defTX == -1 && qmX == -1 && resP == 0 && gpx == -1 && rotPX == -1)
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

		check:
		for(int i = 0; i<11; i++)
		{
			for(int j = 0; j<11; j++)
			{
				if(board[j][i] != null && board[j][i][0] < 0 && board[j][i][7] == 5 && board[j][i][8] <= plies - 2)
				{
					board[j][i] = new int[] {pieceIdCap,queen,turn.get(),j,i,113,0,PieceData.pieceHP(9)};
					pieceIdCap++;
					System.out.println("setting to true on turn: "+turn.get());
					redraw(grid);
					break check;
				}
			}
		}

		boardList.add(board);
		savePos(board);
		
	}
	
	private String pieceType(char type)
	{
		type = Character.toLowerCase(type);
		
		switch(type)
		{
		case 'p':
			return "Soldier";
		case 'f':
			return "Swordsman";
		case 'v':
			return "Vanguard";
		case 'h':
			return "Scythe";
		case 'r':
			return "Prince";
		case 'g':
			return "Guardian";
		case 's':
			return "Spearman";
		case 'k':
			return "General";
		case 'q':
			return "Queen";
		default:
			return null;
		}
	}
	
	/*
	 * form necessary string for a piece or mark to get the corresponding image from resources/
	 */
	
	private String squareContent(int x, int y) {
		int[] element = board[x][y];

		
		if(element == null || x == 11 || y == 11) {
			return null;
		}

		char subType = 0;
		String type = "piece";
		
		//piece_qm_b
		if(element[0] < 0)
		{
			type = "mark";
			switch(element[7]) {
			case 1:
				subType = 'a';
				break;
			case 5:
				subType = 'q';
				break;
			}
		}
		else
		{
			subType = (char)element[5];
		}
		
	
		return type + "_" + subType + '_' + (element[2] == 1 ? 'b' : 'r');

	}
	
	
	/*
	 * coordinate transform from [0-10] to [a-k]
	 */
	
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
	
	
	/*
	 * iterate whole board and fill legal moves and legal range arrays, depending on when matrix,matrix2 returned from RuleSet
	 * contain '1' at current square
	 */
	
	private void iterateBoard(int i,int j) {

		int[][] matrix = RuleSet.validateMove(board[i][j][2], board[i][j][1]);
		int[][] matrix2 = RuleSet.validateRange( board[i][j][2], board[i][j][1]);
		
		movementRange = new ArrayList<int[]>();
		placementRange = new ArrayList<int[]>();
		
		if(board[i][j][1] != 7)
		{
			//apply rotations
			if(board[i][j][6] == 90)
			{
				matrix = RuleSet.rotateMatrix(matrix, 1);
				matrix2 = RuleSet.rotateMatrix(matrix2, 1);
			}
			else if(board[i][j][6] == -90)
			{
				matrix = RuleSet.rotateMatrix(matrix, -1);
				matrix2 = RuleSet.rotateMatrix(matrix2, -1);
			}
			else if(board[i][j][6] == 180)
			{
				matrix = RuleSet.rotateMatrix(RuleSet.rotateMatrix(matrix, 1), 1);
				matrix2 = RuleSet.rotateMatrix(RuleSet.rotateMatrix(matrix2, 1), 1);
			}
		}
		
		else {
			ArrayList<int[]> set = RuleSet.iterateRange(i, j, board, true, true);
			for (int h = 0; h < set.size();h++) {
				placementRange.add(set.get(h));
			}
		}
		
		for(int x = 0; x<11; x++) {
			for(int y = 0; y<11; y++) {
				try {
					//pieces use a matrix from RuleSet class, in which elements '1' mark the legal squares in relation to current piece

					if(board[i][j][1] != 7 && matrix2[3-(i-x)][3-(j-y)] == 1) 
					{
						placementRange.add(new int[] {x,y});
					}		

					//test for legality of the target square
					
					if(matrix[3-(i-x)][3-(j-y)] == 1 && board[x][y] == null) 
					{
						movementRange.add(new int[] {x,y});
					}
				} 
				catch (ArrayIndexOutOfBoundsException e2) 
				{
					
				}
			}
		}

		
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
		//System.out.println("changed"+list.size());
		if(movementRange != null) {
			legalMovesObs.addAll(movementRange);
		}
		
	}
	
	
	/*
	 * get starting position
	 */
	
	private GridPane initBoard(GridPane grid) {
		
		//id, type, color, x, y, char, angle, hp of each piece

		board[0][0] = new int[] {1,swordsman,2,0,0,102, 0, PieceData.pieceHP(2)};
		board[0][1] = new int[] {2,vanguard,2,0,1,118, 0, PieceData.pieceHP(3)};
		board[0][2] = new int[] {3,spearman,2,0,2,115, 0, PieceData.pieceHP(7)};
		board[0][3] = new int[] {4,prince,2,0,3,114,0, PieceData.pieceHP(5)};
		board[0][4] = new int[] {5,guardian,2,0,4,103, 0, PieceData.pieceHP(6)};
		board[0][5] = new int[] {6,general,2,0,5,107, 0, PieceData.pieceHP(8)};
		board[0][6] = new int[] {7,guardian,2,0,6,103, 0,PieceData.pieceHP(6)};
		board[0][7] = new int[] {8,prince,2,0,7,114, 0,PieceData.pieceHP(5)};
		board[0][8] = new int[] {9,spearman,2,0,8,115, 0,PieceData.pieceHP(7)};
		board[0][9] = new int[] {10,vanguard,2,0,9,118, 0,PieceData.pieceHP(3)};
		board[0][10] = new int[] {11,swordsman,2,0,10,102, 0,PieceData.pieceHP(2)};
		board[0][11] = null;
		
		board[10][0] = new int[] {34,swordsman,1,10,0,102,0,PieceData.pieceHP(2)};
		board[10][1] = new int[] {35,vanguard,1,10,1,118,0,PieceData.pieceHP(3)};
		board[10][2] = new int[] {36,spearman,1,10,2,115,0,PieceData.pieceHP(7)};
		board[10][3] = new int[] {37,prince,1,10,3,114,0,PieceData.pieceHP(5)};
		board[10][4] = new int[] {38,guardian,1,10,4,103,0,PieceData.pieceHP(6)};
		board[10][5] = new int[] {39,general,1,10,5,107,0,PieceData.pieceHP(8)};
		board[10][6] = new int[] {40,guardian,1,10,6,103,0,PieceData.pieceHP(6)};
		board[10][7] = new int[] {41,prince,1,10,7,114,0,PieceData.pieceHP(5)};
		board[10][8] = new int[] {42,spearman,1,10,8,115,0,PieceData.pieceHP(7)};
		board[10][9] = new int[] {43,vanguard,1,10,9,118,0,PieceData.pieceHP(3)};
		board[10][10] = new int[]{44,swordsman,1,10,10,102,0,PieceData.pieceHP(2)};
		board[11][11] = null;
		
		
		
		for(int i = 0; i<11; i++) {
			if(i == 3 || i == 7) {
				board[1][i] = new int[] {12+i,scythe,2,1,i,104,0,PieceData.pieceHP(4)};
				board[9][i] = new int[] {23+i,scythe,1,9,i,104,0,PieceData.pieceHP(4)};
				continue;
			}
			
			board[1][i] = new int[] {12+i,soldier,2,1,i,112,0,PieceData.pieceHP(1)};
			board[9][i] = new int[] {23+i,soldier,1,9,i,112,0,PieceData.pieceHP(1)};
			
		}
		
		for(int i = 0; i<11; i++) {
			for(int j = 2; j<9; j++) {
				board[j][i] = null;
			}
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
		for(int i = 0; i<11; i++) {
			for(int j = 0; j<11; j++) {
				board[j][i] = null;
			}
		}
	}
	
	
	/*
	 * Copies all elements from b1 into b2
	 */
	
	public void arrayCopy(int[][][] b1, int[][][] b2) {
		for(int i = 0; i < 12;i++) {
			for(int j = 0;j < 12;j++) {
				
				if(b1[i][j] != null)
				{
						
					b2[i][j] = new int[b1[i][j].length];
					for(int k = 0; k < b1[i][j].length; k++)
					{
						b2[i][j][k] = b1[i][j][k];
					}
				}
				else
				{
					b2[i][j] = null;
				}
				
				

			}
		}

	}
	


	public static void main(String[] args) {
		launch(args);

	}

}
