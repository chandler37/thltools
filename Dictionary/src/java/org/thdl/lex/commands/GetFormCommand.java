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
public class GetFormCommand extends LexCommand implements Command
{
	private boolean insertMode;
	private boolean termMode;


	/**
	 *  Sets the termMode attribute of the GetFormCommand object
	 *
	 * @param  termMode  The new termMode value
	 */
	public void setTermMode( boolean termMode )
	{
		this.termMode = termMode;
	}


	/**
	 *  Gets the termMode attribute of the GetFormCommand object
	 *
	 * @return    The termMode value
	 */
	public boolean isTermMode()
	{
		return termMode;
	}


	/**
	 *  Sets the insertMode attribute of the GetFormCommand object
	 *
	 * @param  insertMode  The new insertMode value
	 */
	public void setInsertMode( boolean insertMode )
	{
		this.insertMode = insertMode;
	}


	/**
	 *  Gets the insertMode attribute of the GetFormCommand object
	 *
	 * @return    The insertMode value
	 */
	public boolean isInsertMode()
	{
		return insertMode;
	}

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
		String defaultNext = getNext();
		LexQuery query = getSessionManager().getQuery( req.getSession( true ) );
		ITerm term = query.getEntry();
		String msg = null;
		try
		{
			component.populate( req.getParameterMap() );
			if ( isInsertMode() && !isTermMode() )
			{
				term.addChild( component );
			}
			else if ( isInsertMode() && isTermMode() )
			{
				Term newTerm = new Term();
				newTerm.populate( component );
				LexQuery newTermQuery = new LexQuery( newTerm, LexComponentRepository.EXACT );
				if ( termExists( newTermQuery ) )
				{
					msg = newTerm.getTerm() + " is present in the dictionary, please add to this term.";
					setNext( "displayEntry.jsp" );
					getSessionManager().setQuery( req.getSession( true ), query );
				}
				else
				{
					query.setEntry( newTerm );
				}
			}
			else if ( !isTermMode() )
			{
				LexComponentRepository.loadByPk( component );
				//find the persistent copy of this component from the term stored in session.query
				component = term.findChild( component );
			}

			query.setUpdateComponent( component );
			req.setAttribute( LexConstants.COMPONENT_REQ_ATTR, component );
		}
		catch ( LexRepositoryException e )
		{
			throw new CommandException( e );
		}
		catch ( LexComponentException e )
		{
			throw new CommandException( e );
		}
		finally
		{
			setNext( defaultNext );
		}

		//if the component is a translation of another component get the original as well to assist in editing
		if ( component instanceof Translatable )
		{
			Translatable translatable = (Translatable) component;
			if ( null != translatable.getTranslationOf() && translatable.getTranslationOf().intValue() > 0 )
			{
				try
				{
					LexComponent source = (LexComponent) translatable.getClass().newInstance();
					Integer sourcePk = translatable.getTranslationOf();
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
		}

		if ( false == isInsertMode() )
		{
			Integer creator = component.getMeta().getCreatedBy();
			ThdlUser user = getSessionManager().getSessionUser( req.getSession( true ) );
			if ( user.getId().equals( creator ) || user.hasRole( "admin" ) || user.hasRole( "dev" ) )
			{
				msg = "You have reached the Update Form";
				getSessionManager().setDisplayMode( req.getSession( true ), "addEditForm" );
			}
			else
			{
				msg = "A dictionary component can only be edited by the person who created it or an administrator.";
				setNext( "displayEntry.jsp" );
			}
		}
		else
		{
			msg = "You have reached the Insert Form";
			getSessionManager().setDisplayMode( req.getSession( true ), "addEditForm" );
		}

		req.setAttribute( LexConstants.MESSAGE_REQ_ATTR, msg );

		return getNext();
	}


	/**
	 *  Description of the Method
	 *
	 * @param  query                       Description of the Parameter
	 * @return                             The newTerm value
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public boolean termExists( LexQuery query ) throws LexRepositoryException
	{
		boolean termExists = false;
		query.setFindMode( LexComponentRepository.EXACT );
		LexComponentRepository.findTermsByTerm( query );
		if ( query.getResults().keySet().size() > 0 )
		{
			termExists = true;
		}
		return termExists;
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
	 *Constructor for the GetFormCommand object
	 *
	 * @param  next        Description of the Parameter
	 * @param  insertMode  Description of the Parameter
	 * @param  termMode    Description of the Parameter
	 */
	public GetFormCommand( String next, Boolean insertMode, Boolean termMode )
	{
		super( next );
		setInsertMode( insertMode.booleanValue() );
		setTermMode( termMode.booleanValue() );
	}


	/**
	 *Constructor for the GetFormCommand object
	 *
	 * @param  next        Description of the Parameter
	 * @param  insertMode  Description of the Parameter
	 */
	public GetFormCommand( String next, Boolean insertMode )
	{
		this( next, insertMode, Boolean.FALSE );
	}

}

