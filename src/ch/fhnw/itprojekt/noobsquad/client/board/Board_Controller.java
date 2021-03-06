package ch.fhnw.itprojekt.noobsquad.client.board;



/**
 * 
 * @author Heiko Meyer
 * Der Controller legt fest, welche View zur Anwendung kommt. Ausserdem aktualisiert 
 * er mithilfe der Update-Methode die View, wenn er über Änderungen informiert wird.
 */

import java.util.ArrayList;

import ch.fhnw.itprojekt.noobsquad.client.interfaces.Observer;
import ch.fhnw.itprojekt.noobsquad.client.interfaces.Subject;
import ch.fhnw.itprojekt.noobsquad.client.main.JavaFX_App_Template;
import ch.fhnw.itprojekt.noobsquad.client.supportClasses.ServiceLocator;
import ch.fhnw.itprojekt.noobsquad.abstractClasses.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class Board_Controller extends Controller<Board_Model, Board_View>implements Observer {

	private ServiceLocator serviceLocator;
	private ArrayList<Button> dicebtnList;
	private Subject subject;

	public Board_Controller(JavaFX_App_Template main, Board_Model model, Board_View view) {
		super(model, view);

		this.subject = model;
		model.register(this); // Observer wird beim Subject (hier Model) registriert
		serviceLocator = ServiceLocator.getServiceLocator();

		dicebtnList = new ArrayList<Button>();
		dicebtnList.add(view.btnDice1);
		dicebtnList.add(view.btnDice2);
		dicebtnList.add(view.btnDice3);
		dicebtnList.add(view.btnDice4);
		dicebtnList.add(view.btnDice5);
		dicebtnList.add(view.btnDice6);

		for (Button btn : dicebtnList) {
			btn.setDisable(true);
		}

		view.btnRoll.setDisable(true);
		view.btnLeaveTokyo.setDisable(true);
		view.btnSend.setDisable(true);

		// Button, um zu würfeln bis 3 Würfe getätigt wurden. 
		view.btnRoll.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				boolean btnRollState = model.roll();

				Platform.runLater(() -> {
					view.btnRoll.setDisable(btnRollState);
				});
			}
		});

		// Button, um Tokyo zu verlassen
		view.btnLeaveTokyo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				model.playerLeavesTokyo();

				Platform.runLater(() -> {
					view.btnLeaveTokyo.setDisable(true);
				});
			}
		});

		/**
		 * Diejenigen Buttons, welche man behalten will, werden geblockt.
		**/
		
		view.btnDice1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				model.diceBtnLockUnlock(0);

			}
		});

		view.btnDice2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				model.diceBtnLockUnlock(1);

			}
		});
		view.btnDice3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				model.diceBtnLockUnlock(2);

			}
		});
		view.btnDice4.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				model.diceBtnLockUnlock(3);

			}
		});
		view.btnDice5.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				model.diceBtnLockUnlock(4);

			}
		});
		view.btnDice6.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				model.diceBtnLockUnlock(5);

			}
		});

		// Versenden von Chatnachrichten
		view.btnSend.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				model.sendChatMessage(view.tfChat.getText());

				Platform.runLater(() -> {
					view.tfChat.setText("");
					;
				});
			}
		});

		// Login wird neu gestartet, Board wird geschlossen.
		view.btnNewConnection.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				main.restart();
			}
		});

		//	schliesst den JavaFx-Thread
		view.btnClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				model.endGame();
				Platform.exit();
			}
		});

		
		// 	Wenn keine Verbindung zum Server besteht, wird enableRestart aufgerufen,
		//	damit der Button und das Label für eine neue Verbindung erscheint
		
		if (!model.getServerConnection().getServerconnection()) {
			enableRestart();
		}

		serviceLocator.getLogger().info("Board controller initialized");
	}
    
    //    
    // Die View zeigt den Button und das Label zur Wiederherstellung der 
    // Verbindung an.
    //  
	public void enableRestart() {
		view.btnNewConnection.setVisible(true);
		view.lblNewConnection.setVisible(true);
		view.hboxNewCon.setStyle("-fx-background-color: #FFFFFF");
	}

	// setzt das Subjekt für den Observer
	@Override
	public void setSubject(Subject sub) {
		this.subject = sub;

	}

	//    
	// Die Update-Methode holt sich die Message des Subjekts und prüft ihren Inhalt.
	// Je nachdem, welcher String ausgewertet wird,führt der Controller andere Operationen durch.
	//  
	@Override
	public void update() {

		// Die Update-Methode holt sich die Nachricht vom Subject und prüft den Inhalt
		
		String msg = (String) subject.getUpdate(this);
		if (msg == null) {
			serviceLocator.getLogger().info(":: No new message");
		} else {
			try {
				serviceLocator.getLogger().info(":: Consuming message::" + msg);
				switch (msg) {

				
				// Holt das Player-Objekt und setzt es als Player1 ein und setzt die Werte im View ein. 
				case "player1":
					view.lblPlayer1Name.setText(model.getPlayerName(0));
					view.lblPlayer1HealthPoints.setText(Integer.toString(model.getPlayerHealthPoints(0)));
					view.lblPlayer1VictoryPoints.setText(Integer.toString(model.getPlayerVictoryPoints(0)));
					if (model.getPlayer(0).getInTokyo() == true) {
						view.lblPlayer1TokyoStatus.setText(view.t.getString("label.tokyo.in"));
					} else {
						view.lblPlayer1TokyoStatus.setText(view.t.getString("label.tokyo.out"));
					}
					break;

					        
				//	Holt das Player-Objekt und setzt es als Player2 ein und setzt die Werte im View ein. 
				case "player2":
					view.lblPlayer2Name.setText(model.getPlayerName(1));
					view.lblPlayer2HealthPoints.setText(Integer.toString(model.getPlayerHealthPoints(1)));
					view.lblPlayer2VictoryPoints.setText(Integer.toString(model.getPlayerVictoryPoints(1)));
					if (model.getPlayer(1).getInTokyo() == true) {
						view.lblPlayer2TokyoStatus.setText(view.t.getString("label.tokyo.in"));
					} else {
						view.lblPlayer2TokyoStatus.setText(view.t.getString("label.tokyo.out"));
					}
					break;

	        
				//	Die setDisable-Nachrichten sperren oder entsperren die jeweiligen Buttons.
				case "btnRollsetDisable(true)":
					view.btnRoll.setDisable(true);
					break;

				case "btnRollsetDisable(false)":
					view.btnRoll.setDisable(false);
					break;

				case "btnListDicesetDisable(true)":
					view.btnDice1.setDisable(true);
					view.btnDice2.setDisable(true);
					view.btnDice3.setDisable(true);
					view.btnDice4.setDisable(true);
					view.btnDice5.setDisable(true);
					view.btnDice6.setDisable(true);
					break;

				case "btnListDicesetDisable(false)":
					view.btnDice1.setDisable(false);
					view.btnDice2.setDisable(false);
					view.btnDice3.setDisable(false);
					view.btnDice4.setDisable(false);
					view.btnDice5.setDisable(false);
					view.btnDice6.setDisable(false);
					break;

				case "btnLeaveTokyosetDisable(true)":
					view.btnLeaveTokyo.setDisable(true);
					break;

				case "btnLeaveTokyosetDisable(false)":
					view.btnLeaveTokyo.setDisable(false);
					break;

				case "btnSendsetDisable(true)":
					view.btnSend.setDisable(true);
					break;

				case "btnSendsetDisable(false)":
					view.btnSend.setDisable(false);
					break;
	        
					
				// Holt das Würfel-Objekt vom Model und setzt die neuen Bilder im View ein.	
				case "dice":
					view.btnDice1.setGraphic(new ImageView(model.getDiePicture(0)));
					view.btnDice2.setGraphic(new ImageView(model.getDiePicture(1)));
					view.btnDice3.setGraphic(new ImageView(model.getDiePicture(2)));
					view.btnDice4.setGraphic(new ImageView(model.getDiePicture(3)));
					view.btnDice5.setGraphic(new ImageView(model.getDiePicture(4)));
					view.btnDice6.setGraphic(new ImageView(model.getDiePicture(5)));
					break;

					
				// Die Winner/Loser-Nachrichten geben dem Observer die Nachricht, welche View-
				// Updates er durchzuführen hat und dieser holt sich die entsprechenden Daten
				// und übergibt sie dem View.
				case "Player1lost":
					model.setGameState(1);
					view.lblGameEnd.setText(view.t.getString("label.gameend.lose"));
					view.lblGameEnd.setStyle("-fx-background-color: #FFFFFF");
					view.imgVPlayer1Monster.setImage(view.player1Pictures.get(2));
					view.imgVPlayer2Monster.setImage(view.player2Pictures.get(1));
					view.btnRoll.setDisable(true);
					view.btnLeaveTokyo.setDisable(true);
					for (Button b : dicebtnList) {
						b.setDisable(true);
					}
					view.aGameEnd.setTitle(view.t.getString("label.gameend.lose"));
					ImageView p1looser = new ImageView(view.player1Pictures.get(2));
					p1looser.setFitWidth(260);
					p1looser.setFitHeight(358);
					view.aGameEnd.setGraphic(p1looser);
					view.aGameEnd.showAndWait();
					break;

				case "Player2lost":
					model.setGameState(2);
					view.lblGameEnd.setText(view.t.getString("label.gameend.lose"));
					view.lblGameEnd.setStyle("-fx-background-color: #FFFFFF");
					view.imgVPlayer1Monster.setImage(view.player1Pictures.get(1));
					view.imgVPlayer2Monster.setImage(view.player2Pictures.get(2));
					view.btnRoll.setDisable(true);
					view.btnLeaveTokyo.setDisable(true);
					for (Button b : dicebtnList) {
						b.setDisable(true);
					}
					view.aGameEnd.setTitle(view.t.getString("label.gameend.lose"));
					ImageView p2looser = new ImageView(view.player2Pictures.get(2));
					p2looser.setFitWidth(260);
					p2looser.setFitHeight(358);
					view.aGameEnd.setGraphic(p2looser);
					view.aGameEnd.showAndWait();
					break;

				case "Player1won":
					model.setGameState(3);
					view.lblGameEnd.setText(view.t.getString("label.gameend.win"));
					view.lblGameEnd.setStyle("-fx-background-color: #FFFFFF");
					view.imgVPlayer1Monster.setImage(view.player1Pictures.get(1));
					view.imgVPlayer2Monster.setImage(view.player2Pictures.get(2));
					view.btnRoll.setDisable(true);
					view.btnLeaveTokyo.setDisable(true);
					for (Button b : dicebtnList) {
						b.setDisable(true);
					}
					view.aGameEnd.setTitle(view.t.getString("label.gameend.win"));
					ImageView p1winner = new ImageView(view.player1Pictures.get(1));
					p1winner.setFitWidth(260);
					p1winner.setFitHeight(358);
					view.aGameEnd.setGraphic(p1winner);
					view.aGameEnd.showAndWait();
					break;

				case "Player2won":
					model.setGameState(4);
					view.lblGameEnd.setText(view.t.getString("label.gameend.win"));
					view.lblGameEnd.setStyle("-fx-background-color: #FFFFFF");
					view.imgVPlayer1Monster.setImage(view.player1Pictures.get(2));
					view.imgVPlayer2Monster.setImage(view.player2Pictures.get(1));
					view.btnRoll.setDisable(true);
					view.btnLeaveTokyo.setDisable(true);
					for (Button b : dicebtnList) {
						b.setDisable(true);
					}
					view.aGameEnd.setTitle(view.t.getString("label.gameend.win"));
					ImageView p2winner = new ImageView(view.player2Pictures.get(1));
					p2winner.setFitWidth(260);
					p2winner.setFitHeight(358);
					view.aGameEnd.setGraphic(p2winner);
					view.aGameEnd.showAndWait();
					break;

					
				// Die LockbtnDice/UnlockbtnDice-Nachrichten holen sich jeweils die entsprechenden Daten
				// aus dem Model und lassen auf der View anzeigen, ob der Würfel locked ist oder nicht.
				case "LockbtnDice1:":
					view.btnDice1.setGraphic(new ImageView(model.getDiePicture(0)));
					break;

				case "UnlockbtnDice1:":
					view.btnDice1.setGraphic(new ImageView(model.getDiePicture(0)));
					break;

				case "LockbtnDice2:":
					view.btnDice2.setGraphic(new ImageView(model.getDiePicture(1)));
					break;

				case "UnlockbtnDice2:":
					view.btnDice2.setGraphic(new ImageView(model.getDiePicture(1)));
					break;

				case "LockbtnDice3:":
					view.btnDice3.setGraphic(new ImageView(model.getDiePicture(2)));
					break;

				case "UnlockbtnDice3:":
					view.btnDice3.setGraphic(new ImageView(model.getDiePicture(2)));
					break;

				case "LockbtnDice4:":
					view.btnDice4.setGraphic(new ImageView(model.getDiePicture(3)));
					break;

				case "UnlockbtnDice4:":
					view.btnDice4.setGraphic(new ImageView(model.getDiePicture(3)));
					break;

				case "LockbtnDice5:":
					view.btnDice5.setGraphic(new ImageView(model.getDiePicture(4)));
					break;

				case "UnlockbtnDice5:":
					view.btnDice5.setGraphic(new ImageView(model.getDiePicture(4)));
					break;

				case "LockbtnDice6:":
					view.btnDice6.setGraphic(new ImageView(model.getDiePicture(5)));
					break;

				case "UnlockbtnDice6:":
					view.btnDice6.setGraphic(new ImageView(model.getDiePicture(5)));
					break;

					
				//  Chat-Nachrichten werden in der View angezeigt
				case "chatMessage":
					view.taChat.appendText(model.getChatMessage() + "\n");
					break;

					
				// Wenn der Server stoppt werden die Buttons gesperrt und der
				//	Button zur Wiederherstellung der Verbindung wird angezeigt.	
				case "Server_quit":
					enableRestart();
					view.btnLeaveTokyo.setDisable(true);
					view.btnRoll.setDisable(true);
					view.btnSend.setDisable(true);
					view.tfChat.setEditable(false);
					break;
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}
}


