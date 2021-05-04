package commands;

import actor.Player;
import game.Game;

import items.Inventory;
import items.Item;
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
			if (room.hasObject(location)) {
				if (inventory.contains(noun)) {
					RoomObject object = room.getObject(location);
					if (noun.equals(puzzle.getSolution())) {
						if (!object.isFed()) {						
							game.feedItem(object, player, puzzle, noun, location);														
						} else {
							game.setOutput("This object is already fed.");
						}
					} else {
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