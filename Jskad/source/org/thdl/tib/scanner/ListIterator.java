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

public class ListIterator
{
	Link current;
	
	public ListIterator(Link current)
	{
		this.current=current;
	}
	
	public ListIterator(Link current, int n)
	{
		this(current);
		int i;
		for (i=0; i<n; i++) current=current.next();
	}	
	
	public boolean hasNext()
	{
		return current!=null;
	}
	
	public Object next()
	{
		Object o = current.get();
		current = current.next();
		return o;
	}
}