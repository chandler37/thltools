package org.thdl.roster.om;

public interface RosterMergeData
{
	public Object getByPosition( int pos );
	public org.apache.torque.om.ObjectKey getPrimaryKey();
	public void setByPosition( int position, Integer data ) throws java.lang.Exception;
	public void save() throws java.lang.Exception;
	public void remove() throws java.lang.Exception;
}
