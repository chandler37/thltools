package org.thdl.roster.components;

public class AbbreviatedInsert extends org.apache.tapestry.BaseComponent
{
//attributes
	private String value;
	private Integer characterCount;
	private boolean abbreviated;
	//accessors
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setCharacterCount(Integer characterCount) {
		this.characterCount = characterCount;
	}
	
	public Integer getCharacterCount() {
		return characterCount;
	}

	public void setAbbreviated(boolean abbreviated) {
		this.abbreviated = abbreviated;
	}

	public boolean getAbbreviated() {
		return abbreviated;
	}
	
	public String getAbbreviatedValue()
	{
		String value = getValue();
		int count = getCharacterCount().intValue();
		if ( null != value && value.length() > count )
		{
			value = value.substring( 0, count ) + "....";
			setAbbreviated( true );
		}
		else
		{
			setAbbreviated( false );
		}
		return value;
	}
}

