package commands;

import game.Game;
import items.Inventory;
import map.Room;

public class OpenCommand extends UserCommand {
	@Override
	public void execute() {
		String noun = getNoun();
		Room room = getRoom();
		Inventory inventory = getInventory();
		Game game = getGame();
		
		if (noun.equals("inventory")) {	// print out inventory description
			game.setOutput(inventory.openInventory());
		} else if (room.hasObject(noun)) {	// check if the object exists
			if (room.getObject(noun).isLocked()) {	
				// most likely a chest here, but check if it is locked and do not print out if so
				game.setOutput("This " + room.getObject(noun).getName() + " is locked.");
			} else {
				game.setOutput(room.getObject(noun).getInventory().listItems(noun));	// list items otherwise
			}
		} else {
			game.setOutput("Could not find " + noun + "to open.");
		}
	}

}
