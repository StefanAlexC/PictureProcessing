package picture;

import java.util.ArrayList;

public class Command {

	private Option option;
	private Picture[] inputPictures;
	private String outputPath;

	public Command(ArrayList<String> input){

		option = stringToOption(input);
		inputPictures = fetchObjects(input);
		outputPath = input.get(0);

	}

	public Command(Option option, Picture picture, String outputPath) {

		this.option = option;
		this.inputPictures = new Picture[] {picture};
		this.outputPath = outputPath;

	}

	public Option getOption() {
		return option;
	}

	public Picture[] getInputPictures() {
		return inputPictures;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOption(Option option) {

		this.option = option;
	}

	private static Option stringToOption(ArrayList<String> input) {

		String option  = input.get(0);
		input.remove(0);
		String setting = input.get(0);

		switch (option) {
			case "rotate": {
				input.remove(0);
				if (setting.equals("90"))
					return Option.ROTATE90;
				else if (setting.equals("180"))
					return Option.ROTATE180;
				else if (setting.equals("270"))
					return Option.ROTATE270;
				else
					System.err.println("Invalid Setting!");
			}
			case "invert":
				return Option.INVERT;
			case "grayscale":
				return Option.GRAYSCALE;
			case "blend":
				return Option.BLEND;
			case "blur":
				return Option.BLUR;
			case "flip": {
				input.remove(0);
				if (setting.equals("H"))
					return Option.FLIPH;
				else if (setting.equals("V"))
					return Option.FLIPV;
				else
					System.err.println("Invalid Setting!");
			}
		}
		System.err.println("Invalid Command!");
		return null;

	}

	private static Picture[] fetchObjects(ArrayList<String> input) {

		Picture[] tempPictures = new Picture[input.size() - 1];
		int length = input.size();

		for (int i = 1 ; i < length ; i++) {
			tempPictures[i - 1] = Utils.loadPicture(input.get(0));
			input.remove(0);
			if (tempPictures[i - 1] == null) {
				System.err.println("The picture at the following location " +
								"could not be fetched"); //change to resest maybe
			}
		}

		return tempPictures;
	}
}
