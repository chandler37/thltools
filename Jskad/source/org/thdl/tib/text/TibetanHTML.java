package org.thdl.tib.text;

public class TibetanHTML {
	static String[] styleNames =
		{"tmw","tmw1","tmw2","tmw3","tmw4","tmw5","tmw6","tmw7","tmw8","tmw9"};

	public static String getStyles(String fontSize) {
		return	".tmw {font: "+fontSize+"pt TibetanMachineWeb}\n"+
				".tmw1 {font: "+fontSize+"pt TibetanMachineWeb1}\n"+
				".tmw2 {font: "+fontSize+"pt TibetanMachineWeb2}\n"+
				".tmw3 {font: "+fontSize+"pt TibetanMachineWeb3}\n"+
				".tmw4 {font: "+fontSize+"pt TibetanMachineWeb4}\n"+
				".tmw5 {font: "+fontSize+"pt TibetanMachineWeb5}\n"+
				".tmw6 {font: "+fontSize+"pt TibetanMachineWeb6}\n"+
				".tmw7 {font: "+fontSize+"pt TibetanMachineWeb7}\n"+
				".tmw8 {font: "+fontSize+"pt TibetanMachineWeb8}\n"+
				".tmw9 {font: "+fontSize+"pt TibetanMachineWeb9}\n";
	}


	public static String getHTML(String wylie) {
		try {
			return getHTML(TibetanDocument.getTibetanMachineWeb(wylie));
		}
		catch (InvalidWylieException ive) {
			return null;
		}
	}

	public static String getHTML(TibetanDocument.DuffData[] duffData) {
		String[] styleNames =
			{"tmw","tmw1","tmw2","tmw3","tmw4","tmw5","tmw6","tmw7","tmw8","tmw9"};

		StringBuffer htmlBuffer = new StringBuffer();
		htmlBuffer.append("<nobr>");

		for (int i=0; i<duffData.length; i++) {
			char[] c = duffData[i].text.toCharArray();
			for (int k=0; k<c.length; k++) {
				htmlBuffer.append("<span class=\"");
				htmlBuffer.append(styleNames[duffData[i].font-1]);
				htmlBuffer.append("\">");

				switch (c[k]) {
					case '"':
						htmlBuffer.append("&quot");
						break;
					case '<':
						htmlBuffer.append("&lt");
						break;
					case '>':
						htmlBuffer.append("&gt");
						break;
					case '&':
						htmlBuffer.append("&amp");
						break;
					default:
						htmlBuffer.append(c[k]);
						break;
				}

				htmlBuffer.append("</span>");
				if (c[k] < 32) //must be formatting, like carriage return or tab
					htmlBuffer.append("<br>");

				else {
					String wylie = TibetanMachineWeb.getWylieForGlyph(duffData[i].font, c[k]);
					if (TibetanMachineWeb.isWyliePunc(wylie))
						htmlBuffer.append("<wbr>");
				}
			}
		}

		htmlBuffer.append("</nobr>");
		return htmlBuffer.toString();
	}
}



//import org.apache.xerces.dom.DOMImplementationImpl;
//import org.w3c.dom.*;

/*
	public static Node getHTML(String wylie) {
		try {
			return getHTML(TibetanDocument.getTibetanMachineWeb(wylie));
		}
		catch (InvalidWylieException ive) {
			return null;
		}
	}

	public static Node getHTML(TibetanDocument.DuffData[] duffData) {
		try {
			DOMImplementationImpl impl = new DOMImplementationImpl();
			Document doc = impl.createDocument(null, "root", null);
//			DocumentFragment frag = doc.createDocumentFragment()


			Element nobr = doc.createElement("nobr");
//			frag.appendChild(nobr);

			for (int i=0; i<duffData.length; i++) {
				char[] c = duffData[i].text.toCharArray();
				for (int k=0; k<c.length; k++) {
					Element span = doc.createElement("span");
					span.setAttribute("class", styleNames[duffData[i].font-1]);
//					Text tib = doc.createTextNode(String.valueOf(c[k]));
//					span.appendChild(tib);
					nobr.appendChild(span);

					String wylie = TibetanMachineWeb.getWylieForGlyph(duffData[i].font, c[k]);
					if (TibetanMachineWeb.isWyliePunc(wylie)) {
						Element wbr = doc.createElement("wbr");
						nobr.appendChild(wbr);
					}
				}
			}

//doc.appendChild(nobr);
return nobr;

//			return frag;
		}
		catch (DOMException dome) {
switch (dome.code) {

case DOMException.HIERARCHY_REQUEST_ERR:
System.out.println("hierarchy error!!");
break;

case DOMException.WRONG_DOCUMENT_ERR:
System.out.println("wrong doc error!!");
break;

case DOMException.NO_MODIFICATION_ALLOWED_ERR:
System.out.println("no mod allowed error!!");
break;
}
			return null;
		}
	}
*/
