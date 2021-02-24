package cs320.windows98.tbag.input;

import java.util.ArrayList;

public class Input {
	private String userInput;
	private ArrayList<String> words;
	
	public Input(String userInput) {
		this.userInput = userInput;
		this.words = new ArrayList<String>();
		String[] individualWords = this.userInput.split(" ");
		
		for (String word : individualWords) {
			words.add(word);
		}
	}
	
	public ArrayList<String> getWords() {
		return words;
	}
	
	public void removeArticles() {
		for (String word : words) {
			if (word.equals("the") || word.equals("a") || word.equals("an")) {
				words.remove(word);
			}
		}
	}	
	
	public String getAction() {
		return words.get(0);
	}
	
	public boolean containsNoun() {
		return userInput.contains("the") || userInput.contains("an") || userInput.contains("a");
	}
	
	public String getNoun() {
		if (userInput.contains("the")) {
			int index = words.indexOf("the");
			return words.get(index + 1);
		} else if (userInput.contains("an")) {
			int index = words.indexOf("an");
			return words.get(index + 1);
		} else if (userInput.contains("a")) {
			int index = words.indexOf("a");
			return words.get(index + 1);
		} else {
			return null;
		}
	}
}
