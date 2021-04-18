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
					RoomObject obstacle = room.getObject(puzzle.getUnlockObstacle());
					
					game.type(puzzle, obstacle, noun);
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