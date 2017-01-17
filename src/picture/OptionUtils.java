package picture;

public class OptionUtils {

	public static Option stringToOption(String option, String setting){

		switch (option){
			case "rotate": {
				if(setting.equals("90"))
					return Option.ROTATE90;
				if(setting.equals("180"))
					return Option.ROTATE180;
				if(setting.equals("270"))
					return Option.ROTATE270;
			}
			case "invert":    return Option.INVERT;
			case "grayscale": return Option.GRAYSCALE;
			case "blend":     return Option.BLEND;
			case "blur":      return Option.BLUR;
			case "flip": {
				if(setting.equals("H"))
					return Option.FLIPH;
				if(setting.equals("V"))
					return Option.FLIPV;
			}
		}

		return null;

	}

}
