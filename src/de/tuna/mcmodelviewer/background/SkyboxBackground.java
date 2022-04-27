package de.tuna.mcmodelviewer.background;

import java.io.IOException;

import javax.imageio.ImageIO;

import de.tuna.mcmodelviewer.Launcher;
import de.tuna.mcmodelviewer.ModelViewer;

public class SkyboxBackground extends ImageBackground {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7760035499366350804L;

	public SkyboxBackground(Launcher launcher) {
		super(launcher);
		try {
			backgroundImage = ImageIO.read(SkyboxBackground.class.getResourceAsStream("/skyboxes/skybox.png"));
		} catch (IOException e) {
			System.err.println("default skybox not loaded");
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "Skybox";
	}

	@Override
	protected void applyBackground(ModelViewer modelViewer) {
		modelViewer.setSkyboxTexture(backgroundImage);
		System.out.println("apply");
	}

}
