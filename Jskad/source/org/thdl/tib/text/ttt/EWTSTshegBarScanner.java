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
Library (THDL). Portions created by the THDL are Copyright 2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.ttt;

import java.util.ArrayList;

/**
* This singleton class is able to break up Strings of EWTS text (for
* example, an entire sutra file) into tsheg bars, comments, etc.
* Non-Tibetan parts are segregated (so that consumers can ensure that
* they remain non-Tibetan), and Tibetan passages are broken up into
* tsheg bars.
*
* This is not public because you should use {@link EWTSTraits#scanner()}.
*
* @author David Chandler */
class EWTSTshegBarScanner extends TTshegBarScanner {
    /** See the comment in TTshegBarScanner.  This does not find
        errors and warnings that you'd think of a parser finding (DLC
        DOES IT?). */
    public ArrayList scan(String s, StringBuffer errors, int maxErrors,
                          boolean shortMessages, String warningLevel) {
        // the size depends on whether it's mostly Tibetan or mostly
        // Latin and a number of other factors.  This is meant to be
        // an underestimate, but not too much of an underestimate.
        ArrayList al = new ArrayList(s.length() / 10);
        throw new Error("DLC unimplemented");
    }

    /** non-public because this is a singleton */
    protected EWTSTshegBarScanner() { }
    private static EWTSTshegBarScanner singleton = null;
    /** Returns the sole instance of this class. */
    public synchronized static EWTSTshegBarScanner instance() {
        if (null == singleton) {
            singleton = new EWTSTshegBarScanner();
        }
        return singleton;
    }
}
