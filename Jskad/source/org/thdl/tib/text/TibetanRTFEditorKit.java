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

import javax.swing.*; 
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;

/** An EditorKit that is cognizant of the line-wrapping rules for
 *  Tibetan text.  That is, this class knows about the tsheg and other
 *  Tibetan punctuation.
 *  @author David Chandler */
public class TibetanRTFEditorKit extends RTFEditorKit {
    /** Creates a new TibetanRTFEditorKit. */
    public TibetanRTFEditorKit() {
        super();
    }

    /** the Tibetan-aware ViewFactory */
    private TibetanRTFViewFactory f = null;

    /** Returns a ViewFactory that is cognizant of Tibetan punctuation
     *  and line-breaking rules. */
    public ViewFactory getViewFactory() {
        if (null == f) {
            f = new TibetanRTFViewFactory(super.getViewFactory());
        }
        return f;
    }
}
