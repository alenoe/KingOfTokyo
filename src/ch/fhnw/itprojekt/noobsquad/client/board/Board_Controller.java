package ch.fhnw.itprojekt.noobsquad.client.board;



/**
 * 
 * @author Heiko Meyer
 * 
 */

import java.util.ArrayList;
import ch.fhnw.itprojekt.noobsquad.gameLogic.Player;
import ch.fhnw.itprojekt.noobsquad.client.interfaces.Observer;
import ch.fhnw.itprojekt.noobsquad.client.interfaces.Subject;
import ch.fhnw.itprojekt.noobsquad.client.main.JavaFX_App_Template;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import ch.fhnw.itprojekt.noobsquad.client.supportClasses.ServiceLocator;
import ch.fhnw.itprojekt.noobsquad.abstractClasses.Controller;

public class Board_Controller extends Controller<Board_Model, Board_View> implements Observer{
	
	ServiceLocator serviceLocator;
	private static ArrayList<Button> dicebtnList;
	private Player player1;
	private Player player2;
	private Subject subject;
	
    public Board_Controller(JavaFX_App_Template main, Board_Model model, Board_View view) {
        super(model, view);
        
        this.subject = model;
        model.register(this);
        serviceLocator = ServiceLocator.getServiceLocator();
        
        dicebtnList = new ArrayList<Button>();
        dicebtnList.add(view.btnDice1);
		dicebtnList.add(view.btnDice2);
		dicebtnList.add(view.btnDice3);
		dicebtnList.add(view.btnDice4);
		dicebtnList.add(view.btnDice5);
		dicebtnList.add(view.btnDice6);
		
		for (Button btn: dicebtnList){
			btn.setDisable(true);
		}
		
		view.btnRoll.setDisable(true);
		view.btnLeaveTokyo.setDisable(true);
		view.btnSend.setDisable(true);

		
		view.btnRoll.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				boolean btnRollState = model.roll();
				
				Platform.runLater(() -> {
				view.btnRoll.setDisable(btnRollState);
				});
			}
		});
		
		
		view.btnLeaveTokyo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				model.playerLeavesTokyo();
				
				Platform.runLater(() -> {
					view.btnLeaveTokyo.setDisable(true);
				});
			}
		});
		
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
		
		view.btnSend.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
					model.sendChatMessage(view.tfChat.getText());
					
					Platform.runLater(() -> {
						view.tfChat.setText("");;
					});
				}
		});
		
		view.btnNewConnection.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				main.restart();
			}
		});	
				
		view.btnClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Platform.exit();
			}
		});
		
			if(!model.getServerConnection().getServerconnection()){
				enableRestart();
			}
			
        serviceLocator.getLogger().info("Board controller initialized");
    }
    
    // Implementation Restart
    public void enableRestart(){
		view.btnNewConnection.setVisible(true);
		view.lblNewConnection.setVisible(true);
		view.hboxNewCon.setStyle("-fx-background-color: #FFFFFF");
    }

	@Override
	public void update() {
		String msg = (String) subject.getUpdate(this);
        if(msg == null){
            System.out.println(":: No new message");
        }else {
        	try{
		        System.out.println(":: Consuming message::"+msg);
		        switch (msg){
		        
		
		        //____________________________________________________________
		        //newGame start messages
		        //
		        
		
		        case "player1":
		        	this.player1 = model.getPlayer(0);
		    		view.lblPlayer1Name.setText(player1.getName());
		        	view.lblPlayer1HealthPoints.setText(Integer.toString(player1.getLifePoints()));
		        	view.lblPlayer1VictoryPoints.setText(Integer.toString(player1.getVictoryPoints()));
		        	if(player1.getInTokyo() == true){
		        	view.lblPlayer1TokyoStatus.setText(view.t.getString("label.tokyo.in"));
		        	} else {
		        		view.lblPlayer1TokyoStatus.setText(view.t.getString("label.tokyo.out"));
		        	}
		//        	view.imgVPlayer1Monster.setImage(view.player1Pictures.get(0));
		        	break;
		        case "player2":
		        	this.player2 = model.getPlayer(1);
		    		view.lblPlayer2Name.setText(player2.getName());
		        	view.lblPlayer2HealthPoints.setText(Integer.toString(player2.getLifePoints()));
		        	view.lblPlayer2VictoryPoints.setText(Integer.toString(player2.getVictoryPoints()));
		        	if(player2.getInTokyo() == true){
		        	view.lblPlayer2TokyoStatus.setText(view.t.getString("label.tokyo.in"));
		        	} else {
		        		view.lblPlayer2TokyoStatus.setText(view.t.getString("label.tokyo.out"));
		        	}
		        	break;
		        	
		        	//
		        	//Buttons setDisable Messages
		        	//
		        	
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
		        case "dice":
		        	model.getDice();
		        	view.btnDice1.setGraphic(new ImageView(model.getDiePicture(0)));
		        	view.btnDice2.setGraphic(new ImageView(model.getDiePicture(1)));
		        	view.btnDice3.setGraphic(new ImageView(model.getDiePicture(2)));
		        	view.btnDice4.setGraphic(new ImageView(model.getDiePicture(3)));
		        	view.btnDice5.setGraphic(new ImageView(model.getDiePicture(4)));
		        	view.btnDice6.setGraphic(new ImageView(model.getDiePicture(5)));
		        	break;
		        case "Player1lost":
		        	model.setGameState(1);
		        	view.lblGameEnd.setText(view.t.getString("label.gameend.lose"));
		        	view.lblGameEnd.setStyle("-fx-background-color: #FFFFFF");
		        	view.imgVPlayer1Monster.setImage(view.player1Pictures.get(2));
		        	view.imgVPlayer2Monster.setImage(view.player2Pictures.get(1));
		        	view.btnRoll.setDisable(true);
		        	view.btnLeaveTokyo.setDisable(true);
		        	for(Button b: dicebtnList){
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
		        	for(Button b: dicebtnList){
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
		        	for(Button b: dicebtnList){
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
		        	for(Button b: dicebtnList){
		        		b.setDisable(true);
		        	}
		        	view.aGameEnd.setTitle(view.t.getString("label.gameend.win"));
		        	ImageView p2winner = new ImageView(view.player2Pictures.get(1));
		        	p2winner.setFitWidth(260);
		        	p2winner.setFitHeight(358);
		        	view.aGameEnd.setGraphic(p2winner);
		        	view.aGameEnd.showAndWait();
		        	break;
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
				case "chatMessage":
					view.taChat.appendText(model.getChatMessage()+"\n");
				case "Server_quit":
					enableRestart();
					view.btnLeaveTokyo.setDisable(true);
					view.btnRoll.setDisable(true);
					view.btnSend.setDisable(true);
					view.tfChat.setEditable(false);
		        }
        	} catch (NullPointerException e){
        		e.printStackTrace();
        	}
        }
	}

	@Override
	public void setSubject(Subject sub) {
		this.subject=sub;
		
	}   
}


