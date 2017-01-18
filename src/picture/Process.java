package picture;

public class Process {

	public static Picture process(Command inputCommand) {

		Picture[] inputPictures = inputCommand.getInputPictures();

		Picture outputPicture = createPicture(inputCommand.getOption(), inputPictures);

		int height = outputPicture.getHeight() - 1;
		int width = outputPicture.getWidth() - 1;

		for (int i = 0; i <= width; i++)
			for (int j = 0; j <= height; j++) {

				switch (inputCommand.getOption()) {
					case INVERT:
						outputPicture.setPixel(i, j, invertPixel(inputPictures[0].getPixel(i, j)));
						break;
					case GRAYSCALE:
						outputPicture.setPixel(i, j, grayscalePixel(inputPictures[0].getPixel(i, j)));
						break;
					case ROTATE270:
					case ROTATE180:
					case ROTATE90:
						pixelInBounds(outputPicture, j, i);
						outputPicture.setPixel(height - j, i, inputPictures[0].getPixel(i, j));
						break;
					case FLIPH:
					case FLIPV:
						outputPicture.setPixel(i, height - j, inputPictures[0].getPixel(i, j));
						break;
					case BLUR: {
						if (marginPixel(width, height, i, j))
							outputPicture.setPixel(i, j, inputPictures[0].getPixel(i, j));
						else
							outputPicture.setPixel(i, j, blurPixel(i, j, inputPictures[0]));
						break;
					}
					case BLEND:
						outputPicture.setPixel(i, j, addPixels(inputPictures, i, j));
						break;
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

	private static Color blurPixel(int i, int j, Picture picture) {

		int dx[] = {-1, 0, 1, 1, 1, 0, -1, -1, 0};
		int dy[] = {-1, -1, -1, 0, 1, 1, 1, 0, 0};

		int red = 0;
		int blue = 0;
		int green = 0;
		int x, y;

		for (int k = 0; k <= 8; k++) {
			x = i + dx[k];
			y = j + dy[k];
			red += picture.getPixel(x, y).getRed();
			blue += picture.getPixel(x, y).getBlue();
			green += picture.getPixel(x, y).getGreen();
		}

		return new Color(red / 9, green / 9, blue / 9);

	}

	private static Picture createPicture(Option option, Picture[] inputPictures) {

		switch (option) {
			case BLUR:
			case FLIPV:
			case INVERT:
			case GRAYSCALE:
				return Utils.createPicture(inputPictures[0].getWidth(), inputPictures[0].getHeight());
			case FLIPH:
			case ROTATE270:
			case ROTATE180:
			case ROTATE90:
				return Utils.createPicture(inputPictures[0].getHeight(), inputPictures[0].getWidth());
			case BLEND:
				return blendedPicture(inputPictures);

		}

		return null;
	}

	private static void pixelInBounds(Picture picture, int x, int y) {
		if (!picture.contains(x, y))
			System.err.println("Coordinates are not contained by the picture");
	} //function used for testing

	private static boolean marginPixel(int width, int height, int i, int j) {
		return (i == 0) || (i == width) || (j == 0) || (j == height);
	}

	private static Picture blendedPicture(Picture[] inputPictures) {

		final int MAX_INT = 2000000000;
		int minWidth = MAX_INT, minHeight = MAX_INT;

		for (Picture p : inputPictures) {

			if (p.getHeight() < minHeight)
				minHeight = p.getHeight();

			if (p.getWidth() < minWidth)
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
