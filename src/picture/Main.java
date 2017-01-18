package picture;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {

		ArrayList<String> input = new ArrayList<>();

		parseArgs(args, input);

		Command inputCommand = new Command(input);

		menu(inputCommand);

	}

	private static void saveObject(Picture picture, String locationString) {

		boolean successful = Utils.savePicture(picture, locationString);

		if (!successful) {
			System.err.println("The picture could not be saved at the following locaiton "
							+ locationString);
		}

	}

	private static void menu(Command inputCommand) {

		Picture partialPicture;
		Picture processedPicture;

		switch (inputCommand.getOption()) {
			case FLIPH:
			case ROTATE270:
				partialPicture = Process.process(inputCommand);
				inputCommand = new Command(Option.ROTATE180, partialPicture, inputCommand.getOutputPath());
			case ROTATE180:
				partialPicture = Process.process(inputCommand);
				inputCommand = new Command(Option.ROTATE90, partialPicture, inputCommand.getOutputPath());
			default: processedPicture = Process.process(inputCommand);
		}

		saveObject(processedPicture, inputCommand.getOutputPath());

	}


	private static void parseArgs(String[] args, ArrayList input) {

		for (String s : args) {
			input.add(s);
		}

	}
}
