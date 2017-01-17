package picture;

import java.util.HashMap;

public class Process {

	public static Picture process(Option option, Picture inputPicture) {

		Picture outputPicture = createPicture(option, inputPicture);

		int height = inputPicture.getHeight() - 1;
		int width  = inputPicture.getWidth() - 1;

		for (int i = 0 ; i <= width ; i++)
			for (int j = 0 ; j <= height ; j++) {

				pixelInBounds(inputPicture, i, j);

					switch (option) {
						case INVERT: outputPicture.setPixel(i, j, invertPixel(inputPicture.getPixel(i, j))); break;
						case GRAYSCALE: outputPicture.setPixel(i, j, grayscalePixel(inputPicture.getPixel(i, j))); break;
						case ROTATE90: pixelInBounds(outputPicture, j, i);
						outputPicture.setPixel(height - j, i, inputPicture.getPixel(i, j)); break;
						case FLIPV: outputPicture.setPixel(i, height - j, inputPicture.getPixel(i, j)); break;
						case BLUR: {
							if(marginPixel(width, height, i ,j))
								outputPicture.setPixel(i, j, inputPicture.getPixel(i, j));
							else
								outputPicture.setPixel(i, j, blurPixel(i, j, inputPicture));
							break;
						}
					}

			}

		return outputPicture;

	}

	private static Color invertPixel(Color currentPixel) {

		final int colorMaxValue = 255;

		currentPixel.setRed(colorMaxValue - currentPixel.getRed());
		currentPixel.setBlue(colorMaxValue - currentPixel.getBlue());
		currentPixel.setGreen(colorMaxValue - currentPixel.getGreen());

		return currentPixel;

	}

	private static Color grayscalePixel(Color currentPixel) {

		int average;

		average = (currentPixel.getRed() +
		           currentPixel.getBlue() +
		           currentPixel.getGreen()) / 3;

		return new Color(average, average, average);

	}

	private static Color blurPixel(int i, int j, Picture picture){

		int dx[] = {-1, 0, 1, 1, 1, 0, -1, -1, 0};
		int dy[] = {-1, -1, -1, 0, 1, 1, 1, 0, 0};

		int red = 0;
		int blue = 0;
		int green = 0;
		int x, y;

		for(int k = 0 ; k <= 8 ; k++){
			x = i + dx[k];
			y = j + dy[k];
			red   += picture.getPixel(x, y).getRed();
			blue  += picture.getPixel(x, y).getBlue();
			green += picture.getPixel(x, y).getGreen();
		}

		return new Color(red / 9, green / 9, blue / 9);

	}

	public static Picture blend(Picture[] inputPictures) {

		Picture blendedPicture = blendedPicture(inputPictures);

		int width = blendedPicture.getWidth();
		int height = blendedPicture.getHeight();

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				blendedPicture.setPixel(i, j, addPixels(inputPictures, i, j));
			}

		return blendedPicture;
	}

	private static Picture createPicture(Option option, Picture inputPicture) {

		switch (option) {
			case BLUR:
			case FLIPV:
			case INVERT:
			case GRAYSCALE: return Utils.createPicture(inputPicture.getWidth(), inputPicture.getHeight());
			case ROTATE90: return Utils.createPicture(inputPicture.getHeight(), inputPicture.getWidth());
		}

		return null;
	}

	private static void pixelInBounds(Picture picture, int x, int y) {
		if(!picture.contains(x, y))
			System.err.println("Coordinates are not contained by the picture");
	}

	private static boolean marginPixel(int width, int height, int i, int j) {
		return (i == 0) || (i == width) || (j == 0) || (j == height);
	}

	private static Picture blendedPicture(Picture[] inputPictures) {

		final int MAX_INT = 2000000000;
		int minWidth = MAX_INT, minHeight = MAX_INT;

		for(Picture p : inputPictures) {

			if(p.getHeight() < minHeight)
				minHeight = p.getHeight();

			if(p.getWidth() < minWidth)
				minWidth = p.getWidth();

		}
		return Utils.createPicture(minWidth, minHeight);
	}

	private static Color addPixels(Picture[] inputPictures, int i, int j) {

		int red = 0, blue = 0, green = 0;

		for (Picture p : inputPictures) {
			red += p.getPixel(i, j).getRed();
			blue += p.getPixel(i, j).getBlue();
			green += p.getPixel(i, j).getGreen();
		}

		red /= inputPictures.length;
		blue /= inputPictures.length;
		green /= inputPictures.length;

		return new Color(red, green, blue);
	}
}
