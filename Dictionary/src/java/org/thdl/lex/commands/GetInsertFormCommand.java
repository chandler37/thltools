package org.thdl.lex.commands;

import java.util.*;

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
public class GetInsertFormCommand extends LexCommand implements Command
{
	private boolean termMode;


	/**
	 *  Sets the termMode attribute of the GetInsertFormCommand object
	 *
	 * @param  termMode  The new termMode value
	 */
	public void setTermMode( boolean termMode )
	{
		this.termMode = termMode;
	}


	/**
	 *  Gets the termMode attribute of the GetInsertFormCommand object
	 *
	 * @return    The termMode value
	 */
	public boolean isTermMode()
	{
		return termMode;
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
		String next = getNext();
		Visit visit = UserSessionManager.getInstance().getVisit( req.getSession( true ) );
		LexQuery query = visit.getQuery();
		ITerm term = query.getEntry();
		String msg = null;

		try
		{
			LexUser user = (LexUser) visit.getUser();
			Preferences prefs = visit.getPreferences();

			if ( isTermMode() )
			{
				Term newTerm = new Term();
				newTerm.populate( component );
				query.setQueryComponent( newTerm );
				if ( termExists( query ) )
				{
					msg = newTerm.getTerm() + " is present in the dictionary, please add to this term.";
					next = "displayEntry.jsp";
					visit.setQuery( query );
				}
				component = newTerm;
			}

			Meta meta = new Meta( user, prefs );
			meta.populate( req.getParameterMap() );
			component.setMeta( meta );

			if ( req.getParameter( "cmd" ).equals( "getAnnotationForm" ) )
			{
				AnalyticalNote note = new AnalyticalNote();
				note.setParentId( component.getMetaId() );
				//note.setAnalyticalNote( req.getParameter( "analyticalNote" ) );
				//note.setPrecedence( new Integer( 0 ) );
				component.setAnalyticalNotes( new LinkedList() );
				component.getAnalyticalNotes().add( note );
				meta = new Meta( user, prefs );
				meta.populate( req.getParameterMap() );
				note.setMeta( meta );
				req.setAttribute( LexConstants.ORIGINALBEAN_REQ_ATTR, component );
				component = note;
			}

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

		msg = "You have reached the Insert Form";
		visit.setDisplayMode( "addEditForm" );

		req.setAttribute( LexConstants.MESSAGE_REQ_ATTR, msg );

		return next;
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
		query.setFindMode( LexComponentRepository.STARTS_WITH );
		return termExists;
	}



//constructors

	/**
	 *Constructor for the GetInsertFormCommand object
	 *
	 * @param  next      Description of the Parameter
	 * @param  termMode  Description of the Parameter
	 */
	public GetInsertFormCommand( String next, Boolean termMode )
	{
		super( next );
		setTermMode( termMode.booleanValue() );
	}

}

