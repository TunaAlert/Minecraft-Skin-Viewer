package de.tuna.mcmodelviewer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.tuna.mcmodelviewer.animation.AnimationList;

public class Launcher extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2673901456683692815L;

	public MyMenu menuBar;
	public Sidebar sidebar;
	
	public JSplitPane hSplit;
	public ModelViewer modelviewer;

	public String customTitle = "Minecraft skin viewer";
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		AnimationList.exportDefaultAnimations();
		AnimationList.loadAnimations();
		
		final Launcher launcher = new Launcher();
		launcher.init();
	}
	
	private void init() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setLocationRelativeTo(null);

		hSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		setContentPane(hSplit);
		
		hSplit.setDividerSize(5);
		hSplit.setEnabled(false);
		
		modelviewer = new ModelViewer(this);

		hSplit.setRightComponent(modelviewer);
		
		updateTitle();
		
		menuBar = new MyMenu(this);
		setJMenuBar(menuBar);
		
		sidebar = new Sidebar(this);
		sidebar.setVisible(false);
		
		JPanel leftHSplit = new JPanel();
		leftHSplit.setLayout(new BoxLayout(leftHSplit, BoxLayout.Y_AXIS));
		
		hSplit.setLeftComponent(leftHSplit);
		leftHSplit.add(sidebar);
		
		leftHSplit.add(Box.createVerticalStrut(2000));
		
		setVisible(true);

		new Thread(new Runnable() {
			public void run() {
				modelviewer.init();
				modelviewer.run();
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					updateTitle();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void updateTitle() {
		if(modelviewer.getTextureSource() == ModelViewer.TEXTURE_SOURCE_FILE)
			setTitle(modelviewer.getFile().getName() + " | MCSV");
		else
			setTitle(customTitle + " | MCSV");
		
		BufferedImage texture = modelviewer.getImage();
		if(texture == null) return;
		BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
		Graphics g = icon.getGraphics();
		int faceSize = texture.getWidth()/8;
		g.drawImage(texture.getSubimage(faceSize, faceSize, faceSize, faceSize), 0, 0, 32, 32, null);
		g.drawImage(texture.getSubimage(5*faceSize, faceSize, faceSize, faceSize), 0, 0, 32, 32, null);
		setIconImage(icon);
	}
	
}
