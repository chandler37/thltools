/*
The contents of this file are subject to the AMP Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the AMP web site 
(http://www.tibet.iteso.mx/Guatemala/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is Andres Montano Pellegrini. Portions
created by Andres Montano Pellegrini are Copyright 2001 Andres Montano
Pellegrini. All Rights Reserved. 

Contributor(s): ______________________________________.
*/
package org.thdl.tib.scanner;

import org.thdl.tib.text.InvalidTransliterationException;
import org.thdl.tib.text.TibTextUtils;
import org.thdl.tib.text.TibetanDocument;
import org.thdl.tib.text.reverter.Converter;
import org.thdl.tib.text.ttt.EwtsToUnicodeForXslt;

/**
 * Wrap-up class for the various converters that the Translation Tool needs.
 * All conversions are done by static methods meant to be as straight-forward
 * and simple as possible not caring about error or warning messages.
 * 
 * @author Andres Montano
 *
 */
public class BasicTibetanTranscriptionConverter {
	
	/** Converts from the Acip transliteration scheme to EWTS.*/
	public static String acipToWylie(String acip)
	{
    	TibetanDocument tibDoc = new TibetanDocument();
    	try
    	{
    		TibTextUtils.insertTibetanMachineWebForTranslit(false, acip, tibDoc, 0, false);
    	}
    	catch (InvalidTransliterationException e)
    	{
    		return null;
    	}
    	return tibDoc.getWylie(new boolean[] { false });
		
		/* char caract[], ch, chP, chN;
		String nuevaLinea;
		int i, len;
		boolean open;
		
		caract = acip.toCharArray();
		len = acip.length();
		for (i=0; i<len; i++)
		{
			if (Character.isLowerCase(caract[i]))
				caract[i] = Character.toUpperCase(caract[i]);
			else if (Character.isUpperCase(caract[i]))
				caract[i] = Character.toLowerCase(caract[i]);
		}
		nuevaLinea = new String(caract);
		
		/* ahora hacer los cambios de Michael Roach ts -> tsh, tz -> ts, v -> w,
		TH -> Th, kSH, kaSH -> k+Sh, SH -> Sh, : -> H, dh -> d+h, gh -> g+h, bh -> b+h, dzh -> dz+h,
	    aa -> a, a'a -> A, ai->i, aee ->ai, au->u, aoo->au, ae->e,
		ao->o, ee->ai, oo->au, 'I->-I I->-i,  a'i->I, a'u->U, a'e->E, a'o->O,
		a'i->I, a'u->U, a'e->E, a'o->O, ,->/, # -> @##, * -> @#, \ -> ?, ` -> !,
		/-/ -> (-), ga-y -> g.y, g-y -> g.y, na-y -> n+y
		
		nuevaLinea = replace(nuevaLinea, "ts", "tq");
		nuevaLinea = replace(nuevaLinea, "tz", "ts");
		nuevaLinea = replace(nuevaLinea, "tq", "tsh");
		nuevaLinea = replace(nuevaLinea, "v", "w");
		nuevaLinea = replace(nuevaLinea, "TH", "Th");
		nuevaLinea = replace(nuevaLinea, "kSH", "k+Sh");
		nuevaLinea = replace(nuevaLinea, "kaSH", "k+Sh");
		nuevaLinea = replace(nuevaLinea, "SH", "Sh");
		nuevaLinea = replace(nuevaLinea, ":", "H");
		nuevaLinea = replace(nuevaLinea, "NH", "NaH");
		nuevaLinea = replace(nuevaLinea, "dh", "d+h");
		nuevaLinea = replace(nuevaLinea, "gh", "g+h");
		nuevaLinea = replace(nuevaLinea, "bh", "b+h");
		nuevaLinea = replace(nuevaLinea, "dzh", "dz+h");
		nuevaLinea = replace(nuevaLinea, "aa", "a");
		nuevaLinea = replace(nuevaLinea, "ai", "i");
		nuevaLinea = replace(nuevaLinea, "aee", "ai");
		nuevaLinea = replace(nuevaLinea, "au", "u");
		nuevaLinea = replace(nuevaLinea, "aoo", "au");
		nuevaLinea = replace(nuevaLinea, "ae", "e");
		nuevaLinea = replace(nuevaLinea, "ao", "o");
		nuevaLinea = replace(nuevaLinea, "ee", "ai");
		nuevaLinea = replace(nuevaLinea, "oo", "au");
		nuevaLinea = replace(nuevaLinea, "\'I", "\'q");
		nuevaLinea = replace(nuevaLinea, "I", "-i");
		nuevaLinea = replace(nuevaLinea, "\'q", "-I");
		nuevaLinea = replace(nuevaLinea, "\\", "?");
		nuevaLinea = replace(nuevaLinea, "`", "!");
		nuevaLinea = replace(nuevaLinea, "ga-y", "g.y");
		nuevaLinea = replace(nuevaLinea, "g-y", "g.y");
		nuevaLinea = replace(nuevaLinea, "na-y", "n+y");

		len = nuevaLinea.length();
		for (i=0; i<len; i++)
		{
		    ch = nuevaLinea.charAt(i);
		    switch(ch)
		    {
		        case '#':
		          nuevaLinea = nuevaLinea.substring(0,i) + "@##" + nuevaLinea.substring(i+1);
		          i+=3;
		          len+=2;
		        break;
		        case '*':
		          nuevaLinea = nuevaLinea.substring(0,i) + "@#" + nuevaLinea.substring(i+1);
		          i+=2;
		          len++;
		        break;
		        case '\'':
		          if (i>0 && i<len-1)
		          {
		            chP = nuevaLinea.charAt(i-1);
		            chN = nuevaLinea.charAt(i+1);
		            if (isVowel(chN))
		            {
		                if (Character.isLetter(chP) && !isVowel(chP))
		                {
		                    nuevaLinea = nuevaLinea.substring(0, i) + Character.toUpperCase(chN) + nuevaLinea.substring(i+2);
		                    len--;
		                }
		                else if (chP=='a' && (i==1 || i>1 && !Character.isLetter(nuevaLinea.charAt(i-2)) || chN == 'a' && (i+2==len || !Character.isLetter(nuevaLinea.charAt(i+2)))))
		                {
		                    nuevaLinea = nuevaLinea.substring(0,i-1) + Character.toUpperCase(chN) + nuevaLinea.substring(i+2);
		                    len-=2;
		                }
		            }
		          }
		    }
		}
		
		open = false;
		for (i=0; i<len; i++)
		{
		    ch = nuevaLinea.charAt(i);
		    if (ch=='/')
		    {
		        if (open)
		        {
		          nuevaLinea = nuevaLinea.substring(0, i) + ")" + nuevaLinea.substring(i+1);
		          open = false;		            
		        }

		        else
		        {
		          nuevaLinea = nuevaLinea.substring(0, i) + "(" + nuevaLinea.substring(i+1);
		          open = true;
		        }
		    }
		}
		nuevaLinea = replace(nuevaLinea, ",", "/");
		
		return nuevaLinea; */
	}
	
