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
Library (THDL). Portions created by the THDL are Copyright 2004 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.ttt;

import org.thdl.util.ThdlOptions;

import java.util.ArrayList;

import junit.framework.TestCase;


/** Tests this package's ability to understand EWTS and turn it into
 *  the appropriate TMW or Unicode.
 *
 *  @author David Chandler */
public class EWTSTest extends TestCase {

    /** Invokes a text UI and runs all this class's tests. */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(EWTSTest.class);
    }

    protected void setUp() {
        // We don't want to use options.txt:
        ThdlOptions.forTestingOnlyInitializeWithoutDefaultOptionsFile();

        ThdlOptions.setUserPreference("thdl.acip.to.tibetan.warning.and.error.severities.are.built.in.defaults", "true");
        ThdlOptions.setUserPreference("thdl.acip.to.tibetan.warning.severity.507", "Most");
        ErrorsAndWarnings.setupSeverityMap();

        // We don't want to load the TM or TMW font files ourselves:
        ThdlOptions.setUserPreference("thdl.rely.on.system.tmw.fonts", true);
        ThdlOptions.setUserPreference("thdl.rely.on.system.tm.fonts", true);
        ThdlOptions.setUserPreference("thdl.debug", true);
    }


    public EWTSTest() { }

    /** Causes a JUnit test case failure unless the EWTS document ewts
     *  converts to the unicode expectedUnicode. */
    static void ewts2uni_test(String ewts, String expectedUnicode) {
        // TODO(DLC)[EWTS->Tibetan]: NOW! Implement me.
    }

    /** Causes a JUnit test case failure iff the EWTS document ewts is
     *  legal EWTS transliteration. */
    static void assert_EWTS_error(String ewts) {
        // TODO(DLC)[EWTS->Tibetan]: NOW! Implement me.
    }

    /** Miscellaneous tests of EWTS->Unicode conversion. */
    public void test__EWTS__miscellany() {

        ewts2uni_test("", "");

        ewts2uni_test("0\\u0f19", "\u0f20\u0f19");
        ewts2uni_test("0\\u0f18", "\u0f20\u0f18");
        ewts2uni_test("0\\u0f3e", "\u0f20\u0f3e"); // TODO(DLC)[EWTS->Tibetan]: test ewts->tmw
        ewts2uni_test("0\\u0f3f", "\u0f20\u0f3f"); // TODO(DLC)[EWTS->Tibetan]: test ewts->tmw

        ewts2uni_test("R", "\u0f6A");
        ewts2uni_test("Ra", "\u0f6A");

        ewts2uni_test("R+ka", "\u0F6A\u0f90");
        ewts2uni_test("k+Wa", "\u0f40\u0FBA");
        ewts2uni_test("k+Ya", "\u0f40\u0FBB");
        ewts2uni_test("k+Ra", "\u0f40\u0FBC");
        ewts2uni_test("k+wa", "\u0f40\u0Fad");
        ewts2uni_test("k+ya", "\u0f40\u0Fb3");
        ewts2uni_test("k+ra", "\u0f40\u0Fb2");

        ewts2uni_test("r-I", "\u0f62\u0f81");
        ewts2uni_test("l-I", "\u0f63\u0f81");
        ewts2uni_test("r-i", "\u0f62\u0f80");
        ewts2uni_test("l-i", "\u0f63\u0f80");
        ewts2uni_test("gr-i", "\u0f42\u0f76"); // TODO(DLC)[EWTS->Tibetan]: "\u0f42\u0fb2\u0f80"
        ewts2uni_test("gr-I", "\u0f42\u0f77"); // TODO(DLC)[EWTS->Tibetan]: "\u0f42\u0fb2\u0f81"
        ewts2uni_test("gl-i", "\u0f42\u0f78"); // TODO(DLC)[EWTS->Tibetan]: "\u0f42\u0fb3\u0f80"
        ewts2uni_test("gl-I", "\u0f42\u0f79"); // TODO(DLC)[EWTS->Tibetan]: "\u0f42\u0fb3\u0f81"
    }



    /** Tests that our implementation of EWTS's wowels are correct,
     *  mostly by testing that the Unicode generated for a single
     *  wowel or set of wowels atop achen (U+0F68) is correct. */
    public void test__EWTS__wowels_on_achen() {
        ewts2uni_test("A", "\u0f68\u0f71");
        ewts2uni_test("i", "\u0f68\u0f72");
        ewts2uni_test("I", "\u0f68\u0f73");
        ewts2uni_test("u", "\u0f68\u0f74");
        ewts2uni_test("U", "\u0f68\u0f75");
        ewts2uni_test("a+r-i", "\u0f68\u0f76");
        ewts2uni_test("a+r-I", "\u0f68\u0f77");
        ewts2uni_test("a+l-i", "\u0f68\u0f78");
        ewts2uni_test("a+l-I", "\u0f68\u0f79");
        ewts2uni_test("e", "\u0f68\u0f7a");
        ewts2uni_test("ai", "\u0f68\u0f7b");
        ewts2uni_test("o", "\u0f68\u0f7c");
        ewts2uni_test("au", "\u0f68\u0f7d");
        ewts2uni_test("aM", "\u0f68\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("aH", "\u0f68\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("-i", "\u0f68\u0f80");
        ewts2uni_test("-I", "\u0f68\u0f81");
        ewts2uni_test("a~M`", "\u0f68\u0f82"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("a~M", "\u0f68\u0f83"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("a?", "\u0f68\u0f84"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("a\\u0f86", "\u0f68\u0f86");
        ewts2uni_test("a\\U0f86", "\u0f68\u0f86");
        ewts2uni_test("a\\U0F86", "\u0f68\u0f86");
        ewts2uni_test("a\\u0F86", "\u0f68\u0f86");
        ewts2uni_test("a\\u00000f86", "\u0f68\u0f86");
        ewts2uni_test("a\\u00000f86", "\u0f68\u0f86");
        ewts2uni_test("a\\u00000F86", "\u0f68\u0f86");
        ewts2uni_test("a\\u00000F86", "\u0f68\u0f86");
        ewts2uni_test("a\\u0f87", "\u0f68\u0f87");

        ewts2uni_test("aMH", "\u0f68\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("aHM", "\u0f68\u0f7f\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say


        // Than's e-mails of Aug 10 and Aug 11, 2004 say that A+i is
        // the same as I and o+o is the same as au.
        ewts2uni_test("A+i", "\u0f68\u0f73");
        ewts2uni_test("o+o", "\u0f68\u0f7d");
        ewts2uni_test("e+e", "\u0f68\u0f7b");
        ewts2uni_test("e+e+e", "\u0f68\u0f7b\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("e+e+e+e", "\u0f68\u0f7b\u0f7b"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("e+e+e+e+e", "\u0f68\u0f7b\u0f7b\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("o+e", "\u0f68\u0f7c\u0f7a");
        ewts2uni_test("u+A+i+o+e", "\u0f68\u0f74\u0f72\u0f7c\u0f7a");
        ewts2uni_test("u+A+i+o+eHM", "\u0f68\u0f74\u0f72\u0f7c\u0f7a\u0f7f\u0f7e");
        ewts2uni_test("u+A", "\u0f68\u0f75");

        ewts2uni_test("a", "\u0f68");
    }

    /** Tests that our implementation of EWTS's wowels are correct,
     *  mostly by testing that the Unicode generated for a single
     *  wowel or set of wowels atop ka (U+0F40) is correct. */
    public void test__EWTS__wowels_on_ka() {
        ewts2uni_test("kA", "\u0f40\u0f71");
        ewts2uni_test("ki", "\u0f40\u0f72");
        ewts2uni_test("kI", "\u0f40\u0f73");
        ewts2uni_test("ku", "\u0f40\u0f74");
        ewts2uni_test("kU", "\u0f40\u0f75");
        ewts2uni_test("ka+r-i", "\u0f40\u0f76");
        ewts2uni_test("ka+r-I", "\u0f40\u0f77");
        ewts2uni_test("ka+l-i", "\u0f40\u0f78");
        ewts2uni_test("ka+l-I", "\u0f40\u0f79");
        ewts2uni_test("ke", "\u0f40\u0f7a");
        ewts2uni_test("kai", "\u0f40\u0f7b");
        ewts2uni_test("ko", "\u0f40\u0f7c");
        ewts2uni_test("kau", "\u0f40\u0f7d");
        ewts2uni_test("kaM", "\u0f40\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("kaH", "\u0f40\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k-i", "\u0f40\u0f80");
        ewts2uni_test("k-I", "\u0f40\u0f81");
        ewts2uni_test("ka~M`", "\u0f40\u0f82"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("ka~M", "\u0f40\u0f83"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("ka?", "\u0f40\u0f84"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("ka\\u0f86", "\u0f40\u0f86");
        ewts2uni_test("ka\\U0f86", "\u0f40\u0f86");
        ewts2uni_test("ka\\U0F86", "\u0f40\u0f86");
        ewts2uni_test("ka\\u0F86", "\u0f40\u0f86");
        ewts2uni_test("ka\\u00000f86", "\u0f40\u0f86");
        ewts2uni_test("ka\\u00000f86", "\u0f40\u0f86");
        ewts2uni_test("ka\\u00000F86", "\u0f40\u0f86");
        ewts2uni_test("ka\\u00000F86", "\u0f40\u0f86");
        ewts2uni_test("ka\\u0f87", "\u0f40\u0f87");

        ewts2uni_test("kaMH", "\u0f40\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("kaHM", "\u0f40\u0f7f\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say


        // Than's e-mails of Aug 10 and Aug 11, 2004 say that A+i is
        // the same as I and o+o is the same as au.
        ewts2uni_test("kA+i", "\u0f40\u0f73");
        ewts2uni_test("ko+o", "\u0f40\u0f7d");
        ewts2uni_test("ke+e", "\u0f40\u0f7b");
        ewts2uni_test("ke+e+e", "\u0f40\u0f7b\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("ke+e+e+e", "\u0f40\u0f7b\u0f7b"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("ke+e+e+e+e", "\u0f40\u0f7b\u0f7b\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("ko+e", "\u0f40\u0f7c\u0f7a");
        ewts2uni_test("ku+A+i+o+e", "\u0f40\u0f74\u0f72\u0f7c\u0f7a");
        ewts2uni_test("ku+A+i+o+eHM", "\u0f40\u0f74\u0f72\u0f7c\u0f7a\u0f7f\u0f7e");
        ewts2uni_test("ku+A", "\u0f40\u0f75");

        ewts2uni_test("k", "\u0f40");
        ewts2uni_test("ka", "\u0f40");
    }

    /** Tests that our implementation of EWTS's wowels are correct,
     *  mostly by testing that the Unicode generated for a single
     *  wowel or set of wowels atop achung (U+0F60) is correct. */
    public void test__EWTS__wowels_on_achung() {
        ewts2uni_test("'A", "\u0f60\u0f71");
        ewts2uni_test("'i", "\u0f60\u0f72");
        ewts2uni_test("'I", "\u0f60\u0f73");
        ewts2uni_test("'u", "\u0f60\u0f74");
        ewts2uni_test("'U", "\u0f60\u0f75");
        ewts2uni_test("'a+r-i", "\u0f60\u0f76");
        ewts2uni_test("'a+r-I", "\u0f60\u0f77");
        ewts2uni_test("'a+l-i", "\u0f60\u0f78");
        ewts2uni_test("'a+l-I", "\u0f60\u0f79");
        ewts2uni_test("'e", "\u0f60\u0f7a");
        ewts2uni_test("'ai", "\u0f60\u0f7b");
        ewts2uni_test("'o", "\u0f60\u0f7c");
        ewts2uni_test("'au", "\u0f60\u0f7d");
        ewts2uni_test("'aM", "\u0f60\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("'aH", "\u0f60\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("'-i", "\u0f60\u0f80");
        ewts2uni_test("'-I", "\u0f60\u0f81");
        ewts2uni_test("'a~M`", "\u0f60\u0f82"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("'a~M", "\u0f60\u0f83"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("'a?", "\u0f60\u0f84"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("'a\\u0f86", "\u0f60\u0f86");
        ewts2uni_test("'a\\U0f86", "\u0f60\u0f86");
        ewts2uni_test("'a\\U0F86", "\u0f60\u0f86");
        ewts2uni_test("'a\\u0F86", "\u0f60\u0f86");
        ewts2uni_test("'a\\u00000f86", "\u0f60\u0f86");
        ewts2uni_test("'a\\u00000f86", "\u0f60\u0f86");
        ewts2uni_test("'a\\u00000F86", "\u0f60\u0f86");
        ewts2uni_test("'a\\u00000F86", "\u0f60\u0f86");
        ewts2uni_test("'a\\u0f87", "\u0f60\u0f87");

        ewts2uni_test("'aMH", "\u0f60\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("'aHM", "\u0f60\u0f7f\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say


        // Than's e-mails of Aug 10 and Aug 11, 2004 say that A+i is
        // the same as I and o+o is the same as au.
        ewts2uni_test("'A+i", "\u0f60\u0f73");
        ewts2uni_test("'o+o", "\u0f60\u0f7d");
        ewts2uni_test("'e+e", "\u0f60\u0f7b");
        ewts2uni_test("'e+e+e", "\u0f60\u0f7b\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("'e+e+e+e", "\u0f60\u0f7b\u0f7b"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("'e+e+e+e+e", "\u0f60\u0f7b\u0f7b\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("'o+e", "\u0f60\u0f7c\u0f7a");
        ewts2uni_test("'u+A+i+o+e", "\u0f60\u0f74\u0f72\u0f7c\u0f7a");
        ewts2uni_test("'u+A+i+o+eHM", "\u0f60\u0f74\u0f72\u0f7c\u0f7a\u0f7f\u0f7e");

        ewts2uni_test("'u+A", "\u0f60\u0f75");

        ewts2uni_test("'", "\u0f60");
        ewts2uni_test("'a", "\u0f60");
    }

    /** Tests that our implementation of EWTS's wowels are correct,
     *  mostly by testing that the Unicode generated for a single
     *  wowel or set of wowels atop k+Sh (U+0F69) is correct. */
    public void test__EWTS__wowels_on_kSh() {
        ewts2uni_test("k+ShA", "\u0f69\u0f71");
        ewts2uni_test("k+Shi", "\u0f69\u0f72");
        ewts2uni_test("k+ShI", "\u0f69\u0f73");
        ewts2uni_test("k+Shu", "\u0f69\u0f74");
        ewts2uni_test("k+ShU", "\u0f69\u0f75");
        ewts2uni_test("k+Sha+r-i", "\u0f69\u0f76");
        ewts2uni_test("k+Sha+r-I", "\u0f69\u0f77");
        ewts2uni_test("k+Sha+l-i", "\u0f69\u0f78");
        ewts2uni_test("k+Sha+l-I", "\u0f69\u0f79");
        ewts2uni_test("k+She", "\u0f69\u0f7a");
        ewts2uni_test("k+Shai", "\u0f69\u0f7b");
        ewts2uni_test("k+Sho", "\u0f69\u0f7c");
        ewts2uni_test("k+Shau", "\u0f69\u0f7d");
        ewts2uni_test("k+ShaM", "\u0f69\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+ShaH", "\u0f69\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+Sh-i", "\u0f69\u0f80");
        ewts2uni_test("k+Sh-I", "\u0f69\u0f81");
        ewts2uni_test("k+Sha~M`", "\u0f69\u0f82"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+Sha~M", "\u0f69\u0f83"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+Sha?", "\u0f69\u0f84"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+Sha\\u0f86", "\u0f69\u0f86");
        ewts2uni_test("k+Sha\\U0f86", "\u0f69\u0f86");
        ewts2uni_test("k+Sha\\U0F86", "\u0f69\u0f86");
        ewts2uni_test("k+Sha\\u0F86", "\u0f69\u0f86");
        ewts2uni_test("k+Sha\\u00000f86", "\u0f69\u0f86");
        ewts2uni_test("k+Sha\\u00000f86", "\u0f69\u0f86");
        ewts2uni_test("k+Sha\\u00000F86", "\u0f69\u0f86");
        ewts2uni_test("k+Sha\\u00000F86", "\u0f69\u0f86");
        ewts2uni_test("k+Sha\\u0f87", "\u0f69\u0f87");

        ewts2uni_test("k+ShaMH", "\u0f69\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+ShaHM", "\u0f69\u0f7f\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say


        // Than's e-mails of Aug 10 and Aug 11, 2004 say that A+i is
        // the same as I and o+o is the same as au.
        ewts2uni_test("k+ShA+i", "\u0f69\u0f73");
        ewts2uni_test("k+Sho+o", "\u0f69\u0f7d");
        ewts2uni_test("k+She+e", "\u0f69\u0f7b");
        ewts2uni_test("k+She+e+e", "\u0f69\u0f7b\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("k+She+e+e+e", "\u0f69\u0f7b\u0f7b"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("k+She+e+e+e+e", "\u0f69\u0f7b\u0f7b\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("k+Sho+e", "\u0f69\u0f7c\u0f7a");
        ewts2uni_test("k+Shu+A+i+o+e", "\u0f69\u0f74\u0f72\u0f7c\u0f7a");
        ewts2uni_test("k+Shu+A+i+o+eHM", "\u0f69\u0f74\u0f72\u0f7c\u0f7a\u0f7f\u0f7e");
        ewts2uni_test("k+Shu+A", "\u0f69\u0f75");

        ewts2uni_test("k+Sh", "\u0f69");
        ewts2uni_test("k+Sha", "\u0f69");
    }

    /** Tests that our implementation of EWTS's wowels are correct,
     *  mostly by testing that the Unicode generated for a single
     *  wowel or set of wowels atop phyw (U+0F55,0FB1,0FAD) is
     *  correct. */
    public void test__EWTS__wowels_on_phyw() {
        ewts2uni_test("phywA", "\u0f55\u0fb1\u0fad\u0f71");
        ewts2uni_test("phywi", "\u0f55\u0fb1\u0fad\u0f72");
        ewts2uni_test("phywI", "\u0f55\u0fb1\u0fad\u0f73");
        ewts2uni_test("phywu", "\u0f55\u0fb1\u0fad\u0f74");
        ewts2uni_test("phywU", "\u0f55\u0fb1\u0fad\u0f75");
        ewts2uni_test("phywa+r-i", "\u0f55\u0fb1\u0fad\u0f76");
        ewts2uni_test("phywa+r-I", "\u0f55\u0fb1\u0fad\u0f77");
        ewts2uni_test("phywa+l-i", "\u0f55\u0fb1\u0fad\u0f78");
        ewts2uni_test("phywa+l-I", "\u0f55\u0fb1\u0fad\u0f79");
        ewts2uni_test("phywe", "\u0f55\u0fb1\u0fad\u0f7a");
        ewts2uni_test("phywai", "\u0f55\u0fb1\u0fad\u0f7b");
        ewts2uni_test("phywo", "\u0f55\u0fb1\u0fad\u0f7c");
        ewts2uni_test("phywau", "\u0f55\u0fb1\u0fad\u0f7d");
        ewts2uni_test("phywaM", "\u0f55\u0fb1\u0fad\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("phywaH", "\u0f55\u0fb1\u0fad\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("phyw-i", "\u0f55\u0fb1\u0fad\u0f80");
        ewts2uni_test("phyw-I", "\u0f55\u0fb1\u0fad\u0f81");
        ewts2uni_test("phywa~M`", "\u0f55\u0fb1\u0fad\u0f82"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("phywa~M", "\u0f55\u0fb1\u0fad\u0f83"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("phywa?", "\u0f55\u0fb1\u0fad\u0f84"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("phywa\\u0f86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\U0f86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\U0F86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\u0F86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\u00000f86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\u00000f86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\u00000F86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\u00000F86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\u0f87", "\u0f55\u0fb1\u0fad\u0f87");

        ewts2uni_test("phywaMH", "\u0f55\u0fb1\u0fad\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("phywaHM", "\u0f55\u0fb1\u0fad\u0f7f\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say


        // Than's e-mails of Aug 10 and Aug 11, 2004 say that A+i is
        // the same as I and o+o is the same as au.
        ewts2uni_test("phywA+i", "\u0f55\u0fb1\u0fad\u0f73");
        ewts2uni_test("phywo+o", "\u0f55\u0fb1\u0fad\u0f7d");
        ewts2uni_test("phywe+e", "\u0f55\u0fb1\u0fad\u0f7b");
        ewts2uni_test("phywe+e+e", "\u0f55\u0fb1\u0fad\u0f7b\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("phywe+e+e+e", "\u0f55\u0fb1\u0fad\u0f7b\u0f7b"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("phywe+e+e+e+e", "\u0f55\u0fb1\u0fad\u0f7b\u0f7b\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("phywo+e", "\u0f55\u0fb1\u0fad\u0f7c\u0f7a");
        ewts2uni_test("phywu+A+i+o+e", "\u0f55\u0fb1\u0fad\u0f74\u0f72\u0f7c\u0f7a");
        ewts2uni_test("phywu+A+i+o+eHM", "\u0f55\u0fb1\u0fad\u0f74\u0f72\u0f7c\u0f7a\u0f7f\u0f7e");
        ewts2uni_test("phywu+A", "\u0f55\u0fb1\u0fad\u0f75");

        ewts2uni_test("phyw", "\u0f55\u0fb1\u0fad");
        ewts2uni_test("phywa", "\u0f55\u0fb1\u0fad");
    }

    /** Tests that our implementation of EWTS's wowels are correct,
     *  mostly by testing that the Unicode generated for a single
     *  wowel or set of wowels atop k+j+j+k+k+j
     *  (U+0F40,U+0F97,U+0F97,U+0F90,U+0F90,U+0F97) is correct.  I
     *  chose this stack as an example of an absurd stack. */
    public void test__EWTS__wowels_on_kjjkkj() {
        ewts2uni_test("k+j+j+k+k+jA", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f71");
        ewts2uni_test("k+j+j+k+k+ji", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f72");
        ewts2uni_test("k+j+j+k+k+jI", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f73");
        ewts2uni_test("k+j+j+k+k+ju", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f74");
        ewts2uni_test("k+j+j+k+k+jU", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f75");
        ewts2uni_test("k+j+j+k+k+ja+r-i", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f76");
        ewts2uni_test("k+j+j+k+k+ja+r-I", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f77");
        ewts2uni_test("k+j+j+k+k+ja+l-i", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f78");
        ewts2uni_test("k+j+j+k+k+ja+l-I", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f79");
        ewts2uni_test("k+j+j+k+k+je", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7a");
        ewts2uni_test("k+j+j+k+k+jai", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7b");
        ewts2uni_test("k+j+j+k+k+jo", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7c");
        ewts2uni_test("k+j+j+k+k+jau", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7d");
        ewts2uni_test("k+j+j+k+k+jaM", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+j+j+k+k+jaH", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+j+j+k+k+j-i", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f80");
        ewts2uni_test("k+j+j+k+k+j-I", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f81");
        ewts2uni_test("k+j+j+k+k+ja~M`", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f82"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+j+j+k+k+ja~M", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f83"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+j+j+k+k+ja?", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f84"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+j+j+k+k+ja\\u0f86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        ewts2uni_test("k+j+j+k+k+ja\\U0f86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        ewts2uni_test("k+j+j+k+k+ja\\U0F86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        ewts2uni_test("k+j+j+k+k+ja\\u0F86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        ewts2uni_test("k+j+j+k+k+ja\\u00000f86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        ewts2uni_test("k+j+j+k+k+ja\\u00000f86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        ewts2uni_test("k+j+j+k+k+ja\\u00000F86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        ewts2uni_test("k+j+j+k+k+ja\\u00000F86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        ewts2uni_test("k+j+j+k+k+ja\\u0f87", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f87");

        ewts2uni_test("k+j+j+k+k+jaMH", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+j+j+k+k+jaHM", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7f\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say


        // Than's e-mails of Aug 10 and Aug 11, 2004 say that A+i is
        // the same as I and o+o is the same as au.
        ewts2uni_test("k+j+j+k+k+jA+i", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f73");
        ewts2uni_test("k+j+j+k+k+jo+o", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7d");
        ewts2uni_test("k+j+j+k+k+je+e", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7b");
        ewts2uni_test("k+j+j+k+k+je+e+e", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7b\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("k+j+j+k+k+je+e+e+e", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7b\u0f7b"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("k+j+j+k+k+je+e+e+e+e", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7b\u0f7b\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("k+j+j+k+k+jo+e", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7c\u0f7a");
        ewts2uni_test("k+j+j+k+k+ju+A+i+o+e", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f74\u0f72\u0f7c\u0f7a");
        ewts2uni_test("k+j+j+k+k+ju+A+i+o+eHM", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f74\u0f72\u0f7c\u0f7a\u0f7f\u0f7e");
        ewts2uni_test("k+j+j+k+k+ju+A", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f75");

        ewts2uni_test("k+j+j+k+k+j", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97");
        ewts2uni_test("k+j+j+k+k+ja", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97");
    }

    /** Tests that the EWTS that the spec says corresponds to each
     *  codepoint really does. */
    public void test__EWTS__tags_each_unicode_value() {
        ewts2uni_test("\\u0000", "\u0000");
        ewts2uni_test("\\u0eff", "\u0eff");
        ewts2uni_test("\\u0eff", "\u0eff");
        ewts2uni_test("\\u0f00", "\u0f00");
        ewts2uni_test("\\u0f40", "\u0f40");
        ewts2uni_test("\\u0f70", "\u0f70");
        ewts2uni_test("\\u0fff", "\u0fff");
        ewts2uni_test("\\uf000", "\uf000");
        ewts2uni_test("\\uf01f", "\uf01f");
        ewts2uni_test("\\uefff", "\uefff");

        ewts2uni_test("\\ucafe0000", "\ucafe0000");
        ewts2uni_test("\\ucafe0eff", "\ucafe0eff");
        ewts2uni_test("\\ucafe0eff", "\ucafe0eff");
        ewts2uni_test("\\ucafe0f00", "\ucafe0f00");
        ewts2uni_test("\\ucafe0f40", "\ucafe0f40");
        ewts2uni_test("\\ucafe0f70", "\ucafe0f70");
        ewts2uni_test("\\ucafe0fff", "\ucafe0fff");
        ewts2uni_test("\\ucafef000", "\ucafef000");
        ewts2uni_test("\\ucafef01f", "\ucafef01f");
        ewts2uni_test("\\ucafeefff", "\ucafeefff");


        ewts2uni_test("\\u00000000", "\u00000000");
        ewts2uni_test("\\u00000eff", "\u00000eff");
        ewts2uni_test("\\u00000eff", "\u00000eff");
        ewts2uni_test("\\u00000f00", "\u00000f00");
        ewts2uni_test("\\u00000f40", "\u00000f40");
        ewts2uni_test("\\u00000f70", "\u00000f70");
        ewts2uni_test("\\u00000fff", "\u00000fff");
        ewts2uni_test("\\u0000f000", "\u0000f000");
        ewts2uni_test("\\u0000f01f", "\u0000f01f");
        ewts2uni_test("\\u0000efff", "\u0000efff");

        ewts2uni_test("\\u00000000", "\u0000");
        ewts2uni_test("\\u00000eff", "\u0eff");
        ewts2uni_test("\\u00000eff", "\u0eff");
        ewts2uni_test("\\u00000f00", "\u0f00");
        ewts2uni_test("\\u00000f40", "\u0f40");
        ewts2uni_test("\\u00000f70", "\u0f70");
        ewts2uni_test("\\u00000fff", "\u0fff");
        ewts2uni_test("\\u0000f000", "\uf000");
        ewts2uni_test("\\u0000f01f", "\uf01f");
        ewts2uni_test("\\u0000efff", "\uefff");

        ewts2uni_test("\\UcaFe0000", "\ucaFe0000");
        ewts2uni_test("\\UcaFe0eff", "\ucaFe0eff");
        ewts2uni_test("\\UcaFe0eff", "\ucaFe0eff");
        ewts2uni_test("\\UcaFe0f00", "\ucaFe0f00");
        ewts2uni_test("\\UcaFe0f40", "\ucaFe0f40");
        ewts2uni_test("\\UcaFe0f70", "\ucaFe0f70");
        ewts2uni_test("\\UcaFe0fff", "\ucaFe0fff");
        ewts2uni_test("\\UcaFef000", "\ucaFef000");
        ewts2uni_test("\\UcaFef01f", "\ucaFef01f");
        ewts2uni_test("\\UcaFeefff", "\ucaFeefff");

        // Below was semiautomatically generated from the EWTS spec's
        // 'ewts.xml' representation (early August 2004 edition):
        ewts2uni_test("v", "\u0F56\u0F39");
        ewts2uni_test("f", "\u0F55\u0F39");
        
        ewts2uni_test("oM", "\u0F00");
        ewts2uni_test("\\u0F01", "\u0F01");
        ewts2uni_test("\\u0F02", "\u0F02");
        ewts2uni_test("\\u0F03", "\u0F03");
        ewts2uni_test("@", "\u0F04");
        ewts2uni_test("#", "\u0F05");
        ewts2uni_test("$", "\u0F06");
        ewts2uni_test("%", "\u0F07");
        ewts2uni_test("!", "\u0F08");
        ewts2uni_test("\\u0F09", "\u0F09");
        ewts2uni_test("\\u0F0A", "\u0F0A");
        ewts2uni_test(" ", "\u0F0B");
        ewts2uni_test("*", "\u0F0C");
        ewts2uni_test("/", "\u0F0D");
        ewts2uni_test("//", "\u0F0E");
        ewts2uni_test(";", "\u0F0F");
        ewts2uni_test("\\u0F10", "\u0F10");
        ewts2uni_test("|", "\u0F11");
        ewts2uni_test("\\u0F12", "\u0F12");
        ewts2uni_test("\\u0F13", "\u0F13");
        ewts2uni_test(":", "\u0F14");
        ewts2uni_test("\\u0F15", "\u0F15");
        ewts2uni_test("\\u0F16", "\u0F16");
        ewts2uni_test("\\u0F17", "\u0F17");
        ewts2uni_test("\\u0F18", "\u0F18"); // TODO(DLC)[EWTS->Tibetan]: error combiner
        ewts2uni_test("\\u0F19", "\u0F19"); // TODO(DLC)[EWTS->Tibetan]: error combiner
        ewts2uni_test("\\u0F1A", "\u0F1A");
        ewts2uni_test("\\u0F1B", "\u0F1B");
        ewts2uni_test("\\u0F1C", "\u0F1C");
        ewts2uni_test("\\u0F1D", "\u0F1D");
        ewts2uni_test("\\u0F1E", "\u0F1E");
        ewts2uni_test("\\u0F1F", "\u0F1F");
        ewts2uni_test("0", "\u0F20");
        ewts2uni_test("1", "\u0F21");
        ewts2uni_test("2", "\u0F22");
        ewts2uni_test("3", "\u0F23");
        ewts2uni_test("4", "\u0F24");
        ewts2uni_test("5", "\u0F25");
        ewts2uni_test("6", "\u0F26");
        ewts2uni_test("7", "\u0F27");
        ewts2uni_test("8", "\u0F28");
        ewts2uni_test("9", "\u0F29");
        ewts2uni_test("\\u0F2A", "\u0F2A");
        ewts2uni_test("\\u0F2B", "\u0F2B");
        ewts2uni_test("\\u0F2C", "\u0F2C");
        ewts2uni_test("\\u0F2D", "\u0F2D");
        ewts2uni_test("\\u0F2E", "\u0F2E");
        ewts2uni_test("\\u0F2F", "\u0F2F");
        ewts2uni_test("\\u0F30", "\u0F30");
        ewts2uni_test("\\u0F31", "\u0F31");
        ewts2uni_test("\\u0F32", "\u0F32");
        ewts2uni_test("\\u0F33", "\u0F33");
        ewts2uni_test("=", "\u0F34");
        ewts2uni_test("~X", "\u0F35");
        ewts2uni_test("\\u0F36", "\u0F36");
        ewts2uni_test("X", "\u0F37"); // TODO(DLC)[EWTS->Tibetan]: error combiner
        ewts2uni_test("\\u0F38", "\u0F38");
        ewts2uni_test("^", "\u0F39"); // TODO(DLC)[EWTS->Tibetan]: error combiner
        ewts2uni_test("<", "\u0F3A");
        ewts2uni_test(">", "\u0F3B");
        ewts2uni_test("(", "\u0F3C");
        ewts2uni_test(")", "\u0F3D");
        ewts2uni_test("\\u0F3E", "\u0F3E"); // TODO(DLC)[EWTS->Tibetan]: error combiner
        ewts2uni_test("\\u0F3F", "\u0F3F"); // TODO(DLC)[EWTS->Tibetan]: error combiner
        ewts2uni_test("k", "\u0F40");
        ewts2uni_test("kh", "\u0F41");
        ewts2uni_test("g", "\u0F42");
        ewts2uni_test("g+h", "\u0F43");
        ewts2uni_test("ng", "\u0F44");
        ewts2uni_test("c", "\u0F45");
        ewts2uni_test("ch", "\u0F46");
        ewts2uni_test("j", "\u0F47");
        ewts2uni_test("ny", "\u0F49");
        ewts2uni_test("T", "\u0F4A");
        ewts2uni_test("Th", "\u0F4B");
        ewts2uni_test("D", "\u0F4C");
        ewts2uni_test("D+h", "\u0F4D");
        ewts2uni_test("N", "\u0F4E");
        ewts2uni_test("t", "\u0F4F");
        ewts2uni_test("th", "\u0F50");
        ewts2uni_test("d", "\u0F51");
        ewts2uni_test("d+h", "\u0F52");
        ewts2uni_test("n", "\u0F53");
        ewts2uni_test("p", "\u0F54");
        ewts2uni_test("ph", "\u0F55");
        ewts2uni_test("b", "\u0F56");
        ewts2uni_test("b+h", "\u0F57");
        ewts2uni_test("m", "\u0F58");
        ewts2uni_test("ts", "\u0F59");
        ewts2uni_test("tsh", "\u0F5A");
        ewts2uni_test("dz", "\u0F5B");
        ewts2uni_test("dz+h", "\u0F5C");
        ewts2uni_test("w", "\u0F5D");
        ewts2uni_test("zh", "\u0F5E");
        ewts2uni_test("z", "\u0F5F");
        ewts2uni_test("'", "\u0F60");
        ewts2uni_test("y", "\u0F61");
        ewts2uni_test("r", "\u0F62");
        ewts2uni_test("l", "\u0F63");
        ewts2uni_test("sh", "\u0F64");
        ewts2uni_test("Sh", "\u0F65");
        ewts2uni_test("s", "\u0F66");
        ewts2uni_test("h", "\u0F67");
        ewts2uni_test("a", "\u0F68");
        ewts2uni_test("k+Sh", "\u0F69");
        ewts2uni_test("R+", "\u0F6A"); // TODO(DLC)[EWTS->Tibetan]: move to illegal test
        ewts2uni_test("A", "\u0F71");
        ewts2uni_test("i", "\u0F72");
        ewts2uni_test("I", "\u0F73");
        ewts2uni_test("u", "\u0F74");
        ewts2uni_test("U", "\u0F75");
        ewts2uni_test("r-i", "\u0F76");
        ewts2uni_test("r-I", "\u0F77");
        ewts2uni_test("l-i", "\u0F78");
        ewts2uni_test("l-I", "\u0F79");
        ewts2uni_test("e", "\u0F7A");
        ewts2uni_test("ai", "\u0F7B");
        ewts2uni_test("o", "\u0F7C");
        ewts2uni_test("au", "\u0F7D");
        ewts2uni_test("M", "\u0F7E");
        ewts2uni_test("H", "\u0F7F");
        ewts2uni_test("-i", "\u0F80");
        ewts2uni_test("-I", "\u0F81");
        ewts2uni_test("~M`", "\u0F82");
        ewts2uni_test("~M", "\u0F83");
        ewts2uni_test("?", "\u0F84");
        ewts2uni_test("&", "\u0F85");
        ewts2uni_test("\\u0F86", "\u0F86");
        ewts2uni_test("\\u0F87", "\u0F87");
        ewts2uni_test("\\u0F88", "\u0F88");
        ewts2uni_test("\\u0F89", "\u0F89");
        ewts2uni_test("\\u0F8A", "\u0F8A");
        ewts2uni_test("\\u0F8B", "\u0F8B");
        ewts2uni_test("k", "\u0F90"); // TODO(DLC)[EWTS->Tibetan]: NO!  Need a+...
        ewts2uni_test("kh", "\u0F91");
        ewts2uni_test("g", "\u0F92");
        ewts2uni_test("g+h", "\u0F93");
        ewts2uni_test("ng", "\u0F94");
        ewts2uni_test("c", "\u0F95");
        ewts2uni_test("ch", "\u0F96");
        ewts2uni_test("j", "\u0F97");
        ewts2uni_test("ny", "\u0F99");
        ewts2uni_test("T", "\u0F9A");
        ewts2uni_test("Th", "\u0F9B");
        ewts2uni_test("D", "\u0F9C");
        ewts2uni_test("D+h", "\u0F9D");
        ewts2uni_test("N", "\u0F9E");
        ewts2uni_test("t", "\u0F9F");
        ewts2uni_test("th", "\u0FA0");
        ewts2uni_test("d", "\u0FA1");
        ewts2uni_test("d+h", "\u0FA2");
        ewts2uni_test("n", "\u0FA3");
        ewts2uni_test("p", "\u0FA4");
        ewts2uni_test("ph", "\u0FA5");
        ewts2uni_test("b", "\u0FA6");
        ewts2uni_test("b+h", "\u0FA7");
        ewts2uni_test("m", "\u0FA8");
        ewts2uni_test("ts", "\u0FA9");
        ewts2uni_test("tsh", "\u0FAA");
        ewts2uni_test("dz", "\u0FAB");
        ewts2uni_test("dz+h", "\u0FAC");
        ewts2uni_test("w", "\u0FAD");
        ewts2uni_test("zh", "\u0FAE");
        ewts2uni_test("z", "\u0FAF");
        ewts2uni_test("'", "\u0FB0");
        ewts2uni_test("y", "\u0FB1");
        ewts2uni_test("r", "\u0FB2");
        ewts2uni_test("l", "\u0FB3");
        ewts2uni_test("sh", "\u0FB4");
        ewts2uni_test("Sh", "\u0FB5");
        ewts2uni_test("s", "\u0FB6");
        ewts2uni_test("h", "\u0FB7");
        ewts2uni_test("a", "\u0FB8");
        ewts2uni_test("k+Sh", "\u0FB9");
        ewts2uni_test("+W", "\u0FBA"); // TODO(DLC)[EWTS->Tibetan]: move to illegal test
        ewts2uni_test("+Y", "\u0FBB");
        ewts2uni_test("+R", "\u0FBC");
        ewts2uni_test("\\u0FBE", "\u0FBE");
        ewts2uni_test("\\u0FBF", "\u0FBF");
        ewts2uni_test("\\u0FC0", "\u0FC0");
        ewts2uni_test("\\u0FC1", "\u0FC1");
        ewts2uni_test("\\u0FC2", "\u0FC2");
        ewts2uni_test("\\u0FC3", "\u0FC3");
        ewts2uni_test("\\u0FC4", "\u0FC4");
        ewts2uni_test("\\u0FC5", "\u0FC5");
        ewts2uni_test("\\u0FC6", "\u0FC6");
        ewts2uni_test("\\u0FC7", "\u0FC7");
        ewts2uni_test("\\u0FC8", "\u0FC8");
        ewts2uni_test("\\u0FC9", "\u0FC9");
        ewts2uni_test("\\u0FCA", "\u0FCA");
        ewts2uni_test("\\u0FCB", "\u0FCB");
        ewts2uni_test("\\u0FCC", "\u0FCC");
        ewts2uni_test("\\u0FCF", "\u0FCF");
        ewts2uni_test("\\u0FD0", "\u0FD0");
        ewts2uni_test("\\u0FD1", "\u0FD1");
        ewts2uni_test("_", "\u0020");
        ewts2uni_test("\\u534D", "\u534D");
        ewts2uni_test("\\u5350", "\u5350");
        ewts2uni_test("\\u0F88+k", "\u0F880F90"); // TODO(DLC)[EWTS->Tibetan]:
        ewts2uni_test("\\u0F88+kh", "\u0F880F91");
        /* TODO(DLC)[EWTS->Tibetan]: NOW do we want to ever generate \u0f21?  EWTS->TMW and this makes sense, but EWTS->Unicode? */
        ewts2uni_test("\\uF021", "\uF021");
        ewts2uni_test("\\uF022", "\uF022");
        ewts2uni_test("\\uF023", "\uF023");
        ewts2uni_test("\\uF024", "\uF024");
        ewts2uni_test("\\uF025", "\uF025");
        ewts2uni_test("\\uF026", "\uF026");
        ewts2uni_test("\\uF027", "\uF027");
        ewts2uni_test("\\uF028", "\uF028");
        ewts2uni_test("\\uF029", "\uF029");
        ewts2uni_test("\\uF02A", "\uF02A");
        ewts2uni_test("\\uF02B", "\uF02B");
        ewts2uni_test("\\uF02C", "\uF02C");
        ewts2uni_test("\\uF02D", "\uF02D");
        ewts2uni_test("\\uF02E", "\uF02E");
        ewts2uni_test("\\uF02F", "\uF02F");
        ewts2uni_test("\\uF030", "\uF030");
        ewts2uni_test("\\uF031", "\uF031");
        ewts2uni_test("\\uF032", "\uF032");
        ewts2uni_test("\\uF033", "\uF033");
        ewts2uni_test("\\uF034", "\uF034");
        ewts2uni_test("\\uF035", "\uF035");
        ewts2uni_test("\\uF036", "\uF036");
        ewts2uni_test("\\uF037", "\uF037");
        ewts2uni_test("\\uF038", "\uF038");
        ewts2uni_test("\\uF039", "\uF039");
        ewts2uni_test("\\uF03A", "\uF03A");
        ewts2uni_test("\\uF03B", "\uF03B");
        ewts2uni_test("\\uF03C", "\uF03C");
        ewts2uni_test("\\uF03D", "\uF03D");
        ewts2uni_test("\\uF03E", "\uF03E");
        ewts2uni_test("\\uF03F", "\uF03F");
        ewts2uni_test("\\uF040", "\uF040");
        ewts2uni_test("\\uF041", "\uF041");
        ewts2uni_test("\\uF042", "\uF042");
    }

    // TODO(DLC)[EWTS->Tibetan]: test that "\[JAVA_SOURCE_WILL_NOT_COMPILE_WITHOUT_ME]uxxxx " works out well

    /** Tests that certain strings are not legal EWTS. */
    public void test__EWTS__illegal_things() {
        assert_EWTS_error("k\\u0f19"); // only numbers combine with f19,f18,f3e,f3f
        assert_EWTS_error("k\\u0f18"); // only numbers combine with f19,f18,f3e,f3f
        assert_EWTS_error("k\\u0f3e"); // only numbers combine with f19,f18,f3e,f3f
        assert_EWTS_error("k\\u0f3f"); // only numbers combine with f19,f18,f3e,f3f

        assert_EWTS_error("kSha"); // use "k+Sha" instead

        assert_EWTS_error("pM"); // use "paM" instead (TODO(DLC)[EWTS->Tibetan]: NOW NO!)
        assert_EWTS_error("pH"); // use "paM" instead (TODO(DLC)[EWTS->Tibetan]: NOW NO!)
        assert_EWTS_error("kja"); // use "kaja" or "k.ja" instead

        assert_EWTS_error("kA+u"); // use "ku+A" (bottom-to-top) or "kU" instead


        assert_EWTS_error("bna"); // use "b+na" or "bana" instead // TODO(DLC)[EWTS->Tibetan]: tell D. Chapman about this; an old e-mail said my test cases would be brutal and here's brutal
        assert_EWTS_error("bn?");
        assert_EWTS_error("bni");
        assert_EWTS_error("bnA");
        assert_EWTS_error("bn-I");

        // a+r is not a standard stack; neither is a+l:
        assert_EWTS_error("ar-i");
        assert_EWTS_error("ar-I");
        assert_EWTS_error("al-i");
        assert_EWTS_error("al-I");

        assert_EWTS_error("g..ya"); // use "g.ya" instead
        assert_EWTS_error("g"); // use "ga" instead TODO(DLC)[EWTS->Tibetan]:?
    }
}

        // TODO(DLC)[EWTS->Tibetan]: if 'k' were illegal, then would you have to say
        // 'ka\u0f84' or would 'k\u0f84' be legal?


        // TODO(DLC)[EWTS->Tibetan]: ask than what's the order, top to bottom, of
        // u,i,o,e,M,A,I,-i,-I,ai,au,etc.?  TODO(DLC)[EWTS->Tibetan]: ANSWER: Basically, there are a few classes -- above, below, both.

        // TODO(DLC)[EWTS->Tibetan]: NOW: write a tool that takes Tibetan Unicode and finds
        // flaws in it.  E.g., if Unicode 4.0 says that
        // \u0f40\u0f7a\u0f74 is illegal (thus \u0f40\u0f74\u0f7a is
        // what you probably intended), have it find \u0f7a\u0f74.
        //
        // TODO(DLC)[EWTS->Tibetan]:: and have it find \u0f7a\u0f7a and suggest \u0f7b, etc.
        //
        // TODO(DLC)[EWTS->Tibetan]: and \u0f7f\u0f7e is probably illegal and should be switched?

        // TODO(DLC)[EWTS->Tibetan]: flesh out \[JAVA_SOURCE_WILL_NOT_COMPILE_WITHOUT_ME]u rules in lexing, is it like Java (where in Java source code, escapes are done in a pre-lexing pass)? no, right, \u0060 causes \u0060 in the output...  and \u0f40a is not like ka.  escapes separate tsheg bars as far as lexing is concerned, yes?  But we use them (and only them, i.e. there is no other transliteration available) for some Tibetan Unicode characters, and then ka\[JAVA_SOURCE_WILL_NOT_COMPILE_WITHOUT_ME]u0fXX may need to seem Java-ish, maybe?

        // TODO(DLC)[EWTS->Tibetan]: spell-check ewts spec, puncutation e.g.


        // TODO(DLC)[EWTS->Tibetan]: ask than aM, not M, is legal, what else is like this? ~M`?  0f84?

        // TODO(DLC)[EWTS->Tibetan]: NOW 0f84 ? not a? but ? according to rule n=7
        /* TODO(DLC)[EWTS->Tibetan]: make a method that tests the unicode directly and by going from ewts/acip->tmw->unicode. */

    // TODO(DLC)[EWTS->Tibetan]: s/anyways/anyway/g in ewts spec
    // TODO(DLC)[EWTS->Tibetan]: s/(Spacebar)/(Space)/g
/* TODO(DLC)[EWTS->Tibetan]: in spec, inconsistency:
   <code>0F880F90</code>
        <code>0F880F91</code>
            <code rend="U+0F55 U+0F39">\u0F55\u0F39</code>
            <code rend="U+0F56 U+0F39">\u0F56\u0F39</code>

TODO(DLC)[EWTS->Tibetan]:: also, <equiv>F042</equiv> is inconsistent with <equiv></equiv> for U+0f01.
 */