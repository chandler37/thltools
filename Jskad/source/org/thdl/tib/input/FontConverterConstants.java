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
    // DLC prune

    // File Constants
    String HOME_PATH = System.getProperties().getProperty("user.dir");
    File HOME_FOLDER = new File(HOME_PATH);

    // GUI Constants
    // Sizes
    Dimension WIN_SIZE = new Dimension(600,400);


    // Colors
    Color BG_COLOR = Color.white;

    // Data Delimiters
    String COMMENT_DELIM = "////";
    String HEADER_DELIM = "<?";
    String DATA_DELIM = "=";
    String FONT_NUM_DELIM = ",";

    // Data File Headers
    String OLD_FONT_HEADER = "old_fonts";
    String NEW_FONT_HEADER = "new_fonts";
    String CORRESP_HEADER = "correspondences";
    String END_OF_SECTION = "EOS";

    // Type Constants
    public final int TIB_TO_TIB = 0;
    public final int DIA_TO_UNICODE = 1;

    // String Constants
    public final String PROGRAM_TITLE = "THDL Font Conversion (with Jskad Technology)";

}
