package com.example.dbms1;

import java.lang.reflect.Field;
import java.util.Comparator;

public class Sorter<T> implements Comparator<T> {

	Field field;
	Sorter( )
	{
		this.field = field;
	}
	@Override
	public int compare(T o1, T o2) {
		try {
			if(field.getType() == String.class) return field.get(o1).toString().compareTo(field.get(o2).toString());
		 return  -1*((int)field.get(o2) - (int)field.get(o1));
			
		
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
