package org.thdl.roster.components;

public class ConditionalInsert extends org.apache.tapestry.BaseComponent
{
//attributes
	private String value;
//accessors
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public boolean isDisplayWorthy()
	{
		return ( getValue() != null  && ! java.util.regex.Pattern.matches( "\\s*", getValue() )  );
	}
}

