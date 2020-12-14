package org.fcr.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.fcr.gui.MainGUI;
import org.fcr.service.CommonService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public class MainController implements Initializable {

	@FXML
	private TabPane tabPane;

	@FXML
	private Tab diskTab;

	@FXML
	private Tab cpuTab;

	@FXML
	private Tab memoryTab;

	@FXML
	private Tab networkTab;

	@FXML
	private MenuItem exportMenu;

	@FXML
	private MenuItem exitMenu;

	@FXML
	private MenuItem aboutMenu;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			AnchorPane pane = MainGUI.getFXMLPage("diskTab");
			if(pane!=null)
				diskTab.setContent(pane);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exitApplication() {
		System.exit(0);
	}

	public void exportCurrentTabData() {
		Writable obj = getCurrentTabList(tabPane.getSelectionModel().getSelectedItem().getId());
		if(obj==null)
			MainGUI.loadAlert("ERROR","You Cannot Export Data from the selected Tab.");
		else 
			obj.writeToFile();
	}	

	public static Writable getCurrentTabList(String id){
		switch(id) {
			case "diskTab":
				return new DiskController();
			default :
				return null;
		}
	}
	
	public void aboutApplication() {
		MainGUI.loadAlert("About File Change Reader",CommonService.getProperties().getProperty("fcr_about"));
	}

}
