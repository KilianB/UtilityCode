package dev.brachtendorf.graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import dev.brachtendorf.MathUtil;

/**
 * @author Kilian
 *
 */
public class FastPixelInt extends FastPixelImpl {
	
	/** Full alpha constant */
	private static final int FULL_ALPHA = 255 << 24;

	/** Raw data */
	private int[] imageData;

	private int redMask;
	private int greenMask;
	private int blueMask;
	private int alphaMask;

	private int alphaOffset;
	private int redOffset;
	private int blueOffset;
	private int greenOffset;

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
	public FastPixelInt(BufferedImage bImage) {

		super(bImage.getWidth(),bImage.getHeight());
		
		imageData = ((DataBufferInt) bImage.getRaster().getDataBuffer()).getData();

		switch (bImage.getType()) {

		case BufferedImage.TYPE_INT_ARGB:
			redMask 	= 0x00ff0000;
			greenMask 	= 0x0000ff00;
			blueMask 	= 0x000000ff;
			alphaMask 	= 0xff000000;
			alpha = true;
			break;
		case BufferedImage.TYPE_INT_RGB:
			redMask 	= 0x00ff0000;
			greenMask 	= 0x0000ff00;
			blueMask 	= 0x000000ff;
			alphaMask 	= 0x00000000;
			break;
		case BufferedImage.TYPE_INT_BGR:
			redMask 	= 0x000000ff;
			greenMask 	= 0x0000ff00;
			blueMask 	= 0x00ff0000;
			alphaMask 	= 0x00000000;
			break;
		}

		redOffset = MathUtil.getLowerShiftBitMask(redMask);
		greenOffset = MathUtil.getLowerShiftBitMask(greenMask);
		blueOffset = MathUtil.getLowerShiftBitMask(blueMask);
		alphaOffset = MathUtil.getLowerShiftBitMask(alphaMask);

	}
	
	@Override
	public int getRGB(int index) {
		return (alpha ? (getAlpha(index) << 24) : FULL_ALPHA) | (getRed(index) << 16)
			| (getGreen(index) << 8) | (getBlue(index));
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
	 * @since 1.3.0 com.github.kilianB
	 */
	@Override
	public int getRGB(int x, int y) {
		return getRGB(getOffset(x,y));
	}

	@Override
	public int getAlphaInternal(int index) {
		if (!alpha)
			return -1;
		return (imageData[index] & alphaMask) >>> alphaOffset;
	}

	@Override
	public void setAlpha(int index, int newAlpha) {
		if (!alpha)
			return;
		imageData[index] |= (newAlpha << alphaOffset);
	}
	
	@Override
	protected int getRedInternal(int index) {
		return (imageData[index] & redMask) >>> redOffset;
	}
		
	@Override
	public void setRed(int index, int newRed) {
		//Clear red part first	
		imageData[index] = (imageData[index] & (~redMask)) | (newRed << redOffset);
	}

	/**
	 * Set the red value of the specified pixel
	 * 
	 * @param x      The x coordinate of the images' pixel
	 * @param y      The y coordinate of the images' pixel
	 * @param newRed the new red value in range [0-255]
	 * @since 1.3.0 com.github.kilianB
	 */
	@Override
	public void setRed(int x, int y, int newRed) {
		setRed(getOffset(x,y),newRed);
	}

	@Override
	protected int getGreenInternal(int index) {
		return (imageData[index] & greenMask) >>> greenOffset;
	}
	
	@Override
	public void setGreen(int index, int newGreen) {
		//Clear green part first	
		imageData[index] = (imageData[index] & (~greenMask)) | (newGreen << greenOffset);
	}

	
	@Override
	protected int getBlueInternal(int index) {
		return (imageData[index] & blueMask) >>> blueOffset;
	}

	@Override
	public void setBlue(int index, int newBlue) {
		imageData[index] = (imageData[index] & (~blueMask)) | (newBlue << blueOffset);
	}
	
	// grayscale


	

	// YCrCb

	public int getOffset(int x, int y) {
		return (y * width) + x;
	}
}
