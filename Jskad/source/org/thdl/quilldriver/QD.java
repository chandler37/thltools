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

package org.thdl.quilldriver;

import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.plaf.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import org.thdl.tib.input.*;
import org.thdl.tib.text.*;
import org.thdl.tib.text.TibetanDocument.*;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMSource;
import javax.xml.transform.stream.*;
import javax.xml.transform.*;

import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlActionListener;
import org.thdl.util.ThdlAbstractAction;
import org.thdl.util.ThdlOptions;

public class QD extends JDesktopPane {
	//project related data
	protected Project project;

	//speaker related
	protected SpeakerTable speakerTable;

	//video related
	protected SmartMoviePanel player = null;

	//frame related
	protected JInternalFrame videoFrame = null;
	protected JInternalFrame textFrame = null;
	protected JInternalFrame actionFrame = null;

	//miscellaneous stuff
	public JTabbedPane jtp;
	public TimeCodeManager tcp = null;
	public SpeakerManager spm = null;
	public JTextPane pane;
	public Hashtable actions;
	public ImageIcon clockIcon;
	public static final String componentStyleName = "Component";
	public Style componentStyle;
	public DataFlavor timeFlavor;

	protected String thdl_mediaplayer_property = null;

	//class fields because they are affected depending on whether we're
	//in read-only or edit mode
	protected JMenu editMenu, searchMenu;
	protected JMenuItem replaceTextItem;

	protected ResourceBundle messages;

	protected java.util.List work;
	protected Work currentWork;

	protected DuffPane sharedDP = new DuffPane();
	protected DuffPane sharedDP2 = new DuffPane();

	protected TibetanDocument findDoc = null;
	protected TibetanDocument replaceDoc = null;

	protected URL keyboard_url = null;

	protected KeyStroke cutKey, copyKey, pasteKey, selectAllKey;
	protected KeyStroke insert1TimeKey, insert2TimesKey, insertSpeakerKey;
	protected KeyStroke findKey, replaceKey;


public QD(ResourceBundle messages) {
	this.messages = messages;
	
	setBackground(new JFrame().getBackground());
	setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

	clockIcon = new ImageIcon(QD.class.getResource("clock.gif"));
	timeFlavor = new DataFlavor(Integer.class, "time");

	Action insert1TimeAction = new ThdlAbstractAction("insert1Time", null) {
		public void theRealActionPerformed(ActionEvent e) {
			new TimePoint(pane, clockIcon, tcp.getOutTime());
			tcp.setInTime(tcp.getOutTime().intValue());
			tcp.setOutTime(player.getEndTime());
		}
	};

	Action insert2TimesAction = new ThdlAbstractAction("insert2Times", null) {
		public void theRealActionPerformed(ActionEvent e) {
			int p1 = pane.getSelectionStart();
			int p2 = pane.getSelectionEnd();
			pane.setCaretPosition(p1);
			new TimePoint(pane, clockIcon, tcp.getInTime());
			pane.setCaretPosition(p2+1);
			new TimePoint(pane, clockIcon, tcp.getOutTime());
			if (p1 == p2)
				pane.setCaretPosition(pane.getCaretPosition()-1);
			tcp.setInTime(tcp.getOutTime().intValue());
			tcp.setOutTime(player.getEndTime());
		}
	};

/*
	Action saveAction = new ThdlAbstractAction("saveTranscript", null) {
		public void theRealActionPerformed(ActionEvent e) {
			getSave();
		}
	};
*/

	JTextPane tp = new JTextPane();
	Keymap keymap = tp.addKeymap("QDBindings", tp.getKeymap());

	insert1TimeKey = KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, Event.CTRL_MASK);
	insert2TimesKey = KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, Event.CTRL_MASK);
	insertSpeakerKey = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.ALT_MASK);
	findKey = KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK);
	replaceKey = KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK);
	cutKey = KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK);
	pasteKey = KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK);
	copyKey = KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK);
	selectAllKey = KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK);

//	KeyStroke saveKey = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);

	keymap.addActionForKeyStroke(insert1TimeKey, insert1TimeAction);
	keymap.addActionForKeyStroke(insert2TimesKey, insert2TimesAction);
//	keymap.addActionForKeyStroke(saveKey, saveAction);

	Speaker[] speakers = new Speaker[0];
	SpeakerData speakerData = new SpeakerData(speakers, keymap);
	speakerTable = new SpeakerTable(speakerData);

	pane = new DuffPane();
	pane.setKeymap(keymap);
	new TimePointDropTarget(pane);

	work = new ArrayList();
	currentWork = new Work();
	work.add(currentWork);

	JPanel textPanel = new JPanel(new BorderLayout());
	textPanel.add("Center", new JScrollPane(pane));

	JPanel actionPanel = new JPanel(new BorderLayout());
	jtp = new JTabbedPane();
	project = null;
	spm = new SpeakerManager(speakerTable);
	jtp.addTab(messages.getString("Speakers"), spm);
	actionPanel.add("Center", jtp);

	//(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable)
	videoFrame = new JInternalFrame(null, false, false, false, false);
	videoFrame.setVisible(true);
	videoFrame.setLocation(0,0);
	videoFrame.setSize(0,0);
	add(videoFrame, JLayeredPane.POPUP_LAYER);
	invalidate();
	validate();
	repaint();

	textFrame = new JInternalFrame(null, false, false, false, true);
	textFrame.setVisible(true);
	textFrame.setContentPane(textPanel);
	textFrame.setLocation(0,0);
	textFrame.setSize(0,0);
	textFrame.setJMenuBar(getTextMenuBar());
	add(textFrame, JLayeredPane.DEFAULT_LAYER);
	invalidate();
	validate();
	repaint();

	actionFrame = new JInternalFrame(null, false, false, false, true);
	actionFrame.setVisible(true);
	actionFrame.setContentPane(actionPanel);
	actionFrame.setLocation(0,0);
	actionFrame.setSize(0,0);
	add(actionFrame, JLayeredPane.DEFAULT_LAYER);
	invalidate();
	validate();
	repaint();

	addComponentListener(new ComponentAdapter() {
		public void componentResized(ComponentEvent ce) {
			Dimension d = videoFrame.getSize();
			if (d.width == 0 && d.height == 0)
				videoFrame.setSize(getSize().width / 2, 0);
			textFrame.setLocation(videoFrame.getSize().width, 0);
			textFrame.setSize(getSize().width - videoFrame.getSize().width, getSize().height);
			actionFrame.setLocation(0, videoFrame.getSize().height);
			actionFrame.setSize(videoFrame.getSize().width, getSize().height - videoFrame.getSize().height);
		}
	});
}

private void startTimer() {
		final java.util.Timer timer = new java.util.Timer(true);
		timer.schedule(new TimerTask() {
			public void run()
			{
				if (player.isInitialized())
				{
					timer.cancel();

					if (tcp != null)
						jtp.remove(tcp);
					tcp = new TimeCodeManager();
					jtp.addTab(messages.getString("TimeCoding"), tcp);

					videoFrame.setContentPane(player);
					videoFrame.pack();
					videoFrame.setMaximumSize(videoFrame.getSize());
				invalidate();
				validate();
				repaint();
					textFrame.setLocation(videoFrame.getSize().width, 0);
					textFrame.setSize(getSize().width - videoFrame.getSize().width, getSize().height);
				invalidate();
				validate();
				repaint();
					actionFrame.setLocation(0, videoFrame.getSize().height);
					actionFrame.setSize(videoFrame.getSize().width, getSize().height - videoFrame.getSize().height);
				invalidate();
				validate();
				repaint();
				}
			}}, 0, 50);
}

