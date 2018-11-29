package com.github.kilianB.graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

import javax.imageio.ImageIO;

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
 * @since 1.3.0
 */
public class FastPixelByte implements FastPixel {

	/** Full alpha constant */
	private static final int ALPHA_MASK = 255 << 24;

	/** True if the underlying image has an alpha component */
	private final boolean alpha;
	/** Offset used in case alpha is present */
	private final int alphaOffset;
	/** Bytes used to represent a single pixel */
	private final int bytesPerColor;

	/** Width of the image */
	private final int width;

	/** Height of the image */
	private final int height;

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
	 * @since 1.3.0
	 */
	public FastPixelByte(BufferedImage bImage) {

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
		width = bImage.getWidth();
		height = bImage.getHeight();
	}

	@Override
	public int getRGB(int index) {
		return (alpha ? (imageData[index++] & 0xFF) << 24 : ALPHA_MASK) | ((imageData[index++] & 0xFF))
				| ((imageData[index++] & 0xFF) << 8) | ((imageData[index++] & 0xFF) << 16);
	}
	
	/**
	 * Returns an integer pixel in the default RGB color model(TYPE_INT_ARGB). There
	 * are only 8-bits of precision for each color component in the returned data
	 * when using this method. An ArrayOutOfBoundsException may be thrown if the
	 * coordinates are not in bounds.
	 * 
	 * @param x the X coordinate of the pixel from which to get the pixel in the
	 *          default RGB color model
	 * @param y the Y coordinate of the pixel from which to get the pixel in the
	 *          default RGB color model
	 * @return an integer pixel in the default RGB color model and default sRGB
	 *         colorspace.
	 * @since 1.3.0
	 */
	@Override
	public int getRGB(int x, int y) {
		int index = getOffset(x, y);
		return getRGB(index);
	}

	/**
	 * Returns the rgb values of the entire image in an 2 d array in the default RGB
	 * color model(TYPE_INT_ARGB). There are only 8-bits of precision for each color
	 * component in the returned data when using this method. An
	 * ArrayOutOfBoundsException may be thrown if the coordinates are not in bounds.
	 * 
	 * @return a 2d integer array containing the argb values of the image
	 * @since 1.3.0
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
	 * Get the alpha value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the alpha value in range [0-255] or -1 if alpha is not supported
	 * @since 1.3.0
	 */
	@Override
	public int getAlpha(int x, int y) {
		if (!alpha)
			return -1;
		return getAlpha(getOffset(x,y));
	}

