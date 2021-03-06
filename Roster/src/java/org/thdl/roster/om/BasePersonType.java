package org.thdl.roster.om;


import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.torque.TorqueException;
import org.apache.torque.om.BaseObject;
import org.apache.torque.om.ComboKey;
import org.apache.torque.om.DateKey;
import org.apache.torque.om.NumberKey;
import org.apache.torque.om.ObjectKey;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.om.StringKey;
import org.apache.torque.om.Persistent;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Transaction;


/**
 * This class was autogenerated by Torque on:
 *
 * [Wed May 14 19:01:42 EDT 2003]
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to PersonType
 */
public abstract class BasePersonType extends BaseObject
{
    /** The Peer class */
    private static final PersonTypePeer peer =
        new PersonTypePeer();

                  
        /**
         * The value for the id field
         */
        private Integer id;
              
        /**
         * The value for the person_type field
         */
        private String person_type;
      
      
        /**
         * Get the Id
         *
         * @return Integer
         */
        public Integer getId()
        {
            return id;
        }

                                                                              
        /**
         * Set the value of Id
         *
         * @param v new value
         */
        public void setId(Integer v) throws TorqueException
        {
          


         if (!ObjectUtils.equals(this.id, v))
        {
             this.id = v;
            setModified(true);
        }

                  
                                                  
              // update associated PersonPersonType
              if (collPersonPersonTypes != null)
              {
                  for (int i = 0; i < collPersonPersonTypes.size(); i++)
                  {
                      ((PersonPersonType) collPersonPersonTypes.get(i))
                          .setPersonTypeId(v);
                  }
              }
                                   }


        /**
         * Get the PersonType
         *
         * @return String
         */
        public String getPersonType()
        {
            return person_type;
        }

                                            
        /**
         * Set the value of PersonType
         *
         * @param v new value
         */
        public void setPersonType(String v) 
        {
          


         if (!ObjectUtils.equals(this.person_type, v))
        {
             this.person_type = v;
            setModified(true);
        }

                  
                       }


 
        
                
      
    /**
     * Collection to store aggregation of collPersonPersonTypes
     */
    protected List collPersonPersonTypes;

    /**
     * Temporary storage of collPersonPersonTypes to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initPersonPersonTypes()
    {
        if (collPersonPersonTypes == null)
        {
            collPersonPersonTypes = new ArrayList();
        }
    }

    /**
     * Method called to associate a PersonPersonType object to this object
     * through the PersonPersonType foreign key attribute
     *
     * @param l PersonPersonType
     * @throws TorqueException
     */
    public void addPersonPersonType(PersonPersonType l) throws TorqueException
    {
        getPersonPersonTypes().add(l);
        l.setPersonType((PersonType) this);
    }

