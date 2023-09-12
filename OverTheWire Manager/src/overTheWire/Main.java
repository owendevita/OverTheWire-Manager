package overTheWire;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.nio.file.*;


public class Main {

	private static final String NEW_LINE = System.lineSeparator();
	
	private static final Scanner scnr = new Scanner(System.in);
	
	private static long currentLevel = 0;
	
	private static LinkedList<String> passwordList = new LinkedList<>();
	
	public static void initialize() throws IOException {
		
		Path file = Paths.get("Passwords.txt");
		long count = Files.lines(file).count();
		currentLevel = count - 1;
		
		FileReader fileReader = new FileReader("Passwords.txt");
		BufferedReader reader = new BufferedReader(fileReader);
		
		String line = reader.readLine();
		
		while(line != null) {
			passwordList.add(line);
			line = reader.readLine();
		}
		
		reader.close();
		
	}
	
	public static void openLevel(String password, long level) throws IOException, InterruptedException {
		
		Runtime rt = Runtime.getRuntime();
		
		Thread.sleep(250);
		
		String sshUsername = "ssh bandit" + level + "@bandit.labs.overthewire.org -p 2220";
		
		Process cmd = rt.exec("cmd.exe /c start " + sshUsername);
		
		StringSelection selection = new StringSelection(password);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
		
		System.out.println("Password copied to clipboard.\n");
		
	}
	
	
	public static void newPass() throws IOException, InterruptedException {
		
		FileWriter passWriter = new FileWriter("Passwords.txt", true);
		
		System.out.println("Enter new password:");
		String password = scnr.nextLine();
		
		openLevel(password, currentLevel + 1);
	
		System.out.println("Was password correct? y/n");
		
		String qAnswer = scnr.nextLine();
		
		if (qAnswer.equals("y")) {
			
			passWriter.write(NEW_LINE);
			
			passWriter.write(password);
			
			passwordList.add(password);
			
			currentLevel++;
		}
		
	
		
		passWriter.close();
	
		
	}
	
	public static void traverse() throws IOException, InterruptedException {
		
		System.out.println("What level would you like to go to?");
		
		long newLevel = scnr.nextLong();
		
		openLevel(passwordList.get((int)(newLevel)), newLevel);
		
	}
	
	public static void continueLevel() throws IOException, InterruptedException {
		
		openLevel(passwordList.getLast(), currentLevel);
		
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		initialize();
		
		while(true) {
			
			System.out.println("Choose an option:\n");
			System.out.println("> newpass -- enter a new found password to save it and open the next level");
			System.out.println("> traverse -- go back to a previous level");
			System.out.println("> continue -- continue from your last saved level");
			System.out.println("> exit -- exit the program\n");
			
			String answer = scnr.nextLine();
			
			switch (answer) {				
				case "newpass":
					newPass();
					break;
				case "traverse":
					traverse();
					break;
				case "continue":
					continueLevel();
					break;
				case "exit":
					Runtime rt = Runtime.getRuntime();
					rt.exec("taskkill /F /IM OpenConsole.exe");
					System.exit(0);
					break;
			}
			
			System.out.println("");
			
		}
		
	
	}
	
}
