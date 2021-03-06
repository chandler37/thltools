package org.thdl.roster.om.map;

import java.util.Date;
import java.math.BigDecimal;

import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import org.apache.torque.map.MapBuilder;
import org.apache.torque.map.DatabaseMap;
import org.apache.torque.map.TableMap;

/**
  *  This class was autogenerated by Torque on:
  *
  * [Wed May 14 19:01:42 EDT 2003]
  *
  */
public class MemberMapBuilder implements MapBuilder
{
    /**
     * The name of this class
     */
    public static final String CLASS_NAME =
        "org.thdl.roster.om.map.MemberMapBuilder";


    /**
     * The database map.
     */
    private DatabaseMap dbMap = null;

    /**
     * Tells us if this DatabaseMapBuilder is built so that we
     * don't have to re-build it every time.
     *
     * @return true if this DatabaseMapBuilder is built
     */
    public boolean isBuilt()
    {
        if (dbMap != null)
        {
            return true;
        }
        return false;
    }

    /**
     * Gets the databasemap this map builder built.
     *
     * @return the databasemap
     */
    public DatabaseMap getDatabaseMap()
    {
        return this.dbMap;
    }

    /**
     * The doBuild() method builds the DatabaseMap
     *
     * @throws TorqueException
     */
    public void doBuild() throws TorqueException
    {
        dbMap = Torque.getDatabaseMap("Roster");

        dbMap.addTable("Member");
        TableMap tMap = dbMap.getTable("Member");

                tMap.setPrimaryKeyMethod(TableMap.ID_BROKER);
        
                tMap.setPrimaryKeyMethodInfo(tMap.getName());
        
                                      tMap.addPrimaryKey("Member.ID", new Integer(0));
                                                        tMap.addColumn("Member.CREATED_BY", new Integer(0));
                                                        tMap.addColumn("Member.MODIFIED_BY", new Integer(0));
                                                        tMap.addColumn("Member.CREATED_ON", new Date());
                                                        tMap.addColumn("Member.MODIFIED_ON", new Date());
                                                        tMap.addColumn("Member.DELETED", new String());
                                                        tMap.addForeignKey(
                "Member.CONTACT_INFO_ID", new Integer(0) , "ContactInfo" ,
                    "id");
                                                        tMap.addForeignKey(
                "Member.RESEARCH_INTEREST_ID", new Integer(0) , "ResearchInterest" ,
                    "id");
                                                        tMap.addForeignKey(
                "Member.PUBLICATION_ID", new Integer(0) , "Publication" ,
                    "id");
                                                        tMap.addColumn("Member.MEMBER_TYPE", new String());
                                                        tMap.addForeignKey(
                "Member.PERSON_DATA_ID", new Integer(0) , "PersonData" ,
                    "id");
                                                        tMap.addForeignKey(
                "Member.PROJECT_DATA_ID", new Integer(0) , "ProjectData" ,
                    "id");
                                                        tMap.addForeignKey(
                "Member.ORGANIZATION_DATA_ID", new Integer(0) , "OrganizationData" ,
                    "id");
                              }
}
