package junit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import game.Game;
import input.Command;

public class TestCommands {
	private Command command;
	private Game game;
	
	@Before
	public void setUp() {
		game = new Game();
	}
	
	@Test
	public void testOneWord() {
		command = new Command("drop", game);
		assertEquals("drop", command.getBreakdown());
	}
	
	@Test
	public void testVerbNoun() {
		command = new Command("drop keys", game);
		assertEquals("drop/keys", command.getBreakdown());
	}
	
	@Test
	public void testVerbNounStripArticles() {
		command = new Command("drop the keys", game);
		assertEquals("drop/keys", command.getBreakdown());
	}
	
	@Test
	public void testVerbMultiwordNoun() {
		command = new Command("drop jar of eyes", game);
		assertEquals("drop/jar of eyes", command.getBreakdown());
	}
	
	@Test
	public void testVerbMultiwordNounWithArticles() {
		command = new Command("drop the jar of eyes", game);
		assertEquals("drop/jar of eyes", command.getBreakdown());
	}
	
	@Test
	public void testVerbNounLocation() {
		command = new Command("drop keys on table", game);
		assertEquals("drop/keys/table", command.getBreakdown());
	}
	
	@Test
	public void testVerbMultiwordNounLocation() {
		command = new Command("drop jar of eyes on table", game);
		assertEquals("drop/jar of eyes/table", command.getBreakdown());
	}
	
	@Test
	public void testVerbMultiwordNounMultiwordLocation() {
		command = new Command("drop jar of eyes on table top", game);
		assertEquals("drop/jar of eyes/table top", command.getBreakdown());
	}
	
	@Test
	public void testVerbMultiwordNounMultiwordLocationWithArticles() {
		command = new Command("drop the jar of eyes on a floor board", game);
		assertEquals("drop/jar of eyes/floor board", command.getBreakdown());
	}
}
