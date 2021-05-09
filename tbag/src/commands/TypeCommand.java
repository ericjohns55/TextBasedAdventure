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
		
		if (puzzle.isWrittenSolution()) {	// check that puzzle is written
			if (!puzzle.isSolved()) {	// make sure the puzzle is not already solved
				if (puzzle.getSolution().equals(noun)) {	// check solution
					RoomObject obstacle = room.getObject(puzzle.getUnlockObstacle());
					
					game.type(puzzle, obstacle, noun);	// solve and update DB
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