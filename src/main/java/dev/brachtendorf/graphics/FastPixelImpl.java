package dev.brachtendorf.graphics;

import dev.brachtendorf.ArrayUtil;

/**
 * @author Kilian
 *
 */
public abstract class FastPixelImpl implements FastPixel {

	/** Width of the image */
	protected final int width;
	/** Height of the image */
	protected final int height;

	/** True if the underlying image has an alpha component */
	protected boolean alpha = false;

	protected int alphaReplacementThreshold = -1;
	protected int replacementR = -1;
	protected int replacementG = -1;
	protected int replacementB = -1;
	protected int replacementA = -1;

	@Override
	public boolean isReplaceOpaqueColors() {
		return alphaReplacementThreshold >= 0;
	}

	@Override
	public void setReplaceOpaqueColors(int alphaThreshold, int r, int g, int b, int a) {
		alphaReplacementThreshold = alphaThreshold;
		replacementR = r;
		replacementG = g;
		replacementB = b;
		replacementA = a;
	}

	public FastPixelImpl(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public int[] getRed1D() {
		int[] red = new int[width * height];
		ArrayUtil.fillArray(red, i -> {
			return getRed(i);
		});
		return red;
	}

	@Override
	public int[] getBlue1D() {
		int[] blue = new int[width * height];
		ArrayUtil.fillArray(blue, i -> {
			return getBlue(i);
		});
		return blue;
	}

	@Override
	public int[] getGreen1D() {
		int[] green = new int[width * height];
		ArrayUtil.fillArray(green, i -> {
			return getGreen(i);
		});
		return green;
	}

	@Override
	public int[] getLuma1D() {
		int luma[] = new int[width * height];
		ArrayUtil.fillArray(luma, i -> {
			return getLuma(i);
		});
		return luma;
	}

	@Override
	public int getAverageGrayscale(int index) {
		return (getRed(index) + getGreen(index) + getBlue(index)) / 3;
	}

	@Override
	public int getLuma(int index) {

		int r;
		int g;
		int b;

		r = getRed(index);
		g = getGreen(index);
		b = getBlue(index);

		int lum = (int) ((r) * ColorUtil.LUMA_RED + (g) * ColorUtil.LUMA_GREEN + (b) * ColorUtil.LUMA_BLUE);
		return lum > 255 ? 255 : lum;
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
		for (int i = 0; i < width * height; i++) {
			rgb[x][y] = getRGB(x, y);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return rgb;
	}

	/**
	 * Get the red component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the red values
	 * @since 1.3.0 com.github.kilianB
	 */
	@Override
	public int[][] getRed() {
		int[][] red = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < width * height; i++) {
			red[x][y] = getRed(x, y);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return red;
	}

	@Override
	public int[][] getGreen() {
		int[][] green = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < width * height; i++) {
			green[x][y] = getGreen(x, y);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return green;
	}

	@Override
	public int[][] getBlue() {
		int[][] blue = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < width * height; i++) {
			blue[x][y] = getBlue(x, y);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return blue;
	}

	@Override
	public int[][] getAlpha() {
		if (!hasAlpha())
			return null;
		int[][] alpha = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < width * height; i++) {
			alpha[x][y] = getAlpha(x, y);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return alpha;
	}

	@Override
	public int[][] getLuma() {
		int luma[][] = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < width * height; i++) {
			luma[x][y] = getLuma(x, y);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return luma;
	}

	@Override
	public int[][] getAverageGrayscale() {
		int[][] gray = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < width * height; i++) {
			gray[x][y] = getAverageGrayscale(x, y);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return gray;
	}

	/**
	 * Set new alpha values for the entire picture
	 * 
	 * @param newAlpha red values in range [0-255]
	 * @since 1.4.5 com.github.kilianB
	 */
	@Override
	public void setAlpha(int[][] newAlpha) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setAlpha(x, y, newAlpha[x][y]);
			}
		}
	}

	/**
	 * Set new red values for the entire picture
	 * 
	 * @param newRed red values in range [0-255]
	 * @since 1.4.5 com.github.kilianB
	 */
	@Override
	public void setRed(int[][] newRed) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setRed(x, y, newRed[x][y]);
			}
		}
	}

	/**
	 * Set new green values for the entire picture
	 * 
	 * @param newGreen red values in range [0-255]
	 * @since 1.4.5 com.github.kilianB
	 */
	@Override
	public void setGreen(int[][] newGreen) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setGreen(x, y, newGreen[x][y]);
			}
		}
	}

	/**
	 * Set new blue values for the entire picture
	 * 
	 * @param newBlue red values in range [0-255]
	 * @since 1.4.5 com.github.kilianB
	 */
	@Override
	public void setBlue(int[][] newBlue) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setBlue(x, y, newBlue[x][y]);
			}
		}
	}

	@Override
	public void setAverageGrayscale(int[][] newGrayValue) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setAverageGrayscale(x, y, newGrayValue[x][y]);
			}
		}
	}

	@Override
	public final int getRed(int offset) {
		// return the red value of the replacement color if we mask out opaque pixels
		if (hasAlpha() && isReplaceOpaqueColors() && getAlphaInternal(offset) <= alphaReplacementThreshold) {
			return replacementR;
		}
		return getRedInternal(offset);
	}

	@Override
	public final int getGreen(int offset) {
		// return the red value of the replacement color if we mask out opaque pixels
		if (hasAlpha() && isReplaceOpaqueColors() && getAlphaInternal(offset) <= alphaReplacementThreshold) {
			return replacementG;
		}
		return getGreenInternal(offset);
	}

	@Override
	public final int getBlue(int offset) {
		// return the red value of the replacement color if we mask out opaque pixels
		if (hasAlpha() && isReplaceOpaqueColors() && getAlphaInternal(offset) <= alphaReplacementThreshold) {
			return replacementB;
		}
		return getBlueInternal(offset);
	}

	@Override
	public final int getAlpha(int offset) {
		// return the red value of the replacement color if we mask out opaque pixels
		if (hasAlpha() && isReplaceOpaqueColors() && getAlphaInternal(offset) <= alphaReplacementThreshold) {
			return replacementA;
		}
		return getAlphaInternal(offset);
	}



	protected abstract int getRedInternal(int offset);

	protected abstract int getGreenInternal(int offset);

	protected abstract int getBlueInternal(int offset);

	protected abstract int getAlphaInternal(int offset);


	@Override
	public boolean hasAlpha() {
		return alpha;
	}

}