package com.example.dbms1;

  import java.io.*;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utils.BinaryTree;
public class Repository   {
  private File file;
  private FileInputStream fip;
  private XSSFWorkbook workbook;
    private void Filetobecreated(String db) throws IOException {
      
    	   workbook = new XSSFWorkbook();
    	   OutputStream fileOut = new FileOutputStream(db+ ".xlsx");
    	   
    	   workbook.write(fileOut);
         
    	  
       
       


    }

    private boolean FileNotexisted(String db) throws IOException {
    	try {
        file = new File(db +".xlsx");
        fip = new FileInputStream(file);
    workbook = new XSSFWorkbook(fip);

    	}
    	catch(Exception e)
    	{
    		if(fip!= null) fip.close();
    		
    		
    		e.printStackTrace();
    	}
        return !file.isFile() || !file.exists();
    }

    public  boolean Save(Entity obj, String db) throws IOException, IllegalAccessException {
    	
    	HashMap<String, Double> doubles =(HashMap<String, Double>) obj.getDoubleFields();
    
    	HashMap<String, String> strings  = (HashMap<String,String>) obj.getStringFields();
    	
    
     if(FileNotexisted(db)) {
    	 
    	 Filetobecreated(db);
    	 Repository ripo = new Repository();
    	 Entity e = new Entity();
    	 e.setTitle(db);
    	 ripo.Save(e, "Alldbs");
     
     }
      XSSFSheet table = workbook.getSheet(obj.getTitle())== null?workbook.createSheet(obj.getTitle()):workbook.getSheet(obj.getTitle());
       XSSFRow HeaderRow =  table.createRow(0);
      Object[] doublekeys =  doubles.keySet().toArray();
    
     Object[]  StringKeys = strings.keySet().toArray();
       int i;
     for( i = 0;i< doubles.size();i++)
     {
       XSSFCell  cell=  HeaderRow.createCell(i);
       cell.setCellValue(doublekeys[i].toString());
     }
    
     for(;i-doubles.size() < strings.size();i++)
     {
    	 XSSFCell  cell=  HeaderRow.createCell(i);
         cell.setCellValue(StringKeys[i-doubles.size()].toString());
     }
       XSSFRow objRow =  table.createRow(table.getLastRowNum()+1);
       
       
       for( i = 0;i< doubles.size();i++)
       {
         XSSFCell  cell=  objRow.createCell(i);
         cell.setCellValue(doubles.get(doublekeys[i]));
       }
      
       for(;i-doubles.size() < strings.size();i++)
       {
    	   try {
      	 XSSFCell  cell=  objRow.createCell(i);
           cell.setCellValue(strings.get(StringKeys[i-doubles.size()]));
       }
       catch(Exception exception)
       {
    	     exception.printStackTrace();
       }
       }
       fip.close();
       FileOutputStream fos = new FileOutputStream(file);
       workbook.write(fos);
       fos.close();
       return true;






  }
    Entity getObject(String field, Object data, String db,String Table) throws IOException, IllegalAccessException, NoSuchFieldException {
          if(FileNotexisted(db))
          {
              System.out.println("Error the file doesn't exist");
              return null;

          }

      XSSFSheet table = workbook.getSheet(Table);
     
    int n = 0;
for(int i = 0;i< table.getRow(0).getLastCellNum();i++) if(field.equals(table.getRow(0).getCell(i).getStringCellValue()))	 n=i;

    
    for(int i = 1;i<=table.getLastRowNum() ;i++)
    {
    	
    	
    	XSSFCell cell =table.getRow(i).getCell(n);
    	
    	try {
    	if(data.equals(cell.getNumericCellValue())) return returnObjectByRow(cell.getRow(),table.getRow(0),Table);
    	if(data.equals( cell.getStringCellValue()))  return returnObjectByRow(cell.getRow(),table.getRow(0),Table);
    	}
    	catch(Exception e) {}
    	
    }
      
     return null; 
  }

    private Entity returnObjectByRow(XSSFRow row, XSSFRow head, String Table) {
	Entity e = new Entity();
	e.setTitle(Table);
	for(int  i = 0;i< head.getLastCellNum(); i++)
	{
		try {
			if(row.getCell(i).getCellType() == CellType.NUMERIC) e.getDoubleFields().put(head.getCell(i).getStringCellValue(), row.getCell(i).getNumericCellValue());
		if(row.getCell(i).getCellType() == CellType.STRING) e.getStringFields().put(head.getCell(i).getStringCellValue(), row.getCell(i).getStringCellValue());
		}catch(Exception ex)
		{
			
		}
		
	}
	
	return e;
}
   
    void Overwrite(Entity[] e,String db ) throws IOException, IllegalAccessException
    {
    	if(FileNotexisted(db)) {
    		
    	}
   
	 int x=   workbook.getSheetIndex(e[0].getTitle());
	workbook.removeSheetAt(x);
	 
     FileOutputStream fos = new FileOutputStream(file);
     workbook.write(fos);
	fos.close();
	 for(Entity ex:e)
		Save(ex,db);
    	
    	
    }
  Entity[]   GetAllTables(String db) throws IOException
     {
	 
	  if(FileNotexisted(db))
	  {
		  System.out.println("File Doesn't Exists");
		  return null;
	  }
	  Entity[] e= new Entity[workbook.getNumberOfSheets()];
	  for(int i = 0;i<workbook.getNumberOfSheets();i++)
	  {
		  e[i] = new Entity();
		e[i].setTitle(  workbook.getSheetAt(i).getSheetName());
	  }
		return e;
    	 
     }
  Entity[] getAllEntities(String db,String Title) throws IOException
  {
	  if(FileNotexisted(db))
	  {
		  System.out.println("File Doesn't Exists");
		  return null;
	  }
	 XSSFSheet  sheet =  workbook.getSheet(Title);
	 Entity [] e = new Entity[sheet.getLastRowNum()];
	 
	 for(int i =0;i<e.length;i++)
	 {
		 e[i] = returnObjectByRow(sheet.getRow(i+1),sheet.getRow(0),Title);
		
	 }   
	return e;
	  
  }
  void setHistory(String table, String data) throws IOException
  {
	  if(FileNotexisted("Alldbs")){}
 XSSFSheet sheet =	  workbook.getSheet(table);
 XSSFRow row = sheet.getRow(sheet.getLastRowNum());
 XSSFCell cell = row.getCell(0);
 cell.setCellValue(data);
 
 OutputStream fileOut = new FileOutputStream("Alldbs"+ ".xlsx");
 
 workbook.write(fileOut);
	  fileOut.close();
  }
   BinaryTree<Entity> getBinaryTree(String key,String db,String Title, int code ) throws IOException
   {
	   if(FileNotexisted(db))
		  {
			  System.out.println("File Doesn't Exists");
			  return null;
		  }
	   
	   XSSFSheet  sheet =  workbook.getSheet(Title);
	   BinaryTree<Entity> bt = new BinaryTree();
	for(int i =0;i<sheet.getLastRowNum();i++)
	{
		bt.Insert(returnObjectByRow(sheet.getRow(i+1),sheet.getRow(0),Title), new Comparator<Entity>() {

			@Override
			public int compare(Entity o1, Entity o2) {
				
				if(code == 0)
				{
					return  ( o1.getDoubleFields().get(key).compareTo( o2.getDoubleFields().get(key)));
				}
				else return o1.getStringFields().get(key).compareToIgnoreCase(o2.getStringFields().get(key));
			}});
	}
	   
	 return bt;  
	   
   }
}
