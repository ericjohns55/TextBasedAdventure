package commands;

import game.Game;
import items.Inventory;
import items.Item;
import map.PlayableObject;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class PlayCommand extends UserCommand {
	@Override
	public void execute() {
		String noun = getNoun();
		String location = getLocation();
		
		Room room = getRoom();
		Inventory inventory = getInventory();
		Puzzle puzzle = getPuzzle();
		Game game = getGame();
		
		if (location != null) {	// confirm a location is specified
			if (room.hasObject(location)) {
				if (room.getObject(location) instanceof PlayableObject) {	// make sure the object is playable
					PlayableObject object = (PlayableObject) room.getObject(location);
					
					game.setOutput("You played " + noun + " on the " + location + ".");	// play the noun on the object
					
					boolean unlock = false;	// check if something needs to be unlocked
					
					if (object.isInstrument()) {						
						unlock = game.play(object, noun, puzzle);	// if instrument, update DB with notes played
					} else {
						if (inventory.contains(noun)) {	// if object contains an item
							Item toDrop = inventory.removeItem(noun);
							
							// update DB and add item to object inventory
							unlock = game.play(object, toDrop, getPlayer(), puzzle, noun, location);
						} else {
							game.setOutput("You do not have that item!");	// item doesnt exist, overwrite output
						}
					}
					
					if (unlock) {	
						// if something needs to be unlocked, grab the unlockable object from the puzzle
						RoomObject toUnlock = room.getObject(puzzle.getUnlockObstacle());
						
						if (toUnlock.isLocked()) {	// if still locked, unlock object
							game.unlockObject(toUnlock, false);
							game.addOutput("\nA " + toUnlock.getName() + " to the " + toUnlock.getDirection() + " swings open.");
						}
					}
				} else {
					game.setOutput("You cannot play anything on that!");
				}
			} else {
				game.setOutput("Could not find a " + location + ".");
			}
		} else {
			game.setOutput("Not sure where you want me to play that...");
		}
	}
}
