package commands;

import actor.Player;
import game.Game;

import items.Inventory;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class FeedCommand extends UserCommand {
	@Override
	public void execute() {
		String noun = getNoun();
		String location = getLocation();

		Room room = getRoom();
		Inventory inventory = getInventory();
		Puzzle puzzle = getPuzzle();
		Game game = getGame();
		Player player = getPlayer();

		if (location != null) {
			if (room.hasObject(location)) {	// check that location exists
				if (inventory.contains(noun)) {
					RoomObject object = room.getObject(location);
					
					// check that the noun specified is required to solve the puzzle
					if (noun.equals(puzzle.getSolution())) {
						if (!object.isFed()) {		// feed the object the required solution if it has not been solved yet	
							game.feedItem(object, player, puzzle, noun, location);														
						} else {
							game.setOutput("This object is already fed.");
						}
					} else {
						// if not the specific noun we dont want it
						game.setOutput("The " + location + " does not want to be fed " + noun + ".");
					}
				} else {
					game.setOutput("You do not possess a " + noun + ".");
				}
			} else {
				game.setOutput("A " + location + " does not exist in your room.");
			}
		} else {
			game.setOutput("I am not sure what to feed that.");
		}
	}
}