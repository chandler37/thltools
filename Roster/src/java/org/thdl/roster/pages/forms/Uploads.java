package org.thdl.roster.pages.forms;

import java.io.*;
import java.util.*;

import org.thdl.roster.*;
import org.thdl.roster.om.*;
import org.thdl.roster.pages.*;

import org.apache.tapestry.*;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.form.IPropertySelectionModel;

import org.apache.commons.lang.exception.NestableException;

import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

/**
 *  Description of the Class
 *
 *@author     travis
 *@created    March 11, 2003
 */
public class Uploads extends MemberFormSeries {
//attributes
	private static final String[] ACCEPTED_MIME_TYPES = { "image/gif", "image/jpeg", "text/html", "text/plain", "application/pdf", "application/rtf", "application/msword", "application/vnd.ms-powerpoint", "application/zip", "application/x-zip-compressed" };
   private Document document;
	private IUploadFile file;
	private Integer documentType;
	private IPropertySelectionModel documentTypeModel;
//accessors
	public void setDocument(Document document) {
		this.document = document;
	}
	public Document getDocument() {
		return document;
	}
	public void setFile(IUploadFile file) {
		this.file = file;
	}
	public IUploadFile getFile() {
		return file;
	}
	public void setDocumentType(Integer documentType) {
		this.documentType = documentType;
	}
	public Integer getDocumentType() {
		return documentType;
	}
	public void setDocumentTypeModel(IPropertySelectionModel documentTypeModel) {
		this.documentTypeModel = documentTypeModel;
	}
	public IPropertySelectionModel getDocumentTypeModel() {
		if (null== documentTypeModel)
		{
			setDocumentTypeModel( buildDocumentTypeModel() );
		}
		return documentTypeModel;
	}
//helpers
	public IPropertySelectionModel buildDocumentTypeModel() {
		LinkedList list;
		try {
			Criteria crit = new Criteria();
			crit.addAscendingOrderByColumn( DocumentTypePeer.DOCUMENT_TYPE );
			list = new LinkedList(BaseDocumentTypePeer.doSelect(crit));
		} catch (TorqueException te) {
			throw new ApplicationRuntimeException("Torque Exception says: " + te.getMessage(), te);
		}
		EntitySelectionModel documentTypeModel = new EntitySelectionModel();
		ListIterator looper = list.listIterator(0);
		while ( looper.hasNext() ) {
			DocumentType documentType = (DocumentType) looper.next();
			documentTypeModel.add(documentType.getId(), documentType.getDocumentType());
		}
		return documentTypeModel;
	}

	/**
	 *  Description of the Method
	 *
	 *@param  cycle  Description of the Parameter
	 */
	public void processForm(IRequestCycle cycle) 
	{
		 Visit visit = (Visit) getVisit();
       Member member = (Member) visit.getMember();
		 processFile( cycle, member );
		 try
		 {
			 member.save();
		 }
		 catch (Exception e)
		 {
			 throw new ApplicationRuntimeException( e );
		 }
	}

	public boolean mimeTypeOk()
	{
		boolean pass = false;
		for (int index = 0; index < ACCEPTED_MIME_TYPES.length; index++)
		{
			if ( getFile().getContentType().equals( ACCEPTED_MIME_TYPES[ index ] ) )
			{
				pass = true;
			}
		}
		return pass;
	}
	
	public boolean tokensValidate()
	{
		Visit visit = (Visit) getVisit();
		String visitToken = visit.getToken();
		String pageToken = getToken();

		boolean validTokens = false;
		if ( null != visitToken && null != pageToken && visitToken.equals( pageToken ) )
		{
			validTokens = true;
		}
		return validTokens;
	}
	
