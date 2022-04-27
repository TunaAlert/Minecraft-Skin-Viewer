package de.tuna.mcmodelviewer.animation;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.tuna.mcmodelviewer.ModelViewer;

public class AnimationList {

	private static final HashMap<String, Animation> animations = new HashMap<>();
	
	public static final String animationFolderPath = "animations/";
	
	public static Animation loadAnimation(InputStream stream) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document doc = db.parse(stream);
			
	    return KeyFrameAnimation.load(doc);
			
		} catch (ParserConfigurationException | IllegalArgumentException | SAXException | IOException e) {
			System.err.println("Animation couldn't be loaded.");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void putAnimation(String name, Animation animation) {
		animations.put(name, animation);
	}

	public static Animation getAnimation(String name) {
		return animations.get(name);
	}
	
	public static String[] getAnimationNames() {
		Set<String> keySet = animations.keySet();
		String[] names = new String[keySet.size()];
		keySet.toArray(names);
		return names;
	}
	
	public static void exportDefaultAnimations() {
		exportAnimation("breathe");
		exportAnimation("walk");
		exportAnimation("sneak");
		exportAnimation("wave");
	}
	
	private static void exportAnimation(String name) {
		try {
			File target = new File(animationFolderPath+name+".xml");
			if(target.exists()) return;
			if(!target.getParentFile().exists())
			target.getParentFile().mkdirs();
			target.createNewFile();
			
			InputStream in = ModelViewer.class.getResourceAsStream("/animations/" + name + ".xml");
			
			FileOutputStream out = new FileOutputStream(target);
			int read;
			byte[] buffer = new byte[4096];
			while((read = in.read(buffer)) > 0) {
				out.write(buffer, 0, read);
			}
			in.close();
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loadAnimations() {
		putAnimation("None", new KeyFrameAnimation());
		
		File animationFolder = new File(animationFolderPath);
		File[] files = animationFolder.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return f.isFile() && f.getName().toLowerCase().endsWith(".xml");
			}
		});
		
		for (int i = 0; i < files.length; i++) {
			try {
				Animation anim = loadAnimation(new FileInputStream(files[i]));
				if(anim != null) {
					putAnimation(anim.getName(), anim);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
