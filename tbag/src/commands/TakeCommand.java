package commands;

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
			if (room.hasItem(noun)) {
				Item toGrab = room.getItem(noun);
				
				if (toGrab != null) {
					game.take(room, toGrab, getPlayer(), noun);
					game.setOutput("You picked up " + noun + ".");
				} else {
					game.setOutput("This item does not exist in your current room.");
				}
			} else if (noun == null) {
				game.setOutput("Please specify an item.");
			} else {
				game.setOutput("This item does not exist in your current room.");
			}
		} 
		else if (room.hasNpc() && location.equals(npc.getName())) {
			if (npc.getInventory().contains(noun)) {
				Item toGrab = npc.getInventory().getItem(noun);
				
				if (toGrab != null) {
					toGrab.setInInventory(true);
					getInventory().addItem(noun, toGrab);
					// TODO: DB take for NPC
					npc.getInventory().removeItem(noun);
					
					game.setOutput("You picked up " + noun + ".");
				} else {
					game.setOutput(npc.getName() + " doesn't have that item.");
				}
			}
			else if (noun == null) {
				game.setOutput("Please specify an item.");
			} else {
				game.setOutput(npc.getName() + " doesn't have that item.");
			} 
		}
		else {
			if (room.hasObject(location)) {
				RoomObject roomObject = room.getObject(location);
				
				if (roomObject.canHoldItems()) {
					Inventory objectInventory = roomObject.getInventory();
					
					if (!roomObject.isLocked()) {
						if (objectInventory.contains(noun)) {
							Item toGrab = objectInventory.removeItem(noun);
														
							game.take(roomObject, toGrab, getPlayer(), puzzle, noun);
						} else {
							game.setOutput("This object does not have that item.");
						}
					} else {
						game.setOutput("This " + roomObject.getName() + " is locked.");
					}									
				} else {
					game.setOutput("This object does not possess any items.");
				}
			} else if (room.hasNpc() && !location.equals(npc.getName())) {
				game.setOutput("There is no one named " + location + " in this room.");
			} else {
				game.setOutput("Could not find that object.");
			}
		}
	}
}
