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

package org.thdl.tib.input;

import org.thdl.tib.input.JskadKeyboard;

/** A JskadKeyboardFactory determines which Tibetan keyboards Jskad
    supports.  Adding a new one is as easy as adding 3 lines of text
    to this class's source code.
*/
public class JskadKeyboardFactory {
    public static JskadKeyboard[] getAllAvailableJskadKeyboards() {
        return new JskadKeyboard[] {
            new JskadKeyboard("Extended Wylie",
                              null,
                              "Wylie_keyboard.rtf"),
            new JskadKeyboard("TCC Keyboard #1",
                              "tcc_keyboard_1.ini",
                              "TCC_keyboard_1.rtf"),
            new JskadKeyboard("TCC Keyboard #2",
                              "tcc_keyboard_2.ini",
                              "TCC_keyboard_2.rtf"),
            new JskadKeyboard("Sambhota Keymap One",
                              "sambhota_keyboard_1.ini",
                              "Sambhota_keymap_one.rtf"),
            new JskadKeyboard("Asian Classics Input Project (ACIP)",
                              "acip_keyboard.ini",
                              null)
        };
    }
}

