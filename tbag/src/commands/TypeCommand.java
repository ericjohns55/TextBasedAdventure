package commands;

import game.Game;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class TypeCommand extends UserCommand {
	public TypeCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public String getOutput() {
		String output;
		
		String noun = getNoun();
		
		Puzzle puzzle = getPuzzle();
		Room room = getRoom();
		
		if (puzzle.isWrittenSolution()) {
			if (!puzzle.isSolved()) {
				if (puzzle.getSolution().equals(noun)) {
					puzzle.setSolved(true);
					
					RoomObject obstacle = room.getObject(puzzle.getUnlockObstacle());
					
					if (obstacle.isLocked()) {
						obstacle.setLocked(false);
						output = "A " + obstacle.getName() + " to the " + obstacle.getDirection() + " swings open!";
					} else {
						output = "You typed " + noun + ".";
					}
				} else {
					output = noun + " is not correct.";
				}
			} else {
				output = "You already solved this puzzle!";
			}						
		} else {
			output = "Could not find anything to solve...";
		}
		
		return output;
	}

}
