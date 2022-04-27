package de.tuna.mcmodelviewer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import de.tuna.mcmodelviewer.background.Background;
import de.tuna.mcmodelviewer.background.ColorBackground;
import de.tuna.mcmodelviewer.background.ImageBackground;
import de.tuna.mcmodelviewer.background.SkyboxBackground;
import de.tuna.mcmodelviewer.model.PlayerModelData;

public class Sidebar extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7764651098851058930L;
	
	JCheckBox torsoVisible;
	JCheckBox headVisible;
	JCheckBox leftArmVisible;
	JCheckBox rightArmVisible;
	JCheckBox leftLegVisible;
	JCheckBox rightLegVisible;

	JCheckBox jacketVisible;
	JCheckBox hatVisible;
	JCheckBox leftSleeveVisible;
	JCheckBox rightSleeveVisible;
	JCheckBox leftPantsVisible;
	JCheckBox rightPantsVisible;
	
	private final Launcher launcher;
	
	public Sidebar(Launcher launcher) {
		this.launcher = launcher;
		initDialog();
	}

	private void initDialog() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.LOWERED));
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
		
		JLabel titleLabel = new JLabel("View settings");
		titlePanel.add(titleLabel);
		
		JButton closeButton = new JButton(UIManager.getIcon("InternalFrame.closeIcon"));
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				launcher.hSplit.resetToPreferredSizes();
			}
		});
		titlePanel.add(closeButton);
		
		add(titlePanel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(0, 3));
		
		buttonPanel.add(Box.createHorizontalBox());
		buttonPanel.add(new JLabel("inner"));
		buttonPanel.add(new JLabel("outer"));
		
		JLabel headLabel = new JLabel("Head");
		headVisible = new JCheckBox();
		headVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.modelviewer.getModelData().setHeadVisible(headVisible.isSelected());
				updateCheckBoxes();
			}
		});
		hatVisible = new JCheckBox();
		hatVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.modelviewer.getModelData().setHatVisible(hatVisible.isSelected());
				updateCheckBoxes();
			}
		});
		buttonPanel.add(headLabel);
		buttonPanel.add(headVisible);
		buttonPanel.add(hatVisible);
		headVisible.setSelected(true);
		hatVisible.setSelected(true);

		JLabel torsoLabel = new JLabel("Chest");
		torsoVisible = new JCheckBox();
		torsoVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.modelviewer.getModelData().setTorsoVisible(torsoVisible.isSelected());
				updateCheckBoxes();
			}
		});
		jacketVisible = new JCheckBox();
		jacketVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.modelviewer.getModelData().setJacketVisible(jacketVisible.isSelected());
				updateCheckBoxes();
			}
		});
		buttonPanel.add(torsoLabel);
		buttonPanel.add(torsoVisible);
		buttonPanel.add(jacketVisible);
		torsoVisible.setSelected(true);
		jacketVisible.setSelected(true);

		JLabel rightArmLabel = new JLabel("Right arm");
		rightArmVisible = new JCheckBox();
		rightArmVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.modelviewer.getModelData().setRightArmVisible(rightArmVisible.isSelected());
				updateCheckBoxes();
			}
		});
		rightSleeveVisible = new JCheckBox();
		rightSleeveVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.modelviewer.getModelData().setRightSleeveVisible(rightSleeveVisible.isSelected());
				updateCheckBoxes();
			}
		});
		buttonPanel.add(rightArmLabel);
		buttonPanel.add(rightArmVisible);
		buttonPanel.add(rightSleeveVisible);
		rightArmVisible.setSelected(true);
		rightSleeveVisible.setSelected(true);

		JLabel leftArmLabel = new JLabel("Left arm");
		leftArmVisible = new JCheckBox();
		leftArmVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.modelviewer.getModelData().setLeftArmVisible(leftArmVisible.isSelected());
				updateCheckBoxes();
			}
		});
		leftSleeveVisible = new JCheckBox();
		leftSleeveVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.modelviewer.getModelData().setLeftSleeveVisible(leftSleeveVisible.isSelected());
				updateCheckBoxes();
			}
		});
		buttonPanel.add(leftArmLabel);
		buttonPanel.add(leftArmVisible);
		buttonPanel.add(leftSleeveVisible);
		leftArmVisible.setSelected(true);
		leftSleeveVisible.setSelected(true);

		JLabel rightLegLabel = new JLabel("Right leg");
		rightLegVisible = new JCheckBox();
		rightLegVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.modelviewer.getModelData().setRightLegVisible(rightLegVisible.isSelected());
				updateCheckBoxes();
			}
		});
		rightPantsVisible = new JCheckBox();
		rightPantsVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.modelviewer.getModelData().setRightPantsVisible(rightPantsVisible.isSelected());
				updateCheckBoxes();
			}
		});
		buttonPanel.add(rightLegLabel);
		buttonPanel.add(rightLegVisible);
		buttonPanel.add(rightPantsVisible);
		rightLegVisible.setSelected(true);
		rightPantsVisible.setSelected(true);

		JLabel leftLegLabel = new JLabel("Left leg");
		leftLegVisible = new JCheckBox();
		leftLegVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.modelviewer.getModelData().setLeftLegVisible(leftLegVisible.isSelected());
				updateCheckBoxes();
			}
		});
		leftPantsVisible = new JCheckBox();
		leftPantsVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.modelviewer.getModelData().setLeftPantsVisible(leftPantsVisible.isSelected());
				updateCheckBoxes();
			}
		});
		buttonPanel.add(leftLegLabel);
		buttonPanel.add(leftLegVisible);
		buttonPanel.add(leftPantsVisible);
		leftLegVisible.setSelected(true);
		leftPantsVisible.setSelected(true);

		add(buttonPanel);
		
		add(Box.createVerticalStrut(20));
		
		JPanel backgroundPanel = new JPanel();
		backgroundPanel.setLayout(new BorderLayout());
		
		JPanel bgTitle = new JPanel();
		bgTitle.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
		bgTitle.add(new JLabel("Background"));
		backgroundPanel.add(bgTitle, BorderLayout.NORTH);
		
		Background colorBG = new ColorBackground(launcher);

		Background imageBG = new ImageBackground(launcher);

		Background skyboxBG = new SkyboxBackground(launcher);

		Background envBG = new Background(launcher) {
			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return "Environment";
			}

			@Override
			public void applyBackground(ModelViewer modelViewer) {
				// TODO Auto-generated method stub
				
			}
		};
		
		Background[] backgrounds = new Background[] {
				colorBG,
				imageBG,
				skyboxBG,
				envBG
		};
		
		JComboBox<Background> bgSelect = new JComboBox<Background>(backgrounds);
		
		bgSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object sel = bgSelect.getSelectedItem();
				if(sel instanceof Background) {
					Background selectedBG = (Background) sel;
					selectedBG.apply();
					for (int i = 0; i < backgrounds.length; i++) {
						boolean vis = backgrounds[i] == selectedBG;
						backgrounds[i].setVisible(vis);
					}
				}
			}
		});
		
		backgroundPanel.add(bgSelect, BorderLayout.NORTH);
		JPanel bgsPanel = new JPanel();
		backgroundPanel.add(bgsPanel, BorderLayout.WEST);

		for (int i = 0; i < backgrounds.length; i++) {
			bgsPanel.add(backgrounds[i]);
			backgrounds[i].setVisible(i == 0);
		}
		bgsPanel.add(Box.createHorizontalGlue());
		
		add(backgroundPanel);
		
		doLayout();
	}
	
	private void updateCheckBoxes() {
		PlayerModelData data = launcher.modelviewer.getModelData();
		
		torsoVisible.setSelected(data.isTorsoVisible());
		headVisible.setSelected(data.isHeadVisible());
		leftArmVisible.setSelected(data.isLeftArmVisible());
		rightArmVisible.setSelected(data.isRightArmVisible());
		leftLegVisible.setSelected(data.isLeftLegVisible());
		rightLegVisible.setSelected(data.isRightLegVisible());

		jacketVisible.setEnabled(data.isTorsoVisible());
		hatVisible.setEnabled(data.isHeadVisible());
		leftSleeveVisible.setEnabled(data.isLeftArmVisible());
		rightSleeveVisible.setEnabled(data.isRightArmVisible());
		leftPantsVisible.setEnabled(data.isLeftLegVisible());
		rightPantsVisible.setEnabled(data.isRightLegVisible());
	}
	
}