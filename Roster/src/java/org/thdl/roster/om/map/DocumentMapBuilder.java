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
public class DocumentMapBuilder implements MapBuilder
{
    /**
     * The name of this class
     */
    public static final String CLASS_NAME =
        "org.thdl.roster.om.map.DocumentMapBuilder";


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

        dbMap.addTable("Document");
        TableMap tMap = dbMap.getTable("Document");

                tMap.setPrimaryKeyMethod(TableMap.ID_BROKER);
        
                tMap.setPrimaryKeyMethodInfo(tMap.getName());
        
                                      tMap.addPrimaryKey("Document.ID", new Integer(0));
                                                        tMap.addForeignKey(
                "Document.MEMBER_ID", new Integer(0) , "Member" ,
                    "id");
                                                        tMap.addForeignKey(
                "Document.DOCUMENT_TYPE_ID", new Integer(0) , "DocumentType" ,
                    "id");
                                                        tMap.addColumn("Document.CONTENT_TYPE", new String());
                                                        tMap.addColumn("Document.PATH", new String());
                                                        tMap.addColumn("Document.FILENAME", new String());
                                                        tMap.addColumn("Document.LABEL", new String());
                              }
}