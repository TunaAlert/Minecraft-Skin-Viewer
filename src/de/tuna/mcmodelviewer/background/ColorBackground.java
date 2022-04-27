package de.tuna.mcmodelviewer.background;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.tuna.mcmodelviewer.Launcher;
import de.tuna.mcmodelviewer.ModelViewer;

public class ColorBackground extends Background {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8994856882894104772L;

	private JButton colorButton;
	private JColorChooser colorChooser;
	private JFrame colorChooserFrame;
	
	public ColorBackground(Launcher launcher) {
		super(launcher);
		colorButton = new JButton();
		colorButton.setMargin(new Insets(2,2,2,2));
		add(new JLabel("Background color"));
		add(colorButton);
		
		colorChooserFrame = new JFrame("pick background color");
		colorChooserFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		colorChooserFrame.setType(Type.POPUP);
		
		colorChooser = new JColorChooser(getBackground());
		
		colorChooserFrame.add(colorChooser);
		colorChooserFrame.pack();
		colorChooserFrame.setAlwaysOnTop(true);
		colorChooserFrame.setVisible(false);
		colorChooserFrame.setLocationRelativeTo(ColorBackground.this);
		
		colorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorChooserFrame.setVisible(true);
				colorChooserFrame.requestFocus();
			}
		});
		
		colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				apply();
			}
		});
		setButtonColor();
	}
	
	private void setButtonColor() {
		BufferedImage img = new BufferedImage(30, 12, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setColor(new Color(colorChooser.getColor().getRGB() | 0xFF000000));
		g.fillRect(0, 0, 32, 16);
		colorButton.setIcon(new ImageIcon(img));
	}
	
	@Override
	public String getName() {
		return "Color";
	}

	@Override
	public void applyBackground(ModelViewer modelViewer) {
		setButtonColor();
		modelViewer.setBackground(colorChooser.getColor());
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(!visible) {
			colorChooserFrame.setVisible(false);
		}
	}

}
