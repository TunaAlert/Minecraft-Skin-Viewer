package de.tuna.mcmodelviewer.model;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PlayerModel {
	
	public int textureWidth = 0;
	public int textureHeight = 0;
	
	public Cuboid torso;
	public Cuboid head;
	public Cuboid rightArm;
	public Cuboid leftArm;
	public Cuboid rightLeg;
	public Cuboid leftLeg;

	public Cuboid jacket;
	public Cuboid hat;
	public Cuboid rightSleeve;
	public Cuboid leftSleeve;
	public Cuboid rightPants;
	public Cuboid leftPants;
	
	private PlayerModel() {
		
	}
	
	public static PlayerModel load(Document doc) {
		PlayerModel model = new PlayerModel();
		
		Node texture = doc.getElementsByTagName("texture").item(0);
		model.textureWidth = Integer.parseInt(texture.getAttributes().getNamedItem("width").getTextContent());
		model.textureHeight = Integer.parseInt(texture.getAttributes().getNamedItem("height").getTextContent());
		
		model.torso = model.loadCuboid(doc.getElementsByTagName("torso").item(0));
		model.head = model.loadCuboid(doc.getElementsByTagName("head").item(0));
		model.rightArm = model.loadCuboid(doc.getElementsByTagName("right-arm").item(0));
		model.leftArm = model.loadCuboid(doc.getElementsByTagName("left-arm").item(0));
		model.rightLeg = model.loadCuboid(doc.getElementsByTagName("right-leg").item(0));
		model.leftLeg = model.loadCuboid(doc.getElementsByTagName("left-leg").item(0));
		
		model.jacket = model.loadCuboid(doc.getElementsByTagName("jacket").item(0));
		model.hat = model.loadCuboid(doc.getElementsByTagName("hat").item(0));
		model.rightSleeve = model.loadCuboid(doc.getElementsByTagName("right-sleeve").item(0));
		model.leftSleeve = model.loadCuboid(doc.getElementsByTagName("left-sleeve").item(0));
		model.rightPants = model.loadCuboid(doc.getElementsByTagName("right-pants").item(0));
		model.leftPants = model.loadCuboid(doc.getElementsByTagName("left-pants").item(0));
		
		return model;
	}
	
	private Cuboid loadCuboid(Node node) {
		if(node ==  null || node.getNodeType() != Node.ELEMENT_NODE) return new Cuboid(0, 0, 0, 0, 0, 0);
		
		Element element = (Element) node;
		
		float x = Float.parseFloat(element.getAttribute("x"));
		float y = Float.parseFloat(element.getAttribute("y"));
		float z = Float.parseFloat(element.getAttribute("z"));
		float dx = Float.parseFloat(element.getAttribute("dx"));
		float dy = Float.parseFloat(element.getAttribute("dy"));
		float dz = Float.parseFloat(element.getAttribute("dz"));

		Cuboid cuboid = new Cuboid(x, y, z, dx, dy, dz);
		
		NodeList textures = element.getElementsByTagName("texture");
		
		for (int i = 0; i < textures.getLength(); i++) {
			Node textureNode = textures.item(i);
			NamedNodeMap texAttribs = textureNode.getAttributes();
			String face = texAttribs.getNamedItem("face").getTextContent();
			Quad q = null;
			switch (face) {
			case Cuboid.FACE0:
				q = cuboid.quads[0];
				break;
			case Cuboid.FACE1:
				q = cuboid.quads[1];
				break;
			case Cuboid.FACE2:
				q = cuboid.quads[2];
				break;
			case Cuboid.FACE3:
				q = cuboid.quads[3];
				break;
			case Cuboid.FACE4:
				q = cuboid.quads[4];
				break;
			case Cuboid.FACE5:
				q = cuboid.quads[5];
				break;
			}
			if(q != null) {
				int dir = 1;
				int rot = 0;
				if(texAttribs.getNamedItem("flipped") != null && "true".equals(texAttribs.getNamedItem("flipped").getTextContent())) {
					dir = 3;
					rot++;
				}
				if(texAttribs.getNamedItem("rot") != null) {
					rot += Integer.parseInt(texAttribs.getNamedItem("rot").getTextContent());
				}
				float u1 = Float.parseFloat(texAttribs.getNamedItem("u1").getTextContent()) / textureWidth;
				float u2 = Float.parseFloat(texAttribs.getNamedItem("u2").getTextContent()) / textureWidth;
				float v1 = Float.parseFloat(texAttribs.getNamedItem("v1").getTextContent()) / textureHeight;
				float v2 = Float.parseFloat(texAttribs.getNamedItem("v2").getTextContent()) / textureHeight;
				q.t[(rot)%4] = new Vector2f(u1, v1);
				q.t[(2+rot)%4] = new Vector2f(u2, v2);
				q.t[(dir+rot)%4] = new Vector2f(u2, v1);
				q.t[(4+rot-dir)%4] = new Vector2f(u1, v2);
			}
		}
		
		Element origin = (Element) element.getElementsByTagName("origin").item(0);
		cuboid.origin = new Vector3f(
				Float.parseFloat(origin.getAttribute("x")),
				Float.parseFloat(origin.getAttribute("y")),
				Float.parseFloat(origin.getAttribute("z"))
				);
		
		return cuboid;
	}
	
	public PlayerModel get(PlayerModelData data) {
		final float deg2rad = (float) (Math.PI/180f);
		PlayerModel model = new PlayerModel();
		
		model.torso = torso.clone();
		model.head = head.clone();
		model.rightArm = rightArm.clone();
		model.leftArm = leftArm.clone();
		model.rightLeg = rightLeg.clone();
		model.leftLeg = leftLeg.clone();

		model.jacket = jacket.clone();
		model.hat = hat.clone();
		model.rightSleeve = rightSleeve.clone();
		model.leftSleeve = leftSleeve.clone();
		model.rightPants = rightPants.clone();
		model.leftPants = leftPants.clone();
		
		model.head.rotate(deg2rad*data.getHeadPitch(), deg2rad*data.getHeadYaw(), deg2rad*data.getHeadRoll());
		model.torso.rotate(deg2rad*data.getTorsoPitch(), deg2rad*data.getTorsoYaw(), deg2rad*data.getTorsoRoll());
		model.rightArm.rotate(deg2rad*data.getRightArmPitch(), deg2rad*data.getRightArmYaw(), deg2rad*data.getRightArmRoll());
		model.leftArm.rotate(deg2rad*data.getLeftArmPitch(), deg2rad*data.getLeftArmYaw(), deg2rad*data.getLeftArmRoll());
		model.rightLeg.rotate(deg2rad*data.getRightLegPitch(), deg2rad*data.getRightLegYaw(), deg2rad*data.getRightLegRoll());
		model.leftLeg.rotate(deg2rad*data.getLeftLegPitch(), deg2rad*data.getLeftLegYaw(), deg2rad*data.getLeftLegRoll());

		model.hat.rotate(deg2rad*data.getHeadPitch(), deg2rad*data.getHeadYaw(), deg2rad*data.getHeadRoll());
		model.jacket.rotate(deg2rad*data.getTorsoPitch(), deg2rad*data.getTorsoYaw(), deg2rad*data.getTorsoRoll());
		model.rightSleeve.rotate(deg2rad*data.getRightArmPitch(), deg2rad*data.getRightArmYaw(), deg2rad*data.getRightArmRoll());
		model.leftSleeve.rotate(deg2rad*data.getLeftArmPitch(), deg2rad*data.getLeftArmYaw(), deg2rad*data.getLeftArmRoll());
		model.rightPants.rotate(deg2rad*data.getRightLegPitch(), deg2rad*data.getRightLegYaw(), deg2rad*data.getRightLegRoll());
		model.leftPants.rotate(deg2rad*data.getLeftLegPitch(), deg2rad*data.getLeftLegYaw(), deg2rad*data.getLeftLegRoll());

		model.rightArm.translate(model.rightArm.origin.x - model.torso.origin.x, model.rightArm.origin.y - model.torso.origin.y, model.rightArm.origin.z - model.torso.origin.z);
		model.rightArm.rotate(deg2rad*data.getTorsoPitch(), deg2rad*data.getTorsoYaw(), deg2rad*data.getTorsoRoll());
		model.rightArm.translate(model.torso.origin.x - model.rightArm.origin.x, model.torso.origin.y - model.rightArm.origin.y, model.torso.origin.z - model.rightArm.origin.z);
		
		model.leftArm.translate(model.leftArm.origin.x - model.torso.origin.x, model.leftArm.origin.y - model.torso.origin.y, model.leftArm.origin.z - model.torso.origin.z);
		model.leftArm.rotate(deg2rad*data.getTorsoPitch(), deg2rad*data.getTorsoYaw(), deg2rad*data.getTorsoRoll());
		model.leftArm.translate(model.torso.origin.x - model.leftArm.origin.x, model.torso.origin.y - model.leftArm.origin.y, model.torso.origin.z - model.leftArm.origin.z);
		
		model.rightSleeve.translate(model.rightSleeve.origin.x - model.torso.origin.x, model.rightSleeve.origin.y - model.torso.origin.y, model.rightSleeve.origin.z - model.torso.origin.z);
		model.rightSleeve.rotate(deg2rad*data.getTorsoPitch(), deg2rad*data.getTorsoYaw(), deg2rad*data.getTorsoRoll());
		model.rightSleeve.translate(model.torso.origin.x - model.rightSleeve.origin.x, model.torso.origin.y - model.rightSleeve.origin.y, model.torso.origin.z - model.rightSleeve.origin.z);
		
		model.leftSleeve.translate(model.leftSleeve.origin.x - model.torso.origin.x, model.leftSleeve.origin.y - model.torso.origin.y, model.leftSleeve.origin.z - model.torso.origin.z);
		model.leftSleeve.rotate(deg2rad*data.getTorsoPitch(), deg2rad*data.getTorsoYaw(), deg2rad*data.getTorsoRoll());
		model.leftSleeve.translate(model.torso.origin.x - model.leftSleeve.origin.x, model.torso.origin.y - model.leftSleeve.origin.y, model.torso.origin.z - model.leftSleeve.origin.z);
		
		model.head.translate(model.head.origin.x - model.torso.origin.x, model.head.origin.y - model.torso.origin.y, model.head.origin.z - model.torso.origin.z);
		model.head.rotate(deg2rad*data.getTorsoPitch(), deg2rad*data.getTorsoYaw(), deg2rad*data.getTorsoRoll());
		model.head.translate(model.torso.origin.x - model.head.origin.x, model.torso.origin.y - model.head.origin.y, model.torso.origin.z - model.head.origin.z);

		model.hat.translate(model.hat.origin.x - model.torso.origin.x, model.hat.origin.y - model.torso.origin.y, model.hat.origin.z - model.torso.origin.z);
		model.hat.rotate(deg2rad*data.getTorsoPitch(), deg2rad*data.getTorsoYaw(), deg2rad*data.getTorsoRoll());
		model.hat.translate(model.torso.origin.x - model.hat.origin.x, model.torso.origin.y - model.hat.origin.y, model.torso.origin.z - model.hat.origin.z);
		
		model.leftLeg.translate(data.getLegXOffset(), data.getLegYOffset(), data.getLegZOffset());
		model.rightLeg.translate(data.getLegXOffset(), data.getLegYOffset(), data.getLegZOffset());
		model.leftPants.translate(data.getLegXOffset(), data.getLegYOffset(), data.getLegZOffset());
		model.rightPants.translate(data.getLegXOffset(), data.getLegYOffset(), data.getLegZOffset());
		
		return model;
	}
	
}
