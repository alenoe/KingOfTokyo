package ch.fhnw.itprojekt.noobsquad.client.board;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeMap;
import ch.fhnw.itprojekt.noobsquad.abstractClasses.View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ch.fhnw.itprojekt.noobsquad.client.supportClasses.Translator;
import ch.fhnw.itprojekt.noobsquad.client.supportClasses.ServiceLocator;

/**
 *
 * @author Alexander Noever
 * 
 */
public class Board_View extends View<Board_Model>{
	
	Menu menuFile;
    Menu menuFileLanguage;
    Menu menuHelp;

	Button btnDice1;
	Button btnDice2;
	Button btnDice3;
	Button btnDice4;
	Button btnDice5;
	Button btnDice6;
	Button btnRoll;
	Button btnLeaveTokyo;
	Button btnNewConnection;
	
	ImageView imgVPlayer1Monster;
	Label lblPlayer1HealthPoints;
	Label lblPlayer1Name;
	Label lblPlayer1VictoryPoints;
	Label lblPlayer1TokyoStatus;
	
	ImageView imgVPlayer2Monster;
	Label lblPlayer2HealthPoints;
	Label lblPlayer2Name;
	Label lblPlayer2VictoryPoints;
	Label lblPlayer2TokyoStatus;
	Label lblGameEnd;
	Label lblNewConnection;
	
	Alert aGameEnd;
	Button btnClose;
	HBox hboxNewCon;
	
