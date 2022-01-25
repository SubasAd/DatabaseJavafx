package com.example.dbms1;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

public class MainController {

	static Alert alert;
	@FXML
	private VBox vb;
	@FXML
	private AnchorPane ViewPane;
	@FXML
	private Menu FileMenu;
	@FXML
	private Menu EditMenu;
	@FXML
	private Button CreateNewTable;
	@FXML
	private TreeView<String> ListOfDatabase;
	@FXML
	private TableView<Entity> DatabaseContents;
	@FXML
	AnchorPane sidepane;
	@FXML
	Text Properties;
	@FXML
	HBox sidehbox;
	@FXML
	VBox sideVBox;
	
	TextField [] textf = new TextField[25];
	int count=0;
	
	TreeItem<String> database;
	
	@FXML
	void CreateNewTable() throws IOException
	{
		CreateNewTable.setVisible(false);
		  FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CreateTable.fxml"));
			VBox vb =  fxmlLoader.load();
		     
		      vb.setVisible(true);
		      alert= new Alert(Alert.AlertType.INFORMATION);
			   
		      HelloApplication.pStage.setScene(new Scene(vb,1920,1080));
		      
		   alert.close();
		   
		      this.initialize();

		
	}
	@FXML
	void CreateNewDatabase() throws IOException
	{
		  FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Dialog.fxml"));
		DialogPane dp =  fxmlLoader.load();
	     
	      dp.setVisible(true);
	     alert = new Alert(Alert.AlertType.INFORMATION);
	      alert.setDialogPane(dp);
	      alert.showAndWait();
	      this.initialize();
	      CreateNewTable();
	     
	      
		
	}
	@FXML
	void Open() {
		
		getDatabaseContents().setPrefWidth(ViewPane.getWidth());
		getDatabaseContents().setPrefHeight(vb.getHeight());
		getDatabaseContents().setVisible(true);
	}
	
	@FXML
	void Close() throws IllegalAccessException, IOException
	{
		Save();
		Exit();
		
		
	}
	@FXML
	void Save() throws IllegalAccessException, IOException
	{
		Entity []e = GetEntities();
		Repository r = new Repository();
		if(e== null) {return ;}
		
			try {
			 r.Overwrite(e,database.getParent().getValue());
			 setHistory(database.getParent().getValue(),"Opened on " +LocalDateTime.now());
			}catch(Exception ex){
				
				System.out.println(ex.getLocalizedMessage()+": while  overwriting");
			}
		
		  
		
	}
	private Entity[] GetEntities() {
		
		Entity [] e =new Entity[DatabaseContents.getItems().size()];
		
		for(int i =0;i<e.length;i++)
		{
			 e[i] =	 DatabaseContents.getItems().get(i);
		}
 	
		return e;
		
	}
	@FXML
	void Exit() throws IllegalAccessException, IOException
	{
		Save();
		HelloApplication.pStage.close();
	}
	@FXML
	void Delete() throws IllegalAccessException, IOException
	{
	Entity e =	DatabaseContents.getSelectionModel().getSelectedItem();
	DatabaseContents.getItems().remove(e);
	Save();
	setHistory(database.getParent().getValue(),"Deleted an Item on  "+LocalDateTime.now() );
		
	}
	@FXML
	 void initialize() throws IOException
	{
		
		Repository repo = new Repository();
		Entity[] e  = repo.GetAllTables("Alldbs");
      TreeItem<String> root = new TreeItem<String>("All Databases");
      CreateNewTable.setVisible(false);
      EventHandler<MouseEvent> click = (MouseEvent event) ->{
    	  try {
    		handleMouseClicked(event);
    	} catch (IOException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}
      };
    
      
   for(int i=1;i<e.length;i++) 
   { 
	   
	   try {
	   
	   Entity[] allTablesofeachdatabase = repo.GetAllTables(e[i].getTitle());
	   TreeItem database = new TreeItem(e[i].getTitle());
	   
	   root.getChildren().add(database);
	   for(int j =0;j< allTablesofeachdatabase.length;j++)
	          database.getChildren().add(new TreeItem(allTablesofeachdatabase[j].getTitle()));
	 
   }catch(Exception exception)
   {
	   exception.printStackTrace();
   }
   }
   
   ListOfDatabase.setRoot(root);
   EventHandler<MouseEvent> databaseSelected = (MouseEvent event) ->
   {
	   try{
 	database =  ListOfDatabase.getSelectionModel().getSelectedItem();
 	
 	CreateNewTable.setVisible(true);
 	CreateTableController.Database = database.getValue();
 	
	   }
	   catch(Exception ex)
	   {
		   
	   }
   };
   
   
 
  ListOfDatabase.addEventHandler(MouseEvent.MOUSE_CLICKED, click);
  ListOfDatabase.addEventHandler(MouseEvent.MOUSE_CLICKED,databaseSelected);
   
   
  
    }

