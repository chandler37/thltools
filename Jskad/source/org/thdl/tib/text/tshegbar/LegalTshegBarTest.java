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
Library (THDL). Portions created by the THDL are Copyright 2002-2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.tshegbar;

import junit.framework.TestCase;

/**
 * @author David Chandler
 *
 * Tests {@link org.thdl.tib.text.tshegbar.LegalTshegBar} at the unit level.
 */
public class LegalTshegBarTest extends TestCase implements UnicodeConstants {
	/**
	 * Plain vanilla constructor for LegalTshegBarTest.
	 * @param arg0
	 */
	public LegalTshegBarTest(String arg0) {
		super(arg0);
	}
    /** Invokes a text UI and runs all this class's tests. */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(LegalTshegBarTest.class);
	}

    /** Tests the getThdlWylie() method and one of the constructors. */
    public void testGetThdlWylie() {
        assertTrue(new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga, EWSUB_ra_btags,
                                     false, true, EWC_la, EWC_sa, EWV_o).getThdlWylie().toString().equals("bsgrAols"));
        assertTrue(new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga,
                                     EWSUB_ra_btags, true, true,
                                     EWC_la, EWC_sa, EWV_o).getThdlWylie().toString().equals("bsgrwAols"));
        assertTrue(new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga,
                                     EWSUB_ra_btags, false, false,
                                     EWC_la, EWC_sa, EWV_o).getThdlWylie().toString().equals("bsgrols"));
    }

    /** Tests the formsLegalTshegBar(..) method. DLC FIXME: but
     * doesn't test it very well. */
    public void testFormsLegalTshegBar() {
        // Ensure that EWTS's jskad is not legal:
        assertTrue(!LegalTshegBar.formsLegalTshegBar(EWC_ja, EWC_sa,
                                                     EWC_ka, EW_ABSENT,
                                                     false, false,
                                                     EW_ABSENT, EWC_da,
                                                     EW_ABSENT));
        assertTrue(LegalTshegBar.formsLegalTshegBar(EWC_ba, EW_ABSENT,
                                                    EWC_ta, EW_ABSENT,
                                                    false, false,
                                                    EWC_da, EW_ABSENT,
                                                    EW_ABSENT));
    }
}
