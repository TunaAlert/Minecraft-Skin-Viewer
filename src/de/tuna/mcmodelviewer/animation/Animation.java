package de.tuna.mcmodelviewer.animation;

import de.tuna.mcmodelviewer.model.PlayerModelData;

public interface Animation {
	
	public String getName();
	public PlayerModelData setDataAtTime(PlayerModelData data, float seconds);
	
}
