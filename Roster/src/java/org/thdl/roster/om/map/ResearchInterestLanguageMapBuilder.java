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
public class ResearchInterestLanguageMapBuilder implements MapBuilder
{
    /**
     * The name of this class
     */
    public static final String CLASS_NAME =
        "org.thdl.roster.om.map.ResearchInterestLanguageMapBuilder";


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

        dbMap.addTable("ResearchInterestLanguage");
        TableMap tMap = dbMap.getTable("ResearchInterestLanguage");

                tMap.setPrimaryKeyMethod(TableMap.ID_BROKER);
        
                tMap.setPrimaryKeyMethodInfo(tMap.getName());
        
                                      tMap.addPrimaryKey("ResearchInterestLanguage.ID", new Integer(0));
                                                        tMap.addForeignKey(
                "ResearchInterestLanguage.LANGUAGE_ID", new Integer(0) , "Language" ,
                    "id");
                                                        tMap.addForeignKey(
                "ResearchInterestLanguage.RESEARCH_INTEREST_ID", new Integer(0) , "ResearchInterest" ,
                    "id");
                                                        tMap.addColumn("ResearchInterestLanguage.RELEVANCE", new Integer(0));
                              }
}