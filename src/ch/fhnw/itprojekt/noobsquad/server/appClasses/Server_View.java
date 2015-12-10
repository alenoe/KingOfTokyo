package ch.fhnw.itprojekt.noobsquad.server.appClasses;

import java.util.Locale;
import ch.fhnw.itprojekt.noobsquad.server.supportClasses.ServiceLocator;
import ch.fhnw.itprojekt.noobsquad.server.supportClasses.Translator;
import ch.fhnw.itprojekt.noobsquad.server.abstractClasses.View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Server_View extends View<Server_Model>{
	
	Menu menuFile;
    Menu menuFileLanguage;
    Menu menuHelp;
    
    TextField tfIP;
    TextField tfPort;
    TextArea taLogger;
    
    Button btnStart;
    Button btnClose;
    Button btnDatabase;
    Label lblHostName;
    
	TextAreaHandler textAreaHandler;

	public Server_View(Stage stage, Server_Model model) {
		super(stage, model);
		
		stage.setTitle("KingOfTokyo byNoobsquad - Server");
		this.model = model;
		
		ServiceLocator.getServiceLocator().getLogger().info("KingOfTokyo Server_View initialized");
		
	}

	@Override
	protected Scene create_GUI() {

		ServiceLocator sl = ServiceLocator.getServiceLocator();  
	    Translator t = sl.getTranslator();
	    
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
        root.setPrefSize(409, 219);
        
        menuHelp = new Menu(t.getString("program.menu.help"));
        menuBar.getMenus().addAll(menuFile, menuHelp);
        root.setTop(menuBar);
        
        //Serverentry Area
        GridPane entryArea = new GridPane();
        entryArea.setAlignment(Pos.CENTER);
        root.setCenter(entryArea);
        
        Label lblIP = new Label();
        lblIP.setText("IP: ");;
        lblIP.setAlignment(Pos.CENTER_LEFT);
        entryArea.add(lblIP, 0, 0);
        GridPane.setMargin(lblIP, new Insets(5, 5, 0, 50));
        
        Label lblPort = new Label();
        lblPort.setText("Port: ");
        lblPort.setAlignment(Pos.CENTER_LEFT);
        entryArea.add(lblPort, 0, 1);
        GridPane.setMargin(lblPort, new Insets(5, 5, 0, 50));
        
        lblHostName = new Label();
        lblHostName.setAlignment(Pos.CENTER_LEFT);
        lblHostName.setId("lblHostName");
        entryArea.add(lblHostName, 0, 2);
        GridPane.setMargin(lblHostName, new Insets(5, 5, 0, 50));
        
        tfIP = new TextField();
        tfIP.setId("tfIP");
        tfIP.setEditable(false);
        entryArea.add(tfIP, 1, 0);
        GridPane.setMargin(tfIP, new Insets(5, 5, 0, 0));
        
        tfPort = new TextField();
        tfPort.setId("tfPort");
		tfPort.setText("14000");
		tfPort.setPromptText("Bitte Portnummer eingeben.");
        entryArea.add(tfPort, 1, 1);
        GridPane.setMargin(tfPort, new Insets(5, 5, 0, 0));
        
        btnStart = new Button();
        btnStart.setId("btnStart");
        btnStart.setText(t.getString("button.start"));
        entryArea.add(btnStart, 2, 0);
        GridPane.setMargin(btnStart, new Insets(5, 5, 0, 50));
        
        btnClose = new Button();
        btnClose.setId("btnClose");
        btnClose.setText(t.getString("button.close"));
        entryArea.add(btnClose, 2, 1);
        GridPane.setMargin(btnClose, new Insets(5, 5, 0, 50));
        
        btnDatabase = new Button();
        btnDatabase.setId("btnDatabase");
        btnDatabase.setText(t.getString("button.database"));
        entryArea.add(btnDatabase, 2, 2);
        GridPane.setMargin(btnDatabase, new Insets(5, 5, 0, 50));
        
        textAreaHandler = new TextAreaHandler();
        taLogger = textAreaHandler.getTextArea();
        taLogger.setId("taLogger");
        taLogger.setEditable(false);
        root.setBottom(taLogger);
        
        Scene scene = new Scene(root);
        
		return scene;
	}
	
	protected void updateTexts() {
		Translator t = ServiceLocator.getServiceLocator().getTranslator();
	        
	    // The menu entries
	    menuFile.setText(t.getString("program.menu.file"));
	    menuFileLanguage.setText(t.getString("program.menu.file.language"));
	    menuHelp.setText(t.getString("program.menu.help"));
	        
	    // Other controls
	    btnStart.setText(t.getString("button.start"));
	    btnClose.setText(t.getString("button.close"));
	    btnDatabase.setText(t.getString("button.database"));
	    
	    //TextField Promt
	    tfPort.setPromptText(t.getString("textfield.prompt.port"));
	    }
}