private void createActionTable(JTextComponent textComponent) {
    actions = new Hashtable();
    Action[] actionsArray = textComponent.getActions();
    for (int i = 0; i < actionsArray.length; i++) {
        Action a = actionsArray[i];
        actions.put(a.getValue(Action.NAME), a);
    }
}    

private Action getActionByName(String name) {
    return (Action)(actions.get(name));
}

class TimePoint extends JLabel implements DragGestureListener, DragSourceListener {
	StyledDocument doc;
	Position pos;
	Integer time;

	TimePoint(final JTextPane tp, Icon icon, Integer time) {
		super(icon);
		this.time = time;
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setToolTipText(getTimeAsString());
		DragSource dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1)
					playSegment();
				else {
					SpinnerNumberModel snm1 = new SpinnerNumberModel(0, 0, player.getEndTime(), 10);
					JSpinner spinner = new JSpinner(snm1);
					spinner.setPreferredSize(new Dimension(100, 40));
					spinner.setValue(getTime());
					if (JOptionPane.showOptionDialog(tp, spinner, messages.getString("AdjustTimeCode"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null) == JOptionPane.OK_OPTION)
						setTime((Integer)spinner.getValue());
				}
			}
		});
		doc = tp.getStyledDocument();
		tp.insertComponent(this);
		try { //insertions occur prior to position, so position should be right before icon after icon is inserted
			pos = doc.createPosition(tp.getCaretPosition()-1);
		} catch (BadLocationException ble) {
			ble.printStackTrace();
			ThdlDebug.noteIffyCode();
		}
	}
	public void setTime(Integer t) {
		time = t;
		setToolTipText(getTimeAsString());
	}
	public Integer getTime() {
		return time;
	}
	public String getTimeAsString() {
		return String.valueOf(time);
	}
	public void playSegment() {
		int i=pos.getOffset();
		System.out.println(String.valueOf(i));
		try {
			for (i++; i<doc.getLength(); i++) {
				AttributeSet attr = doc.getCharacterElement(i).getAttributes();
				Component comp;
				if (null != (comp = StyleConstants.getComponent(attr)))
					if (comp instanceof TimePoint) {
						TimePoint t2 = (TimePoint)comp;
						player.cmd_playSegment(time, t2.time);
						return;
					}
			}
			player.cmd_playSegment(time, null);
		} catch (SmartMoviePanelException smpe) {
			smpe.printStackTrace();
			ThdlDebug.noteIffyCode();
		}
	}
	public void dragGestureRecognized(DragGestureEvent dge) {
		Transferable transferable = new TimePointTransferable(this);
		try {
			dge.startDrag(null, transferable, this);
		} catch (InvalidDnDOperationException idoe) {
			idoe.printStackTrace();
			ThdlDebug.noteIffyCode();
		}
	}
	public void dragEnter(DragSourceDragEvent dsde) {
	}
	public void dragOver(DragSourceDragEvent dsde) {
	}
	public void dragExit(DragSourceEvent dse) {
	}
	public void dropActionChanged(DragSourceDragEvent dsde) {
	}
	public void dragDropEnd(DragSourceDropEvent dsde) {
		if (dsde.getDropSuccess()) {
			if (dsde.getDropAction() == DnDConstants.ACTION_MOVE) {
				try {
					doc.remove(pos.getOffset(), 1);
				} catch (BadLocationException ble) {
					ble.printStackTrace();
					ThdlDebug.noteIffyCode();
				}
			}
		}
	}
}

class TimePointTransferable implements Transferable {
	DataFlavor[] flavors = new DataFlavor[1];
	public Integer time;

	public TimePointTransferable(TimePoint timePoint) {
		time = timePoint.getTime();
		flavors[0] = timeFlavor;
	}
	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		if (flavor.equals(flavors[0]))
			return true;
		else
			return false;
	}
	public Object getTransferData(DataFlavor flavor) {
		return time;
	}
}

public class TimePointDropTarget implements DropTargetListener {
	JTextPane pane;
	DropTarget dropTarget;

	public TimePointDropTarget(JTextPane pane) {
		this.pane = pane;
		dropTarget = new DropTarget(pane, DnDConstants.ACTION_COPY_OR_MOVE, this, true, null);
	}
	public void dragEnter(DropTargetDragEvent dtde) {
		acceptOrRejectDrag(dtde);
		pane.requestFocus();
	}
	public void dragExit(DropTargetEvent dte) {
	}
	public void dragOver(DropTargetDragEvent dtde) {
		acceptOrRejectDrag(dtde);
		Point p = dtde.getLocation();
		pane.setCaretPosition(pane.viewToModel(p));
	}
	public void dropActionChanged(DropTargetDragEvent dtde) {
		acceptOrRejectDrag(dtde);
	}
	public void drop(DropTargetDropEvent dtde) {
		if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
			dtde.acceptDrop(dtde.getDropAction());
			dtde.dropComplete(dropTimePoint(dtde.getTransferable()));
		}
	}
	public boolean acceptOrRejectDrag(DropTargetDragEvent dtde) {
		if (dtde.isDataFlavorSupported(timeFlavor))
			return true;
		else
			return false;
	}
	public boolean dropTimePoint(Transferable transferable) {
		try {
			Integer time = (Integer)transferable.getTransferData(timeFlavor);
			new TimePoint(pane, clockIcon, time);
		} catch (UnsupportedFlavorException ufe) {
			ufe.printStackTrace();
			ThdlDebug.noteIffyCode();
			return false;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			ThdlDebug.noteIffyCode();
			return false;
		}
		return true;
	}
}

class TimeCodeManager extends JPanel {
	JSpinner inSpinner, outSpinner;

	TimeCodeManager() {
		setLayout(new BorderLayout());
		JPanel inPanel = new JPanel();
		JButton inButton = new JButton(messages.getString("In"));
		SpinnerNumberModel snm1 = new SpinnerNumberModel(0, 0, player.getEndTime(), 10);
		inSpinner = new JSpinner(snm1);
		inSpinner.setValue(new Integer(0));
		inSpinner.setPreferredSize(new Dimension(100, inButton.getPreferredSize().height));
		inPanel.add(inButton);
		inPanel.add(inSpinner);

		inButton.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				int k = player.getCurrentTime();
				if (k != -1)
					setInTime(k);
			}
		});


		JPanel outPanel = new JPanel();
		JButton outButton = new JButton(messages.getString("Out"));
		SpinnerNumberModel snm2 = new SpinnerNumberModel(0, 0, player.getEndTime(), 10);
		outSpinner = new JSpinner(snm2);
		outSpinner.setValue(new Integer(player.getEndTime()));
		outSpinner.setPreferredSize(new Dimension(100, inButton.getPreferredSize().height));
		outPanel.add(outButton);
		outPanel.add(outSpinner);

		outButton.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				int k = player.getCurrentTime();
				if (k != -1) {
					setOutTime(k);
					try {
						player.cmd_stop();
					} catch (SmartMoviePanelException smpe) {
						smpe.printStackTrace();
						ThdlDebug.noteIffyCode();
					}
				}
			}
		});


		JButton playSegButton = new JButton(messages.getString("PlaySegment"));
		playSegButton.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				Integer in = getInTime();
				Integer out = getOutTime();
				if (out.intValue() > in.intValue()) {
					try {
						player.cmd_playSegment(in, out);
					} catch (SmartMoviePanelException smpe) {
						smpe.printStackTrace();
						ThdlDebug.noteIffyCode();
					}
				}
			}
		});
		JPanel ps = new JPanel();
		ps.add(playSegButton);

		JButton playPauseButton = new JButton(messages.getString("PlayPause"));
		playPauseButton.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				if (player != null) {
					try {
						if (player.isPlaying())
							player.cmd_stop();
						else
							player.cmd_playOn();
					} catch (SmartMoviePanelException smpe) {
						smpe.printStackTrace();
						ThdlDebug.noteIffyCode();
					}
				}
			}
		});

		JPanel playPausePanel = new JPanel();
		playPausePanel.add(playPauseButton);

		Box box = Box.createVerticalBox();
		box.add(inPanel);
		box.add(outPanel);
		box.add(ps);
		box.add(playPausePanel);

		add("North", box);
	}
	public Integer getInTime() {
		return (Integer)inSpinner.getValue();
	}
	public Integer getOutTime() {
		return (Integer)outSpinner.getValue();
	}
	public void setInTime(int k) {
		inSpinner.setValue(new Integer(k));
	}
	public void setOutTime(int k) {
		outSpinner.setValue(new Integer(k));
	}
}

