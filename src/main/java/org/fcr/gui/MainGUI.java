package org.fcr.gui;

import java.util.Optional;

import org.fcr.gui.controller.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainGUI extends Application {
	
	public static void loadGUI(String... args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			primaryStage.setTitle("File Change Reader");
			//primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResource("icons/title.png").getPath()));
			Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("templates/main.fxml"));
			primaryStage.setScene(new Scene(parent));
			primaryStage.show();
		} catch (Exception e) {
			System.out.println("Application Failed to Start");
			e.printStackTrace();
		}
	}
	
	public static void loadAlert(String title,String message) {
		Alert alert = new Alert(AlertType.NONE,message,ButtonType.OK);
		alert.setTitle(title);
		alert.setResizable(false);
		alert.showAndWait();
	}
	
	public static boolean loadConfirmation(String title,String message) {
		Alert alert = new Alert(AlertType.NONE,message,ButtonType.YES,ButtonType.NO);
		alert.setTitle(title);
		alert.setResizable(false);
		Optional<ButtonType> result = alert.showAndWait();
		return (result.get()==ButtonType.YES);
	}
	
	public static AnchorPane getFXMLPage(String pageName) {
		AnchorPane pane = null;
		try {
			pane = FXMLLoader.load(MainController.class.getClassLoader().getResource("templates/"+pageName+".fxml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pane;
	}
}
