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

package org.thdl.tib.text.tshegbar;

import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlLazyException;

/** DLC FIXMEDOC and turn into junit tests.
 *
 *  @author David Chandler
 */
public class test implements UnicodeConstants {
    public void testTopToBottomForLegalGraphemeClusters() {
        // DLC delete:  System.out.println("see this " + UnicodeUtils.unicodeStringToString(new UnicodeGraphemeCluster("\u0F00").getTopToBottomCodepoints()));
        ThdlDebug.verify(new UnicodeGraphemeCluster("\u0F00").getTopToBottomCodepoints().equals("\u0F7E\u0F7C\u0F68"));
        ThdlDebug.verify(new UnicodeGraphemeCluster("\u0F66\u0F93\u0F91\u0FA7\u0F92\u0FAD\u0F77\u0F83\u0F86\u0F84").getTopToBottomCodepoints().equals("\u0F86\u0F83\u0F80\u0F66\u0F92\u0FB7\u0F91\u0FA6\u0FB7\u0F92\u0FB2\u0FAD\u0F71\u0F84"));
    }

    /** tests this package */
    public test() throws Throwable {
        super();

        testTopToBottomForLegalGraphemeClusters();
    }

    /** Unit tests this package. */
    public static void main(String argv[]) {
        try {
            System.out.println("yo");
            new test();
        } catch (ThdlLazyException ex) {
            System.out.println("Oopsy!");
            ex.getRealException().printStackTrace();
        } catch (Throwable t) {
            System.out.println("Oops!");
            System.out.println(t);
            System.out.println("at:");
            t.printStackTrace();
        }
    }
}
