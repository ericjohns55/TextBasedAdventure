package commands;

import game.Game;
import items.CompoundItem;
import items.Inventory;
import map.Room;
import map.RoomObject;

public class CutCommand extends UserCommand {
	@Override
	public void execute() {		
		String noun = getNoun();
		String location = getLocation();
		
		Room room = getRoom();
		Inventory inventory = getInventory();
		
		Game game = getGame();
		
		// check for location to get object on
		if (location != null) {
			if (room.hasObject(location)) {
				RoomObject object = room.getObject(location);
				
				if (object.getInventory().contains(noun)) {	// check that the location contains the object
					if (object.getInventory().getItem(noun) instanceof CompoundItem) {	// only objects that can be cut are compound items
						CompoundItem item = (CompoundItem) object.getInventory().getItem(noun);
						
						if (item.isBreakable()) {	// check that it is breakable
							// confirm that the player contains the item needed to break
							if (inventory.contains(item.getBreakItem().getItemID())) {	
								game.breakItem(object, item, noun, location);
							} else {
								game.setOutput("You do not possess the needed item to cut this.");
							}
						} else {
							game.setOutput("You cannot cut this item.");
						}
					} else {
						game.setOutput("Cannot cut this item.");
					}
				} else {
					game.setOutput("That " + location + " does not countain a " + noun + ".");
				}
			} else {
				game.setOutput("That location does not exist.");
			}
		} else {
			if (room.hasItem(noun)) {	// no location: check room instead
				if (room.getItem(noun) instanceof CompoundItem) {
					CompoundItem item = (CompoundItem) room.getItem(noun);
					
					// same logic as with location
					if (item.isBreakable()) {
						if (inventory.contains(item.getBreakItem())) {
							game.breakItem(room.getInventory(), item, noun, "on the floor.");
						} else {
							game.setOutput("You do not possess the needed item to cut this.");
						}
					} else {
						game.setOutput("You cannot cut this item.");
					}
				}
			} else if (inventory.contains(noun)) {	// no location and not in room, try player's inventory
				if (inventory.getItem(noun) instanceof CompoundItem) {
					CompoundItem item = (CompoundItem) inventory.getItem(noun);
					
					// same logic with adjusted inventory variables
					if (item.isBreakable()) {
						if (inventory.contains(item.getBreakItem())) {
							game.breakItem(inventory, item, noun, "into your inventory.");
						} else {
							game.setOutput("You do not possess the needed item to cut this.");
						}
					} else {
						game.setOutput("You cannot cut this item.");
					}
				}
			} else {	// not found: item does not exist anywhere
				game.setOutput("The " + noun + " item does not exist.");
			}
		}
	}
}