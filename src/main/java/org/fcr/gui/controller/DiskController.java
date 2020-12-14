package org.fcr.gui.controller;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.fcr.gui.MainGUI;
import org.fcr.gui.dto.DiskDTO;
import org.fcr.service.DiskService;

import com.opencsv.CSVWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class DiskController implements Initializable, Writable{
	
	@FXML 
	private AnchorPane diskAnchor;

	@FXML
	private TableView<DiskDTO> diskTable;

	@FXML
	private TableColumn<DiskDTO, String> name;

	@FXML
	private TableColumn<DiskDTO, String> size;

	@FXML
	private TableColumn<DiskDTO, String> time;

	@FXML
	private TableColumn<DiskDTO, String> path;

	@FXML
	private Label diskTotalFiles;
	
	@FXML
	private ProgressIndicator loader;

	@FXML
	private TextField searchBar;

	@FXML
	private DatePicker datePicker;

	@FXML
	private Button goButton;
	
	private static List<DiskDTO> backupData = new ArrayList<DiskDTO>();

	private static volatile ObservableList<DiskDTO> diskData = FXCollections.observableArrayList();

	synchronized public static void addDiskData(DiskDTO disk) {
		diskData.add(disk);
	}
	
	public static List<DiskDTO> getDiskDataList(){
		return diskData;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loader.setVisible(false);
		searchBar.setDisable(true);
		diskTotalFiles.setText("0");
		datePicker.setValue(LocalDate.now());
		name.setCellValueFactory(new PropertyValueFactory<DiskDTO, String>("name"));
		size.setCellValueFactory(new PropertyValueFactory<DiskDTO, String>("size"));
		time.setCellValueFactory(new PropertyValueFactory<DiskDTO, String>("time"));
		path.setCellValueFactory(new PropertyValueFactory<DiskDTO, String>("path"));
		diskTable.setItems(diskData);
		diskTable.setSortPolicy(e->{
			if(e.getSortOrder().size()>0 && e.getSortOrder().get(0).equals(size))
				FXCollections.sort(
						diskTable.getItems(),
						DiskDTO.getSizeComparator(diskTable.getColumns().get(diskTable.getColumns().indexOf(size)).getSortType().toString()));
			else
				FXCollections.sort(diskTable.getItems(),e.getComparator());
			return true;
		});
	}

	public void fetchData() {
		searchBar.setText("");
		searchBar.setDisable(true);
		goButton.setDisable(true);
		File file= null;
		if(MainGUI.loadConfirmation("Read a specific folder", "Do you want to choose any folder to read that only ??"))
			file = getPathToRead();
		String path = (file==null)?null:file.getAbsolutePath();
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				ExecutorService executor = new DiskService()
						.readDisk(Timestamp.valueOf(datePicker.getValue().atStartOfDay()).getTime(),path);
				while(!executor.isTerminated());
				return null;
			}
		};
		task.setOnSucceeded(event->{
			loader.setVisible(false);
			diskTotalFiles.setText(diskData.size()+"");
			goButton.setDisable(false);
			searchBar.setDisable(false);
		});
		new Thread(task).start();
		diskData.clear();
		backupData.clear();
		diskTotalFiles.setText("");
		loader.setVisible(true);
	}
	
	public File getPathToRead() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select");
		File file = directoryChooser.showDialog(diskAnchor.getScene().getWindow());
		return file;
	}

	public void search() {
		String input = searchBar.getText();
		if(backupData.isEmpty())
			backupData.addAll(diskData);
		diskData.clear();
		diskData.addAll(backupData.stream().filter(e->e.getPath().toLowerCase().contains(input.toLowerCase())).collect(Collectors.toList()));
		diskTotalFiles.setText(diskData.size()+"");
	}
	
	@Override
	public void writeToFile() {
		try {
			if(diskData.isEmpty()) {
				MainGUI.loadAlert("ERROR","No Data to Export on the selected Tab.");
				return;
			}
			FileChooser filechooser = new FileChooser();
			filechooser.setTitle("Save");
			filechooser.getExtensionFilters().add((new ExtensionFilter("CSV Sheet File (*.csv)", ".csv")));
			File file = filechooser.showSaveDialog(diskAnchor.getScene().getWindow());
			CSVWriter writer = new CSVWriter(new FileWriter(file));
			writer.writeNext(new String[] {"Name","Size","Time","Path"});
			diskData.forEach(e->writer.writeNext(new String[] {e.getName(),e.getSize(),e.getTime(),e.getPath()}));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