	public void processFile( IRequestCycle cycle, Member member )
	{
		String filename = null;
		int streamLength = 0;

		try 
		{
			streamLength = getFile().getStream().available() ;
		}
		catch (IOException ioe )
		{
			throw new ApplicationRuntimeException( ioe );
		}
		
		Document doc = null;
		
		if ( streamLength < 8 )
		{
			return;
		}
		else if ( ! mimeTypeOk() )
		{
			RosterPage page = (RosterPage)cycle.getPage( "Uploads" );
			page.setWarning( "Invalid File Type: " + getFile().getContentType() );
			page.validate( cycle );
			throw new PageRedirectException( page );
		}
		else
		{
			StringTokenizer st = new StringTokenizer( getFile().getFileName(), "\\" );
			while ( st.hasMoreTokens() )
			{
				filename = st.nextToken();
			}

			try
			{
				doc = new Document();
				doc.setMemberId( member.getId() );
				doc.setFilename( filename );
				doc.setContentType( getFile().getContentType() );
				doc.setPath( "/roster/uploads/" );
				doc.setDocumentTypeId( getDocumentType() );
				doc.save();
				member.addDocument( doc );
			}
			catch (Exception e) 
			{
				throw new ApplicationRuntimeException( e );
			}
		}

		InputStream fis = getFile().getStream(); 
		FileOutputStream fos = null;

		String absolutePath = cycle.getRequestContext().getServlet().getInitParameter( "roster-uploads-directory" );
		File aFile = new  File( absolutePath + filename );
		int uniqueifier = 1;

		while ( aFile.exists() )
		{
			uniqueifier++;
			StringTokenizer filenameTokens = new StringTokenizer( filename, ".", true );
			StringBuffer newFilename = new StringBuffer();
			while ( filenameTokens.hasMoreTokens() )
			{
				String tok = filenameTokens.nextToken();
				if ( tok.equals( "." ) )
				{
					newFilename.append( uniqueifier );
				}
				newFilename.append( tok );
			}
			aFile = new File( absolutePath + newFilename.toString() );
			doc.setFilename( newFilename.toString() );
		}

		try
		{
				fos = new FileOutputStream( aFile );
		}
		catch (IOException ioe) 
		{
			throw new ApplicationRuntimeException( ioe );
		}

		try
		{
			byte[] buffer = new byte[1024];  
			while ( true ) 
			{            
				 int length = fis.read( buffer ); 
				 if (length <  0) 
				 {
					  break;
				 }
				 fos.write(buffer, 0, length);               
			}
			fis.close();
			fos.close();
		}
		catch (IOException ioe) 
		{
			throw new ApplicationRuntimeException( ioe );
		} 
		finally 
		{
			if (fis != null) 
			{
				try 
				{
					fis.close(); 
				} 
				catch (IOException ioe) 
				{
					throw new ApplicationRuntimeException( ioe );
				}
			}   
			if (fos != null) 
			{
				try 
				{
					fos.close();
				} 
				catch (IOException ioe) 
				{
					throw new ApplicationRuntimeException( ioe );
				}
			}
		}
	}
	
	public void deleteFile( IRequestCycle cycle )
	{
		Integer docId = (Integer) cycle.getServiceParameters()[0];
		Document doc = null;
		try
		{
			doc = DocumentPeer.retrieveByPK( docId );
		}
		catch (TorqueException te )
		{
			throw new ApplicationRuntimeException( te.getMessage(), te );
		}
		

		File file = null;
		String warning = null;
		String message = null;
		if (null != doc)
		{
			String absolutePath = cycle.getRequestContext().getServlet().getInitParameter( "roster-uploads-directory" );
			file = new File( absolutePath + doc.getFilename() );
			file.delete();
			message = "Your document, " + doc.getFilename() + ", was deleted.";
			try
			{
				DocumentPeer.doDelete( doc );
			}
			catch (TorqueException te )
			{
				throw new ApplicationRuntimeException( te.getMessage(), te );
			}
		}

		try
		{
			Visit visit = (Visit) getVisit();
			Member member = (Member)visit.getMember();
			member.getDocuments( new Criteria() );
		}
		catch (Exception te )
		{
			throw new ApplicationRuntimeException( te.getMessage(), te );
		}
	}
				
			
    public void detach()
    {
        setNextPage( "Home" );
        super.detach();
    }

	 public Uploads()
	 {
		try
		{
			if ( !Torque.isInit() )
			{
				Global global = (Global) getGlobal();
				Torque.init( global.getTorqueConfig() );
			}
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( "Torque Exception says: " + te.getMessage(), te );
		}
		  setNextPage( "Home" );
	 }
}