class Work {
	public String name;
	public String task;
	public String startDate;
	public String duration;
	public Date beginTime;

	Work() {
		beginTime = new Date();
		startDate = beginTime.toString();
	}
	public void stopWork() {
		Date stopTime = new Date();
		long milliseconds = stopTime.getTime() - beginTime.getTime();
		int minutes = new Long(milliseconds / 1000 / 60).intValue();
		duration = String.valueOf(minutes);
		name = project.getTranscriberName();
		task = project.getTranscriberTask();
	}
}

public void setMediaPlayerProperty(String property) {
	if (property.equals("jmf") || property.equals("qt4j"))
		if (!thdl_mediaplayer_property.equals(property)) {
			thdl_mediaplayer_property = new String(property);
			if (project != null) {
				File f = project.getMedia();
				if (f != null) {
					project.setMedia(null);
					project.setMedia(f);
				}
			}		
		}
}
public String getMediaPlayerProperty() {
	//if already set, return current media player
	if (thdl_mediaplayer_property == null) {
		//else get default based on system, user prefs, etc.
        String os;
        try {
            os = System.getProperty("os.name").toLowerCase();
        } catch (SecurityException e) {
            os = "unknown";
        }
		if (os.indexOf("mac") != -1) //macs default to qt4j
			thdl_mediaplayer_property = ThdlOptions.getStringOption("thdl.media.player", "qt4j");
		else if (os.indexOf("windows") != -1)
			thdl_mediaplayer_property = ThdlOptions.getStringOption("thdl.media.player", "jmf");
		else //put linux etc. here
			thdl_mediaplayer_property = ThdlOptions.getStringOption("thdl.media.player", "jmf");
	}
	return thdl_mediaplayer_property;
}

class Project extends JPanel {
	JTextField titleField, mediaField, transcriptField, nameField, taskField;
	File transcript = null;
	File media = null;

public String getTitle() {
	return titleField.getText();
}
public void setTitle(String s) {
	titleField.setText(s);
}
public String getTranscriberName() {
	return nameField.getText();
}
public void setTranscriberName(String s) {
	nameField.setText(s);
}
public String getTranscriberTask() {
	return taskField.getText();
}
public void setTranscriberTask(String s) {
	taskField.setText(s);
}
public File getTranscript() {
	return transcript;
}
public void setTranscript(File f) { //doesn't actually load or save transcript, just changes changeTranscript label
	transcript = f;
	if (transcript == null)
		transcriptField.setText("");
	else
		transcriptField.setText(transcript.getPath());
}
public File getMedia() {
	return media;
}
public void setMedia(File f) {
	if (f == null) {
		media = f;
		mediaField.setText("");
		if (player != null) {
			try {
				player.cmd_stop();
				player.destroy();
			} catch (SmartMoviePanelException smpe) {
				smpe.printStackTrace();
				ThdlDebug.noteIffyCode();
			}
			videoFrame.getContentPane().remove(player);
			videoFrame.getContentPane().invalidate();
			videoFrame.getContentPane().validate();
			videoFrame.getContentPane().repaint();
			player = null;
			videoFrame.setSize(new Dimension(QD.this.getSize().width / 2, 0));
			jtp.remove(tcp);
			tcp = null;
			actionFrame.setLocation(0,0);
			actionFrame.setSize(new Dimension(actionFrame.getSize().width, QD.this.getSize().height));
		}
	} else {
		try {
			URL url = f.toURL();

			if (player != null) {
				try {
					player.cmd_stop();
					player.destroy();
					player = null;
				} catch (SmartMoviePanelException smpe) {
					smpe.printStackTrace();
					ThdlDebug.noteIffyCode();
				}
			}

			try {
				if (thdl_mediaplayer_property == null)
					thdl_mediaplayer_property = getMediaPlayerProperty();
				if (thdl_mediaplayer_property.equals("qt4j")) {
					player = new SmartQT4JPlayer();
					player.loadMovie(url);
				} else if (thdl_mediaplayer_property.equals("jmf")) {
					player = new SmartJMFPlayer(QD.this, url);
				}
				media = f;
				mediaField.setText(media.getPath());
				startTimer();
			} catch (SmartMoviePanelException smpe) {
				smpe.printStackTrace();
				ThdlDebug.noteIffyCode();
			}
		} catch (MalformedURLException murle) {
			murle.printStackTrace();
			ThdlDebug.noteIffyCode();
		}
	}
}

public Project() {
	JPanel p = new JPanel(new GridLayout(2,2));
	titleField = new JTextField();
	int preferredHeight = titleField.getPreferredSize().height;
	titleField.setPreferredSize(new Dimension(300, preferredHeight));
	mediaField = new JTextField();
	mediaField.setPreferredSize(new Dimension(300, preferredHeight));
	mediaField.setEditable(false);
	transcriptField = new JTextField();
	transcriptField.setPreferredSize(new Dimension(300, preferredHeight));
	transcriptField.setEditable(false);
	nameField = new JTextField();
	nameField.setPreferredSize(new Dimension(300, preferredHeight));
	taskField = new JTextField();
	taskField.setPreferredSize(new Dimension(300, preferredHeight));

	Box box = Box.createVerticalBox();
	JPanel p1 = new JPanel();
	p1.add(new JLabel(messages.getString("TitleColon")));
	p1.add(titleField);
	JPanel p1b = new JPanel(new BorderLayout());
	p1b.add("East", p1);
	p1b.setAlignmentX(Component.RIGHT_ALIGNMENT);

	JPanel p2 = new JPanel();
	p2.add(new JLabel(messages.getString("MediaColon")));
	p2.add(mediaField);
	JPanel p2b = new JPanel(new BorderLayout());
	p2b.add("East", p2);
	p2b.setAlignmentX(Component.RIGHT_ALIGNMENT);

	JPanel p3 = new JPanel();
	p3.add(new JLabel(messages.getString("TranscriptColon")));
	p3.add(transcriptField);
	JPanel p3b = new JPanel(new BorderLayout());
	p3b.add("East", p3);
	p3b.setAlignmentX(Component.RIGHT_ALIGNMENT);

	JPanel p4 = new JPanel();
	p4.add(new JLabel(messages.getString("TranscriberName")));
	p4.add(nameField);
	JPanel p4b = new JPanel(new BorderLayout());
	p4b.add("East", p4);
	p4b.setAlignmentX(Component.RIGHT_ALIGNMENT);

	JPanel p5 = new JPanel();
	p5.add(new JLabel(messages.getString("TranscriberTask")));
	p5.add(taskField);
	JPanel p5b = new JPanel(new BorderLayout());
	p5b.add("East", p5);
	p5b.setAlignmentX(Component.RIGHT_ALIGNMENT);

	box.add(p1b);
	box.add(p2b);
	box.add(p3b);
	box.add(p4b);
	box.add(p5b);

	JPanel pBig = new JPanel(new BorderLayout());
	pBig.add("West", box);

	setLayout(new BorderLayout());
	add("North", pBig);
}
}

