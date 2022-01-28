package com.example.dbms1;

import java.io.IOException;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class CreateTableController {
   static String Database;
	@FXML
	TextField NameofTable;
	@FXML
	TableView<FieldData> table;
	@FXML
	TableColumn<FieldData,String> Field;
	@FXML
	TableColumn<FieldData,String> DataType;
	@FXML
 public	void initialize()
	{
		for(int i = 0;i<10;i++)
		{
			FieldData fd = new FieldData();
			fd.setDataType(" ");
			fd.setField("  ");
			table.getItems().add(fd);
		}
		
		
		
		table.setEditable(true);
		
		Field.setCellValueFactory(new PropertyValueFactory<FieldData,String>("Field"));
		Field.setCellFactory(TextFieldTableCell.forTableColumn());
		
		
		
		
		
		
		
		
		
		Field.setOnEditCommit(new EventHandler<CellEditEvent<FieldData,String>>(){

			@Override
			public void handle(CellEditEvent<FieldData, String> event) {
				FieldData   fd = event.getRowValue();
				
				
				fd.setField(event.getNewValue());
				
			}});
		
		
		DataType.setEditable(true);
		DataType.setCellFactory(TextFieldTableCell.forTableColumn());
		DataType.setCellValueFactory(new PropertyValueFactory<FieldData,String>("DataType"));
	
	
		
		DataType.setOnEditCommit(new EventHandler<CellEditEvent<FieldData,String>>(){

			@Override
			public void handle(CellEditEvent<FieldData, String> event) {
				FieldData   fd = event.getRowValue();
				fd.setDataType(event.getNewValue());
			}
			
		});
	}
	@FXML
	void onSave() throws IllegalAccessException, IOException
	{
		try {
		Repository repo = new Repository();
		Entity e = new Entity();
	ObservableList<FieldData> list =   table.getItems();
	
	 for(int i = 0;i< list.size();i++)
	 {
		
		 
		if( list.get(i).getDataType().toLowerCase().contains( "double") || list.get(i).getDataType().toLowerCase().contains( "integer")|| list.get(i).getDataType().toLowerCase() .contains( "int"))
		 {
			 e.getDoubleFields().put(list.get(i).getField(), 0.0);
			
		 }
		if(list.get(i).getDataType().toLowerCase().contains("string"))
		{
			e.getStringFields().put(list.get(i).getField(), "h ");
		}
	 }
	 
	 
	 
		e.setTitle(NameofTable.getText());
		repo.Save(e, Database);
		}
		catch( Exception e) {e.printStackTrace();}
		Database = null;
		
		MainController.setScene(HelloApplication.fxmlLoader.getController());
	}
	
	
}
