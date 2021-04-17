package commands;

import game.Game;
import items.Inventory;
import items.Item;
import map.PlayableObject;
import map.Room;
import map.RoomObject;
import puzzle.ObjectPuzzle;
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
		
		if (location != null) {
			if (room.hasObject(location)) {
				if (room.getObject(location) instanceof PlayableObject) {
					PlayableObject object = (PlayableObject) room.getObject(location);
					
					game.setOutput("You played " + noun + " on the " + location + ".");
					
					boolean unlock = false;
					
					if (object.isInstrument()) {
						if (object.playNote(noun)) {
							if (object.playedPassage()) {
								unlock = true;
							}
						} else {
							game.setOutput("You entered an invalid note.");
						}
					} else {
						if (inventory.contains(noun)) {
							Item toDrop = inventory.removeItem(noun);
							object.getInventory().addItem(noun, toDrop);
							
							game.setOutput("Played " + noun + " on the " + location + ".");
							
							if (puzzle instanceof ObjectPuzzle) {
								ObjectPuzzle obstaclePuzzle = (ObjectPuzzle) puzzle;
								
								if (obstaclePuzzle.isSolved()) {
									unlock = true;
								}
							}
						} else {
							game.setOutput("You do not have that item!");
						}
					}
					
					if (unlock) {
						RoomObject toUnlock = room.getObject(puzzle.getUnlockObstacle());
						
						if (toUnlock.isLocked()) {
							toUnlock.setLocked(false);
							
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
