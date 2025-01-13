package src;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
	
	public Boolean codePassword(String rawPassword) {
		if(rawPassword.isBlank()) {
			return false;
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
		
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(buffer.toString()), null);
		return true;
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
	
}
