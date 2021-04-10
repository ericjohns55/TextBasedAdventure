package commands;

import game.Game;
import items.Inventory;
import items.Item;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class PourCommand extends UserCommand {
	public PourCommand(Game game, String verb, String noun, String location) {
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
		
		if (location != null) {
			if (room.hasObject(location)) {
				if (inventory.contains(noun)) {
					RoomObject object = room.getObject(location);
					Item item = inventory.getItem(noun);
					
					if (item.isPourable()) {
						if (object.isCoverable()) {
							if (!object.isCovered()) {
								object.cover(noun);
								
								output = "You poured the " + noun + " on the " + location + ".";
								
								if (item.consumeOnUse()) {
									inventory.removeItem(noun);
								}
								
								if (puzzle.getSolution().equals(object.getCovering())) {
									RoomObject solutionObject = room.getObject(puzzle.getUnlockObstacle());
									
									if (solutionObject.isLocked()) {
										solutionObject.setLocked(false);
										output += "\nA " + solutionObject.getName() + " to the " + solutionObject.getDirection() + " swings open!";
									}
								}
							} else {
								output = "This object is already covered.";
							}
						} else {
							output = "Cannot pour " + noun + " on " + location + ".";
						}
					} else {
						output = "You cannot pour a " + noun + ".";
					}
				} else {
					output = "You do not possess a " + noun + ".";
				}
			} else {
				output = "A " + location + " does not exist in your room.";
			}
		} else {
			output = "I am not sure where to pour that.";
		}
		
		return output;
	}

}
