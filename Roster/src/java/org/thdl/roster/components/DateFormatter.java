package org.thdl.roster.components;

import org.apache.tapestry.*;
import java.text.*;
import java.util.*;

public class DateFormatter extends BaseComponent
{
//attributes
    private Date date;
//accessors
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
//helpers
	public String getFormattedDate()
	{
		String date = null;
		if (null != getDate() )
			date = DateFormat.getDateInstance().format( getDate() ); 
		return date;
	}
}