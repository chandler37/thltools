/*
The contents of this file are subject to the AMP Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the AMP web site 
(http://www.tibet.iteso.mx/Guatemala/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is Andres Montano Pellegrini. Portions
created by Andres Montano Pellegrini are Copyright 2001 Andres Montano
Pellegrini. All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.scanner;
import java.util.*;

public class LinkedList
{	
	protected Link cabeza;
	public LinkedList()
	{
		cabeza=null;
	}
	
	private LinkedList(Link cabeza)
	{
		this.cabeza = cabeza;
	}
	
	public void addLast(Object o)
	{
		if (cabeza==null)
		{
			cabeza = new Link(o);
		}
		else cabeza = cabeza.createPrevious(o);
	}
	
	public Object getLast()
	{
	    if (cabeza==null) return null;
		else return cabeza.get();
	}

	public ListIterator listIterator()
	{
		return new ListIterator(cabeza);
	}	

	public ListIterator listIterator(int n)
	{
		return new ListIterator(cabeza, n);
	}
	
	public boolean isEmpty()
	{
		return cabeza==null;
	}
	
	public boolean contains(Object o)
	{
		Link current=cabeza;
		while (current!=null)
		{
			if (current.get().equals(o)) return true;
			current = current.next();
		}
		return false;
	}
	
	public int size()
	{
		if (cabeza==null) return 0;
		return cabeza.size();
	}
	
	public LinkedList sort()
	{
		return new LinkedList(cabeza.sort());
	}
	
	public Object[] toArray()
	{
		int n = size();
		if (n==0) return null;
		Object array[] = new Object[n];
		ListIterator li = listIterator();
		while (li.hasNext())
		{
			n--;
			array[n] = li.next();
		}
		return array;
	}
	
	public String[] toStringArray()
	{
		int n = size();
		if (n==0) return null;
		Object o;
		String array[] = new String[n];
		ListIterator li = listIterator();
		while (li.hasNext())
		{
			n--;
			o = li.next();
			if (o==null) array[n]=null;
			else array[n] = o.toString();
		}
		return array;		
	}
}
