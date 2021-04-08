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
	public PlayCommand(Game game, String verb, String noun, String location) {
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
				if (room.getObject(location) instanceof PlayableObject) {
					PlayableObject object = (PlayableObject) room.getObject(location);
					
					output = "You played " + noun + " on the " + location + ".";
					
					boolean unlock = false;
					
					if (object.isInstrument()) {
						if (object.playNote(noun)) {
							if (object.playedPassage()) {
								unlock = true;
							}
						} else {
							output = "You entered an invalid note.";
						}
					} else {
						if (inventory.contains(noun)) {
							Item toDrop = inventory.removeItem(noun);
							object.getInventory().addItem(noun, toDrop);
							
							output = "Played " + noun + " on the " + location + ".";
							
							if (puzzle instanceof ObjectPuzzle) {
								ObjectPuzzle obstaclePuzzle = (ObjectPuzzle) puzzle;
								
								if (obstaclePuzzle.isSolved()) {
									unlock = true;
								}
							}
						} else {
							output = "You do not have that item!";
						}
					}
					
					if (unlock) {
						RoomObject toUnlock = room.getObject(puzzle.getUnlockObstacle());
						
						if (toUnlock.isLocked()) {
							toUnlock.setLocked(false);
							
							output += "\nA " + toUnlock.getName() + " to the " + toUnlock.getDirection() + " swings open.";
						}
					}
				} else {
					output = "You cannot play anything on that!";
				}
			} else {
				output = "Could not find a " + location + ".";
			}
		} else {
			output = "Not sure where you want me to play that...";
		}
		
		return output;
	}

}
