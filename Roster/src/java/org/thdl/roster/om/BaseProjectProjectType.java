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
 * extended all references should be to ProjectProjectType
 */
public abstract class BaseProjectProjectType extends BaseObject
{
    /** The Peer class */
    private static final ProjectProjectTypePeer peer =
        new ProjectProjectTypePeer();

                  
        /**
         * The value for the id field
         */
        private Integer id;
              
        /**
         * The value for the project_type_id field
         */
        private Integer project_type_id;
              
        /**
         * The value for the project_data_id field
         */
        private Integer project_data_id;
              
        /**
         * The value for the relevance field
         */
        private Integer relevance;
      
      
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
        public void setId(Integer v) 
        {
          


         if (!ObjectUtils.equals(this.id, v))
        {
             this.id = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the ProjectTypeId
         *
         * @return Integer
         */
        public Integer getProjectTypeId()
        {
            return project_type_id;
        }

                                                      
        /**
         * Set the value of ProjectTypeId
         *
         * @param v new value
         */
        public void setProjectTypeId(Integer v) throws TorqueException
        {
          


         if (!ObjectUtils.equals(this.project_type_id, v))
        {
             this.project_type_id = v;
            setModified(true);
        }

                                          
                if (aProjectType != null && !ObjectUtils.equals(aProjectType.getId(), v))
                {
            aProjectType = null;
        }
          
                       }


        /**
         * Get the ProjectDataId
         *
         * @return Integer
         */
        public Integer getProjectDataId()
        {
            return project_data_id;
        }

                                                      
        /**
         * Set the value of ProjectDataId
         *
         * @param v new value
         */
        public void setProjectDataId(Integer v) throws TorqueException
        {
          


         if (!ObjectUtils.equals(this.project_data_id, v))
        {
             this.project_data_id = v;
            setModified(true);
        }

                                          
                if (aProjectData != null && !ObjectUtils.equals(aProjectData.getId(), v))
                {
            aProjectData = null;
        }
          
                       }


        /**
         * Get the Relevance
         *
         * @return Integer
         */
        public Integer getRelevance()
        {
            return relevance;
        }

                                            
        /**
         * Set the value of Relevance
         *
         * @param v new value
         */
        public void setRelevance(Integer v) 
        {
          


         if (!ObjectUtils.equals(this.relevance, v))
        {
             this.relevance = v;
            setModified(true);
        }

                  
                       }


 
     
   
             
   
       private ProjectData aProjectData;

    /**
     * Declares an association between this object and a ProjectData object
     *
     * @param v ProjectData
     * @throws TorqueException
     */
    public void setProjectData(ProjectData v) throws TorqueException
    {
           if (v == null)
        {
                         setProjectDataId((Integer)null);
                    }
        else
        {
            setProjectDataId(v.getId());
        }
           aProjectData = v;
    }

                 
    /**
     * Get the associated ProjectData object
     *
     * @return the associated ProjectData object
     * @throws TorqueException
     */
    public ProjectData getProjectData() throws TorqueException
    {
        if (aProjectData == null && (!ObjectUtils.equals(this.project_data_id, null)))
        {
              aProjectData = ProjectDataPeer.retrieveByPK(SimpleKey.keyFor(this.project_data_id));
      
            /* The following can be used instead of the line above to
               guarantee the related object contains a reference
               to this object, but this level of coupling
               may be undesirable in many circumstances.
               As it can lead to a db query with many results that may
               never be used.
               ProjectData obj = ProjectDataPeer.retrieveByPK(this.project_data_id);
               obj.addProjectProjectTypes(this);
             */
        }
        return aProjectData;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey.  e.g.
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setProjectDataKey(ObjectKey key) throws TorqueException
    {
    
                                        setProjectDataId(new Integer(((NumberKey) key).intValue()));
                    }
 
   
             
   
       private ProjectType aProjectType;

    /**
     * Declares an association between this object and a ProjectType object
     *
     * @param v ProjectType
     * @throws TorqueException
     */
    public void setProjectType(ProjectType v) throws TorqueException
    {
           if (v == null)
        {
                         setProjectTypeId((Integer)null);
                    }
        else
        {
            setProjectTypeId(v.getId());
        }
           aProjectType = v;
    }

                 
    /**
     * Get the associated ProjectType object
     *
     * @return the associated ProjectType object
     * @throws TorqueException
     */
    public ProjectType getProjectType() throws TorqueException
    {
        if (aProjectType == null && (!ObjectUtils.equals(this.project_type_id, null)))
        {
              aProjectType = ProjectTypePeer.retrieveByPK(SimpleKey.keyFor(this.project_type_id));
      
            /* The following can be used instead of the line above to
               guarantee the related object contains a reference
               to this object, but this level of coupling
               may be undesirable in many circumstances.
               As it can lead to a db query with many results that may
               never be used.
               ProjectType obj = ProjectTypePeer.retrieveByPK(this.project_type_id);
               obj.addProjectProjectTypes(this);
             */
        }
        return aProjectType;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey.  e.g.
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setProjectTypeKey(ObjectKey key) throws TorqueException
    {
    
                                        setProjectTypeId(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("ProjectTypeId");
            fieldNames.add("ProjectDataId");
            fieldNames.add("Relevance");
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
            if (name.equals("ProjectTypeId"))
    {
              return getProjectTypeId();
          }
            if (name.equals("ProjectDataId"))
    {
              return getProjectDataId();
          }
            if (name.equals("Relevance"))
    {
              return getRelevance();
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
            if (name.equals(ProjectProjectTypePeer.ID))
        {
              return getId();
          }
            if (name.equals(ProjectProjectTypePeer.PROJECT_TYPE_ID))
        {
              return getProjectTypeId();
          }
            if (name.equals(ProjectProjectTypePeer.PROJECT_DATA_ID))
        {
              return getProjectDataId();
          }
            if (name.equals(ProjectProjectTypePeer.RELEVANCE))
        {
              return getRelevance();
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
              return getProjectTypeId();
          }
            if (pos == 2)
    {
              return getProjectDataId();
          }
            if (pos == 3)
    {
              return getRelevance();
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
             save(ProjectProjectTypePeer.getMapBuilder()
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
                ProjectProjectTypePeer.doInsert((ProjectProjectType) this, con);
                setNew(false);
            }
            else
            {
                ProjectProjectTypePeer.doUpdate((ProjectProjectType) this, con);
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
        
    {
                    setId(new Integer(((NumberKey) key).intValue()));
            }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) 
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
    public ProjectProjectType copy() throws TorqueException
    {
        return copyInto(new ProjectProjectType());
    }

    protected ProjectProjectType copyInto(ProjectProjectType copyObj) throws TorqueException
    {
        copyObj.setId(id);
        copyObj.setProjectTypeId(project_type_id);
        copyObj.setProjectDataId(project_data_id);
        copyObj.setRelevance(relevance);

  copyObj.setNew(false);
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
    public ProjectProjectTypePeer getPeer()
    {
        return peer;
    }
}
