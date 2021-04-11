package commands;

import java.util.HashMap;

import game.Game;
import items.CompoundItem;
import items.Inventory;
import items.Item;
import map.Room;
import map.RoomObject;

public class CutCommand extends UserCommand {
	public CutCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public String getOutput() {
		String output = null;
		
		String noun = getNoun();
		String location = getLocation();
		
		Room room = getRoom();
		Inventory inventory = getInventory();
		
		if (location != null) {
			if (room.hasObject(location)) {
				RoomObject object = room.getObject(location);
				
				if (object.getInventory().contains(noun)) {
					if (object.getInventory().getItem(noun) instanceof CompoundItem) {
						CompoundItem item = (CompoundItem) object.getInventory().getItem(noun);
						
						if (item.isBreakable()) {
							if (inventory.contains(item.getBreakItem())) {
								HashMap<String, Item> items = item.getItems();
								
								for (String identifier : items.keySet()) {
									object.getInventory().addItem(identifier, items.get(identifier));
									System.out.println("Adding " + identifier);
								}
								
								object.getInventory().removeItem(noun);
								item.getInventory().emptyInventory();
								
								output = "You break apart the " + noun + " and dump the contents on the " + location + ".";
							} else {
								output = "You do not possess the needed item to cut this.";
							}
						} else {
							output = "You cannot cut this item.";
						}
					} else {
						output = "Cannot cut this item.";
					}
				} else {
					output = "That " + location + " does not countain a " + noun + ".";
				}
			} else {
				output = "That location does not exist.";
			}
		} else {
			if (room.hasItem(noun)) {
				if (room.getItem(noun) instanceof CompoundItem) {
					CompoundItem item = (CompoundItem) room.getItem(noun);
					
					if (item.isBreakable()) {
						if (inventory.contains(item.getBreakItem())) {
							HashMap<String, Item> items = item.getItems();
							
							for (String identifier : items.keySet()) {
								room.addItem(identifier, items.get(identifier));
							}
							
							room.removeItem(noun);
							item.getInventory().emptyInventory();
							
							output = "You break apart the " + noun + " and dumb the contents on the floor.";
						} else {
							output = "You do not possess the needed item to cut this.";
						}
					} else {
						output = "You cannot cut this item.";
					}
				}
			} else {
				output = "The " + noun + " item does not exist.";
			}
		}
		
		return output;
	}

}
