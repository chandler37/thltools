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

package org.thdl.tib.text;

/**
* An exception thrown whenever ACIP->TMW conversion in the Jskad GUI
* runs into invalid ACIP.
* @author David Chandler */
public class InvalidACIPException extends Exception {
    private String error;

/**
* Creates an InvalidACIPException.
* @param s an error message
*/
    public InvalidACIPException(String s) {
        error = s;
    }

/**
* Gets the error message.
*/
    public String getMessage() {
        return error;
    }
}
