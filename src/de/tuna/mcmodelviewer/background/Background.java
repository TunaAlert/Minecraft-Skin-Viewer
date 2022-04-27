package de.tuna.mcmodelviewer.background;

import javax.swing.JPanel;

import de.tuna.mcmodelviewer.Launcher;
import de.tuna.mcmodelviewer.ModelViewer;

public abstract class Background extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Launcher launcher; 
	
	public Background(Launcher launcher) {
		this.launcher = launcher;
	}
	
	public abstract String getName();
	protected abstract void applyBackground(ModelViewer modelViewer);
	
	public void apply() {
		applyBackground(launcher.modelviewer);
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
