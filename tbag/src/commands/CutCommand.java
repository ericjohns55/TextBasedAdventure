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
		
		if (location != null) {
			if (room.hasObject(location)) {
				RoomObject object = room.getObject(location);
				
				if (object.getInventory().contains(noun)) {
					if (object.getInventory().getItem(noun) instanceof CompoundItem) {
						CompoundItem item = (CompoundItem) object.getInventory().getItem(noun);
						
						if (item.isBreakable()) {
							if (inventory.contains(item.getBreakItem())) {
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
			if (room.hasItem(noun)) {
				if (room.getItem(noun) instanceof CompoundItem) {
					CompoundItem item = (CompoundItem) room.getItem(noun);
					
					if (item.isBreakable()) {
						if (inventory.contains(item.getBreakItem())) {
							game.breakItem(room, item, noun);
						} else {
							game.setOutput("You do not possess the needed item to cut this.");
						}
					} else {
						game.setOutput("You cannot cut this item.");
					}
				}
			} else {
				game.setOutput("The " + noun + " item does not exist.");
			}
		}
	}

}
