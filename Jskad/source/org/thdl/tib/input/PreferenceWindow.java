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

package org.thdl.tib.input;

import javax.swing.*;
import java.awt.*;

/** Shows a standard dialog window to set the preferences
    for the tibetan and roman script used
*/
public class PreferenceWindow
{
    private JDialog dialog;
	private JComboBox tibetanFontSizes;
	private JComboBox romanFontSizes;
	private JComboBox romanFontFamilies;
	private DuffPane dp;
    
    public PreferenceWindow(Component parent, DuffPane dp)
    {
        this.dp = dp;
   		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNames = genv.getAvailableFontFamilyNames();

		JPanel tibetanPanel;

		tibetanPanel = new JPanel();
		tibetanPanel.setBorder(BorderFactory.createTitledBorder("Set Tibetan Font Size"));
		tibetanFontSizes = new JComboBox(new String[] {"8","10","12","14","16","18","20","22","24","26","28","30","32","34","36","48","72"});
		tibetanFontSizes.setMaximumSize(tibetanFontSizes.getPreferredSize());
		tibetanFontSizes.setSelectedItem(String.valueOf(dp.getTibetanFontSize()));
		tibetanFontSizes.setEditable(true);
		tibetanPanel.add(tibetanFontSizes);

		JPanel romanPanel;

		romanPanel = new JPanel();
		romanPanel.setBorder(BorderFactory.createTitledBorder("Set non-Tibetan Font and Size"));
		romanFontFamilies = new JComboBox(fontNames);
		romanFontFamilies.setMaximumSize(romanFontFamilies.getPreferredSize());
		romanFontFamilies.setSelectedItem(dp.getRomanFontFamily());
		romanFontFamilies.setEditable(true);
		romanFontSizes = new JComboBox(new String[] {"8","10","12","14","16","18","20","22","24","26","28","30","32","34","36","48","72"});
		romanFontSizes.setMaximumSize(romanFontSizes.getPreferredSize());
		romanFontSizes.setSelectedItem(String.valueOf(dp.getRomanFontSize()));
		romanFontSizes.setEditable(true);
		romanPanel.setLayout(new GridLayout(1,2));
		romanPanel.add(romanFontFamilies);
		romanPanel.add(romanFontSizes);

		JPanel preferencesPanel = new JPanel();
		preferencesPanel.setLayout(new GridLayout(2,1));
		preferencesPanel.add(tibetanPanel);
		preferencesPanel.add(romanPanel);

		JOptionPane pane = new JOptionPane(preferencesPanel);
		dialog = pane.createDialog(parent, "Preferences");
    }
    
    private static int stringToInt(String s)
    {
        int num;
 		try 
 		{
			num = Integer.parseInt(s);
		}
		catch (NumberFormatException ne) {
            num = -1;
		}
		return num;
    }
    
    public int getTibetanFontSize()
    {
		return stringToInt(tibetanFontSizes.getSelectedItem().toString());
    }
    
    public int getRomanFontSize()
    {
        return stringToInt(romanFontSizes.getSelectedItem().toString());
    }
    
    public String getRomanFont()
    {
        return romanFontFamilies.getSelectedItem().toString();
    }

    /** This returns only when the user has closed the dialog */
    public void show()
    {
		int size;
        String font;
        
   		dialog.show();
   		
		size = getTibetanFontSize();
		if (size>0) dp.setByUserTibetanFontSize(size);
		
		size = getRomanFontSize();
		if (size==-1) size = dp.getRomanFontSize();
		font = getRomanFont();
		dp.setByUserRomanAttributeSet(font, size);   		
    }
}