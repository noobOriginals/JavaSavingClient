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
import java.util.List;
import java.util.stream.Stream;

public class Restore {
	
	static Console console;
	static BufferedReader fileReader;
	static BufferedWriter fileWriter;
	static Path sourceDir, targetDir;
	static String source, target, save, saveLogContent, date, time;
	static int saveNr;
	static boolean nrProvided;
	
	public static void run(String[] args) throws Exception {
		date = LocalDate.now() + "";
		time = LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond();
		
		nrProvided = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("number=")  || args[i].startsWith("n=")) {
				save = "";
				for (int j = args[i].indexOf('=') + 1; j < args[i].length(); j++) {
					save += args[i].charAt(j) + "";
				}
				nrProvided = true;
			}
		}
		
		console = System.console();
		System.out.println("Save numbers can be found in relation with owners and names in \"SaveLog.txt\".");
		if (!nrProvided) {
			save = console.readLine("Enter the number of the save you want to restore: ");
			System.out.println();
		}
		saveNr = Integer.parseInt(save);
		
		try {
			fileReader = new BufferedReader(new FileReader("SavePaths.txt"));
		} catch (Exception e) {
			System.out.println("\"SavePaths.txt\" is missing!\nPlease provide a \"SavePaths.txt\" file!\n");
			throw e;
		}
		source = fileReader.readLine();
		target = getSavePath(fileReader.readLine(), saveNr);
		fileReader.close();

		sourceDir = Paths.get(source);
		targetDir = Paths.get(target);

		copyPaths(targetDir, sourceDir);
		
		try {
			fileReader = new BufferedReader(new FileReader("SaveLog.txt"));
		} catch (IOException e) {
			new FileWriter("SaveLog.txt").close();
			fileReader = new BufferedReader(new FileReader("SaveLog.txt"));
			System.out.println("Creating \"SaveLog.txt\".\n");
		}
		String line = "";
		saveLogContent = "";
		while (line != null) {
			saveLogContent += (line.length() > 0) ? line + "\n" : "";
			line = fileReader.readLine();
		}
		fileReader.close();
		
		fileWriter = new BufferedWriter(new FileWriter("SaveLog.txt"));
		fileWriter.write(saveLogContent);
		fileWriter.write("[" + date + " at " + time + "] Save " + getSaveName(target) + " (" + "save" + saveNr + ") was restored from \"" + target + "\".\n");
		fileWriter.close();
		
		System.out.println("Restored save \"" + getSaveName(target) + "\".\n");
		System.out.println("More info about the restored save can be found in \"SaveLog.txt\".\n");
	}
	
	static String getSavePath(String folder, int saveNr) throws Exception {
		String savePath = null;
		Path source = Paths.get(folder);
		try (Stream<Path> paths = Files.walk(source)) {
			List<Path> pathsList = paths.toList();
			for (Path src : pathsList) {
				if (Files.isDirectory(src) && src.toString().endsWith("_save" + saveNr)) {
					savePath = src.toString().replace(File.separatorChar, '/');
					break;
				}
			}
		}
		return savePath;
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
	
	static String getSaveName(String save) {
		String name = "";
		int nameEndIdx = save.indexOf("_owner-");
		int nameIdx = nameEndIdx - 1;
		while (save.charAt(nameIdx) != '_' && save.charAt(nameIdx) != '/') {
			nameIdx--;
		}
		nameIdx++;
		for (int i = nameIdx; i < nameEndIdx; i++) {
			name += save.charAt(i) + "";
		}
		return name;
	}
}
