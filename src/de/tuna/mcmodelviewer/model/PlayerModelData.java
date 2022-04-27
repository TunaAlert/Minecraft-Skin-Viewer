package de.tuna.mcmodelviewer.model;

public class PlayerModelData {
	
	private boolean leftArmVisible = true;
	private boolean rightArmVisible = true;
	private boolean leftLegVisible = true;
	private boolean rightLegVisible = true;
	private boolean leftSleeveVisible = true;
	private boolean rightSleeveVisible = true;
	private boolean leftPantsVisible = true;
	private boolean rightPantsVisible = true;
	private boolean torsoVisible = true;
	private boolean jacketVisible = true;
	private boolean headVisible = true;
	private boolean hatVisible = true;
	
	private float rightArmPitch = 0f;
	private float rightArmYaw = 0f;
	private float rightArmRoll = 0f;
	private float leftArmPitch = 0f;
	private float leftArmYaw = 0f;
	private float leftArmRoll = 0f;
	private float rightLegPitch = 0f;
	private float rightLegYaw = 0f;
	private float rightLegRoll = 0f;
	private float leftLegPitch = 0f;
	private float leftLegYaw = 0f;
	private float leftLegRoll = 0f;
	private float headPitch = 0f;
	private float headYaw = 0f;
	private float headRoll = 0f;
	private float torsoPitch = 0f;
	private float torsoYaw = 0f;
	private float torsoRoll = 0f;
	
	private float xOffset = 0;
	private float yOffset = 0;
	private float zOffset = 0;

	private float legXOffset = 0;
	private float legYOffset = 0;
	private float legZOffset = 0;
	
	public boolean isLeftArmVisible() {
		return leftArmVisible;
	}
	
	public void setLeftArmVisible(boolean leftArmVisible) {
		this.leftArmVisible = leftArmVisible;
	}
	
	public boolean isRightArmVisible() {
		return rightArmVisible;
	}
	
	public void setRightArmVisible(boolean rightArmVisible) {
		this.rightArmVisible = rightArmVisible;
	}
	
	public boolean isLeftLegVisible() {
		return leftLegVisible;
	}
	
	public void setLeftLegVisible(boolean leftLegVisible) {
		this.leftLegVisible = leftLegVisible;
	}
	
	public boolean isRightLegVisible() {
		return rightLegVisible;
	}
	
	public void setRightLegVisible(boolean rightLefVisible) {
		this.rightLegVisible = rightLefVisible;
	}
	
	public boolean isLeftSleeveVisible() {
		return leftSleeveVisible && leftArmVisible;
	}
	
	public void setLeftSleeveVisible(boolean leftSleeveVisible) {
		this.leftSleeveVisible = leftSleeveVisible;
		this.leftArmVisible = this.leftArmVisible || leftSleeveVisible;
	}
	
	public boolean isRightSleeveVisible() {
		return rightSleeveVisible && rightArmVisible;
	}
	
	public void setRightSleeveVisible(boolean rightSleeveVisible) {
		this.rightSleeveVisible = rightSleeveVisible;
		this.rightArmVisible = this.rightArmVisible || rightSleeveVisible;
	}
	
	public boolean isLeftPantsVisible() {
		return leftPantsVisible && leftLegVisible;
	}
	
	public void setLeftPantsVisible(boolean leftPantsVisible) {
		this.leftPantsVisible = leftPantsVisible;
		this.leftLegVisible = this.leftLegVisible || leftPantsVisible;
	}
	
	public boolean isRightPantsVisible() {
		return rightPantsVisible && rightLegVisible;
	}
	
	public void setRightPantsVisible(boolean rightPantsVisible) {
		this.rightPantsVisible = rightPantsVisible;
		this.rightLegVisible = this.rightLegVisible || rightPantsVisible;
	}
	
	public boolean isTorsoVisible() {
		return torsoVisible;
	}
	
	public void setTorsoVisible(boolean torsoVisible) {
		this.torsoVisible = torsoVisible;
	}
	
	public boolean isJacketVisible() {
		return jacketVisible && torsoVisible;
	}
	
	public void setJacketVisible(boolean jacketVisible) {
		this.jacketVisible = jacketVisible;
		this.torsoVisible = this.torsoVisible || jacketVisible;
	}
	
	public boolean isHeadVisible() {
		return headVisible;
	}
	
	public void setHeadVisible(boolean headVisible) {
		this.headVisible = headVisible;
	}
	
	public boolean isHatVisible() {
		return hatVisible && headVisible;
	}
	
	public void setHatVisible(boolean hatVisible) {
		this.hatVisible = hatVisible;
		this.headVisible = this.headVisible || hatVisible;
	}

	public float getRightArmPitch() {
		return rightArmPitch;
	}

	public void setRightArmPitch(float rightArmPitch) {
		this.rightArmPitch = rightArmPitch;
	}

	public float getRightArmYaw() {
		return rightArmYaw;
	}

	public void setRightArmYaw(float rightArmYaw) {
		this.rightArmYaw = rightArmYaw;
	}

	public float getRightArmRoll() {
		return rightArmRoll;
	}

	public void setRightArmRoll(float rightArmRoll) {
		this.rightArmRoll = rightArmRoll;
	}
	
	public void setRightArmAngles(float pitch, float yaw, float roll) {
		rightArmPitch = pitch;
		rightArmYaw = yaw;
		rightArmRoll = roll;
	}

