package org.thdl.roster.om;

public interface RosterMember extends org.apache.torque.om.Persistent
{
public void setMemberData( RosterMemberData memberData ) throws RosterMemberTypeException;
public RosterMemberData getMemberData() throws RosterMemberTypeException;
}
