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
		HttpSession ses = req.getSession( true );
		LexQuery query = getSessionManager().getQuery( ses );
		ITerm term = query.getEntry();
		String msg = null;
		ThdlUser user = getSessionManager().getSessionUser( req.getSession( true ) );
		if ( validate( user, component ) )
		{

			try
			{

				LexLogger.debug( "Checking component state from getUpdateFormCommand BEFORE component assignment" );
				LexLogger.debugComponent( component );
				if ( isTermMode() )
				{
					component = query.getEntry();
				}
				else
				{
					LexComponentRepository.update( term );
					component = term.findChild( component.getMetaId() );
				}
				req.setAttribute( LexConstants.COMPONENT_REQ_ATTR, component );
				LexLogger.debug( "Checking component state from getUpdateFormCommand AFTER component assignment" );
				LexLogger.debugComponent( component );
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

			msg = "You have reached the Update Form";
			getSessionManager().setDisplayMode( req.getSession( true ), "addEditForm" );
		}
		else
		{
			msg = "A dictionary component can only be edited by the person who created it or an administrator.";
			next = "displayEntry.jsp";
		}

		req.setAttribute( LexConstants.MESSAGE_REQ_ATTR, msg );

		return next;
	}



//constructors

	/**
	 *Constructor for the GetFormCommand object
	 *
	 * @param  next      Description of the Parameter
	 * @param  termMode  Description of the Parameter
	 */
	public GetUpdateFormCommand( String next, Boolean termMode )
	{
		super( next );
		setTermMode( termMode.booleanValue() );
	}

}
