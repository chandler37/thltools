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

import java.io.*;
import org.thdl.util.*;

/** Miscelaneous static methods for the manipulation of Tibetan text.
	
    @author Andr&eacute;s Montano Pellegrini
*/

public class Manipulate
{

	public static String[] parseFields (String s, char delimiter)
	{
	    int pos;
	    String field;
	    SimplifiedLinkedList ll = new SimplifiedLinkedList();
	    
	    while ((pos = s.indexOf(delimiter))>=0)
	    {
	        field = s.substring(0, pos).trim();
	        ll.addLast(field);
	        s = s.substring(pos+1);
	    }
	    
	    ll.addLast(s.trim());
	    return ll.toStringArray();
	}    
	
	public static String replace(String linea, String origSub, String newSub)
	{
		int pos, lenOrig = origSub.length();
      while ((pos = linea.indexOf(origSub))!=-1)
		{
			linea = linea.substring(0, pos).concat(newSub).concat(linea.substring(pos+lenOrig));
		}
		return linea;
	}
	
	public static boolean isVowel (char ch)
	{
	    ch = Character.toLowerCase(ch);
	    return ch=='a' || ch=='e' || ch=='i' || ch=='o' || ch=='u';
	}
	
	public static String wylieToAcip(String palabra)
	{
		// DLC FIXME: for unknown things, return null.
		if (palabra.equals("@#")) return "#";
		if (palabra.equals("!")) return "`";
		if (palabra.equals("b+h")) return "BH";
		if (palabra.equals("d+h")) return "DH";
		if (palabra.equals("X")) return null;
                if (palabra.equals("iA")) return null;
                if (palabra.equals("ai")) return "EE";
                if (palabra.equals("au")) return "OO";
                if (palabra.equals("$")) return null;
		if (palabra.startsWith("@") || palabra.startsWith("#"))
			return null; // we can't convert this in isolation!  We need context.
		char []caract;
		int i, j, len;
		String nuevaPalabra;
		
		caract = palabra.toCharArray();
		len = palabra.length();
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
			default:*/
			if (Character.isLowerCase(caract[i]))
				caract[i] = Character.toUpperCase(caract[i]);
			else if (Character.isUpperCase(caract[i]))
				caract[i] = Character.toLowerCase(caract[i]);
			/*						break ciclo;
			}
			}*/
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
		nuevaPalabra = fixWazur(nuevaPalabra);
		return nuevaPalabra;
	}
	
	public static String acipToWylie(String linea)
	{
		char caract[], ch, chP, chN;
		String nuevaLinea;
		int i, len;
		boolean open;
		
		caract = linea.toCharArray();
		len = linea.length();
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
		/-/ -> (-), ga-y -> g.y, g-y -> g.y, na-y -> n+y */
		
		nuevaLinea = replace(nuevaLinea, "ts", "tq");
		nuevaLinea = replace(nuevaLinea, "tz", "ts");
		nuevaLinea = replace(nuevaLinea, "tq", "tsh");
		nuevaLinea = replace(nuevaLinea, "v", "w");
		nuevaLinea = replace(nuevaLinea, "TH", "Th");
		nuevaLinea = replace(nuevaLinea, "kSH", "k+Sh");
		nuevaLinea = replace(nuevaLinea, "kaSH", "k+Sh");
		nuevaLinea = replace(nuevaLinea, "SH", "Sh");
		nuevaLinea = replace(nuevaLinea, ":", "H");
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
		            if (Character.isLetter(chP) && !isVowel(chP) && isVowel(chN))
		            {
		                nuevaLinea = nuevaLinea.substring(0, i) + Character.toUpperCase(chN) + nuevaLinea.substring(i+2);
		                len--;
		            }
		          }
		        break;
		        case 'a':
		          if ((i<len-3 && nuevaLinea.charAt(i+1)=='\'' && isVowel(nuevaLinea.charAt(i+2))) && (i==0 || !Character.isLetter(nuevaLinea.charAt(i-1))))
		          {
		            nuevaLinea = nuevaLinea.substring(0, i) + Character.toUpperCase(nuevaLinea.charAt(i+2)) + nuevaLinea.substring(i+3);
		            len-=2;
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
		
		return nuevaLinea;
	}
	
