package commands;

import game.Game;
import items.Inventory;
import map.Room;
import map.RoomObject;

public class LookCommand extends UserCommand {
	public LookCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public void execute() {
		String noun = getNoun();
		Room room = getRoom();
		Inventory inventory = getInventory();
		Game game = getGame();
		
		if (noun == null || noun == "" || noun.equals("room")) {
			game.setOutput(room.getDescription());
		} else {
			if (room.hasItem(noun)) {
				game.setOutput(room.getItem(noun).getDescription());
			} else if (inventory.contains(noun)) {
				game.setOutput(inventory.getItem(noun).getDescription());
			} else if (room.hasObject(noun)) {
				RoomObject object = room.getObject(noun);
				
				if (object.isLocked()) {
					game.setOutput("This object is locked, I cannot see what is inside.");
				} else {
					game.setOutput(room.getObject(noun).getDescription());
				}
			}
			
			else {
				game.setOutput("That item doesn't exist!");
			}
		}
	}
}
