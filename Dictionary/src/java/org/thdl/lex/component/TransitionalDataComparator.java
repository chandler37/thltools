/*
 * Created on Feb 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.thdl.lex.component;

import java.util.Comparator;

/**
 * @author Anoop
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TransitionalDataComparator implements Comparator {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		TransitionalData td1 = (TransitionalData) o1;
		TransitionalData td2 = (TransitionalData) o2;
		
		return td1.getTransitionalDataLabel().getPriority().compareTo(td2.getTransitionalDataLabel().getPriority());
	}

}
