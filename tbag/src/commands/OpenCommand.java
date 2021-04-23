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
		
		if (noun.equals("inventory")) {
			game.setOutput(inventory.openInventory());
		} else if (room.hasObject(noun)) {
			if (room.getObject(noun).isLocked()) {
				game.setOutput("This " + room.getObject(noun).getName() + " is locked.");
			} else {
				game.setOutput(room.getObject(noun).getInventory().listItems(noun));
			}
		} else {
			game.setOutput("Could not find " + noun + "to open.");
		}
	}

}
