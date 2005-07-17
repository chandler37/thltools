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
Library (THDL). Portions created by the THDL are Copyright 2005 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.reverter;

/** Static methods for converting Unicode to EWTS and
 *  (TODO(dchandler): ACIP).
 *  @author David Chandler
 */
public class Converter {
    /** Static methods provide all the fun! */
    private Converter() {
        throw new Error("There's no point in instantiating this class.");
    }

    /** Converts Tibetan Unicode to EWTS transliteration.  If errors
     *  is non-null, error messages are appended to it.  (Errors are
     *  always inline.) */
    public static String convertToEwts(String unicode,
                                       StringBuffer errors /* DLC: use it */) {
        throw new Error("DLC not yet");
    }
}
