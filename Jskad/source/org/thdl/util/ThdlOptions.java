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

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.thdl.util.ThdlLazyException;

/**
 * Provides a clean interface to the multi-tiered system of user
 * preferences (also known as options).
 *
 * <p>The {@link java.util.Properties java.util.Properties} class
 * makes it easy for us to provide a hierarchical preferences (that
 * is, options) structure.  One option a user might wish to set is the
 * font size for Tibetan, for example. The highest precedence goes to
 * a pref that the user set on the Java command line using something
 * like <code>java -Dpref=value -jar jskad_or_whatever.jar</code>.  If
 * the user went to the trouble of doing this, we definitely want to
 * respect his or her wish.  The next highest precedence goes to an
 * individual user's preferences file (a file readable and writable by
 * {@link java.util.Properties java.util.Properties}, but also
 * hand-editable), if one exists.  Next is the system-wide preferences
 * file, if one exists.  Finally, we fall back on the preferences file
 * shipped with the application inside the JAR.</p>
 *
 * <p>ThdlOptions is not instantiable.  It contains only static
 * methods for answering questions about the user's preferences.</p>
 * 
 * <p>There are three kinds of preferences: boolean-valued preferences
 * ("true" or "false"), integer-valued preferences, and string-valued
 * preferences.  Boolean-valued preferences should, by convention, be
 * false by default.  If you want to enable feature foo by default,
 * but give a preference for disabling it, then call the preference
 * "DisableFeatureFoo".  If you want to disable feature foo by
 * default, call it "EnableFeatureFoo".  This makes the users' lives
 * easier.</p>
 * 
 * <p>ThdlOptions is a final class so that compilers can make this
 * code run efficiently.</p>
 *
 * @author David Chandler
 */
public final class ThdlOptions {
	/**
	 * So that you're not tempted to instantiate this class, the
	 * constructor is private: */
	private ThdlOptions() {
        // don't instantiate this class.
	}

	/**
	 * Returns the value of a boolean-valued option, or false if that
	 * option is set nowhere in the hierarchy of properties files.
	 * 
	 * @param optionName the option whose value you wish to retrieve
	 * (note the naming conventions detailed in the class comment)
     */
	public static boolean getBooleanOption(String optionName)
    {
        init();

        // Look to the System first.
        String answer = getSystemValue(optionName);
        if (answer == null) {
            answer = userProperties.getProperty(optionName, "false");
        }
        return answer.equalsIgnoreCase("true");
    }

    // FIXMEDOC
    private static String getSystemValue(String optionName) {
        // Look to the System first.
        String answer = null;
        try {
            answer = System.getProperty(optionName);
        } catch (SecurityException e) {
            if (!suppressErrs())
                throw e;
        }
        return answer;
    }

	/**
	 * Returns the value of a string-valued option, or null if that
	 * option is set nowhere in the hierarchy of properties files.
	 * 
	 * @param optionName the option whose value you wish to retrieve
	 * (note the naming conventions detailed in the class comment) */
	public static String getStringOption(String optionName)
    {
        init();
        // Look to the System first.
        String answer = getSystemValue(optionName);
        if (answer == null) {
            answer = userProperties.getProperty(optionName, null);
        }
        return answer;
    }

	/**
	 * Returns the value of a string-valued option, or defaultValue if that
	 * option is set nowhere in the hierarchy of properties files.
	 * 
	 * @param optionName the option whose value you wish to retrieve
	 * (note the naming conventions detailed in the class comment)
     * @param defaultValue the default value */
	public static String getStringOption(String optionName,
                                         String defaultValue)
    {
        String x = getStringOption(optionName);
        if (null == x)
            return defaultValue;
        else
            return x;
    }

	/**
	 * Returns the value of an integer-valued option, or defaultValue
	 * if that option is set nowhere in the hierarchy of properties
	 * files or is set to something that cannot be decoded to an
	 * integer.
	 * 
	 * @param optionName the option whose value you wish to retrieve
	 * (note the naming conventions detailed in the class comment)
     * @param defaultValue the default value */
	public static int getIntegerOption(String optionName, int defaultValue)
    {
        Integer x = getIntegerOption(optionName);
        if (null == x)
            return defaultValue;
        else
            return x.intValue();
    }

	/**
	 * Returns the value of an integer-valued option, or null if that
	 * option is set nowhere in the hierarchy of properties files or
	 * is set to something that cannot be decoded to an integer.
	 * 
	 * @param optionName the option whose value you wish to retrieve
	 * (note the naming conventions detailed in the class comment) */
	public static Integer getIntegerOption(String optionName)
    {
        init();

        // Look to the System first.
        String answer = getSystemValue(optionName);
        if (answer == null) {
            answer = userProperties.getProperty(optionName, null);
        }
        if (null == answer) {
            return null;
        } else {
            try {
                return Integer.decode(answer);
            } catch (NumberFormatException e) {
                if (getBooleanOption("thdl.debug"))
                    throw new ThdlLazyException(e);
                else
                    return null;
            }
        }
    }

