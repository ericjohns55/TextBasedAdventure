package commands;

import game.Game;
import items.Inventory;
import map.Room;

public class ListCommand extends UserCommand {
	@Override
	public void execute() {
		String noun = getNoun();
		Room room = getRoom();
		Inventory inventory = getInventory();
		Game game = getGame();
		
		if (noun == null || noun.equals("room")) {	// list the room items
			if (room.hasItems()) {
				game.setOutput("This room has a " + room.listItems());
			} else {
				game.setOutput("This room does not contain any items.");
			}
		} else if (noun.equals("inventory")) {	// list player inventory
			game.setOutput(inventory.openInventory());
		} else if (noun.equals("objects")) {	// list all objects in the room
			game.setOutput(room.listObjects());
		} else if (room.hasObject(noun)) {	// list all items on a room object
			game.setOutput(room.getObject(noun).getInventory().listItems());
		} else {
			game.setOutput("Could not find " + noun + " to list.");
		}
	}
}
