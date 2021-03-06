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
 * extended all references should be to Member
 */
public abstract class BaseMember extends BaseObject
{
    /** The Peer class */
    private static final MemberPeer peer =
        new MemberPeer();

                  
        /**
         * The value for the id field
         */
        private Integer id;
              
        /**
         * The value for the created_by field
         */
        private Integer created_by;
              
        /**
         * The value for the modified_by field
         */
        private Integer modified_by;
              
        /**
         * The value for the created_on field
         */
        private Date created_on;
              
        /**
         * The value for the modified_on field
         */
        private Date modified_on;
              
        /**
         * The value for the deleted field
         */
        private String deleted;
              
        /**
         * The value for the contact_info_id field
         */
        private Integer contact_info_id;
              
        /**
         * The value for the research_interest_id field
         */
        private Integer research_interest_id;
              
        /**
         * The value for the publication_id field
         */
        private Integer publication_id;
              
        /**
         * The value for the member_type field
         */
        private String member_type;
              
        /**
         * The value for the person_data_id field
         */
        private Integer person_data_id;
              
        /**
         * The value for the project_data_id field
         */
        private Integer project_data_id;
              
        /**
         * The value for the organization_data_id field
         */
        private Integer organization_data_id;
      
      
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

                  
                                                  
              // update associated Document
              if (collDocuments != null)
              {
                  for (int i = 0; i < collDocuments.size(); i++)
                  {
                      ((Document) collDocuments.get(i))
                          .setMemberId(v);
                  }
              }
                                   }


        /**
         * Get the CreatedBy
         *
         * @return Integer
         */
        public Integer getCreatedBy()
        {
            return created_by;
        }

                                            
        /**
         * Set the value of CreatedBy
         *
         * @param v new value
         */
        public void setCreatedBy(Integer v) 
        {
          


         if (!ObjectUtils.equals(this.created_by, v))
        {
             this.created_by = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the ModifiedBy
         *
         * @return Integer
         */
        public Integer getModifiedBy()
        {
            return modified_by;
        }

                                            
        /**
         * Set the value of ModifiedBy
         *
         * @param v new value
         */
        public void setModifiedBy(Integer v) 
        {
          


         if (!ObjectUtils.equals(this.modified_by, v))
        {
             this.modified_by = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the CreatedOn
         *
         * @return Date
         */
        public Date getCreatedOn()
        {
            return created_on;
        }

                                            
        /**
         * Set the value of CreatedOn
         *
         * @param v new value
         */
        public void setCreatedOn(Date v) 
        {
          


         if (!ObjectUtils.equals(this.created_on, v))
        {
             this.created_on = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the ModifiedOn
         *
         * @return Date
         */
        public Date getModifiedOn()
        {
            return modified_on;
        }

                                            
        /**
         * Set the value of ModifiedOn
         *
         * @param v new value
         */
        public void setModifiedOn(Date v) 
        {
          


         if (!ObjectUtils.equals(this.modified_on, v))
        {
             this.modified_on = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the Deleted
         *
         * @return String
         */
        public String getDeleted()
        {
            return deleted;
        }

                                            
        /**
         * Set the value of Deleted
         *
         * @param v new value
         */
        public void setDeleted(String v) 
        {
          


         if (!ObjectUtils.equals(this.deleted, v))
        {
             this.deleted = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the ContactInfoId
         *
         * @return Integer
         */
        public Integer getContactInfoId()
        {
            return contact_info_id;
        }

                                                      
        /**
         * Set the value of ContactInfoId
         *
         * @param v new value
         */
        public void setContactInfoId(Integer v) throws TorqueException
        {
          


         if (!ObjectUtils.equals(this.contact_info_id, v))
        {
             this.contact_info_id = v;
            setModified(true);
        }

                                          
                if (aContactInfo != null && !ObjectUtils.equals(aContactInfo.getId(), v))
                {
            aContactInfo = null;
        }
          
                       }


        /**
         * Get the ResearchInterestId
         *
         * @return Integer
         */
        public Integer getResearchInterestId()
        {
            return research_interest_id;
        }

                                                      
        /**
         * Set the value of ResearchInterestId
         *
         * @param v new value
         */
        public void setResearchInterestId(Integer v) throws TorqueException
        {
          


         if (!ObjectUtils.equals(this.research_interest_id, v))
        {
             this.research_interest_id = v;
            setModified(true);
        }

                                          
                if (aResearchInterest != null && !ObjectUtils.equals(aResearchInterest.getId(), v))
                {
            aResearchInterest = null;
        }
          
                       }


        /**
         * Get the PublicationId
         *
         * @return Integer
         */
        public Integer getPublicationId()
        {
            return publication_id;
        }

                                                      
        /**
         * Set the value of PublicationId
         *
         * @param v new value
         */
        public void setPublicationId(Integer v) throws TorqueException
        {
          


         if (!ObjectUtils.equals(this.publication_id, v))
        {
             this.publication_id = v;
            setModified(true);
        }

                                          
                if (aPublication != null && !ObjectUtils.equals(aPublication.getId(), v))
                {
            aPublication = null;
        }
          
                       }


        /**
         * Get the MemberType
         *
         * @return String
         */
        public String getMemberType()
        {
            return member_type;
        }

                                            
        /**
         * Set the value of MemberType
         *
         * @param v new value
         */
        public void setMemberType(String v) 
        {
          


         if (!ObjectUtils.equals(this.member_type, v))
        {
             this.member_type = v;
            setModified(true);
        }

                  
                       }


        /**
         * Get the PersonDataId
         *
         * @return Integer
         */
        public Integer getPersonDataId()
        {
            return person_data_id;
        }

                                                      
        /**
         * Set the value of PersonDataId
         *
         * @param v new value
         */
        public void setPersonDataId(Integer v) throws TorqueException
        {
          


         if (!ObjectUtils.equals(this.person_data_id, v))
        {
             this.person_data_id = v;
            setModified(true);
        }

                                          
                if (aPersonData != null && !ObjectUtils.equals(aPersonData.getId(), v))
                {
            aPersonData = null;
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
         * Get the OrganizationDataId
         *
         * @return Integer
         */
        public Integer getOrganizationDataId()
        {
            return organization_data_id;
        }

                                                      
        /**
         * Set the value of OrganizationDataId
         *
         * @param v new value
         */
        public void setOrganizationDataId(Integer v) throws TorqueException
        {
          


         if (!ObjectUtils.equals(this.organization_data_id, v))
        {
             this.organization_data_id = v;
            setModified(true);
        }

                                          
                if (aOrganizationData != null && !ObjectUtils.equals(aOrganizationData.getId(), v))
                {
            aOrganizationData = null;
        }
          
                       }


 
     
   
             
   
       private ContactInfo aContactInfo;

    /**
     * Declares an association between this object and a ContactInfo object
     *
     * @param v ContactInfo
     * @throws TorqueException
     */
    public void setContactInfo(ContactInfo v) throws TorqueException
    {
           if (v == null)
        {
                         setContactInfoId((Integer)null);
                    }
        else
        {
            setContactInfoId(v.getId());
        }
           aContactInfo = v;
    }

                 
    /**
     * Get the associated ContactInfo object
     *
     * @return the associated ContactInfo object
     * @throws TorqueException
     */
    public ContactInfo getContactInfo() throws TorqueException
    {
        if (aContactInfo == null && (!ObjectUtils.equals(this.contact_info_id, null)))
        {
              aContactInfo = ContactInfoPeer.retrieveByPK(SimpleKey.keyFor(this.contact_info_id));
      
            /* The following can be used instead of the line above to
               guarantee the related object contains a reference
               to this object, but this level of coupling
               may be undesirable in many circumstances.
               As it can lead to a db query with many results that may
               never be used.
               ContactInfo obj = ContactInfoPeer.retrieveByPK(this.contact_info_id);
               obj.addMembers(this);
             */
        }
        return aContactInfo;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey.  e.g.
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setContactInfoKey(ObjectKey key) throws TorqueException
    {
    
                                        setContactInfoId(new Integer(((NumberKey) key).intValue()));
                    }
 
   
             
   
       private ResearchInterest aResearchInterest;

    /**
     * Declares an association between this object and a ResearchInterest object
     *
     * @param v ResearchInterest
     * @throws TorqueException
     */
    public void setResearchInterest(ResearchInterest v) throws TorqueException
    {
           if (v == null)
        {
                         setResearchInterestId((Integer)null);
                    }
        else
        {
            setResearchInterestId(v.getId());
        }
           aResearchInterest = v;
    }

                 
    /**
     * Get the associated ResearchInterest object
     *
     * @return the associated ResearchInterest object
     * @throws TorqueException
     */
    public ResearchInterest getResearchInterest() throws TorqueException
    {
        if (aResearchInterest == null && (!ObjectUtils.equals(this.research_interest_id, null)))
        {
              aResearchInterest = ResearchInterestPeer.retrieveByPK(SimpleKey.keyFor(this.research_interest_id));
      
            /* The following can be used instead of the line above to
               guarantee the related object contains a reference
               to this object, but this level of coupling
               may be undesirable in many circumstances.
               As it can lead to a db query with many results that may
               never be used.
               ResearchInterest obj = ResearchInterestPeer.retrieveByPK(this.research_interest_id);
               obj.addMembers(this);
             */
        }
        return aResearchInterest;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey.  e.g.
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setResearchInterestKey(ObjectKey key) throws TorqueException
    {
    
                                        setResearchInterestId(new Integer(((NumberKey) key).intValue()));
                    }
 
   
             
   
       private Publication aPublication;

    /**
     * Declares an association between this object and a Publication object
     *
     * @param v Publication
     * @throws TorqueException
     */
    public void setPublication(Publication v) throws TorqueException
    {
           if (v == null)
        {
                         setPublicationId((Integer)null);
                    }
        else
        {
            setPublicationId(v.getId());
        }
           aPublication = v;
    }

                 
    /**
     * Get the associated Publication object
     *
     * @return the associated Publication object
     * @throws TorqueException
     */
    public Publication getPublication() throws TorqueException
    {
        if (aPublication == null && (!ObjectUtils.equals(this.publication_id, null)))
        {
              aPublication = PublicationPeer.retrieveByPK(SimpleKey.keyFor(this.publication_id));
      
            /* The following can be used instead of the line above to
               guarantee the related object contains a reference
               to this object, but this level of coupling
               may be undesirable in many circumstances.
               As it can lead to a db query with many results that may
               never be used.
               Publication obj = PublicationPeer.retrieveByPK(this.publication_id);
               obj.addMembers(this);
             */
        }
        return aPublication;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey.  e.g.
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setPublicationKey(ObjectKey key) throws TorqueException
    {
    
                                        setPublicationId(new Integer(((NumberKey) key).intValue()));
                    }
 
   
             
   
       private PersonData aPersonData;

    /**
     * Declares an association between this object and a PersonData object
     *
     * @param v PersonData
     * @throws TorqueException
     */
    public void setPersonData(PersonData v) throws TorqueException
    {
           if (v == null)
        {
                         setPersonDataId((Integer)null);
                    }
        else
        {
            setPersonDataId(v.getId());
        }
           aPersonData = v;
    }

                 
    /**
     * Get the associated PersonData object
     *
     * @return the associated PersonData object
     * @throws TorqueException
     */
    public PersonData getPersonData() throws TorqueException
    {
        if (aPersonData == null && (!ObjectUtils.equals(this.person_data_id, null)))
        {
              aPersonData = PersonDataPeer.retrieveByPK(SimpleKey.keyFor(this.person_data_id));
      
            /* The following can be used instead of the line above to
               guarantee the related object contains a reference
               to this object, but this level of coupling
               may be undesirable in many circumstances.
               As it can lead to a db query with many results that may
               never be used.
               PersonData obj = PersonDataPeer.retrieveByPK(this.person_data_id);
               obj.addMembers(this);
             */
        }
        return aPersonData;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey.  e.g.
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setPersonDataKey(ObjectKey key) throws TorqueException
    {
    
                                        setPersonDataId(new Integer(((NumberKey) key).intValue()));
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
               obj.addMembers(this);
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
 
   
             
   
       private OrganizationData aOrganizationData;

    /**
     * Declares an association between this object and a OrganizationData object
     *
     * @param v OrganizationData
     * @throws TorqueException
     */
    public void setOrganizationData(OrganizationData v) throws TorqueException
    {
           if (v == null)
        {
                         setOrganizationDataId((Integer)null);
                    }
        else
        {
            setOrganizationDataId(v.getId());
        }
           aOrganizationData = v;
    }

                 
    /**
     * Get the associated OrganizationData object
     *
     * @return the associated OrganizationData object
     * @throws TorqueException
     */
    public OrganizationData getOrganizationData() throws TorqueException
    {
        if (aOrganizationData == null && (!ObjectUtils.equals(this.organization_data_id, null)))
        {
              aOrganizationData = OrganizationDataPeer.retrieveByPK(SimpleKey.keyFor(this.organization_data_id));
      
            /* The following can be used instead of the line above to
               guarantee the related object contains a reference
               to this object, but this level of coupling
               may be undesirable in many circumstances.
               As it can lead to a db query with many results that may
               never be used.
               OrganizationData obj = OrganizationDataPeer.retrieveByPK(this.organization_data_id);
               obj.addMembers(this);
             */
        }
        return aOrganizationData;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey.  e.g.
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setOrganizationDataKey(ObjectKey key) throws TorqueException
    {
    
                                        setOrganizationDataId(new Integer(((NumberKey) key).intValue()));
                    }
    
                
      
    /**
     * Collection to store aggregation of collDocuments
     */
    protected List collDocuments;

    /**
     * Temporary storage of collDocuments to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initDocuments()
    {
        if (collDocuments == null)
        {
            collDocuments = new ArrayList();
        }
    }

    /**
     * Method called to associate a Document object to this object
     * through the Document foreign key attribute
     *
     * @param l Document
     * @throws TorqueException
     */
    public void addDocument(Document l) throws TorqueException
    {
        getDocuments().add(l);
        l.setMember((Member) this);
    }

    /**
     * The criteria used to select the current contents of collDocuments
     */
    private Criteria lastDocumentsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getDocuments(new Criteria())
     *
     * @throws TorqueException
     */
    public List getDocuments() throws TorqueException
    {
        if (collDocuments == null)
        {
            collDocuments = getDocuments(new Criteria(10));
        }
        return collDocuments;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this Member has previously
     * been saved, it will retrieve related Documents from storage.
     * If this Member is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List getDocuments(Criteria criteria) throws TorqueException
    {
        if (collDocuments == null)
        {
            if (isNew())
            {
               collDocuments = new ArrayList();
            }
            else
            {
                   criteria.add(DocumentPeer.MEMBER_ID, getId() );
                   collDocuments = DocumentPeer.doSelect(criteria);
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
                   criteria.add(DocumentPeer.MEMBER_ID, getId());
                   if (!lastDocumentsCriteria.equals(criteria))
                {
                    collDocuments = DocumentPeer.doSelect(criteria);
                }
            }
        }
        lastDocumentsCriteria = criteria;

        return collDocuments;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getDocuments(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List getDocuments(Connection con) throws TorqueException
    {
        if (collDocuments == null)
        {
            collDocuments = getDocuments(new Criteria(10), con);
        }
        return collDocuments;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this Member has previously
     * been saved, it will retrieve related Documents from storage.
     * If this Member is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List getDocuments(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collDocuments == null)
        {
            if (isNew())
            {
               collDocuments = new ArrayList();
            }
            else
            {
                     criteria.add(DocumentPeer.MEMBER_ID, getId());
                     collDocuments = DocumentPeer.doSelect(criteria, con);
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
                     criteria.add(DocumentPeer.MEMBER_ID, getId());
                     if (!lastDocumentsCriteria.equals(criteria))
                 {
                     collDocuments = DocumentPeer.doSelect(criteria, con);
                 }
             }
         }
         lastDocumentsCriteria = criteria;

         return collDocuments;
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
            fieldNames.add("CreatedBy");
            fieldNames.add("ModifiedBy");
            fieldNames.add("CreatedOn");
            fieldNames.add("ModifiedOn");
            fieldNames.add("Deleted");
            fieldNames.add("ContactInfoId");
            fieldNames.add("ResearchInterestId");
            fieldNames.add("PublicationId");
            fieldNames.add("MemberType");
            fieldNames.add("PersonDataId");
            fieldNames.add("ProjectDataId");
            fieldNames.add("OrganizationDataId");
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
            if (name.equals("CreatedBy"))
    {
              return getCreatedBy();
          }
            if (name.equals("ModifiedBy"))
    {
              return getModifiedBy();
          }
            if (name.equals("CreatedOn"))
    {
              return getCreatedOn();
          }
            if (name.equals("ModifiedOn"))
    {
              return getModifiedOn();
          }
            if (name.equals("Deleted"))
    {
              return getDeleted();
          }
            if (name.equals("ContactInfoId"))
    {
              return getContactInfoId();
          }
            if (name.equals("ResearchInterestId"))
    {
              return getResearchInterestId();
          }
            if (name.equals("PublicationId"))
    {
              return getPublicationId();
          }
            if (name.equals("MemberType"))
    {
              return getMemberType();
          }
            if (name.equals("PersonDataId"))
    {
              return getPersonDataId();
          }
            if (name.equals("ProjectDataId"))
    {
              return getProjectDataId();
          }
            if (name.equals("OrganizationDataId"))
    {
              return getOrganizationDataId();
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
            if (name.equals(MemberPeer.ID))
        {
              return getId();
          }
            if (name.equals(MemberPeer.CREATED_BY))
        {
              return getCreatedBy();
          }
            if (name.equals(MemberPeer.MODIFIED_BY))
        {
              return getModifiedBy();
          }
            if (name.equals(MemberPeer.CREATED_ON))
        {
              return getCreatedOn();
          }
            if (name.equals(MemberPeer.MODIFIED_ON))
        {
              return getModifiedOn();
          }
            if (name.equals(MemberPeer.DELETED))
        {
              return getDeleted();
          }
            if (name.equals(MemberPeer.CONTACT_INFO_ID))
        {
              return getContactInfoId();
          }
            if (name.equals(MemberPeer.RESEARCH_INTEREST_ID))
        {
              return getResearchInterestId();
          }
            if (name.equals(MemberPeer.PUBLICATION_ID))
        {
              return getPublicationId();
          }
            if (name.equals(MemberPeer.MEMBER_TYPE))
        {
              return getMemberType();
          }
            if (name.equals(MemberPeer.PERSON_DATA_ID))
        {
              return getPersonDataId();
          }
            if (name.equals(MemberPeer.PROJECT_DATA_ID))
        {
              return getProjectDataId();
          }
            if (name.equals(MemberPeer.ORGANIZATION_DATA_ID))
        {
              return getOrganizationDataId();
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
              return getCreatedBy();
          }
            if (pos == 2)
    {
              return getModifiedBy();
          }
            if (pos == 3)
    {
              return getCreatedOn();
          }
            if (pos == 4)
    {
              return getModifiedOn();
          }
            if (pos == 5)
    {
              return getDeleted();
          }
            if (pos == 6)
    {
              return getContactInfoId();
          }
            if (pos == 7)
    {
              return getResearchInterestId();
          }
            if (pos == 8)
    {
              return getPublicationId();
          }
            if (pos == 9)
    {
              return getMemberType();
          }
            if (pos == 10)
    {
              return getPersonDataId();
          }
            if (pos == 11)
    {
              return getProjectDataId();
          }
            if (pos == 12)
    {
              return getOrganizationDataId();
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
             save(MemberPeer.getMapBuilder()
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
                MemberPeer.doInsert((Member) this, con);
                setNew(false);
            }
            else
            {
                MemberPeer.doUpdate((Member) this, con);
            }
        }

                                    
                
          if (collDocuments != null)
          {
              for (int i = 0; i < collDocuments.size(); i++)
              {
                  ((Document) collDocuments.get(i)).save(con);
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
    public Member copy() throws TorqueException
    {
        return copyInto(new Member());
    }

    protected Member copyInto(Member copyObj) throws TorqueException
    {
        copyObj.setId(id);
        copyObj.setCreatedBy(created_by);
        copyObj.setModifiedBy(modified_by);
        copyObj.setCreatedOn(created_on);
        copyObj.setModifiedOn(modified_on);
        copyObj.setDeleted(deleted);
        copyObj.setContactInfoId(contact_info_id);
        copyObj.setResearchInterestId(research_interest_id);
        copyObj.setPublicationId(publication_id);
        copyObj.setMemberType(member_type);
        copyObj.setPersonDataId(person_data_id);
        copyObj.setProjectDataId(project_data_id);
        copyObj.setOrganizationDataId(organization_data_id);

  copyObj.setNew(false);
                                    
                
        List v = getDocuments();
        for (int i = 0; i < v.size(); i++)
        {
            Document obj = (Document) v.get(i);
            copyObj.addDocument(obj.copy());
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
    public MemberPeer getPeer()
    {
        return peer;
    }
}
