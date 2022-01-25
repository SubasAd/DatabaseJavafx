package com.example.dbms1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
	static Stage pStage;
	static Scene pScene;
	static FXMLLoader fxmlLoader;
    @Override
    public void start(Stage stage) throws IOException {
    	
    fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainWindow1.fxml"));
    
        Scene scene = new Scene(fxmlLoader.load(), 1800, 1060);
        pStage = stage;
        pScene = scene;
       
        stage.setTitle("DBMS");
        stage.setScene(scene);
        
        stage.show();
    }

    public static void main(String[] args)  {
    	
     launch();
    }
}
