package junit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import game.Game;
import input.Command;

public class TestGame {	
	@Test
	public void testBadCommand() {
		Game game = new Game(1);
		game.runCommand("eat chocolate");
		assertEquals("Unknown command.", game.getOutput());
	}
	
	@Test
	public void testOutput() {
		Game game = new Game(1);
		game.setOutput("OUTPUT");
		assertEquals("OUTPUT", game.getOutput());
		
		game.addOutput("TUPTUO");
		assertEquals("OUTPUTTUPTUO", game.getOutput());
	}
}
