package org.thdl.roster.om;

public interface RosterMemberData extends org.apache.torque.om.Persistent
{
	public Integer getId() throws org.apache.torque.TorqueException;
	public java.util.List getMemberTypes() throws org.apache.torque.TorqueException;
	public String getDescription();
	public void setMemberTypes( java.util.List memberTypes );
	public void save() throws java.lang.Exception;
	public String getName() throws java.lang.Exception;
}
