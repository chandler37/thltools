package org.thdl.lex.commands;

import org.thdl.lex.*;
import org.thdl.lex.component.*;

import javax.servlet.http.HttpServletRequest;

public class PreferencesCommand extends LexCommand implements Command
{
	public String execute(HttpServletRequest req, ILexComponent component) throws CommandException
	{
		Preferences isb = UserSessionManager.getInstance().getPreferences( req.getSession( true ) );
		if (req.getParameter( LexConstants.COMMAND_REQ_PARAM ).equals("setMetaPrefs") )
		{
			isb.setLanguageSet( LexUtilities.convertToIntArray( req.getParameterValues( "languages" ) ) );
			isb.setDialectSet( LexUtilities.convertToIntArray( req.getParameterValues( "dialects" ) ) );
			isb.setSourceSet( LexUtilities.convertToIntArray( req.getParameterValues( "sources" ) ) );
			isb.setProjectSubjectSet( LexUtilities.convertToIntArray( req.getParameterValues( "projectSubjects" ) ) );
			isb.setScriptSet( LexUtilities.convertToIntArray( req.getParameterValues( "scripts" ) ) );
		}
		else if ( req.getParameter( LexConstants.COMMAND_REQ_PARAM ).equals("setMetaDefaults") )
		{
			isb.setLanguage( Integer.parseInt( req.getParameter( "language" ) ) );
			isb.setDialect( Integer.parseInt( req.getParameter( "dialect" ) ) );
			isb.setSource( Integer.parseInt( req.getParameter( "source" ) ) );
			isb.setProjectSubject( Integer.parseInt( req.getParameter( "projectSubject" ) ) );
			isb.setScript( Integer.parseInt( req.getParameter( "script" ) ) );	
			isb.setNote( req.getParameter( "note" ) );	
			if ( null != req.getParameter( "useDefaultLanguage" ) && req.getParameter( "useDefaultLanguage" ).equals( "true" ) ) 
				isb.setUseDefaultLanguage( true );
			else
				isb.setUseDefaultLanguage( false );					
			if ( null != req.getParameter( "useDefaultDialect" ) && req.getParameter( "useDefaultDialect" ).equals( "true"  )   )
				isb.setUseDefaultDialect( true );
			else
				isb.setUseDefaultDialect( false );					
			if ( null != req.getParameter( "useDefaultSource" ) && req.getParameter( "useDefaultSource" ).equals( "true"  ) )
				isb.setUseDefaultSource( true );
			else
				isb.setUseDefaultSource( false );					
			if ( null != req.getParameter( "useDefaultProjSub" ) && req.getParameter( "useDefaultProjSub" ).equals( "true"  ) )
				isb.setUseDefaultProjSub( true );
			else
				isb.setUseDefaultProjSub( false );					
			if ( null != req.getParameter( "useDefaultScript" ) && req.getParameter( "useDefaultScript" ).equals( "true"  ) )
				isb.setUseDefaultScript( true );
			else
				isb.setUseDefaultScript( false );					
			if ( null != req.getParameter( "useDefaultNote" ) && req.getParameter( "useDefaultNote" ).equals( "true"  ) )
				isb.setUseDefaultNote( true );
			else
				isb.setUseDefaultNote( false );					
		}
		try
		{
			isb.save();
		}
		catch( LexComponentException lre )
		{
			throw new CommandException( "LexComponentException says: " + lre.getMessage() );
		}
		return getNext();
	}
	public java.util.HashMap initForwards() { return null;}
	public PreferencesCommand(String next)
	{
		super(next);
	}
}
