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
 * extended all references should be to DocumentType
 */
public abstract class BaseDocumentType extends BaseObject
{
    /** The Peer class */
    private static final DocumentTypePeer peer =
        new DocumentTypePeer();

                  
        /**
         * The value for the id field
         */
        private Integer id;
              
        /**
         * The value for the document_type field
         */
        private String document_type;
      
      
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
                          .setDocumentTypeId(v);
                  }
              }
                                   }


        /**
         * Get the DocumentType
         *
         * @return String
         */
        public String getDocumentType()
        {
            return document_type;
        }

                                            
        /**
         * Set the value of DocumentType
         *
         * @param v new value
         */
        public void setDocumentType(String v) 
        {
          


         if (!ObjectUtils.equals(this.document_type, v))
        {
             this.document_type = v;
            setModified(true);
        }

                  
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
        l.setDocumentType((DocumentType) this);
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
     * Otherwise if this DocumentType has previously
     * been saved, it will retrieve related Documents from storage.
     * If this DocumentType is new, it will return
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
                   criteria.add(DocumentPeer.DOCUMENT_TYPE_ID, getId() );
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
                   criteria.add(DocumentPeer.DOCUMENT_TYPE_ID, getId());
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
     * Otherwise if this DocumentType has previously
     * been saved, it will retrieve related Documents from storage.
     * If this DocumentType is new, it will return
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
                     criteria.add(DocumentPeer.DOCUMENT_TYPE_ID, getId());
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
                     criteria.add(DocumentPeer.DOCUMENT_TYPE_ID, getId());
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
            fieldNames.add("DocumentType");
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
            if (name.equals("DocumentType"))
    {
              return getDocumentType();
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
            if (name.equals(DocumentTypePeer.ID))
        {
              return getId();
          }
            if (name.equals(DocumentTypePeer.DOCUMENT_TYPE))
        {
              return getDocumentType();
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
              return getDocumentType();
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
             save(DocumentTypePeer.getMapBuilder()
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
                DocumentTypePeer.doInsert((DocumentType) this, con);
                setNew(false);
            }
            else
            {
                DocumentTypePeer.doUpdate((DocumentType) this, con);
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
    public DocumentType copy() throws TorqueException
    {
        return copyInto(new DocumentType());
    }

    protected DocumentType copyInto(DocumentType copyObj) throws TorqueException
    {
        copyObj.setId(id);
        copyObj.setDocumentType(document_type);

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
    public DocumentTypePeer getPeer()
    {
        return peer;
    }
}
