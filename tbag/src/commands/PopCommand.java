package commands;

import game.Game;
import items.CompoundItem;
import items.Inventory;
import map.Room;
import map.RoomObject;

public class PopCommand extends UserCommand {
	@Override
	public void execute() {		
		String noun = getNoun();
		String location = getLocation();
		
		Room room = getRoom();
		Inventory inventory = getInventory();
		
		Game game = getGame();
		
		if (location != null) {	// check location
			if (room.hasObject(location)) {
				RoomObject object = room.getObject(location);
				
				if (object.getInventory().contains(noun)) {
					if (object.getInventory().getItem(noun) instanceof CompoundItem) {	// make sure the noun is a compound item
						CompoundItem item = (CompoundItem) object.getInventory().getItem(noun);
						
						if (item.isPoppable()) {
							if (inventory.contains(item.getBreakItem().getItemID())) {
								game.popItem(object, item, noun, location);	// pop item if the correct break identifier is in inventory
							} else {
								game.setOutput("You do not possess the needed item to pop this.");
							}
						} else {
							game.setOutput("You cannot pop this item.");
						}
					} else {
						game.setOutput("Cannot pop this item.");
					}
				} else {
					game.setOutput("That " + location + " does not countain a " + noun + ".");
				}
			} else {
				game.setOutput("That location does not exist.");
			}
		} else {
			if (room.hasItem(noun)) {	// in room 
				if (room.getItem(noun) instanceof CompoundItem) {
					CompoundItem item = (CompoundItem) room.getItem(noun);
					
					if (item.isPoppable()) {
						if (inventory.contains(item.getBreakItem())) {	// pop item if correct break identifier is in inventory
							game.popItem(room, item, noun);
						} else {
							game.setOutput("You do not possess the needed item to pop this.");
						}
					} else {
						game.setOutput("You cannot pop this item.");
					}
				}
			} else {
				game.setOutput("The " + noun + " item does not exist.");
			}
		}
	}
}
