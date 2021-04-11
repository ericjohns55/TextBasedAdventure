package junit;

import org.junit.Test;

import game.Game;
import items.Item;
import map.RoomObject;

import static org.junit.Assert.*;

public class TestCommandLogic {
	private Game game;

	@Test
	public void testExamineLook() {
		game = new Game(1);
		
		String expectedOutput = game.getRoom(1).getDescription().trim();
		String commandOutput = game.runCommand("look");
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testOpen() {
		game = new Game(1);
		
		String expectedOutput = "Your inventory is empty!";
		String commandOutput = game.runCommand("open inventory");
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("grab key");
		expectedOutput = "You have key (0.1kgs)";
		commandOutput = game.runCommand("open inventory");
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testList() {
		game = new Game(1);
	}
	
	@Test
	public void testGrabTake() {
		game = new Game(1);
		
		String expectedOutput = "You picked up key.";
		String commandOutput = game.runCommand("grab key");
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("drop key on table");
		
		expectedOutput = "You picked up key.";
		commandOutput = game.runCommand("take key from table");
		
		assertEquals(expectedOutput, commandOutput);
		
		expectedOutput = "This item does not exist in your current room.";
		commandOutput = game.runCommand("grab trash");
		
		assertEquals(expectedOutput, commandOutput);
		
		expectedOutput = "This object does not have that item.";
		commandOutput = game.runCommand("grab trash from table");
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testDropPlace() {
		game = new Game(1);

		game.runCommand("grab key");
		
		String expectedOutput = "You dropped key on the floor.";
		String commandOutput = game.runCommand("drop key");
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("grab key");
		
		expectedOutput = "Could not find that object.";
		commandOutput = game.runCommand("drop key on trash");
		
		assertEquals(expectedOutput, commandOutput);
		
		expectedOutput = "You do not possess this item.";
		commandOutput = game.runCommand("place YEYEYE");
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testMoveWalk() {
		game = new Game(1);
		game.runCommand("grab key");
		
		String expectedOutput = "There is not an exit here.";
		String commandOutput = game.runCommand("move EEE");
		
		assertEquals(expectedOutput, commandOutput);
		
		expectedOutput = "This door appears to be locked... perhaps there is something in the room that can help you.";
		commandOutput = game.runCommand("move west");
		
		assertEquals(expectedOutput, commandOutput);

		game.runCommand("unlock door");
		game.runCommand("move west");
		
		assertEquals(2, game.getPlayer().getRoomID());
		
		game = new Game(4);
		
		expectedOutput = "A dresser is blocking your path!";
		commandOutput = game.runCommand("move south");
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testUnlock() {
		game = new Game(1);
		
		String expectedOutput = "You do not have the required item to unlock this door.";
		String commandOutput = game.runCommand("unlock door");
		
		assertEquals(expectedOutput, commandOutput);

		game.runCommand("grab key");
		
		expectedOutput = "You successfully unlocked the door.";
		commandOutput = game.runCommand("unlock door");
		
		assertEquals(expectedOutput, commandOutput);
		
		expectedOutput = "This obstacle does not exist...";
		commandOutput = game.runCommand("unlock trash");
		
		assertEquals(expectedOutput, commandOutput);
		
	}
	
	@Test
	public void testTypeSolve() {
		game = new Game(3);
		
		String expectedOutput = "leedle is not correct.";
		String commandOutput = game.runCommand("type LEEDLE");
		
		assertEquals(expectedOutput, commandOutput);
		
		expectedOutput = "A door to the south swings open!";
		commandOutput = game.runCommand("solve 1016");
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testRead() {
		game = new Game(3);
		
		Item key = game.getRoom(2).getItem("small key");
		game.getPlayer().getInventory().addItem("small key", key);
		
		Item apple = game.getRoom(2).getItem("apple");
		game.getPlayer().getInventory().addItem("apple", apple);
		
		
		String expectedOutput = "Cannot read this item.";
		String commandOutput = game.runCommand("read apple");
		
		assertEquals(expectedOutput, commandOutput);
		
		expectedOutput = "Could not find a trash.";
		commandOutput = game.runCommand("read trash");
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("unlock chest");
		game.runCommand("grab note from chest");
		
		expectedOutput = "This note says \"(8/2(2+2)) + 1000\"";
		commandOutput = game.runCommand("read note");
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testPush() {
		game = new Game(4);
		
		String expectedOutput = "Cannot find yeet to move.";
		String commandOutput = game.runCommand("push yeet");
		
		assertEquals(expectedOutput, commandOutput);
		
		expectedOutput = "Moved desk out of the way.";
		commandOutput = game.runCommand("push desk");
		
		assertEquals(expectedOutput, commandOutput);
		
		expectedOutput = "Pushed dresser east";
		commandOutput = game.runCommand("push dresser to the east");
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testPlay() {
		game = new Game(5);
		
		String expectedOutput = "You played a on the piano.";
		String commandOutput = game.runCommand("play A on piano");
		
		assertEquals(expectedOutput, commandOutput);

		expectedOutput = "You entered an invalid note.";
		commandOutput = game.runCommand("play h on piano");
		
		assertEquals(expectedOutput, commandOutput);

		game = new Game(6);
		
		expectedOutput = "You do not have that item!";
		commandOutput = game.runCommand("play h on record player");
		
		assertEquals(expectedOutput, commandOutput);

		game.runCommand("grab hey you record from table");
		
		expectedOutput = "Played hey you record on the record player.";
		commandOutput = game.runCommand("play hey you record on record player");
		
		assertEquals(expectedOutput, commandOutput);
		
	}
	
	@Test
	public void testCut() {
		game = new Game(7);
		
		String expectedOutput = "You do not possess the needed item to cut this.";
		String commandOutput = game.runCommand("cut lamb heart on table");
		
		assertEquals(expectedOutput, commandOutput);
		
		expectedOutput = "Cannot cut this item.";
		commandOutput = game.runCommand("cut note on table");
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("grab butcher knife from table");

		expectedOutput = "You break apart the lamb heart and dump the contents on the table.";
		commandOutput = game.runCommand("cut lamb heart on table");
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testPour() {
		game = new Game(8);
		
		Item apple = game.getRoom(2).getItem("apple");
		game.getPlayer().getInventory().addItem("apple", apple);
		
		String expectedOutput = "You cannot pour a apple.";
		String commandOutput = game.runCommand("pour apple on pentagram");
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("grab blood vial");
		
		expectedOutput = "You poured the blood vial on the pentagram.";
		commandOutput = game.runCommand("pour blood vial on pentagram");
		
		assertTrue(commandOutput.contains(expectedOutput));
		
		RoomObject table6 = new RoomObject("Table", "A table that can hold things!", "south", true, true, false, 8);
		game.getRoom(8).addObject("table", table6);
		
		expectedOutput = "Cannot pour blood vial on table.";
		commandOutput = game.runCommand("pour blood vial on table");
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testHint() {
		game = new Game(1);
		
		String expectedOutput = game.getRoom(1).getPuzzle().getHint();
		String commandOutput = game.runCommand("hint");
		
		assertEquals(expectedOutput, commandOutput);
	}
}