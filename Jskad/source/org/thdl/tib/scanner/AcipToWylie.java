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

import java.net.*;
import java.io.*;

public class AcipToWylie
{
	private BufferedReader in;
	private PrintWriter out;

	public AcipToWylie(BufferedReader in, PrintWriter out)
	{
		this.in = in;
		this.out = out;
	}
	
	public static void main (String[] args) throws Exception
	{
		PrintWriter out;
		BufferedReader in=null;
		boolean file=false;
		
		switch (args.length)
		{
		case 0: out = new PrintWriter(System.out);
				in = new BufferedReader(new InputStreamReader(System.in));
				break;
		case 1: out = new PrintWriter(System.out);
				file = true;
				break;
		default: out = new PrintWriter(new FileOutputStream(args[1]));
				 file = true;		
		}

		if (file)
		{
			if (args[0].indexOf("http://") >= 0) 
				in = new BufferedReader(new InputStreamReader(new BufferedInputStream((new URL(args[0])).openStream())));
			else 
				in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));
		}
		
		new AcipToWylie(in, out).run();
	}
	
	public void run() throws Exception
	{
		String linea;
		while ((linea=in.readLine())!=null)
		{
			out.println(Manipulate.acipToWylie(linea));
		}
		out.flush();
	}
}
