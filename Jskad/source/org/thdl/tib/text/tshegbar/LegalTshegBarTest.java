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
        assertTrue(new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga, EWC_ra,
                                     false, true, EWC_la, EWC_sa, EWV_o).getThdlWylie().toString().equals("bsgrAols"));
        assertTrue(new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga,
                                     EWC_ra, true, true,
                                     EWC_la, EWC_sa, EWV_o).getThdlWylie().toString().equals("bsgrwAols"));
        assertTrue(new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga,
                                     EWC_ra, false, false,
                                     EWC_la, EWC_sa, EWV_o).getThdlWylie().toString().equals("bsgrols"));
        assertTrue(new LegalTshegBar(EWC_ba, EW_ABSENT, EWC_ta,
                                     EW_ABSENT, false, false,
                                     EWC_nga, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("btang"));

        // dga and dag are fun, as both are represented by "\u0F51\u0F42":
        {
            assertTrue(new LegalTshegBar(EWC_da, EW_ABSENT, EWC_ga,
                                         EW_ABSENT, false, false,
                                         EW_ABSENT, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("dga"));
            assertTrue(new LegalTshegBar(EW_ABSENT, EW_ABSENT, EWC_da,
                                         EW_ABSENT, false, false,
                                         EWC_ga, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("dag"));
        }

        assertTrue(new LegalTshegBar(EW_ABSENT, EWC_ra, EWC_da,
                                     EW_ABSENT, false, false,
                                     EWC_ga, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("rdag"));
        assertTrue(new LegalTshegBar(EWC_ba, EWC_ra, EWC_da,
                                     EW_ABSENT, false, false,
                                     EWC_ga, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("brdag"));

        assertTrue(new LegalTshegBar(EW_ABSENT, EW_ABSENT, EWC_nga,
                                     EW_ABSENT, false, false,
                                     "\u0F60\u0F72", EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("nga'i"));

        assertTrue(new LegalTshegBar(EW_ABSENT, EW_ABSENT, EWC_nga,
                                     EW_ABSENT, false, false,
                                     null, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("nga"));

        assertTrue(new LegalTshegBar(EW_ABSENT, EW_ABSENT, EWC_sa,
                                     EWC_la, false, false,
                                     null, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("sla"));

        {
            boolean threw = false;
            try {
                new LegalTshegBar(EW_ABSENT, EWC_sa, EWC_la,
                                  EW_ABSENT, false, false,
                                  null, EW_ABSENT, EW_ABSENT);
            } catch (IllegalArgumentException e) {
                threw = true;
            }
            assertTrue(threw);
        }
    }

    /** Tests the formsLegalTshegBar(..) method. DLC FIXME: but
     * doesn't test it very well. */
    public void testFormsLegalTshegBar() {
        StringBuffer eb = new StringBuffer();

        // Ensure that EWTS's jskad is not legal:
        {
            assertTrue(!LegalTshegBar.formsLegalTshegBar(EWC_ja, EWC_sa,
                                                         EWC_ka, EW_ABSENT,
                                                         false, false,
                                                         EW_ABSENT, EWC_da,
                                                         EW_ABSENT, eb));
        }

        assertTrue(LegalTshegBar.formsLegalTshegBar(EWC_ba, EW_ABSENT,
                                                    EWC_ta, EW_ABSENT,
                                                    false, false,
                                                    EWC_da, EW_ABSENT,
                                                    EW_ABSENT, eb));
        
        // test that there's only one way to make dwa:
        assertTrue(!LegalTshegBar.formsLegalTshegBar(EW_ABSENT, EW_ABSENT,
                                                     EWC_da, EWSUB_wa_zur,
                                                     false, false,
                                                     EW_ABSENT, EW_ABSENT,
                                                     EW_ABSENT, eb));
        assertTrue(!LegalTshegBar.formsLegalTshegBar(EW_ABSENT, EW_ABSENT,
                                                     EWC_da, EWC_wa,
                                                     false, false,
                                                     EW_ABSENT, EW_ABSENT,
                                                     EW_ABSENT, eb));
        boolean result
            = LegalTshegBar.formsLegalTshegBar(EW_ABSENT, EW_ABSENT,
                                               EWC_da, EW_ABSENT,
                                               true, false,
                                               EW_ABSENT, EW_ABSENT,
                                               EW_ABSENT, eb);
        assertTrue(eb.toString(), result);
    }

    /** Tests the behavior of the constructors. */
    public void testConstructors() {
        boolean x;
        
        x = false;
        try {
            new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga,
                              EWSUB_ra_btags, false, false,
                              EWC_la, EWC_sa, EWV_o);
        } catch (IllegalArgumentException e) {
            x = true;
        }
        assertTrue(x);

        x = false;
        try {
            new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga,
                              EWSUB_ra_btags, false, false,
                              new String(new char[] { EWC_la }), EWC_sa,
                              EWV_o);
        } catch (IllegalArgumentException e) {
            x = true;
        }
        assertTrue(x);
    }
}
