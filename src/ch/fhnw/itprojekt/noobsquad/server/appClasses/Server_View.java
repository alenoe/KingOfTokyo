package ch.fhnw.itprojekt.noobsquad.server.appClasses;

/**
 * 
 * @author Alexander Noever
 * 
 */

import java.util.Locale;
import ch.fhnw.itprojekt.noobsquad.server.supportClasses.ServiceLocator;
import ch.fhnw.itprojekt.noobsquad.server.supportClasses.Translator;
import ch.fhnw.itprojekt.noobsquad.abstractClasses.View;
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
    
    TextField tfIP;
    TextField tfPort;
    TextArea taLogger;
    
    Button btnStart;
    Button btnClose;
    Label lblHostName;
    
	TextAreaHandler textAreaHandler;
	
	ServiceLocator serviceLocator;

	/**
	 * Die Server_View Klasse erbt von der abstrakten Klasse View und stellt damit die visuelle Komponente des Servers dar.
	 * Diese View beinhaltet den Construktor, eine create_GUI methode und eine updateText methode.
	 * Der Constructor und die create_GUI methode entstammen der abstrakten Klasse View - geschrieben von Bradley Richards 
	 * Die updateText methode wird zur Uebersetzung der GuiElemente verwendet und ist an ein Beispiel aus Bradley Richards Vorlesung
	 * angelehnt.
	 * @param stage
	 * @param model
	 */
	public Server_View(Stage stage, Server_Model model) {
		super(stage, model);
		
		stage.setTitle("KingOfTokyo byNoobsquad - Server");
		this.model = model;
		
		serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.getLogger().info("KingOfTokyo Server_View initialized");
		
	}

    /**
     * Das GUI besteht aus zwei wesentlichen Komponenten, einer GridPane namens entryArea
     * und einem TextAreaHandler, beide sind in eine BorderPane eingebettet.
     */
	@Override
	protected Scene create_GUI() {
		
    	// ServiceLocator und Translator werden gesetzt.
		ServiceLocator sl = ServiceLocator.getServiceLocator();  
	    Translator t = sl.getTranslator();
	    
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
        root.setPrefSize(409, 219);
        
        menuBar.getMenus().addAll(menuFile);
        root.setTop(menuBar);
        

/**______________________________________________________________________________________________
 *							START OF GRIDPANE !!! ENTRYAREA !!!
 */
        
        
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
              

/**______________________________________________________________________________________________
 *							START OF !!! TEXTAREAHANDLER !!!
 */
        
        
        textAreaHandler = new TextAreaHandler();
        taLogger = textAreaHandler.getTextArea();
        taLogger.setId("taLogger");
        taLogger.setEditable(false);
        root.setBottom(taLogger);
        
        Scene scene = new Scene(root);
        
		return scene;
	}
	
	
    /**
     * Diese Methode wird aufgerufen, wenn die Sprachauswahl geaendert wird.
     * Sie geht über alle GUI Elemente und holt den entsprechenden String aus dem 
     * Eigenschaften File.
     */
	protected void updateTexts() {
		Translator t = ServiceLocator.getServiceLocator().getTranslator();
	        
	    // The menu entries
	    menuFile.setText(t.getString("program.menu.file"));
	    menuFileLanguage.setText(t.getString("program.menu.file.language"));
	        
	    // Buttons
	    btnStart.setText(t.getString("button.start"));
	    btnClose.setText(t.getString("button.close"));
	    
	    //TextField Promt
	    tfPort.setPromptText(t.getString("textfield.prompt.port"));
	    }
}
