package commands;

import game.Game;
import items.Inventory;
import map.Room;

public class ListCommand extends UserCommand {
	public ListCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public String getOutput() {
		String output;
		
		String noun = getNoun();
		Room room = getRoom();
		Inventory inventory = getInventory();
		
		if (noun == null || noun.equals("room")) {
			output = "This room has a " + room.listItems();
		} else if (noun.equals("inventory")) {
			output = inventory.openInventory();
		} else if (noun.equals("objects")) {
			output = room.listObjects();
		} else if (room.hasObject(noun)) {
			output = room.getObject(noun).getInventory().listItems();
		} else {
			output = "Could not find " + noun + " to list.";
		}
		
		return output;
	}

}
