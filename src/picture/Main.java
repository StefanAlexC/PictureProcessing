package picture;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class Main {

  private final String desktopPath = "/Users/Alex/Desktop/test.jpg";
  private final String imagesPath = "/Users/Alex/Desktop/Java/javapictureprocessing_sc3316/images";

  public static void main(String[] args) {

    //error no input

    menu(OptionUtils.stringToOption(args[0], args[1]), args);

    //Utils.savePicture(fetchObject(args[1]), "/Users/Alex/Desktop/test.jpg");

  }

  //might not be useful
  private static Picture fetchObject(Option option, String[] args) {

    String locationString = null;

    switch (option) {
      case INVERT:
      case GRAYSCALE:
      case BLUR: locationString = args[1]; break;
      case ROTATE90:
      case ROTATE180:
      case ROTATE270:
      case FLIPV:
      case FLIPH: locationString = args[2]; break;
      case BLEND:

    }

    Picture tempPicture = Utils.loadPicture(locationString);

    if (tempPicture == null){
      System.err.println("The picture at the following location " + locationString
              + " could not be fetched"); //change to resest maybe
	  }

    return tempPicture;

  }

  private static Picture[] fetchObjects(String[] args) {

    Picture[] tempPictures = new Picture[args.length - 2];

    for (int i = 1 ; i < args.length - 1 ; i++) {
      tempPictures[i - 1] = Utils.loadPicture(args[i]);
      if (tempPictures [i - 1] == null){
        System.err.println("The picture at the following location " + args[i]
                + " could not be fetched"); //change to resest maybe
      }
    }

    return tempPictures;
  }

  private static void saveObject(Picture picture, String locationString) {

    boolean successful = Utils.savePicture(picture, locationString);

    if(!successful){
      System.err.println("The picture could not be saved at the following locaiton "
              + locationString);
    }

  }

  private static void menu(Option option, String[] args) {

    Picture inputPicture = null;
    Picture[] inputPictures;
    Picture processedPicture = null;

    if (!checkValidInput(option, args))
      return; //reset program maybe?

    if(option != Option.BLEND)
      inputPicture = fetchObject(option, args);



    switch (option) {
      case BLUR:
      case INVERT:
      case GRAYSCALE: processedPicture = Process.process(option, inputPicture); break;
      case ROTATE270: inputPicture = Process.process(Option.ROTATE90, inputPicture);
      case ROTATE180: inputPicture = Process.process(Option.ROTATE90, inputPicture);
      case ROTATE90:  processedPicture = Process.process(Option.ROTATE90, inputPicture); break;
      case FLIPH: {
        inputPicture = Process.process(Option.ROTATE90, inputPicture);
        inputPicture = Process.process(Option.ROTATE90, inputPicture);
      }
      case FLIPV: processedPicture = Process.process(Option.FLIPV, inputPicture); break;
      case BLEND: {
        inputPictures = fetchObjects(args);
        processedPicture = Process.blend(inputPictures);
        break;
      }
    }

    saveObject(processedPicture, args[args.length - 1]);

  }

  private static Boolean checkValidInputLength(Option option, String[] args) {

    switch (option) {
      case INVERT:
      case GRAYSCALE:
      case BLUR:      return (args.length == 3);
      case ROTATE90:
      case ROTATE180:
      case ROTATE270:
      case FLIPV:
      case FLIPH:      return (args.length == 4);
      case BLEND:     return (args.length > 3);

    }

    System.err.println("checkValidInputLength has failed");
    return null;
  }

  private static Boolean checkValidInput(Option option, String[] args) {

    return checkValidInputLength(option, args); //add other checkers

  }
}
