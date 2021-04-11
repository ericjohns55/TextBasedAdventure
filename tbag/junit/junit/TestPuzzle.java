package junit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import items.Item;
import map.PlayableObject;
import map.RoomObject;
import puzzle.ObjectPuzzle;
import puzzle.Puzzle;

public class TestPuzzle {
	private Puzzle puzzle;
	
	@Before
	public void setUp() {
		puzzle = new Puzzle("Description", "Solution", "Hint", false, "door", 1);
	}
	
	@Test
	public void testObjectPuzzle() {
		Item record1 = new Item("record1", 0.2);
		PlayableObject recordPlayer = new PlayableObject("record player", "Plays music.", "north", "", false, 1);
		
		ObjectPuzzle objectPuzzle = new ObjectPuzzle("Play a record on a record player", "Play 'Crumbling Land'", "Zabriskie Point sountrack", recordPlayer, record1, "room6Door", 1);
		
		recordPlayer.getInventory().addItem("record1", record1);
		
		assertTrue(objectPuzzle.isSolved());
	}
	
	@Test
	public void testGettersSetters() {
		String description = "desc";
		String solution = "sol";
		boolean solved = false;
		String hint = "hInT";
		boolean writtenSolution = false;
		String unlockObstacle = "dOoR";
		
		puzzle.setDescription(description);
		puzzle.setSolution(solution);
		puzzle.setSolved(solved);
		puzzle.setHint(hint);
		puzzle.setWrittenSolution(writtenSolution);
		puzzle.setUnlockObstacle(unlockObstacle);
		
		assertEquals(description, puzzle.getDescription());
		assertEquals(solution, puzzle.getSolution());
		assertEquals(solved, puzzle.isSolved());
		assertEquals(hint, puzzle.getHint());
		assertEquals(writtenSolution, puzzle.isWrittenSolution());
		assertEquals(unlockObstacle, puzzle.getUnlockObstacle());
	}
}
