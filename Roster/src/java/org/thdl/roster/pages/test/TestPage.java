package org.thdl.roster.pages.test;

import org.thdl.roster.*;
import org.thdl.roster.om.*;
import org.thdl.roster.pages.*;
import org.thdl.roster.pages.test.*;
import org.apache.tapestry.*;
import org.apache.tapestry.html.*;
import java.util.*;
import org.apache.tapestry.request.IUploadFile;

public class TestPage extends org.thdl.roster.pages.RosterPage //org.apache.tapestry.html.BasePage
{
	private static final String[] ACCEPTED_MIME_TYPES = { "image/gif", "image/jpeg", "text/html", "text/plain", "application/pdf", "application/rtf", "application/msword", "application/vnd.ms-powerpoint", "application/zip", "application/x-zip-compressed" };

	private List theSelected;
	private List theSelected2;
	private List theSelected3;

	private IUploadFile theFile;
	private String token;
	private String[] tokens;
public void setTokens(String[] tokens) {
	this.tokens = tokens;
}
public String[] getTokens() {
	return tokens;
}
public void setTheFile(IUploadFile theFile) {
	this.theFile = theFile;
}
public void setToken(String token) {
	this.token = token;
}
public IUploadFile getTheFile() {
	return theFile;
}
public String getToken() {
	return token;
}


public void processFile(IRequestCycle cycle)
{
	StringTokenizer toks = new StringTokenizer( getTheFile().getFileName(), "\\" );
	String[] sa = new String[ toks.countTokens() ];
	int i=0;
	int count = toks.countTokens();
	while (toks.hasMoreTokens())
	{
		sa[ i ]= i +" " +toks.nextToken();
		if ( i == count-1 ) setMessage( "Filename: " + sa[ i ] );
		i++;
	}
	setTokens( sa );
	setWarning( "mime type: " + getTheFile().getContentType() );
}


	public void setTheSelected(List theSelected) {
		if (null == theSelected) throw new ApplicationRuntimeException( "null theSelected" );
		this.theSelected = theSelected;
	}
	public List getTheSelected() {
		return theSelected;
	}
	public void setTheSelected3(List theSelected3) {
		this.theSelected3 = theSelected3;
	}
	public void setTheSelected2(List theSelected2) {
		this.theSelected2 = theSelected2;
	}
	public List getTheSelected3() {
		return theSelected3;
	}
	public List getTheSelected2() {
		return theSelected2;
	}

	public void processForm( IRequestCycle cycle )
	{
		getEngine().setVisit( null );
		
		Visit visit = (Visit)getVisit();
		String s = "Sponge Bob";
		visit.setTest( s );
		cycle.activate( "Test2" );
	}
	public void testValidate( IRequestCycle cycle )
	{		
		RosterPage page = (RosterPage)cycle.getPage( "Test2" );
		page.validate( cycle );
		cycle.activate( page );
	}
	
	public EntitySelectionModel getTestModel()
	{
		EntitySelectionModel model = new EntitySelectionModel();
		model.add( new Integer( 1 ), "1 Little Indian" );
		model.add( new Integer( 2 ), "2 Little Indians" );
		model.add( new Integer( 3 ), "3 Little Indians" );
		model.add( new Integer( 4 ), "4 Little Indians" );
		return model;
	}
	public void processPalette( IRequestCycle cycle )
	{
		cycle.activate( "Admin" );
	}

	public TestPage()
	{
		super();
	}
}
