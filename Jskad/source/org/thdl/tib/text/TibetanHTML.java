package org.thdl.tib.text;

import java.util.StringTokenizer;

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

	public static String getHTMLX(String wylie) {
		try {
			StringBuffer buffer = new StringBuffer();
			for (StringTokenizer tokenizer = new StringTokenizer(wylie, " \t\n", true); tokenizer.hasMoreElements();) {
				String next = tokenizer.nextToken();
				if (next.equals("\t") || next.equals("\n")) {
					buffer.append("<wbr/>");
					buffer.append(getHTML(TibTextUtils.getTibetanMachineWebForEWTS("_")));
					buffer.append("<wbr/>");
				}
				else
					buffer.append(getHTML(TibTextUtils.getTibetanMachineWebForEWTS(next)));
			}
			return buffer.toString();
		} catch (InvalidWylieException ive) {
			return "";
		}
	}

	public static String getHTMLX(DuffData[] duffData) {
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

				if (c[k] > 32 && c[k] < 127) { //ie if it's not formatting
					switch (c[k]) {
						case '"':
							htmlBuffer.append("&quot;");
							break;
						case '<':
							htmlBuffer.append("&lt;");
							break;
						case '>':
							htmlBuffer.append("&gt;");
							break;
						case '&':
							htmlBuffer.append("&amp;");
							break;
						default:
							htmlBuffer.append(c[k]);
							break;
					}
					htmlBuffer.append("</span>");
					String wylie = TibetanMachineWeb.getWylieForGlyph(duffData[i].font, c[k], TibTextUtils.weDoNotCareIfThereIsCorrespondingWylieOrNot);
					if (TibetanMachineWeb.isWyliePunc(wylie))
						htmlBuffer.append("<wbr/>");
				} else {
					htmlBuffer.append("</span><br/>");
				}
			}
		}

		htmlBuffer.append("</nobr>");
		return htmlBuffer.toString();
	}

	public static String getIndentedHTML(String wylie) {
		return getHTML("_" + wylie);
	}

	public static String getHTML(String wylie) {
		try {
			StringBuffer buffer = new StringBuffer();
			for (StringTokenizer tokenizer = new StringTokenizer(wylie, " \t\n", true); tokenizer.hasMoreElements();) {
				String next = tokenizer.nextToken();
				if (next.equals("\t") || next.equals("\n")) {
					buffer.append("<wbr/>");
					buffer.append(getHTML(TibTextUtils.getTibetanMachineWebForEWTS("_")));
					buffer.append("<wbr/>");
				}
				else
					buffer.append(getHTML(TibTextUtils.getTibetanMachineWebForEWTS(next)));
			}
			return buffer.toString();
		} catch (InvalidWylieException ive) {
			return "";
		}
	}

	public static String getHTML(DuffData[] duffData) {
		String[] styleNames =
			{"tmw","tmw1","tmw2","tmw3","tmw4","tmw5","tmw6","tmw7","tmw8","tmw9"};

		StringBuffer htmlBuffer = new StringBuffer();
		htmlBuffer.append("<nobr>");

		for (int i=0; i<duffData.length; i++) {
			htmlBuffer.append("<span class=\"");
			htmlBuffer.append(styleNames[duffData[i].font-1]);
			htmlBuffer.append("\">");
			char[] c = duffData[i].text.toCharArray();
			for (int k=0; k<c.length; k++) {
				if (c[k] > 31 && c[k] < 127) { //ie if it's not formatting
					switch (c[k]) {
						case '"':
							htmlBuffer.append("&quot;");
							break;
						case '<':
							htmlBuffer.append("&lt;");
							break;
						case '>':
							htmlBuffer.append("&gt;");
							break;
						case '&':
							htmlBuffer.append("&amp;");
							break;
						default:
							htmlBuffer.append(c[k]);
							break;
					}
					String wylie = TibetanMachineWeb.getWylieForGlyph(duffData[i].font, c[k], TibTextUtils.weDoNotCareIfThereIsCorrespondingWylieOrNot);
					if (TibetanMachineWeb.isWyliePunc(wylie))
						htmlBuffer.append("<wbr/>");
				} else {
					htmlBuffer.append("<br/>");
				}
			}
			htmlBuffer.append("</span>");
		}

		htmlBuffer.append("</nobr>");
		return htmlBuffer.toString();
	}

	public static String getHTMLforJava(String wylie) {
		//differences:
		//	as of 1.4.1, anyway, browser built into java does not accept <wbr/> and <br/>,
		//	only <wbr> and <br>

		try {
			StringBuffer buffer = new StringBuffer();
			for (StringTokenizer tokenizer = new StringTokenizer(wylie, " \t\n", true); tokenizer.hasMoreElements();) {
				String next = tokenizer.nextToken();
				if (next.equals("\t") || next.equals("\n")) {
					buffer.append("<wbr>");
					buffer.append(getHTML(TibTextUtils.getTibetanMachineWebForEWTS("_")));
					buffer.append("<wbr>");
				}
				else
					buffer.append(getHTML(TibTextUtils.getTibetanMachineWebForEWTS(next)));
			}
			return buffer.toString();
		} catch (InvalidWylieException ive) {
			return "";
		}
	}

	public static String getHTMLforJava(DuffData[] duffData) {
		String[] fontNames = {
			"TibetanMachineWeb","TibetanMachineWeb1", "TibetanMachineWeb2",
			"TibetanMachineWeb3","TibetanMachineWeb4","TibetanMachineWeb5",
			"TibetanMachineWeb6","TibetanMachineWeb7","TibetanMachineWeb8",
			"TibetanMachineWeb9"};

		StringBuffer htmlBuffer = new StringBuffer();
		htmlBuffer.append("<nobr>");

		for (int i=0; i<duffData.length; i++) {
			htmlBuffer.append("<font size=\"36\" face=\"");
			htmlBuffer.append(fontNames[duffData[i].font-1]);
			htmlBuffer.append("\">");
			char[] c = duffData[i].text.toCharArray();
			for (int k=0; k<c.length; k++) {
				if (c[k] > 31 && c[k] < 127) { //ie if it's not formatting
					switch (c[k]) {
						case '"':
							htmlBuffer.append("&quot;");
							break;
						case '<':
							htmlBuffer.append("&lt;");
							break;
						case '>':
							htmlBuffer.append("&gt;");
							break;
						case '&':
							htmlBuffer.append("&amp;");
							break;
						default:
							htmlBuffer.append(c[k]);
							break;
					}
					String wylie = TibetanMachineWeb.getWylieForGlyph(duffData[i].font, c[k], TibTextUtils.weDoNotCareIfThereIsCorrespondingWylieOrNot);
					if (TibetanMachineWeb.isWyliePunc(wylie))
						htmlBuffer.append("<wbr>");
				} else {
					htmlBuffer.append("<br>");
				}
			}
			htmlBuffer.append("</font>");
		}

		htmlBuffer.append("</nobr>");
		return htmlBuffer.toString();
	}
}
