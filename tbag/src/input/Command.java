package input;

import java.util.ArrayList;

import actor.Player;
import game.Game;
import items.Inventory;
import items.Item;
import map.Room;

public class Command {
	public final static String invalidCommand = "I do not understand that command";
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
		
		if (breakdown.size() >= 1) {
			verb = breakdown.get(0);
			noun = breakdown.get(breakdown.size() - 1);
		} else {
			noun = null;
		}
	}
	
	public String execute() {
		String output = null;
		
		Player player = game.getPlayer();
		Room room = player.getRoom();
		Inventory inventory = player.getInventory();
		
		if (verb != null) {
			switch (verb) {
				case "look":
					output = room.getDescription();
					break;
				case "open":
					if (noun.equals("inventory")) {
						output = inventory.openInventory();
					}
					
					break;
				case "grab":
				case "take":
					if (room.contains(noun)) {
						Item toGrab = room.getItem(noun);
						
						if (toGrab != null) {
							toGrab.setInInventory(true);
							inventory.addItem(noun, toGrab);
							room.removeItem(noun);
							
							output = "You picked up " + noun;
						} else {
							output = "This item does not exist in your current room.";
						}
					} else {
						output = "This item does not exist in your current room.";
					}
					
					break;
				case "drop":
					if (inventory.contains(noun)) {
						Item removed = inventory.removeItem(noun);
						removed.setInInventory(false);
						room.addItem(noun, removed);
						output = "You dropped " + noun + " on the floor.";
					} else {
						output = "You do not possess this item.";
					}
					
					break;
				case "move":
				case "walk":
					if (room.hasExit(noun)) {
						int roomID = room.getExit(noun).getRoomID();
						player.setRoomID(roomID);
						output = "You walk " + noun + "\n";
						output += player.getRoom().getDescription();
					} else {
						output = "You cannot move this direction.";
					}
					
					break;
			}
		}
		
		
		return output != null ? output : invalidCommand;
	}
}
