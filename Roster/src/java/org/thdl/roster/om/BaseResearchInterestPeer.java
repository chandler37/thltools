package org.thdl.roster.om;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import org.apache.torque.map.MapBuilder;
import org.apache.torque.map.TableMap;
import org.apache.torque.om.DateKey;
import org.apache.torque.om.NumberKey;
import org.apache.torque.om.StringKey;
import org.apache.torque.om.ObjectKey;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria;

import com.workingdogs.village.DataSetException;
import com.workingdogs.village.QueryDataSet;
import com.workingdogs.village.Record;

// Local classes
import org.thdl.roster.om.map.*;


/**
  * This class was autogenerated by Torque on:
  *
  * [Wed May 14 19:01:42 EDT 2003]
  *
  */
public abstract class BaseResearchInterestPeer
    extends BasePeer
{

    /** the default database name for this class */
    public static final String DATABASE_NAME = "Roster";

     /** the table name for this class */
    public static final String TABLE_NAME = "ResearchInterest";

    /**
     * @return the map builder for this peer
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static MapBuilder getMapBuilder()
        throws TorqueException
    {
        return getMapBuilder(ResearchInterestMapBuilder.CLASS_NAME);
    }

    /** the column name for the ID field */
    public static final String ID;
    /** the column name for the INTERESTS field */
    public static final String INTERESTS;
    /** the column name for the ACTIVITIES field */
    public static final String ACTIVITIES;
    /** the column name for the COLLABORATION_INTERESTS field */
    public static final String COLLABORATION_INTERESTS;
    /** the column name for the FOCUS_FROM field */
    public static final String FOCUS_FROM;
    /** the column name for the FOCUS_TO field */
    public static final String FOCUS_TO;

    static
    {
    ID = "ResearchInterest.ID";
    INTERESTS = "ResearchInterest.INTERESTS";
    ACTIVITIES = "ResearchInterest.ACTIVITIES";
    COLLABORATION_INTERESTS = "ResearchInterest.COLLABORATION_INTERESTS";
    FOCUS_FROM = "ResearchInterest.FOCUS_FROM";
    FOCUS_TO = "ResearchInterest.FOCUS_TO";

        if (Torque.isInit())
        {
            try
            {
                getMapBuilder();
            }
            catch (Exception e)
            {
                category.error("Could not initialize Peer", e);
            }
        }
        else
        {
            Torque.registerMapBuilder(ResearchInterestMapBuilder.CLASS_NAME);
        }
    }

 
    /** number of columns for this peer */
    public static final int numColumns =  6;

    /** A class that can be returned by this peer. */
    protected static final String CLASSNAME_DEFAULT =
        "org.thdl.roster.om.ResearchInterest";

    /** A class that can be returned by this peer. */
    protected static final Class CLASS_DEFAULT = initClass(CLASSNAME_DEFAULT);

    /**
     * Class object initialization method.
     *
     * @param className name of the class to initialize
     * @return the initialized class
     */
    private static Class initClass(String className)
    {
        Class c = null;
        try
        {
            c = Class.forName(className);
        }
        catch (Throwable t)
        {
            category.error("A FATAL ERROR has occurred which should not "
                + "have happened under any circumstance.  Please notify "
                + "the Turbine developers <turbine-dev@jakarta.apache.org> "
                + "and give as many details as possible (including the error "
                + "stack trace).", t);

            // Error objects should always be propogated.
            if (t instanceof Error)
            {
                throw (Error) t.fillInStackTrace();
            }
        }
        return c;
    }


    /**
     * Get the list of objects for a ResultSet.  Please not that your
     * resultset MUST return columns in the right order.  You can use
     * getFieldNames() in BaseObject to get the correct sequence.
     *
     * @param results the ResultSet
     * @return the list of objects
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List resultSet2Objects(java.sql.ResultSet results)
            throws TorqueException
    {
        try
        {
            QueryDataSet qds = null;
            List rows = null;
            try
            {
                qds = new QueryDataSet(results);
                rows = getSelectResults(qds);
            }
            finally
            {
                if (qds != null)
                {
                    qds.close();
                }
            }

            return populateObjects(rows);
        }
        catch (SQLException e)
        {
            throw new TorqueException(e);
        }
        catch (DataSetException e)
        {
            throw new TorqueException(e);
        }
    }



    /**
     * Method to do inserts.
     *
     * @param criteria object used to create the INSERT statement.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static ObjectKey doInsert(Criteria criteria)
        throws TorqueException
    {
        return BaseResearchInterestPeer
            .doInsert(criteria, (Connection) null);
    }

    /**
     * Method to do inserts.  This method is to be used during a transaction,
     * otherwise use the doInsert(Criteria) method.  It will take care of
     * the connection details internally.
     *
     * @param criteria object used to create the INSERT statement.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static ObjectKey doInsert(Criteria criteria, Connection con)
        throws TorqueException
    {
                                                                                         
        // Set the correct dbName if it has not been overridden
        // criteria.getDbName will return the same object if not set to
        // another value so == check is okay and faster
        if (criteria.getDbName() == Torque.getDefaultDB())
        {
            criteria.setDbName(DATABASE_NAME);
        }
        if (con == null)
        {
            return BasePeer.doInsert(criteria);
        }
        else
        {
            return BasePeer.doInsert(criteria, con);
        }
    }

    /**
     * Add all the columns needed to create a new object.
     *
     * @param criteria object containing the columns to add.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void addSelectColumns(Criteria criteria)
            throws TorqueException
    {
            criteria.addSelectColumn(ID);
            criteria.addSelectColumn(INTERESTS);
            criteria.addSelectColumn(ACTIVITIES);
            criteria.addSelectColumn(COLLABORATION_INTERESTS);
            criteria.addSelectColumn(FOCUS_FROM);
            criteria.addSelectColumn(FOCUS_TO);
        }


    /**
     * Create a new object of type cls from a resultset row starting
     * from a specified offset.  This is done so that you can select
     * other rows than just those needed for this object.  You may
     * for example want to create two objects from the same row.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static ResearchInterest row2Object(Record row,
                                             int offset,
                                             Class cls)
        throws TorqueException
    {
        try
        {
            ResearchInterest obj = (ResearchInterest) cls.newInstance();
            populateObject(row, offset, obj);
                            obj.setModified(false);
                        obj.setNew(false);

            return obj;
        }
        catch (InstantiationException e)
        {
            throw new TorqueException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new TorqueException(e);
        }
    }

    /**
     * Populates an object from a resultset row starting
     * from a specified offset.  This is done so that you can select
     * other rows than just those needed for this object.  You may
     * for example want to create two objects from the same row.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void populateObject(Record row,
                                      int offset,
                                      ResearchInterest obj)
        throws TorqueException
    {
        try
        {
                                        obj.setId(row.getValue(offset + 0).asIntegerObj());
                                            obj.setInterests(row.getValue(offset + 1).asString());
                                            obj.setActivities(row.getValue(offset + 2).asString());
                                            obj.setCollaborationInterests(row.getValue(offset + 3).asString());
                                            obj.setFocusFrom(row.getValue(offset + 4).asIntegerObj());
                                            obj.setFocusTo(row.getValue(offset + 5).asIntegerObj());
                            }
        catch (DataSetException e)
        {
            throw new TorqueException(e);
        }
    }

    /**
     * Method to do selects.
     *
     * @param criteria object used to create the SELECT statement.
     * @return List of selected Objects
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List doSelect(Criteria criteria) throws TorqueException
    {
        return populateObjects(doSelectVillageRecords(criteria));
    }

    /**
     * Method to do selects within a transaction.
     *
     * @param criteria object used to create the SELECT statement.
     * @param con the connection to use
     * @return List of selected Objects
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List doSelect(Criteria criteria, Connection con)
        throws TorqueException
    {
        return populateObjects(doSelectVillageRecords(criteria, con));
    }

    /**
     * Grabs the raw Village records to be formed into objects.
     * This method handles connections internally.  The Record objects
     * returned by this method should be considered readonly.  Do not
     * alter the data and call save(), your results may vary, but are
     * certainly likely to result in hard to track MT bugs.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List doSelectVillageRecords(Criteria criteria)
        throws TorqueException
    {
        return BaseResearchInterestPeer
            .doSelectVillageRecords(criteria, (Connection) null);
    }

    /**
     * Grabs the raw Village records to be formed into objects.
     * This method should be used for transactions
     *
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List doSelectVillageRecords(Criteria criteria, Connection con)
        throws TorqueException
    {
    
        if (criteria.getSelectColumns().size() == 0)
        {
            addSelectColumns(criteria);
        }

                                                                                         
        // Set the correct dbName if it has not been overridden
        // criteria.getDbName will return the same object if not set to
        // another value so == check is okay and faster
        if (criteria.getDbName() == Torque.getDefaultDB())
        {
            criteria.setDbName(DATABASE_NAME);
        }
        // BasePeer returns a List of Value (Village) arrays.  The array
        // order follows the order columns were placed in the Select clause.
        if (con == null)
        {
            return BasePeer.doSelect(criteria);
        }
        else
        {
            return BasePeer.doSelect(criteria, con);
        }
    }

    /**
     * The returned List will contain objects of the default type or
     * objects that inherit from the default.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List populateObjects(List records)
        throws TorqueException
    {
        List results = new ArrayList(records.size());

        // populate the object(s)
        for (int i = 0; i < records.size(); i++)
        {
            Record row = (Record) records.get(i);
            results.add(ResearchInterestPeer.row2Object(row, 1,
                ResearchInterestPeer.getOMClass()));
        }
        return results;
    }
 

    /**
     * The class that the Peer will make instances of.
     * If the BO is abstract then you must implement this method
     * in the BO.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static Class getOMClass()
        throws TorqueException
    {
            return CLASS_DEFAULT;
        }


    /**
     * Method to do updates.
     *
     * @param criteria object containing data that is used to create the UPDATE
     *        statement.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doUpdate(Criteria criteria) throws TorqueException
    {
         BaseResearchInterestPeer
            .doUpdate(criteria, (Connection) null);
    }

    /**
     * Method to do updates.  This method is to be used during a transaction,
     * otherwise use the doUpdate(Criteria) method.  It will take care of
     * the connection details internally.
     *
     * @param criteria object containing data that is used to create the UPDATE
     *        statement.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doUpdate(Criteria criteria, Connection con)
        throws TorqueException
    {
        Criteria selectCriteria = new Criteria(DATABASE_NAME, 2);
                                selectCriteria.put(ID, criteria.remove(ID));
                                                                                                                                 
        // Set the correct dbName if it has not been overridden
        // criteria.getDbName will return the same object if not set to
        // another value so == check is okay and faster
        if (criteria.getDbName() == Torque.getDefaultDB())
        {
            criteria.setDbName(DATABASE_NAME);
        }
        if (con == null)
        {
            BasePeer.doUpdate(selectCriteria, criteria);
        }
        else
        {
            BasePeer.doUpdate(selectCriteria, criteria, con);
        }
    }

    /**
     * Method to do deletes.
     *
     * @param criteria object containing data that is used DELETE from database.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
     public static void doDelete(Criteria criteria) throws TorqueException
     {
         BaseResearchInterestPeer
            .doDelete(criteria, (Connection) null);
     }

    /**
     * Method to do deletes.  This method is to be used during a transaction,
     * otherwise use the doDelete(Criteria) method.  It will take care of
     * the connection details internally.
     *
     * @param criteria object containing data that is used DELETE from database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
     public static void doDelete(Criteria criteria, Connection con)
        throws TorqueException
     {
                                                                                         
        // Set the correct dbName if it has not been overridden
        // criteria.getDbName will return the same object if not set to
        // another value so == check is okay and faster
        if (criteria.getDbName() == Torque.getDefaultDB())
        {
            criteria.setDbName(DATABASE_NAME);
        }
        if (con == null)
        {
            BasePeer.doDelete(criteria);
        }
        else
        {
            BasePeer.doDelete(criteria, con);
        }
     }

    /**
     * Method to do selects
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List doSelect(ResearchInterest obj) throws TorqueException
    {
        return doSelect(buildCriteria(obj));
    }

    /**
     * Method to do inserts
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doInsert(ResearchInterest obj) throws TorqueException
    {
                obj.setPrimaryKey(doInsert(buildCriteria(obj)));
                obj.setNew(false);
        obj.setModified(false);
    }

    /**
     * @param obj the data object to update in the database.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doUpdate(ResearchInterest obj) throws TorqueException
    {
        doUpdate(buildCriteria(obj));
        obj.setModified(false);
    }

    /**
     * @param obj the data object to delete in the database.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(ResearchInterest obj) throws TorqueException
    {
        doDelete(buildCriteria(obj));
    }

    /**
     * Method to do inserts.  This method is to be used during a transaction,
     * otherwise use the doInsert(ResearchInterest) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to insert into the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doInsert(ResearchInterest obj, Connection con)
        throws TorqueException
    {
                obj.setPrimaryKey(doInsert(buildCriteria(obj), con));
                obj.setNew(false);
        obj.setModified(false);
    }

    /**
     * Method to do update.  This method is to be used during a transaction,
     * otherwise use the doUpdate(ResearchInterest) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to update in the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doUpdate(ResearchInterest obj, Connection con)
        throws TorqueException
    {
        doUpdate(buildCriteria(obj), con);
        obj.setModified(false);
    }

    /**
     * Method to delete.  This method is to be used during a transaction,
     * otherwise use the doDelete(ResearchInterest) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to delete in the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(ResearchInterest obj, Connection con)
        throws TorqueException
    {
        doDelete(buildCriteria(obj), con);
    }

    /**
     * Method to do deletes.
     *
     * @param pk ObjectKey that is used DELETE from database.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(ObjectKey pk) throws TorqueException
    {
        BaseResearchInterestPeer
           .doDelete(pk, (Connection) null);
    }

    /**
     * Method to delete.  This method is to be used during a transaction,
     * otherwise use the doDelete(ObjectKey) method.  It will take
     * care of the connection details internally.
     *
     * @param pk the primary key for the object to delete in the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(ObjectKey pk, Connection con)
        throws TorqueException
    {
        doDelete(buildCriteria(pk), con);
    }

    /** Build a Criteria object from an ObjectKey */
    public static Criteria buildCriteria( ObjectKey pk )
    {
        Criteria criteria = new Criteria();
              criteria.add(ID, pk);
          return criteria;
     }

    /** Build a Criteria object from the data object for this peer */
    public static Criteria buildCriteria( ResearchInterest obj )
    {
        Criteria criteria = new Criteria(DATABASE_NAME);
                            if (!obj.isNew())
                       criteria.add(ID, obj.getId());
                                criteria.add(INTERESTS, obj.getInterests());
                                criteria.add(ACTIVITIES, obj.getActivities());
                                criteria.add(COLLABORATION_INTERESTS, obj.getCollaborationInterests());
                                criteria.add(FOCUS_FROM, obj.getFocusFrom());
                                criteria.add(FOCUS_TO, obj.getFocusTo());
                return criteria;
    }

 

    
    /**
     * Retrieve a single object by pk
     *
     * @param pk the primary key
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static ResearchInterest retrieveByPK(Integer pk)
        throws TorqueException
    {
        return retrieveByPK(SimpleKey.keyFor(pk));
    }

    /**
     * Retrieve a single object by pk
     *
     * @param pk the primary key
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static ResearchInterest retrieveByPK(ObjectKey pk)
        throws TorqueException
    {
        Connection db = null;
        ResearchInterest retVal = null;
        try
        {
            db = Torque.getConnection(DATABASE_NAME);
            retVal = retrieveByPK(pk, db);
        }
        finally
        {
            Torque.closeConnection(db);
        }
        return(retVal);
    }

    /**
     * Retrieve a single object by pk
     *
     * @param pk the primary key
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static ResearchInterest retrieveByPK(ObjectKey pk, Connection con)
        throws TorqueException
    {
        Criteria criteria = buildCriteria(pk);
        List v = doSelect(criteria, con);
        if (v.size() != 1)
        {
            throw new TorqueException("Failed to select one and only one row.");
        }
        else
        {
            return (ResearchInterest)v.get(0);
        }
    }

    /**
     * Retrieve a multiple objects by pk
     *
     * @param pks List of primary keys
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List retrieveByPKs(List pks)
        throws TorqueException
    {
        Connection db = null;
        List retVal = null;
        try
        {
           db = Torque.getConnection(DATABASE_NAME);
           retVal = retrieveByPKs(pks, db);
        }
        finally
        {
            Torque.closeConnection(db);
        }
        return(retVal);
    }

    /**
     * Retrieve a multiple objects by pk
     *
     * @param pks List of primary keys
     * @param dbcon the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List retrieveByPKs( List pks, Connection dbcon )
        throws TorqueException
    {
        List objs = null;
        if (pks == null || pks.size() == 0)
        {
            objs = new LinkedList();
        }
        else
        {
            Criteria criteria = new Criteria();
              criteria.addIn( ID, pks );
          objs = doSelect(criteria, dbcon);
        }
        return objs;
    }

 



    
 

  

    /**
     * Returns the TableMap related to this peer.  This method is not
     * needed for general use but a specific application could have a need.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static TableMap getTableMap()
        throws TorqueException
    {
        return Torque.getDatabaseMap(DATABASE_NAME).getTable(TABLE_NAME);
    }
 }
