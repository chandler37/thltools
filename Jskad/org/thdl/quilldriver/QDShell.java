package org.thdl.quilldriver;

import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.text.rtf.*;

public class QDShell extends JFrame {
	ResourceBundle messages = null;
	QD qd = null;

public static void main(String[] args) {
		try {
			PrintStream ps = new PrintStream(new FileOutputStream("qd.log"));
			System.setErr(ps);
			System.setOut(ps);
		}
		catch (Exception e) {
		}

	Locale locale;

	if (args.length == 2) {
		locale = new Locale(new String(args[0]), new String(args[1]));
		Locale[] locales = Locale.getAvailableLocales();
		for (int k=0; k<locales.length; k++)
			if (locales[k].equals(locale)) {
				JComponent.setDefaultLocale(locale);
				break;
			}
	}
	else
		locale = Locale.getDefault();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
		}

	QDShell qdsh = new QDShell(locale);
	qdsh.setVisible(true);
}

	public QDShell(Locale locale) {
		setTitle("QuillDriver");

/*
		// Code for Merlin
		if (getToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH))
		{
			setLocation(0,0);
			setSize(getToolkit().getScreenSize().width,getToolkit().getScreenSize().height);
			setVisible(true);
			setExtendedState(Frame.MAXIMIZED_BOTH);
		} else {
*/
			Dimension gs = getToolkit().getScreenSize();
			setLocation(0,0);
			setSize(new Dimension(gs.width, gs.height));
			setVisible(true);

//		}

		messages = ResourceBundle.getBundle("MessageBundle", locale);

		setJMenuBar(getQDShellMenu());
		qd = new QD(messages);
		getContentPane().add(qd);
		addWindowListener(new WindowAdapter () {
			public void windowClosing (WindowEvent e) {
				qd.getSave();
//should only system exit if no cancel!!
				System.exit(0);
			}
		});
	}

	public JMenuBar getQDShellMenu() {
		JMenu projectMenu = new JMenu(messages.getString("Project"));
		JMenuItem newItem = new JMenuItem(messages.getString("New"));
		JMenuItem openItem = new JMenuItem(messages.getString("Open"));
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				qd.getOpen();
			}
		});
		JMenuItem closeItem = new JMenuItem(messages.getString("Close"));
		JMenuItem saveItem = new JMenuItem(messages.getString("Save"));
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				qd.getSave();
			}
		});
		JMenuItem quitItem = new JMenuItem(messages.getString("Quit"));
		quitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				qd.getSave();
//should only system exit if no cancel!!
				System.exit(0);
			}
		});
		projectMenu.add(newItem);
		projectMenu.add(openItem);
		projectMenu.add(closeItem);
		projectMenu.addSeparator();
		projectMenu.add(saveItem);
		projectMenu.addSeparator();
		projectMenu.add(quitItem);

	//Tibetan-specific value: remove in non-Tibetan version
	//non-Tibetan specific version would have Transcription Language option here instead
		JRadioButton wylieButton = new JRadioButton("THDL Extended Wylie");
		JRadioButton sambhotaButton = new JRadioButton("Sambhota Keymap One");
		JRadioButton tcc1Button = new JRadioButton("TCC Keyboard 1");
		JRadioButton tcc2Button = new JRadioButton("TCC Keyboard 2");
		wylieButton.setActionCommand("wylie");
		sambhotaButton.setActionCommand("sambhota1");
		tcc1Button.setActionCommand("tcc1");
		tcc2Button.setActionCommand("tcc2");
		RadioListener l = new RadioListener();
		wylieButton.addActionListener(l);
		sambhotaButton.addActionListener(l);
		tcc1Button.addActionListener(l);
		tcc2Button.addActionListener(l);
		wylieButton.setSelected(true);
		ButtonGroup bg = new ButtonGroup();
		bg.add(wylieButton);
		bg.add(sambhotaButton);
		bg.add(tcc1Button);
		bg.add(tcc2Button);
		JPanel buttons = new JPanel(new GridLayout(0,1));
		buttons.add(wylieButton);
		buttons.add(sambhotaButton);
		buttons.add(tcc1Button);
		buttons.add(tcc2Button);
		JMenu keyboardMenu = new JMenu(messages.getString("Keyboard"));
		keyboardMenu.add(buttons);

		JMenu preferencesMenu = new JMenu(messages.getString("Preferences"));
		preferencesMenu.add(keyboardMenu);

		JMenuBar bar = new JMenuBar();
		bar.add(projectMenu);
		bar.add(preferencesMenu);
		return bar;
	}

class RadioListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		qd.changeKeyboard(e);
	}
}

}