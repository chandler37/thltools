package org.thdl.quilldriver;

import org.jdom.Document;
import org.thdl.tib.input.DuffPane;

public class TranscriptMediator {
	private Document xml;
	private DuffPane pane;
	Properties nodeAbbreviations;
	Properties globalProperties;
	
	public TranscriptMediator(Document xmlDoc, DuffPane duffPane) {
		xml = xmlDoc;
		pane = duffPane;
		
		
		
	}
	
	public DuffPane getPane() {
		return pane;
	}
	
	public Document getXML() {
		return xml;
	}
	
	public Object getNodeAtPosition(int position) {
		return null;	
	}

	
}
