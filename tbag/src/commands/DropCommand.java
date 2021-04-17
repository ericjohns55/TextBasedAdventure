package commands;

import game.Game;
import items.Inventory;
import items.Item;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class DropCommand extends UserCommand {
	public DropCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public void execute() {		
		String noun = getNoun();
		String location = getLocation();
		
		Inventory inventory = getInventory();
		Room room = getRoom();
		Puzzle puzzle = getPuzzle();
		
		Game game = getGame();
		
		if (inventory.contains(noun)) {
			if (location == null || location.equals("room") || location.equals("floor")) {
				Item removed = inventory.removeItem(noun);
				removed.setInInventory(false);
				room.addItem(noun, removed);
				game.setOutput("You dropped " + noun + " on the floor.");
			} else {
				if (room.hasObject(location)) {
					RoomObject roomObject = room.getObject(location);
					
					if (roomObject.canHoldItems()) {
						Inventory objectInventory = roomObject.getInventory();
						
						Item toRemove = inventory.removeItem(noun);
						objectInventory.addItem(noun, toRemove);
						game.setOutput("You placed the " + noun + " on the " + location + "."); 
						
						if (puzzle.getDescription().equals("weightPuzzle")) {
							double weightSolution = Double.parseDouble(puzzle.getSolution());
							
							if (objectInventory.getCurrentWeight() >= weightSolution) {
								RoomObject obstacle = room.getObject(puzzle.getUnlockObstacle());	
								if (obstacle.isLocked()) {
									obstacle.setLocked(false);
									obstacle.setPreviouslyUnlocked(true);
									game.setOutput("A " + obstacle.getName() + " to the " + obstacle.getDirection() + " swings open.");
								}
							}
						}
					} else {
						game.setOutput("This object cannot hold that...");
					}
				} else {
					game.setOutput("Could not find that object.");
				}
			}
		} else if (noun == null) {
			game.setOutput("Please specify an item.");
		} else {
			game.setOutput("You do not possess this item.");
		}
	}

}
