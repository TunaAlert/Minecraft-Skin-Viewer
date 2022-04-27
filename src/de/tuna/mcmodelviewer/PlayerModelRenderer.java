package de.tuna.mcmodelviewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import de.tuna.mcmodelviewer.model.Cuboid;
import de.tuna.mcmodelviewer.model.PlayerModel;
import de.tuna.mcmodelviewer.model.PlayerModelData;
import de.tuna.mcmodelviewer.model.Quad;

public class PlayerModelRenderer {
	
	private PlayerModelData modelData;
	private int playerTexture;
	
	private float ambientLight = 0.7f;
	private float topLight = 0.3f;
	private float rightLight = 0.1f;
	private float frontLight = 0.2f;
	private boolean lightEffects = true;
	
	public PlayerModelRenderer() {
		this(new PlayerModelData());
	}
	
	public PlayerModelRenderer(PlayerModelData modelData) {
		this.modelData = modelData;
	}
	
	public void setModelData(PlayerModelData modelData) {
		this.modelData = modelData;
	}
	
	public PlayerModelData getModelData() {
		return modelData;
	}
	
	public int getPlayerTexture() {
		return playerTexture;
	}

	public void setPlayerTexture(int playerTexture) {
		this.playerTexture = playerTexture;
	}

	public void setLightEffectEnabled(boolean enabled) {
		lightEffects = enabled;
	}

	public void renderModel(PlayerModel model, Vector3f camPos) {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, playerTexture);
		PlayerModel rotated = model.get(modelData);

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		
		renderBottomLayer(rotated);

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		
		renderTopLayer(rotated, camPos);
		
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	private void renderBottomLayer(PlayerModel model) {

		if(modelData.isTorsoVisible()) renderCuboid(model.torso);
		if(modelData.isHeadVisible()) renderCuboid(model.head);
		if(modelData.isLeftArmVisible()) renderCuboid(model.leftArm);
		if(modelData.isRightArmVisible()) renderCuboid(model.rightArm);
		if(modelData.isLeftLegVisible()) renderCuboid(model.leftLeg);
		if(modelData.isRightLegVisible()) renderCuboid(model.rightLeg);

	}

	private void renderTopLayer(PlayerModel model, Vector3f camPos) {

		ArrayList<Quad> transparentQuads = new ArrayList<>();
		
		if(modelData.isJacketVisible()) {
			model.jacket.translateToOrigin();
			transparentQuads.addAll(Arrays.asList(model.jacket.quads));
		}
		if(modelData.isHatVisible()) {
			model.hat.translateToOrigin();
			transparentQuads.addAll(Arrays.asList(model.hat.quads));
		}
		if(modelData.isLeftSleeveVisible()) {
			model.leftSleeve.translateToOrigin();
			transparentQuads.addAll(Arrays.asList(model.leftSleeve.quads));
		}
		if(modelData.isRightSleeveVisible()) {
			model.rightSleeve.translateToOrigin();
			transparentQuads.addAll(Arrays.asList(model.rightSleeve.quads));
		}
		if(modelData.isLeftPantsVisible()) {
			model.leftPants.translateToOrigin();
			transparentQuads.addAll(Arrays.asList(model.leftPants.quads));
		}
		if(modelData.isRightPantsVisible()) {
			model.rightPants.translateToOrigin();
			transparentQuads.addAll(Arrays.asList(model.rightPants.quads));
		}
		
		sortQuads(transparentQuads, camPos);
		
	}

	private void sortQuads(ArrayList<Quad> quads, Vector3f camPos) {
		HashMap<Quad, Float> sqrdistances = new HashMap<>();
		for (Quad quad : quads) {
			Vector3f center = new Vector3f(quad.v[0]);
			Vector3f.add(center, quad.v[1], center);
			Vector3f.add(center, quad.v[2], center);
			Vector3f.add(center, quad.v[3], center);
			center.scale(0.25f);
			Vector3f camToCenter = new Vector3f(); 
			Vector3f.sub(center, camPos, camToCenter);
			sqrdistances.put(quad, camToCenter.lengthSquared());
		}
		quads.sort(new Comparator<Quad>() {
			@Override
			public int compare(Quad q1, Quad q2) {
				float d1 = sqrdistances.get(q1);
				float d2 = sqrdistances.get(q2);
				return d2 > d1 ? 1 : d2 < d1 ? -1 : 0;
			}
		});
		Quad[] quadsArray = new Quad[quads.size()];
		quads.toArray(quadsArray);
		renderQuads(quadsArray);
	}

	private float getLightLevel(Vector3f normal) {
		if(!lightEffects) return 1;
		if(normal.lengthSquared() == 0) return 0;
		Vector3f n = new Vector3f(normal);
		n.normalise();
		return ambientLight
				+ topLight * Vector3f.dot(n, new Vector3f(0, 1, 0))
				+ rightLight * Vector3f.dot(n, new Vector3f(-1, 0, 0))
				+ frontLight * Vector3f.dot(n, new Vector3f(0, 0, 1));
	}
	
	private void setLight(Vector3f normal) {
		float light = getLightLevel(normal);
		GL11.glColor3f(light, light, light);
	}
	
	public void renderCuboid(Cuboid c) {
		GL11.glPushMatrix();
		
		GL11.glTranslatef(c.origin.x, c.origin.y, c.origin.z);
		renderQuads(c.quads);
		
		GL11.glPopMatrix();
	}
	
	private void renderQuads(Quad[] quads) {

		GL11.glBegin(GL11.GL_QUADS);
		
		for (int i = 0; i < quads.length; i++) {
			Quad q = quads[i];
			setLight(q.n);
			for (int j = 0; j < 4; j++) {
				GL11.glTexCoord2f(q.t[j].x, q.t[j].y);
				GL11.glVertex3f(q.v[j].x, q.v[j].y, q.v[j].z);
			}
		}
		
		GL11.glEnd();
	}
}