    /**
     * The criteria used to select the current contents of collPersonPersonTypes
     */
    private Criteria lastPersonPersonTypesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getPersonPersonTypes(new Criteria())
     *
     * @throws TorqueException
     */
    public List getPersonPersonTypes() throws TorqueException
    {
        if (collPersonPersonTypes == null)
        {
            collPersonPersonTypes = getPersonPersonTypes(new Criteria(10));
        }
        return collPersonPersonTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this PersonType has previously
     * been saved, it will retrieve related PersonPersonTypes from storage.
     * If this PersonType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List getPersonPersonTypes(Criteria criteria) throws TorqueException
    {
        if (collPersonPersonTypes == null)
        {
            if (isNew())
            {
               collPersonPersonTypes = new ArrayList();
            }
            else
            {
                   criteria.add(PersonPersonTypePeer.PERSON_TYPE_ID, getId() );
                   collPersonPersonTypes = PersonPersonTypePeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                   criteria.add(PersonPersonTypePeer.PERSON_TYPE_ID, getId());
                   if (!lastPersonPersonTypesCriteria.equals(criteria))
                {
                    collPersonPersonTypes = PersonPersonTypePeer.doSelect(criteria);
                }
            }
        }
        lastPersonPersonTypesCriteria = criteria;

        return collPersonPersonTypes;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getPersonPersonTypes(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List getPersonPersonTypes(Connection con) throws TorqueException
    {
        if (collPersonPersonTypes == null)
        {
            collPersonPersonTypes = getPersonPersonTypes(new Criteria(10), con);
        }
        return collPersonPersonTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this PersonType has previously
     * been saved, it will retrieve related PersonPersonTypes from storage.
     * If this PersonType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List getPersonPersonTypes(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collPersonPersonTypes == null)
        {
            if (isNew())
            {
               collPersonPersonTypes = new ArrayList();
            }
            else
            {
                     criteria.add(PersonPersonTypePeer.PERSON_TYPE_ID, getId());
                     collPersonPersonTypes = PersonPersonTypePeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                     criteria.add(PersonPersonTypePeer.PERSON_TYPE_ID, getId());
                     if (!lastPersonPersonTypesCriteria.equals(criteria))
                 {
                     collPersonPersonTypes = PersonPersonTypePeer.doSelect(criteria, con);
                 }
             }
         }
         lastPersonPersonTypesCriteria = criteria;

         return collPersonPersonTypes;
     }

    

     
      
      
          
                    
                
        
        
    
      
         
          
                    
                
        
        
      



     
    
    private static List fieldNames = null;

    /**
     * Generate a list of field names.
     *
     * @return a list of field names
     */
    public static synchronized List getFieldNames()
    {
      if (fieldNames == null)
      {
        fieldNames = new ArrayList();
            fieldNames.add("Id");
            fieldNames.add("PersonType");
            fieldNames = Collections.unmodifiableList(fieldNames);
      }
      return fieldNames;
    }

    /**
     * Retrieves a field from the object by name passed in as a String.
     *
     * @param name field name
     * @return value
     */
    public Object getByName(String name)
    {
            if (name.equals("Id"))
    {
              return getId();
          }
            if (name.equals("PersonType"))
    {
              return getPersonType();
          }
            return null;
    }
    /**
     * Retrieves a field from the object by name passed in
     * as a String.  The String must be one of the static
     * Strings defined in this Class' Peer.
     *
     * @param name peer name
     * @return value
     */
    public Object getByPeerName(String name)
    {
            if (name.equals(PersonTypePeer.ID))
        {
              return getId();
          }
            if (name.equals(PersonTypePeer.PERSON_TYPE))
        {
              return getPersonType();
          }
            return null;
    }

    /**
     * Retrieves a field from the object by Position as specified
     * in the xml schema.  Zero-based.
     *
     * @param pos position in xml schema
     * @return value
     */
    public Object getByPosition(int pos)
    {
            if (pos == 0)
    {
              return getId();
          }
            if (pos == 1)
    {
              return getPersonType();
          }
                return null;
    }

     


    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     *
     * @throws Exception
     */
    public void save() throws Exception
    {
             save(PersonTypePeer.getMapBuilder()
                .getDatabaseMap().getName());
     }

    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     * Note: this code is here because the method body is
     * auto-generated conditionally and therefore needs to be
     * in this file instead of in the super class, BaseObject.
     *
     * @param dbName
     * @throws TorqueException
     */
    public void save(String dbName) throws TorqueException
    {
        Connection con = null;
         try
        {
            con = Transaction.begin(dbName);
            save(con);
            Transaction.commit(con);
        }
        catch(TorqueException e)
        {
            Transaction.safeRollback(con);
            throw e;
        }

     }

      /** flag to prevent endless save loop, if this object is referenced
        by another object which falls in this transaction. */
    private boolean alreadyInSave = false;
      /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.  This method
     * is meant to be used as part of a transaction, otherwise use
     * the save() method and the connection details will be handled
     * internally
     *
     * @param con
     * @throws TorqueException
     */
    public void save(Connection con) throws TorqueException
    {
        if (!alreadyInSave)
      {
        alreadyInSave = true;



  
        // If this object has been modified, then save it to the database.
        if (isModified())
        {
            if (isNew())
            {
                PersonTypePeer.doInsert((PersonType) this, con);
                setNew(false);
            }
            else
            {
                PersonTypePeer.doUpdate((PersonType) this, con);
            }
        }

                                    
                
          if (collPersonPersonTypes != null)
          {
              for (int i = 0; i < collPersonPersonTypes.size(); i++)
              {
                  ((PersonPersonType) collPersonPersonTypes.get(i)).save(con);
              }
          }
                  alreadyInSave = false;
      }
      }


                        
    
    

        /**
     * Set the PrimaryKey using ObjectKey.
     *
     * @param  id ObjectKey
     */
    public void setPrimaryKey(ObjectKey key)
        throws TorqueException
    {
                    setId(new Integer(((NumberKey) key).intValue()));
            }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) throws TorqueException
    {
                    setId(new Integer(key));
            }


    /**
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey()
    {
        return SimpleKey.keyFor(getId());
    }

 

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
     * It then fills all the association collections and sets the
     * related objects to isNew=true.
     */
    public PersonType copy() throws TorqueException
    {
        return copyInto(new PersonType());
    }

    protected PersonType copyInto(PersonType copyObj) throws TorqueException
    {
        copyObj.setId(id);
        copyObj.setPersonType(person_type);

  copyObj.setNew(false);
                                    
                
        List v = getPersonPersonTypes();
        for (int i = 0; i < v.size(); i++)
        {
            PersonPersonType obj = (PersonPersonType) v.get(i);
            copyObj.addPersonPersonType(obj.copy());
            ((Persistent) v.get(i)).setNew(true);
        }
                copyObj.setNew(true);

                      copyObj.setId((Integer)null);
                        return copyObj;
    }

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public PersonTypePeer getPeer()
    {
        return peer;
    }
}
