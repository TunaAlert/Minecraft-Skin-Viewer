package de.tuna.mcmodelviewer.animation;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.tuna.mcmodelviewer.model.PlayerModelData;

public class KeyFrameAnimation implements Animation{
	
	public Spline headPitch;
	public Spline headYaw;
	public Spline headRoll;

	public Spline torsoPitch;
	public Spline torsoYaw;
	public Spline torsoRoll;
	
	public Spline leftArmPitch;
	public Spline leftArmYaw;
	public Spline leftArmRoll;

	public Spline rightArmPitch;
	public Spline rightArmYaw;
	public Spline rightArmRoll;

	public Spline leftLegPitch;
	public Spline leftLegYaw;
	public Spline leftLegRoll;

	public Spline rightLegPitch;
	public Spline rightLegYaw;
	public Spline rightLegRoll;

	public Spline xOffset;
	public Spline yOffset;
	public Spline zOffset;
	
	public Spline legXOffset;
	public Spline legYOffset;
	public Spline legZOffset;
	
	public float loopLength;
	
	public String name;
	
	public KeyFrameAnimation() {
		loopLength = 1f;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public PlayerModelData setDataAtTime(PlayerModelData data, float seconds) {
		if(loopLength > 0) seconds %= loopLength;
		
		if(headPitch != null) data.setHeadPitch(headPitch.getDataAt(seconds));
		if(headYaw != null)   data.setHeadYaw(headYaw.getDataAt(seconds));
		if(headRoll != null)  data.setHeadRoll(headRoll.getDataAt(seconds));

		if(torsoPitch != null) data.setTorsoPitch(torsoPitch.getDataAt(seconds));
		if(torsoYaw != null)   data.setTorsoYaw(torsoYaw.getDataAt(seconds));
		if(torsoRoll != null)  data.setTorsoRoll(torsoRoll.getDataAt(seconds));

		if(leftArmPitch != null) data.setLeftArmPitch(leftArmPitch.getDataAt(seconds));
		if(leftArmYaw != null)   data.setLeftArmYaw(leftArmYaw.getDataAt(seconds));
		if(leftArmRoll != null)  data.setLeftArmRoll(leftArmRoll.getDataAt(seconds));

		if(rightArmPitch != null) data.setRightArmPitch(rightArmPitch.getDataAt(seconds));
		if(rightArmYaw != null)   data.setRightArmYaw(rightArmYaw.getDataAt(seconds));
		if(rightArmRoll != null)  data.setRightArmRoll(rightArmRoll.getDataAt(seconds));

		if(leftLegPitch != null) data.setLeftLegPitch(leftLegPitch.getDataAt(seconds));
		if(leftLegYaw != null)   data.setLeftLegYaw(leftLegYaw.getDataAt(seconds));
		if(leftLegRoll != null)  data.setLeftLegRoll(leftLegRoll.getDataAt(seconds));

		if(rightLegPitch != null) data.setRightLegPitch(rightLegPitch.getDataAt(seconds));
		if(rightLegYaw != null)   data.setRightLegYaw(rightLegYaw.getDataAt(seconds));
		if(rightLegRoll != null)  data.setRightLegRoll(rightLegRoll.getDataAt(seconds));

		if(xOffset != null) data.setXOffset(xOffset.getDataAt(seconds));
		if(yOffset != null) data.setYOffset(yOffset.getDataAt(seconds));
		if(zOffset != null) data.setZOffset(zOffset.getDataAt(seconds));

		if(legXOffset != null) data.setLegXOffset(legXOffset.getDataAt(seconds));
		if(legYOffset != null) data.setLegYOffset(legYOffset.getDataAt(seconds));
		if(legZOffset != null) data.setLegZOffset(legZOffset.getDataAt(seconds));

		return data;
	}
	
	public static KeyFrameAnimation load(Document doc) {
		KeyFrameAnimation anim = new KeyFrameAnimation();

		Node length = doc.getDocumentElement().getAttributes().getNamedItem("length");
		if(length != null) {
			anim.loopLength = Float.parseFloat(length.getTextContent());
		}

		Node name = doc.getDocumentElement().getAttributes().getNamedItem("name");
		if(name != null) {
			anim.name = name.getTextContent();
		}
		
		Node head = doc.getElementsByTagName("head").item(0);
		if(head instanceof Element) {
			Element elem = (Element) head;
			Node pitch = elem.getElementsByTagName("pitch").item(0);
			if(pitch instanceof Element) {
				anim.headPitch = loadSpline((Element) pitch);
			}
			Node yaw = elem.getElementsByTagName("yaw").item(0);
			if(yaw instanceof Element) {
				anim.headYaw = loadSpline((Element) yaw);
			}
			Node roll = elem.getElementsByTagName("roll").item(0);
			if(roll instanceof Element) {
				anim.headRoll = loadSpline((Element) roll);
			}
		}
		
		Node torso = doc.getElementsByTagName("torso").item(0);
		if(torso instanceof Element) {
			Element elem = (Element) torso;
			Node pitch = elem.getElementsByTagName("pitch").item(0);
			if(pitch instanceof Element) {
				anim.torsoPitch = loadSpline((Element) pitch);
			}
			Node yaw = elem.getElementsByTagName("yaw").item(0);
			if(yaw instanceof Element) {
				anim.torsoYaw = loadSpline((Element) yaw);
			}
			Node roll = elem.getElementsByTagName("roll").item(0);
			if(roll instanceof Element) {
				anim.torsoRoll = loadSpline((Element) roll);
			}
		}
		
		Node rarm = doc.getElementsByTagName("right-arm").item(0);
		if(rarm instanceof Element) {
			Element elem = (Element) rarm;
			Node pitch = elem.getElementsByTagName("pitch").item(0);
			if(pitch instanceof Element) {
				anim.rightArmPitch = loadSpline((Element) pitch);
			}
			Node yaw = elem.getElementsByTagName("yaw").item(0);
			if(yaw instanceof Element) {
				anim.rightArmYaw = loadSpline((Element) yaw);
			}
			Node roll = elem.getElementsByTagName("roll").item(0);
			if(roll instanceof Element) {
				anim.rightArmRoll = loadSpline((Element) roll);
			}
		}

		Node larm = doc.getElementsByTagName("left-arm").item(0);
		if(larm instanceof Element) {
			Element elem = (Element) larm;
			Node pitch = elem.getElementsByTagName("pitch").item(0);
			if(pitch instanceof Element) {
				anim.leftArmPitch = loadSpline((Element) pitch);
			}
			Node yaw = elem.getElementsByTagName("yaw").item(0);
			if(yaw instanceof Element) {
				anim.leftArmYaw = loadSpline((Element) yaw);
			}
			Node roll = elem.getElementsByTagName("roll").item(0);
			if(roll instanceof Element) {
				anim.leftArmRoll = loadSpline((Element) roll);
			}
		}
		
		Node rleg = doc.getElementsByTagName("right-leg").item(0);
		if(rleg instanceof Element) {
			Element elem = (Element) rleg;
			Node pitch = elem.getElementsByTagName("pitch").item(0);
			if(pitch instanceof Element) {
				anim.rightLegPitch = loadSpline((Element) pitch);
			}
			Node yaw = elem.getElementsByTagName("yaw").item(0);
			if(yaw instanceof Element) {
				anim.rightLegYaw = loadSpline((Element) yaw);
			}
			Node roll = elem.getElementsByTagName("roll").item(0);
			if(roll instanceof Element) {
				anim.rightLegRoll = loadSpline((Element) roll);
			}
		}

		Node lleg = doc.getElementsByTagName("left-leg").item(0);
		if(lleg instanceof Element) {
			Element elem = (Element) lleg;
			Node pitch = elem.getElementsByTagName("pitch").item(0);
			if(pitch instanceof Element) {
				anim.leftLegPitch = loadSpline((Element) pitch);
			}
			Node yaw = elem.getElementsByTagName("yaw").item(0);
			if(yaw instanceof Element) {
				anim.leftLegYaw = loadSpline((Element) yaw);
			}
			Node roll = elem.getElementsByTagName("roll").item(0);
			if(roll instanceof Element) {
				anim.leftLegRoll = loadSpline((Element) roll);
			}
		}

		Node offset = doc.getElementsByTagName("offset").item(0);
		if(offset instanceof Element) {
			Element elem = (Element) offset;
			Node x = elem.getElementsByTagName("x").item(0);
			if(x instanceof Element) {
				anim.xOffset = loadSpline((Element) x);
			}
			Node y = elem.getElementsByTagName("y").item(0);
			if(y instanceof Element) {
				anim.yOffset = loadSpline((Element) y);
			}
			Node z = elem.getElementsByTagName("z").item(0);
			if(z instanceof Element) {
				anim.zOffset = loadSpline((Element) z);
			}
		}

		Node legOffset = doc.getElementsByTagName("leg-offset").item(0);
		if(legOffset instanceof Element) {
			Element elem = (Element) legOffset;
			Node x = elem.getElementsByTagName("x").item(0);
			if(x instanceof Element) {
				anim.legXOffset = loadSpline((Element) x);
			}
			Node y = elem.getElementsByTagName("y").item(0);
			if(y instanceof Element) {
				anim.legYOffset = loadSpline((Element) y);
			}
			Node z = elem.getElementsByTagName("z").item(0);
			if(z instanceof Element) {
				anim.legZOffset = loadSpline((Element) z);
			}
		}
		
		return anim;
	}
	
	private static Spline loadSpline(Element elem) {
		NodeList keyFrameNodes = elem.getElementsByTagName("keyframe");
		if(keyFrameNodes.getLength() == 0) return null;

		
		Spline spline = new Spline();
		ArrayList<KeyFrame> keyFrames = new ArrayList<>();

		Interpolation defaultInterpolation = Interpolation.CONSTANT;
		if(!elem.getAttribute("i").isEmpty())
			defaultInterpolation = Interpolation.valueOf(elem.getAttribute("i"));
		if(!elem.getAttribute("length").isEmpty())
			spline.loopLength = Float.parseFloat(elem.getAttribute("length"));
		
		for (int i = 0; i < keyFrameNodes.getLength(); i++) {
			Node node = keyFrameNodes.item(i);
			NamedNodeMap attributes = node.getAttributes();
			KeyFrame frame = new KeyFrame();
			if(attributes.getNamedItem("t") != null)
				frame.t = Float.parseFloat(attributes.getNamedItem("t").getTextContent());
			
			if(attributes.getNamedItem("i") != null)
				frame.i = Interpolation.valueOf(attributes.getNamedItem("i").getTextContent());
			else
				frame.i = defaultInterpolation;

			frame.v = Float.parseFloat(node.getTextContent());
			keyFrames.add(frame);
		}
		
		spline.keyFrames = new KeyFrame[keyFrames.size()];
		keyFrames.toArray(spline.keyFrames);
		
		return spline;
	}

	public static class Spline{
		
		public KeyFrame[] keyFrames;
		public float loopLength = 0;
		
		public float getDataAt(float t) {
			if(keyFrames == null || keyFrames.length == 0) return 0;
			if(loopLength > 0) t %= loopLength;
			
			for (int i = keyFrames.length-1; i >= 0; i--) {
				KeyFrame k0 = keyFrames[i];
				if(k0.t <= t) {
					if(i+1 < keyFrames.length) {
						KeyFrame k1 = keyFrames[i+1];
						
						//dt is always > 0
						float dt = k1.t - k0.t;
						float st = t - k0.t;
						//0 <= a < 1
						float a = st/dt;
						
						switch (k0.i) {
						default:
						case CONSTANT:
							return k0.v;
						case LINEAR:
							return k0.v * (1-a) + k1.v * a;
						case SMOOTH:
							return k0.v + (1f - (float)Math.cos(a*Math.PI))*0.5f * (k1.v - k0.v);
						case SMOOTH_OUT:
							return k0.v + (1f - (float)Math.cos(a*0.5*Math.PI)) * (k1.v - k0.v);
						case SMOOTH_IN:
							return k0.v + (float)Math.sin(a*0.5*Math.PI) * (k1.v - k0.v);
						}
					}else {
						return k0.v;
					}
				}
			}
			return keyFrames[0].v;
		}
		
	}
	
	public static class KeyFrame{
		
		public float t;
		public float v;
		public Interpolation i;
		
	}
	
	public static enum Interpolation{
		CONSTANT,
		LINEAR,
		SMOOTH,
		SMOOTH_IN,
		SMOOTH_OUT
	}
}
