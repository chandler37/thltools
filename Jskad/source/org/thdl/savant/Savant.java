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
import java.awt.Cursor;
import java.awt.Window;
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
import java.util.ResourceBundle;

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

import org.thdl.media.*;
import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlActionListener;
import org.thdl.util.ThdlI18n;

public class Savant extends JDesktopPane
{
	protected SmartMoviePanel sp = null;
	protected TwoWayTextPlayer tp = null;

	protected JInternalFrame videoFrame = null;
	protected JInternalFrame textFrame = null;
	protected JInternalFrame vocabFrame = null;

	protected JFrame fullScreen = null;
	protected boolean isFullScreen = false;
	protected Dimension fullScreenSize = null;

	protected JPanel textPanel = null;
	protected JScrollPane vocabPanel = null;

	protected URL mediaURL = null;
	protected URL extras = null;
	protected TranscriptView[] transcriptViews;

	protected int orientation = 0;

	protected ResourceBundle messages;

	public final int TOP_TO_BOTTOM = 1;
	public final int LEFT_TO_RIGHT = 2;

	public Savant()
	{
		messages = ThdlI18n.getResourceBundle();

		setBackground(new JFrame().getBackground());
		setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

		//(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable)
		videoFrame = new JInternalFrame(null, false, false, false, false);
		videoFrame.setVisible(true);
		videoFrame.setLocation(0,0);
		videoFrame.setSize(0,0);
		add(videoFrame, JLayeredPane.POPUP_LAYER);
		invalidate();
		validate();
		repaint();

		textFrame = new JInternalFrame(messages.getString("Transcript"), false, false, false, true);
		textPanel = new JPanel(new BorderLayout());
		textFrame.setVisible(true);
		textFrame.setLocation(0,0);
		textFrame.setSize(0,0);
		textFrame.setContentPane(textPanel);
		add(textFrame, JLayeredPane.DEFAULT_LAYER);
		invalidate();
		validate();
		repaint();

		addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent ce)
			{
				switch (orientation) {
					case TOP_TO_BOTTOM:
						videoFrame.setLocation(getSize().width/2 - videoFrame.getSize().width/2, 0);
						textFrame.setLocation(0, videoFrame.getSize().height);
						textFrame.setSize(getSize().width, getSize().height - videoFrame.getSize().height);
						break;

					case LEFT_TO_RIGHT:
						videoFrame.setLocation(0,0);
						textFrame.setLocation(videoFrame.getSize().width, 0);
						textFrame.setSize(getSize().width - videoFrame.getSize().width, getSize().height);
						break;

					default:
						break;
				}
			}
		});
	}

	public void close()
	{
		if (sp != null) {
			try {
				sp.destroy();
			} catch (SmartMoviePanelException smpe) {
				smpe.printStackTrace();
			}
		}
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

	public void setMediaPlayer(SmartMoviePanel smp) {
		if (sp == smp)
			return;

		if (sp != null) {
			try {
				sp.destroy();
				videoFrame.getContentPane().removeAll();
			} catch (SmartMoviePanelException smpe) {
				smpe.printStackTrace();
				ThdlDebug.noteIffyCode();
			}
		}

		sp = smp;
		sp.setParentContainer(Savant.this);	
		if (mediaURL != null) {
			String t1s = convertTimesForSmartMoviePanel(transcriptViews[0].getT1s());
			String t2s = convertTimesForSmartMoviePanel(transcriptViews[0].getT2s());
			sp.initForSavant(t1s, t2s, transcriptViews[0].getIDs());
			sp.addAnnotationPlayer(tp);
			try {
				sp.loadMovie(mediaURL);
				startTimer();
			} catch (SmartMoviePanelException smpe) {
				smpe.printStackTrace();
				ThdlDebug.noteIffyCode();
			}
		}
	}

	private void startTimer() {
		final java.util.Timer timer = new java.util.Timer(true);
		timer.schedule(new TimerTask() {
			public void run()
			{
				if (sp.isInitialized())
				{
					System.out.println("initialized");

					timer.cancel();
					videoFrame.getContentPane().add(sp);
					videoFrame.pack();
					videoFrame.setMaximumSize(videoFrame.getSize());
					if (videoFrame.getSize().height < 100) //must be audio file, so frame top to bottom
					{
						orientation = TOP_TO_BOTTOM;
						videoFrame.setTitle(messages.getString("Audio"));
						videoFrame.setLocation(getSize().width/2 - videoFrame.getSize().width/2, 0);
						textFrame.setLocation(0, videoFrame.getSize().height);
						textFrame.setSize(getSize().width, getSize().height - videoFrame.getSize().height);
					} else { //must be video file, so frame left to right
						orientation = LEFT_TO_RIGHT;
						videoFrame.setTitle(messages.getString("Video"));
						videoFrame.setLocation(0,0);
						textFrame.setLocation(videoFrame.getSize().width, 0);
						textFrame.setSize(getSize().width - videoFrame.getSize().width, getSize().height);
					}
					invalidate();
					validate();
					repaint();

					Window rootWindow = (Window)SwingUtilities.getAncestorOfClass(Window.class, Savant.this);
					rootWindow.setCursor(null);
				}
		}}, 0, 50);
	}

	public SmartMoviePanel getMediaPlayer() {
		return sp;
	}

	private String convertTimesForSmartMoviePanel(String s) {
		StringBuffer sBuff = new StringBuffer();
		StringTokenizer sTok = new StringTokenizer(s, ",");
		while (sTok.hasMoreTokens()) {
			sBuff.append(String.valueOf(new Float(Float.parseFloat(sTok.nextToken()) * 1000).intValue()));
			sBuff.append(',');
		}
		return sBuff.toString();
	}
	public void open(TranscriptView[] views, URL video, final URL vocabulary)
	{
/* FIXME eventually
   w/Savant and SoundPanel times were in seconds. QuillDriver's
   SmartMoviePanel uses milliseconds instead, so here I convert
   (for the time being anyway - eventually we need the same
   time code format for both QD and Savant. */

		String t1s = convertTimesForSmartMoviePanel(views[0].getT1s());
		String t2s = convertTimesForSmartMoviePanel(views[0].getT2s());

		if (sp == null) {
			JOptionPane.showConfirmDialog(Savant.this, messages.getString("SupportedMediaError"), null, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			return;
		}

		sp.initForSavant(t1s, t2s, views[0].getIDs());
		tp = new TwoWayTextPlayer(sp, views[0], Color.cyan);

		transcriptViews = views;

		JPanel jp = new JPanel();
		String[] viewNames = new String[views.length];
		for (int i=0; i<views.length; i++)
			viewNames[i] = new String(views[i].getTitle());

		JComboBox viewOptions = new JComboBox(viewNames);
		viewOptions.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e)
			{
				JComboBox jcb = (JComboBox)e.getSource();
				setTranscriptView(transcriptViews[jcb.getSelectedIndex()]);
			}
		});
		jp.add(viewOptions);

		textPanel.add("Center", tp);

		textPanel.add("North", jp);
		sp.addAnnotationPlayer(tp);
	try {
		sp.loadMovie(video);
		mediaURL = video;
		startTimer();	
	} catch (SmartMoviePanelException smpe) {
			smpe.printStackTrace();
			ThdlDebug.noteIffyCode();
		}
	}

	public void setTranscriptView(TranscriptView view)
	{
		textPanel.invalidate();
		textPanel.remove(tp);
		tp.reset();
		tp = new TwoWayTextPlayer(sp, view, Color.cyan);
		sp.addAnnotationPlayer(tp);
		if (sp.isPlaying())
			sp.launchAnnotationTimer();
		textPanel.add("Center", tp);
		textPanel.validate();
		textPanel.repaint();
	}
}

/*
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

				We don't do anything special if this fails:
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

						FIXME: In SavantShell, we test
                                       to see if MAXIMIZE_BOTH is
                                       supported, but we don't
                                       here. 
							Ignore failure:
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
*/
