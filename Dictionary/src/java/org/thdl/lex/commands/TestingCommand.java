package org.thdl.lex.commands;

import org.thdl.lex.*;
import org.thdl.lex.component.*;

import javax.servlet.http.HttpServletRequest;

public class TestingCommand extends LexCommand implements Command
{
	public String execute(HttpServletRequest req, ILexComponent component) throws CommandException
	{

		try {
		/* 	Term term = new Term();
			term.setId( 1 );
			term.setTerm( "this is a word" );
			term.setAnalyticalNotes( new java.util.Vector() );
			term.getAnalyticalNotes().add(  "hi" );
			term.getAnalyticalNotes().add( "there" );
			req.setAttribute( "term", term ); */
			return getNext();
		}
		catch (Exception e) { throw new CommandException( e.toString() + e.getMessage() ); } 
	}
	public java.util.HashMap initForwards() { return null;}
	public TestingCommand(String next)
	{
		super(next);
	}
}
