package cs320.windows98.tbag.input;

import java.util.ArrayList;

public class Command {
	final static String invalidCommand = "I do not understand that command";
	private String input;
	
	private String verb;
	private String noun;
	
	public Command(String input) {
		this.input = input;
		parseCommands();
	}
	
	private void parseCommands() {
		String[] individualWords = input.split(" ");
		
		ArrayList<String> breakdown = new ArrayList<String>();
		
		// REMOVE ARTICLES
		
		for (String word : individualWords) {
			if (!word.equalsIgnoreCase("the") && !word.equalsIgnoreCase("a") && !word.equalsIgnoreCase("an")) {
				breakdown.add(word);
			}
		}
		
		verb = breakdown.get(0);
		noun = breakdown.get(breakdown.size() - 1);
	}
		
	public String execute() {
		String output = null;
		
		if (verb.equals("look")) {
			
		} else if (verb.equals("open")) {
			if (noun.equals("inventory")) {
				
			}
		} else if (verb.equals("grab")) {
			// check if item is in room with second arg name
		} else if (verb.equals("drop")) {
			// check if item is in inventory
		} else if (verb.equals("move")) {
			// get room connections and check if 2nd arg is in there
		}
		
		return output != null ? output : invalidCommand;
	}
}
