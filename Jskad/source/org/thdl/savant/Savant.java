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

package org.thdl.savant;

import java.awt.Font;
import java.awt.Label;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import java.io.CharArrayWriter;
import java.io.CharArrayReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.net.URL;
import java.net.MalformedURLException;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;

import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlActionListener;

public class Savant extends JDesktopPane
{
	protected SoundPanel sp = null;
	protected TwoWayTextPlayer tp = null;

	protected JInternalFrame videoFrame = null;
	protected JInternalFrame textFrame = null;
	protected JInternalFrame vocabFrame = null;

	protected JFrame fullScreen = null;
	protected boolean isFullScreen = false;
	protected Dimension fullScreenSize = null;

	protected JPanel videoPanel = null;
	protected JPanel textPanel = null;
	protected JScrollPane vocabPanel = null;

	protected URL extras = null;

	protected int orientation = 0;
	public final int TOP_TO_BOTTOM = 1;
	public final int LEFT_TO_RIGHT = 2;

	public Savant()
	{
		setBackground(new JFrame().getBackground());
		setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		setLayout(new BorderLayout());
		String labelString = "Please wait while Savant loads this transcript and media.";
		JLabel label = new JLabel(labelString, SwingConstants.CENTER);
		label.setFont(new Font("Serif", Font.PLAIN, 14));
		add("Center", label);

		addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent ce)
			{
				switch (orientation) {
					case TOP_TO_BOTTOM:
						videoFrame.setLocation(getSize().width/2 - videoFrame.getSize().width/2, 0);
						textFrame.setLocation(0, videoFrame.getSize().height);
						if (vocabFrame != null)
						{
							textFrame.setSize(getSize().width / 2, getSize().height - videoFrame.getSize().height);
							vocabFrame.setLocation(textFrame.getSize().width, videoFrame.getSize().height);
							vocabFrame.setSize(getSize().width - textFrame.getSize().width, getSize().height - videoFrame.getSize().height);
						} else
							textFrame.setSize(getSize().width, getSize().height - videoFrame.getSize().height);
						break;

					case LEFT_TO_RIGHT:
						videoFrame.setLocation(0,0);
						textFrame.setLocation(videoFrame.getSize().width, 0);
						textFrame.setSize(getSize().width - videoFrame.getSize().width, getSize().height);
						if (vocabFrame != null)
						{
							vocabFrame.setLocation(0, videoFrame.getSize().height);
							vocabFrame.setSize(videoFrame.getSize().width, getSize().height - videoFrame.getSize().height);
						}
						break;

					default:
						break;
				}
			}
		});
	}

	public void close()
	{
		if (sp != null)
			sp.destroy();
	}

	public void open(TranscriptView[] views, String video, String vocabulary)
	{
		try {
			if (vocabulary == null)
				open(views, new URL(video), null);
			else
				open(views, new URL(video), new URL(vocabulary));
		} catch (MalformedURLException murle) {
			murle.printStackTrace();
			ThdlDebug.noteIffyCode();
		}
	}

	public void open(final TranscriptView[] views, final URL video, final URL vocabulary)
	{
		videoPanel = new JPanel(new GridLayout(1,1));
		textPanel = new JPanel(new BorderLayout());

		sp = new SoundPanel(this, video, views[0].getT1s(), views[0].getT2s(), views[0].getIDs());
		tp = new TwoWayTextPlayer(sp, views[0], Color.cyan);

		JPanel jp = new JPanel();
		String[] viewNames = new String[views.length];
		for (int i=0; i<views.length; i++)
			viewNames[i] = new String(views[i].getTitle());

		JComboBox viewOptions = new JComboBox(viewNames);
		viewOptions.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e)
			{
				JComboBox jcb = (JComboBox)e.getSource();
				setTranscriptView(views[jcb.getSelectedIndex()]);
			}
		});
		jp.add(viewOptions);

		extras = vocabulary;
		textPanel.add("Center", tp);

		textPanel.add("North", jp);
		sp.addAnnotationPlayer(tp);
		sp.start();
		final Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			public void run()
			{
				if (sp.cmd_isSized())
				{
					timer.cancel();
					//(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable)
					videoFrame = new JInternalFrame(null, false, false, false, false);
					textFrame = new JInternalFrame("Transcript", false, false, false, true);
					textFrame.setVisible(true);
					videoFrame.setVisible(true);
					if (vocabulary != null)
					{
						try {
							JTextPane vocabPane = new JTextPane();
							MouseListener[] mls = (MouseListener[])(vocabPane.getListeners(MouseListener.class));
							for (int i=0; i<mls.length; i++)
								vocabPane.removeMouseListener(mls[i]);
							vocabPane.setEditable(false);
							vocabPanel = new JScrollPane(vocabPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
							InputStream in = extras.openStream();
							RTFEditorKit rtf = new RTFEditorKit();
							rtf.read(in, vocabPane.getDocument(), 0);
							vocabPane.getCaret().setDot(0);
						} catch (IOException ioe) {
							ioe.printStackTrace();
							ThdlDebug.noteIffyCode();
						} catch (BadLocationException ble) {
							ble.printStackTrace();
							ThdlDebug.noteIffyCode();
						}
						vocabFrame = new JInternalFrame("About the Video", false, false, false, true);
						vocabFrame.setContentPane(vocabPanel);
						vocabFrame.setVisible(true);
					}
					videoPanel.add(sp);
					videoFrame.setContentPane(videoPanel);
					videoFrame.pack();
					videoFrame.setMaximumSize(videoFrame.getSize());
					textFrame.setContentPane(textPanel);
					if (videoFrame.getSize().height < 100) //must be audio file, so frame top to bottom
					{
						orientation = TOP_TO_BOTTOM;
						videoFrame.setTitle("Audio");
						videoFrame.setLocation(getSize().width/2 - videoFrame.getSize().width/2, 0);
						textFrame.setLocation(0, videoFrame.getSize().height);
						if (vocabulary != null)
						{
							textFrame.setSize(getSize().width / 2, getSize().height - videoFrame.getSize().height);
							vocabFrame.setLocation(textFrame.getSize().width, videoFrame.getSize().height);
							vocabFrame.setSize(getSize().width - textFrame.getSize().width, getSize().height - videoFrame.getSize().height);
						} else
							textFrame.setSize(getSize().width, getSize().height - videoFrame.getSize().height);
					} else { //must be video file, so frame left to right
						orientation = LEFT_TO_RIGHT;
						videoFrame.setTitle("Video");
						videoFrame.setLocation(0,0);
						textFrame.setLocation(videoFrame.getSize().width, 0);
						textFrame.setSize(getSize().width - videoFrame.getSize().width, getSize().height);
						if (vocabulary != null)
						{
							vocabFrame.setLocation(0, videoFrame.getSize().height);
							vocabFrame.setSize(videoFrame.getSize().width, getSize().height - videoFrame.getSize().height);
						}
					}
					if (sp.getVisualComponent() != null)
					{ //video, so can be full screen
						sp.getVisualComponent().addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent e)
							{
								java.awt.Component visual = null;

								if (fullScreen == null)
								{
									fullScreen = new JFrame();

									/* We don't do anything special if this fails: */
									JdkVersionHacks.undecorateJFrame(fullScreen);

									fullScreen.getContentPane().setBackground(Color.black);
									Dimension screenSize = fullScreen.getToolkit().getScreenSize();
									Dimension videoSize = sp.getVisualComponent().getPreferredSize();
//									Dimension controlSize = sp.getControlComponent().getPreferredSize();
									int videoWidth = videoSize.width;
									int videoHeight = videoSize.height;
									float vWidth = new Integer(videoWidth).floatValue();
									float vHeight = new Integer(videoHeight).floatValue();
									float xFactor = vHeight / vWidth;
									int fullVideoWidth = screenSize.width;
									float product = fullVideoWidth * xFactor;
									int fullVideoHeight = new Float(product).intValue();
									fullScreenSize = new Dimension(fullVideoWidth, fullVideoHeight);
									fullScreen.getContentPane().setLayout(null);
								}
								if (isFullScreen)
								{
									fullScreen.dispose();

									invalidate();
									validate();
									repaint();

									fullScreen.getContentPane().removeAll();
									sp.restoreVisualComponent();

									invalidate();
									validate();
									repaint();

									isFullScreen = false;
								} else {
									visual = sp.popVisualComponent();
									fullScreen.show();

									/* FIXME: In SavantShell, we test
                                       to see if MAXIMIZE_BOTH is
                                       supported, but we don't
                                       here. */
									/* Ignore failure: */
									JdkVersionHacks.maximizeJFrameInBothDirections(fullScreen);

									fullScreen.getContentPane().add(visual);
									visual.setLocation(0, (fullScreen.getSize().height - fullScreenSize.height)/2);
									visual.setSize(fullScreenSize);
									fullScreen.getContentPane().invalidate();
									fullScreen.getContentPane().validate();
									fullScreen.getContentPane().repaint();
									isFullScreen = true;
								}
							}
						});
					}
					removeAll();
					setLayout(null);
					add(videoFrame, JLayeredPane.POPUP_LAYER);
					invalidate();
					validate();
					repaint();
					add(textFrame, JLayeredPane.DEFAULT_LAYER);
					invalidate();
					validate();
					repaint();
					if (vocabulary != null)
					{
						add(vocabFrame, JLayeredPane.DEFAULT_LAYER);
						invalidate();
						validate();
						repaint();
					}
				}
			}}, 0, 50);
	}

	public void setTranscriptView(TranscriptView view)
	{
		textPanel.invalidate();
		textPanel.remove(tp);
		tp.reset();
		tp = new TwoWayTextPlayer(sp, view, Color.cyan);
		sp.addAnnotationPlayer(tp);
		textPanel.add("Center", tp);
		textPanel.validate();
		textPanel.repaint();
	}
}
