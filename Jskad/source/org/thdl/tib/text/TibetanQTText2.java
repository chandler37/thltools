/*
The contents of this file are subject to the THDL Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the THDL web site 
(http://www.thdl.org/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is the Tibetan and Himalayan Digital
Library (THDL). Portions created by the THDL are Copyright 2001 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text;

import java.util.*;
import org.w3c.dom.*;

public class TibetanQTText2 {
	private static String tibFontSize = "28";
	private Map lines;
	private Map times;
	int id;

	public TibetanQTText2() {
		lines = new HashMap();
		times = new TreeMap();
		id = 0;
	}

	public void addLine(String wylie, String t1, String t2) {
		id++;
		String lineID = String.valueOf(id);
		lines.put(lineID, wylie);

		try {
			Float startTime = new Float(t1);
			if (!times.containsKey(startTime))
				times.put(startTime, lineID+",");
			else {
				String val = (String)times.get(startTime);
				val += lineID+",";
				times.put(startTime, val);
			}

			Float stopTime = new Float(t2);
			if (!times.containsKey(stopTime))
				times.put(stopTime, lineID+",");
			else {
				String val = (String)times.get(stopTime);
				val += lineID+",";
				times.put(stopTime, val);
			}
		}
		catch (NumberFormatException nfe) {
		}

	}

	public void organizeLines() {
		List line_list = new ArrayList();

		Iterator iter = times.keySet().iterator();
		while (iter.hasNext()) {
			Float this_time = (Float)iter.next();
			String these_lines = (String)times.get(this_time);

			StringTokenizer sTok = new StringTokenizer(these_lines,",");
			while (sTok.hasMoreTokens()) {
				String lineID = sTok.nextToken();
				if (line_list.contains(lineID))
					line_list.remove(lineID);
				else
					line_list.add(lineID);
			}

			StringBuffer sb = new StringBuffer();
			Iterator line_list_iter = line_list.iterator();
			while (line_list_iter.hasNext()) {
				String lineID = (String)line_list_iter.next();
				sb.append(lineID);
				sb.append(',');
			}

			times.put(this_time, sb.toString());
		}
	} 

	public String getQTTextForLines() {
		StringBuffer sb = new StringBuffer();

		Iterator iter = times.keySet().iterator();
		while (iter.hasNext()) {
			Float this_time = (Float)iter.next();
			sb.append(getQTTimeTag(String.valueOf(this_time)));
			String these_lines = (String)times.get(this_time);

			StringTokenizer sTok = new StringTokenizer(these_lines,",");
			while (sTok.hasMoreTokens()) {
				String lineID = sTok.nextToken();
				String wylie = (String)lines.get(lineID);
				sb.append(getQTText(wylie));
				sb.append('\n');
			}
		}

		return sb.toString();
	}

	public static String getQTHeader() {
		return "{QTtext}{plain}{anti-alias:off}{size:28}{justify:left}{timeScale:1000}{width:320}{height:120}{timeStamps:absolute}{language:0}{textEncoding:0}\n";
	}

	public static String getQTTimeTag(String t) {
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		sb.append(t);
		sb.append(']');
		return sb.toString();
	}

	public static String getQTText(String wylie) {
		try {
			return getQTText(TibetanDocument.getTibetanMachineWeb(wylie));
		}
		catch (InvalidWylieException ive) {
			return null;
		}
	}

	public static String getQTText(TibetanDocument.DuffData[] duffData) {
		StringBuffer qtBuffer = new StringBuffer();
		qtBuffer.append("{size:" + tibFontSize + "}");

		for (int i=0; i<duffData.length; i++) {
			qtBuffer.append("{font:" + TibetanMachineWeb.tmwFontNames[duffData[i].font] + "}");
			qtBuffer.append(duffData[i].text);
		}

		return qtBuffer.toString();
	}
}

/*
	public void addLine(NodeList nodes) {


		Element line = (Element)nodes.item(0);
Node wylie_node = line.getFirstChild();		
//		NodeList wylie_nodes = line.getElementsByTagName("wylie");

//		Node wylie_node = wylie_nodes.item(0);
		Node wylie_text = wylie_node.getFirstChild();
System.out.println(wylie_text.getNodeName());
//		String wylie = wylie_text.getNodeValue();
		id++;
		String lineID = String.valueOf(id);
		lines.put(lineID, wylie);

		NodeList audio_nodes = line.getElementsByTagName("AUDIO");
		Element audio_element = (Element)audio_nodes.item(0);
		try {
			Integer startTime = Integer.valueOf(audio_element.getAttribute("begin"));
			if (!times.containsKey(startTime))
				times.put(startTime, lineID+",");
			else {
				String val = (String)times.get(startTime);
				val += lineID+",";
				times.put(startTime, val);
			}

			Integer stopTime = Integer.valueOf(audio_element.getAttribute("end"));
			if (!times.containsKey(stopTime))
				times.put(stopTime, lineID+",");
			else {
				String val = (String)times.get(stopTime);
				val += lineID+",";
				times.put(stopTime, val);
			}
		}
		catch (NumberFormatException nfe) {
		}
	}
*/