public void changeKeyboard(ActionEvent e) {
	DuffPane dp = (DuffPane)pane;
	if (e.getActionCommand().equals("wylie")) {
		dp.registerKeyboard();
	//	project.tName.setupKeyboard();
	//	project.tTask.setupKeyboard();
		sharedDP.setupKeyboard();
		sharedDP2.setupKeyboard();
		return;
	}
	keyboard_url = null;
	if (e.getActionCommand().equals("sambhota1"))
		keyboard_url = TibetanMachineWeb.class.getResource("sambhota_keyboard_1.ini");
	else if (e.getActionCommand().equals("tcc1"))
		keyboard_url = TibetanMachineWeb.class.getResource("tcc_keyboard_1.ini");
	else if (e.getActionCommand().equals("tcc2"))
		keyboard_url = TibetanMachineWeb.class.getResource("tcc_keyboard_2.ini");
	dp.registerKeyboard(keyboard_url);
//	project.tName.setupKeyboard();
//	project.tTask.setupKeyboard();
	sharedDP.setupKeyboard();
	sharedDP2.setupKeyboard();
}

public boolean saveTranscript() {
	currentWork.stopWork();

//change keyboard back to wylie for a second
		DuffPane dp = (DuffPane)pane;
if (keyboard_url != null) {

			dp.registerKeyboard();
	//		project.tName.setupKeyboard();
	//		project.tTask.setupKeyboard();
			sharedDP.setupKeyboard();
			sharedDP.setupKeyboard();
}


	org.jdom.Element title = new org.jdom.Element("title");
	title.setText(project.getTitle().trim());

	org.jdom.Element media = new org.jdom.Element("mediafile");
	if (project.getMedia() == null)
		media.setText("");
	else {
		String tPath = project.getTranscript().getPath();
		String mPath = project.getMedia().getPath();
		if (tPath.substring(0, tPath.lastIndexOf(File.separatorChar)).equals(mPath.substring(0, mPath.lastIndexOf(File.separatorChar))))
			media.setText(mPath.substring(mPath.lastIndexOf(File.separatorChar)+1));
		else
			media.setText(mPath);
	}

	org.jdom.Element speakers = new org.jdom.Element("speakers");
	SpeakerData sd = (SpeakerData)speakerTable.getModel();
	java.util.List allSpeakers = sd.getSpeakers();
	Iterator spIter = allSpeakers.iterator();
	while (spIter.hasNext()) {
		Speaker sp = (Speaker)spIter.next();
		org.jdom.Element speaker = new org.jdom.Element("speaker");
		speaker.setAttribute("iconid", String.valueOf(sp.getIconID()));
		speaker.setText(sp.getName());
		speakers.addContent(speaker);
	}

	org.jdom.Element works = new org.jdom.Element("workhistory");
	Iterator wkIter = work.iterator();
	while (wkIter.hasNext()) {
		Work wkUnit = (Work)wkIter.next();
		org.jdom.Element wk = new org.jdom.Element("work");
		org.jdom.Element name = new org.jdom.Element("name");
		name.setText(wkUnit.name);
		org.jdom.Element task = new org.jdom.Element("task");
		task.setText(wkUnit.task);
		org.jdom.Element date = new org.jdom.Element("start");
		date.setText(wkUnit.startDate);
		org.jdom.Element duration = new org.jdom.Element("duration");
		duration.setText(wkUnit.duration);
		wk.addContent(name);
		wk.addContent(task);
		wk.addContent(date);
		wk.addContent(duration);
		works.addContent(wk);
	}

	org.jdom.Element meta = new org.jdom.Element("metadata");
	meta.addContent(title);
	meta.addContent(media);
	meta.addContent(speakers);
	meta.addContent(works);

	org.jdom.Element text = new org.jdom.Element("text");
	TibetanDocument doc = (TibetanDocument)pane.getDocument();
	ImageIcon[] icons = sd.getSpeakerIcons();
	int lastPoint = 0;
	int k;
	for (k=0; k<doc.getLength(); k++) {
		AttributeSet attr = doc.getCharacterElement(k).getAttributes();
		Component comp;
		if (null != (comp = StyleConstants.getComponent(attr))) {
			if (comp instanceof TimePoint) {
				TimePoint t = (TimePoint)comp;
				String wylie = doc.getWylie(lastPoint, k).trim();
				if (!wylie.equals(""))
					text.addContent(wylie);
				org.jdom.Element time = new org.jdom.Element("time");
				time.setAttribute("t", t.getTimeAsString());
				text.addContent(time);
				lastPoint = k+1;
			}
			else if (comp instanceof JLabel) {
				String wylie = doc.getWylie(lastPoint, k);
				if (!wylie.equals(""))
					text.addContent(wylie);
				JLabel l = (JLabel)comp;
				Speaker sp = sd.getSpeakerForIcon(l.getIcon());
				org.jdom.Element spkr = new org.jdom.Element("who");
				if (sp == null)
					spkr.setAttribute("id", "-1");
				else
					spkr.setAttribute("id", String.valueOf(sp.getIconID()));
				text.addContent(spkr);
				lastPoint = k+1;
			}
		}	
	}
	if (lastPoint < k) {
		String wylie = doc.getWylie(lastPoint, k);
		if (!wylie.equals(""))
			text.addContent(wylie);
	}

	org.jdom.Element qd = new org.jdom.Element("qd");
	qd.addContent(meta);
	qd.addContent(text);

	org.jdom.Document qdDoc = new org.jdom.Document(qd);
	XMLOutputter xmlOut = new XMLOutputter();
	try {
		FileOutputStream fous = new FileOutputStream(project.getTranscript());
		xmlOut.output(qdDoc, fous);
		Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(QD.class.getResource("QDtoHTML.xsl").openStream()));
		String f = project.getTranscript().getPath();
		File htmlOut = new File(f.substring(0, f.lastIndexOf('.')) + ".html");
		transformer.transform(new JDOMSource(qdDoc), new StreamResult(new FileOutputStream(htmlOut)));	

if (keyboard_url != null) {
		dp.registerKeyboard(keyboard_url);
	//	project.tName.setupKeyboard();
	//	project.tTask.setupKeyboard();
		sharedDP.setupKeyboard();
		sharedDP2.setupKeyboard();
}

		return true;
	} catch (FileNotFoundException fnfe) {
		fnfe.printStackTrace();
		ThdlDebug.noteIffyCode();
		return false;
	} catch (IOException ioe) {
		ioe.printStackTrace();
		ThdlDebug.noteIffyCode();
		return false;
	} catch (TransformerException e) {
		e.printStackTrace();
		ThdlDebug.noteIffyCode();
		return false;
	}
}

protected void clearProject() {
	if (project != null) {
		project.setMedia(null); //do this first to properly dispose of media player
		jtp.remove(project);
		project = null;

	}

	if (tcp != null) {
		tcp = null;
		jtp.remove(tcp);
	}

	SpeakerData sd = (SpeakerData)speakerTable.getModel();
	sd.removeAllSpeakers();

	jtp.invalidate();
	jtp.validate();
	jtp.repaint();

	pane.setText("");	
}

