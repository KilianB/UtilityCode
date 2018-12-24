package com.github.kilianB.graphics;

import java.awt.image.BufferedImage;

/**
 * @author Kilian
 *
 */
public class FastPixelSlowDefault  implements FastPixel {

	/** Full alpha constant */
	private static final int ALPHA_MASK = 255 << 24;
	private static final int RED_MASK = 255 << 16;
	private static final int GREEN_MASK = 255 << 8;
	private static final int BLUE_MASK = 255 << 0;

	/** True if the underlying image has an alpha component */
	private final boolean alpha;

	/** Width of the image */
	private final int width;

	/** Height of the image */
	private final int height;

	/** Raw data */
	private final int[] rgbImageData;


	private BufferedImage bImage;
	/**
	 * Constructs a fast pixel object with the underlying buffered image.
	 * 
	 * <p>
	 * Note that calling this method may cause this DataBufferobject to be
	 * incompatible with performance optimizations used by some implementations
	 * (such as caching an associated image in video memory).
	 * 
	 * @param bImage The buffered image to extract data from
	 * @since 1.3.0
	 */
	public FastPixelSlowDefault(BufferedImage bImage) {

		
		alpha = bImage.getColorModel().hasAlpha();
		
		width = bImage.getWidth();
		height = bImage.getHeight();
		
		rgbImageData = bImage.getRGB(0,0,width,height,null,0,width);

	}
	@Override
	public int getRGB(int index) {
		return rgbImageData[index];
	}
	@Override
	public int[][] getRGB() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getAlpha(int index) {
		if(!alpha) {
			return -1;
		}else {
			return (rgbImageData[index] & ALPHA_MASK) >>> 24;
		}
	}
	@Override
	public int[][] getAlpha() {
		if (!alpha)
			return null;
		int[][] alpha = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < rgbImageData.length; i++) {
			alpha[x][y] = getAlpha(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return alpha;
	}
	@Override
	public void setAlpha(int index, int newAlpha) {
		
		//Get rgb. modify 
		
		//mask out alpha. shift new alpa in 
	}
	@Override
	public void setAlpha(int[][] newAlpha) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getRed(int index) {
		return (rgbImageData[index] & RED_MASK) >>> 16;
	}
	@Override
	public int[][] getRed() {
		int[][] red = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < rgbImageData.length; i++) {
			red[x][y] = getRed(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return red;
	}
	@Override
	public void setRed(int index, int newRed) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setRed(int[][] newRed) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getGreen(int index) {
		return (rgbImageData[index] & GREEN_MASK) >>> 8;
	}
	@Override
	public void setGreen(int index, int newGreen) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGreen(int[][] newGreen) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int[][] getGreen() {
		int[][] green = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < rgbImageData.length; i++) {
			green[x][y] = getGreen(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return green;
	}
	@Override
	public int getBlue(int index) {
		return rgbImageData[index] & BLUE_MASK;
	}
	@Override
	public void setBlue(int index, int newBlue) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int[][] getBlue() {
		int[][] blue = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < rgbImageData.length; i++) {
			blue[x][y] = getBlue(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return blue;
	}
	@Override
	public void setBlue(int[][] newBlue) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int[][] getAverageGrayscale() {
		int[][] gray = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < rgbImageData.length; i++) {
			gray[x][y] = getAverageGrayscale(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return gray;
	}
	@Override
	public void setAverageGrayscale(int[][] newGrayValue) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int[][] getLuma() {
		int[][] luma = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < rgbImageData.length; i++) {
			luma[x][y] = getLuma(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return luma;
	}
	@Override
	public int[] getLuma1D() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean hasAlpha() {
		return alpha;
	}
	@Override
	public int getOffset(int x, int y) {
		return (y * width) + x;
	}
}
