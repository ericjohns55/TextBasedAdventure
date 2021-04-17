package commands;

import game.Game;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class TypeCommand extends UserCommand {
	@Override
	public void execute() {
		String noun = getNoun();
		
		Puzzle puzzle = getPuzzle();
		Room room = getRoom();
		Game game = getGame();
		
		if (puzzle.isWrittenSolution()) {
			if (!puzzle.isSolved()) {
				if (puzzle.getSolution().equals(noun)) {
					puzzle.setSolved(true);
					
					RoomObject obstacle = room.getObject(puzzle.getUnlockObstacle());
					
					if (obstacle.isLocked()) {
						obstacle.setLocked(false);
						game.setOutput("A " + obstacle.getName() + " to the " + obstacle.getDirection() + " swings open!");
					} else {
						game.setOutput("You typed " + noun + ".");
					}
				} else {
					game.setOutput(noun + " is not correct.");
				}
			} else {
				game.setOutput("You already solved this puzzle!");
			}						
		} else {
			game.setOutput("Could not find anything to solve...");
		}
	}
}
