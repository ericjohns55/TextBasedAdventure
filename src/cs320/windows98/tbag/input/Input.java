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
	
	public void removeArticles(ArrayList<String> words) {
		for (int i = 0; i < words.size(); i++) {
			String word = words.get(i);
			
			if (word.equals("the") || word.equals("a") || word.equals("an")) {
				words.remove(word);
			}
		}
	}	
	
	public String getAction() {
		String action = words.get(0);
		
		String[] articles = {" the ", " an ", " a "};
		
		for (String article : articles) {
			if (userInput.contains(article)) {
				return userInput.substring(0, userInput.indexOf(article));
			}
		}
		
		return action;
	}
	
	public boolean containsNoun() {
		return userInput.contains(" the ") || userInput.contains(" an ") || userInput.contains(" a ");
	}
	
	public String getSubject() {
		if (words.size() == 1) {
			return "<none>";
		}
		
		String[] articles = {" the ", " an ", " a "};
		
		for (String article : articles) {
			if (userInput.contains(article)) {
				return userInput.substring(userInput.indexOf(article) + article.length());
			}
		}
		
		return words.get(words.size() - 1);
	}
}
