package org.thdl.roster;

import java.security.MessageDigest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class TokenMaker
{
	public static String make() throws java.security.NoSuchAlgorithmException
	{
		long systime = System.currentTimeMillis();
		byte[] time = new Long(systime).toString().getBytes();

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(time);
		return  toHex( md5.digest() );
		//System.err.println("Unable to calculate MD5 Digests.\nCould not create unique token");
	}
	public static String toHex(byte[] digest)
	{
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < digest.length; i++)
			buf.append( Integer.toHexString( (int)digest[i] & 0x00ff ) ); //param=BITWISE operation
		return buf.toString();
	}
}
