package picture;

import utils.Tuple;

public class Process {

	public static Picture process(Command inputCommand) {

		Picture[] inputPictures = inputCommand.getInputPictures();

		Picture outputPicture = createPicture(inputCommand, inputPictures);

		int height = intialiseHeight(inputCommand, outputPicture);
		int width = intialiseWidth(inputCommand, outputPicture);

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
						outputPicture.setPixel(height - j, i, inputPictures[0].getPixel(i, j));
						break;
					case FLIPH:
						outputPicture.setPixel(width - i, j, inputPictures[0].getPixel(i, j));
						break;
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
					case MOSAIC:
						outputPicture.setPixel(i, j, inputPictures[
										mosaicImage(i, j, inputCommand.getTileSize(), inputPictures.length)
										].getPixel(i,j));
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

	private static int mosaicImage(int x, int y, int tileSize, int pictureNumber) {

		return (y / tileSize + x / tileSize) % pictureNumber;

	}

	private static Picture createPicture(Command inputCommand, Picture[] inputPictures) {

		Tuple<Integer, Integer> size = pictureSize(inputCommand, inputPictures);

		return Utils.createPicture(size.getX(), size.getY());
	}

	private static boolean marginPixel(int width, int height, int i, int j) {
		return (i == 0) || (i == width) || (j == 0) || (j == height);
	}

	private static Tuple pictureSize(Command inputCommand, Picture[] inputPictures) {

		final int MAX_INT = 2000000000;
		int minWidth = MAX_INT, minHeight = MAX_INT;

		for (Picture p : inputPictures) {

			if (p.getHeight() < minHeight)
				minHeight = p.getHeight();

			if (p.getWidth() < minWidth)
				minWidth = p.getWidth();

		}

		switch (inputCommand.getOption()) {
			case MOSAIC:   return adjustedSize(minWidth, minHeight, inputCommand.getTileSize());
			case ROTATE270:
			case ROTATE180:
			case ROTATE90: return new Tuple(minHeight, minWidth);
			default:       return new Tuple(minWidth, minHeight);
		}
	}

	private static Tuple adjustedSize(int Width, int Height, int size) {

		return new Tuple((Width / size) * size, (Height / size) * size);

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

	private static int intialiseWidth(Command inputCommand, Picture outputPicture) {

		switch (inputCommand.getOption()) {
			case ROTATE90:
			case ROTATE180:
			case ROTATE270:
				return inputCommand.getInputPictures()[0].getWidth() - 1;
			default:
				return outputPicture.getWidth() - 1;
		}

	}

	private static int intialiseHeight(Command inputCommand, Picture outputPicture) {

		switch (inputCommand.getOption()) {
			case ROTATE90:
			case ROTATE180:
			case ROTATE270:
				return inputCommand.getInputPictures()[0].getHeight() - 1;
			default:
				return outputPicture.getHeight() - 1;
		}

	}
}
