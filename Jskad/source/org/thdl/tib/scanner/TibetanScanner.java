/*
The contents of this file are subject to the AMP Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the AMP web site 
(http://www.tibet.iteso.mx/Guatemala/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is Andres Montano Pellegrini. Portions
created by Andres Montano Pellegrini are Copyright 2001 Andres Montano
Pellegrini. All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.scanner;
import java.util.*;

public interface TibetanScanner
{
	public static final String copyrightUnicode="Copyright " + '\u00A9' + " 2000-2002 by Andr" + '\u00E9' + "s Montano Pellegrini, all rights reserved.";
	public static final String copyrightASCII="Copyright 2000-2002 by Andres Montano Pellegrini, all rights reserved.";
	public static final String copyrightHTML="<hr><h5>Copyright &copy; 2000-2002 by <a href=\"http://www.people.virginia.edu/~am2zb/\" target=\"_blank\">Andr&eacute;s Montano Pellegrini</a><br>All rights reserved</h5>";
	public static final String aboutUnicode=
	"The Tibetan to English Translation Tool, version 1.1.0\n" +
	"Copyright " + '\u00A9' + " 2000-2002 by Andr" + '\u00E9' + "s Montano Pellegrini, all rights reserved.\n" + 
	"This software is protected by the terms of the AMP Open Community License,\n" +
	"Version 1.0 (available at www.tibet.iteso.mx/Guatemala/). The Tibetan script\n" +
	"input facility was built by THDL's Edward Garrett (http://www.thdl.org/).\n" +
	"It uses Tibetan Computer Company (http://www.tibet.dk/tcc/)fonts created by\n" +
	"Tony Duff and made available by the Trace Foundation (http://trace.org/).";
	public void scanLine(String linea);
	public void scanBody(String linea);
	public void finishUp();
	public LinkedList getTokenLinkedList();
	public Token[] getTokenArray();
	public void clearTokens();
	public DictionarySource getDictionarySource();
	public String[] getDictionaryDescriptions();
}