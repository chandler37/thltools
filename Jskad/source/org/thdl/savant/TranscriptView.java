package org.thdl.savant;

public interface TranscriptView
{
	public String getTitle();
	public javax.swing.text.JTextComponent getTextComponent();
	public String getIDs();
	public String getT1s();
	public String getT2s();
	public String getStartOffsets();
	public String getEndOffsets();
	public org.jdom.Document getDocument();
}