	/**
	 * Get the alpha component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the alpha values or null if alpha is not supported
	 * @since 1.3.0
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
	public int getAlpha(int index) {
		return imageData[index] & 0xFF;
	}
	
	@Override
	public void setAlpha(int index, int newAlpha) {
		if (!alpha)
			return;
		imageData[index] = (byte) (newAlpha);
	}

	/**
	 * Set the alpha value of the specified pixel. This method is a NOP if alpha is
	 * not supported.
	 * 
	 * @param x        The x coordinate of the images' pixel
	 * @param y        The y coordinate of the images' pixel
	 * @param newAlpha the new alpha value in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public void setAlpha(int x, int y, int newAlpha) {
		setAlpha(getOffset(x,y),newAlpha);
	}
	
	/**
	 * Set new alpha values for the entire picture
	 * @param newAlpha red values in range [0-255]
	 * @since 1.4.5
	 */
	@Override
	public void setAlpha(int[][] newAlpha) {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				setAlpha(x,y,newAlpha[x][y]);
			}
		}
	}
	
	@Override
	public int getRed(int index) {
		return imageData[index + alphaOffset + 2] & 0xFF;
	}
	
	/**
	 * Get the red value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the red value in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public int getRed(int x, int y) {
		return getRed(getOffset(x, y));
	}
	
	/**
	 * Get the red component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the red values
	 * @since 1.3.0
	 */
	@Override
	public int[][] getRed() {
		int[][] red = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor) {
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
		imageData[index + alphaOffset + 2] = (byte) (newRed);
	}

	/**
	 * Set the red value of the specified pixel
	 * 
	 * @param x      The x coordinate of the images' pixel
	 * @param y      The y coordinate of the images' pixel
	 * @param newRed the new red value in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public void setRed(int x, int y, int newRed) {
		setRed(getOffset(x,y),newRed);
	}

	/**
	 * Set new red values for the entire picture
	 * @param newRed red values in range [0-255]
	 * @since 1.4.5
	 */
	@Override
	public void setRed(int[][] newRed) {
		//TODO inline method call?
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				setRed(x,y,newRed[x][y]);
			}
		}
	}

	@Override
	public int getGreen(int index) {
		return imageData[index + alphaOffset + 1] & 0xFF;
	}
	
	
	/**
	 * Get the green value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the green value in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public int getGreen(int x, int y) {
		return getGreen(getOffset(x, y));
	}

	@Override
	public void setGreen(int index, int newGreen) {
		imageData[index + alphaOffset + 1] = (byte) (newGreen);
	}
	
	/**
	 * Set the green value of the specified pixel
	 * 
	 * @param x        The x coordinate of the images' pixel
	 * @param y        The y coordinate of the images' pixel
	 * @param newGreen the new green value in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public void setGreen(int x, int y, int newGreen) {
		setGreen(getOffset(x,y),newGreen);
	}
	
	/**
	 * Set new green values for the entire picture
	 * @param newGreen red values in range [0-255]
	 * @since 1.4.5
	 */
	@Override
	public void setGreen(int[][] newGreen) {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				setGreen(x,y,newGreen[x][y]);
			}
		}
	}

	/**
	 * Get the green component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the green values
	 * @since 1.3.0
	 */
	@Override
	public int[][] getGreen() {
		int[][] green = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor) {
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
		return imageData[index + alphaOffset] & 0xFF;
	}

	/**
	 * Get the blue value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the blue value in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public int getBlue(int x, int y) {
		return getBlue(getOffset(x,y));
	}


	@Override
	public void setBlue(int index, int newBlue) {
		imageData[index + alphaOffset] = (byte) (newBlue);
	}
	
	/**
	 * Set the blue value of the specified pixel
	 * 
	 * @param x       The x coordinate of the images' pixel
	 * @param y       The y coordinate of the images' pixel
	 * @param newBlue the new blue value in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public void setBlue(int x, int y, int newBlue) {
		setBlue(getOffset(x,y),newBlue);
	}

	/**
	 * Get the blue component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the blue values
	 * @since 1.3.0
	 */
	@Override
	public int[][] getBlue() {
		int[][] blue = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor) {
			blue[x][y] = getBlue(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return blue;
	}
	
	/**
	 * Set new blue values for the entire picture
	 * @param newBlue red values in range [0-255]
	 * @since 1.4.5
	 */
	@Override
	public void setBlue(int[][] newBlue) {
		//TODO inline method call?
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				setBlue(x,y,newBlue[x][y]);
			}
		}
	}
	
	// Grayscale
	
	@Override
	public int getAverageGrayscale(int x, int y) {
		return getAverageGrayscale(getOffset(x,y));
	}

	@Override
	public int[][] getAverageGrayscale() {
		int[][] gray = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor) {
			gray[x][y] = getAverageGrayscale(x,y);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return gray;
	}

	@Override
	public void setAverageGrayscale(int x, int y, int newGrayValue) {
		setAverageGrayscale(getOffset(x,y),newGrayValue);
	}

	@Override
	public void setAverageGrayscale(int[][] newGrayValue) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setAverageGrayscale(x, y, newGrayValue[x][y]);
			}
		}
	}

	// YCrCb
	/**
	 * Return the Y(Luma) component of the YCbCr color model for the specified
	 * pixel.
	 * 
	 * @param x the x coordinate of the image
	 * @param y the y coordinate of the image
	 * @return the luma component in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public int getLuma(int x, int y) {
		return getLuma(getOffset(x,y));
	}

	/**
	 * Return the Y(Luma) component of the YCbCr color model fof the entire image
	 * mapped to a 2d array representing the x and y coordinates of the pixel.
	 * 
	 * @return the luma component in range [0-255]
	 * @since 1.3.1
	 */
	@Override
	public int[][] getLuma() {
		int luma[][] = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0 ; i < imageData.length; i += bytesPerColor) {
			
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
		int luma[]  = new int[width*height];
		for (int i = 0, j = 0; i < imageData.length; i += bytesPerColor, j++) {
			luma[j] =getLuma(i);
		}
		return luma;
	}
	

	/**
	 * Return the Cr(red-difference) component of the YCbCr color model for the
	 * specified pixel.
	 * 
	 * @param x the x coordinate of the image
	 * @param y the y coordinate of the image
	 * @return the cr component in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public int getCr(int x, int y) {
		return getCr(getOffset(x,y));
	}

	/**
	 * Return the Cb(blue-difference) component of the YCbCr color model for the
	 * specified pixel.
	 * 
	 * @param x the x coordinate of the image
	 * @param y the y coordinate of the image
	 * @return the cb component in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public int getCb(int x, int y) {
		return getCb(getOffset(x,y));
	}

	// HSV

	/**
	 * Return the hue component (angle) of the HSV color model for the specified pixel
	 * 
	 * @param x the x coordinate of the image
	 * @param y the y coordinate of the image
	 * @return the hue component in range [0-360]. As defined the hue is 0 for
	 *         undefined colors (e.g. white or black)
	 * @since 1.3.0
	 */
	@Override
	public int getHue(int x, int y) {
		return getHue(getOffset(x, y));
	}

	/**
	 * Return the saturation component of the HSV color model for the specified
	 * pixel
	 * 
	 * <p>
	 * Note: Opposed to all other values for the hsb model saturation is returned as
	 * double in the range of [0-1] instead of [0-255] to allow for a higher
	 * accuracy.
	 * 
	 * @param x the x coordinate of the image
	 * @param y the y coordinate of the image
	 * @return the sat component in range [0-1]. As defined the sat is 0 for
	 *         undefined colors (i.e. black)
	 * @since 1.3.0
	 */
	@Override
	public double getSat(int x, int y) {
		return getSat(getOffset(x,y));
	}

	/**
	 * Return the value component of the HSV color model for the specified pixel
	 * 
	 * @param x the x coordinate of the image
	 * @param y the y coordinate of the image
	 * @return the value component in range [0-255].
	 */
	@Override
	public int getVal(int x, int y) {
		return getVal(getOffset(x,y));
	}

	private int getOffset(int x, int y) {
		return (y * bytesPerColor * width) + (x * bytesPerColor);
	}

	@Override
	public boolean hasAlpha() {
		return alpha;
	}

	
}