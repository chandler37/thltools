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
Library (THDL). Portions created by the THDL are Copyright 2001-2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.tshegbar;


/** DLC FIXMEDOC */
class TransitionInstruction implements UnicodeReadingStateMachineConstants {
    private TransitionInstruction() { super(); }
    TransitionInstruction(int nextState, int action) {
        super();

        assert(action == ACTION_CONTINUES_GRAPHEME_CLUSTER
               || action == ACTION_BEGINS_NEW_GRAPHEME_CLUSTER
               || action == ACTION_PREPEND_WITH_0F68);

        assert(nextState == STATE_START
               || nextState == STATE_READY
               || nextState == STATE_DIGIT
               || nextState == STATE_STACKING
               || nextState == STATE_STACKPLUSACHUNG
               || nextState == STATE_PARTIALMARK);

        // we start in the start state, but we can never return to it.
        assert(nextState != STATE_START);
        
        this.nextState = nextState;
        this.action = action;
    }

    /** the state (e.g., {@link #STATE_READY}) to which to transition
     *  next */
    private int nextState;
    
    /** the action to perform upon transition, either {@link
     *  #ACTION_CONTINUES_GRAPHEME_CLUSTER}, {@link
     *  #ACTION_BEGINS_NEW_GRAPHEME_CLUSTER}, or {@link
     *  #ACTION_PREPEND_WITH_0F68} */
    private int action;

    int getAction() { return action; }
    int getNextState() { return nextState; }
}
