package input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import game.Game;
import commands.*;

public class Command {
	private static Set<String> SINGLE_WORD_COMMANDS = new HashSet<>(Arrays.asList("look", "examine", "hint", "y", "n"));
	private static Set<String> PREPOSITIONS = new HashSet<>(Arrays.asList("on", "from", "to", "in"));
	private static Set<String> ARTICLES = new HashSet<>(Arrays.asList("the", "a", "an"));
	
	private HashMap<String, UserCommand> commands;
	
	public final static String invalidCommand = "I do not understand that command";
	private String input;
	private Game game;
	
	private String verb;
	private String noun;
	private String location;
	
	public Command(String input, Game game) {
		this.input = replaceSynonyms(input);
		this.game = game;
		
		commands = new HashMap<String, UserCommand>();
		commands.put("examine", new LookCommand());
		commands.put("look", new LookCommand());
		commands.put("open", new OpenCommand());
		commands.put("list", new ListCommand());
		commands.put("grab", new TakeCommand());
		commands.put("take", new TakeCommand());
		commands.put("place", new DropCommand());
		commands.put("drop", new DropCommand());
		commands.put("move", new WalkCommand());
		commands.put("walk", new WalkCommand());
		commands.put("unlock", new UnlockCommand());
		commands.put("type", new TypeCommand());
		commands.put("solve", new TypeCommand());
		commands.put("read", new ReadCommand());
		commands.put("push", new PushCommand());
		commands.put("play", new PlayCommand());
		commands.put("cut", new CutCommand());
		commands.put("slice", new CutCommand());
		commands.put("pour", new PourCommand());
		commands.put("hint", new HintCommand());
		commands.put("feed", new FeedCommand());
		commands.put("scan", new ScanCommand());
		commands.put("climb", new ClimbCommand());

    commands.put("y", new  YesCommand());
    commands.put("n", new NoCommand());
    commands.put("give", new GiveCommand());
    commands.put("options", new OptionsCommand());
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
//		replace = replace.replace("yes", "y");
//		replace = replace.replace("no", "n");
		return replace;
	}
	
	public void execute() {
		System.err.println("VERB: " + verb + " | NOUN: " + noun + " | LOCATION: " + location);
		
		// eat chocolate
		if (verb != null) {
			if (commands.containsKey(verb)) {
				if (noun != null || SINGLE_WORD_COMMANDS.contains(verb)) {
					UserCommand command = commands.get(verb);
					command.loadInputandGame(game, verb, noun, location);
					command.execute();
				} else {
					game.setOutput("Missing argument for command \"" + verb + "\"");
				}
			} else {
				game.setOutput("Unknown command.");
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