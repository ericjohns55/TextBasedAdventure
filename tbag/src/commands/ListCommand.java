package commands;

import game.Game;
import items.Inventory;
import map.Room;

public class ListCommand extends UserCommand {
	public ListCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public void execute() {
		String output;
		
		String noun = getNoun();
		Room room = getRoom();
		Inventory inventory = getInventory();
		Game game = getGame();
		
		if (noun == null || noun.equals("room")) {
			game.setOutput("This room has a " + room.listItems());
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
