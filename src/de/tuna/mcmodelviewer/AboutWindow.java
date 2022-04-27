package de.tuna.mcmodelviewer;

import javax.swing.JFrame;

public class AboutWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8473884500974723483L;

	public AboutWindow() {
		super("About");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setType(Type.POPUP);
		setResizable(false);
		
	}
	
}