	private void handleMouseClicked(MouseEvent event) throws IOException {
		
		String name="";
		String db="";
	   try {
	      name = (String) ((TreeItem<String>)ListOfDatabase.getSelectionModel().getSelectedItem()).getValue();
	      db = (String) ListOfDatabase.getSelectionModel().getSelectedItem().getParent().getValue();
	      
	   }
	   catch( NullPointerException npe)
	   {
		   
	   }catch(Exception e) {
		   e.printStackTrace();
	   }
	       if(name.length() > 1)
	       {
	    	   
	    	   DatabaseContents.getColumns().clear();
	    	   DatabaseContents.getItems().clear();
	    	   Properties.setText(getPropeerties(name,db));
	    	   PopulateTableview(name,db);
	       }
	  
	}
	private String getPropeerties(String name,String db) {
		
		return "dshjgflh;asjkdlfjhaskdlgfjksad"
				+ "\n fldskahf;askldjfh;asgkdljfgsdalkf"
				+ "\ndhslfdsakfh;asklfghskdlfgshdjklfhjgsadhfkjadsf"
				+ "\nadsklgjfhksljdahgfhsdkljahjfsd";
	}
	private void PopulateTableview(String table,String Database) throws IOException 
	{
		count = 0;
		final ObservableList data = FXCollections.observableArrayList();
		
		   sideVBox.getChildren().clear();
		
		Repository repo = new Repository();
		Entity[] e = repo.getAllEntities(Database,table);
		try {
		HashMap<String,Double> doubles = (HashMap<String,Double>) e[0].getDoubleFields();
		HashMap<String,Integer> ints = (HashMap<String,Integer>) e[0].getIntegerFields();
		HashMap<String,String> strings = (HashMap<String,String>) e[0].getStringFields();
		for(int i =0;i<e.length;i++)
			data.add(e[i]);
		
		
		DatabaseContents.setEditable(true);
		DatabaseContents.setItems(data);
		int i  = 0;
		  Object[] doublekeys =  doubles.keySet().toArray();
	      Object[] intkeys =  ints.keySet().toArray();
	     Object[]  StringKeys = strings.keySet().toArray();
	   
	   for( i = 0;i<StringKeys.length;i++)
	    {
		   Label label= new Label(StringKeys[i].toString());
		   TextField tf = new TextField();
		   textf[count++] = tf;
	    	TableColumn<Entity, String> tc = getCellDataFactoryImplementedforEntitiesString(StringKeys,i);
	    	final int j = i;
	    	tc.setCellFactory(TextFieldTableCell.forTableColumn());
	    	tc.setOnEditCommit(new EventHandler<CellEditEvent<Entity,String>>(){

			@Override
			public void handle(CellEditEvent<Entity, String> event) {
				Entity   entity = event.getRowValue();
				
				
			//String value = 	entity.getStringFields().get(StringKeys[j]);
			entity.getStringFields().put(StringKeys[j].toString(), event.getNewValue());
			
			}
	    	}
	    	
	    	
				);
	    	Region r = new Region();
	    	r.setMinWidth((25-label.getText().length()*2));
	    	sideVBox.getChildren().addAll(new HBox(label,r,tf));
	    	
	    	if(tc!=null)
	    	DatabaseContents.getColumns().add(tc);
	    	
	    }
	   for(i = 0;i<doublekeys.length;i++)
	   {
		   final int j = i;
		   TableColumn<Entity,Double> tc = getCellDataFactoryImplementedforEntitiesDouble(doublekeys,i);
		   Label label= new Label(doublekeys[i].toString());
		   TextField tf = new TextField();
		   textf[count++] = tf;
		   tc.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
	    	tc.setOnEditCommit(new EventHandler<CellEditEvent<Entity,Double>>(){

			@Override
			public void handle(CellEditEvent<Entity, Double> event) {
				Entity   entity = event.getRowValue();
				
				
			//String value = 	entity.getStringFields().get(StringKeys[j]);
			entity.getDoubleFields().put(doublekeys[j].toString(), event.getNewValue());
			}
	    	}
	    	
				);
	    	Region r = new Region();
	    	r.setMinWidth((25-label.getText().length()*2));
		   DatabaseContents.getColumns().add(tc);
		   sideVBox.getChildren().addAll(new HBox(label,r,tf), new Region());
		   
	   }
	
	   Button B  = new Button("Add");
	   sideVBox.getChildren().add(B);
	   B.setOnAction(event -> {
		
		Entity e1 = new Entity();
		e1.setTitle(table);
	for(int i1 =0;i1<count;i1++)
	{
		if(i1 < StringKeys.length)
		{
			e1.getStringFields().put(StringKeys[i1].toString(), textf[i1].getText());
		}
		else
		{
			e1.getDoubleFields().put(doublekeys[i1-StringKeys.length].toString(), Double.parseDouble(textf[i1].getText()));
		}
		textf[i1].clear();
	}
	
	DatabaseContents.getItems().add(e1);
	try {
		setHistory(Database,"Added new Items at" + LocalDateTime.now());
	} catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	});
	   sideVBox.setPrefHeight(10);
	   
		}
		catch(Exception exception) {
			
			exception.printStackTrace();
		}
	}
	
