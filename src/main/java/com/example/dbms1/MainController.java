package com.example.dbms1;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import org.apache.poi.xssf.usermodel.XSSFCell;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
import utils.BinaryTree;

public class MainController {

	private static Alert alert;
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
	private AnchorPane sidepane;
	@FXML
	 private Text Properties;
	@FXML
	 private HBox sidehbox;
	@FXML
	 private VBox sideVBox;
	boolean isAscending = false;
	
	ToggleGroup togglegroup = new ToggleGroup();
	VBox vb2 =new VBox(25);
	
	 private TextField [] textf = new TextField[25];
	 private int count=0;
	
	 private TreeItem<String> database;
	 private BinaryTree<Entity>[] btStrings ;
	 private BinaryTree<Entity>[] btDoubles ;
	 Object [] StringKeys;
	 Object [] doublekeys;
	
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
			 System.out.println("Saving...");
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
		
		initializer();
   
   
  
    }
	private void initializer() throws IOException {
		
		Repository repo = new Repository();
		Entity[] e  = repo.GetAllTables("Alldbs");
      TreeItem<String> root = new TreeItem<String>("All Databases");
      CreateNewTable.setVisible(false);
      EventHandler<MouseEvent> Listclick = handleMouseEventExceptionhandled();
     ListofDatabasePopulated(repo, e, root);
   ListOfDatabase.setRoot(root);
   EventHandler<MouseEvent> databaseSelected = (MouseEvent event) ->
   {
	   handleDatabaseSelected();
   };
   
 
  ListOfDatabase.addEventHandler(MouseEvent.MOUSE_CLICKED, Listclick);
  ListOfDatabase.addEventHandler(MouseEvent.MOUSE_CLICKED,databaseSelected);
	}
	private void handleDatabaseSelected() {
		try{
		database =  ListOfDatabase.getSelectionModel().getSelectedItem();
		
		CreateNewTable.setVisible(true);
		CreateTableController.Database = database.getValue();
		
		   }
		   catch(Exception ex)
		   {
			   
		   }
	}
	private void ListofDatabasePopulated(Repository repo, Entity[] e, TreeItem<String> root) {
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
	}
	private EventHandler<MouseEvent> handleMouseEventExceptionhandled() {
		EventHandler<MouseEvent> click = (MouseEvent event) ->{
			  try {
				handleMouseClicked(event);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  };
		return click;
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
		
		return " ";
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
		
		HashMap<String,String> strings = (HashMap<String,String>) e[0].getStringFields();
 Object[] doublekeys =  doubles.keySet().toArray();
	     
	     Object[]  StringKeys = strings.keySet().toArray();
	     
	     this.StringKeys = StringKeys;
	     this.doublekeys = doublekeys;
	     btDoubles = new BinaryTree[doubles.size()];
	     btStrings = new BinaryTree[strings.size()];
		for(int i =0;i<doubles.size();i++)  btDoubles[i] = repo.getBinaryTree(doublekeys[i].toString(), Database, table, 0);
	    for(int i = 0;i<strings.size();i++) btStrings[i] = repo.getBinaryTree(StringKeys[i].toString(), Database, table, 1);
		
		for(int i =0;i<e.length;i++)
			data.add(e[i]);
		
		
		DatabaseContents.setEditable(true);
		DatabaseContents.setItems(data);
	
		 
	   
	   stringfieldsAdded(StringKeys);
	   doubleFieldsAdded(doublekeys);
	
	   Button B  = new Button("Add");
	   sideVBox.getChildren().add(B);
	   B.setOnAction(event -> {
		
		try {
			itemadded(table, Database, doublekeys, StringKeys);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	});
	   sideVBox.setPrefHeight(10);
	   
		}
		catch(Exception exception) {
			
			exception.printStackTrace();
		}
	}
	private void itemadded(String table, String Database, Object[] doublekeys, Object[] StringKeys)
			throws NumberFormatException, IllegalAccessException, IOException {
		Entity e1 = new Entity();
		e1.setTitle(table);
	addEntityImplemented(doublekeys, StringKeys, e1);
	
	DatabaseContents.getItems().add(e1);
	historysetted(Database);
	}
	private void historysetted(String Database) {
		try {
			setHistory(Database,"Added new Items at" + LocalDateTime.now());
		} catch (IOException e2) {
			
			e2.printStackTrace();
		}
	}
	private void addEntityImplemented(Object[] doublekeys, Object[] StringKeys, Entity e1)
			throws NumberFormatException, IllegalAccessException, IOException {
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
			Save();
		}
	}
	private void doubleFieldsAdded(Object[] doublekeys) {
		int i;
		for(i = 0;i<doublekeys.length;i++)
		   {
			   final int j = i;
			   TableColumn<Entity,Double> tc = getCellDataFactoryImplementedforEntitiesDouble(doublekeys,i);
			   tc.setSortable(false);
			   Label label= new Label(doublekeys[i].toString());
			   TextField tf = new TextField();
			   textf[count++] = tf;
			   tc.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		    	tc.setOnEditCommit(new EventHandler<CellEditEvent<Entity,Double>>(){

				@Override
				public void handle(CellEditEvent<Entity, Double> event) {
					Entity   entity = event.getRowValue();
					
					entity.getDoubleFields().put(doublekeys[j].toString(), event.getNewValue());
				
				}
		    	}
		    	
					);
		    	Region r = new Region();
		    	r.setMinWidth((25-label.getText().length()*2));
			   DatabaseContents.getColumns().add(tc);
			   sideVBox.getChildren().addAll(new HBox(label,r,tf), new Region());
			   
		   }
	}
	private void stringfieldsAdded(Object[] StringKeys) {
		int i;
		for( i = 0;i<StringKeys.length;i++)
		    {
			
			   Label label= new Label(StringKeys[i].toString());
			   TextField tf = new TextField();
			   textf[count++] = tf;
		    	TableColumn<Entity, String> tc = getCellDataFactoryImplementedforEntitiesString(StringKeys,i);
		    	final int j = i;
		    	tc.setCellFactory(TextFieldTableCell.forTableColumn());
		    	tc.setSortable(false);
		 
		    	tc.setOnEditCommit(new EventHandler<CellEditEvent<Entity,String>>(){

				@Override
				public void handle(CellEditEvent<Entity, String> event) {
					Entity   entity = event.getRowValue();
					
					
				
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
	}
	
	private TableColumn<Entity, String> getCellDataFactoryImplementedforEntitiesString(Object[] doublekeys, int i) {
	
		
		Label l =  new Label(doublekeys[i].toString());
		TableColumn<Entity,String> tc = new TableColumn<>();
		tc.setGraphic(l);
		
		l.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler() {

			@Override
			public void handle(Event event) {
						tableSorter(doublekeys[i].toString());
			}
			
		});
		
		
		
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
		
		Label l =  new Label(doublekeys[i].toString());
		TableColumn<Entity,Double> tc = new TableColumn<>();
		tc.setGraphic(l);
		
		l.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler() {

			@Override
			public void handle(Event event) {
						tableSorter(doublekeys[i].toString());
			}
			
		});
		
		
		
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
    void tableSorter(String field)
    {
    	
    	Integer []arr = new Integer[10];
    	for(int i =0;i<10;i++)
    	{
    		Random r = new Random();
    	arr[i] = 	r.nextInt(0, 10);
    		System.out.println(arr[i]);
    	}
    	
    	
    	Object [] e  = DatabaseContents.getItems().toArray();
    	if(!isAscending)
    	{
    		BinaryTree.MergeSort(e, new Comparator<Entity>() {

			@Override
			public int compare(Entity o1, Entity o2) {
				try {
			return	o1.getDoubleFields().get(field).compareTo(o2.getDoubleFields().get(field)) ;
				
				}catch(Exception ex)
				{
					return o1.getStringFields().get(field).compareToIgnoreCase(o2.getStringFields().get(field));
				}
				
				
			}});
    		isAscending = true;
    	}
    	else
    	{
    		BinaryTree.MergeSort(e, new Comparator<Entity>() {

    			@Override
    			public int compare(Entity o1, Entity o2) {
    				try {
    			return	-1*(o1.getDoubleFields().get(field).compareTo(o2.getDoubleFields().get(field))) ;
    				
    				}catch(Exception ex)
    				{
    					return -1*(o1.getStringFields().get(field).compareToIgnoreCase(o2.getStringFields().get(field)));
    				}
    				
    				
    			}});
    		isAscending = false;
    	}
     
    	DatabaseContents.getItems().clear();
    	for(int i =0;i< e.length;i++)
    	{
    		DatabaseContents.getItems().add((Entity)e[i]);
    		
    	
    	}
    	
    	
    	
    	
    	
    }
    @FXML
    void unSearch() {}
    @FXML
    void Search()
    {
    	sideVBox.setSpacing(15);
    	vb2.getChildren().clear();
    	
    	if(sidepane.getChildren().contains(vb2))
    	sidepane.getChildren().remove(vb2);
    	
    	sidepane.getChildren().add(vb2);
    	
    	String field = "field";
    	TextField tf = new TextField();
    	tf.setPromptText("Enter "+ field+" Value");
vb2.getChildren().addAll( tf);
    RadioButton [] cb = new RadioButton[doublekeys.length + StringKeys.length];
    
    
    	
   
    for(int i = 0;i<cb.length ;i++ )
    {
    	
    	cb[i] = new RadioButton();
    	if(i< doublekeys.length)
    	cb[i].setText(doublekeys[i].toString());
    	else
    	{
    		cb[i].setText(StringKeys[i-doublekeys.length].toString());
    		
    		
    	}
    	cb[i].setToggleGroup(togglegroup);
    	cb[i].setVisible(true);
    	vb2.getChildren().add(cb[i]);
    }
    
    	
    Button b = new Button("Search");
    vb2.getChildren().add(b);
    b.setOnAction(event->{
   RadioButton rb =  	(RadioButton) togglegroup.getSelectedToggle();
   
    Entity e =	SeparateFieldandgetEntity(tf.getText(),rb.getText());
    	
   
    	
    });
    	
    	
    	
    }
    
  Entity  SeparateFieldandgetEntity(String name, String Field)
  {
	 boolean isDouble = false ;
	//  BinaryTree<Entity> bt = null;
	  for(int i = 0;i< StringKeys.length;i++)
	  {
		  if(Field.equalsIgnoreCase(StringKeys[i].toString())) 
		  {
			 // bt = btStrings[i];
			 isDouble = false;
			  break;
		  }
		  
		  
	  }
	  for(int i = 0;i< doublekeys.length;i++)
	  {
		  if(Field.equalsIgnoreCase(doublekeys[i].toString())) 
		  {
			
			  isDouble = true;
			  break;
		  }
		  
		  
	  }
	//  if(bt!=null)
	return SearchfromTable(name,Field,isDouble);
	 // return null;
	  
  }
  Entity SearchfromTable(String name,String Field, boolean isDouble)
  {
	  if(isDouble)
	  {
		//  BinaryTree.Node node = bt.getRoot();
		 
		  int i = -1;
			Iterator <Entity>it =  DatabaseContents.getItems().iterator() ;
			while(it.hasNext())
			{
				Entity e = it.next();
				i++;
				if(e.getDoubleFields().get(Field) == Double.parseDouble(name))
				{
					DatabaseContents.getSelectionModel().clearAndSelect(i);
					
					return e;
				}
				
			}
		  
	    return null;
	  
	  }
	  else
	  {
		  int i = -1;
			Iterator <Entity>it =  DatabaseContents.getItems().iterator() ;
			while(it.hasNext())
			{
				Entity e = it.next();
				i++;
				if(e.getStringFields().get(Field).compareTo(name) == 0)
				{
					DatabaseContents.getSelectionModel().clearAndSelect(i);
					return e;
				}
				
			}
	    
	  }
	 return null;
	  
  }
  
    


}
