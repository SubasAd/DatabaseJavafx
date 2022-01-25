package com.example.dbms1;

import java.io.IOException;


import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

public class DialogController {
@FXML
DialogPane	createdatabase;
@FXML
TextField CreateDatabaseField;
@FXML
DatePicker Dp;
@FXML
 Button Create;
@FXML
void OnCreate() throws IllegalAccessException, IOException
{
	Repository r = new Repository();
	Entity e = new Entity();
	e.setTitle(CreateDatabaseField.getText());
	try {
	r.Save(e,CreateDatabaseField.getText() );
	CreateTableController.Database = CreateDatabaseField.getText();
	
	}catch(Exception ex)
	{
		ex.printStackTrace();
	}

Button b = (Button)	createdatabase.lookupButton(ButtonType.CLOSE);
b.fire();


}

}
