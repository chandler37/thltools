package org.thdl.roster.components;

import org.apache.tapestry.*;
import java.util.*;

public class Century extends BaseComponent
{
//attributes
    private HashMap centuries;
	 private Integer century;
//accessors
	public void setCentury(Integer century) {
		this.century = century;
	}
	public Integer getCentury() {
		return century;
	}
	private void setCenturies(HashMap centuries) {
		this.centuries = centuries;
	}
	private HashMap getCenturies() {
		return centuries;
	}
//synthetic attribute accessors
	public String getCenturyText()
	{
		String cent = (String)getCenturies().get( getCentury() );
		return cent;
	}
//helper
//constructors
	public Century()
	{
		HashMap map = new HashMap();
		String centuries[] = {"twenty-first", "twentieth", "nineteenth", "eighteenth", "seventeenth", "sixteenth", "fifteenth", "fourteenth", "thirteenth", "twelfth", "eleventh", "tenth", "ninth", "eighth", "seventh", "pre-seventh"};
		int centIntegers[] = {21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 0};
		for ( int i = 0; i < centuries.length; i++ )
		{
			map.put( new Integer( centIntegers[i] ), centuries[i] );
		}
		setCenturies( map );
	}
}