************************************************************************
*	  INSTRUCTIONS FOR WYLIE WORD VERSION 1.0                            *
*		MS Word Macro for Windows	    		                                 *
* 	Written by Than Garson			  	                                   *
*		Samantabhadra Project, UVa                              		       *
*		September, 2001		       			                                     *
************************************************************************

ACKNOWLEDGEMENTS: 
The public domain Tibetan fonts provided in this package 
have been made available through the gracious donations of the Tibetan Computer 
Company and the Trace Foundation. All other material is the result of the 
collaborative efforts of the Tibetan and Himalayan Digital Library. 

Web-site: http://iris.lib.virginia.edu/tibet/index.html

LICENSE INFO: 
The contents of this file are subject to the THDL Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. The license is included in this Zip archive, or you may 
obtain a copy of the License on the THDL web site:

     http://iris.lib.virginia.edu/tibet/tools/license.html

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is the Tibetan and Himalayan Digital
Library (THDL). Portions created by the THDL are Copyright 2001 THDL.
All Rights Reserved. 

ABOUT WYLIE WORD 1.0:
This Zip file contains all the necessary files you will need to install and run
the Wylie Word macro for Microsoft Word, EXCEPT the font files which are available  
for free download from the Tibetan and Himalayan Digital Library's tools page. 
This document describes three aspects of the Wylie Word macro: I. Installation, 
II. Fonts, III. How to Use the Macro.

I. INSTALLATION

The Zip archive contains the following files:

1. Extended Wylie Scheme-rev.doc - This is a Word document that contains 
	the latest extended Wylie transliteration scheme (6/10/2001), as 
	used by the Tibetan and Himalayan Digital Library and proposed by 
	the Library as a draft for a standard transliteration scheme. The
	Wylie Word program uses this scheme with a slight modification for
	entering Sanskrit stacks.

2. WylieWord1.dot - This is a Word document template that actually houses
	the macro. It can be used in a number of ways.
	
	a. Stored on your hard drive it will act like a regular document and
	   the macro can be run only from within this document.

	b. If it is placed in the templates folder for MS Word, it will act
	   as a standard template and will be one of the options available
	   when you choose New under the File menu. The macro will only be 
	   runnable from within documents that use this template. To set this
	   up, put the wywrd1.dot file in the following folder, assuming "C"
           is your hard drive:

		C:\Windows\Application Data\Microsoft\Templates

	c. If it is placed in the start up folder, the macro will be available
	   from ANY word document, since it is loaded the minute you start word.
	   To do this, put the WyEd2.2b.dot file in the following folder:

		C:\Program Files\Office\StartUp

           or, if you are networked,

		C:\WINDOWS\All Users\Start Menu\Programs\StartUp

3. tibwn.ini - This is the data file that is used to convert the extended Wylie
	       to the Tibetan fonts. It needs to reside at the root level of your
	       primary hard drive, such as C:. Not in any folder. This way wherever 
	       the document it is knows where to fide it and can read the necessary
	       information. 

II. FONTS

