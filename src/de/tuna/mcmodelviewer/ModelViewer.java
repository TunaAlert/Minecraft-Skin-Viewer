package de.tuna.mcmodelviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.MouseInputListener;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.tuna.mcmodelviewer.animation.Animation;
import de.tuna.mcmodelviewer.animation.AnimationList;
import de.tuna.mcmodelviewer.model.PlayerModel;
import de.tuna.mcmodelviewer.model.PlayerModelData;
import de.tuna.mcmodelviewer.model.PlayerModelType;

public class ModelViewer extends JPanel implements MouseInputListener, MouseWheelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7344406448651391537L;

	private static final PlayerModel alexDouble = loadModel("/models/alex_double.xml");
	private static final PlayerModel steveDouble = loadModel("/models/steve_double.xml");
	private static final PlayerModel alexSingle = loadModel("/models/alex_single.xml");
	private static final PlayerModel steveSingle = loadModel("/models/steve_single.xml");

	public static final int TEXTURE_SOURCE_FILE = 0;
	public static final int TEXTURE_SOURCE_BUFFER = 1;
	public static final int TEXTURE_SOURCE_TRANSFERABLE = 2;

	public static final int BACKGROUND_COLOR = 0;
	public static final int BACKGROUND_IMAGE = 1;
	public static final int BACKGROUND_SKYBOX = 2;
	public static final int BACKGROUND_ENVIRONMENT = 3;

	public static final int MAP_STRETCH = 0;
	public static final int MAP_FILL = 1;
	public static final int MAP_CONTAIN = 2;
	public static final int MAP_REPEAT = 2;
	
	private final Launcher launcher;

	private int background = 0;
	
	private int backgroundTexture = 0;
	private BufferedImage backgroundImage = null;
	private int backgroundMap = 1; 
	
	private int textureSource = 1;
	private File currentFile = new File(System.getProperty("user.dir"));
	private BufferedImage currentImage = null;
	private Transferable currentTransfer = null;
	
	private PlayerModelRenderer renderer;
	private PlayerModel selectedModel = alexDouble;
	
	private PlayerModelType selectedType = PlayerModelType.ALEX;
	
	private AWTGLCanvas canvas;
	private boolean closed;
	
	private float camRotYaw = 0;
	private float camRotPitch = 30;
	private float newCamDistance = 5;
	private float camDistance = 5;
	private boolean camPerspective = true;
	
	private Animation animation;
	
	private int mouseX = 0;
	private int mouseY = 0;

	private boolean forceReload;

	public ModelViewer(Launcher launcher) {
		this.launcher = launcher;
		renderer = new PlayerModelRenderer();
		setLayout(new BorderLayout(0, 0));
		closed = false;
	}
	
	private static PlayerModel loadModel(String name) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document doc = db.parse(ModelViewer.class.getResourceAsStream(name));
			
			return PlayerModel.load(doc);
		} catch (ParserConfigurationException | IllegalArgumentException | SAXException | IOException e) {
			System.err.println("Model " + name + " couldn't be loaded.");
			return null;
		}
	}
	
	public PlayerModelData getModelData() {
		return renderer.getModelData();
	}
	
	public void setModelData(PlayerModelData modelData) {
		renderer.setModelData(modelData);
	}
	
	public PlayerModelType getModelType() {
		return selectedType;
	}

	public void setModelType(PlayerModelType modelType) {
		this.selectedType = modelType;
		forceReload = true;
	}

	public void init(){
		try {
			canvas = new AWTGLCanvas();
			add(canvas, BorderLayout.CENTER);
			canvas.setFocusable(false);
			canvas.setEnabled(false);
			doLayout();
			Display.setDisplayMode(new DisplayMode(canvas.getWidth(), canvas.getHeight()));
			//System.out.println(canvas.getWidth() + " " + canvas.getHeight());
			Display.setParent(canvas);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);

		try {
			String skin;
			if(Math.random() < 0.5) {
				skin = "/textures/alex.png";
				selectedType = PlayerModelType.ALEX;
				launcher.menuBar.steveButton.setAccelerator(KeyStroke.getKeyStroke("M"));
				launcher.menuBar.alexButton.setAccelerator(null);
			}else{
				skin = "/textures/steve.png";
				selectedType = PlayerModelType.STEVE;
				launcher.menuBar.steveButton.setAccelerator(null);
				launcher.menuBar.alexButton.setAccelerator(KeyStroke.getKeyStroke("M"));
			}
			loadTexture(ModelViewer.class.getResourceAsStream(skin));
		} catch (IOException | InvalidTextureSizeException e) {
			e.printStackTrace();
		}
		
		animation = AnimationList.getAnimation("Breathe");
		
		initGL();

		renderer.setPlayerTexture(GL11.glGenTextures());
	}
	
	private void initGL() {
		 initGLPerspective();
		 GL11.glEnable(GL11.GL_DEPTH_TEST);
		 GL11.glCullFace(GL11.GL_FRONT);
		 GL11.glEnable(GL11.GL_TEXTURE_2D);
		 GL11.glEnable(GL11.GL_CULL_FACE);
		 GL11.glEnable(GL11.GL_BLEND);
		 GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		 GL11.glEnable(GL11.GL_ALPHA_TEST);
		 GL11.glAlphaFunc(GL11.GL_GREATER, 0.5f);
		 backgroundTexture = GL11.glGenTextures();
		 loadTexture(null, backgroundTexture);
	}
	
	private void initGLPerspective() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    float aspect = (float)canvas.getWidth() / canvas.getHeight();
    if(camPerspective) {
    	GLU.gluPerspective(2*(float)Math.toDegrees(Math.atan(1.5f/camDistance)), aspect, 0.25f, 100f);
    }else {
    	GL11.glOrtho(-1.5*aspect, 1.5*aspect, -1.5, 1.5, 0, 100);
    }
		GL11.glViewport(0, 0, canvas.getWidth(), canvas.getHeight());
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	private void initGLSkyboxPerspective() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    float aspect = (float)canvas.getWidth() / canvas.getHeight();
  	GLU.gluPerspective(90, aspect, 0.25f, 4f);
		GL11.glViewport(0, 0, canvas.getWidth(), canvas.getHeight());
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	private void initGLOrtho() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
  	GL11.glOrtho(0, canvas.getWidth(), canvas.getHeight(), 0, -1, 1);
		GL11.glViewport(0, 0, canvas.getWidth(), canvas.getHeight());
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	private void loadTexture(InputStream in) throws IOException, InvalidTextureSizeException {
		BufferedImage tex = ImageIO.read(in);
		loadTexture(tex);
		currentImage = tex;
	}

	private void loadTextureFromTransferable() throws IOException, InvalidTextureSizeException, UnsupportedFlavorException {
		textureSource = TEXTURE_SOURCE_BUFFER;
	  if (currentTransfer != null) {
	  	if(currentTransfer.isDataFlavorSupported(DataFlavor.imageFlavor)) {
	  		Image img =  (Image) currentTransfer.getTransferData(DataFlavor.imageFlavor);
	  		BufferedImage tex = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	  		Graphics g = tex.getGraphics();
	  		g.drawImage(img, 0, 0, null);
	  		loadTexture(tex);
	  		currentImage = tex;
	  	}else if(currentTransfer.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
	  		textureSource = TEXTURE_SOURCE_FILE;
	  		@SuppressWarnings("unchecked")
				List<File> fileList = (List<File>) currentTransfer.getTransferData(DataFlavor.javaFileListFlavor);
	  		if(fileList.size() > 0) {
	  			currentFile = fileList.get(0);
	  		}
	  	}else if(currentTransfer.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	  		String pasted = (String) currentTransfer.getTransferData(DataFlavor.stringFlavor);
	  		PlayerSkinLoader.loadPlayerSkin(pasted, launcher);
	  	}
	  }
	}
	
	private void loadTexture(BufferedImage tex, int target){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, target);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		if(tex == null) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(16);
			buffer.put(new byte[] {
					~0, 0, ~0, ~0,
					0, 0, 0, ~0,
					0, 0, 0, ~0,
					~0, 0, ~0, ~0
					});
			buffer.flip();
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, 2, 2, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
			return;
		}
		
		ByteBuffer texBuffer = convertImageData(tex);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, tex.getWidth(), tex.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, texBuffer);
	}
	
	private void loadTexture(BufferedImage tex) throws InvalidTextureSizeException {
		int w = tex.getWidth();
		int h = tex.getHeight();
		if(w < 8 || (w & (w>>1)) != 0 || h != w && h != w/2) throw new InvalidTextureSizeException(tex.getWidth(), tex.getHeight());
		int aspect = w/h;
		
		loadTexture(tex, renderer.getPlayerTexture());
		
		switch (selectedType) {
		case STEVE:
			if(aspect == 2)
				selectedModel = steveSingle;
			else
				selectedModel = steveDouble;
			break;
		case ALEX:
			if(aspect == 2)
				selectedModel = alexSingle;
			else
				selectedModel = alexDouble;
			break;
		}
	}
	private ByteBuffer convertImageData(BufferedImage image) {
	    int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
	    ByteBuffer buffer = ByteBuffer.allocateDirect(pixels.length * 4);
	    for (int pixel : pixels) {
	        buffer.put((byte) ((pixel >> 16) & 0xFF));
	        buffer.put((byte) ((pixel >> 8) & 0xFF));
	        buffer.put((byte) (pixel & 0xFF));
	        buffer.put((byte) (pixel >> 24));
	    }
	    buffer.flip();
	    return buffer;
	}
	
	public void run() {
		long zero = System.currentTimeMillis();
		long lastReload = zero;
		File lastLoaded = null;
		BufferedImage lastTex = null;
		BufferedImage lastBgImage = null;
		while(!closed) {
			if(newCamDistance != camDistance || Display.wasResized()){
				camDistance = newCamDistance;
				doLayout();
			}
			
			if ((background == BACKGROUND_IMAGE || background == BACKGROUND_SKYBOX) && backgroundImage != lastBgImage) {
				lastBgImage = backgroundImage;
				loadTexture(backgroundImage, backgroundTexture);
			}
			
			long now = System.currentTimeMillis();
			renderer.setModelData(animation.setDataAtTime(renderer.getModelData(), (float)(now-zero)/1000f));
			
			switch (textureSource) {
			case TEXTURE_SOURCE_FILE:
				if(currentFile.lastModified()+1000 < now && (currentFile != lastLoaded || currentFile.lastModified() > lastReload || forceReload)) {
					try {
						loadTexture(new FileInputStream(currentFile));
						lastLoaded = currentFile;
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(this, "The file couldn't be found.", "Not loaded", JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(this, "The file couldn't be loaded.", "Not loaded", JOptionPane.ERROR_MESSAGE);
					} catch (InvalidTextureSizeException e1) {
						JOptionPane.showMessageDialog(this, "The image must be 64x32 or 64x64.", "Not loaded", JOptionPane.ERROR_MESSAGE);
					}
					currentFile = lastLoaded;
					lastReload = now;
					lastTex = currentImage;
					forceReload = false;
				}
				break;
			case TEXTURE_SOURCE_BUFFER:
				if(currentImage != lastTex || forceReload) {
					try {
						loadTexture(currentImage);
					} catch (InvalidTextureSizeException e) {
						e.printStackTrace();
					}
					lastTex = currentImage;
				}
				break;
			case TEXTURE_SOURCE_TRANSFERABLE:
				try {
					loadTextureFromTransferable();
				} catch (IOException | InvalidTextureSizeException | UnsupportedFlavorException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
			
			
			renderGL();
			
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
	}

	private void renderGL() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		Color bg = getBackground();
		GL11.glClearColor(bg.getRed()/255f, bg.getGreen()/255f, bg.getBlue()/255f, 1f);
		
		if(background == BACKGROUND_IMAGE) {
			initGLOrtho();

			float u1 = 0;
			float v1 = 0;
			float u2 = 1;
			float v2 = 1;
			
			float x1 = 0;
			float y1 = 0;
			float x2 = canvas.getWidth();
			float y2 = canvas.getHeight();
			
			if(backgroundMap == MAP_FILL) {
				float canvasAspect = (float)canvas.getWidth()/canvas.getHeight();
				float imageAspect = (float)backgroundImage.getWidth()/backgroundImage.getHeight();
				if(canvasAspect < imageAspect) {
					x1 = (canvas.getWidth() - canvas.getHeight()*imageAspect)*0.5f;
					x2 = (canvas.getWidth() + canvas.getHeight()*imageAspect)*0.5f;
				}else {
					y1 = (canvas.getHeight() - canvas.getHeight()/imageAspect)*0.5f;
					y2 = (canvas.getHeight() + canvas.getHeight()/imageAspect)*0.5f;
				}
			}else if(backgroundMap == MAP_CONTAIN) {
				
			}else if(backgroundMap == MAP_REPEAT) {
				
			}
			
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTexture);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(u1, v1);GL11.glVertex2f(x1, y1);
			GL11.glTexCoord2f(u2, v1);GL11.glVertex2f(x2, y1);
			GL11.glTexCoord2f(u2, v2);GL11.glVertex2f(x2, y2);
			GL11.glTexCoord2f(u1, v2);GL11.glVertex2f(x1, y2);
			GL11.glEnd();
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		}else if(background == BACKGROUND_SKYBOX) {
			initGLSkyboxPerspective();
			GL11.glPushMatrix();
	    GL11.glRotatef(camRotPitch, 1, 0, 0);
	    GL11.glRotatef(camRotYaw, 0, 1, 0);
			GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
	    
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTexture);
			
			GL11.glBegin(GL11.GL_QUADS);
			float s = 0.25f;
			//front
			GL11.glTexCoord2f(2*s, 2*s);GL11.glVertex3f(0, 1, 1);
			GL11.glTexCoord2f(1*s, 2*s);GL11.glVertex3f(1, 1, 1);
			GL11.glTexCoord2f(1*s, 4*s);GL11.glVertex3f(1, 0, 1);
			GL11.glTexCoord2f(2*s, 4*s);GL11.glVertex3f(0, 0, 1);
			//right
			GL11.glTexCoord2f(3*s, 2*s);GL11.glVertex3f(0, 1, 0);
			GL11.glTexCoord2f(2*s, 2*s);GL11.glVertex3f(0, 1, 1);
			GL11.glTexCoord2f(2*s, 4*s);GL11.glVertex3f(0, 0, 1);
			GL11.glTexCoord2f(3*s, 4*s);GL11.glVertex3f(0, 0, 0);
			//back
			GL11.glTexCoord2f(4*s, 2*s);GL11.glVertex3f(1, 1, 0);
			GL11.glTexCoord2f(3*s, 2*s);GL11.glVertex3f(0, 1, 0);
			GL11.glTexCoord2f(3*s, 4*s);GL11.glVertex3f(0, 0, 0);
			GL11.glTexCoord2f(4*s, 4*s);GL11.glVertex3f(1, 0, 0);
			//left
			GL11.glTexCoord2f(1*s, 2*s);GL11.glVertex3f(1, 1, 1);
			GL11.glTexCoord2f(0*s, 2*s);GL11.glVertex3f(1, 1, 0);
			GL11.glTexCoord2f(0*s, 4*s);GL11.glVertex3f(1, 0, 0);
			GL11.glTexCoord2f(1*s, 4*s);GL11.glVertex3f(1, 0, 1);
			//top
			GL11.glTexCoord2f(2*s, 0*s);GL11.glVertex3f(0, 1, 0);
			GL11.glTexCoord2f(1*s, 0*s);GL11.glVertex3f(1, 1, 0);
			GL11.glTexCoord2f(1*s, 2*s);GL11.glVertex3f(1, 1, 1);
			GL11.glTexCoord2f(2*s, 2*s);GL11.glVertex3f(0, 1, 1);
			//top
			GL11.glTexCoord2f(3*s, 0*s);GL11.glVertex3f(0, 0, 0);
			GL11.glTexCoord2f(3*s, 2*s);GL11.glVertex3f(0, 0, 1);
			GL11.glTexCoord2f(2*s, 2*s);GL11.glVertex3f(1, 0, 1);
			GL11.glTexCoord2f(2*s, 0*s);GL11.glVertex3f(1, 0, 0);
			GL11.glEnd();
			
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glPopMatrix();
		}
		
		initGLPerspective();
		
    GL11.glPushMatrix();
    GL11.glTranslatef(0, 0, -camDistance);
    GL11.glRotatef(camRotPitch, 1, 0, 0);
    GL11.glRotatef(camRotYaw, 0, 1, 0);
    GL11.glTranslatef(renderer.getModelData().getXOffset(), renderer.getModelData().getYOffset()-1, renderer.getModelData().getZOffset());

    final float cosp = (float)Math.cos(Math.toRadians(-camRotPitch));
    final float sinp = (float)Math.sin(Math.toRadians(-camRotPitch));
    final float cosy = (float)Math.cos(Math.toRadians(camRotYaw));
    final float siny = (float)Math.sin(Math.toRadians(camRotYaw));
    
		Vector3f camPos = new Vector3f(0, 0, camDistance);
		camPos.set(
				camPos.x,
				camPos.y*cosp - camPos.z*sinp,
				camPos.y*sinp + camPos.z*cosp);
		camPos.set(
				camPos.x*cosy - camPos.z*siny,
				camPos.y,
				camPos.x*siny + camPos.z*cosy);
		camPos.translate(0, 1, 0);
    
    renderer.renderModel(selectedModel, camPos);
    
    GL11.glPopMatrix();
	}

	public boolean isClosed() {
		return closed;
	}

	public void close() {
		this.closed = true;
	}

	public File getFile() {
		return currentFile;
	}

	public void setFile(File file) {
		this.currentFile = file;
		setTextureSource(TEXTURE_SOURCE_FILE);
	}

	public BufferedImage getImage() {
		return currentImage;
	}

	public void setImage(BufferedImage texture) {
		this.currentImage = texture;
		setTextureSource(TEXTURE_SOURCE_BUFFER);
	}

	public void setTransferable(Transferable transferable) {
		this.currentTransfer = transferable;
		setTextureSource(TEXTURE_SOURCE_TRANSFERABLE);
	}

	public void setTextureSource(int textureSource) {
		this.textureSource = textureSource;
	}

	public int getTextureSource() {
		return textureSource;
	}

	public void setLightEffectEnabled(boolean enabled) {
		renderer.setLightEffectEnabled(enabled);
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
	
	@Override
	public void setBackground(Color bg) {
		background = BACKGROUND_COLOR;
		super.setBackground(new Color(bg.getRGB() | 0xFF000000));
	}

	public void setBackgroundImage(BufferedImage bg) {
		background = BACKGROUND_IMAGE;
		if(bg == null) {
			bg = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
			bg.setRGB(0, 0, 2, 2, new int[] {0xFFFF00FF, 0xFF000000, 0xFF000000, 0xFFFF00FF}, 0, 2);
		}
		backgroundImage = bg;
	}

	public void setSkyboxTexture(BufferedImage bg) {
		background = BACKGROUND_SKYBOX;
		if(bg == null) {
			bg = new BufferedImage(8, 4, BufferedImage.TYPE_INT_ARGB);
			bg.setRGB(0, 0, 8, 4, new int[] {
					0x00000000, 0x00000000, 0xFF000000, 0xFFFF00FF, 0xFF000000, 0xFFFF00FF, 0x00000000, 0x00000000,
					0x00000000, 0x00000000, 0xFFFF00FF, 0xFF000000, 0xFFFF00FF, 0xFF000000, 0x00000000, 0x00000000,
					0xFFFF00FF, 0xFF000000, 0xFFFF00FF, 0xFF000000, 0xFFFF00FF, 0xFF000000, 0xFFFF00FF, 0xFF000000,
					0xFF000000, 0xFFFF00FF, 0xFF000000, 0xFFFF00FF, 0xFF000000, 0xFFFF00FF, 0xFF000000, 0xFFFF00FF
					}, 0, 8);
		}
		backgroundImage = bg;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		int dx = e.getX() - mouseX;
		int dy = e.getY() - mouseY;
		
		camRotPitch += dy;
		camRotYaw += dx;
		
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		newCamDistance *= Math.pow(1.2f, e.getPreciseWheelRotation());
		final float nearest = 1.4f;
		final float farthest = 85f;
		final float orthoThreshold = 84f;
		if(newCamDistance < nearest) newCamDistance = nearest;
		if(newCamDistance > farthest) newCamDistance = farthest;
		camPerspective = newCamDistance <= orthoThreshold;
	}
	
}
