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

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.*;

public class SimpleSpinner extends JPanel {
	JTextField textSpinner = null;
	Object jSpinner = null;
	Class[] getSetParameterTypes;
	Method getMethod, setMethod;

//add SpinnerNumberModel so that player.getEndTime() is max, and 0 min

	public SimpleSpinner() {
		jSpinner = SimpleSpinner.createObject("javax.swing.JSpinner");
		if (jSpinner == null) {
			textSpinner = new JTextField("0");
			setLayout(new BorderLayout());
			add("Center", textSpinner);
		} else {
			getSetParameterTypes = new Class[] {Object.class};
			setLayout(new BorderLayout());
			add("Center", (JComponent)jSpinner);
		}
	}
	public void setValue(Integer num) {
		if (jSpinner == null) {
			textSpinner.setText(num.toString());
		} else { //must be JSpinner
			Object[] argument = new Object[] {num};
			try {
				setMethod = jSpinner.getClass().getMethod("setValue", getSetParameterTypes);
				setMethod.invoke(jSpinner, argument);
			} catch (NoSuchMethodException nsme) {
				nsme.printStackTrace();
				ThdlDebug.noteIffyCode();
			} catch (IllegalAccessException illae) {
				illae.printStackTrace();
				ThdlDebug.noteIffyCode();
			} catch (InvocationTargetException ite) {
				ite.printStackTrace();
				ThdlDebug.noteIffyCode();
			}
		}
	}
	public Integer getValue() {
		if (jSpinner == null) {
			try {
				return new Integer(textSpinner.getText());
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
				ThdlDebug.noteIffyCode();
			}
		} else { //must be JSpinner
			try {
				setMethod = jSpinner.getClass().getMethod("getValue", null);
				return (Integer)setMethod.invoke(jSpinner, null);
			} catch (NoSuchMethodException nsme) {
				nsme.printStackTrace();
				ThdlDebug.noteIffyCode();
			} catch (IllegalAccessException illae) {
				illae.printStackTrace();
				ThdlDebug.noteIffyCode();
			} catch (InvocationTargetException ite) {
				ite.printStackTrace();
				ThdlDebug.noteIffyCode();
			}
		}
		return null;
	}

   static Object createObject(String className) {
      Object object = null;
      try {
          Class classDefinition = Class.forName(className);
          object = classDefinition.newInstance();
      } catch (InstantiationException e) {
          System.out.println(e);
      } catch (IllegalAccessException e) {
          System.out.println(e);
      } catch (ClassNotFoundException e) {
          System.out.println(e);
      }
      return object;
   }
}

/* originally slated for JdkVersionHacks by dlc

	public static Object getJSpinner(Dimension dim,
									  Object spinnerValue,
									  int value, int minimum,
									  int maximum, int stepSize) {
		In Java 1.4, do the following:

		   SpinnerNumberModel snm1
		     = new SpinnerNumberModel(value, minimum, maximum, stepSize);
           JSpinner spinner = new JSpinner(snm1);
           spinner.setPreferredSize(dim);
           spinner.setValue(spinnerValue);
           return spinner;

		

		FIXME;
		return null;
	}
*/
