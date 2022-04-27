package de.tuna.mcmodelviewer.background;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import de.tuna.mcmodelviewer.Launcher;
import de.tuna.mcmodelviewer.ModelViewer;

public class ImageBackground extends Background {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6532333939829896865L;

	private File imageSource = new File(System.getProperty("user.dir"));
	protected BufferedImage backgroundImage = null;
	
	private static final FileFilter imgFilter = new FileFilter() {
		public String getDescription() {
			return null;
		}
		public boolean accept(File f) {
			Pattern p = Pattern.compile(".*\\.(png)|(jpg)|(jpeg)|(gif)|(bmp)|(wbmp)$", Pattern.CASE_INSENSITIVE);
			return f.isDirectory() || p.matcher(f.getName()).find();
		}
	};
	
	public ImageBackground(Launcher launcher) {
		super(launcher);

		JButton browse = new JButton(UIManager.getIcon("FileView.directoryIcon"));
		add(browse);
		
		JLabel imageName = new JLabel("None");
		imageName.setToolTipText("No image is loaded");
		add(imageName);
		
		
		browse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(imageSource);
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setMultiSelectionEnabled(false);
				jfc.setFileFilter(imgFilter);
				jfc.showOpenDialog(launcher);
				File selectedFile = jfc.getSelectedFile();
				if(selectedFile == null) return;
				if(!selectedFile.isFile()) return;

				try {
					BufferedImage loadedImg = ImageIO.read(selectedFile);
					if(loadedImg == null) throw new Exception();//bad form but who gives a shit
					backgroundImage = loadedImg;
					imageSource = selectedFile;
					applyBackground(launcher.modelviewer);
					imageName.setText(selectedFile.getName());
					imageName.setToolTipText(selectedFile.getName());
				} catch (Exception e1) {
					JPanel errorPanel = new JPanel();
					errorPanel.add(new JLabel("The file"));
					errorPanel.add(new JLabel(selectedFile.getName()));
					errorPanel.add(new JLabel("couldn't be loaded"));
					JOptionPane.showMessageDialog(launcher, errorPanel, "Image not loaded", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	@Override
	public String getName() {
		return "Image";
	}

	@Override
	protected void applyBackground(ModelViewer modelViewer) {
		modelViewer.setBackgroundImage(backgroundImage);
	}

}
