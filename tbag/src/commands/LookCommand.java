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
	public String getOutput() {
		String output;

		String noun = getNoun();
		Room room = getRoom();
		Inventory inventory = getInventory();
		
		if (noun == null || noun == "" || noun.equals("room")) {
			output = room.getDescription();
		} else {
			if (room.hasItem(noun)) {
				output = room.getItem(noun).getDescription();
			} else if (inventory.contains(noun)) {
				output = inventory.getItem(noun).getDescription();
			} else if (room.hasObject(noun)) {
				RoomObject object = room.getObject(noun);
				
				if (object.isLocked()) {
					output = "This object is locked, I cannot see what is inside.";
				} else {
					output = room.getObject(noun).getDescription();
				}
			}
			
			else {
				output = "That item doesn't exist!";
			}
		}
		
		return output;
	}
}
