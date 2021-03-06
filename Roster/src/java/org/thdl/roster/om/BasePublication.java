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
 * extended all references should be to Publication
 */
public abstract class BasePublication extends BaseObject
{
    /** The Peer class */
    private static final PublicationPeer peer =
        new PublicationPeer();

                  
        /**
         * The value for the id field
         */
        private Integer id;
              
        /**
         * The value for the formal_publications field
         */
        private String formal_publications;
              
        /**
         * The value for the works_in_progress field
         */
        private String works_in_progress;
              
        /**
         * The value for the projects field
         */
        private String projects;
      
      
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

                  
                                                  
              // update associated Member
              if (collMembers != null)
              {
                  for (int i = 0; i < collMembers.size(); i++)
                  {
                      ((Member) collMembers.get(i))
                          .setPublicationId(v);
                  }
              }
                                   }


        /**
         * Get the FormalPublications
         *
         * @return String
         */
        public String getFormalPublications()
        {
            return formal_publications;
        }

                                            
        /**
         * Set the value of FormalPublications
         *
         * @param v new value
         */
        public void setFormalPublications(String v) 
        {
          


         if (!ObjectUtils.equals(this.formal_publications, v))
        {
             this.formal_publications = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the WorksInProgress
         *
         * @return String
         */
        public String getWorksInProgress()
        {
            return works_in_progress;
        }

                                            
        /**
         * Set the value of WorksInProgress
         *
         * @param v new value
         */
        public void setWorksInProgress(String v) 
        {
          


         if (!ObjectUtils.equals(this.works_in_progress, v))
        {
             this.works_in_progress = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the Projects
         *
         * @return String
         */
        public String getProjects()
        {
            return projects;
        }

                                            
        /**
         * Set the value of Projects
         *
         * @param v new value
         */
        public void setProjects(String v) 
        {
          


         if (!ObjectUtils.equals(this.projects, v))
        {
             this.projects = v;
            setModified(true);
        }

                  
                       }


 
        
                
      
    /**
     * Collection to store aggregation of collMembers
     */
    protected List collMembers;

    /**
     * Temporary storage of collMembers to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initMembers()
    {
        if (collMembers == null)
        {
            collMembers = new ArrayList();
        }
    }

    /**
     * Method called to associate a Member object to this object
     * through the Member foreign key attribute
     *
     * @param l Member
     * @throws TorqueException
     */
    public void addMember(Member l) throws TorqueException
    {
        getMembers().add(l);
        l.setPublication((Publication) this);
    }

    /**
     * The criteria used to select the current contents of collMembers
     */
    private Criteria lastMembersCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getMembers(new Criteria())
     *
     * @throws TorqueException
     */
    public List getMembers() throws TorqueException
    {
        if (collMembers == null)
        {
            collMembers = getMembers(new Criteria(10));
        }
        return collMembers;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this Publication has previously
     * been saved, it will retrieve related Members from storage.
     * If this Publication is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List getMembers(Criteria criteria) throws TorqueException
    {
        if (collMembers == null)
        {
            if (isNew())
            {
               collMembers = new ArrayList();
            }
            else
            {
                   criteria.add(MemberPeer.PUBLICATION_ID, getId() );
                   collMembers = MemberPeer.doSelect(criteria);
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
                   criteria.add(MemberPeer.PUBLICATION_ID, getId());
                   if (!lastMembersCriteria.equals(criteria))
                {
                    collMembers = MemberPeer.doSelect(criteria);
                }
            }
        }
        lastMembersCriteria = criteria;

        return collMembers;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getMembers(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List getMembers(Connection con) throws TorqueException
    {
        if (collMembers == null)
        {
            collMembers = getMembers(new Criteria(10), con);
        }
        return collMembers;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this Publication has previously
     * been saved, it will retrieve related Members from storage.
     * If this Publication is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List getMembers(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collMembers == null)
        {
            if (isNew())
            {
               collMembers = new ArrayList();
            }
            else
            {
                     criteria.add(MemberPeer.PUBLICATION_ID, getId());
                     collMembers = MemberPeer.doSelect(criteria, con);
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
                     criteria.add(MemberPeer.PUBLICATION_ID, getId());
                     if (!lastMembersCriteria.equals(criteria))
                 {
                     collMembers = MemberPeer.doSelect(criteria, con);
                 }
             }
         }
         lastMembersCriteria = criteria;

         return collMembers;
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
            fieldNames.add("FormalPublications");
            fieldNames.add("WorksInProgress");
            fieldNames.add("Projects");
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
            if (name.equals("FormalPublications"))
    {
              return getFormalPublications();
          }
            if (name.equals("WorksInProgress"))
    {
              return getWorksInProgress();
          }
            if (name.equals("Projects"))
    {
              return getProjects();
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
            if (name.equals(PublicationPeer.ID))
        {
              return getId();
          }
            if (name.equals(PublicationPeer.FORMAL_PUBLICATIONS))
        {
              return getFormalPublications();
          }
            if (name.equals(PublicationPeer.WORKS_IN_PROGRESS))
        {
              return getWorksInProgress();
          }
            if (name.equals(PublicationPeer.PROJECTS))
        {
              return getProjects();
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
              return getFormalPublications();
          }
            if (pos == 2)
    {
              return getWorksInProgress();
          }
            if (pos == 3)
    {
              return getProjects();
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
             save(PublicationPeer.getMapBuilder()
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
                PublicationPeer.doInsert((Publication) this, con);
                setNew(false);
            }
            else
            {
                PublicationPeer.doUpdate((Publication) this, con);
            }
        }

                                    
                
          if (collMembers != null)
          {
              for (int i = 0; i < collMembers.size(); i++)
              {
                  ((Member) collMembers.get(i)).save(con);
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
    public Publication copy() throws TorqueException
    {
        return copyInto(new Publication());
    }

    protected Publication copyInto(Publication copyObj) throws TorqueException
    {
        copyObj.setId(id);
        copyObj.setFormalPublications(formal_publications);
        copyObj.setWorksInProgress(works_in_progress);
        copyObj.setProjects(projects);

  copyObj.setNew(false);
                                    
                
        List v = getMembers();
        for (int i = 0; i < v.size(); i++)
        {
            Member obj = (Member) v.get(i);
            copyObj.addMember(obj.copy());
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
    public PublicationPeer getPeer()
    {
        return peer;
    }
}
