package junit;

import org.junit.Test;

import game.Game;
import items.Item;
import map.RoomObject;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;

public class TestCommandLogic {
	private Game game;
	
	@Before
	public void setUp() {
		game = new Game(1);
		game.reloadDatabaseForJUnit();
	}

	@Test
	public void testExamineLook() {
		game = new Game(1);
		game.movePlayerJUnit(1);
		
		game.runCommand("look");
		String expectedOutput = game.getRoom(1).getDescription().trim();
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		System.out.println("EXAMINE TEST");
	}
	
	@Test
	public void testOpen() {
		game = new Game(1);
		game.movePlayerJUnit(1);
		
		game.runCommand("open inventory");
		String expectedOutput = "Your inventory is empty!";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("grab key");
		game.runCommand("open inventory");
		expectedOutput = "You have key (0.1kgs)";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testListRoom() {
		game = new Game(1);
		game.movePlayerJUnit(1);
		
		game.runCommand("list table");
		String expectedOutput = "This does not contain any items.";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("list room");
		expectedOutput = "This room has a key";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testListObject() {
		game = new Game(1);
		game.movePlayerJUnit(7);
		
		game.runCommand("list table");
		String expectedOutput = "This object has a lamb heart, note, butcher knife";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("list room");
		expectedOutput = "This room does not contain any items.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testGrabTake() {
		game = new Game(1);
		game.movePlayerJUnit(1);
		
		game.runCommand("grab key");
		String expectedOutput = "You picked up key.";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("drop key on table");

		game.runCommand("take key from table");
		expectedOutput = "You picked up key.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("grab trash");
		expectedOutput = "This item does not exist in your current room.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("grab trash from table");
		expectedOutput = "This object does not have that item.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testDropPlace() {
		game = new Game(1);
		game.movePlayerJUnit(1);

		game.runCommand("grab key");
		
		game.runCommand("drop key");
		String expectedOutput = "You dropped key on the floor.";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("grab key");
		
		game.runCommand("drop key on trash");
		expectedOutput = "Could not find that object.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("place YEYEYE");
		expectedOutput = "You do not possess this item.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testMoveWalk() {
		game = new Game(1);
		game.movePlayerJUnit(1);
		
		game.runCommand("grab key");
		
		game.runCommand("move EEE");
		String expectedOutput = "There is not an exit here.";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("move east");
		expectedOutput = "There is not an exit here.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("move west");
		expectedOutput = "This door appears to be locked... perhaps there is something in the room that can help you.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);

		game.runCommand("unlock door");
		game.runCommand("move west");
		
		assertEquals(2, game.getPlayer().getRoomID());
		
		game = new Game(1);
		game.movePlayerJUnit(4);
		
		game.runCommand("move south");
		expectedOutput = "A dresser is blocking your path!";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testUnlock() {
		game = new Game(1);
		game.movePlayerJUnit(1);
		
		game.runCommand("unlock door");
		String expectedOutput = "You do not have the required item to unlock this door.";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);

		game.runCommand("grab key");
		game.runCommand("unlock door");
		expectedOutput = "You successfully unlocked the door.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("unlock trash");
		expectedOutput = "This obstacle does not exist...";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testTypeSolve() {
		game = new Game(1);
		game.movePlayerJUnit(3);
		
		game.runCommand("type LEEDLE");
		String expectedOutput = "leedle is not correct.";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("solve 1016");
		expectedOutput = "A door to the south swings open!";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testRead() {
		game = new Game(1);
		game.movePlayerJUnit(3);
		
		Item key = game.getRoom(2).getItem("small key");
		game.getPlayer().getInventory().addItem("small key", key);
		
		Item apple = game.getRoom(2).getItem("apple");
		game.getPlayer().getInventory().addItem("apple", apple);
		
		
		game.runCommand("read apple");
		String expectedOutput = "Cannot read this item.";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("read trash");
		expectedOutput = "Could not find a trash.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("unlock chest");
		game.runCommand("grab note from chest");
		
		game.runCommand("read note");
		expectedOutput = "This note says \"(8/2(2+2)) + 1000\"";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testPush() {
		game = new Game(1);
		game.movePlayerJUnit(4);
		
		game.runCommand("push yeet");
		String expectedOutput = "Cannot find yeet to move.";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("push desk");
		expectedOutput = "Moved desk out of the way.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("push dresser to the east");
		expectedOutput = "Pushed dresser east";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testPlay() {
		game = new Game(1);
		game.movePlayerJUnit(5);
		
		game.runCommand("play A on piano");
		String expectedOutput = "You played a on the piano.";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);

		game.runCommand("play h on piano");
		expectedOutput = "You entered an invalid note.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);

		game = new Game(1);
		game.movePlayerJUnit(6);
		
		game.runCommand("play h on record player");
		expectedOutput = "You do not have that item!";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);

		game.runCommand("grab hey you record from table");
		
		game.runCommand("play hey you record on record player");
		expectedOutput = "Played hey you record on the record player.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
	}
	
	@Test
	public void testCut() {
		game = new Game(1);
		game.movePlayerJUnit(7);
		
		game.runCommand("cut lamb heart on table");
		String expectedOutput = "You do not possess the needed item to cut this.";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("cut note on table");
		expectedOutput = "Cannot cut this item.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("grab butcher knife from table");

		game.runCommand("cut lamb heart on table");
		expectedOutput = "You break apart the lamb heart and dump the contents on the table.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testPour() {
		game = new Game(1);
		game.movePlayerJUnit(8);
		
		Item apple = game.getRoom(2).getItem("apple");
		game.getPlayer().getInventory().addItem("apple", apple);
		
		game.runCommand("pour apple on pentagram");
		String expectedOutput = "You cannot pour a apple.";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("grab blood vial from table");
		
		game.runCommand("pour blood vial on pentagram");
		expectedOutput = "You poured the blood vial on the pentagram.";
		commandOutput = game.getOutput();
		
		assertTrue(commandOutput.contains(expectedOutput));
		
		RoomObject table6 = new RoomObject("Table", "A table that can hold things!", "south", true, true, false, 8);
		game.getRoom().addObject("table", table6);
		
		game.runCommand("pour blood vial on table");
		expectedOutput = "Cannot pour blood vial on table.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testHint() {
		game = new Game(1);
		game.movePlayerJUnit(1);
		
		game.runCommand("hint");
		String expectedOutput = game.getRoom(1).getPuzzle().getHint();
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
	}
	
	@Test
	public void testFeed() {
		game = new Game(1);
		game.movePlayerJUnit(9);
		
		game.runCommand("feed hellhound");
		
		String expectedOutput = "I am not sure what to feed that.";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);

		Item apple = game.getRoom(2).getItem("apple");
		game.getPlayer().getInventory().addItem("apple", apple);
		
		game.runCommand("feed apple to hellhound");
		expectedOutput = "The hellhound does not want to be fed apple.";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.getPlayer().getInventory().addItem("meat", game.getDatabase().getItemByID(16));
		
		game.runCommand("feed meat to hellhound");
		
		expectedOutput = "You fed meat to the hellhound.";
		commandOutput = game.getOutput();
		
		assertTrue(commandOutput.contains(expectedOutput));
	}
	
	@Test
	public void testClimb() {
		game = new Game(1);
		game.movePlayerJUnit(16);
		
		game.runCommand("climb ladder");
		
		String expectedOutput = "You do not have what is needed to climb this ladder!";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		RoomObject table = new RoomObject("Table", "A table that can hold things!", "south", true, true, false, 8);
		game.getRoom().addObject("table", table);
		
		game.runCommand("climb table");
		expectedOutput = "That can't be climbed!";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);
		
		game.runCommand("climb potato");
		expectedOutput = "A potato doesn't exist in this room!";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);

		game.getPlayer().getInventory().addItem("sticky gloves", game.getDatabase().getItemByID(43));
		
		game.runCommand("climb ladder");
		expectedOutput = "You climbed the ladder.";
		commandOutput = game.getOutput();
		
		assertTrue(commandOutput.contains(expectedOutput));
	}
	
	@Test
	public void testScan() {
		game = new Game(1);
		game.movePlayerJUnit(12);
		
		game.runCommand("scan apple");
		
		String expectedOutput = "Not sure where you want me to scan...";
		String commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);

		Item apple = game.getRoom(2).getItem("apple");
		game.getPlayer().getInventory().addItem("apple", apple);
		
		game.runCommand("scan apple on scanner");
		
		expectedOutput = "The scanner can't scan that!";
		commandOutput = game.getOutput();
		
		assertEquals(expectedOutput, commandOutput);

		game.getPlayer().getInventory().addItem("severed finger", game.getDatabase().getItemByID(30));
		
		game.runCommand("scan severed finger on scanner");
		expectedOutput = "You scanned severed finger on the scanner.";
		commandOutput = game.getOutput();
		
		assertTrue(commandOutput.contains(expectedOutput));
	}
	
	@AfterClass
	public static void testFinish() {
		Game newGame = new Game(1);
		newGame.reloadDatabaseForJUnit();
	}
}