package org.thdl.quilldriver;

import org.jaxen.XPath;
import org.jaxen.JaxenException;
import org.jaxen.jdom.JDOMXPath;
import java.util.List;

public class TranscriptNavigator {	
	private TranscriptNavigator() {} //can't instantiate this class
	
	public static Object findSingleNode(Object jdomNode, String xpathExpression) {
		List l = findNodeSet(jdomNode, xpathExpression);
		if (l == null || l.size() == 0)
			return null;
		else {
			return l.get(0);
		}
	}
	public static List findNodeSet(Object jdomNode, String xpathExpression) {
		if (jdomNode == null)
			return null;
		try {
			XPath path = new JDOMXPath(xpathExpression);
			return path.selectNodes(jdomNode);
		} catch (JaxenException je) {
			je.printStackTrace();
			return null;
		}
	}
}
