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

/**
	Defines interfase that the Tibetan scanners will
	use to look up the Tibetan words organized in
	a tree structure.

    @author Andr&eacute;s Montano Pellegrini
    @see TibetanScanner
*/

package org.thdl.tib.scanner;

public interface SyllableListTree
{
	public String getDef();
	public Definitions getDefs();
	public boolean hasDef();
	public SyllableListTree lookUp(String silStr);	
	public DictionarySource getDictionarySource();
}