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

package org.thdl.util;

import java.io.PrintStream;
import java.io.FileOutputStream;

import org.thdl.util.TeeStream;

/**
 * This uninstantiable class provides assertions and the like in a
 * JVM-version-independent fashion.
 * @author David Chandler 
 */
public class ThdlDebug {
    /* FIXME: make this configurable. */
    static final String contactMsg
        = "  Please visit http://thdltools.sf.net/ or contact thdltools-devel@lists.sourceforge.net and give us a bug report so that we can improve the quality of this software.";

    /** Do not instantiate this class. */
    private ThdlDebug() { }

    /** Throws an unchecked exception if condition is not true.  Note that
     * unlike a real assertion, this happens always.  We can certainly
     * use AspectJ (with which I, DC, am intimately familiar) to avoid
     * the overhead of such things in release builds if performance
     * becomes a real issue.  */
    public static void verify(boolean condition) {
        verify(null, condition);
    }

    /** Throws an unchecked exception if condition is not true.  The
     *  exception's message will include the string msg if msg is not
     *  null.  Note that unlike a real assertion, this happens always.
     *  We can certainly use AspectJ (with which I, DC, am intimately
     *  familiar) to avoid the overhead of such things in release
     *  builds if performance becomes a real issue.
     *
     *  Throws a THDL-specific exception so that you can catch these
     *  specially in case you want to ignore them.
     *
     *  @throws ThdlLazyException if condition is not true */
    public static void verify(String msg, boolean condition)
        throws ThdlLazyException
    {
        if (!condition) {
            throw new ThdlLazyException(new Error(((msg == null)
                                                   ? "THDL Tools sanity check: "
                                                   : msg)
                                                  + "An assertion failed.  This means that there is a bug in this software."
                                                  + contactMsg));
        }
    }

    /** Call this from control-flow paths that are not well thought
     *  out.  For example, if you have to catch an IOException, but
     *  you're fairly certain that it'll never be thrown, call this
     *  function if it is indeed thrown.  Developers can set the
     *  THDL_DIE_EAGERLY property to true (using <code>'java
     *  -DTHDL_DIE_ON_IFFY_CODE=true'</code>) in order to test the
     *  code's robustness.
     *
     *  Throws a THDL-specific exception so that you can catch these
     *  specially in case you want to ignore them.
     *
     *  @throws ThdlLazyException if the THDL_DIE_ON_IFFY_CODE system
     *  property is set to "true" */
    public static void noteIffyCode()
        throws ThdlLazyException
    {

		/* FIXME: find all calls to this function and rethink or shore
           up the calling code. */

        if (Boolean.getBoolean("THDL_DIE_ON_IFFY_CODE"))
            throw new ThdlLazyException(new Error("You've reached some iffy code, some code that's not well thought-out.  Because you invoked the Java runtime environment with the property THDL_DIE_ON_IFFY_CODE set to true (developers: use 'ant -Dthdl.die.on.iffy=false' to prevent this), the program is now aborting."));
    }

	/** Exits the program with a message that the CLASSPATH is not set
        properly. */
	public static void handleClasspathError(String whoseWhat, Throwable error) {
		System.err.println(((whoseWhat == null) ? "Your CLASSPATH" : whoseWhat)
						   + " is not set properly.");

        /* FIXME */
        System.err.println("Note that Savant and QuillDriver CANNOT be invoked via the");
        System.err.println("'java -jar Savant-xyz.jar' option, because that silently ignores");
        System.err.println("the CLASSPATH.  This means that double-clicking them won't work");
        System.err.println("either, because we don't set the JARs' manifest files to contain");
        System.err.println("Class-path attributes.  See installation instructions."); /* FIXME: we don't HAVE installation instructions, do we? */
        System.err.println("");
		System.err.println("Details: Missing class: "
                           + ((error == null)
                              ? "unknown!" : error.getMessage()));
		if (Boolean.getBoolean("THDL_DEBUG")) {
			System.err.println("Details: Stack trace: "
                               + ((error == null)
                                  ? "unknown!" : error.getMessage()));
			error.printStackTrace(System.err);
		}
		System.exit(1);
	}

	/** Sets it up so that a call to System.out or System.err prints
     *  to standard output/error but ALSO prints to the log file named
     *  logFile. */
	public static void attemptToSetUpLogFile(String logFile) {
		try {
			PrintStream logFilePrintStream
				= new PrintStream(new FileOutputStream(logFile));
			PrintStream psOut = new TeeStream(System.out, logFilePrintStream);
			PrintStream psErr = new TeeStream(System.err, logFilePrintStream);
			System.setErr(psErr);
			System.setOut(psOut);
		} catch (Exception e) {
			/* don't let this stop us. */
			noteIffyCode();
		}
	}
};
