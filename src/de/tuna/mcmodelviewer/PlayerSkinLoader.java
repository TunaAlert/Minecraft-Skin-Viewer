package de.tuna.mcmodelviewer;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.tuna.mcmodelviewer.model.PlayerModelType;
import jakarta.xml.bind.DatatypeConverter;

public class PlayerSkinLoader {

	private static final Pattern uuid36 = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);
	private static final Pattern uuid32 = Pattern.compile("^[0-9a-f]{32}$", Pattern.CASE_INSENSITIVE);
	
	public static void loadPlayerSkin(String input, Launcher launcher) {
		JFrame status = new JFrame("Loading skin");
		JPanel panel = new JPanel();
		
		panel.setLayout(new GridLayout(0, 1));
		status.setContentPane(panel);
		
		JLabel statusLabel = new JLabel();
		JProgressBar progressbar = new JProgressBar(0, 3);
		
		panel.add(Box.createVerticalStrut(10));
		panel.add(statusLabel);
		panel.add(progressbar);
		panel.add(Box.createVerticalStrut(10));
		panel.doLayout();
		
		status.setResizable(false);
		status.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		status.pack();
		status.setLocationRelativeTo(launcher);
		status.setAlwaysOnTop(true);
		
		new Thread(new Runnable() {
			public void run() {
				try {
					UUID uuid = getUUID(input);
					if(uuid == null) {
						status.setVisible(true);
						statusLabel.setText("fetching uuid");
						progressbar.setValue(0);
						
						uuid = loadUuidFromDatabase(input);
					}
					if(uuid != null) {
						status.setVisible(true);
						statusLabel.setText("getting texture info");
						progressbar.setValue(1);
						
						String texinfo = getTextureInfo(uuid);
						if(texinfo != null) {
							byte[] bytes = DatatypeConverter.parseBase64Binary(texinfo);
							status.setVisible(true);
							statusLabel.setText("loading skin");
							progressbar.setValue(2);
							loadPlayerSkinFromInfo(new String(bytes, StandardCharsets.UTF_8), launcher);
						}
					}else {
						status.dispose();
						JOptionPane.showMessageDialog(launcher.modelviewer, "The player " + input + " couldn't be found.", "Not found!", JOptionPane.ERROR_MESSAGE);
					}
				}catch (IllegalArgumentException ex) {
					ex.printStackTrace();
				}
				status.dispose();
			}
		}).start();
	}

	private static UUID getUUID(String id) {
		if(uuid36.matcher(id).matches()) {
			return UUID.fromString(id);
		}else if(uuid32.matcher(id).matches()) {
			return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20));
		}
		return null;
	}

	private static UUID loadUuidFromDatabase(String playername) {
		UUID uuid = null;
		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+playername);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			InputStream responseStream = connection.getInputStream();
			InputStreamReader reader = new InputStreamReader(responseStream);
			
			Object parsed = new JSONParser().parse(reader);
			if(parsed instanceof JSONObject) {
				JSONObject json = (JSONObject) parsed;
				Object id = json.get("id");
				if(id instanceof String) {
					uuid = getUUID((String) id);
				}
			}
			
			reader.close();
			connection.disconnect();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return uuid;
	}

	private static String getTextureInfo(UUID uuid) {
		String base64 = null;
		try {
			URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			InputStream responseStream = connection.getInputStream();
			InputStreamReader reader = new InputStreamReader(responseStream);
			
			Object parsed = new JSONParser().parse(reader);
			if(parsed instanceof JSONObject) {
				JSONObject json = (JSONObject) parsed;
				Object properties = json.get("properties");
				if(properties instanceof JSONArray) {
					JSONArray propArray = (JSONArray) properties;
					for (Object prop : propArray) {
						if(prop instanceof JSONObject) {
							JSONObject propObj = (JSONObject) prop;
							if("textures".equals(propObj.get("name"))) {
								base64 = (String) propObj.get("value");
								break;
							}
						}
					}
				}
			}
			
			reader.close();
			connection.disconnect();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return base64;
	}

	private static void loadPlayerSkinFromInfo(String textureInfo, Launcher launcher) {

		String textureURL = null;
		String playername = "unknown player";
		PlayerModelType modelType = PlayerModelType.STEVE;
		
		try {
			Object parsed;
			parsed = new JSONParser().parse(textureInfo);
			if(parsed instanceof JSONObject) {
				playername = (String) ((JSONObject) parsed).get("profileName");
				Object textures = ((JSONObject) parsed).get("textures");
				if(textures instanceof JSONObject) {
					Object skin = ((JSONObject) textures).get("SKIN");
					if(skin instanceof JSONObject) {
						textureURL = (String) ((JSONObject) skin).get("url");
						Object meta = ((JSONObject) skin).get("metadata");
						if(meta instanceof JSONObject) {
							if("slim".equals(((JSONObject) meta).get("model")))
								modelType = PlayerModelType.ALEX;
						}
					}
				}
			}
		} catch (ParseException | ClassCastException e) {
			e.printStackTrace();
		}
		
		if(textureURL == null) return;
		
		try {
			URL url = new URL(textureURL);
			
			BufferedImage skin = ImageIO.read(url);
			launcher.modelviewer.setImage(skin);
			launcher.modelviewer.setModelType(modelType);
			launcher.customTitle = playername;
			
			switch (modelType) {
			case ALEX:
				launcher.menuBar.alexButton.setSelected(true);
				launcher.menuBar.steveButton.setAccelerator(KeyStroke.getKeyStroke("M"));
				launcher.menuBar.alexButton.setAccelerator(null);
				break;
			case STEVE:
				launcher.menuBar.steveButton.setSelected(true);
				launcher.menuBar.steveButton.setAccelerator(null);
				launcher.menuBar.alexButton.setAccelerator(KeyStroke.getKeyStroke("M"));
				break;
			}
			
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