public void newProject(File mediaFile) {
	clearProject();
	project = new Project();
	jtp.addTab(messages.getString("ProjectDetails"), project);
	project.setMedia(mediaFile);
}

public boolean loadTranscript(File t) {
	if (!t.exists())
		return false;
	SAXBuilder builder = new SAXBuilder();

	try {

		clearProject();
		project = new Project();

//change keyboard back to wylie for a second
		DuffPane dp = (DuffPane)pane;
if (keyboard_url != null) {

			dp.registerKeyboard();
		//	project.tName.setupKeyboard();

		//	project.tTask.setupKeyboard();
			sharedDP.setupKeyboard();
			sharedDP2.setupKeyboard();
}


		org.jdom.Document doc = builder.build(t);
		org.jdom.Element qd = doc.getRootElement();
		org.jdom.Element meta = qd.getChild("metadata");
		project.setTitle(meta.getChild("title").getText());

		String mediaString = meta.getChild("mediafile").getText().trim();

		if (!mediaString.equals("")) {
			String tPath = t.getPath().substring(0, t.getPath().lastIndexOf(File.separatorChar)+1);
			if (new File(tPath + mediaString).exists())
				project.setMedia(new File(tPath + mediaString));
			else if (new File(mediaString).exists())
				project.setMedia(new File(mediaString));
			else
				project.setMedia(null);
		} else
			project.setMedia(null);

		work = new ArrayList();
		java.util.List wkUnit = meta.getChild("workhistory").getChildren("work");
		Iterator wkIter = wkUnit.iterator();
		while (wkIter.hasNext()) {
			org.jdom.Element wk = (org.jdom.Element)wkIter.next();
			Work wk2 = new Work();
			wk2.name = wk.getChild("name").getText();
			wk2.task = wk.getChild("task").getText();
			wk2.startDate = wk.getChild("start").getText();
			wk2.duration = wk.getChild("duration").getText();
			work.add(wk2);
		}
		project.setTranscriberName("");
		project.setTranscriberTask("");

		SpeakerData sd = (SpeakerData)speakerTable.getModel();
		sd.removeAllSpeakers();
		java.util.List newSpkrs = meta.getChild("speakers").getChildren("speaker");
		Iterator spIter = newSpkrs.iterator();
		while (spIter.hasNext()) {
			org.jdom.Element speaker = (org.jdom.Element)spIter.next();
			Speaker sp = new Speaker(Integer.parseInt(speaker.getAttributeValue("iconid")), speaker.getText());
			sd.addSpeaker(sp);
		}

		org.jdom.Element text = qd.getChild("text");
//		DuffPane dp = (DuffPane)pane;
		TibetanDocument tDoc = (TibetanDocument)dp.getDocument();
		if (tDoc.getLength() > 0)
			dp.setText("");
		java.util.List textContent = text.getContent();
		ImageIcon[] icons = sd.getSpeakerIcons();
		Iterator textIter = textContent.iterator();
		boolean wasLastComponent = true;
		while (textIter.hasNext()) {
			Object nextContent = textIter.next();
			if (nextContent instanceof org.jdom.Text) {
				String wylie = ((org.jdom.Text)nextContent).getText();
				dp.toTibetanMachineWeb(wylie, tDoc.getLength());
				wasLastComponent = false;
			}
			else if (nextContent instanceof org.jdom.Element) {
				org.jdom.Element e = (org.jdom.Element)nextContent;
				dp.setCaretPosition(tDoc.getLength());
				if (e.getName().equals("time")) {
/*					if (!wasLastComponent)
						try {
							tDoc.insertString(tDoc.getLength(), "\n", null);
						} catch (BadLocationException ble) {
							ble.printStackTrace();
							ThdlDebug.noteIffyCode();
						}
*/
					new TimePoint(dp, clockIcon, Integer.valueOf(e.getAttributeValue("t")));
					wasLastComponent = true;
				}
				else if (e.getName().equals("who")) {
/*					if (!wasLastComponent)
						try {
							tDoc.insertString(tDoc.getLength(), "\n", null);
						} catch (BadLocationException ble) {
							ble.printStackTrace();
							ThdlDebug.noteIffyCode();
						}
*/
					dp.insertComponent(new JLabel("  ", icons[Integer.parseInt(e.getAttributeValue("id"))], SwingConstants.LEFT));
					wasLastComponent = true;
				}
			}
		}
		currentWork = new Work();
		work.add(currentWork);

if (keyboard_url != null) {
		dp.registerKeyboard(keyboard_url);
	//	project.tName.setupKeyboard();
	//	project.tTask.setupKeyboard();
		sharedDP.setupKeyboard();
		sharedDP2.setupKeyboard();
}

project.setTranscript(t);
jtp.addTab(messages.getString("ProjectDetails"), project);
		return true;
	} catch (JDOMException jdome) {
		jdome.printStackTrace();
		ThdlDebug.noteIffyCode();
		return false;
	}
}

