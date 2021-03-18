package cs320.windows98.tbag.input;

import java.util.ArrayList;

import cs320.windows98.tbag.game.Game;
import cs320.windows98.tbag.inventory.Inventory;
import cs320.windows98.tbag.items.Item;
import cs320.windows98.tbag.player.Player;
import cs320.windows98.tbag.room.Room;

public class Command {
	final static String invalidCommand = "I do not understand that command";
	private String input;
	private Game game;
	
	private String verb;
	private String noun;
	
	public Command(String input, Game game) {
		this.input = input;
		this.game = game;
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
		
		Player player = game.getPlayer();
		Room room = player.getRoom();
		Inventory inventory = player.getInventory();

		System.out.println("VERB: " + verb);
		System.out.println("NOUN: " + noun);
		
		if (verb.equals("look")) {
			output = room.getDescription();
			output += room.listItems();
		} else if (verb.equals("open")) {
			if (noun.equals("inventory")) {
				output = inventory.openInventory();
			}
		} else if (verb.equals("grab")) {
			if (room.contains(noun)) {
				Item toGrab = room.getItem(noun);
				
				if (toGrab != null) {
					inventory.addItem(noun, toGrab);
					room.removeItem(noun);
					
					output = "You picked up " + noun;
				} else {
					output = "This item does not exist in your current room.";
				}
			} else {
				output = "This item does not exist in your current room.";
			}
		} else if (verb.equals("drop")) {
			if (inventory.contains(noun)) {
				Item removed = inventory.removeItem(noun);
				room.addItem(noun, removed);
				output = "You dropped " + noun + " on the table.";
			} else {
				output = "You do not possess this item.";
			}
		} else if (verb.equals("move") || verb.equals("walk")) {
			if (room.hasExit(noun)) {
				int roomID = room.getExit(noun).getRoomID();
				player.setRoomID(roomID);
				output = "You walk " + noun + "\n";
				output += player.getRoom().getDescription();
			} else {
				output = "You cannot move this direction.";
			}
		}
		
		return output != null ? output : invalidCommand;
	}
}
