package org.thdl.roster.components;

import java.util.*;

import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.*;
import org.apache.torque.util.Criteria;
import org.apache.torque.*;

import org.thdl.roster.om.*;
import org.thdl.roster.*;

public class DocumentDisplay extends BaseComponent
{
//attributes
	private Member member;
	private Document document;
//accessors
	public void setMember(Member member) {
		this.member = member;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public Member getMember() {
		return member;
	}
	public Document getDocument() {
		return document;
	}
//helpers
//constructors
	public DocumentDisplay()
	{
		super();
	/* 	try
		{
			if ( ! Torque.isInit() )
			{
				Global global = (Global) getPage().getGlobal();
				Torque.init( global.getTorqueConfig() );
			}
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( te );
		} */

	}
}