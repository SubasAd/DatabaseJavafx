package utils;


import java.util.Comparator;
import java.util.LinkedList;

public class BinaryTree<T> {
	int size;
	Comparator Compara;
	 public class Node
	{
		
		Node Left;
		Node Right;
		Object data;
	public	Node getRight()
		{
			return Right;
		}
	public Node getLeft()
	{
		return Left;
	}
		
	public Object getData()
	{
		return data;
	}
	}
	Node root;
	LinkedList ll = new LinkedList();
   
    
  public void Insert(T data, Comparator<T> c)
   {
	  Compara = c;
	if(root == null)
	{ root = new Node();
		root.data = data;
		size++;
		ll.add(root);
		return;
		
	}
	seekPos(data, c, root).data = data;
	
   size++;
   
   
   }
 
   int getParent(int pos)
  {
	  return (int) Math.ceil(pos/2-1);
  }
  int getRight(int pos)
  {
	  return pos*2+2;
  }
  int getLeft(int pos)
  {
	  return pos*2+1;
  }
  @SuppressWarnings("unchecked")
Node seekPos(T data,Comparator c, Node root)

  {
	 
	  
		  if(c.compare(data,root.data)<0)
		  {
			  
			  if(root.Left == null)
			  {
				  root.Left = new Node();
				  ll.add(root.Left);
				  return root.Left;
			  }
			  
			  return seekPos(data,c,root.Left);
		  }
		  else
		  {
			  
			  if(root.Right == null)
			  {
				  root.Right = new Node();
				  ll.add(root.Right);
				  return root.Right;
			  }
			  return seekPos(data,c,root.Right);
		  }
	 
	
	
		 
  }
  
  
  
  
  
  
  
 public Object[]  toArray()
  { 
	  return ll.toArray();
  }
 
 
 public Comparator getComparator()
 {
	 return Compara;
 }
 
 public Node getRoot()
 {
	 return root;
 }
 
 
 
 	
 static public void MergeSort(Object[] obj,  Comparator c)
  {
	  if(obj.length < 2)
	  {
		  return ;
	  }
	 
	  int mid = obj.length /2;
	  Object[] l = new Object[mid];
	  Object [] r = new Object[obj.length -mid];
	  
	  
	  for(int i =0;i<mid;i++)
	  {
		  l[i] = obj[i];
	  }
	  for(int i = mid;i<obj.length;i++)
	  {
		  r[i-mid] = obj[i];
	  }
	  
	 
	  
	  
	  MergeSort(l,c);
	  MergeSort(r, c);
 	  Merge(obj,l,r,c);
	  
  }
 @SuppressWarnings({ "rawtypes", "unchecked" })
static  void Merge( Object[] obj ,Object[] left,Object[] right ,Comparator c )
  {
	
	
	
	 
	 int i =0,j=0,k=0;
	 while(i < left.length && j < right.length)
	 {
		 if(c.compare(left[i], right[j]) <0)
		 {
			 obj[k] = left[i];
			 k++;i++;
		 }
		 else
		 {
			 obj[k++]=right[j++];
		 }
	 }
	 while(i<left.length)
		  obj[k++] = left[i++];
	 
	 while(j<right.length)
		 obj[k++] = right[j++];
	 
	 
	
	 
	
  }
 
 
 
 
 
 
}
