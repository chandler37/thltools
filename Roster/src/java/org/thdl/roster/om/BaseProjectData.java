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
 * extended all references should be to ProjectData
 */
public abstract class BaseProjectData extends BaseObject
{
    /** The Peer class */
    private static final ProjectDataPeer peer =
        new ProjectDataPeer();

                  
        /**
         * The value for the id field
         */
        private Integer id;
              
        /**
         * The value for the name field
         */
        private String name;
              
        /**
         * The value for the parent_organization field
         */
        private String parent_organization;
              
        /**
         * The value for the divisions field
         */
        private String divisions;
              
        /**
         * The value for the people field
         */
        private String people;
              
        /**
         * The value for the mailing_list field
         */
        private String mailing_list;
              
        /**
         * The value for the description field
         */
        private String description;
              
        /**
         * The value for the history field
         */
        private String history;
              
        /**
         * The value for the education_programs field
         */
        private String education_programs;
              
        /**
         * The value for the resources field
         */
        private String resources;
      
      
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
                          .setProjectDataId(v);
                  }
              }
                                                              
              // update associated ProjectProjectType
              if (collProjectProjectTypes != null)
              {
                  for (int i = 0; i < collProjectProjectTypes.size(); i++)
                  {
                      ((ProjectProjectType) collProjectProjectTypes.get(i))
                          .setProjectDataId(v);
                  }
              }
                                   }


        /**
         * Get the Name
         *
         * @return String
         */
        public String getName()
        {
            return name;
        }

                                            
        /**
         * Set the value of Name
         *
         * @param v new value
         */
        public void setName(String v) 
        {
          


         if (!ObjectUtils.equals(this.name, v))
        {
             this.name = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the ParentOrganization
         *
         * @return String
         */
        public String getParentOrganization()
        {
            return parent_organization;
        }

                                            
        /**
         * Set the value of ParentOrganization
         *
         * @param v new value
         */
        public void setParentOrganization(String v) 
        {
          


         if (!ObjectUtils.equals(this.parent_organization, v))
        {
             this.parent_organization = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the Divisions
         *
         * @return String
         */
        public String getDivisions()
        {
            return divisions;
        }

                                            
        /**
         * Set the value of Divisions
         *
         * @param v new value
         */
        public void setDivisions(String v) 
        {
          


         if (!ObjectUtils.equals(this.divisions, v))
        {
             this.divisions = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the People
         *
         * @return String
         */
        public String getPeople()
        {
            return people;
        }

                                            
        /**
         * Set the value of People
         *
         * @param v new value
         */
        public void setPeople(String v) 
        {
          


         if (!ObjectUtils.equals(this.people, v))
        {
             this.people = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the MailingList
         *
         * @return String
         */
        public String getMailingList()
        {
            return mailing_list;
        }

                                            
        /**
         * Set the value of MailingList
         *
         * @param v new value
         */
        public void setMailingList(String v) 
        {
          


         if (!ObjectUtils.equals(this.mailing_list, v))
        {
             this.mailing_list = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the Description
         *
         * @return String
         */
        public String getDescription()
        {
            return description;
        }

                                            
        /**
         * Set the value of Description
         *
         * @param v new value
         */
        public void setDescription(String v) 
        {
          


         if (!ObjectUtils.equals(this.description, v))
        {
             this.description = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the History
         *
         * @return String
         */
        public String getHistory()
        {
            return history;
        }

                                            
        /**
         * Set the value of History
         *
         * @param v new value
         */
        public void setHistory(String v) 
        {
          


         if (!ObjectUtils.equals(this.history, v))
        {
             this.history = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the EducationPrograms
         *
         * @return String
         */
        public String getEducationPrograms()
        {
            return education_programs;
        }

                                            
        /**
         * Set the value of EducationPrograms
         *
         * @param v new value
         */
        public void setEducationPrograms(String v) 
        {
          


         if (!ObjectUtils.equals(this.education_programs, v))
        {
             this.education_programs = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the Resources
         *
         * @return String
         */
        public String getResources()
        {
            return resources;
        }

                                            
        /**
         * Set the value of Resources
         *
         * @param v new value
         */
        public void setResources(String v) 
        {
          


         if (!ObjectUtils.equals(this.resources, v))
        {
             this.resources = v;
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
        l.setProjectData((ProjectData) this);
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
     * Otherwise if this ProjectData has previously
     * been saved, it will retrieve related Members from storage.
     * If this ProjectData is new, it will return
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
                   criteria.add(MemberPeer.PROJECT_DATA_ID, getId() );
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
                   criteria.add(MemberPeer.PROJECT_DATA_ID, getId());
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
     * Otherwise if this ProjectData has previously
     * been saved, it will retrieve related Members from storage.
     * If this ProjectData is new, it will return
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
                     criteria.add(MemberPeer.PROJECT_DATA_ID, getId());
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
                     criteria.add(MemberPeer.PROJECT_DATA_ID, getId());
                     if (!lastMembersCriteria.equals(criteria))
                 {
                     collMembers = MemberPeer.doSelect(criteria, con);
                 }
             }
         }
         lastMembersCriteria = criteria;

         return collMembers;
     }

        

     
      
      
          
                    
                
        
        
    
      
      
          
                    
                
        
        
    
      
      
          
                    
                
        
        
    
      
      
          
                    
                
        
        
    
      
         
          
                    
                
        
        
    
      
      
          
                    
                
        
        
      



             
      
    /**
     * Collection to store aggregation of collProjectProjectTypes
     */
    protected List collProjectProjectTypes;

    /**
     * Temporary storage of collProjectProjectTypes to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initProjectProjectTypes()
    {
        if (collProjectProjectTypes == null)
        {
            collProjectProjectTypes = new ArrayList();
        }
    }

    /**
     * Method called to associate a ProjectProjectType object to this object
     * through the ProjectProjectType foreign key attribute
     *
     * @param l ProjectProjectType
     * @throws TorqueException
     */
    public void addProjectProjectType(ProjectProjectType l) throws TorqueException
    {
        getProjectProjectTypes().add(l);
        l.setProjectData((ProjectData) this);
    }

    /**
     * The criteria used to select the current contents of collProjectProjectTypes
     */
    private Criteria lastProjectProjectTypesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getProjectProjectTypes(new Criteria())
     *
     * @throws TorqueException
     */
    public List getProjectProjectTypes() throws TorqueException
    {
        if (collProjectProjectTypes == null)
        {
            collProjectProjectTypes = getProjectProjectTypes(new Criteria(10));
        }
        return collProjectProjectTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this ProjectData has previously
     * been saved, it will retrieve related ProjectProjectTypes from storage.
     * If this ProjectData is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List getProjectProjectTypes(Criteria criteria) throws TorqueException
    {
        if (collProjectProjectTypes == null)
        {
            if (isNew())
            {
               collProjectProjectTypes = new ArrayList();
            }
            else
            {
                   criteria.add(ProjectProjectTypePeer.PROJECT_DATA_ID, getId() );
                   collProjectProjectTypes = ProjectProjectTypePeer.doSelect(criteria);
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
                   criteria.add(ProjectProjectTypePeer.PROJECT_DATA_ID, getId());
                   if (!lastProjectProjectTypesCriteria.equals(criteria))
                {
                    collProjectProjectTypes = ProjectProjectTypePeer.doSelect(criteria);
                }
            }
        }
        lastProjectProjectTypesCriteria = criteria;

        return collProjectProjectTypes;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getProjectProjectTypes(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List getProjectProjectTypes(Connection con) throws TorqueException
    {
        if (collProjectProjectTypes == null)
        {
            collProjectProjectTypes = getProjectProjectTypes(new Criteria(10), con);
        }
        return collProjectProjectTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this ProjectData has previously
     * been saved, it will retrieve related ProjectProjectTypes from storage.
     * If this ProjectData is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List getProjectProjectTypes(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collProjectProjectTypes == null)
        {
            if (isNew())
            {
               collProjectProjectTypes = new ArrayList();
            }
            else
            {
                     criteria.add(ProjectProjectTypePeer.PROJECT_DATA_ID, getId());
                     collProjectProjectTypes = ProjectProjectTypePeer.doSelect(criteria, con);
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
                     criteria.add(ProjectProjectTypePeer.PROJECT_DATA_ID, getId());
                     if (!lastProjectProjectTypesCriteria.equals(criteria))
                 {
                     collProjectProjectTypes = ProjectProjectTypePeer.doSelect(criteria, con);
                 }
             }
         }
         lastProjectProjectTypesCriteria = criteria;

         return collProjectProjectTypes;
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
            fieldNames.add("Name");
            fieldNames.add("ParentOrganization");
            fieldNames.add("Divisions");
            fieldNames.add("People");
            fieldNames.add("MailingList");
            fieldNames.add("Description");
            fieldNames.add("History");
            fieldNames.add("EducationPrograms");
            fieldNames.add("Resources");
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
            if (name.equals("Name"))
    {
              return getName();
          }
            if (name.equals("ParentOrganization"))
    {
              return getParentOrganization();
          }
            if (name.equals("Divisions"))
    {
              return getDivisions();
          }
            if (name.equals("People"))
    {
              return getPeople();
          }
            if (name.equals("MailingList"))
    {
              return getMailingList();
          }
            if (name.equals("Description"))
    {
              return getDescription();
          }
            if (name.equals("History"))
    {
              return getHistory();
          }
            if (name.equals("EducationPrograms"))
    {
              return getEducationPrograms();
          }
            if (name.equals("Resources"))
    {
              return getResources();
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
            if (name.equals(ProjectDataPeer.ID))
        {
              return getId();
          }
            if (name.equals(ProjectDataPeer.NAME))
        {
              return getName();
          }
            if (name.equals(ProjectDataPeer.PARENT_ORGANIZATION))
        {
              return getParentOrganization();
          }
            if (name.equals(ProjectDataPeer.DIVISIONS))
        {
              return getDivisions();
          }
            if (name.equals(ProjectDataPeer.PEOPLE))
        {
              return getPeople();
          }
            if (name.equals(ProjectDataPeer.MAILING_LIST))
        {
              return getMailingList();
          }
            if (name.equals(ProjectDataPeer.DESCRIPTION))
        {
              return getDescription();
          }
            if (name.equals(ProjectDataPeer.HISTORY))
        {
              return getHistory();
          }
            if (name.equals(ProjectDataPeer.EDUCATION_PROGRAMS))
        {
              return getEducationPrograms();
          }
            if (name.equals(ProjectDataPeer.RESOURCES))
        {
              return getResources();
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
              return getName();
          }
            if (pos == 2)
    {
              return getParentOrganization();
          }
            if (pos == 3)
    {
              return getDivisions();
          }
            if (pos == 4)
    {
              return getPeople();
          }
            if (pos == 5)
    {
              return getMailingList();
          }
            if (pos == 6)
    {
              return getDescription();
          }
            if (pos == 7)
    {
              return getHistory();
          }
            if (pos == 8)
    {
              return getEducationPrograms();
          }
            if (pos == 9)
    {
              return getResources();
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
             save(ProjectDataPeer.getMapBuilder()
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
                ProjectDataPeer.doInsert((ProjectData) this, con);
                setNew(false);
            }
            else
            {
                ProjectDataPeer.doUpdate((ProjectData) this, con);
            }
        }

                                    
                
          if (collMembers != null)
          {
              for (int i = 0; i < collMembers.size(); i++)
              {
                  ((Member) collMembers.get(i)).save(con);
              }
          }
                                        
                
          if (collProjectProjectTypes != null)
          {
              for (int i = 0; i < collProjectProjectTypes.size(); i++)
              {
                  ((ProjectProjectType) collProjectProjectTypes.get(i)).save(con);
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
    public ProjectData copy() throws TorqueException
    {
        return copyInto(new ProjectData());
    }

    protected ProjectData copyInto(ProjectData copyObj) throws TorqueException
    {
        copyObj.setId(id);
        copyObj.setName(name);
        copyObj.setParentOrganization(parent_organization);
        copyObj.setDivisions(divisions);
        copyObj.setPeople(people);
        copyObj.setMailingList(mailing_list);
        copyObj.setDescription(description);
        copyObj.setHistory(history);
        copyObj.setEducationPrograms(education_programs);
        copyObj.setResources(resources);

  copyObj.setNew(false);
                                    
                
        List v = getMembers();
        for (int i = 0; i < v.size(); i++)
        {
            Member obj = (Member) v.get(i);
            copyObj.addMember(obj.copy());
            ((Persistent) v.get(i)).setNew(true);
        }
                                              
                
        v = getProjectProjectTypes();
        for (int i = 0; i < v.size(); i++)
        {
            ProjectProjectType obj = (ProjectProjectType) v.get(i);
            copyObj.addProjectProjectType(obj.copy());
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
    public ProjectDataPeer getPeer()
    {
        return peer;
    }
}
