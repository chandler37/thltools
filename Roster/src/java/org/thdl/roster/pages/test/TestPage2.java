package org.thdl.roster.pages.test;

import org.thdl.roster.*;
import org.thdl.roster.om.*;
import org.thdl.roster.pages.*;
import org.thdl.roster.pages.test.*;
import org.apache.tapestry.*;
import org.apache.tapestry.html.*;
import java.util.List;

public class TestPage2 extends org.thdl.roster.pages.RosterPage //org.apache.tapestry.html.BasePage
{
	private List theSelected;
	private List theSelected2;
	private List theSelected3;

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

   public void validate(IRequestCycle cycle)
    {
			  RosterPage home = (RosterPage) cycle.getPage( "Home" );
			  home.setWarning( "There was no Roster Member in your session. This is possibly due to navigating to an edit screen after your session had timed out." );
			  throw new PageRedirectException( home );
    }
	public void processForm( IRequestCycle cycle )
	{
		getEngine().setVisit( null );
		
		Visit visit = (Visit)getVisit();
		String s = "Sponge Bob";
		visit.setTest( s );
		cycle.activate( "Test2" );
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

	public TestPage2()
	{
		super();
	}
}
