package src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;


public class DAOkeys {
	private String documentsPath = "";
	private File file1;
	private File file2;
	
	Map<Integer, File> files = new HashMap<>();
	
	public DAOkeys() {
		
		documentsPath = new JFileChooser().getFileSystemView().getDefaultDirectory().getPath();
		createDirectory();
		file1 = new File(documentsPath + "/Password Creator Keys/Password Key 1.txt");
		file2 = new File(documentsPath + "/Password Creator Keys/Password Key 2.txt");
		files.put(1, file1);
		files.put(2, file2);
		
	}
	
	public boolean hasKeys() {
		if(file1.exists() && file2.exists()) {
			return true;
		}
		return false;
	}
	
	public void saveKey(ArrayList<String> key, int keyNumber) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(files.get(keyNumber));
			for(int i = 0; i < key.size(); i++) {
				writer.write(key.get(i) + "\n");
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
		if(writer != null) {
			try {
				writer.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public ArrayList<String> loadKey(int keyNumber) {
		Scanner reader = null;
		ArrayList<String> key = new ArrayList<>();
		try {
			 reader = new Scanner(files.get(keyNumber));
			 while(reader.hasNextLine()) {
				 key.add(reader.nextLine());
			 }
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		if(reader != null) {
			try {
				reader.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return key;
	}
	
	private void createDirectory() {
		try {
			Files.createDirectories(Paths.get(documentsPath + "/Password Creator Keys/"));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean saveTo(String filePath) {
		boolean saveOk = true;
		FileWriter writer = null;
		ArrayList<String> key1 = loadKey(1);
		ArrayList<String> key2 = loadKey(2);
		String keyPath1 = "/Password Key 1.txt";
		String keyPath2 = "/Password Key 2.txt";
		File file1 = new File(filePath + keyPath1);
		File file2 = new File(filePath + keyPath2);
		try {
			file1.createNewFile();
			file2.createNewFile();
		}catch(IOException e) {
			e.printStackTrace();
			saveOk = false;
			
		}
		
		try {
			writer = new FileWriter(file1);
			for(int i = 0; i < key1.size(); i++) {
				writer.write(key1.get(i) + "\n");
			}
		}catch(IOException e) {
			e.printStackTrace();
			saveOk = false;
		}
		
		
		if(writer != null) {
			try {
				writer.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		try {
			writer = new FileWriter(file2);
			for(int i = 0; i < key2.size(); i++) {
				writer.write(key2.get(i) + "\n");
			}
		}catch(IOException e) {
			e.printStackTrace();
			saveOk = false;
		}
		
		if(writer != null) {
			try {
				writer.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return saveOk;
		
	}
	
	public boolean importKeys(File[] files) {
		boolean importOk = true;
		
		
		return importOk;
	}
	
}
