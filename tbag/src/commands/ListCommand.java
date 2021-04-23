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
		
		if (noun == null || noun.equals("room")) {
			if (room.hasItems()) {
				game.setOutput("This room has a " + room.listItems());
			} else {
				game.setOutput("This room does not contain any items.");
			}
		} else if (noun.equals("inventory")) {
			game.setOutput(inventory.openInventory());
		} else if (noun.equals("objects")) {
			game.setOutput(room.listObjects());
		} else if (room.hasObject(noun)) {
			game.setOutput(room.getObject(noun).getInventory().listItems());
		} else {
			game.setOutput("Could not find " + noun + " to list.");
		}
	}

}
