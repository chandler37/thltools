package org.thdl.quilldriver;

import java.util.HashMap;

public class XMLParameters {
	private HashMap displayYesNo, displayContentsYesNo, displays;

	public XMLParameters() {
		displayYesNo = new HashMap();
		displayContentsYesNo = new HashMap();
		displays = new HashMap();
	}
	public boolean containsTag(String tag) {
		if (displayYesNo.get(tag) == null) return false;
		else return true;
	}
	public void addTagOptions(String tag, Boolean display, Boolean displayContents, String displayAs) {
		displayYesNo.put(tag, display);
		displayContentsYesNo.put(tag, displayContents);
		displays.put(tag, displayAs);
	}
	public void removeTagOptions(String tag) {
		displayYesNo.remove(tag);
		displayContentsYesNo.remove(tag);
		displays.remove(tag);
	}
	
	public boolean isTagForDisplay(String tag) {
		Object obj = displayYesNo.get(tag);
		if (obj == null) return false; 
		else return ((Boolean)obj).booleanValue();
	}
	public boolean areTagContentsForDisplay(String tag) {
		Object obj = displayContentsYesNo.get(tag);
		if (obj == null) return false; 
		else return ((Boolean)obj).booleanValue();
	}
	public getDisplayForTag(String tag) {
		Object obj = displayAs.get(tag);
		if (obj == null) return null; 
		else return (String)obj;
	}
}
