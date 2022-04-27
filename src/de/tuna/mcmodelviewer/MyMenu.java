package de.tuna.mcmodelviewer;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import de.tuna.mcmodelviewer.animation.Animation;
import de.tuna.mcmodelviewer.animation.AnimationList;
import de.tuna.mcmodelviewer.model.PlayerModelType;

public class MyMenu extends JMenuBar{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4816365456445560549L;
	
	private static final FileFilter pngFilter = new FileFilter() {
		public String getDescription() {
			return null;
		}
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().matches(".*\\.(p|P)(n|N)(g|G)$");
		}
	};
	
	private static AboutWindow aboutWindow = new AboutWindow();

	private final Launcher launcher;
	
	private JMenu load;
	private JMenu view;
	private JMenu pose;
	private JMenu window;
	
	private ButtonGroup modelSelectionGroup;
	public JRadioButtonMenuItem steveButton;
	public JRadioButtonMenuItem alexButton;
	
	public JMenu anim;
	
	public MyMenu(Launcher launcher) {
		this.launcher = launcher;
		initLoad();
		initView();
		initPose();
		initWindow();
	}

	private void initLoad() {
		load = new JMenu("Load");
		load.setMnemonic(KeyEvent.VK_L);
		add(load);
		JMenuItem loadFile = new JMenuItem("File", KeyEvent.VK_F);
		loadFile.setIcon(UIManager.getIcon("FileView.fileIcon"));
		loadFile.setAccelerator(KeyStroke.getKeyStroke("control O"));
		loadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				if(launcher.modelviewer.getFile() != null)
					jfc.setCurrentDirectory(launcher.modelviewer.getFile().getParentFile());
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setFileFilter(pngFilter);
				jfc.showOpenDialog(launcher);
				if(jfc.getSelectedFile() != null) {
					launcher.modelviewer.setFile(jfc.getSelectedFile());
					launcher.updateTitle();
				}
			}
		});
		load.add(loadFile);
		JMenuItem loadPlayerSkin = new JMenuItem("Player Skin", KeyEvent.VK_S);
		loadPlayerSkin.setIcon(UIManager.getIcon("FileView.computerIcon"));
		loadPlayerSkin.setAccelerator(KeyStroke.getKeyStroke("control S"));
		loadPlayerSkin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JPanel playerInputPanel = new JPanel();
				playerInputPanel.setLayout(new BoxLayout(playerInputPanel, BoxLayout.Y_AXIS));
				playerInputPanel.add(new JLabel("Player name or uuid"));
				JTextField jtf = new JTextField(36);
				playerInputPanel.add(jtf);
				
				int option = JOptionPane.showConfirmDialog(launcher, playerInputPanel, "Load player skin", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				
				if(option == JOptionPane.OK_OPTION) {
					String input = jtf.getText().toLowerCase();
					PlayerSkinLoader.loadPlayerSkin(input, launcher);
				}
			}
		});
		load.add(loadPlayerSkin);
		JMenuItem loadFromClipboard = new JMenuItem("Load from Clipboard");
		loadFromClipboard.setAccelerator(KeyStroke.getKeyStroke("control V"));
		loadFromClipboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.modelviewer.setTransferable(Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null));
				launcher.modelviewer.setTextureSource(ModelViewer.TEXTURE_SOURCE_TRANSFERABLE);
				launcher.customTitle = "Pasted skin";
				launcher.updateTitle();
			}
		});
		load.add(loadFromClipboard);
	}

	private void initView() {
		view = new JMenu("View");
		view.setMnemonic(KeyEvent.VK_V);
		add(view);
		JMenuItem showHideParts = new JMenuItem("Show/Hide sidebar", KeyEvent.VK_H);
		showHideParts.setAccelerator(KeyStroke.getKeyStroke("H"));
		showHideParts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.sidebar.setVisible(!launcher.sidebar.isVisible());
				launcher.hSplit.resetToPreferredSizes();
			}
		});
		view.add(showHideParts);
		JMenu modelType = new JMenu("Model type");
		modelType.setMnemonic(KeyEvent.VK_M);
		steveButton = new JRadioButtonMenuItem("Steve", false);
		modelSelectionGroup = new ButtonGroup();
		modelSelectionGroup.add(steveButton);
		steveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(steveButton.isSelected()) {
					launcher.modelviewer.setModelType(PlayerModelType.STEVE);
					steveButton.setAccelerator(null);
					alexButton.setAccelerator(KeyStroke.getKeyStroke("M"));
				}
			}
		});
		steveButton.setMnemonic(KeyEvent.VK_S);
		alexButton = new JRadioButtonMenuItem("Alex", true);
		modelSelectionGroup.add(alexButton);
		alexButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(alexButton.isSelected()) {
					launcher.modelviewer.setModelType(PlayerModelType.ALEX);
					alexButton.setAccelerator(null);
					steveButton.setAccelerator(KeyStroke.getKeyStroke("M"));
				}
			}
		});
		alexButton.setMnemonic(KeyEvent.VK_A);
		
		modelType.add(steveButton);
		modelType.add(alexButton);
		
		view.add(modelType);
		
		JCheckBoxMenuItem lightEffects = new JCheckBoxMenuItem("Lighting");
		lightEffects.setSelected(true);
		lightEffects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.modelviewer.setLightEffectEnabled(lightEffects.isSelected());
			}
		});
		lightEffects.setMnemonic(KeyEvent.VK_L);
		lightEffects.setAccelerator(KeyStroke.getKeyStroke("L"));
		view.add(lightEffects);
	}

	private void initPose() {
		pose = new JMenu("Pose");
		pose.setMnemonic(KeyEvent.VK_P);
		add(pose);
		
		anim = new JMenu("Animation");
		anim.setMnemonic(KeyEvent.VK_A);
		
		createAnimationMenu();
		
		pose.add(anim);

		JMenuItem resetPose = new JMenuItem("Reset", KeyEvent.VK_R);
		resetPose.setAccelerator(KeyStroke.getKeyStroke("R"));
		resetPose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.modelviewer.getModelData().resetValues();
			}
		});
		pose.add(resetPose);
		
	}
	
	public void createAnimationMenu() {
		anim.removeAll();
		ButtonGroup animButtonGroup = new ButtonGroup();
		
		String[] animNames = AnimationList.getAnimationNames(); 
		
		for (int i = animNames.length-1, j = 0; i >= 0; i--, j++) {
			String name = animNames[i];
			JRadioButtonMenuItem animButton = new JRadioButtonMenuItem(name);
			Animation a = AnimationList.getAnimation(name);
			animButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					launcher.modelviewer.setAnimation(a);
				}
			});
			animButton.setSelected(a == launcher.modelviewer.getAnimation());
			anim.add(animButton);
			animButtonGroup.add(animButton);
			if(j < 10) {
				animButton.setAccelerator(KeyStroke.getKeyStroke(Integer.toString(j)));
			}
		}
		
		anim.add(new JSeparator());
		
		JMenuItem addNew = new JMenuItem("Load Animation");
		addNew.setIcon(UIManager.getIcon("FileView.fileIcon"));
		addNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(new File(AnimationList.animationFolderPath));
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.showOpenDialog(launcher);
				if(jfc.getSelectedFile() != null) {
					try {
						Animation loaded = AnimationList.loadAnimation(new FileInputStream(jfc.getSelectedFile()));
						if (loaded != null) {
							AnimationList.putAnimation(loaded.getName(), loaded);
							launcher.modelviewer.setAnimation(loaded);
							createAnimationMenu();
						}else {
							JOptionPane.showMessageDialog(launcher, "The file couldn't be loaded.", "Animation not loaded", JOptionPane.ERROR_MESSAGE);
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		anim.add(addNew);
		
	}

	private void initWindow() {
		window = new JMenu("Window");
		window.setMnemonic(KeyEvent.VK_W);
		add(window);
		
		JCheckBoxMenuItem alwaysOnTop = new JCheckBoxMenuItem("Always on top");
		alwaysOnTop.setMnemonic(KeyEvent.VK_T);
		alwaysOnTop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.setAlwaysOnTop(alwaysOnTop.isSelected());
			}
		});
		window.add(alwaysOnTop);
		
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aboutWindow.setLocationRelativeTo(launcher);
				aboutWindow.setVisible(true);
			}
		});
		//window.add(about);
	}
	
}