	/** Converts from EWTS to the ACIP transliteration scheme. */
	public static String wylieToAcip(String wylie)
	{
		TibetanDocument tibDoc = new TibetanDocument();
    	try
    	{
    		TibTextUtils.insertTibetanMachineWebForTranslit(false, wylie, tibDoc, 0, false);
    	}
    	catch (InvalidTransliterationException e)
    	{
    		return null;
    	}
    	return tibDoc.getACIP(new boolean[] { false });
    	
		/* DLC FIXME: for unknown things, return null.
		if (wylie.equals("@##")) return "#";
		if (wylie.equals("@#")) return "*";
		if (wylie.equals("!")) return "`";
		if (wylie.equals("b+h")) return "BH";
		if (wylie.equals("d+h")) return "DH";
		if (wylie.equals("X")) return null;
                if (wylie.equals("iA")) return null;
                if (wylie.equals("ai")) return "EE";
                if (wylie.equals("au")) return "OO";
                if (wylie.equals("$")) return null;
		if (wylie.startsWith("@") || wylie.startsWith("#"))
			return null; // we can't convert this in isolation!  We need context.
		char []caract;
		int i, j, len;
		String nuevaPalabra;
		
		caract = wylie.toCharArray();
		len = wylie.length();
		for (j=0; j<len; j++)
		{
			i = j;
			/*ciclo:
			while(true) // para manejar excepciones; que honda!
			{
			switch(caract[i])
			{
			case 'A': 
			if (i>0)
			{
			i--;
			break;
			}
			default:
			if (Character.isLowerCase(caract[i]))
				caract[i] = Character.toUpperCase(caract[i]);
			else if (Character.isUpperCase(caract[i]))
				caract[i] = Character.toLowerCase(caract[i]);
			/*						break ciclo;
			}
			}
		}
		nuevaPalabra = new String(caract);
		//			nuevaPalabra = palabra.toUpperCase();
		
		// ahora hacer los cambios de Michael Roach
		
		nuevaPalabra = replace(nuevaPalabra, "TSH", "TQQ");
		nuevaPalabra = replace(nuevaPalabra, "TS", "TZ");
		nuevaPalabra = replace(nuevaPalabra, "TQQ", "TS");
		nuevaPalabra = replace(nuevaPalabra, "a", "'A");
		nuevaPalabra = replace(nuevaPalabra, "i", "'I");
		nuevaPalabra = replace(nuevaPalabra, "u", "'U");
		nuevaPalabra = replace(nuevaPalabra, "-I", "i");
		nuevaPalabra = replace(nuevaPalabra, "/", ",");
		nuevaPalabra = replace(nuevaPalabra, "_", " ");
		nuevaPalabra = replace(nuevaPalabra, "|", ";");
		nuevaPalabra = fixWazur(nuevaPalabra);
		return nuevaPalabra; */
	}	
    
	/** Converts Tibetan Unicode to EWTS. */
    public static String unicodeToWylie(String unicode)
    {
    	String machineWylie;
    	TibetanDocument tibDoc = new TibetanDocument();
    	StringBuffer errors = new StringBuffer();
    	
    	machineWylie = Converter.convertToEwtsForComputers(unicode, errors);
    	try
    	{
    		TibTextUtils.insertTibetanMachineWebForTranslit(true, machineWylie, tibDoc, 0, false);
    	}
    	catch (InvalidTransliterationException e)
    	{
    		return null;
    	}
    	return tibDoc.getWylie(new boolean[] { false });   	
    }
    
    /** Converts EWTS to Tibetan Unicode. */
    public static String wylieToUnicode(String wylie)
    {
    	return EwtsToUnicodeForXslt.convertEwtsTo(wylie);
    }
    
    /** Converts EWTS to Tibetan Unicode represented in NCR. */
    public static String wylieToHTMLUnicode(String wylie)
    {
    	return Manipulate.UnicodeString2NCR(wylieToUnicode(wylie));
    }
    
	/** Converts Tibetan Unicode represented in NCR to EWTS. */
    public static String HTMLUnicodeToWylie(String unicode)
    {
    	return unicodeToWylie(Manipulate.NCR2UnicodeString(unicode));
    }
}