package org.thdl.savant;

public interface AnnotationPlayer extends java.util.EventListener
{
	public void startAnnotation(String id);
	public void stopAnnotation(String id);
}