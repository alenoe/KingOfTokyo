package ch.fhnw.itprojekt.noobsquad.client.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Locale;

import ch.fhnw.itprojekt.noobsquad.client.abstractClasses.*;
import ch.fhnw.itprojekt.noobsquad.server.supportClasses.ServiceLocator;
import ch.fhnw.itprojekt.noobsquad.server.supportClasses.Translator;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class Login_View extends View<Login_Model>{
	
	Menu menuFile;
    Menu menuFileLanguage;
    Menu menuHelp;

	Label lblUsername;
	Label lblIP;
	Label lblPort;
	TextField tfUsername;
	TextField tfIP;
	TextField tfPort;
	Button btnConnect;

    public Login_View(Stage stage, Login_Model model) {
        super(stage, model);
        stage.setTitle("KingOfTokyo byNoobsquad - Login");
    }

    @Override
    protected Scene create_GUI() {
       
        BorderPane root = new BorderPane();
        root.setId("splash");
        root.setStyle("-fx-background-color: #FFFFFF");
        
        GridPane gridPane = new GridPane();
        root.setCenter(gridPane);
        gridPane.setAlignment(Pos.CENTER);
        Insets is = new Insets(5.0);
        
        lblUsername = new Label();
        lblUsername.setId("lblUsername");
        lblUsername.setText("Username: ");
        gridPane.add(lblUsername, 1, 1);
        GridPane.setMargin(lblUsername, is);
        
        lblIP = new Label();
        lblIP.setId("lblIP");
        lblIP.setText("IP: ");
        gridPane.add(lblIP, 1, 2);
        GridPane.setMargin(lblIP, is);
        
        lblPort = new Label();
        lblPort.setId("lblPort");
        lblPort.setText("Port: ");
        gridPane.add(lblPort, 1, 3);
        GridPane.setMargin(lblPort, is);
        
        btnConnect = new Button();
        btnConnect.setId("btnConnect");
        btnConnect.setText("Connect");
        gridPane.add(btnConnect, 1, 4);
        GridPane.setMargin(btnConnect, is); 
            
        tfUsername = new TextField();
        tfUsername.setId("tfUsername");
        tfUsername.setPromptText("gib einen Usernamen ein.");
        gridPane.add(tfUsername, 2, 1);
        GridPane.setMargin(tfUsername, is);
        
        tfIP = new TextField();
        tfIP.setId("tfIP");
        gridPane.add(tfIP, 2, 2);
        GridPane.setMargin(tfIP, is);
		tfIP.setText(model.getLokalHost());
		tfIP.setPromptText("deine jetzige IP: "+model.getLokalHost());

        tfPort = new TextField();
        tfPort.setId("tfPort");
        gridPane.add(tfPort, 2, 3);
        GridPane.setMargin(tfPort, is);
		tfPort.setText("14000");
		tfPort.setPromptText("gib eine Portnummer ein.");
		

        Scene scene = new Scene(root, 300, 300, Color.TRANSPARENT);
//        scene.getStylesheets().addAll(
//                this.getClass().getResource("splash.css").toExternalForm());

        return scene;
    }
}