	public float getLeftArmPitch() {
		return leftArmPitch;
	}

	public void setLeftArmPitch(float leftArmPitch) {
		this.leftArmPitch = leftArmPitch;
	}

	public float getLeftArmYaw() {
		return leftArmYaw;
	}

	public void setLeftArmYaw(float leftArmYaw) {
		this.leftArmYaw = leftArmYaw;
	}

	public float getLeftArmRoll() {
		return leftArmRoll;
	}

	public void setLeftArmRoll(float leftArmRoll) {
		this.leftArmRoll = leftArmRoll;
	}
	
	public void setLeftArmAngles(float pitch, float yaw, float roll) {
		leftArmPitch = pitch;
		leftArmYaw = yaw;
		leftArmRoll = roll;
	}

	public float getRightLegPitch() {
		return rightLegPitch;
	}

	public void setRightLegPitch(float rightLegPitch) {
		this.rightLegPitch = rightLegPitch;
	}

	public float getRightLegYaw() {
		return rightLegYaw;
	}

	public void setRightLegYaw(float rightLegYaw) {
		this.rightLegYaw = rightLegYaw;
	}

	public float getRightLegRoll() {
		return rightLegRoll;
	}

	public void setRightLegRoll(float rightLegRoll) {
		this.rightLegRoll = rightLegRoll;
	}
	
	public void setRightLegAngles(float pitch, float yaw, float roll) {
		rightLegPitch = pitch;
		rightLegYaw = yaw;
		rightLegRoll = roll;
	}

	public float getLeftLegPitch() {
		return leftLegPitch;
	}

	public void setLeftLegPitch(float leftLegPitch) {
		this.leftLegPitch = leftLegPitch;
	}

	public float getLeftLegYaw() {
		return leftLegYaw;
	}

	public void setLeftLegYaw(float leftLegYaw) {
		this.leftLegYaw = leftLegYaw;
	}

	public float getLeftLegRoll() {
		return leftLegRoll;
	}

	public void setLeftLegRoll(float leftLegRoll) {
		this.leftLegRoll = leftLegRoll;
	}
	
	public void setLeftLegAngles(float pitch, float yaw, float roll) {
		leftLegPitch = pitch;
		leftLegYaw = yaw;
		leftLegRoll = roll;
	}

	public float getHeadPitch() {
		return headPitch;
	}

	public void setHeadPitch(float headPitch) {
		this.headPitch = headPitch;
	}

	public float getHeadYaw() {
		return headYaw;
	}

	public void setHeadYaw(float headYaw) {
		this.headYaw = headYaw;
	}

	public float getHeadRoll() {
		return headRoll;
	}

	public void setHeadRoll(float headRoll) {
		this.headRoll = headRoll;
	}
	
	public void setHeadAngles(float pitch, float yaw, float roll) {
		headPitch = pitch;
		headYaw = yaw;
		headRoll = roll;
	}

	public float getTorsoPitch() {
		return torsoPitch;
	}

	public void setTorsoPitch(float torsoPitch) {
		this.torsoPitch = torsoPitch;
	}

	public float getTorsoYaw() {
		return torsoYaw;
	}

	public void setTorsoYaw(float torsoYaw) {
		this.torsoYaw = torsoYaw;
	}

	public float getTorsoRoll() {
		return torsoRoll;
	}

	public void setTorsoRoll(float torsoRoll) {
		this.torsoRoll = torsoRoll;
	}

	public void setTorsoAngles(float pitch, float yaw, float roll) {
		torsoPitch = pitch;
		torsoYaw = yaw;
		torsoRoll = roll;
	}

	public float getXOffset() {
		return xOffset;
	}

	public void setXOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	public float getYOffset() {
		return yOffset;
	}

	public void setYOffset(float yOffset) {
		this.yOffset = yOffset;
	}

	public float getZOffset() {
		return zOffset;
	}

	public void setZOffset(float zOffset) {
		this.zOffset = zOffset;
	}
	
	public void setOffsets(float x, float y, float z) {
		xOffset = x;
		yOffset = y;
		zOffset = z;
	}

	public float getLegXOffset() {
		return legXOffset;
	}

	public void setLegXOffset(float legXOffset) {
		this.legXOffset = legXOffset;
	}

	public float getLegYOffset() {
		return legYOffset;
	}

	public void setLegYOffset(float legYOffset) {
		this.legYOffset = legYOffset;
	}

	public float getLegZOffset() {
		return legZOffset;
	}

	public void setLegZOffset(float legZOffset) {
		this.legZOffset = legZOffset;
	}
	
	public void setLegOffsets(float x, float y, float z) {
		legXOffset = x;
		legYOffset = y;
		legZOffset = z;
	}

	public void resetValues() {
		setHatVisible(true);
		setJacketVisible(true);
		setLeftSleeveVisible(true);
		setRightSleeveVisible(true);
		setLeftPantsVisible(true);
		setRightPantsVisible(true);
		setHeadAngles(0, 0, 0);
		setLeftArmAngles(0, 0, 0);
		setRightArmAngles(0, 0, 0);
		setLeftLegAngles(0, 0, 0);
		setRightLegAngles(0, 0, 0);
		setTorsoAngles(0, 0, 0);
		setOffsets(0, 0, 0);
		setLegOffsets(0, 0, 0);
	}
	
}
