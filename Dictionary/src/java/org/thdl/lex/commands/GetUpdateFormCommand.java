package org.thdl.lex.commands;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.thdl.lex.*;
import org.thdl.lex.component.*;
import org.thdl.users.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 6, 2003
 */
public class GetUpdateFormCommand extends LexCommand implements Command
{
//helper methods
	/**
	 *  Description of the Method
	 *
	 * @param  req                   Description of the Parameter
	 * @param  component             Description of the Parameter
	 * @return                       Description of the Return Value
	 * @exception  CommandException  Description of the Exception
	 */
	public String execute( HttpServletRequest req, ILexComponent component ) throws CommandException
	{
		LexQuery query = getSessionMgr().getQuery( req.getSession( true ) );
		Term term = null;
		try
		{
			term = (Term) query.getEntry();
		}
		catch ( ClassCastException cce )
		{
			throw new CommandException( term.toString(), cce );
		}
		String msg = null;
		try
		{
			component.populate( req.getParameterMap() );
			ILexComponent child = term.findChild( component );
			query.setUpdateComponent( child );
			req.setAttribute( LexConstants.COMPONENT_REQ_ATTR, child );
		}
		catch ( LexComponentException e )
		{
			throw new CommandException( e );
		}

		//if the component is a translation of another component get the original as well to assist in editing
		if ( component.getTranslationOf().intValue() > 0 )
		{
			try
			{
				LexComponent source = (LexComponent) component.getClass().newInstance();
				Integer sourcePk = component.getTranslationOf();
				source.setMetaId( sourcePk );
				LexComponentRepository.loadByPk( source );
				req.setAttribute( LexConstants.ORIGINALBEAN_REQ_ATTR, source );
			}
			catch ( InstantiationException ie )
			{
				throw new CommandException( ie );
			}
			catch ( IllegalAccessException iae )
			{
				throw new CommandException( iae );
			}
			catch ( LexRepositoryException lre )
			{
				throw new CommandException( lre );
			}
		}

		Integer creator = component.getMeta().getCreatedBy();
		ThdlUser user = getSessionMgr().getSessionUser( req.getSession( true ) );
		if ( user.getId().equals( creator ) || user.hasRole( "admin" ) || user.hasRole( "dev" ) )
		{
			msg = "You have reached the Update Form";
			getSessionMgr().setDisplayMode( req.getSession( true ), "addEditForm" );
			setNext( "displayForm.jsp?formMode=update" );
		}
		else
		{
			msg = "A dictionary component can only be edited by the person who created it or an administrator.";
			setNext( "displayEntry.jsp" );
		}
		req.setAttribute( LexConstants.MESSAGE_REQ_ATTR, msg );
		return getNext();
	}


	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public HashMap initForwards()
	{
		HashMap map = new HashMap();
//map.put( LexConstants.TERMLABEL_VALUE, "displayEntry.jsp?formMode=update" );
//map.put( LexConstants.SUBDEFINITIONLABEL_VALUE, "displayEntry.jsp?formMode=update" );
//map.put( LexConstants.TRANSLATIONLABEL_VALUE, "displayEntry.jsp?formMode=update" );
// map.put( LexConstants.DEFINITIONLABEL_VALUE, "displayEntry.jsp" );
// map.put( LexConstants.PASSAGELABEL_VALUE, "displayEntry.jsp" );
		return map;
	}

//constructors
	/**
	 *Constructor for the GetUpdateFormCommand object
	 */
	public GetUpdateFormCommand()
	{
		super();
//setForwards( initForwards() );
	}


	/**
	 *Constructor for the GetUpdateFormCommand object
	 *
	 * @param  next  Description of the Parameter
	 */
	public GetUpdateFormCommand( String next )
	{
		super();
		setNext( next );
	}
}

