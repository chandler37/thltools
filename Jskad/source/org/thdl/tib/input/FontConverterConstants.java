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

package org.thdl.tib.input;

import java.io.*;
import java.awt.*;

/** Constants used by ConvertDialog.

    @author Nathaniel Garson, Tibetan and Himalayan Digital Library */
interface FontConverterConstants
{
    final String ACIP_TO_UNI = "ACIP to Unicode";
    final String ACIP_TO_TMW = "ACIP to TMW";
    final String TM_TO_TMW = "TM to TMW";
    final String TMW_TO_UNI = "TMW to Unicode";
    final String TMW_TO_WYLIE = "TMW to Wylie";
    final String TMW_TO_TM = "TMW to TM";
    final String FIND_SOME_NON_TMW = "Find some non-TMW";
    final String FIND_SOME_NON_TM = "Find some non-TM";
    final String FIND_ALL_NON_TMW = "Find all non-TMW";
    final String FIND_ALL_NON_TM = "Find all non-TM";

    final String[] CHOICES = new String[] {
        ACIP_TO_UNI,
        ACIP_TO_TMW,
        TM_TO_TMW,
        TMW_TO_UNI,
        TMW_TO_WYLIE,
        TMW_TO_TM,
        FIND_SOME_NON_TMW,
        FIND_SOME_NON_TM,
        FIND_ALL_NON_TMW,
        FIND_ALL_NON_TM
    };

    final String suggested_WYLIE_prefix = "THDL_Wylie_";
    final String suggested_TO_TMW_prefix = "TMW_";
    final String suggested_TO_UNI_prefix = "Uni_";
    final String suggested_TO_TM_prefix = "TM_";

    // String Constants
    public final String PROGRAM_TITLE = "THDL Font Conversion (with Jskad Technology)";

}
