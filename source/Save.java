package appClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;

public class Save {

	static Console console;
	static BufferedReader fileReader;
    static BufferedWriter fileWriter;
	static String source, target, originalTarget, date, time, saveLogContent, owner, saveName, folderPath;
	static Path sourceDir, targetDir;
	static int saveNr;
	static boolean nameProvided, ownerProvided;
	
	public static void run(String[] args) throws Exception {
		date = LocalDate.now() + "";
		time = LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond();
		folderPath = getFolder(new File(Save.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
		
		nameProvided = ownerProvided = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("name=") || args[i].startsWith("n=")) {
				saveName = "";
				for (int j = args[i].indexOf('=') + 1; j < args[i].length(); j++) {
					saveName += args[i].charAt(j) + "";
				}
				nameProvided = true;
			}
			if (args[i].startsWith("owner=") || args[i].startsWith("o=")) {
				owner = "";
				for (int j = args[i].indexOf('=') + 1; j < args[i].length(); j++) {
					owner += args[i].charAt(j) + "";
				}
				ownerProvided = true;
			}
		}
		console = System.console();
		if (!nameProvided) {
			saveName = console.readLine("Enter the name of the save: ");
		}
		if (!ownerProvided) {
			owner = console.readLine("Enter the name of the save owner: ");
		}
		if (!nameProvided || !ownerProvided) {
			System.out.println();
		}
		try {
			fileReader = new BufferedReader(new FileReader(folderPath + "SavePaths.txt"));
		} catch (Exception e) {
			System.out.println("\"SavePaths.txt\" is missing!\nPlease provide a \"SavePaths.txt\" file!\n");
			throw e;
		}
		
		if (saveName.contains("_")) {
			System.out.println("Save name cannot contain \"_\"!\nReplacing all \"_\" with \" \".\n");
			saveName.replace('_', ' ');
		}
		
		source = fileReader.readLine();
		target = originalTarget = fileReader.readLine();
		saveNr = Integer.parseInt(fileReader.readLine()) + 1;		
		fileReader.close();
		target += "/" + date + "_" + saveName + "_owner-" + owner + "_save" + saveNr;
		
		sourceDir = Paths.get(source);
		targetDir = Paths.get(target);
		copyPaths(sourceDir, targetDir);
	
		try {
			fileReader = new BufferedReader(new FileReader(folderPath + "SaveLog.txt"));
		} catch (IOException e) {
			new FileWriter(folderPath + "SaveLog.txt").close();
			fileReader = new BufferedReader(new FileReader(folderPath + "SaveLog.txt"));
			System.out.println("Creating \"SaveLog.txt\".\n");
		}
		String line = "";
		saveLogContent = "";
		while (line != null) {
			saveLogContent += (line.length() > 0) ? line + "\n" : "";
			line = fileReader.readLine();
		}
		fileReader.close();

		fileWriter = new BufferedWriter(new FileWriter(folderPath + "SavePaths.txt"));
		fileWriter.write(source + "\n");
		fileWriter.write(originalTarget + "\n");
		fileWriter.write("" + saveNr + "\n");
		fileWriter.close();
		
		fileWriter = new BufferedWriter(new FileWriter(folderPath + "SaveLog.txt"));
		fileWriter.write(saveLogContent);
		fileWriter.write("[" + date + " at " + time + "] Owner " + owner + " created save " + saveName + " (save" + saveNr + ") which was saved at location \"" + target + "\".\n");
		fileWriter.close();
		
		System.out.println("Saved from \"" + source + "\" to \"" + target + "\".");
		System.out.println("Save name is \"" + saveName + "\".\nOwner is \"" + owner + "\".\nDate is [" + date + " at " + time + "].\n");
		System.out.println("All info saved in \"SaveLog.txt\".\nPath to \"SaveLog.txt\" is \"" + folderPath + "SaveLog.txt\".\n");
	}
	
	static String getFolder(String path) {
		path = path.replace(File.separatorChar, '/');
		StringBuilder folder = new StringBuilder(path);
		folder.delete(path.lastIndexOf('/') + 1, path.length());
		return folder.toString();
	}
	
	static void deletePath(Path target) throws Exception {
        if (!Files.isDirectory(target)) {
			Files.delete(target);
        	return;
        }
        try (Stream<Path> paths = Files.walk(target)) {
	        paths.filter(Files::isRegularFile).forEach((Path src) -> {
        		try {
        			Files.delete(src);
				} catch (IOException e) {
					e.printStackTrace();
				}
	        });
        }
        try (Stream<Path> paths = Files.walk(target)) {
	        paths.filter(Files::isDirectory).forEach((Path src) -> {
        		try {
        			Files.delete(src);
				} catch (IOException e) {
					e.printStackTrace();
				}
	        });
        }
	}
	
	static void copyPaths(Path source, Path target) throws Exception {
		if (Files.exists(target)) {
			deletePath(target);
		}
        if (!Files.isDirectory(source)) {
        	Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        	return;
        }
        try (Stream<Path> paths = Files.walk(source)) {
	        paths.forEach((Path src) -> {
	        	Path relativePath = source.relativize(src);
	        	Path targetResolved = target.resolve(relativePath);
        		try {
					Files.copy(src, targetResolved, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
	        });
        }
    }
}
