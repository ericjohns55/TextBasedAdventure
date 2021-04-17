package input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import game.Game;
import commands.*;

public class Command {
	private static Set<String> SINGLE_WORD_COMMANDS = new HashSet<>(Arrays.asList("look", "examine", "hint"));
	private static Set<String> PREPOSITIONS = new HashSet<>(Arrays.asList("on", "from", "to", "in"));
	private static Set<String> ARTICLES = new HashSet<>(Arrays.asList("the", "a", "an"));
	
	private static Set<String> VALID_COMMANDS = new HashSet<>(Arrays.asList("examine", "look", "open", "list", "grab", "take", "place",
			"drop", "move", "walk", "unlock", "type", "solve", "read", "push", "play", "cut", "pour", "hint"));
	
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
		return replace;
	}
	
	public void execute() {
		System.out.println("VERB: " + verb + " | NOUN: " + noun + " | LOCATION: " + location);
		
		if (verb != null) {
			if (noun != null || SINGLE_WORD_COMMANDS.contains(verb)) {
				switch (verb) {
					case "examine":
					case "look":
						new LookCommand(game, verb, noun, location).execute();						
						break;
					case "open":
						new OpenCommand(game, verb, noun, location).execute();	
						break;
					case "list":
						new ListCommand(game, verb, noun, location).execute();						
						break;
					case "grab":
					case "take":
						new TakeCommand(game, verb, noun, location).execute();						
						break;
					case "place":
					case "drop":
						new DropCommand(game, verb, noun, location).execute();					
						break;
					case "move":
					case "walk":
						new WalkCommand(game, verb, noun, location).execute();						
						break;
					case "unlock":
						new UnlockCommand(game, verb, noun, location).execute();						
						break;
					case "solve":
					case "type":
						new TypeCommand(game, verb, noun, location).execute();						
						break;
					case "read":
						new ReadCommand(game, verb, noun, location).execute();						
						break;
					case "push":
						new PushCommand(game, verb, noun, location).execute();						
						break;
					case "play":
						new PlayCommand(game, verb, noun, location).execute();						
						break;
					case "cut":
						new CutCommand(game, verb, noun, location).execute();						
						break;
					case "pour":
						new PourCommand(game, verb, noun, location).execute();						
						break;
					case "hint":
						new HintCommand(game, verb, noun, location).execute();
						break;
				}
			} else {
				if (VALID_COMMANDS.contains(verb)) {
					game.setOutput("Missing argument for command \"" + verb + "\"");
				} else {
					game.setOutput("Unknown command.");
				}
			}
		} else {
			game.setOutput("Unknown command.");
		}
		
		if (game.getOutput() == "") {
			game.setOutput(invalidCommand);
		} else {
			game.setOutput(game.getOutput().trim());
		}
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