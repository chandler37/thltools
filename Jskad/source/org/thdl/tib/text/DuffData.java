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

/**
* A wrapper object for a stretch of TibetanMachineWeb data that shares the same font.
* A piece of DuffData consists of a font number and a string.
* The font number is a number from one to ten, corresponding
* to the ten TibetanMachineWeb fonts, as follows:
* <p>
*    1 - TibetanMachineWeb<br>
*    2 - TibetanMachineWeb1<br>
*    ...<br>
*    10 - TibetanMachineWeb9<br>
* <p>
* The string represents a contiguous stretch of data in that
* font, i.e. a stretch of TibetanMachineWeb that doesn't require a font change.
*/
public class DuffData {
/**
* a string of text
*/
    public String text;
/**
* the font number for this text (see class description)
*/
    public int font;

/**
* @param s a string of TibetanMachineWeb text
* @param i a TibetanMachineWeb font number
*/
    public DuffData(String s, int i) {
        text = s;
        font = i;
    }
}