    private static boolean suppressErrs() {
        return false; /* FIXME--make THIS configurable. */
    }
    private static void init() {
        try {
            initialize(ThdlOptions.class, "/options.txt",
                       suppressErrs());
        } catch (FileNotFoundException e) {
            if (!suppressErrs())
                throw new ThdlLazyException(e);
        }
    }

    /** the properties object that is chained to the system-wide
        properties which is chained to the built-in properties which
        is chained to the Java System properties */
    private static Properties userProperties = null;

    /** to avoid initializing twice */
    private static boolean isInitialized = false;

    /** Sets userProperties so that it represents the entire, chained
        preferences hierarchy.

        @param resourceHolder the class associated with the builtin
        defaults properties file resource
        @param resourceName the name of the builtin defaults
        properties file resource
        @param suppressErrors true if the show must go on, false if
        you want unchecked exceptions thrown when bad things happen

        @throws FileNotFoundException if !suppressErrors and if the
        user gave the location of the system-wide or user preferences
        files, but gave it incorrectly. */
    private static void initialize(Class resourceHolder,
                                   String resourceName,
                                   boolean suppressErrors)
        throws FileNotFoundException
    {
        if (isInitialized)
            return;
        isInitialized = true;
        try {

            // Get the application's built-in, default properties.
            Properties defaultProperties
                = getPropertiesFromResource(resourceHolder,
                                            resourceName,
                                            suppressErrors,
                                            new Properties() /* empty */);

            // Get the system's properties, if the system administrator
            // has created any:
            Properties systemWideProperties
                = tryToGetPropsFromFile("thdl.system.wide.options.file",
                                        // FIXME this default is
                                        // system-dependent:
                                        "C:\\thdl_opt.txt",
                                        defaultProperties,
                                        suppressErrors);

            // Get the user's properties, if they've set any:
            userProperties
                = tryToGetPropsFromFile("thdl.user.options.file",
                                        // FIXME this default is
                                        // system-dependent:
                                        "C:\\thdl_uopt.txt",
                                        systemWideProperties,
                                        suppressErrors);
        } catch (SecurityException e) {
            if (suppressErrors) {
                if (userProperties == null) {
                    userProperties = new Properties(); // empty
                } // else leave it as is.
            } else {
                throw new ThdlLazyException(e);
            }
        }
    }

    /** Returns a new, nonnull Properties object if (1) a preferences
        file is found at the location specified by the value of the
        System property pName, if it is set, or at defaultLoc, if
        pName is not set, and (2) if that file is successfully read
        in.  Otherwise, this returns defaultProps.

        @param pName the name of the System property that overrides
        this application's default idea of where to look for the file
        @param defaultLoc the default preferences file name
        @param suppressErrors true iff you want to proceed without
        throwing exceptions whenever possible
        
        @throws FileNotFoundException if !suppressErrors and if the
        user gave the location of the system-wide or user preferences
        files, but gave it incorrectly.  @throws SecurityException if
        playing with files or system properties is not OK */
    private static Properties tryToGetPropsFromFile(String pName,
                                                    String defaultLoc,
                                                    Properties defaultProps,
                                                    boolean suppressErrors)
        throws FileNotFoundException, SecurityException
    {
        Properties props = defaultProps;
        String systemPropFileName = System.getProperty(pName, defaultLoc);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(systemPropFileName);
        } catch (FileNotFoundException e) {
            if (null != System.getProperty(pName)) {
                // the user explicitly set this, but not
                // correctly.
                if (!suppressErrors)
                    throw e;
            } else {
                // definitely suppress this.  On a Mac or
                // Unix/Linux box, this'll happen every time
                // at present.  (FIXME)
            }
        }
                
        if (fis != null) {
            props = getPropertiesFromStream(fis,
                                            suppressErrors,
                                            defaultProps);
        }
        return props;
    }


    // FIXMEDOC
    private static Properties getPropertiesFromResource(Class resourceHolder,
                                                        String resourceName,
                                                        boolean suppressErrors,
                                                        Properties defaults)
    {
        InputStream in = resourceHolder.getResourceAsStream(resourceName);
        return getPropertiesFromStream(in, suppressErrors, defaults);
    }

    // FIXMEDOC
    private static Properties getPropertiesFromStream(InputStream in,
                                                      boolean suppressErrors,
                                                      Properties defaults)
        throws NullPointerException
    {
        Properties options;
        if (defaults == null)
            options = new Properties(); // empty properties list
        else
            options = new Properties(defaults);
        try {
            // Load props from the resource:
            options.load(in);
            return options;
        } catch (Exception e) {
            // e is an IOException or a SecurityException or, if the
            // resource was not found, a NullPointerException.
            if (suppressErrors) {
                return options;
            } else {
                throw new ThdlLazyException(e);
            }
        }
    }
}



