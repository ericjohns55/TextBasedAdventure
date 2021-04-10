package commands;

import game.Game;
import items.Inventory;
import map.Room;

public class OpenCommand extends UserCommand {
	public OpenCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public String getOutput() {
		String output;
		
		String noun = getNoun();
		Room room = getRoom();
		Inventory inventory = getInventory();
		
		if (noun.equals("inventory")) {
			output = inventory.openInventory();
		} else if (room.hasObject(noun)) {
			if (room.getObject(noun).isLocked()) {
				output = "This " + room.getObject(noun).getName() + " is locked.";
			} else {
				output = room.getObject(noun).getInventory().listItems(noun);
			}
		} else {
			output = "Could not find " + noun + "to open.";
		}
		
		return output;
	}

}
