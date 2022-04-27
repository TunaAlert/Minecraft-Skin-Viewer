package de.tuna.mcmodelviewer;

public class InvalidTextureSizeException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4584393422698093668L;

	public int width;
	public int height;
	
	public InvalidTextureSizeException(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public String getMessage() {
		return "The texture size (" + width + "|" + height + ") is invalid.";
	}
	
}
