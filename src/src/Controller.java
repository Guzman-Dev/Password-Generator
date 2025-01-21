package src;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Controller {
	private String baseChars = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ1234567890";
	private String fullChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!#$%&/()=?¡*[]_:;`´~-.";
	
	private HashMap<String, String> firstKey = new HashMap<>();
	private HashMap<String, String> secondKey = new HashMap<>();
	private Random rand = new Random();
	
	private HashMap<Integer, HashMap<String, String>> keys = new HashMap<>();
	
	private DAOkeys keysDAO = new DAOkeys();
	
	public Controller() {
		keys.put(1, firstKey);
		keys.put(2, secondKey);
		
		if(!keysDAO.hasKeys()) {
			newKey(1);
			newKey(2);
		}else {
			loadKey(1);
			loadKey(2);
		}
		
		saveKey(1);
		saveKey(2);
		
		
	}
	
	public void newKey(Integer keyNumber) {
		List<String> baseCharList = new ArrayList<>(Arrays.asList(baseChars.split("")));
		List<String> fullCharList = new ArrayList<>(Arrays.asList(fullChars.split("")));
		String currentChar = null;
		String keyChar = null;
		int keyIdx = 0;
		for(int i = 0; i < baseCharList.size(); i++) {
			keyIdx = rand.nextInt(fullCharList.size());
			currentChar = baseCharList.get(i);
			keyChar = fullCharList.get(keyIdx);
			keys.get(keyNumber).put(currentChar, keyChar);
			fullCharList.remove(keyIdx);
		}
	}
	
	private void loadKey(int keyNumber) {
		ArrayList<String> key = keysDAO.loadKey(keyNumber);
		HashMap<String, String> keyToLoad = keys.get(keyNumber);
		for(int i = 0; i < key.size(); i++) {
			String[] keyValue = key.get(i).split(",");
			keyToLoad.put(keyValue[0], keyValue[1]);
		}
	}
	
	public void saveKey(int keyNumber) {
		HashMap<String, String> key = keys.get(keyNumber);
		ArrayList<String> keysSet = new ArrayList<>(key.keySet());
		ArrayList<String> values = new ArrayList<>();
		ArrayList<String> keyToSave = new ArrayList<>();
		for(int i = 0; i < key.size(); i++) {
			values.add(key.get(keysSet.get(i)));
		}
		for(int i = 0; i < key.size(); i++) {
			keyToSave.add(keysSet.get(i) + "," + values.get(i));
		}
		
		keysDAO.saveKey(keyToSave, keyNumber);
		
		
	}
	
	public String codePassword(String rawPassword) {
		if(rawPassword.isBlank()) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		String passwordToSplit = rawPassword.trim().replaceAll("\\W", "");
		String[] passwordChars = passwordToSplit.split("");
		String newChar = "";
		int keyNumber = 1;
		for(int i = 0; i < passwordChars.length; i++) {
			newChar = keys.get(keyNumber).get(passwordChars[i]);
			buffer.append(newChar);
			
			if(keyNumber== 1 ) {
				keyNumber = 2;
			}else if(keyNumber == 2){
				keyNumber = 1;
			}
		}
		
		buffer.append(checkConditions(buffer.toString()));
		
		return buffer.toString();
	}
	
	private String checkConditions(String passwordToCheck) {
		StringBuffer buffer = new StringBuffer();
		boolean hasUpperCase = false;
		boolean hasLowerCase = false;
		boolean hasDigit = false;
		for(int i = 0; i < passwordToCheck.length(); i++) {
			char ch = passwordToCheck.charAt(i);
			if(Character.isDigit(ch)) {
				hasDigit = true;
			}else if(Character.isUpperCase(ch)) {
				hasUpperCase = true;
			}else if(Character.isLowerCase(ch)) {
				hasLowerCase = true;
			}
		}
		
		if(!hasDigit) {
			buffer.append("6");
		}
		if(!hasUpperCase) {
			buffer.append("X");
		}
		if(!hasLowerCase) {
			buffer.append("z");
		}
		
		return buffer.toString();
	}
	
	public void resetKeys() {
		newKey(1);
		newKey(2);
		saveKey(1);
		saveKey(2);
	}
	
	public boolean saveKeysTo(File fileWhereToSave) {
		String path = fileWhereToSave.getPath();
		boolean saveOk = keysDAO.saveTo(path);
		return saveOk;
	}
	
	public boolean checkKeyNames(File[] files) {
		String fileName1 = files[0].getName();
		String fileName2 = files[1].getName();
		if(!Set.of("Password Key 1.txt", "Password Key 2.txt").contains(fileName1)) {
			return false;
		}
		if(!Set.of("Password Key 1.txt", "Password Key 2.txt").contains(fileName2)) {
			return false;
		}
		return true;
	}
	
	public boolean importKeys(File[] files) throws Exception{
		Scanner reader = null;
		File[] orderedFiles = orderFiles(files);
		ArrayList<String> key1 = new ArrayList<>();
		ArrayList<String> key2 = new ArrayList<>();
		try {
			reader = new Scanner(orderedFiles[0]);
			while(reader.hasNextLine()) {
				key1.add(reader.nextLine());
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		try {
			reader = new Scanner(orderedFiles[1]);
			while(reader.hasNextLine()) {
				key2.add(reader.nextLine());
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
		
		keysDAO.saveKey(key1, 1);
		keysDAO.saveKey(key2, 2);
		loadKey(1);
		loadKey(2);
		return true;
	}
	
	private File[] orderFiles(File[] files) {
		File[] orderedFiles = new File[2];
		if(files[0].getName().equals("Password Key 1.txt")) {
			orderedFiles[0] = files[0];
			orderedFiles[1] = files[1];
			return orderedFiles;
		}else {
			orderedFiles[0] = files[1];
			orderedFiles[1] = files[0];
			return orderedFiles;
		}
	}
	
}
