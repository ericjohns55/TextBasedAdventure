package junit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import game.Game;

public class TestGame {
	private Game game;
	
	@Before
	public void setUp() {
		game = new Game();
	}
	
	@Test
	public void testBadCommand() {
		game.runCommand("eat chocolate");
		String feedback = game.getOutput();
		assertEquals(feedback, "Unknown command.");
	}
}