	private TableColumn<Entity, String> getCellDataFactoryImplementedforEntitiesString(Object[] doublekeys, int i) {
	
		
		TableColumn<Entity,String> tc = new TableColumn(doublekeys[i].toString());
		Callback<CellDataFeatures<Entity, String>, ObservableValue<String>>  cb =  new Callback<CellDataFeatures<Entity,String>,ObservableValue<String>> (){

			@Override
			public ObservableValue<String> call(CellDataFeatures<Entity, String> param) {
				
				try {
				return new ReadOnlyObjectWrapper<>(param.getValue().getStringFields().get(doublekeys[i]).toString());
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				return null;
			}};
		tc.setCellValueFactory(cb);
		return tc;
	}
	private TableColumn<Entity, Double> getCellDataFactoryImplementedforEntitiesDouble(Object[] doublekeys, int i) {
		TableColumn<Entity,Double> tc = new TableColumn(doublekeys[i].toString());
		Callback<CellDataFeatures<Entity, Double>, ObservableValue<Double>>  cb =  new Callback<CellDataFeatures<Entity,Double>,ObservableValue<Double>> (){

			@Override
			public ObservableValue<Double> call(CellDataFeatures<Entity, Double> param) {
				
				try {
				return new ReadOnlyObjectWrapper<>(param.getValue().getDoubleFields().get(doublekeys[i]));
				}
				catch(Exception e)
				{	e.printStackTrace();
				}
				return null;
			}};
		tc.setCellValueFactory(cb);
		return tc;
	}
	
	public TableView<Entity> getDatabaseContents() {
		
		return DatabaseContents;
	}
	public void setDatabaseContents(TableView<Entity> databaseContents) {
		DatabaseContents = databaseContents;
	}
	static void setScene(MainController obj) throws IOException
	{
		
		obj.initialize();
		HelloApplication.pStage.setScene(HelloApplication.pScene);
		
	       
	}
	void setHistory(String table, String data) throws IOException
	{
		Repository repo=new Repository();
		repo.setHistory(table, data);
	}
}
