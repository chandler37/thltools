
package org.thdl.roster.om;


import org.apache.torque.om.Persistent;
import org.apache.torque.TorqueException;

/** 
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Thu Feb 27 15:11:05 EST 2003]
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public  class ResearchInterestDiscipline 
    extends org.thdl.roster.om.BaseResearchInterestDiscipline
    implements java.io.Serializable, Persistent, RosterMergeData
{
		public void setByPosition( int position, Integer data ) throws TorqueException
		{
			if ( position == 1 )
			{
				setId( data );
			}
			if ( position == 2 )
			{
				setDisciplineId( data );
			}
			if ( position == 3 )
			{
				setResearchInterestId( data );
			}
			if ( position == 4 )
			{
				setRelevance( data );
			}
		}		
		public void remove() throws TorqueException
		{
			getPeer().doDelete( this );
		}
}
