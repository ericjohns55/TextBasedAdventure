package commands;

import game.Game;
import items.Inventory;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class DropCommand extends UserCommand {
	@Override
	public void execute() {		
		String noun = getNoun();
		String location = getLocation();
		
		Inventory inventory = getInventory();
		Room room = getRoom();
		Puzzle puzzle = getPuzzle();
		
		Game game = getGame();
		
		if (inventory.contains(noun)) {
			if (location == null || location.equals("room") || location.equals("floor")) {
				game.dropItem(room, noun, getPlayer(), puzzle);
			} else {
				if (room.hasObject(location)) {
					RoomObject roomObject = room.getObject(location);
					
					if (roomObject.canHoldItems()) {						
						game.dropItem(roomObject, noun, getPlayer(), puzzle, location);
					} else {
						game.setOutput("This object cannot hold that...");
					}
				} else {
					game.setOutput("Could not find that object.");
				}
			}
		} else if (noun == null) {
			game.setOutput("Please specify an item.");
		} else {
			game.setOutput("You do not possess this item.");
		}
	}

}
