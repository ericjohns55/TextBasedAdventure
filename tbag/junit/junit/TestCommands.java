package junit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import input.Command;

public class TestCommands {
	private Command command;
	
	@Test
	public void testOneWord() {
		command = new Command("drop", null);
		assertEquals("drop", command.getBreakdown());
	}
	
	@Test
	public void testVerbNoun() {
		command = new Command("drop keys", null);
		assertEquals("drop/keys", command.getBreakdown());
	}
	
	@Test
	public void testVerbNounStripArticles() {
		command = new Command("drop the keys", null);
		assertEquals("drop/keys", command.getBreakdown());
	}
	
	@Test
	public void testVerbMultiwordNoun() {
		command = new Command("drop jar of eyes", null);
		assertEquals("drop/jar of eyes", command.getBreakdown());
	}
	
	@Test
	public void testVerbMultiwordNounWithArticles() {
		command = new Command("drop the jar of eyes", null);
		assertEquals("drop/jar of eyes", command.getBreakdown());
	}
	
	@Test
	public void testVerbNounLocation() {
		command = new Command("drop keys on table", null);
		assertEquals("drop/keys/table", command.getBreakdown());
	}
	
	@Test
	public void testVerbMultiwordNounLocation() {
		command = new Command("drop jar of eyes on table", null);
		assertEquals("drop/jar of eyes/table", command.getBreakdown());
	}
	
	@Test
	public void testVerbMultiwordNounMultiwordLocation() {
		command = new Command("drop jar of eyes on table top", null);
		assertEquals("drop/jar of eyes/table top", command.getBreakdown());
	}
	
	@Test
	public void testVerbMultiwordNounMultiwordLocationWithArticles() {
		command = new Command("drop the jar of eyes on a floor board", null);
		assertEquals("drop/jar of eyes/floor board", command.getBreakdown());
	}
}
