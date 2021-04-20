package commands;

import actor.NPC;
import game.Game;
import items.Inventory;
import items.Item;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class TakeCommand extends UserCommand {
	public TakeCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public String getOutput() {
		String output;
		
		String noun = getNoun();
		String location = getLocation();
		
		Room room = getRoom();
		Inventory inventory = getInventory();
		Puzzle puzzle = getPuzzle();
		NPC npc = room.getNpc();
		
		if (location == null || location.equals("room") || location.equals("floor")) {
			if (room.hasItem(noun)) {
				Item toGrab = room.getItem(noun);
				
				if (toGrab != null) {
					toGrab.setInInventory(true);
					inventory.addItem(noun, toGrab);
					room.removeItem(noun);
					
					output = "You picked up " + noun + ".";
				} else {
					output = "This item does not exist in your current room.";
				}
			} else if (noun == null) {
				output = "Please specify an item.";
			} else {
				output = "This item does not exist in your current room.";
			}
		} 
		else if (room.hasNpc() && location.equals(npc.getName())) {
			if (npc.getInventory().contains(noun)) {
				Item toGrab = npc.getInventory().getItem(noun);
				
				if (toGrab != null) {
					toGrab.setInInventory(true);
					inventory.addItem(noun, toGrab);
					npc.getInventory().removeItem(noun);
					
					output = "You picked up " + noun + ".";
				} else {
					output = npc.getName() + " doesn't have that item.";
				}
			}
			else if (noun == null) {
				output = "Please specify an item.";
			} else {
				output = npc.getName() + " doesn't have that item.";
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
							inventory.addItem(noun, toGrab);
							toGrab.setInInventory(true);
							
							output = "You picked up " + noun + ".";
							
							if (puzzle.getDescription().equals("weightPuzzle")) {
								double weightSolution = Double.parseDouble(puzzle.getSolution());
								
								if (objectInventory.getCurrentWeight() < weightSolution) {
									RoomObject obstacle = room.getObject(puzzle.getUnlockObstacle());	
									
									if (!obstacle.isLocked() && obstacle.wasPreviouslyUnlocked()) {
										obstacle.setLocked(true);
										obstacle.setPreviouslyUnlocked(false);
										output += "\nA " + obstacle.getName() + " to the " + obstacle.getDirection() + " slams shut.";
									}
								}
							}
						} else {
							output = "This object does not have that item.";
						}
					} else {
						output = "This " + roomObject.getName() + " is locked.";
					}									
				} else {
					output = "This object does not possess any items.";
				}
			} else if (room.hasNpc() && !location.equals(npc.getName())) {
				output = "There is no one named " + location + " in this room.";
			} else {
				output = "Could not find that object.";
			}
		}
		
		return output;
	}

}
