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
 * extended all references should be to Country
 */
public abstract class BaseCountry extends BaseObject
{
    /** The Peer class */
    private static final CountryPeer peer =
        new CountryPeer();

                  
        /**
         * The value for the id field
         */
        private Integer id;
              
        /**
         * The value for the country field
         */
        private String country;
      
      
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

                  
                                                  
              // update associated Address
              if (collAddresss != null)
              {
                  for (int i = 0; i < collAddresss.size(); i++)
                  {
                      ((Address) collAddresss.get(i))
                          .setCountryId(v);
                  }
              }
                                   }


        /**
         * Get the Country
         *
         * @return String
         */
        public String getCountry()
        {
            return country;
        }

                                            
        /**
         * Set the value of Country
         *
         * @param v new value
         */
        public void setCountry(String v) 
        {
          


         if (!ObjectUtils.equals(this.country, v))
        {
             this.country = v;
            setModified(true);
        }

                  
                       }


 
        
                
      
    /**
     * Collection to store aggregation of collAddresss
     */
    protected List collAddresss;

    /**
     * Temporary storage of collAddresss to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initAddresss()
    {
        if (collAddresss == null)
        {
            collAddresss = new ArrayList();
        }
    }

    /**
     * Method called to associate a Address object to this object
     * through the Address foreign key attribute
     *
     * @param l Address
     * @throws TorqueException
     */
    public void addAddress(Address l) throws TorqueException
    {
        getAddresss().add(l);
        l.setCountry((Country) this);
    }

    /**
     * The criteria used to select the current contents of collAddresss
     */
    private Criteria lastAddresssCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getAddresss(new Criteria())
     *
     * @throws TorqueException
     */
    public List getAddresss() throws TorqueException
    {
        if (collAddresss == null)
        {
            collAddresss = getAddresss(new Criteria(10));
        }
        return collAddresss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this Country has previously
     * been saved, it will retrieve related Addresss from storage.
     * If this Country is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List getAddresss(Criteria criteria) throws TorqueException
    {
        if (collAddresss == null)
        {
            if (isNew())
            {
               collAddresss = new ArrayList();
            }
            else
            {
                   criteria.add(AddressPeer.COUNTRY_ID, getId() );
                   collAddresss = AddressPeer.doSelect(criteria);
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
                   criteria.add(AddressPeer.COUNTRY_ID, getId());
                   if (!lastAddresssCriteria.equals(criteria))
                {
                    collAddresss = AddressPeer.doSelect(criteria);
                }
            }
        }
        lastAddresssCriteria = criteria;

        return collAddresss;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getAddresss(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List getAddresss(Connection con) throws TorqueException
    {
        if (collAddresss == null)
        {
            collAddresss = getAddresss(new Criteria(10), con);
        }
        return collAddresss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this Country has previously
     * been saved, it will retrieve related Addresss from storage.
     * If this Country is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List getAddresss(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collAddresss == null)
        {
            if (isNew())
            {
               collAddresss = new ArrayList();
            }
            else
            {
                     criteria.add(AddressPeer.COUNTRY_ID, getId());
                     collAddresss = AddressPeer.doSelect(criteria, con);
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
                     criteria.add(AddressPeer.COUNTRY_ID, getId());
                     if (!lastAddresssCriteria.equals(criteria))
                 {
                     collAddresss = AddressPeer.doSelect(criteria, con);
                 }
             }
         }
         lastAddresssCriteria = criteria;

         return collAddresss;
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
            fieldNames.add("Country");
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
            if (name.equals("Country"))
    {
              return getCountry();
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
            if (name.equals(CountryPeer.ID))
        {
              return getId();
          }
            if (name.equals(CountryPeer.COUNTRY))
        {
              return getCountry();
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
              return getCountry();
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
             save(CountryPeer.getMapBuilder()
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
                CountryPeer.doInsert((Country) this, con);
                setNew(false);
            }
            else
            {
                CountryPeer.doUpdate((Country) this, con);
            }
        }

                                    
                
          if (collAddresss != null)
          {
              for (int i = 0; i < collAddresss.size(); i++)
              {
                  ((Address) collAddresss.get(i)).save(con);
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
    public Country copy() throws TorqueException
    {
        return copyInto(new Country());
    }

    protected Country copyInto(Country copyObj) throws TorqueException
    {
        copyObj.setId(id);
        copyObj.setCountry(country);

  copyObj.setNew(false);
                                    
                
        List v = getAddresss();
        for (int i = 0; i < v.size(); i++)
        {
            Address obj = (Address) v.get(i);
            copyObj.addAddress(obj.copy());
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
    public CountryPeer getPeer()
    {
        return peer;
    }
}
