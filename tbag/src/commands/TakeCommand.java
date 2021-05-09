package commands;

import java.util.HashMap;

import actor.NPC;
import game.Game;
import items.Inventory;
import items.Item;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class TakeCommand extends UserCommand {
	@Override
	public void execute() {		
		String noun = getNoun();
		String location = getLocation();
		
		Room room = getRoom();
		Puzzle puzzle = getPuzzle();
		NPC npc = room.getNpc();
		Game game = getGame();

		if (location == null || location.equals("room") || location.equals("floor")) {
			if (noun == null) {	// make sure an item is specified
				game.setOutput("Please specify an item.");
			} else if (room.hasItem(noun)) {
				Item toGrab = room.getItem(noun);	// grab item from room
				
				if (toGrab != null) {
					game.take(room, toGrab, getPlayer(), noun);	// update DB
					game.setOutput("You picked up " + noun + ".");
				} else {
					game.setOutput("This item does not exist in your current room.");
				}
			} else if (noun.equals("all")) {	// take all command
				if (!room.getInventory().isEmpty()) {	// make sure inventory is not empty
					HashMap<String, Item> items = room.getInventory().getAllItems();
					
					for (String identifier : items.keySet()) {	// loop through all items and add them to player inventory; update DB
						game.take(room, items.get(identifier), getPlayer(), noun);
						game.addOutput("You picked up " + identifier + ".\n");
					}
				} else {
					game.setOutput("This room does not contain any items.");
				}
			} else {
				game.setOutput("This item does not exist in your current room.");
			}
		} 
		else if (room.hasNpc() && location.equals(npc.getName())) {	// room has an NPC
			if (npc.getInventory().contains(noun)) {	// make sure NPC has said item
				Item toGrab = npc.getInventory().getItem(noun);
				
				if (toGrab != null) {
					toGrab.setInInventory(true);	
					getInventory().addItem(noun, toGrab);
					npc.getInventory().removeItem(noun);
					game.take(room, toGrab, getPlayer(), noun);	// grab item from NPC and update DB
					game.setOutput("You picked up " + noun + ".");
				} else {
					game.setOutput(npc.getName() + " doesn't have that item.");
				}
			}
			else if (noun == null) {
				game.setOutput("Please specify an item.");	// item not specified
			} else {
				game.setOutput(npc.getName() + " doesn't have that item.");
			} 
		}
		else {
			if (room.hasObject(location)) {	// player wants to grab from a location
				RoomObject roomObject = room.getObject(location);
				
				if (roomObject.canHoldItems()) {	// make sure the object can actual hold items first, if not theres nothing to take
					Inventory objectInventory = roomObject.getInventory();
					
					if (!roomObject.isLocked()) {	// check if the object is not locked, cannot grab from a locked object
						if (objectInventory.contains(noun)) {
							Item toGrab = objectInventory.removeItem(noun);
														
							game.take(roomObject, toGrab, getPlayer(), puzzle, noun);	// grab item from object and update DB
							game.setOutput("You picked up " + noun + ".");	
						} else if (noun.equals("all")) {	// they want to take all
							if (!objectInventory.isEmpty()) {
								HashMap<String, Item> items = objectInventory.getAllItems();
								
								for (String identifier : items.keySet()) {	// loop through all items and take them; update DB
									game.take(roomObject, objectInventory.getItem(identifier), getPlayer(), puzzle, noun);
									game.addOutput("You picked up " + identifier + " from " + location + ".\n");
								}								
							} else {
								game.setOutput("The " + location + " does not possess any items.");
							}
						} else {
							game.setOutput("This object does not have that item.");
						}
					} else {
						game.setOutput("This " + roomObject.getName() + " is locked.");
					}									
				} else {
					game.setOutput("This object does not possess any items.");
				}
			} else if (room.hasNpc() && !location.equals(npc.getName())) {	// there is no NPC
				game.setOutput("There is no one named " + location + " in this room.");
			} else {
				game.setOutput("Could not find that object.");
			}
		}
	}
}
