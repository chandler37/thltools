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

package org.thdl.tib.input;

import junit.framework.TestCase;

import javax.swing.Action;
import javax.swing.KeyStroke;

import org.thdl.util.ThdlOptions;

/**
 * @author David Chandler
 *
 * Tests {@link org.thdl.tib.input.Duffpane} at the unit level to see
 * that the various keyboards work as expected.
 */
public class DuffPaneTest extends TestCase {
    /** A DuffPane that uses THDL's extended Wylie keyboard: */
    private DuffPane dp;

    /** Sets us up a DuffPane. */
    protected void setUp() {
        // We don't want to use options.txt:
        ThdlOptions.forTestingOnlyInitializeWithoutDefaultOptionsFile();

        // We don't want to load the TMW font files ourselves:
        ThdlOptions.setUserPreference("thdl.rely.on.system.tmw.fonts", true);
        ThdlOptions.setUserPreference("thdl.debug", true);

        dp = new DuffPane();
        dp.enableCaretManaging();
        dp.registerKeyboard();
    }

    /** Tears us down a DuffPane. */
    protected void tearDown() {
        // let GC do its worst with dp.  We're all set.
        dp = null;
    }

	/**
	 * Plain vanilla constructor for DuffPaneTest.
	 * @param arg0
	 */
	public DuffPaneTest(String arg0) {
		super(arg0);
	}
    /** Invokes a text UI and runs all this class's tests. */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(DuffPaneTest.class);
	}

    /** After ensuring that the caret is at the very end of the
     *  DuffPane's text, and that nothing is selected, this tells the
     *  DuffPane that the user has pressed key. */
    private void fireKeypressWithoutModifiers(char key) {
        dp.performKeyStroke(0, new String(new char[] { key }));
    }

    private void fireKeysWithoutModifiers(String x) {
        for (int i = 0; i < x.length(); i++) {
            fireKeypressWithoutModifiers(x.charAt(i));
        }
    }

    private void ensureKeysGiveCorrectWylie(String wylie) {
        ensureKeysGiveCorrectWylie(wylie, wylie);
    }

    private void ensureKeysGiveCorrectWylie(String keys, String wylie) {
        dp.newDocument(); // initialize to a blank canvas.
        fireKeysWithoutModifiers(keys);
        boolean passes = wylie.equals(dp.getWylie());
        if (!passes) {
            System.out.println("Congrats! These keys, \"" + keys
                               + "\", give this wylie, \"" + dp.getWylie()
                               + "\", not the expected \"" + wylie + "\"");
        }
        assertTrue(passes);
    }

    /** Tests performing a few keystrokes in the Extended Wylie
     *  keyboard, turning those into our internal representation (IR),
     *  and then converting the result to Extended Wylie. */
    public void testWylieToIRToWylie() {
        ensureKeysGiveCorrectWylie("nga ");

        ensureKeysGiveCorrectWylie("nag");

        ensureKeysGiveCorrectWylie("bkra shis bde legs/");
        ensureKeysGiveCorrectWylie("bakra shisa bade legs/",
                                   "bkra shis bde legs/");

        ensureKeysGiveCorrectWylie("sgom pa'am ");

        ensureKeysGiveCorrectWylie("sgom pe'am ");

        ensureKeysGiveCorrectWylie("le'u'i'o");

        ensureKeysGiveCorrectWylie("la'u'i'o");

        ensureKeysGiveCorrectWylie("la'u'i'o/la'am/pa'ang/pa'am'ang/pe'ang");

        ensureKeysGiveCorrectWylie("bskyar.d'am'ang");

        ensureKeysGiveCorrectWylie("lar.d");
        ensureKeysGiveCorrectWylie("lard",
                                   "larda");

        ensureKeysGiveCorrectWylie("lal.d");
        ensureKeysGiveCorrectWylie("lald",
                                   "lalda");

        ensureKeysGiveCorrectWylie("las.d");
        ensureKeysGiveCorrectWylie("lasd",
                                   "lasda");

        ensureKeysGiveCorrectWylie("b.lar.d");
        ensureKeysGiveCorrectWylie("blar.d");
        ensureKeysGiveCorrectWylie("blarad",
                                   "blar.d");
        ensureKeysGiveCorrectWylie("b.lard");

        ensureKeysGiveCorrectWylie("b.lal.d");
        ensureKeysGiveCorrectWylie("blald",
                                   "blalda");
        ensureKeysGiveCorrectWylie("b.lald");

        ensureKeysGiveCorrectWylie("b.las.d");
        ensureKeysGiveCorrectWylie("blasd",
                                   "blasda");
        ensureKeysGiveCorrectWylie("b.lasd");

        ensureKeysGiveCorrectWylie("b.lag");
        ensureKeysGiveCorrectWylie("blg",
                                   "blga");

        ensureKeysGiveCorrectWylie("b.las",
                                   "bals");
        ensureKeysGiveCorrectWylie("bl.s",
                                   "blas");
        ensureKeysGiveCorrectWylie("bls",
                                   "bals");

        ensureKeysGiveCorrectWylie("b.rag");
        ensureKeysGiveCorrectWylie("brg",
                                   "brga");

        ensureKeysGiveCorrectWylie("bars");
        ensureKeysGiveCorrectWylie("b.rs",
                                   "bars");
        ensureKeysGiveCorrectWylie("brs",
                                   "bars");
        ensureKeysGiveCorrectWylie("br.s",
                                   "bras");
        ensureKeysGiveCorrectWylie("bras");

        ensureKeysGiveCorrectWylie("d.wa");
        ensureKeysGiveCorrectWylie("dawa",
                                   "d.wa");
        ensureKeysGiveCorrectWylie("dwa");

        ensureKeysGiveCorrectWylie("g.wa");
        ensureKeysGiveCorrectWylie("gawa",
                                   "g.wa");
        ensureKeysGiveCorrectWylie("gwa");

        ensureKeysGiveCorrectWylie("'.wa",
                                   "'wa");
        ensureKeysGiveCorrectWylie("'awa",
                                   "'wa");
        ensureKeysGiveCorrectWylie("'wa");

        ensureKeysGiveCorrectWylie("gyg",
                                   "g.yag");
        ensureKeysGiveCorrectWylie("g.yag");
        ensureKeysGiveCorrectWylie("gyag");
        ensureKeysGiveCorrectWylie("gy.g",
                                   "gyag");

        ensureKeysGiveCorrectWylie("gys",
                                   "g.yas");
        ensureKeysGiveCorrectWylie("g.yas");
        ensureKeysGiveCorrectWylie("gyas");
        ensureKeysGiveCorrectWylie("gy.s",
                                   "gyas");

        // FIXME: shouldn't this give the four-glyph combo m-a-a-s?
        ensureKeysGiveCorrectWylie("ma.a.asa",
                                   "mas");

        ensureKeysGiveCorrectWylie("'ka");

        ensureKeysGiveCorrectWylie("'gas");

        ensureKeysGiveCorrectWylie("gangs");

        ensureKeysGiveCorrectWylie("gnags");

        ensureKeysGiveCorrectWylie("'angs");

        ensureKeysGiveCorrectWylie("'ag");

        ensureKeysGiveCorrectWylie("'byung");

        ensureKeysGiveCorrectWylie("'byungs");

        ensureKeysGiveCorrectWylie("b.lags");
        ensureKeysGiveCorrectWylie("blags");

        // DLC add b-r-g-s, b-l-g-s,


        ensureKeysGiveCorrectWylie("mngas",
                                   "mangs");
        ensureKeysGiveCorrectWylie("mangs");

        ensureKeysGiveCorrectWylie("mn.gs",
                                   "mnags");
        ensureKeysGiveCorrectWylie("mnags");

        ensureKeysGiveCorrectWylie("lmn.g",
                                   "lamanaga");

        ensureKeysGiveCorrectWylie("l.m.ng",
                                   "lamanga");

        ensureKeysGiveCorrectWylie("b.m.ng",
                                   "bmang");
        ensureKeysGiveCorrectWylie("bmang");

        ensureKeysGiveCorrectWylie("gdams");
        ensureKeysGiveCorrectWylie("g.d.m.s.",
                                   "gdams");

        ensureKeysGiveCorrectWylie("'gams");
        ensureKeysGiveCorrectWylie("'.g.m.s",
                                   "'gams");

        {
            // These are correctly handled in terms of
            // makeIllegalTibetanGoEndToEnd:
            ensureKeysGiveCorrectWylie("skalazasa");
            ensureKeysGiveCorrectWylie("jskad",
                                       "jaskada");
        }
        {
            // These are incorrectly handled in terms of
            // makeIllegalTibetanGoEndToEnd.  DLC FIXME.
            ensureKeysGiveCorrectWylie("jeskad",
                                       "jeskd");
            ensureKeysGiveCorrectWylie("jeskd");
            ensureKeysGiveCorrectWylie("jesakada",
                                       "jeskd");
        }
    }
}
