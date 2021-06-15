package com.github.kilianB.graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * High performant access of RGB/YCrCb/HSV Data.
 * 
 * <p>
 * RGB methods run faster by a magnitude (factor 10) compared to the original
 * java getRGB method.
 * 
 * <p>
 * Currently only ARGB and RGB image types are supported.
 * 
 * @author Kilian
 * @since 1.3.0 com.github.kilianB
 */
public class FastPixelByte extends FastPixelImpl{

	/** Full alpha constant */
	private static final int ALPHA_MASK = 255 << 24;

	/** Offset used in case alpha is present */
	private final int alphaOffset;
	/** Bytes used to represent a single pixel */
	private final int bytesPerColor;

	/** Raw data */
	private final byte[] imageData;

	/**
	 * Constructs a fast pixel object with the underlying buffered image.
	 * 
	 * <p>
	 * Note that calling this method may cause this DataBufferobject to be
	 * incompatible with performance optimizations used by some implementations
	 * (such as caching an associated image in video memory).
	 * 
	 * @param bImage The buffered image to extract data from
	 * @since 1.3.0 com.github.kilianB
	 */
	public FastPixelByte(BufferedImage bImage) {
		super(bImage.getWidth(),bImage.getHeight());
		imageData = ((DataBufferByte) bImage.getRaster().getDataBuffer()).getData();

		if (bImage.getColorModel().hasAlpha()) {
			alphaOffset = 1;
			alpha = true;
			bytesPerColor = 4;
		} else {
			alphaOffset = 0;
			alpha = false;
			bytesPerColor = 3;
		}
	}

	@Override
	public int getRGB(int index) {
		return (alpha ? (imageData[index++] & 0xFF) << 24 : ALPHA_MASK) | ((imageData[index++] & 0xFF))
				| ((imageData[index++] & 0xFF) << 8) | ((imageData[index++] & 0xFF) << 16);
	}

	/**
	 * Returns the rgb values of the entire image in an 2 d array in the default RGB
	 * color model(TYPE_INT_ARGB). There are only 8-bits of precision for each color
	 * component in the returned data when using this method. An
	 * ArrayOutOfBoundsException may be thrown if the coordinates are not in bounds.
	 * 
	 * @return a 2d integer array containing the argb values of the image
	 * @since 1.3.0 com.github.kilianB
	 */
	@Override
	public int[][] getRGB() {
		int[][] rgb = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i++) {
			// We could use the getRGB(x,y) method. but lets inline some calls
			int argb;
			argb = alpha ? (imageData[i++] & 0xFF) << 24 : ALPHA_MASK;
			// Red
			argb |= (imageData[i++] & 0xFF) | (imageData[i++] & 0xFF) << 8 | (imageData[i] & 0xFF) << 16;

			rgb[x][y] = argb;
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return rgb;
	}

	/**
	 * Get the alpha component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the alpha values or null if alpha is not supported
	 * @since 1.3.0 com.github.kilianB
	 */
	@Override
	public int[][] getAlpha() {
		if (!alpha)
			return null;
		int[][] alpha = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor) {
			alpha[x][y] = (imageData[i] & 0xFF);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return alpha;
	}

	@Override
	public int getAlphaInternal(int index) {
		if (!alpha)
			return -1;
		return imageData[index] & 0xFF;
	}

	@Override
	public void setAlpha(int index, int newAlpha) {
		if (!alpha)
			return;
		imageData[index] = (byte) (newAlpha);
	}

	@Override
	public int getRedInternal(int index) {
		return imageData[index + alphaOffset + 2] & 0xFF;
	}
	

	@Override
	public void setRed(int index, int newRed) {
		imageData[index + alphaOffset + 2] = (byte) (newRed);
	}


	@Override
	public int getGreenInternal(int index) {
		return imageData[index + alphaOffset + 1] & 0xFF;
	}

	@Override
	public void setGreen(int index, int newGreen) {
		imageData[index + alphaOffset + 1] = (byte) (newGreen);
	}

	@Override
	public int getBlueInternal(int index) {
		return imageData[index + alphaOffset] & 0xFF;
	}

	@Override
	public void setBlue(int index, int newBlue) {
		imageData[index + alphaOffset] = (byte) (newBlue);
	}

	
	// YCrCb

	public int getOffset(int x, int y) {
		return (y * bytesPerColor * width) + (x * bytesPerColor);
	}

	@Override
	public int[] getLuma1D() {
		int luma[] = new int[width * height];
		for (int i = 0, j = 0; i < imageData.length; i += bytesPerColor, j++) {
			luma[j] = getLuma(i);
		}
		return luma;
	}
	
	@Override
	public int[] getRed1D() {
		int[] red = new int[width * height];
		int j = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor,j++) {
			red[j] = getRed(i);
		}
		return red;
	}

	@Override
	public int[] getGreen1D() {
		int[] green = new int[width * height];
		int j = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor,j++) {
			green[j] = getGreen(i);
		}
		return green;
	}

	@Override
	public int[] getBlue1D() {
		int[] blue = new int[width * height];
		int j = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor,j++) {
			blue[j] = getBlue(i);
		}
		return blue;
	}

}