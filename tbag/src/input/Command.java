package input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import game.Game;
import commands.*;

public class Command {
	private static Set<String> SINGLE_WORD_COMMANDS = new HashSet<>(Arrays.asList("look", "examine", "hint", "y", "n"));
	private static Set<String> PREPOSITIONS = new HashSet<>(Arrays.asList("on", "from", "to", "in"));
	private static Set<String> ARTICLES = new HashSet<>(Arrays.asList("the", "a", "an"));
	
	private static Set<String> VALID_COMMANDS = new HashSet<>(Arrays.asList("examine", "look", "open", "list", "grab", "take", "place",
			"drop", "move", "walk", "unlock", "type", "solve", "read", "push", "play", "cut", "pour", "hint", "talk", "y", "n"));
	
	public final static String invalidCommand = "I do not understand that command";
	private String input;
	private Game game;
	
	private String verb;
	private String noun;
	private String location;
	
	public Command(String input, Game game) {
		this.input = replaceSynonyms(input);
		this.game = game;
		parseCommands();
	}
	
	private void parseCommands() {
		String[] individualWords = input.split(" ");
		
		ArrayList<String> breakdown = new ArrayList<String>();
		
		// REMOVE ARTICLES
		
		for (String word : individualWords) {
			if (!ARTICLES.contains(word.toLowerCase())) {
				breakdown.add(word.toLowerCase());
			} else {	// MUSIC EDGE CASE RIGHT HERE !!!
				if (word.toLowerCase().equals("a") && breakdown.get(0).equals("play")) {
					breakdown.add(word.toLowerCase());
				}
			}
		}
		
		if (breakdown.size() > 1) {
			verb = breakdown.get(0);
			
			String fullNoun = "";
			String fullLocation = "";
			
			boolean addLocation = false;

			for (int i = 1; i < breakdown.size(); i++) {					
				if (PREPOSITIONS.contains(breakdown.get(i))) {
					addLocation = true;
				} else {
					if (addLocation) {
						fullLocation += breakdown.get(i) + " ";
					} else {
						fullNoun += breakdown.get(i) + " ";
					}
				}
			}
			
			noun = fullNoun.trim();
			
			if (fullLocation != "") {
				location = fullLocation.trim();
			}
		} else if (breakdown.size() == 1) {
			verb = breakdown.get(0);
		}
	}
	
	private String replaceSynonyms(String input) {
		String replace = input.replace("left", "east");
		replace = replace.replace("right", "west");
		replace = replace.replace("up", "north");
		replace = replace.replace("down", "south");
		replace = replace.replace("yes", "y");
		replace = replace.replace("no", "n");
		return replace;
	}
	
	public String execute() {
		String output = null;
		
		System.out.println("VERB: " + verb + " | NOUN: " + noun + " | LOCATION: " + location);
		
		if (verb != null) {
			if (noun != null || SINGLE_WORD_COMMANDS.contains(verb)) {
				switch (verb) {
					case "examine":
					case "look":
						output = new LookCommand(game, verb, noun, location).getOutput();						
						break;
					case "open":
						output = new OpenCommand(game, verb, noun, location).getOutput();	
						break;
					case "list":
						output = new ListCommand(game, verb, noun, location).getOutput();						
						break;
					case "grab":
					case "take":
						output = new TakeCommand(game, verb, noun, location).getOutput();						
						break;
					case "place":
					case "drop":
						output = new DropCommand(game, verb, noun, location).getOutput();					
						break;
					case "move":
					case "walk":
						output = new WalkCommand(game, verb, noun, location).getOutput();						
						break;
					case "unlock":
						output = new UnlockCommand(game, verb, noun, location).getOutput();						
						break;
					case "solve":
					case "type":
						output = new TypeCommand(game, verb, noun, location).getOutput();						
						break;
					case "read":
						output = new ReadCommand(game, verb, noun, location).getOutput();						
						break;
					case "push":
						output = new PushCommand(game, verb, noun, location).getOutput();						
						break;
					case "play":
						output = new PlayCommand(game, verb, noun, location).getOutput();						
						break;
					case "cut":
						output = new CutCommand(game, verb, noun, location).getOutput();						
						break;
					case "pour":
						output = new PourCommand(game, verb, noun, location).getOutput();						
						break;
					case "hint":
						output = new HintCommand(game, verb, noun, location).getOutput();
						break;
					case "talk":
						output = new TalkCommand(game, verb, noun, location).getOutput();
						break;
					case "y":
						output = new YesCommand(game, verb, noun, location).getOutput();
						break;
					case "n":
						output = new NoCommand(game, verb, noun, location).getOutput();
						break;
				}
			} else {
				if (VALID_COMMANDS.contains(verb)) {
					output = "Missing argument for command \"" + verb + "\"";
				} else {
					output = "Unknown command.";
				}
			}
		} else {
			output = "Unknown command.";
		}
		
		return output != null ? output.trim() : invalidCommand;
	}
	
	public String getBreakdown() {
		String breakdown = "";
		
		if (verb != null) {
			breakdown += verb;
			
			if (noun != null) {
				breakdown += "/" + noun;
				
				if (location != null) {
					breakdown += "/" + location;
				}
			}
		}
		
		return breakdown;
	}
}