	public static String fixWazur(String linea)
	{
		int i;

		for (i=1; i<linea.length(); i++)
		{
			if (linea.charAt(i)=='W')
			{
				if (Character.isLetter(linea.charAt(i-1)))
					linea = linea.substring(0,i) + 'V' + linea.substring(i+1);					
			}
		}
		return linea;
	}
	
	/** Returns the base letter of a syllable. Does not include the vowel!
	Ignoring cases for now. */
	public static String getBaseLetter (String sil)
	{
	    sil = sil.toLowerCase();
	    
	    int i=0;
	    char ch, ch2;
	    
	    while (!isVowel(sil.charAt(i))) i++;
	    if (i==0) return "";
	    
	    i--;
	    if (i==-1) return "";
	    
	    if (sil.charAt(i)=='-') i--;
	    
	    ch = sil.charAt(i);
	    
	    // check to see if it is a subscript (y, r, l, w)
	    if (i>0)
	    {
	        switch (ch)
	        {
	            case 'r': case 'l': case 'w': i--;
	            break;
	            case 'y':
    	            ch2 = sil.charAt(i-1);
    	            switch (ch2)
    	            {
    	                case '.': return "y";
    	                case 'n': return "ny";
    	                default: i--;
    	            }
	        }
	    }
	    if (i==0) return sil.substring(i,i+1);
	    ch = sil.charAt(i);
	    ch2 = sil.charAt(i-1);
	    
	    switch(ch)
	    {
	        case 'h':
                switch (ch2)
       	        {
                    case 'k': case 'c': case 't': case 'p': case 'z':
                        return sil.substring(i-1,i+1);
                    case 's':
   	                    if (i-2>=0 && sil.charAt(i-2)=='t') return "tsh";
       	                else return "sh";
                    default: return "h";
                }
	        case 's':
	            if (ch2=='t') return "ts";
	            else return "s";
	        case 'g':
	            if (ch2=='n') return "ng";
	            else return "g";
	        case 'z':
	            if (ch2=='d') return "dz";
	            else return "z";
	    }
	    return sil.substring(i,i+1);
	}
	
	
	/** Syntax: java Manipulate [word-file] < source-dic-entries > dest-dic-entries
	
    Takes the output of ConsoleScannerFilter
	(in RY format), converts the Wylie to Acip
	and displays the result in csv format.	 
	arch-palabras es usado solo cuando deseamos las palabras cambiadas
	a otro archivo.
	
	
	public static void main (String[] args) throws Exception												   
	{
		String linea, palabra, definicion, nuevaPalabra;
		int marker;
		PrintWriter psPalabras = null;
		
		BufferedReader keyb = new BufferedReader(new InputStreamReader(System.in));
		
		if (args.length==1)
			psPalabras = new PrintWriter(new FileOutputStream(args[0]));		
		
		while ((linea=keyb.readLine())!=null)
		{
			if (linea.trim().equals("")) continue;
			marker = linea.indexOf('-');
			if (marker<0) // linea tiene error
			{
				palabra = linea;
				definicion = "";
			}
			else
			{
				palabra = linea.substring(0, marker).trim();
				definicion = linea.substring(marker+1).trim();
			}
			
			nuevaPalabra = wylieToAcip(palabra);
			
			if (psPalabras!=null)
				psPalabras.println(nuevaPalabra);
			else System.out.print(nuevaPalabra + '\t');
			if (definicion.equals(""))
				System.out.println(palabra);
			else
				System.out.println(palabra + '\t' + definicion);
		}
      if (psPalabras!=null) psPalabras.flush();
	}*/	
}