	TextArea taChat;
	TextField tfChat;
	Button btnSend;
	ArrayList<Image> player1Pictures;
	ArrayList<Image> player2Pictures;
	TreeMap<Integer, Image> dicePictures;
	TreeMap<Integer, Image> dicePicturesOk;
	ServiceLocator serviceLocator;
	Translator t;
		
	
	/**
	 * Die Board_View Klasse erbt von der abstrakten Klasse View und stellt damit die visuelle Komponente des Boards dar.
	 * Diese View beinhaltet den Construktor, eine create_GUI methode und eine updateText methode.
	 * Der Constructor und die create_GUI methode entstammen der abstrakten Klasse View - geschrieben von Bradley Richards 
	 * Die updateText methode wird zur Uebersetzung der GuiElemente verwendet und ist an ein Beispiel aus Bradley Richards Vorlesung
	 * angelehnt.
	 * @param stage
	 * @param model
	 */
    public Board_View(Stage stage, Board_Model model) {
        super(stage, model);
        stage.setTitle("KingOfTokyo byNoobsquad - Game");
        this.model = model;
        
        serviceLocator = ServiceLocator.getServiceLocator();
        ServiceLocator.getServiceLocator().getLogger().info("KingOfTokyo view initialized");
        
        // Initialisierung der Affenbilder zur Darstellung auf dem Board
        player1Pictures = new ArrayList<Image>();
        player1Pictures.add((new Image("ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"RiApe_brown_angry.png")));
        player1Pictures.add((new Image("ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"RiApe_brown_King.png")));
        player1Pictures.add((new Image("ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"RiApe_brown_sad.png")));
        
        player2Pictures = new ArrayList<Image>();
        player2Pictures.add(new Image("ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"LeApe_purple_angry.png"));
        player2Pictures.add(new Image("ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"LeApe_purple_King.png"));
        player2Pictures.add(new Image("ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"LeApe_purple_sad.png"));
    }

    
    /**
     * Das GUI besteht aus zwei wesentlichen Komponenten, einer GridPane namens playArea
     * und einer VBox chatArea, beide sind in eine BorderPane eingebettet.
     */
    @Override
    protected Scene create_GUI() {
    	
    	// ServiceLocator und Translator werden gesetzt.
    	ServiceLocator sl = ServiceLocator.getServiceLocator();  
	    t = sl.getTranslator();
	    
	    // Die MenuBar enthält die Sprachauswahl
	    MenuBar menuBar = new MenuBar();
	    menuFile = new Menu(t.getString("program.menu.file"));
	    menuFileLanguage = new Menu(t.getString("program.menu.file.language"));
	    menuFile.getItems().add(menuFileLanguage);
	    
       for (Locale locale : sl.getLocales()) {
           MenuItem language = new MenuItem(locale.getLanguage());
           menuFileLanguage.getItems().add(language);
           language.setOnAction( event -> {
                   sl.setTranslator(new Translator(locale.getLanguage()));
                   updateTexts();
            });
        }
    	
        BorderPane root = new BorderPane();
        root.setId("KingOfTokyoMap");
       
        
        // Menu wird der BorderPane hinzugefuegt
        menuHelp = new Menu(t.getString("program.menu.help"));
        menuBar.getMenus().addAll(menuFile, menuHelp);
        root.setTop(menuBar);
        
        
/**______________________________________________________________________________________________
 *							START OF GRIDPANE !!! PLAYAREA !!!
 */
        
        
        // playArea der BorderPane hinzufuegen.
        GridPane playArea = new GridPane();
        root.setCenter(playArea);
        Insets is = new Insets(5);
        
        
        // Player1 VBox GuiElemente
        VBox leftPlayer = new VBox();
        playArea.add(leftPlayer, 0, 0);
        leftPlayer.setAlignment(Pos.CENTER);
        leftPlayer.setPrefWidth(290);
        leftPlayer.setPrefHeight(259);
        GridPane.setMargin(leftPlayer, is);
        
        
        // Player1 ImageView Monster
        imgVPlayer1Monster = new ImageView(new Image("ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"RiApe_brown_angry.png"));
        imgVPlayer1Monster.setFitWidth(220);//250
        imgVPlayer1Monster.setFitHeight(358);//175
        imgVPlayer1Monster.pickOnBoundsProperty().set(true);
        imgVPlayer1Monster.preserveRatioProperty().set(true);
        imgVPlayer1Monster.setId("imgVPplayer1Monster");
        leftPlayer.getChildren().add(imgVPlayer1Monster);
        
        
        // Player1 LifePoints HBox
        HBox hboxPlayer1HealthPoints = new HBox();
        hboxPlayer1HealthPoints.setPrefWidth(200); 
        hboxPlayer1HealthPoints.setPrefHeight(100);
        hboxPlayer1HealthPoints.setAlignment(Pos.CENTER_LEFT);
        leftPlayer.getChildren().add(hboxPlayer1HealthPoints);
        hboxPlayer1HealthPoints.setStyle("-fx-background-color: #FFFFFF");

        // Player1 heart Icon
        ImageView heartPlayer1 = new ImageView(new Image("ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"heart.png"));
        heartPlayer1.setFitWidth(100);
        heartPlayer1.setFitHeight(35);
        heartPlayer1.pickOnBoundsProperty().set(true);
        heartPlayer1.preserveRatioProperty().set(true);
        hboxPlayer1HealthPoints.getChildren().add(heartPlayer1);
        HBox.setMargin(heartPlayer1, new Insets(0,10,0,0));
        
        // Player1 LifePoints Label
        lblPlayer1HealthPoints = new Label();
        lblPlayer1HealthPoints.setId("lblPlayer1HealthPoints");
        lblPlayer1HealthPoints.setText("10");
        hboxPlayer1HealthPoints.getChildren().add(lblPlayer1HealthPoints);
        HBox.setMargin(lblPlayer1HealthPoints, new Insets(0,10,0,0));
        
        // Player1 UserName Label
        lblPlayer1Name = new Label();
        lblPlayer1Name.setId("lblPlayer1Name");
        lblPlayer1Name.setText("Player 1");
        hboxPlayer1HealthPoints.getChildren().add(lblPlayer1Name);
        
        
        //Player1 VictoryPoints HBox
        HBox hboxPlayer1VictoryPoints = new HBox();
        hboxPlayer1VictoryPoints.setPrefWidth(200); 
        hboxPlayer1VictoryPoints.setPrefHeight(100); 
        hboxPlayer1VictoryPoints.setAlignment(Pos.CENTER_LEFT);
        leftPlayer.getChildren().add(hboxPlayer1VictoryPoints);
        hboxPlayer1VictoryPoints.setStyle("-fx-background-color: #FFFFFF");
        
        // Player1 VictoryPoints Icon
        ImageView starPlayer1 = new ImageView(new Image("ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"blue-star-hi.png"));
        starPlayer1.setFitWidth(100);
        starPlayer1.setFitHeight(35);
        starPlayer1.pickOnBoundsProperty().set(true);
        starPlayer1.preserveRatioProperty().set(true);
        hboxPlayer1VictoryPoints.getChildren().add(starPlayer1);
        HBox.setMargin(starPlayer1, new Insets(0,10,0,0));
        
        // Player1 VictoryPoints Label
        lblPlayer1VictoryPoints = new Label();
        lblPlayer1VictoryPoints.setId("lblPlayer1VictoryPoints");
        lblPlayer1VictoryPoints.setText("0");
        hboxPlayer1VictoryPoints.getChildren().add(lblPlayer1VictoryPoints);
        HBox.setMargin(lblPlayer1VictoryPoints, new Insets(0,10,0,0));
        
        // Player1 TokyoStatus Label
        lblPlayer1TokyoStatus = new Label();
        lblPlayer1TokyoStatus.setId("lblPlayer1TokyoStatus");
        lblPlayer1TokyoStatus.setText(t.getString("label.inTokyo.out"));
        hboxPlayer1VictoryPoints.getChildren().add(lblPlayer1TokyoStatus);
        
        
//--------------------------------------------------------------------------------------------------
        
        
        //Wuerfelflaeche Gui Elemente der playArea hinzufuegen
        GridPane dicePane = new GridPane();
        playArea.add(dicePane, 1, 0);
        dicePane.setAlignment(Pos.CENTER);
        dicePane.setPrefSize(372, 203);
        playArea.setPadding(is);
        
        btnDice1 = new Button();
        btnDice1.setPrefWidth(84);
        btnDice1.setPrefHeight(75);
        btnDice1.setId("btnDice1");
        dicePane.add(btnDice1, 0, 0);
        GridPane.setMargin(btnDice1, new Insets(5));
        
        btnDice4 = new Button();
        btnDice4.setPrefWidth(84);
        btnDice4.setPrefHeight(75);
        btnDice4.setId("btnDice4");
        dicePane.add(btnDice4, 0, 1);
        GridPane.setMargin(btnDice4, new Insets(5));
        
        btnDice2 = new Button();
        btnDice2.setPrefWidth(84);
        btnDice2.setPrefHeight(75);
        btnDice2.setId("btnDice2");
        dicePane.add(btnDice2, 1, 0);
        GridPane.setMargin(btnDice2, new Insets(5));
        
        btnDice5 = new Button();
        btnDice5.setPrefWidth(84);
        btnDice5.setPrefHeight(75);
        btnDice5.setId("btnDice5");
        dicePane.add(btnDice5, 1, 1);
        GridPane.setMargin(btnDice5, new Insets(5));
        
        btnDice3 = new Button();
        btnDice3.setPrefWidth(84);
        btnDice3.setPrefHeight(75);
        btnDice3.setId("btnDice3");
        dicePane.add(btnDice3, 2, 0);
        GridPane.setMargin(btnDice3, new Insets(5));
        
        btnDice6 = new Button();
        btnDice6.setPrefWidth(84);
        btnDice6.setPrefHeight(75);
        btnDice6.setId("btnDice6");
        dicePane.add(btnDice6, 2, 1);
        GridPane.setMargin(btnDice6, new Insets(5));
        
        
        // Button roll unterhalb der Wuerfel
        VBox vboxBtnRoll = new VBox();
        dicePane.add(vboxBtnRoll, 1, 2);
        vboxBtnRoll.setPrefSize(81, 60);
        vboxBtnRoll.setAlignment(Pos.CENTER);

        btnRoll = new Button();
        btnRoll.setPrefWidth(131);
        btnRoll.setPrefHeight(26);
        btnRoll.setId("btnRoll");
        btnRoll.setText(t.getString("button.roll"));
        vboxBtnRoll.getChildren().add(btnRoll);
        VBox.setMargin(btnRoll, is);
        
        
//--------------------------------------------------------------------------------------------------
        
        
        // Player2 VBox GuiElemente
        VBox rightPlayer = new VBox();
        playArea.add(rightPlayer, 2, 0);
        rightPlayer.setAlignment(Pos.CENTER);
        rightPlayer.setPrefWidth(290);
        rightPlayer.setPrefHeight(259);
        GridPane.setMargin(rightPlayer, is);
        
        
        // Player2 ImageView Monster
        imgVPlayer2Monster = new ImageView(new Image("ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"LeApe_purple_angry.png"));
        imgVPlayer2Monster.setFitWidth(220);
        imgVPlayer2Monster.setFitHeight(358);
        imgVPlayer2Monster.setId("imgVPlayer2Monster");
        imgVPlayer2Monster.pickOnBoundsProperty().set(true);
        imgVPlayer2Monster.preserveRatioProperty().set(true);
        rightPlayer.getChildren().add(imgVPlayer2Monster);
        
        
        // Player2 Healthpoints HBox
        HBox hboxPlayer2HealthPoints = new HBox();
        hboxPlayer2HealthPoints.setPrefWidth(200);
        hboxPlayer2HealthPoints.setPrefHeight(100);
        hboxPlayer2HealthPoints.setAlignment(Pos.CENTER_RIGHT);
        rightPlayer.getChildren().add(hboxPlayer2HealthPoints);
        hboxPlayer2HealthPoints.setStyle("-fx-background-color: #FFFFFF");
        
        // Player2 UserName Label
        lblPlayer2Name = new Label();
        lblPlayer2Name.setId("lblPlayer2Name");
        lblPlayer2Name.setText("Player 2");
        hboxPlayer2HealthPoints.getChildren().add(lblPlayer2Name);
        
        // Player2 LifePoints Label
        lblPlayer2HealthPoints = new Label();
        lblPlayer2HealthPoints.setId("lblPlayer2HealthPoints");
        lblPlayer2HealthPoints.setText("10");
        hboxPlayer2HealthPoints.getChildren().add(lblPlayer2HealthPoints);
        HBox.setMargin(lblPlayer2HealthPoints, new Insets(0,0,0,10));
        
        // Player2 LifePoints Icon
        ImageView heartPlayer2 = new ImageView(new Image("ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"heart.png"));
        heartPlayer2.setFitWidth(100);
        heartPlayer2.setFitHeight(35);
        heartPlayer2.pickOnBoundsProperty().set(true);
        heartPlayer2.preserveRatioProperty().set(true);
        hboxPlayer2HealthPoints.getChildren().add(heartPlayer2);
        HBox.setMargin(heartPlayer2, new Insets(0,0,0,10));
        
        
        //Player2 VictoryPoints HBox
        HBox hboxPlayer2VictoryPoints = new HBox();
        hboxPlayer2VictoryPoints.setPrefWidth(200);
        hboxPlayer2VictoryPoints.setPrefHeight(100);
        hboxPlayer2VictoryPoints.setAlignment(Pos.CENTER_RIGHT);
        rightPlayer.getChildren().add(hboxPlayer2VictoryPoints);
        hboxPlayer2VictoryPoints.setStyle("-fx-background-color: #FFFFFF");
        
        // Player2 TokyoStatus Label
		lblPlayer2TokyoStatus = new Label();
		lblPlayer2TokyoStatus.setId("lblPlayer2TokyoStatus");
		lblPlayer2TokyoStatus.setText(t.getString("label.inTokyo.out"));
		hboxPlayer2VictoryPoints.getChildren().add(lblPlayer2TokyoStatus);
        
		// Player2 VictoryPoints Label
        lblPlayer2VictoryPoints = new Label();
        lblPlayer2VictoryPoints.setId("lblPlayer2VictoryPoints");
        lblPlayer2VictoryPoints.setText("0");
        hboxPlayer2VictoryPoints.getChildren().add(lblPlayer2VictoryPoints);
        HBox.setMargin(lblPlayer2VictoryPoints, new Insets(0,0,0,10));
        
        // Player2 VictoryPoints Icon
        ImageView starPlayer2 = new ImageView(new Image("ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"blue-star-hi.png"));
        starPlayer2.setFitWidth(100);
        starPlayer2.setFitHeight(35);
        starPlayer2.pickOnBoundsProperty().set(true);
        starPlayer2.preserveRatioProperty().set(true);
        hboxPlayer2VictoryPoints.getChildren().add(starPlayer2);
		HBox.setMargin(starPlayer2, new Insets(0,0,0,10));
        
      
        // Button Tokyo verlassen
		HBox hboxLeaveTokyo = new HBox();
		hboxLeaveTokyo.setAlignment(Pos.CENTER);
		playArea.add(hboxLeaveTokyo, 1, 1);
		btnLeaveTokyo = new Button();
		
		btnLeaveTokyo.setId("btnLeaveTokyo");
		btnLeaveTokyo.setText(t.getString("button.leaveTokyo"));
		btnLeaveTokyo.setAlignment(Pos.CENTER);
		hboxLeaveTokyo.getChildren().add(btnLeaveTokyo);
		

		//--------------------------------------------------------------------------------------------------
		
		
		//Winner|Loserlabel
		VBox vboxGameEnd = new VBox();
		vboxGameEnd.setPrefSize(100, 200);
		vboxGameEnd.setAlignment(Pos.CENTER);
		playArea.add(vboxGameEnd, 1, 2);
		
		lblGameEnd = new Label();
		lblGameEnd.setPrefSize(274, 67);
		lblGameEnd.setAlignment(Pos.CENTER);
		lblGameEnd.setFont(Font.font ("Verdana", 22));
		lblGameEnd.setTextFill(Color.web("#9e1f1f"));
		lblGameEnd.setStyle("-fx-font-weight: bold;");
		vboxGameEnd.getChildren().add(lblGameEnd);
		VBox.setMargin(lblGameEnd, is);
		
		
		

//								END OF GRIDPANE !!! PLAYAREA !!!
//_________________________________________________________________________________________________________
//		
//								START OF VBOX !!!CHAT AREA !!!
        
        //ChatArea Gui Elemente hinzufuegen.
        VBox chatArea = new VBox();
        root.setRight(chatArea);
        chatArea.setAlignment(Pos.CENTER);
        chatArea.setPrefWidth(242);
        chatArea.setPrefHeight(658);
        
        
        VBox chatView = new VBox();
        chatArea.getChildren().add(chatView);
        chatView.setPrefSize(242, 422);
        
        
        // Chat TextArea zeigt die gesendeten und erhaltenen Chat Nachrichten an
        taChat = new TextArea();
        taChat.setId("taChat");
        taChat.setPrefSize(232, 410);
        taChat.setEditable(false);
        chatView.getChildren().add(taChat);
        VBox.setMargin(taChat, new Insets(5));
        
        // Chat Text Field nimmt die Eingabe des Spielers auf
        tfChat = new TextField();
        tfChat.setId("tfChat");
        tfChat.setPromptText(t.getString("textfield.chat"));
        chatArea.getChildren().add(tfChat);
        VBox.setMargin(tfChat, new Insets(5));
        
        // Chat Button send löst den Eventhandler im Controller aus.
        btnSend = new Button();
        btnSend.setText(t.getString("button.send"));
        chatArea.getChildren().add(btnSend);
        VBox.setMargin(btnSend, new Insets(0,0,0,5));
        
        
        //Button newConnection
        hboxNewCon = new HBox();
        hboxNewCon.setAlignment(Pos.CENTER);
        hboxNewCon.setId("hboxNewCon");
        root.setBottom(hboxNewCon);
        
        
//		END OF VBOX !!!CHAT AREA !!!
//_________________________________________________________________________________________________________
//
//		START OF  !!!NEW CONNECTION!!!
        
        
        // Button löst Eventhandler im Controller aus
        /**
         *  Per default sind der Button und das Label invisible und werden nur angezeigt, wenn
         *  der Client die Verbindung zum Server verliert.
         */
		btnNewConnection = new Button();
		btnNewConnection.setId("btnNewConnection");
		btnNewConnection.setText(t.getString("button.newconnection"));
		btnNewConnection.setStyle("-fx-font-size: 15px");
		HBox.setMargin(btnNewConnection, new Insets(10,10,10,10));
		hboxNewCon.getChildren().add(btnNewConnection);
		btnNewConnection.setVisible(false);
        
		lblNewConnection = new Label();
		lblNewConnection.setId("lblNewConnection");
		lblNewConnection.setText(t.getString("label.newconnection"));
		HBox.setMargin(lblNewConnection, new Insets(10,10,10,10));
		hboxNewCon.getChildren().add(lblNewConnection);
		lblNewConnection.setVisible(false);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
        
        /**
         * Der Alert wird angezeigt sobald ein Spieler gewonnen, bzw. verloren hat.
         * Es wird ihm sein Monster in der jeweiligen Stimmung angezeigt und ein Button,
         * um das Spiel zu verlassen.
         */
        //Alert Dialog Pane zur Darstellung des Gewinners.
        aGameEnd = new Alert(AlertType.CONFIRMATION);
        aGameEnd.getButtonTypes().clear();
        ButtonType close = new ButtonType(t.getString("button.close"));
        aGameEnd.getButtonTypes().addAll(close);
        aGameEnd.setHeaderText(null);
    	aGameEnd.setContentText(t.getString("dialog.gameend.content"));
        btnClose = (Button) aGameEnd.getDialogPane().lookupButton(close);
        btnClose.setId("btnClose");
        
        return scene;
    }
    
    /**
     * Diese Methode wird aufgerufen, wenn die Sprachauswahl geaendert wird.
     * Sie geht über alle GUI Elemente und holt den entsprechenden String aus dem 
     * Eigenschaften File.
     */
    protected void updateTexts() {
	    Translator t = ServiceLocator.getServiceLocator().getTranslator();
	    
	    // Dialog Pane
	    btnClose.setText(t.getString("button.close"));
	    aGameEnd.setContentText(t.getString("dialog.gameend.content"));
	        
	    // The menu entries
	    menuFile.setText(t.getString("program.menu.file"));
	    menuFileLanguage.setText(t.getString("program.menu.file.language"));
        menuHelp.setText(t.getString("program.menu.help"));
	        
	    // Buttons
        btnRoll.setText(t.getString("button.roll"));
        btnLeaveTokyo.setText(t.getString("button.leaveTokyo"));
        btnSend.setText(t.getString("button.send"));
        btnNewConnection.setText(t.getString("button.newconnection"));
        
	    // TextField Promt
        tfChat.setPromptText(t.getString("textfield.chat"));
        
        // Labels
        lblNewConnection.setText(t.getString("label.newconnection"));
        int state = model.getGameState();
        switch (state){
        case 0:
        	lblGameEnd.setText("");
        	break;
        case 1:
            lblGameEnd.setText(t.getString("label.gameend.lose"));
            break;
        case 2:
            lblGameEnd.setText(t.getString("label.gameend.lose"));
            break;
        case 3:
            lblGameEnd.setText(t.getString("label.gameend.win"));
            break;
        case 4:
            lblGameEnd.setText(t.getString("label.gameend.win"));
            break;
        }
        if(!model.getPlayer1().getInTokyo()){
        	lblPlayer1TokyoStatus.setText(t.getString("label.tokyo.out"));
        } else {
        	lblPlayer1TokyoStatus.setText(t.getString("label.tokyo.in"));
        }
        if(!model.getPlayer2().getInTokyo()){
        	lblPlayer2TokyoStatus.setText(t.getString("label.tokyo.out"));
        } else {
        	lblPlayer2TokyoStatus.setText(t.getString("label.tokyo.in"));
        }     
    }
}