class SpeakerManager extends JPanel {
	JPanel top;
	boolean isEditable;

//public JPanel getSpeakerManager(final SpeakerTable sTable) {

SpeakerManager(final SpeakerTable sTable) {

	setLayout(new BorderLayout());

	JButton addButton = new JButton(messages.getString("Add"));
	addButton.addActionListener(new ThdlActionListener() {
		public void theRealActionPerformed(ActionEvent e) {
			SpeakerData sd = (SpeakerData)sTable.getModel();
			JPanel p0 = new JPanel(new GridLayout(0,1));
			JPanel p1 = new JPanel();
			p1.add(new JLabel(messages.getString("FaceColon") + " "));
			JComboBox combo = new JComboBox(sd.getSpeakerIcons());
			p1.add(combo);
			JPanel p2 = new JPanel();
			p2.add(new JLabel(messages.getString("NameColon") + " "));
			sharedDP.setText("");
			sharedDP.setPreferredSize(new Dimension(250,50));
			p2.add(sharedDP);
			p0.add(p1);
			p0.add(p2);
			if (JOptionPane.showConfirmDialog(SpeakerManager.this, p0, messages.getString("EnterNewSpeakerInfo"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null) == JOptionPane.OK_OPTION) {
				TibetanDocument doc = (TibetanDocument)sharedDP.getDocument();
				sd.addSpeaker(new Speaker(combo.getSelectedIndex(), doc.getWylie()));
			}
		}
	});
	JButton editButton = new JButton(messages.getString("Edit"));
	editButton.addActionListener(new ThdlActionListener() {
		public void theRealActionPerformed(ActionEvent e) {
			int row = sTable.getSelectedRow();
			if (row == -1)
				return;
			SpeakerData sd = (SpeakerData)sTable.getModel();
			Speaker sp = sd.getSpeakerAt(row);
			JPanel p0 = new JPanel(new GridLayout(0,1));
			JPanel p1 = new JPanel();
			p1.add(new JLabel(messages.getString("FaceColon") + " "));
			JComboBox combo = new JComboBox(sd.getSpeakerIcons());
			combo.setSelectedIndex(sp.getIconID());
			ImageIcon[] icons = sd.getSpeakerIcons();
			ImageIcon oldIcon = icons[sp.getIconID()];
			p1.add(combo);
			JPanel p2 = new JPanel();
			p2.add(new JLabel(messages.getString("NameColon") + " "));
			sharedDP.setText("");
			sharedDP.setPreferredSize(new Dimension(250,50));
			sharedDP.toTibetanMachineWeb(sp.getName(), 0);
			p2.add(sharedDP);
			p0.add(p1);
			p0.add(p2);
			if (JOptionPane.showConfirmDialog(SpeakerManager.this, p0, messages.getString("EditSpeakerInfo"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null) == JOptionPane.OK_OPTION) {
				sd.setValueAt(new Integer(combo.getSelectedIndex()), row, 0);
				TibetanDocument doc = (TibetanDocument)sharedDP.getDocument();
				sd.setValueAt(doc.getWylie(), row, 1);
				if (!oldIcon.equals(icons[combo.getSelectedIndex()])) {
					DefaultStyledDocument doc2 = (DefaultStyledDocument)pane.getDocument();
					int k = pane.getCaretPosition();
					for (int i=0; i<doc2.getLength(); i++) {
						AttributeSet attr = doc2.getCharacterElement(i).getAttributes();
						Component comp;
						if (null != (comp = StyleConstants.getComponent(attr))) {
							if (comp instanceof JLabel) {
								JLabel l = (JLabel)comp;
								if (oldIcon.equals(l.getIcon())) {
									try {
										doc2.remove(i,1);
									} catch (BadLocationException ble) {
										ble.printStackTrace();
										ThdlDebug.noteIffyCode();
									}
									pane.setCaretPosition(i);
									pane.insertComponent(new JLabel("  ", icons[combo.getSelectedIndex()], SwingConstants.LEFT));
								}
							}
						}
					}
					pane.setCaretPosition(k);
				}
			}
		}
	});
	JButton deleteButton = new JButton(messages.getString("Remove"));
	deleteButton.addActionListener(new ThdlActionListener() {
		public void theRealActionPerformed(ActionEvent e) {
			int row = sTable.getSelectedRow();
			if (row > -1 && JOptionPane.showConfirmDialog(SpeakerManager.this, messages.getString("SureDeleteSpeaker"), messages.getString("Warning"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
				SpeakerData sd = (SpeakerData)sTable.getModel();
				Speaker sp = sd.getSpeakerAt(row);
				ImageIcon[] icons = sd.getSpeakerIcons();
				DefaultStyledDocument doc = (DefaultStyledDocument)pane.getDocument();
				int k2 = doc.getLength();
				for (int i=0; i<k2; i++) {
					AttributeSet attr = doc.getCharacterElement(i).getAttributes();
					Component comp;
					if (null != (comp = StyleConstants.getComponent(attr))) {
						if (comp instanceof JLabel) {
							JLabel l = (JLabel)comp;
							if (icons[sp.getIconID()].equals(l.getIcon())) {
								try {
									doc.remove(i,1);
									k2--;
								} catch (BadLocationException ble) {
									ble.printStackTrace();
									ThdlDebug.noteIffyCode();
								}
							}
						}
					}
				}
				pane.setCaretPosition(0);
				sd.removeSpeaker(row);
			}
		}
	});
	top = new JPanel();
	top.add(addButton);
	top.add(editButton);
	top.add(deleteButton);
	add("North", top);
	add("Center", new JScrollPane(sTable));
	isEditable = true;
}

public void setEditable(boolean bool) {
	if (bool) {
		if (!isEditable) {
			add("North", top);
			isEditable = true;
			invalidate();
			validate();
			repaint();
		}
	} else {
		if (isEditable) {
			remove(top);
			isEditable = false;
			invalidate();
			validate();
			repaint();
		}
	}
}

}

class Speaker {
	private String name;
	private int iconID;

	public Speaker(int iconID, String name) {
		setIconID(iconID);
		setName(name);
	}
	public String toString() {
		return name;
	}
	public String getName() {
		return name;
	}
	public int getIconID() {
		return iconID;
	}
	public void setName(String newName) {
		name = new String(newName);
	}
	public void setIconID(int newID) {
		iconID = newID;
	}
}

class SpeakerData extends AbstractTableModel
{
	java.util.List speakers = new ArrayList();
	ImageIcon[] spIcon = new ImageIcon[13];
	Map iconMap = new HashMap();

	public SpeakerData(Speaker[] speaker, Keymap keymap)
	{
		spIcon[0] = new ImageIcon(QD.class.getResource("face01a.jpg"));
		spIcon[1] = new ImageIcon(QD.class.getResource("face02a.jpg"));
		spIcon[2] = new ImageIcon(QD.class.getResource("face03a.jpg"));
		spIcon[3] = new ImageIcon(QD.class.getResource("face04a.jpg"));
		spIcon[4] = new ImageIcon(QD.class.getResource("face05a.jpg"));
		spIcon[5] = new ImageIcon(QD.class.getResource("face06a.jpg"));
		spIcon[6] = new ImageIcon(QD.class.getResource("face07a.jpg"));
		spIcon[7] = new ImageIcon(QD.class.getResource("face08a.jpg"));
		spIcon[8] = new ImageIcon(QD.class.getResource("face09a.jpg"));
		spIcon[9] = new ImageIcon(QD.class.getResource("face10a.jpg"));
		spIcon[10] = new ImageIcon(QD.class.getResource("face11a.jpg"));
		spIcon[11] = new ImageIcon(QD.class.getResource("face12a.jpg"));
		spIcon[12] = new ImageIcon(QD.class.getResource("face13a.jpg"));

		for (int k=0; k<speaker.length; k++)
			addSpeaker(speaker[k]);

		Action insertSpeakerAction = new ThdlAbstractAction("insertSpeaker", null) {
			public void theRealActionPerformed(ActionEvent e) {
				if (speakers.size() == 0)
					return;
				JTextPane tp = (JTextPane)e.getSource();
				int pos = tp.getCaretPosition();
				if (pos > 0) {
					DefaultStyledDocument doc = (DefaultStyledDocument)tp.getDocument();
					AttributeSet attr = doc.getCharacterElement(pos-1).getAttributes();
					Component comp;
					if (null != (comp = StyleConstants.getComponent(attr)))
						if (comp instanceof JLabel) {
							JLabel l = (JLabel)comp;
							ImageIcon ic = (ImageIcon)l.getIcon();
							if (iconMap.containsKey(ic)) {
								Speaker sp = (Speaker)iconMap.get(ic);
								int k = speakers.indexOf(sp) + 1;
								if (k == speakers.size())
									k=0;
								try {
									tp.getDocument().remove(pos-1, 1);
									sp = (Speaker)speakers.get(k);
									tp.insertComponent(new JLabel("  ", spIcon[sp.getIconID()], SwingConstants.LEFT));
									return;
								} catch (BadLocationException ble) {
									ble.printStackTrace();
									ThdlDebug.noteIffyCode();
								}
							}
						}
				}
				Speaker sp = (Speaker)speakers.get(0);
				tp.insertComponent(new JLabel("  ", spIcon[sp.getIconID()], SwingConstants.LEFT));
			}
		};
		keymap.addActionForKeyStroke(insertSpeakerKey, insertSpeakerAction);
	}
	public boolean addSpeaker(Speaker speaker) {
		if (iconMap.containsKey(spIcon[speaker.getIconID()]))
			return false;
		else {
			iconMap.put(spIcon[speaker.getIconID()], speaker);
			speakers.add(speaker);
			fireTableRowsInserted(speakers.size()-1, speakers.size()-1);
			return true;
		}
	}
	public Speaker getSpeakerAt(int row) {
		if (row > -1 && row < speakers.size())
			return ((Speaker)speakers.get(row));
		return null;
	}
	public java.util.List getSpeakers() {
		return speakers;
	}
	public ImageIcon[] getSpeakerIcons() {
		return spIcon;
	}
	public Speaker getSpeakerForIcon(Icon icon) {
		if (iconMap.containsKey(icon))
			return (Speaker)iconMap.get(icon);
		else
			return null;
	}
	public void removeAllSpeakers() {
		speakers.clear();
		iconMap.clear();		
	}
	public void removeSpeaker(int row)
	{
		if (row > -1 && row < speakers.size())
		{
			Speaker sp = getSpeakerAt(row);
			iconMap.remove(spIcon[sp.getIconID()]);
			speakers.remove(row);
			fireTableRowsDeleted(row,row);
		}
	}
	public int getRowCount()
	{
		return speakers.size();
	}	
	public int getColumnCount()
	{
		return 2;
	}
	public Class getColumnClass(int c)
	{
		return getValueAt(0, c).getClass();
	}
	public Object getValueAt(int row, int column)
	{
		Speaker sp = (Speaker)speakers.get(row);
		if (column == 0)
			return spIcon[sp.getIconID()];
		try { //otherwise column 1, the speaker name
			return TibetanDocument.getTibetanMachineWeb(sp.getName().trim());
		} catch (InvalidWylieException iwe) {
			iwe.printStackTrace();
			ThdlDebug.noteIffyCode();
			return null;
		}
	}
	public void setValueAt(Object object, int row, int column) {
		Speaker sp = (Speaker)speakers.get(row);
		if (column == 0 && object instanceof Integer) {
			Integer bigInt = (Integer)object;
			int k = bigInt.intValue();
			if (!iconMap.containsKey(spIcon[k])) {
				iconMap.remove(spIcon[sp.getIconID()]);
				iconMap.put(spIcon[k], sp);
				sp.setIconID(k);
			}
		} else if (object instanceof String) {
			String wylie = (String)object;
			sp.setName(wylie);
		}
		fireTableCellUpdated(row, column);
	}
}

class SpeakerTable extends JTable
{
	private TableCellRenderer duffRenderer, normalRenderer;
	private SpeakerData speakerData;

	public SpeakerTable(SpeakerData speakerData)
	{	
		this.speakerData = speakerData;
		this.setModel(this.speakerData);
		this.setRowHeight(55);
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TableColumnModel tcm = this.getColumnModel();
		duffRenderer = new DuffCellRenderer();

		TableColumn tc = tcm.getColumn(0);
		tc.setResizable(false);
		tc.setMaxWidth(60);
		tc.setMinWidth(60);
		tc.setHeaderValue(messages.getString("Face"));

		tc = tcm.getColumn(1);
		tc.setCellRenderer(duffRenderer);
		tc.setHeaderValue(messages.getString("Name"));
	}

}

public JMenuBar getTextMenuBar() {
	JMenu modeMenu = new JMenu(messages.getString("Mode"));
	JRadioButton editButton = new JRadioButton(messages.getString("Edit"));
	JRadioButton viewButton = new JRadioButton(messages.getString("ReadOnly"));
	editButton.setActionCommand("edit");
	viewButton.setActionCommand("view");
	RadioListener l = new RadioListener();
	editButton.addActionListener(l);
	viewButton.addActionListener(l);
	editButton.setSelected(true);
	ButtonGroup bg = new ButtonGroup();
	bg.add(editButton);
	bg.add(viewButton);
	JPanel buttons = new JPanel(new GridLayout(0,1));
	buttons.add(editButton);
	buttons.add(viewButton);
	modeMenu.add(buttons);

	JMenu viewMenu = new JMenu(messages.getString("View"));
	JMenuItem previousItem = new JMenuItem(messages.getString("Previous"));
	JMenuItem currentItem = new JMenuItem(messages.getString("Current"));
	JMenuItem nextItem = new JMenuItem(messages.getString("Next"));
	JMenuItem suppressTimesItem = new JMenuItem(messages.getString("SuppressTimes"));
	viewMenu.add(previousItem);
	viewMenu.add(currentItem);
	viewMenu.add(nextItem);
	viewMenu.addSeparator();
	viewMenu.add(suppressTimesItem);

	searchMenu = new JMenu(messages.getString("Search"));
	JMenuItem findTextItem = new JMenuItem(messages.getString("FindText"));
	findTextItem.setAccelerator(findKey);
	findTextItem.addActionListener(new ThdlActionListener() {
		public void theRealActionPerformed(ActionEvent e) {
			findText();
		}
	});

	replaceTextItem = new JMenuItem(messages.getString("ReplaceText"));
	replaceTextItem.setAccelerator(replaceKey);
	replaceTextItem.addActionListener(new ThdlActionListener() {
		public void theRealActionPerformed(ActionEvent e) {
			replaceText();
		}
	});

	JMenuItem findspeakerItem = new JMenuItem(messages.getString("FindSpeaker"));
	JMenuItem findtimeItem = new JMenuItem(messages.getString("FindTime"));
	searchMenu.add(findTextItem);
	searchMenu.add(replaceTextItem);
	searchMenu.addSeparator();
	searchMenu.add(findspeakerItem);
	searchMenu.add(findtimeItem);

/* this is a bit odd.
   since all the following keys are already associated with
   actions via the keymap, these menu items don't need to actually do anything.
   however, i have added the accelerators so that the keyboard shortcuts
   are shown next to the menu items. */

	editMenu = new JMenu(messages.getString("Edit"));
	JMenuItem cutItem = new JMenuItem(messages.getString("Cut"));
	cutItem.setAccelerator(cutKey);
	JMenuItem copyItem = new JMenuItem(messages.getString("Copy"));
	copyItem.setAccelerator(copyKey);
	JMenuItem pasteItem = new JMenuItem(messages.getString("Paste"));
	pasteItem.setAccelerator(pasteKey);
	JMenuItem selectallItem = new JMenuItem(messages.getString("SelectAll"));
	selectallItem.setAccelerator(selectAllKey);
	JMenuItem insertspeakerItem = new JMenuItem(messages.getString("InsertSpeaker"));
	insertspeakerItem.setAccelerator(insertSpeakerKey);
	JMenuItem insertonetimeItem = new JMenuItem(messages.getString("InsertOneTime"));
	insertonetimeItem.setAccelerator(insert1TimeKey);
	JMenuItem inserttwotimesItem = new JMenuItem(messages.getString("InsertTwoTimes"));	
	inserttwotimesItem.setAccelerator(insert2TimesKey);

	editMenu.add(cutItem);
	editMenu.add(copyItem);
	editMenu.add(pasteItem);
	editMenu.add(selectallItem);
	editMenu.addSeparator();
	editMenu.add(insertspeakerItem);
	editMenu.add(insertonetimeItem);
	editMenu.add(inserttwotimesItem);

	JMenuBar bar = new JMenuBar();
	bar.add(modeMenu);
	bar.add(viewMenu);
	bar.add(searchMenu);
	bar.add(editMenu);
	return bar;
}

public int findNextText(int startPos, TibetanDocument sourceDoc, TibetanDocument findDoc) {
	if (startPos<0 || startPos>sourceDoc.getLength()-1)
		return -1;

try {

	AttributeSet firstAttr = findDoc.getCharacterElement(0).getAttributes();
	String firstFontName = StyleConstants.getFontFamily(firstAttr);
	char firstSearchChar = findDoc.getText(0,1).charAt(0);

	boolean match = false;
	for (int i=startPos; i<sourceDoc.getLength()-findDoc.getLength(); i++) {
			AttributeSet attr = sourceDoc.getCharacterElement(i).getAttributes();
			String fontName = StyleConstants.getFontFamily(attr);
			char c = sourceDoc.getText(i,1).charAt(0);

			if (c == firstSearchChar && fontName.equals(firstFontName)) { //found first character, now check subsequents
				match = true;
				for (int k=1; k<findDoc.getLength() && match; k++) {
					if (findDoc.getText(k,1).charAt(0) == sourceDoc.getText(i+k,1).charAt(0)) {
	 					AttributeSet	attr2 = findDoc.getCharacterElement(k).getAttributes();
									attr = sourceDoc.getCharacterElement(i+k).getAttributes();
						if (!StyleConstants.getFontFamily(attr).equals(StyleConstants.getFontFamily(attr2)))
							match = false;
					} else
						match = false;
				}
				if (match)
					return i;
			}
	}
	} catch (BadLocationException ble) {
		ble.printStackTrace();
		ThdlDebug.noteIffyCode();
	}

	return -1; //not found
}

public void findText() {
	//get input from user on what to search for
	JPanel p0 = new JPanel(new GridLayout(0,1));
	JPanel p2 = new JPanel();
	p2.add(new JLabel(messages.getString("FindWhat") + " "));
	if (findDoc == null) {
		sharedDP.setText("");
		findDoc = (TibetanDocument)sharedDP.getDocument();
	} else
		sharedDP.setDocument(findDoc);
	sharedDP.setPreferredSize(new Dimension(250,50));
	p2.add(sharedDP);
	p0.add(p2);

	if (JOptionPane.showConfirmDialog(textFrame, p0, messages.getString("FindHeading"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null) == JOptionPane.OK_OPTION) {

	try {
		TibetanDocument doc = (TibetanDocument)pane.getDocument();
		Position pos = doc.createPosition(pane.getCaretPosition());

		boolean startingOver = false;
		int i=pos.getOffset();
		while (true) {
			i = findNextText(i, doc, findDoc);
			if (i>0) { //found!! so highlight text and seek video
				if (startingOver && i>pos.getOffset()-1)
					break;
				pane.setSelectionStart(i);
				pane.setSelectionEnd(i+findDoc.getLength());
				if (JOptionPane.showInternalConfirmDialog(textFrame, messages.getString("FindNextYesNo"), null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null) != JOptionPane.YES_OPTION)
					break;
				i++;
			}
			if (i<0 || i==doc.getLength() && pos.getOffset()>0) { //reached end of document - start over?
				if (startingOver || pos.getOffset()==0)
					break;
				if (JOptionPane.showInternalConfirmDialog(textFrame, messages.getString("StartFromBeginning"), null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null) != JOptionPane.YES_OPTION)
					break;
				i=0;
				startingOver = true;
			}
		}
	} catch (BadLocationException ble) {
		ble.printStackTrace();
		ThdlDebug.noteIffyCode();
	}

	}
}

public void replaceText() {
	//get input from user on what to search for
	JPanel p0 = new JPanel(new GridLayout(0,1));
	JPanel p2 = new JPanel();
	p2.add(new JLabel(messages.getString("FindWhat") + " "));
	if (findDoc == null) {
		sharedDP.setText("");
		findDoc = (TibetanDocument)sharedDP.getDocument();
	} else
		sharedDP.setDocument(findDoc);
	sharedDP.setPreferredSize(new Dimension(250,50));
	p2.add(sharedDP);
	JPanel p3 = new JPanel();
	p3.add(new JLabel(messages.getString("ReplaceWith") + " "));
	if (replaceDoc == null) {
		sharedDP2.setText("");
		replaceDoc = (TibetanDocument)sharedDP2.getDocument();
	} else
		sharedDP2.setDocument(replaceDoc);
	sharedDP2.setPreferredSize(new Dimension(250,50));
	p3.add(sharedDP2);
	
	p0.add(p2);
	p0.add(p3);

	if (JOptionPane.showConfirmDialog(textFrame, p0, messages.getString("FindReplaceHeading"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null) == JOptionPane.OK_OPTION) {

	try {
		java.util.List replaceDCs = new ArrayList();
		for (int k=0; k<replaceDoc.getLength(); k++)
			replaceDCs.add(new DuffCode(TibetanMachineWeb.getTMWFontNumber(StyleConstants.getFontFamily(replaceDoc.getCharacterElement(k).getAttributes())), replaceDoc.getText(k,1).charAt(0)));
		TibetanDocument.DuffData[] dd = TibetanDocument.convertGlyphs(replaceDCs);

		TibetanDocument doc = (TibetanDocument)pane.getDocument();
		Position pos = doc.createPosition(pane.getCaretPosition());

		String[] replaceOptions = new String[3];
		replaceOptions[0] = new String(messages.getString("Replace"));
		replaceOptions[1] = new String(messages.getString("ReplaceAll"));
		replaceOptions[2] = new String(messages.getString("NoReplace"));

		boolean startingOver = false;
		boolean replaceAll = false;
		int i=pos.getOffset();
		outer:
		while (true) {
			i = findNextText(i, doc, findDoc);
			if (i>0) { //found!! so highlight text and seek video
				if (startingOver && i>pos.getOffset()-1)
					break;
				if (replaceAll) {
					doc.remove(i,findDoc.getLength());
					doc.insertDuff(i,dd);
				} else {
					pane.setSelectionStart(i);
					pane.setSelectionEnd(i+findDoc.getLength());
					String choice = (String)JOptionPane.showInternalInputDialog(textFrame, messages.getString("ReplaceQ"), null, JOptionPane.PLAIN_MESSAGE, null, replaceOptions, replaceOptions[0]);
					if (choice == null) //cancel, so stop searching
						break outer;
					if (choice.equals(replaceOptions[0])) { //replace
						doc.remove(i,findDoc.getLength());
						doc.insertDuff(i,dd);
					}
					else if (choice.equals(replaceOptions[1])) { //replace all
						doc.remove(i,findDoc.getLength());
						doc.insertDuff(i,dd);
						replaceAll = true;
					}
				}
				i++;
			}
			if (i<0 || i==doc.getLength() && pos.getOffset()>0) { //reached end of document - start over?
				if (startingOver || pos.getOffset()==0)
					break;
				if (JOptionPane.showInternalConfirmDialog(textFrame, messages.getString("StartFromBeginning"), null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null) != JOptionPane.YES_OPTION)
					break;
				i=0;
				startingOver = true;
			}
		}
	} catch (BadLocationException ble) {
		ble.printStackTrace();
		ThdlDebug.noteIffyCode();
	}

	}
}

class RadioListener extends ThdlActionListener {
	public void theRealActionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("edit")) {
			//make the text pane editable
			pane.setEditable(true);

			//reinstate the edit menu to the pane
			textFrame.getJMenuBar().add(editMenu);
			searchMenu.add(replaceTextItem, 1);
			textFrame.invalidate();
			textFrame.validate();
			textFrame.repaint();

			//add speaker-edit features
			spm.setEditable(true);

			//add the time-coding tab
			if (tcp != null)
				jtp.addTab(messages.getString("TimeCoding"), tcp);

			//make the project details editable
			if (project != null)
//				project.setEditable(true);

				jtp.addTab(messages.getString("ProjectDetails"), project);
		}
		else if (e.getActionCommand().equals("view")) {
			//make the text pane non-editable
			pane.setEditable(false);

			//remove the edit menu from the pane
			textFrame.getJMenuBar().remove(editMenu);
			searchMenu.remove(replaceTextItem);
			textFrame.invalidate();
			textFrame.validate();
			textFrame.repaint();

			//remove speaker-edit features
			spm.setEditable(false);

			//remove the time-coding tab
			if (tcp != null)
				jtp.remove(tcp);

			//make the project tab non-editable
			if (project != null)
				jtp.remove(project);
//				project.setEditable(false);
		}
	}
}
}
