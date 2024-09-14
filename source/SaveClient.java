package appClient;

import java.io.Console;

public class SaveClient {

	static String helpGuide = "\n\n------------------\nJavaSavingClient user guide:\n\n" +
			"SaveClient Arguments:\n" +
			"    'save' or 's' - saves your progress\n" +
			"    'restore' or 'r' - restores your progress\n\n" +
			"If no SaveClient arguments are provided you will be prompted to use one\nof these options:\n" +
			"    'h' - displays this user guide and exits the program\n" +
			"    's' - saves your progress\n" +
			"    'r' - restores your progress\n" +
			"    'q' - to quit the client without making any changes to your saves\n\n\n" +
			"Save Arguments:\n" +
			"    'name=[yourSaveName]' or 'n=[yourSaveName]' - directly provides the\n    save name at runtime\n" +
			"    'owner=[yourOwnerName]' or 'o=[yourOwnerName]' - directly provides\n    the owner name at runtime\n\n" +
			"If one or none of the Save arguments are provided you will be prompted\n    to input the save name, save owner or both during program execution.\n\n\n" +
			"Restore Arguments:\n" +
			"    'number=[restoreSaveNumber]' or 'n=[restoreSaveNumber]' - directly\n    provides the number of the save you want to restore at runtime\n\n" +
			"If Restore arguments are not provided, you will be prompted to input\nthe number of the save you want to restore.\n\n\n" +
			"Save and Restore arguments are provided as arguments for the SaveClient.\nThey can also be provided if no SaveClient arguments are given.\n\n" +
			"Any of the arguments above can be given separately or at the same time\nto the SaveClient at runtime.\n\n" +
			"Where arguments are needed, if not provided, you will be promted to\nprovide them during program execution.\n\n" +
			"If Save and Restore arguments are provided at the same time, only the\n" +
			"ones dedicate to the client action that you selected will be considerated.\n\n" +
			"If both 'save' and 'restore' SaveClient arguments are provided, the\nconsiderated one will be the first one that was given.\n\n\n" +
			"If you get prompted to provide a \"SavePaths.txt\" file, you can copy\n" +
			"the file template from this project's github page and place it in the\nsame folder as the \"SaveClient.jar\" file, or follow these steps:\n" +
			"    1. Create a \"SavePaths.txt\" file in the same folder as the\n       \"SaveClient.jar\" file.\n" +
			"    2. On the first line, place the complete path to the directory you\n       want to save (the directory in which the game save files are stored).\n" +
			"    3. On the second line, plae the complete path to the folder in\n       which your saves will be stored.\n" +
			"    4. On the third and last line, place either a 0 if you have no\n" +
			"       other saves, or the number of your most recent save (Go in your\n" +
			"       saves folder and see which is your most recent save, and its\n       number will be at the very end of its name).\n\n\n" +
			"This is the JavaSavingClient user guide!\n" +
			"Hopefully it helped you, the user, to get the best out of this\nprogram.\n\nEnjoy!\n------------------\n\n";
	
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("save") || args[i].equals("s")) {
				Save.run(args);
				return;
			}
			if (args[i].equals("restore") || args[i].equals("r")) {
				Restore.run(args);
				return;
			}
			if (args[i].equals("help") || args[i].equals("h")) {
				System.out.println(helpGuide);
				return;
			}
		}
		Console console = System.console();
		String task = console.readLine("Enter what do you want to do: \n'h' to diplay the JavaSavingClient user guide\n's' to save\n'r' to restore\n'q' to quit\nInput: ");
		System.out.println();
		if (task.equals("h")) {
			System.out.println(helpGuide);
			return;
		}
		if (task.equals("s")) {
			Save.run(args);
			return;
		}
		if (task.equals("r")) {
			Restore.run(args);
			return;
		}
		if (task.equals("q")) {
			return;
		} else {
			throw new Exception("Invalid input!\nOnly 'h', 's', 'r' or 'q' permited");
		}
	}
}
