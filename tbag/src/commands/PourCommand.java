package commands;

import game.Game;
import items.Inventory;
import items.Item;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class PourCommand extends UserCommand {
	@Override
	public void execute() {
		String noun = getNoun();
		String location = getLocation();
		
		Room room = getRoom();
		Inventory inventory = getInventory();
		Puzzle puzzle = getPuzzle();
		Game game = getGame();
		
		if (location != null) {
			if (room.hasObject(location)) {
				if (inventory.contains(noun)) {
					RoomObject object = room.getObject(location);
					Item item = inventory.getItem(noun);
					
					if (item.isPourable()) {
						if (object.isCoverable()) {
							if (!object.isCovered()) {
								object.cover(noun);
								
								game.setOutput("You poured the " + noun + " on the " + location + ".");
								
								if (item.consumeOnUse()) {
									inventory.removeItem(noun);
								}
								
								if (puzzle.getSolution().equals(object.getCovering())) {
									RoomObject solutionObject = room.getObject(puzzle.getUnlockObstacle());
									
									if (solutionObject.isLocked()) {
										solutionObject.setLocked(false);
										game.addOutput("\nA " + solutionObject.getName() + " to the " + solutionObject.getDirection() + " swings open!");
									}
								}
							} else {
								game.setOutput("This object is already covered.");
							}
						} else {
							game.setOutput("Cannot pour " + noun + " on " + location + ".");
						}
					} else {
						game.setOutput("You cannot pour a " + noun + ".");
					}
				} else {
					game.setOutput("You do not possess a " + noun + ".");
				}
			} else {
				game.setOutput("A " + location + " does not exist in your room.");
			}
		} else {
			game.setOutput("I am not sure where to pour that.");
		}
	}

}