The macro is written so that it can be used with any font as long as that fonts 
code pages are avaialable. In this first release, we have included the most useful 
font, Tibetan Machine Web, because this font is available for download free from the 
THDL's tools page and because it is a cross-platform, web-usable font that should 
significantly ease the transfer of data in Tibetan script. The Tibetan Machine Web 
font is actually a set of fonts made public-domain through the collaboration between 
the Tibetan Computer Company, the Trace Organization, and the THDL. Other fonts may 
be added, though our ultimate goal is to utilize a public-domain Unicode Tibetan font,
once such a font is available. There are ten font files: "timwn.ttf" through "timwn9.ttf".
They can be downloaded from the tools section of the Tibetan and Himalayan Digital Library at:

 (http://iris.lib.virginia.edu/tibet/tools/index.html). 

All ten files must be installed in your font folder, which is contained within the 
control panel. To install the fonts, first unpack them into a temporary folder. 
Then, under the Start menu, choose Settings and then Control Panel and click on 
the Font option. From the Font folder window, click on the File menu and choose 
Install New Font. With the resulting pop-up window navigate to the temporary folder, 
where the Fonts were unpacked, and choose all 10 fonts. When you click OK, they will 
be installed in your font folder.

III. USING THE MACRO

The Wylie Word macro is very simple to use, provided one is familiar with 
the extended Wylie transliteration scheme. When the wywrd1.dot file is opened in any 
of the ways described above, a new menu will appear between the Table and the Window menus. 
It is labelled Tibetan. It gives you the following options:

   A. Type Tibetan with Wylie (F8): Converts Wylie to Tibetan font as it is typed in.
   B. Convert Wylie to Tibetan (F9): Converts already-typed in and highlighted Wylie to Tibetan.
   C. Insert English in Tibetan (F7): Changes to the default English font and size, positioning 
      it so that it is in line with the Tibetan.
   D. Adjust Preferences (Ctrl+Alt+Shift P): Brings up the preference form that provides you with
      a number of changeable features, discussed below.

Each of these will be discussed in turn.

A. Type Tibetan with Wylie (F8):

1. Starting the macro
Choosing the Tibetan option begins running the macro. One can also start the macro by 
typing CTRL + F8. At this point, one can type in standard Wylie and the corresponding 
Tibetan glyphs should appear. Standard Tibetan stacks will be created automatically 
as one types. For non-Standard, Sanskrit stacks, see 3. below. The macro will continue 
running if you use the navigational arrows, the home or end key, page-up or down, delete 
or backspace, Enter, Ctrl S to save, Ctrl A to select all, Ctrl P to print. However, the 
macro will stop once you  use the mouse or press the ESC key. The ESC key will automatically 
set the font back to the default English font. Once either the mouse is used or the ESC key 
is pressed, the program will need to be restarted if you wish to return to typing Tibetan.

2. Common Sanskrit-derived Glyphs
Common, stand-alone transliterations of Sanskrit letters are generally accessible by 
using capitals. That is, for the reverse Tibetan 'da' which represents the retroflex 
'da' in Sanskrit, type capital D. For long vowels, such as long a, type capital A; 
long i, type capital I; and so forth. Thus, "dakini" would be "DAkinI".

3. Non-Standard Sanskrit-derived Stacks
To achieve the less-common stacks that are the result of Sanskrit transliteration, 
the method is also outlined in the transliteration scheme. This is an easy and 
unambiguous way  to enter unusual Tibetan stacks that represent Sanskrit characters. 
The method is to use the "+" key, placing a plus sign between each letter of the stack. 
Thus, the word "sattva" would be typed in as "sat+t+wa" (There is no "v" in Tibetan. 
"w" is always used instead!") and "samuccaya" would be "samuts+tsaya" (The "ts", "tsh", 
and "dz" are used to transcribe the Sanskrit palatals "c", "ch", and "j".)

4. Punctuation
WyEd2.3 allows for the use of Western punctuation interspersed with Tibetan. Common
Tibetan punctuation remains the same:
	(space) - tshek
	_ (underscore) - (white space)
	/ (slash) - shad
However, the parentheses (), the brackets [], the colon :, and the quotation marks "" all will
appear in their Latin-font glyphs in line with the Tibetan. To obtain, the Tibetan symbol that
any of these represent in the Transliteration Scheme, use the Sanskrit stack window.
		
B. Convert Wylie to Tibetan (F9)

This option allows for the conversion of already typed in Wylie. To use it, the Wylie first needs to be
highlighted, then one chooses this option either by pressing F9 or from the menu. The program
will REPLACE the highlighted Wylie with the corresponding glyphs from the Tibetan font. 

C. Insert English in Tibetan (F7)

This option allows for the insertion of English font within a Tibetan line. This is necessary
because of the nature of the font. As Tibetan glyphs in general require more line height, their base
line is raise to provide more room. As a result, Tibetan glyphs appear raised when next to a Latin-
based font. This is solved by adjusting the relative position of the English or Tibetan. In the 
preference form discussed below, one can choose whether to have the Tibetan font be base-line (that is,
the document is primarily Tibetan with English interspersed) or English base-line (where the docuement
is primarily English with Tibetan inserted). In either case, choosing this option will adjust the
relative height of the insertion point so that the English entered will be in line with the Tibetan.
(NOTE: This will work only if the base-line option is the same for both fonts. One does NOT want to
type in Tibetan, then switch the base-line option, and then type in English. If the base-line option
remains the same, the fonts should be in line. Slight variations between fonts and computers can be
adjusted through the preferences window.)

D. Adjust Preferences (Ctrl+Alt+Shift P)

The final option in the menu allows one to adjust the preferences for the macro. These are written as
an ini file to the hard drive in the root directory (e.g., C:\weprefs.ini). If the file is not found,
the macro will automatically create it and update it as changes to the preferences are made. The
following is an explanation of each item in the preference window:

Tibetan Font: TibetanMachineWeb -- At the moment this cannot be changed. The only font that works with
				   this program is that font.

Size: 22 -- This is the default size of the Tibetan font when the macro is used. It can be changed
	    according to one's needs. Of course, once entered the size of the Tibetan font can be 
	    changed as one would any font.

Position: 0 -- This is the relative vertical position of the Tibetan character glyphs above (+) or 
	       below (-) what Word considers to be the base line in points. 0 is the position that fonts 
	       normally appear in. However, next to a Latin-font also at position 0, the Tibetan font 
	       will appear below or subscripted in comparison with the Latin-font. Thus, to insert 
	       Tibetan into already extant English, it needs to be raised 6pts. Conversely, English 
	       within base-line Tibetan, needs to be lowered by 6pts or -6.

English Font: Times New Roman 
	&
Size: 12 -- These represent the default English font. They can both be changed by clicking on the
	    "Choose English Font" button. The size can also be changed manually in the corresponding
	    box.

Position: 6 -- This is the relative vertical position of the English font in comparison to the Tibetan.
	       If the Tibetan and English do not appear in line on your computer, minor adjustments 
	       can be made by changing these two position values.

Tibetan Base-line or English Base-line --
		These two toggle buttons provide the user with different options according to whether
		one wants primarily to enter Tibetan with English inserted within it (Tibetan Base-line)
		or one wants to enter English with Tibetan inserted within it (English Base-line).
		Whichever font is considered to be the base-line will have a vertical position of 0 and
		the other font will be correspondingly raised or lowered to be in-line with the 
		base-line. Thus, with Tibetan Base-line the default is Tibetan position 0 and 
		English position -6. If English Base-line is chosen, these are switched so that Tibetan
		position is 6 and the English position is 0. 

Punctuation (followed by a list box) -- This option allows the user to indicate which punctuation 
		marks are to remain in the English default font. In general, all the punctuation
	  	marks available on the standard keyboard are used to transliterate a Tibetan punctuation
		mark. However, as many of these marks are used very rarely while many of the "English"
		punctuation marks are used with the Tibetan font--quotation marks, colon, parentheses,
		etc.--any of the marks on this list will remain in their "English" form. The list comes
		with the following punctuation marks as its default: ()"[]:?!_<>

		Marks can be added to the list by clicking on the label "Puncutation:". The user will
		then be prompted for the mark to be added.

		Marks can be deleted from the list by scrolling to that particular mark on the list
		and clicking on it. The program will then ask if that is the mark one wants to remove.

Size: 16 -- This indicates the size of the puncutation marks that remain in the English default font.
	    Because the Tibetan font needs to be larger in size to be readable, inserted "English"
	    punctuation needs to be slightly larger than the default English size, if used with
	    Tibetan, i.e., quotes or parentheses around a Tibetan phrase. This number can be adjusted 
	    according to the user's needs.

Position: -8  -- This is the relative vertical position of the inserted punctuation marks and can be
		 adjusted according to the user's needs. Note that the punctuation position will be
		 lowered or raised automatically when either Tibetan-base line or English-base line is 
		 chosen.
