package org.thdl.quilldriver;

import org.jaxen.jdom.JDOMXPath;
import org.jaxen.XPath;

public class TranscriptNavigator {
	private boolean autoInsertFlag = false;
	private TranscriptMediator mediator;
	
	TranscriptNavigator(TranscriptMediator mediator) {
		this.mediator = mediator;
	}

	/*
		Next = ancestor-or-self::S/following-sibling::
	*/
	
	public boolean edit(String xpathExpression) {
		return false;
	}
	
	public boolean editNextText(String elementName) {
		int pos = mediator.getPane().getCaretPosition();
		
		Object next = next(pos, elementName);
		if (next == null) {
			if (autoInsertFlag) {
				//insert after
				return false;
			} else
				return false;
		}
		return false;
	}
	
	public boolean editPrevText(String elementName) {
		pos = mediator.getPane().getCaretPosition();
		
		Object prev = prev(pos, elementName);
		if (prev == null) {
			if (autoInsertFlag) {
				//insert after
				return false;
			} else
				return false;
		}

		return false;
	}
	
	public boolean highlightNext(String elementName) {
		return false;
	}
	
	public boolean highlightPrev(String elementName) {
		return false;
	}
	
	public Object next(int pos, String elementName) {
		Object jdomNode = mediator.getNodeAtPos(int position);
		if (jdomNode == null)
			return null;
		XPath path = new JDOMXPath("a/b/c");
		List results = path.selectNodes(jdomNode);		
		return null;
	}
	
	public Object prev(int pos, String elementName) {
		Object jdomNode = mediator.getNodeAtPos(int position);
		if (jdomNode == null)
			return null;
		XPath path = new JDOMXPath("a/b/c");
		List results = path.selectNodes(jdomNode);
		return null;
	}
}
